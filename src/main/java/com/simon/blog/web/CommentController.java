package com.simon.blog.web;

import com.simon.blog.pojo.Comment;
import com.simon.blog.pojo.User;
import com.simon.blog.service.BlogService;
import com.simon.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/21 15:23
 */
@Controller
public class CommentController {

    @Resource
    CommentService commentService;

    @Resource
    BlogService blogService;

    @Value("${comment.avatar}")
    private String photo;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId , Model model){
        model.addAttribute("comments",commentService.getListCommentByBlogId(blogId));
        return "blog :: commentList" ;
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog_id();
        User user = (User)session.getAttribute("user");
        if (user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar(photo);
        }
        comment.setBlog(blogService.getBlog(blogId));
        comment.setBlog_id(blogId);
        commentService.saveComment(comment);
        return "redirect:/comments/"+blogId;
    }
}
