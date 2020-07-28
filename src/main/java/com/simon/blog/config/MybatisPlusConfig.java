package com.simon.blog.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 17:06
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    //配置mybatis的分页插件pageHelper
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;

    }
}
