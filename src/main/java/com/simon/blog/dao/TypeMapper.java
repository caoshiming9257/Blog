package com.simon.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:56
 * @Decription：后台分类
 */

@Mapper
public interface TypeMapper extends BaseMapper<Type> {

    @Select("select * from t_type where name = #{name}")
    Type findByName(@Param("name") String name);

    List<CountName> typeAndCount();
}
