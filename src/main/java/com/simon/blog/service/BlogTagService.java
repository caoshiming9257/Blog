package com.simon.blog.service;

import com.simon.blog.pojo.Blog;
import com.simon.blog.pojo.BlogTagRelation;
import com.simon.blog.pojo.CountName;

import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/16 13:21
 */
public interface BlogTagService {

    /**
     * 将博客和标签对应的id保存至中间表
     * **/
    void saveBlogTag(Blog blog);

    /**
     * 根据博客id查询删除所有的数据
     * **/
    void deleteAllByBlogId(Long id);

    /**
     * 根据博客id查询所有的数据
     * **/
    List<BlogTagRelation> findAllByBlogId(Long id);

    /**
     * 获取标签出现的次数和对应的名称
     * **/
    List<CountName> findCountTag();
}
