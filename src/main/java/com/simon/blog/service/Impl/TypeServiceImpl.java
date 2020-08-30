package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.Handler.NotFoundException;
import com.simon.blog.dao.TypeMapper;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Type;
import com.simon.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:51
 * @Decription：分类
 */
@Service
public class TypeServiceImpl implements TypeService {

    @Resource
    TypeMapper typeMapper;

    @Resource
    RedisTemplate<Object,Object> redisTemplate;

    /**key的序列化*/
    private void keySerializer(){
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
    }


    /**插入分类*/
    @Override
    public Integer saveTypa(Type type) {
        Integer insert = typeMapper.insert(type);

        return insert;
    }

    /**根据名称查找分类*/
    @Override
    public Type findByName(String name) {
        return typeMapper.findByName(name);
    }

    /**根据id查找分类*/
    @Override
    public Type getType(Long id) {
        return typeMapper.selectById(id);
    }

    /**获取所有的分类*/
    @Override
    public List<Type> listAllType() {
        keySerializer();
        List<Type> allType = (List<Type>)redisTemplate.opsForValue().get("allType");
        if (allType == null){
            synchronized (this){
                allType = (List<Type>)redisTemplate.opsForValue().get("allType");
                if (allType == null){
                    allType = typeMapper.selectList(null);
                    redisTemplate.opsForValue().set("allType",allType,1000, TimeUnit.SECONDS);
                }
            }
        }
        return allType;
    }

    /**分页查询分类*/
    @Transactional
    @Override
    public Page<Type> pageType(Page<Type> typePage) {
        keySerializer();
        Page<Type> pageType = (Page<Type>)redisTemplate.opsForValue().get("pageType");
        if (pageType == null){
            synchronized (this){
                pageType = (Page<Type>)redisTemplate.opsForValue().get("pageType");
                if (pageType == null){
                    pageType = typeMapper.selectPage(typePage, null);
                    redisTemplate.opsForValue().set("pageType",pageType,1000, TimeUnit.SECONDS);
                }
            }
        }
        return pageType;
    }

    /**根据id更新分类*/
    @Transactional
    @Override
    public Integer updateType(Long id, Type type) {
        Type ty = typeMapper.selectById(id);
        if (ty == null){
            throw new NotFoundException("不存在该类型");
        }
        /**更新条件，根据where id来进行更新*/
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",id);
        return typeMapper.update(type,updateWrapper);
    }

    /**删除分类*/
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeMapper.deleteById(id);
    }

    /**联合查询所有的type和博客中使用的type次数**/
    @Override
    public List<CountName> typeAndCount() {
        return typeMapper.typeAndCount();
    }
}
