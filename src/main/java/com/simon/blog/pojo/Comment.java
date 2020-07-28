package com.simon.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

//import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 14:05
 * @Description：留言
 */
@Data
@NoArgsConstructor
@TableName(value = "t_comment")
public class Comment implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**昵称**/
    @TableField(value = "nickName")
    private String nickName;

    /**评论内容**/
    @TableField(value = "content")
    private String content;

    /**邮箱**/
    @TableField(value = "email")
    private String email;

    /**头像**/
    @TableField(value = "avatar")
    private String avatar;

    /**评论时间**/
    @TableField(value = "createTime")
    private Date createTime;

    /**是否为管理员的评论信息**/
    @TableField(value = "adminComment")
    private boolean adminComment;

    /**外键blog**/
    @TableField(value = "blog_id")
    private Long blog_id;

    /**外键blog**/
    @TableField(value = "parentComment_id")
    private Long parentComment_id;

    /**Comment类和Blog类是多对一关系**/
//    @ManyToOne
    @TableField(exist = false)
    private Blog blog;

//    @OneToMany(mappedBy = "parentComment")
    @TableField(exist = false)
    private List<Comment> replyComments = new ArrayList<>();

//    @ManyToOne
    @TableField(exist = false)
    private Comment parentComment;
}
