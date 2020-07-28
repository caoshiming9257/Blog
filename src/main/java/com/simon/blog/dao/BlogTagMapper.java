package com.simon.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.blog.pojo.BlogTagRelation;
import com.simon.blog.pojo.CountName;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/16 13:25
 * @Description: 博客和标签中间表
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTagRelation> {

    /**
     * 根据博客id删除所有的数据
     * **/
    @Select("delete from t_blog_tag where t_blog_id = #{id}")
   BlogTagRelation deleteAllByBlogId(@Param("id") Long id);

    List<CountName> findCountTag();
}
