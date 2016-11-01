package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/13.
 */
public class HereDateilData implements Serializable{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HereDateilData(String name) {
        this.name = name;
    }
}
