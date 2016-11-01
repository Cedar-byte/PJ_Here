package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by me on 2016/5/9.
 * 场景详细概览相关数据
 */
public class SceneDetailsData implements Serializable{

    private int id;//
    private String name;//
    private String address;//
    private String thumb;// 背景缩略图
    private int heat;//
    private String businessPic;// 小图标
    private String mobile;//
    private String about;// 场景介绍

    public SceneDetailsData(int id, String name, String address, String thumb, int heat, String businessPic, String mobile, String about) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.thumb = thumb;
        this.heat = heat;
        this.businessPic = businessPic;
        this.mobile = mobile;
        this.about = about;
    }

    @Override
    public String toString() {
        return "SceneDetailsData{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", thumb='" + thumb + '\'' +
                ", heat=" + heat +
                ", businessPic='" + businessPic + '\'' +
                ", mobile='" + mobile + '\'' +
                ", about='" + about + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public String getBusinessPic() {
        return businessPic;
    }

    public void setBusinessPic(String businessPic) {
        this.businessPic = businessPic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
