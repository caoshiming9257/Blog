package com.simon.blog.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simon.blog.dao.TypeMapper;
import com.simon.blog.dao.UserMapper;
import com.simon.blog.pojo.BlogSearch;
import com.simon.blog.pojo.BlogTagRelation;
import com.simon.blog.pojo.Type;
import com.simon.blog.pojo.User;
import com.simon.blog.util.StringListUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/11 17:10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TypeServiceImplTest {

    @Resource
    private TypeMapper typeMapper;

    @Test
    public void te(){
//        Page<Type> typePage = new Page<>(1,5);
//        Page<Type> typePage1 = typeMapper.selectPage(typePage,null);
////        List<Type> users = typeMapper.selectPage(userPage, null);
//        System.out.println(typePage);
//        System.out.println(typePage1.getRecords());
//        System.out.println(typePage1.getPages());
//        System.out.println(typePage1.getTotal());
//         System.out.println(typePage1.getCurrent());
//        System.out.println(typePage1.getSize());
//        System.out.println(typePage1.hasPrevious());
//        System.out.println(typePage1.hasNext());
    }

    @Test
    public void updateTe(){
        Type tp = new Type();
        tp.setId(Long.parseLong("20"));
        tp.setName("java3");
        Long id = tp.getId();
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id",id);
        int update = typeMapper.update(tp, updateWrapper);
        System.out.println(update);
    }

    @Test
    public void stringToList(){
//        String ss = "1,2,3";
//        List<Long> list = StringListUtil.convertToList(ss);
//        for (int i = 0; i<list.size();i++){
//            System.out.println(list.get(i));
//        }
        BlogSearch blogSearch = new BlogSearch();
        blogSearch.setStatus("发布");
        System.out.println(blogSearch.getStatus() == "草稿"? 1 : 0);
    }
}