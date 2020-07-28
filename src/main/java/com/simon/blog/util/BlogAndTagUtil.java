package com.simon.blog.util;

import com.simon.blog.pojo.Blog;
import com.simon.blog.service.TagService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/24 21:51
 */
@Component
public class BlogAndTagUtil {

    @Resource
    TagService tagService;

    private static BlogAndTagUtil blogAndTagUtil;

    @PostConstruct
    public void init() {
        blogAndTagUtil = this;
        blogAndTagUtil.tagService = this.tagService;
    }
    /**
      @Description: 根据博客id找到对应的所有标签对象，将其设置到博客对象属性中，为了前台方便获取
      @Param: [blogList]
      @return: java.util.List<com.simon.blog.pojo.Blog>
      @Author: Simon_Cao
      @Date: 2020/7/24
     */
    public static List<Blog> blogInsertTag(List<Blog> blogList){
        for (Blog blog : blogList){
            blog.setTags(blogAndTagUtil.tagService.findTagByBlogId(blog.getId()));
        }
        return blogList;
    }
}
