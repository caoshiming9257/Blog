package com.simon.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Tag;
import com.simon.blog.pojo.Type;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 13:56
 * @Decription：后台标签
 */

@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    /**根据名称查找标签*/
    @Select("select * from t_tag where name = #{name}")
    Tag findByName(@Param("name") String name);

    List<CountName> tagAndCount();

    @Select("SELECT * FROM t_tag e RIGHT  JOIN (select * FROM t_blog_tag where t_blog_id = #{id}) t ON t.t_tag_id = e.id ORDER BY e.id ")
    List<Tag> findTagByBlogId(Long id);

}
