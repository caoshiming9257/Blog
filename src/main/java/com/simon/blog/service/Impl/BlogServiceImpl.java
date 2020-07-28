package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.Handler.NotFoundException;
import com.simon.blog.dao.BlogMapper;
import com.simon.blog.dao.BlogTagMapper;
import com.simon.blog.dao.TypeMapper;
import com.simon.blog.pojo.*;
import com.simon.blog.service.BlogService;
import com.simon.blog.util.MarkDownUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/14 14:49
 * @Description: 博客
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Resource
    BlogMapper blogMapper;

    @Resource
    TypeMapper typeMapper;

    @Resource
    BlogTagMapper blogTagMapper;

    /**插入博客*/
    @Transactional
    @Override
    public Integer saveBlog(Blog blog) {
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(0);
        return blogMapper.insert(blog);
    }

    /**根据id获取博客**/
    @Override
    public Blog getBlog(Long id) {
        return blogMapper.selectById(id);
    }

    /**根据id获取博客，将内容从markdown转换为html**/
    @Transactional
    @Override
    public Blog getContenToHtml(Long id) {
        Blog blog = blogMapper.selectById(id);
        if (blog == null){
            throw  new NotFoundException("查找的博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        b.setContent(MarkDownUtil.markdownToHtmlExtensions(b.getContent()));
        blogMapper.updateView(id);
        return b;
    }

    /**获取最新的数据ID**/
    @Override
    public Long findLastID() {
        return blogMapper.findLastID();
    }

    /**根据搜索条件进行分页*/
    @Override
    public Page<Blog> pageSearchBlog(Page<Blog> blogPage, BlogSearch blogSearch) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<Blog>();
        if(blogSearch.getTitle() != null && !"".equals(blogSearch.getTitle())) wrapper.like("title",blogSearch.getTitle());
        if(blogSearch.getTypeId() != null ) wrapper.eq("type_name",(typeMapper.selectById(blogSearch.getTypeId())).getName());
        if(blogSearch.getStatus() != null && !"".equals(blogSearch.getStatus())) wrapper.eq("published",("发布".equals(blogSearch.getStatus()) ? true : false));
        if (blogSearch.getRecommend() != null && blogSearch.getRecommend()) wrapper.eq("recommend",blogSearch.getRecommend());
        return blogMapper.selectPage(blogPage, wrapper);
    }

    /**前台搜索框根据标题和内容模糊查询所有博客并分页**/
    @Override
    public Page<Blog> pageSearchValue(Page<Blog> blogPage, String searchValue) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.like("title",searchValue)
                .or()
                .like("content",searchValue);

        return blogMapper.selectPage(blogPage,wrapper);
    }

    /**根据标签id查询所有的博客**/
    @Override
    public Page<Blog> pageByTagId(Page<Blog> blogPage, Long tagId) {
        return blogPage.setRecords(blogMapper.pageByTagId(blogPage,tagId));
    }

    /**博客分页*/
    @Override
    public Page<Blog> pageBlog(Page<Blog> blogPage) {
        return blogMapper.selectPage(blogPage,null);
    }

    /**修改博客*/
    @Transactional
    @Override
    public Integer updateBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        blogTagMapper.deleteAllByBlogId(blog.getId());
        /**更新条件，根据where id来进行更新*/
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",blog.getId());
        return blogMapper.update(blog,updateWrapper);
    }


    /**删除博客**/
    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogTagMapper.deleteAllByBlogId(id);
        blogMapper.deleteById(id);
    }

    /**根据分类名称排序,获取名称和次数**/
    @Override
    public List<CountName> findCountTypeName() {
        return blogMapper.findCountTypeName();
    }

    /**根据推荐的次数排序,获取博客和次数**/
    @Override
    public List<Blog> findCountRecommendName() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("recommend",1);
        return blogMapper.selectList(queryWrapper);
    }

    /*根据type id查看是否有存在的博客*/
    @Override
    public List<Blog> findBlogByTypeId(Long id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("type_id",id);
        return blogMapper.selectList(queryWrapper);
    }

    /*根据tag id查看是否有存在的博客*/
    @Override
    public List<Blog> findBlogByTagId(Long id) {
        return blogMapper.findBlogByTagId(id);
    }

    /**获取博客修改日期中的年份**/
    @Override
    public Map<String,List<Blog>> getBlogByUpdateYear() {
        List<String> blogUpdateYear = blogMapper.getBlogByUpdateYear();
        LinkedHashMap<String,List<Blog>> map = new LinkedHashMap<>();
        QueryWrapper queryWrapper = new QueryWrapper();
        for (String year : blogUpdateYear){
            queryWrapper.like("updateTime",year);
            List<Blog> list = blogMapper.selectList(queryWrapper);
            map.put(year,list);
            queryWrapper.clear();
        }
        return map;
    }

    /**获取博客总数**/
    @Override
    public Integer getAllBlogCount() {
        return blogMapper.selectCount(null);
    }
}