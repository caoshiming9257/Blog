package com.simon.blog.service.Impl;

import com.simon.blog.dao.BlogTagMapper;
import com.simon.blog.dao.TagMapper;
import com.simon.blog.pojo.CountName;
import com.simon.blog.pojo.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/19 13:47
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogTagServiceImplTest {

    @Resource
    BlogTagMapper blogTagMapper;

    @Resource
    TagMapper tagMapper;

    @Test
    public void findCountTag() {
        List<CountName> countTag = blogTagMapper.findCountTag();
        List<CountName> lists = new ArrayList<>();
        for (CountName countName : countTag){
            Tag tag = tagMapper.selectById(countName.getTagId());
            countName.setName(tag.getName());
            lists.add(countName);
        }
        System.out.println(lists);
    }
}