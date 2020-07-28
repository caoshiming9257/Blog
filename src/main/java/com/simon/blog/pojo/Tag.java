package com.simon.blog.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;


import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 14:05
 * @Description：标签
 */
@Data
@NoArgsConstructor
@TableName(value = "t_tag")
public class Tag implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**标签名称**/
    @NotBlank(message = "名称不能为空")
    private String name;

    /**Tag类和Blog类是多对多关系，同时tag是被维护端**/
//    @ManyToMany(mappedBy = "tags")
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();
}
