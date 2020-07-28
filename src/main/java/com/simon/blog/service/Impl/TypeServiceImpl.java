package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.Handler.NotFoundException;
import com.simon.blog.dao.TypeMapper;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Type;
import com.simon.blog.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;

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
        return typeMapper.selectList(null);
    }

    /**分页查询分类*/
    @Transactional
    @Override
    public Page<Type> pageType(Page<Type> typePage) {
        Page<Type> types = typeMapper.selectPage(typePage, null);
        return types;
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
