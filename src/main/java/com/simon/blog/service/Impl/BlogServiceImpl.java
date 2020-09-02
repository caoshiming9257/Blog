package com.simon.blog.service.Impl;

import com.alibaba.druid.sql.visitor.functions.If;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Resource
    RedisTemplate<Object,Object> redisTemplate;

    /**key的序列化*/
    private void keySerializer(){
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
    }

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
        keySerializer();
        List<CountName> countTypeName = (List<CountName>)redisTemplate.opsForValue().get("countTypeName");
        if (countTypeName == null){
            synchronized (this){
                countTypeName = (List<CountName>)redisTemplate.opsForValue().get("countTypeName");
                if (countTypeName == null){
                    countTypeName = blogMapper.findCountTypeName();
                    redisTemplate.opsForValue().set("countTypeName",countTypeName,600, TimeUnit.SECONDS);
                }
            }
        }
        return blogMapper.findCountTypeName();
    }

    /**根据推荐的次数排序,获取博客和次数**/
    @Override
    public List<Blog> findCountRecommendName() {
        keySerializer();
        List<Blog> countRecommendName = (List<Blog>)redisTemplate.opsForValue().get("countRecommendName");
        if (countRecommendName == null){
            synchronized (this){
                countRecommendName = (List<Blog>)redisTemplate.opsForValue().get("countRecommendName");
                if (countRecommendName == null){
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("recommend",1);
                    countRecommendName = blogMapper.selectList(queryWrapper);
                    redisTemplate.opsForValue().set("countRecommendName",countRecommendName,600, TimeUnit.SECONDS);
                }
            }
        }
        return countRecommendName;
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
    public LinkedHashMap<String,List<Blog>> getBlogByUpdateYear() {
        keySerializer();
        LinkedHashMap<String,List<Blog>> blogByUpdateYear = (LinkedHashMap<String,List<Blog>>)redisTemplate.opsForValue().get("blogByUpdateYear");
        if (blogByUpdateYear == null){
            synchronized (this){
                blogByUpdateYear = (LinkedHashMap<String,List<Blog>>)redisTemplate.opsForValue().get("blogByUpdateYear");
                if (blogByUpdateYear == null){
                    List<String> blogUpdateYear = blogMapper.getBlogByUpdateYear();
                    QueryWrapper queryWrapper = new QueryWrapper();
                    for (String year : blogUpdateYear){
                        queryWrapper.like("updateTime",year);
                        List<Blog> list = blogMapper.selectList(queryWrapper);
                        blogByUpdateYear.put(year,list);
                        queryWrapper.clear();
                    }
                    redisTemplate.opsForValue().set("blogByUpdateYear",blogByUpdateYear,600,TimeUnit.SECONDS);
                }
            }
        }
//        List<String> blogUpdateYear = blogMapper.getBlogByUpdateYear();
//        LinkedHashMap<String,List<Blog>> map = new LinkedHashMap<>();
//        QueryWrapper queryWrapper = new QueryWrapper();
//        for (String year : blogUpdateYear){
//            queryWrapper.like("updateTime",year);
//            List<Blog> list = blogMapper.selectList(queryWrapper);
//            map.put(year,list);
//            queryWrapper.clear();
//        }
        return blogByUpdateYear;
    }

    /**获取博客总数**/
    @Override
    public Integer getAllBlogCount() {
        keySerializer();
        Integer allBlogCount = (Integer) redisTemplate.opsForValue().get("allBlogCount");
        if (allBlogCount == 0){
            synchronized (this){
                allBlogCount = (Integer) redisTemplate.opsForValue().get("allBlogCount");
                if (allBlogCount == 0){
                    allBlogCount = blogMapper.selectCount(null);
                    redisTemplate.opsForValue().set("allBlogCount",allBlogCount,600,TimeUnit.SECONDS);
                }
            }
        }
        return allBlogCount;
    }
}
