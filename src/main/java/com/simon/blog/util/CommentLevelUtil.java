package com.simon.blog.util;

import com.simon.blog.dao.CommentMapper;
import com.simon.blog.pojo.Comment;
import com.simon.blog.service.CommentService;
import jdk.nashorn.internal.objects.annotations.Constructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/22 13:46
 * @Description: 将留言的层级结构进行整理(两级结构)
 */
@Component
public class CommentLevelUtil {

    @Resource
    CommentService commentService;

    private static CommentLevelUtil commentLevelUtil;


    @PostConstruct
    public void init() {
        commentLevelUtil = this;
        commentLevelUtil.commentService = this.commentService;
    }

    /**存放所有子级及其更深层次的层级留言的集合**/
    private List<Comment> allChildReplys = new ArrayList<>();

    /** 
      @Description: 将评论的顶级评论进行迭代
      @Param: [commentList]
      @return: java.util.List<com.simon.blog.pojo.Comment>
      @Author: Simon_Cao
      @Date: 2020/7/22
     */ 
    public static List<Comment> eachComment(List<Comment> commentList){
        List<Comment> commentsView = new ArrayList<>();
        Comment c = null;
        for (Comment comment : commentList){
            c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        /**将子级留言都添加至对应的父级留言下**/
       new CommentLevelUtil().commentChildren(commentsView);
        return commentsView;
    }

    /**
      @Description: 迭代父级并将其所有子介及其更深子介设置为其ReplyComments的数据
      @Param: [commentsView]
      @return: void
      @Author: Simon_Cao
      @Date: 2020/7/22
     */
    public  void commentChildren(List<Comment> commentsView){
        for (Comment comment : commentsView){
            List<Comment> replyComments = commentLevelUtil.commentService.getCommentByparentCommentId(comment.getId());
            for (Comment reply : replyComments){
                reply.setParentComment(comment);
                /*递归找出所有的子介，存放在allChildReplys*/
                recursively(reply);
            }
            /*将父级的reply集合修改为处理后的集合*/
            comment.setReplyComments(allChildReplys);
            /*清空全局集合*/
            allChildReplys = new ArrayList<>();
        }
    }

    /**
      @Description: 递归，处理第二层级及其下面更深层级
      @Param: [comment]
      @return: void
      @Author: Simon_Cao
      @Date: 2020/7/22
     */
    public void recursively(Comment comment){
        /*将子级留言加入到全局集合中*/
//        comment.setParentComment(comment);
        if (!allChildReplys.contains(comment)){
            allChildReplys.add(comment);
        }
        if (commentLevelUtil.commentService.getCommentByparentCommentId(comment.getId()).size()>0){
            /*获取子级留言的数据*/
            List<Comment> replyComments = commentLevelUtil.commentService.getCommentByparentCommentId(comment.getId());
            for (Comment commentChildrens : replyComments){
                /*子级将直属父类加入到其属性对象中*/
                commentChildrens.setParentComment(comment);
                allChildReplys.add(commentChildrens);
                /*递归循环更深层级的留言对象加入到全局集合中*/
                if (commentLevelUtil.commentService.getCommentByparentCommentId(commentChildrens.getId()).size() > 0){
                    recursively(commentChildrens);
                }
            }
        }
    }
}
