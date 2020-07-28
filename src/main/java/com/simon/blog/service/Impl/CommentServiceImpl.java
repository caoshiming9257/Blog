package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.simon.blog.dao.CommentMapper;
import com.simon.blog.pojo.Comment;
import com.simon.blog.service.CommentService;
import com.simon.blog.util.CommentLevelUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    /**
     * 根据blogId获取评论信息
     * **/
    @Override
    public List<Comment> getListCommentByBlogId(Long blogId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("blog_id",blogId);
        queryWrapper.isNull("parentComment_id");
        List<Comment> list = commentMapper.selectList(queryWrapper);
        return  CommentLevelUtil.eachComment(list);
    }

    /**
     * 根据parentComment_id获取评论信息
     * **/
    @Override
    public List<Comment> getCommentByparentCommentId(Long parentCommentId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parentComment_id",parentCommentId);
        return commentMapper.selectList(queryWrapper);
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
