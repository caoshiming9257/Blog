package com.simon.blog.service;

import com.simon.blog.pojo.User;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 20:47
 * @Description: 管理员登陆
 */
public interface UserService {

    User checkUser(String userName,String password);

    User findById(Long id);
}
