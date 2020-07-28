package com.simon.blog.util;

import com.simon.blog.pojo.BlogTagRelation;
import com.simon.blog.pojo.CountName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/15 14:46
 * @Description: 将字符串拆分保存至集合中
 */
public class StringListUtil {

    /** 
      @Description: 将前台传递的标签id字符串分割，为了和博客id对应插入中间表
      @Param: [ids]
      @return: java.util.List<java.lang.Long>
      @Author: Simon_Cao
      @Date: 2020/7/24
     */ 
    public static List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids != null){
            String[] splitArray = ids.split(",");
            for (String split : splitArray){
                list.add(Long.parseLong(split));
            }
        }
        return list;
    }

    /** 
      @Description: 将中间表中的标签拼接为字符串，方便前台显示
      @Param: [blogTagRelationList]
      @return: java.lang.String
      @Author: Simon_Cao
      @Date: 2020/7/24
     */ 
    public static String listToString(List<BlogTagRelation> blogTagRelationList){
        StringBuffer ids = new StringBuffer();
        boolean flag = false;
        for (BlogTagRelation blogTagRelation : blogTagRelationList){
            if (flag){
                ids.append(",");
            }else {
                flag = true;
            }
            ids.append(blogTagRelation.getTagId());
        }
        return ids.toString();
    }

    /*根据count进行排序*/
    public static List<CountName> orderDesc(List<CountName> listCountName){
        List<CountName> orderDesc = new ArrayList<>();
        for(CountName countName : listCountName){
            if (countName.getOtherId() == null){
                countName.setCount(0);
            }
            orderDesc.add(countName);
        }
        Collections.sort(orderDesc);
        return orderDesc;
    }

}
