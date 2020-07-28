package com.simon.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 12:45
 * @Description：博客
 */
@Data
@NoArgsConstructor
@TableName(value = "t_blog")
public class Blog implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**标题**/
    @TableField(value = "title")
    private String title;

    /**描述**/
    @TableField(value = "description")
    private String description;

    /**内容**/
    @TableField(value = "content")
    private String content;

    /**首图 **/
    @TableField(value = "firstPicture")
    private String firstPicture;

    /**标记**/
    @TableField(value = "flag")
    private String flag;

    /**浏览次数**/
    @TableField(value = "views")
    private Integer views;

    /**赞赏开启**/
    @TableField(value = "appreciation")
    private boolean appreciation;

    /**转载声明**/
    @TableField(value = "shareStatement")
    private boolean shareStatement;

    /**评论开启**/
    @TableField(value = "commentabled")
    private boolean commentabled;

    /**发布**/
    @TableField(value = "published")
    private boolean published;

    /**是否推荐**/
    @TableField(value = "recommend")
    private boolean recommend;

    /**创建时间**/
    @TableField(value = "createTime")
    private Date createTime;

    /**更新时间**/
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "updateTime")
    private Date updateTime;

    /**外键type**/
    @TableField(value = "type_id")
    private Long typeId;

    /**外键user**/
    @TableField(value = "user_id")
    private Long userId;

    /**分类**/
    @TableField(value = "type_name")
    private String typeName;

    /**前台传递的多个标签的id**/
    @TableField(exist = false)
    private String tagIds;

    /**Blog类和Type类是多对一关系**/
    @TableField(exist = false)
    private Type type;

    /**Blog类和Tag类是多对多关系**/
    @TableField(exist = false)
    private List<Tag> tags = new ArrayList<>();

    /**Blog类和User类是多对一关系**/
    @TableField(exist = false)
    private User user;

    /**Blog类和Tag类是一对多关系**/
    @TableField(exist = false)
    private List<Comment> comments = new ArrayList<>();

}
