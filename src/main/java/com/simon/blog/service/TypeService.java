package com.simon.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Type;

import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:46
 * @Decription：后台分类新增
 */
public interface TypeService {

    Integer saveTypa(Type type);

    Type findByName(String name);

    Type getType(Long id);

    List<Type> listAllType();

    Page<Type> pageType(Page<Type> typePage);

    Integer updateType(Long id, Type type);

    void deleteType(Long id);

    List<CountName> typeAndCount();
}
