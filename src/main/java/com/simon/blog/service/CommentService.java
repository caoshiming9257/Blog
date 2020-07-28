package com.simon.blog.service;

import com.simon.blog.pojo.Comment;

import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/21 15:27
 * @Description: 留言
 */
public interface CommentService {

    List<Comment> getListCommentByBlogId(Long blogId);

    List<Comment> getCommentByparentCommentId(Long replyCommentId);

    Integer saveComment(Comment comment);
}
