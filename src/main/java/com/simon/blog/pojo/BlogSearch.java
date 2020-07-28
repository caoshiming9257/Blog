package com.simon.blog.pojo;

import lombok.Data;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/14 23:27
 * @Description：前台返回参数
 */
@Data
public class BlogSearch {

    private String title;

    private Long typeId;

    private String status;

    private Boolean recommend;

}
