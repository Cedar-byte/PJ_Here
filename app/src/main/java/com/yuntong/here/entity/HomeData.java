package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by me on 2016/5/3.
 * 首页ListView的数据实体类
 */
public class HomeData implements Serializable{

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
