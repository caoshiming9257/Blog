package com.simon.blog.web.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.Tag;
import com.simon.blog.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 14:47
 * @Description: 后台分类
 */
@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    TagService tagService;

    /**
     * 页面分页展示，
     * */
    @GetMapping("/tags")
    public String tags(Model model, HttpServletRequest request){

        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null){
            page = "1";
        }

        /**先根据page的参数获取数据，然后查看当前当前总页数是否小于page参数，
         * 为了解决一直点下一页而今天跳转的问题
         * **/
        Page<Tag> tagPage = tagService.pageTag(new Page<>(Integer.parseInt(page),5));
        Integer pages = (int)tagPage.getPages();

        if (pages< Integer.parseInt(page)){
            tagPage = tagService.pageTag(new Page<>(pages,5));
        }

        model.addAttribute("tags",tagPage.getRecords());
        model.addAttribute("pages",tagPage);
        return "admin/tags";
    }

    /**
     * 点击添加新增按钮，跳转到新增分类页面
     * */
    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    /**
     * 点击编辑按钮，跳转到新增分类页面(和新增分类共用一个页面)
     * */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 编辑之后进行判断是否重复，若不重复进行新增
     * */
    @PostMapping("/tagBlog/{id}")
    public String post(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes redirectAttributes){
        /**根据分类名称查找数据，如果已经存在就不允许添加**/
        Tag tagName = tagService.findByName(tag.getName());
        if(tagName != null){
            result.rejectValue("name","nameError","不能重复添加标签");
        }

        /**如果新增失败依然停留在当前页面**/
        if (result.hasErrors()){
            return "admin/tags-input";
        }

        /**将分类名称添加至数据库**/
        Integer integer = tagService.updateTag(id,tag);
        if (integer == 0){
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else {
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 新增时检查新增分类是否存在，若不存在则进行新增
     * */
    @PostMapping("/tagBlog")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes redirectAttributes){
        /**根据分类名称查找数据，如果已经存在就不允许添加**/
        Tag tagName = tagService.findByName(tag.getName());
        if(tagName != null){
            result.rejectValue("name","nameError","不能重复添加分类");
        }

        /**如果新增失败依然停留在当前页面**/
        if (result.hasErrors()){
            return "admin/tags-input";
        }

        /**将分类名称添加至数据库**/
        Integer integer = tagService.saveTag(tag);
        if (integer == 0){
            redirectAttributes.addFlashAttribute("message","新增失败");
        }else {
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id,RedirectAttributes redirectAttributes){
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}
