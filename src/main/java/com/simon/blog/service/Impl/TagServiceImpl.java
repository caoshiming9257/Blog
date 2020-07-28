package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.Handler.NotFoundException;
import com.simon.blog.dao.TagMapper;
import com.simon.blog.dao.TypeMapper;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Tag;
import com.simon.blog.pojo.Type;
import com.simon.blog.service.TagService;
import com.simon.blog.service.TypeService;
import com.simon.blog.util.StringListUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:51
 * @Decription：标签
 */
@Service
public class TagServiceImpl implements TagService {

    @Resource
    TagMapper tagMapper;

    /**插入标签*/
    @Transactional
    @Override
    public Integer saveTag(Tag tag) {
        Integer insert = tagMapper.insert(tag);

        return insert;
    }

    /**根据名称查找标签*/
    @Override
    public Tag findByName(String name) {
        return tagMapper.findByName(name);
    }

    /**根据id查找标签*/
    @Override
    public Tag getTag(Long id) {
        return tagMapper.selectById(id);
    }

    /**根据标签ID集合获取所有的标签*/
    @Override
    public List<Tag> listTag(String tags) {
        return tagMapper.selectBatchIds(StringListUtil.convertToList(tags));
    }

    /**获取所有的标签*/
    @Override
    public List<Tag> listAllTag() {
        return tagMapper.selectList(null);
    }

    /**分页查询标签*/
    @Transactional
    @Override
    public Page<Tag> pageTag(Page<Tag> tagPage) {
        Page<Tag> types = tagMapper.selectPage(tagPage, null);
        return types;
    }

    /**根据id更新标签*/
    @Transactional
    @Override
    public Integer updateTag(Long id, Tag tag) {
        Tag ta = tagMapper.selectById(id);
        if (ta == null){
            throw new NotFoundException("不存在该类型");
        }
        /**更新条件，根据where id来进行更新*/
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",id);
        return tagMapper.update(tag,updateWrapper);
    }

    /**删除标签*/
    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagMapper.deleteById(id);
    }

    /**联合查询所有的tag和博客中使用的tag次数**/
    @Override
    public List<CountName> tagAndCount() {
        return tagMapper.tagAndCount();
    }

    /*根据博客id 获取对应的所有标签*/
    @Override
    public List<Tag> findTagByBlogId(Long id) {
        return tagMapper.findTagByBlogId(id);
    }
}
