package com.simon.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 14:05
 * @Description：用户
 */

@Data
@EqualsAndHashCode
@NoArgsConstructor
@TableName(value = "t_user")
public class User implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**昵称**/
    @TableField(value = "nickName")
    private String nickName;

    /**评论内容**/
    @TableField(value = "userName")
    private String userName;

    /**评论内容**/
    @TableField(value = "password")
    private String password;

    /**邮箱**/
    @TableField(value = "email")
    private String email;

    /**头像**/
    @TableField(value = "avatar")
    private String avatar;

    /**类型**/
    private String type;

    /**创建时间**/
    @TableField(value = "createTime")
    private String createTime;

    /**更新时间**/
    @TableField(exist = false)
    private String updateTime;

    /**User类和Blog类是一对多关系，同时user是被维护端**/
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();


}
