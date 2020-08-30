package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simon.blog.dao.CommentMapper;
import com.simon.blog.pojo.Comment;
import com.simon.blog.service.CommentService;
import com.simon.blog.util.CommentLevelUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/21 15:30
 * @Description: ：留言
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    CommentMapper commentMapper;

    @Resource
    RedisTemplate<Object,Object> redisTemplate;

    /**key的序列化*/
    private void keySerializer(){
        RedisSerializer redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
    }

    /**
     * 根据blogId获取评论信息
     * **/
    @Override
    public List<Comment> getListCommentByBlogId(Long blogId) {
        keySerializer();
        List<Comment> commentList = (List<Comment>)redisTemplate.opsForValue().get("commentList");
        if (commentList == null){
            synchronized (this){
                commentList = (List<Comment>)redisTemplate.opsForValue().get("commentList");
                if (commentList == null){
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("blog_id",blogId);
                    queryWrapper.isNull("parentComment_id");
                    commentList = commentMapper.selectList(queryWrapper);
                    commentList = CommentLevelUtil.eachComment(commentList);
                    redisTemplate.opsForValue().set("commentList",commentList,60, TimeUnit.SECONDS);
                }
            }
        }
        return  commentList;
    }

    /**
     * 根据parentComment_id获取评论信息
     * **/
    @Override
    public List<Comment> getCommentByparentCommentId(Long parentCommentId) {
        keySerializer();
        List<Comment> idByParentComment = (List<Comment>)redisTemplate.opsForValue().get("idByParentComment");
        if (idByParentComment == null){
            synchronized (this){
                idByParentComment = (List<Comment>)redisTemplate.opsForValue().get("idByParentComment");
                if (idByParentComment == null){
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("parentComment_id",parentCommentId);
                    idByParentComment = commentMapper.selectList(queryWrapper);
                    redisTemplate.opsForValue().set("idByParentComment",idByParentComment,60, TimeUnit.SECONDS);
                }
            }
        }
        return idByParentComment;
    }

    /**
     * 保存留言信息
     * **/
    @Transactional
    @Override
    public Integer saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment_id();
        if (parentCommentId != -1){
            comment.setParentComment(commentMapper.selectById(parentCommentId));
            comment.setParentComment_id(parentCommentId);
        }else {
            comment.setParentComment_id(null);
        }
        comment.setCreateTime(new Date());
        return commentMapper.insert(comment);
    }

}
