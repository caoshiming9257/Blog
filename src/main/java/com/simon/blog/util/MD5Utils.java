package com.simon.blog.util;

import org.springframework.util.DigestUtils;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/10 21:16
 * @Description: 对密码进行加密，使用的是spring封装好的MD5工具类
 */
public class MD5Utils {

    public static String code(String password){
        String md5Code = DigestUtils.md5DigestAsHex(password.getBytes());
        return md5Code;
    }

    public static void main(String[] args) {
        System.out.println(code("123123"));
    }
}
