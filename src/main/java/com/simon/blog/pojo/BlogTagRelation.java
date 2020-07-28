package com.simon.blog.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 14:05
 * @Description：中间表
 */
@Data
@NoArgsConstructor
@TableName(value = "t_blog_tag")
public class BlogTagRelation implements Serializable {

//    @TableId(value = "id",type = IdType.AUTO)
//    private Long id;

    /**外键blog**/
    @TableField(value = "t_blog_id")
    private Long blogId;

    /**外键tag**/
    @TableField(value = "t_tag_id")
    private Long tagId;

}
