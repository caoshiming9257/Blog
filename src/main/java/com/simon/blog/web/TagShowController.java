package com.simon.blog.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.Blog;
import com.simon.blog.pojo.BlogSearch;
import com.simon.blog.pojo.CountName;
import com.simon.blog.service.BlogService;
import com.simon.blog.service.TagService;
import com.simon.blog.service.UserService;
import com.simon.blog.util.BlogAndTagUtil;
import com.simon.blog.util.StringListUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/23 14:13
 */
@Controller
public class TagShowController {

    @Resource
    TagService tagService;

    @Resource
    BlogService blogService;

    @Resource
    UserService userService;

    @Value("${pageSize}")
    private int pageSize;

    @Value("${userId}")
    private String userId;

    /**标签页面**/
    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Long id, Model model, HttpServletRequest request){

        List<CountName> list = StringListUtil.orderDesc(tagService.tagAndCount());
        if (id == -1){
            id = list.get(0).getId();
        }
        Page<Blog> blogPage = new Page<>();
        List<Blog> blogByTypeId = blogService.findBlogByTagId(id);
        if (blogByTypeId.size() >0){
            blogPage = firstShowAndSearchShow(request, id);
        }
        model.addAttribute("blogs", BlogAndTagUtil.blogInsertTag(blogPage.getRecords()));
        model.addAttribute("pages",blogPage);
        model.addAttribute("tags", list);
        model.addAttribute("user",userService.findById(Long.parseLong(userId)));
        model.addAttribute("activeTypeId",id);
        return "tags";
    }

    private Page<Blog> firstShowAndSearchShow(HttpServletRequest request,Long tagId) {
        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null){
            page = "1";
        }

        /**先根据page的参数获取数据，然后查看当前当前总页数是否小于page参数，
         * 为了解决一直点下一页进行跳转的问题
         * **/
        Page<Blog> blogPage = null;
        blogPage = blogService.pageByTagId(new Page<>(Integer.parseInt(page),pageSize),tagId);


        Integer pages = (int)blogPage.getPages();

        if (pages< Integer.parseInt(page)){
            blogPage = blogService.pageBlog(new Page<>(pages,pageSize));
        }
        return blogPage;
    }
}
