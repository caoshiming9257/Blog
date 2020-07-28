package com.simon.blog.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.Handler.NotFoundException;
import com.simon.blog.pojo.Blog;
import com.simon.blog.pojo.BlogSearch;
import com.simon.blog.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/8 15:07
 */

/**使用restController无法实现页面的跳转，只会打印return的内容，需要使用Controller*/
@Controller
public class IndexController {

    @Resource
    BlogService blogService;

    @Resource
    BlogTagService blogTagService;

    @Resource
    UserService userService;

    @Value("${pageSize}")
    private int pageSize;

    @Value("${userId}")
    private String userId;

    /**前台首页面**/
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request){
        Page<Blog> blogPage = firstShowAndSearchShow(request);
        model.addAttribute("pages",blogPage);
        model.addAttribute("blogs",blogPage.getRecords());
        model.addAttribute("user",userService.findById(Long.parseLong(userId)));
        /**每个分类出现的次数和对应的名称**/
        model.addAttribute("types",blogService.findCountTypeName());
        model.addAttribute("recommends",blogService.findCountRecommendName());
        model.addAttribute("tags",blogTagService.findCountTag());
        return "index";
    }

    @PostMapping("/search")
    public String search(Model model,@RequestParam String query ,HttpServletRequest request){
        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null){
            page = "1";
        }

        /**先根据page的参数获取数据，然后查看当前当前总页数是否小于page参数，
         * 为了解决一直点下一页进行跳转的问题
         * **/
        Page<Blog> blogPage = blogService.pageSearchValue(new Page<>(Integer.parseInt(page),5),query);

        Integer pages = (int)blogPage.getPages();

        if (pages< Integer.parseInt(page)){
            blogPage = blogService.pageBlog(new Page<>(pages,5));
        }
        model.addAttribute("page",blogPage);
        model.addAttribute("user",userService.findById(Long.parseLong(userId)));
        model.addAttribute("blogs",blogPage.getRecords());
        return "search";
    }

    /**跳转博客详情页**/
    @GetMapping("/blog/{id}")
    public String blog(Model model,@PathVariable Long id){
        model.addAttribute("user",userService.findById(Long.parseLong(userId)));
        model.addAttribute("blog",blogService.getContenToHtml(id));
        return "blog";
    }

    /**
     * 首次进入展示页面和根据搜索条件进行数据展示
     * **/
    private Page<Blog> firstShowAndSearchShow(HttpServletRequest request) {
        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null){
            page = "1";
        }

        /**先根据page的参数获取数据，然后查看当前当前总页数是否小于page参数，
         * 为了解决一直点下一页进行跳转的问题
         * **/
        Page<Blog> blogPage = blogService.pageBlog(new Page<>(Integer.parseInt(page),pageSize));

        Integer pages = (int)blogPage.getPages();

        if (pages< Integer.parseInt(page)){
            blogPage = blogService.pageBlog(new Page<>(pages,pageSize));
        }
        return blogPage;
    }


}
