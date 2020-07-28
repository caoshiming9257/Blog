package com.simon.blog.web;

import com.simon.blog.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/25 14:25
 * @Description: 归档
 */
@Controller
public class ArchivesController {

    @Resource
    BlogService blogService;


    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("mapBlog",blogService.getBlogByUpdateYear());
        model.addAttribute("blogCount",blogService.getAllBlogCount());
        return "archives";
    }
}
