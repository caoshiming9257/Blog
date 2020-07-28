package com.simon.blog.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

/**
 * @program: blog
 * @Author: Simon_Cao
 * @Date: 2020/7/18 14:03
 */
@Data
@NoArgsConstructor
public class CountName implements Comparable<CountName> {


    private Long id;

    private Long otherId;

    private String name;

    private Long tagId;

    private Integer count;

    @Override
    public int compareTo(CountName o) {
        return o.getCount()-this.count;
    }
}
