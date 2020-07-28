package com.simon.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.Blog;
import com.simon.blog.pojo.BlogSearch;
import com.simon.blog.pojo.BlogTagRelation;
import com.simon.blog.pojo.CountName;

import java.util.List;
import java.util.Map;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:46
 * @Decription：后台博客新增
 */
public interface BlogService {

    Integer saveBlog(Blog blog);

    Blog getBlog(Long id);

    Blog getContenToHtml(Long id);

    Long findLastID();

    Page<Blog> pageBlog(Page<Blog> blogPage);

    Page<Blog> pageSearchBlog(Page<Blog> blogPage, BlogSearch blogSearch);

    Page<Blog> pageSearchValue(Page<Blog> blogPage, String searchValue);

    Page<Blog> pageByTagId(Page<Blog> blogPage, Long tagId);

    Integer updateBlog(Blog blog);

    void deleteBlog(Long id);

    List<CountName> findCountTypeName();

    List<Blog> findCountRecommendName();

    List<Blog> findBlogByTypeId(Long id);

    List<Blog> findBlogByTagId(Long id);

    Map<String,List<Blog>> getBlogByUpdateYear ();

    Integer getAllBlogCount();

}
