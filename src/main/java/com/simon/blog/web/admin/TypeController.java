package com.simon.blog.web.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.Type;
import com.simon.blog.service.TypeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static org.aspectj.apache.bcel.Constants.types;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 14:47
 * @Description: 后台分类
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Resource
    TypeService typeService;

    /**
     * 页面分页展示，
     * */
    @GetMapping("/types")
    public String types(Model model, HttpServletRequest request){

        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null){
            page = "1";
        }
        Page<Type> typePage = typeService.pageType(new Page<>(Integer.parseInt(page),5));
        Integer pages = (int)typePage.getPages();

        if (pages< Integer.parseInt(page)){
            typePage = typeService.pageType(new Page<>(pages,5));
        }

        model.addAttribute("types",typePage.getRecords());
        model.addAttribute("pages",typePage);
        return "admin/types";
    }

    /**
     * 点击添加新增按钮，跳转到新增分类页面
     * */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    /**
     * 点击编辑按钮，跳转到新增分类页面(和新增分类共用一个页面)
     * */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    /**
     * 编辑之后进行判断是否重复，若不重复进行新增
     * */
    @PostMapping("/typeBlog/{id}")
    public String post(@Valid Type type, BindingResult result,@PathVariable Long id, RedirectAttributes redirectAttributes){
        /**根据分类名称查找数据，如果已经存在就不允许添加**/
        Type typeName = typeService.findByName(type.getName());
        if(typeName != null){
            result.rejectValue("name","nameError","不能重复添加分类");
        }

        /**如果新增失败依然停留在当前页面**/
        if (result.hasErrors()){
            return "admin/types-input";
        }

        /**将分类名称添加至数据库**/
        Integer integer = typeService.updateType(id,type);
        if (integer == 0){
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else {
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    /**
     * 新增时检查新增分类是否存在，若不存在则进行新增
     * */
    @PostMapping("/typeBlog")
    public String post(@Valid Type type, BindingResult result, RedirectAttributes redirectAttributes){
        /**根据分类名称查找数据，如果已经存在就不允许添加**/
        Type typeName = typeService.findByName(type.getName());
        if(typeName != null){
            result.rejectValue("name","nameError","不能重复添加分类");
        }

        /**如果新增失败依然停留在当前页面**/
        if (result.hasErrors()){
            return "admin/types-input";
        }

        /**将分类名称添加至数据库**/
        Integer integer = typeService.saveTypa(type);
        if (integer == 0){
            redirectAttributes.addFlashAttribute("message","新增失败");
        }else {
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id,RedirectAttributes redirectAttributes){
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
