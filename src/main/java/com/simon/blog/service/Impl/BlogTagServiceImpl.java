package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simon.blog.dao.BlogTagMapper;
import com.simon.blog.dao.TagMapper;
import com.simon.blog.pojo.*;
import com.simon.blog.service.BlogService;
import com.simon.blog.service.BlogTagService;
import com.simon.blog.util.CommentLevelUtil;
import com.simon.blog.util.StringListUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/16 13:24
 * @Description：中间表插入数据
 */
@Service
public class BlogTagServiceImpl implements BlogTagService {

    @Resource
    BlogTagMapper blogTagMapper;

    @Resource
    BlogService blogService;

    @Resource
    TagMapper tagMapper;

    @Resource
    RedisTemplate<Object,Object> redisTemplate;

    /**key的序列化*/
    private void keySerializer(){
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
    }

    /**将blog对应的id和对应的Tag的id，插入到中间表**/
    @Transactional
    @Override
    public void saveBlogTag(Blog blog) {
        BlogTagRelation blogTagRelation = null;
        long blogID = 0;
        String tagIds = blog.getTagIds();
        if (blog.getId() == null){
            blogID =  blogService.findLastID();
        }else{
            blogID = blog.getId();
        }

        List<Long> lists = StringListUtil.convertToList(tagIds);
        for (Long tagId : lists){
            blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blogID);
            blogTagRelation.setTagId(tagId);
            blogTagMapper.insert(blogTagRelation);
        }
    }

    /**
     * 根据博客id删除所有的数据
     * @param id
     **/
    @Transactional
    @Override
    public void deleteAllByBlogId(Long id) {
        blogTagMapper.deleteAllByBlogId(id);
    }

    /**
     * 根据博客id查询所有的数据
     *
     * @param id
     **/
    @Override
    public List<BlogTagRelation> findAllByBlogId(Long id) {
        List<BlogTagRelation> blogIdByAll = (List<BlogTagRelation>)redisTemplate.opsForValue().get("blogIdByAll");
        if (blogIdByAll == null){
            synchronized (this){
                blogIdByAll = (List<BlogTagRelation>)redisTemplate.opsForValue().get("blogIdByAll");
                if (blogIdByAll == null){
                    QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
                    wrapper.eq("t_blog_id",id);
                    blogIdByAll = blogTagMapper.selectList(wrapper);
                    redisTemplate.opsForValue().set("blogIdByAll",blogIdByAll,30, TimeUnit.SECONDS);
                }
            }
        }
        return blogIdByAll;
    }

    /**通过博客对使用的标签进行排序，并根据获取的标签id获取对应的name**/
    @Override
    public List<CountName> findCountTag(){
        List<CountName> countTag = blogTagMapper.findCountTag();
        List<CountName> lists = new ArrayList<>();
        for (CountName countName : countTag){
            Tag tag = tagMapper.selectById(countName.getTagId());
            countName.setName(tag.getName());
            lists.add(countName);
        }
        return lists;
    }
}
