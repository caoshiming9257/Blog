package com.simon.blog.service.Impl;

import com.simon.blog.dao.UserMapper;
import com.simon.blog.pojo.User;
import com.simon.blog.service.UserService;
import com.simon.blog.util.MD5Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 20:47
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User checkUser(String userName,String password) {
        return userMapper.findByUser(userName, MD5Utils.code(password));
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }
}
