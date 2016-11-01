package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by me on 2016/5/5.
 * 轮播广告数据实体类
 */
public class AdData implements Serializable{

    private String name;
    private String picUrl;
    private String clickLink;
    private String type;

    public AdData(String name, String picUrl, String clickLink, String type) {
        this.name = name;
        this.picUrl = picUrl;
        this.clickLink = clickLink;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getClickLink() {
        return clickLink;
    }

    public void setClickLink(String clickLink) {
        this.clickLink = clickLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
