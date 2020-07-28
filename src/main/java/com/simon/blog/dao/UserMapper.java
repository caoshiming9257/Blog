package com.simon.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.blog.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 21:58
 * @Description: 管理员登陆，验证用户名和密码
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from t_user where userName=#{userName} and password=#{password}")
   User findByUser(@Param("userName")String userName,@Param("password")String password);
}
