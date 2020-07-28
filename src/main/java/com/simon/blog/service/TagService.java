package com.simon.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Tag;
import com.simon.blog.pojo.Type;

import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:46
 * @Decription：后台分类新增
 */
public interface TagService {

    Integer saveTag(Tag tag);

    Tag findByName(String name);

    Tag getTag(Long id);

    List<Tag> listTag(String tags);

    List<Tag> listAllTag();

    Page<Tag> pageTag(Page<Tag> tagPage);

    Integer updateTag(Long id, Tag tag);

    void deleteTag(Long id);

    List<CountName> tagAndCount();

    List<Tag> findTagByBlogId(Long id);
}
