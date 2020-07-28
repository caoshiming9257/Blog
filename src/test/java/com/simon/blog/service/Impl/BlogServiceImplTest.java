package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.dao.BlogMapper;
import com.simon.blog.dao.BlogTagMapper;
import com.simon.blog.dao.TypeMapper;
import com.simon.blog.pojo.Blog;
import com.simon.blog.pojo.BlogSearch;
import com.simon.blog.pojo.BlogTagRelation;
import com.simon.blog.pojo.CountName;
import com.simon.blog.service.BlogService;
import com.simon.blog.service.BlogTagService;
import com.simon.blog.util.StringListUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/16 14:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogServiceImplTest {
    @Resource
    BlogMapper blogMapper;

    @Resource
    TypeMapper typeMapper;

    @Resource
    BlogTagService blogTagService;

    @Resource
    BlogService blogService;

    @Test
    public void pageBlog() {
        Page<Blog> blogPage = new Page<>(1,3);
        Page<Blog> blogPage1 = blogMapper.selectPage(blogPage, null);
        System.out.println(blogPage1);
        System.out.println(blogPage1.getPages());
        System.out.println(blogPage1.getRecords());
        System.out.println(blogPage1.getCurrent());
        System.out.println(blogPage1.getSize());
        System.out.println(blogPage1.getTotal());
        System.out.println(blogPage1.getOrders());

    }

    @Test
    public void pageSearchBlog() {
        /*SELECT id,title,content,firstPicture,flag,views,appreciation
            ,shareStatement
            ,commentabled,published,recommend,createTime,updateTime
            ,type_id,user_id,type_name FROM t_blog LIMIT ?,? */

        //        Blog blog = new Blog();
        BlogSearch blogSearch = new BlogSearch();
        blogSearch.setTitle("分页");
        blogSearch.setTypeId(null);
        blogSearch.setRecommend(null);

        Page<Blog> blogPage = new Page<>(1,3);
        QueryWrapper<Blog> wrapper = new QueryWrapper<Blog>();
        if(blogSearch.getTitle() != null) wrapper.like("title",blogSearch.getTitle());
        if(blogSearch.getTypeId() != null) wrapper.eq("type_name",(typeMapper.selectById(2)).getName());
        if (blogSearch.getRecommend() != null) wrapper.eq("recommend",blogSearch.getRecommend());

        //        blog.setTitle(blogSearch.getTitle());
        //        blog.setTypeName((typeMapper.selectById(blogSearch.getTypeId())).getName());
        //        blog.setRecommend(blogSearch.getRecommend());
        Page<Blog> blogPage1 = blogMapper.selectPage(blogPage, wrapper);
        System.out.println(blogPage1.getRecords());
    }

    @Test
    public void tt(){
        List<BlogTagRelation> allByBlogId = blogTagService.findAllByBlogId(Long.parseLong("4"));
        String s = StringListUtil.listToString(allByBlogId);
        System.out.println(s);
    }

    @Test
    public void cou(){
        List<CountName> countName = blogMapper.findCountTypeName();
        System.out.println(countName);
    }

    @Test
    public void ttt(){
        Integer allBlogCount = blogService.getAllBlogCount();
        System.out.println(allBlogCount);
        List<CountName> list = typeMapper.typeAndCount();
        System.out.println(list);
    }


    @Test
    public void tag(){
        Page<Blog> page = new Page<>(0,5);
        Page<Blog> blogPage = page.setRecords(blogMapper.pageByTagId(page, 1L));
        System.out.println(blogPage.getRecords());
        System.out.println(blogPage.getPages());
        System.out.println(blogPage.getTotal());
        System.out.println(blogPage.getSize());
        System.out.println(blogPage.getCurrent());
    }


    @Test
    public void year(){
        Map<String, List<Blog>> blogByUpdateYear = blogService.getBlogByUpdateYear();
        Iterator<Map.Entry<String, List<Blog>>> iterator = blogByUpdateYear.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<Blog>> next = iterator.next();
            String key = next.getKey();
            List<Blog> value = next.getValue();
            System.out.println(key+"......."+value);
        }

    }
}