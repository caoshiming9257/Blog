package com.simon.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simon.blog.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/21 15:31
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
