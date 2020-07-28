package com.simon.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.pojo.Blog;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:56
 * @Decription：后台博客
 */

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    /**获取最新的数据ID**/
    @Select("SELECT  id FROM `t_blog` ORDER BY `id` DESC LIMIT 1")
    Long findLastID();

    /**修改浏览次数*/
    @Transactional
    @Select("update t_blog set views = views+1 where id = #{id}")
    Integer updateView(@Param("id") Long id);

    /**根据分类出现的次数获取名称和数量**/
    List<CountName> findCountTypeName();

    /**联合查询分页**/
    @Select("SELECT * FROM t_blog e RIGHT  JOIN (SELECT t_blog_id FROM t_blog_tag WHERE t_tag_id = #{id}) t ON t.t_blog_id = e.id ORDER BY e.id ")
    List<Blog> pageByTagId(Page<Blog> blogPage, @Param("id") Long TagId);

    /*根据type id查看是否有存在的博客*/
    @Select("SELECT * FROM t_blog e RIGHT  JOIN (SELECT t_blog_id FROM t_blog_tag WHERE t_tag_id = #{id}) t ON t.t_blog_id = e.id")
    List<Blog> findBlogByTagId(@Param("id") Long id);

    /**获取博客修改日期中的年份**/
    @Select("SELECT distinct date_format(updateTime,'%Y') AS year FROM t_blog ORDER BY year Desc")
    List<String> getBlogByUpdateYear ();
}
