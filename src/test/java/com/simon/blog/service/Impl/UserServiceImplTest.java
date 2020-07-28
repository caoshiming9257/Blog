package com.simon.blog.service.Impl;

import com.simon.blog.dao.UserMapper;
import com.simon.blog.pojo.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/9 21:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {

    @Resource
    private UserMapper userMapper;


//    @Test
//    public void checkUser() {
//        String userName = "simon";
//        User byUser = userMapper.findByUser(userName);
//        System.out.println(byUser.toString());
//    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//        return sqlSessionFactoryBean.getObject();
//    }

    @Test
    public void te(){
        System.out.println(userMapper.findByUser("nihao","2222"));
    }
}