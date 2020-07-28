package com.simon.blog.web.admin;

import com.simon.blog.pojo.User;
import com.simon.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/10 13:53
 * @Description: 管理员登陆
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Resource
    UserService userService;

    /**登陆页面**/
    @GetMapping
    public String loginPage(){
        return "admin/adminLogin";
    }

    /**登陆成功或失败跳转页面**/
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes){
        User user = userService.checkUser(username, password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        }else {
            redirectAttributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }
    }

    /**注销用户**/
    @GetMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
