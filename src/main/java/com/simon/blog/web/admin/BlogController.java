package com.simon.blog.web.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.*;
import com.simon.blog.service.*;
import com.simon.blog.util.StringListUtil;
import org.springframework.beans.factory.annotation.Value;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/10 21:43
 * @Descriptin: 后台博客
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/admin-input";
    private static final String LIST = "admin/admin";
    private static final String REDIRECT = "redirect:/admin/blogs";

    @Value("${pageSize}")
    private String pageSize;

    @Resource
    BlogService blogService;

    @Resource
    TypeService typeService;

    @Resource
    TagService tagService;

    @Resource
    BlogTagService blogTagService;

    /**
     * 页面分页展示，
     * */
    @GetMapping("/blogs")
    public String blogs(Model model, HttpServletRequest request){
        Page<Blog> blogPage = firstShowAndSearchShow(request);

        model.addAttribute("typeList",typeService.listAllType());
        model.addAttribute("blogs",blogPage.getRecords());
        model.addAttribute("pages",blogPage);
        return LIST;
    }

    /**
     * 搜索后只刷新表格数据
     * */
    @PostMapping("/blogs/search")
    public String search(BlogSearch blogSearch, Model model, HttpServletRequest request){
//        Page<Blog> blogPage = firstShowAndSearchShow(request,blogSearch);
        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null){
            page = "1";
        }

        /**先根据page的参数获取数据，然后查看当前当前总页数是否小于page参数，
         * 为了解决一直点下一页进行跳转的问题
         * **/
        Page<Blog> blogPage = null;
        /**根据BlogSearch的值调用不同的分页方法**/
        blogPage = blogService.pageSearchBlog(new Page<>(Integer.parseInt(page),Integer.parseInt(pageSize)),blogSearch);

        Integer pages = (int)blogPage.getPages();
        if (blogPage.getRecords().size() == 0 && pages< Integer.parseInt(page)){
            if (pages != 0){
                blogPage = blogService.pageBlog(new Page<>(pages, Integer.parseInt(pageSize)));
            }
        }

        model.addAttribute("blogs",blogPage.getRecords());
        model.addAttribute("pages",blogPage);
        return "admin/admin::blogList";
    }

    /**
     * 新增页面
     * **/
    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    /**
     * 新增、编辑博客
     * **/
    @PostMapping("/newBlogs")
    public String submit(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setUserId(((User) session.getAttribute("user")).getId());
        blog.setTypeName((typeService.getType(blog.getType().getId())).getName());
        blog.setTypeId(blog.getType().getId());
//        blog.setTags(tagService.listTag(blog.getTagIds()));
        Integer integer = 0;
        if (blog.getId() == null){
            integer =  blogService.saveBlog(blog);
        }else {
            integer =  blogService.updateBlog(blog);
        }

        if (integer == 0){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            blogTagService.saveBlogTag(blog);
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT;
    }

    /**
     * 编辑页面
     * **/
    @GetMapping("/blogs/{id}/input")
    public String updateInput(@PathVariable Long id, Model model){
        Blog blog = blogService.getBlog(id);
        /**根据blog的id在中间表中找到对应的所有tag**/
        List<BlogTagRelation> allByBlogId = blogTagService.findAllByBlogId(id);
        /**将tag的id转换为1,2,3样式**/
        blog.setTagIds(StringListUtil.listToString(allByBlogId));
        setTypeAndTag(model);
        model.addAttribute("blog",blog);
        return INPUT;
    }


    /**
     * 删除博客
     * **/
    @GetMapping("/blogs/{id}/delete")
    public String deleteTag(@PathVariable Long id,RedirectAttributes redirectAttributes){
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }

    /**
     * 首次进入展示页面和根据搜索条件进行数据展示
     * **/
    private Page<Blog> firstShowAndSearchShow(HttpServletRequest request) {
        /**初次登陆分类页面直接为page设置值**/
        String page = request.getParameter("page");
        if ("null".equals(page) || page == null || "0".equals(page)){
            page = "1";
        }

        /**先根据page的参数获取数据，然后查看当前当前总页数是否小于page参数，
         * 为了解决一直点下一页进行跳转的问题
         * **/
        Page<Blog> blogPage = null;
        /**根据BlogSearch的值调用不同的分页方法**/
        blogPage = blogService.pageBlog(new Page<>(Integer.parseInt(page),Integer.parseInt(pageSize)));


        Integer pages = (int)blogPage.getPages();

        if (pages< Integer.parseInt(page)){
            blogPage = blogService.pageBlog(new Page<>(pages,Integer.parseInt(pageSize)));
        }
        return blogPage;
    }

    /**
     * 设置页面的分类和标签列表值
     * **/
    private void setTypeAndTag(Model model){
        model.addAttribute("typeList",typeService.listAllType());
        model.addAttribute("tagList",tagService.listAllTag());
    }
}
