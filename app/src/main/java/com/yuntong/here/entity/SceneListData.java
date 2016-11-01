package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by me on 2016/5/9.
 * 主页商家列表数据
 */
public class SceneListData implements Serializable{

    private int sceneId;// 场景ID
    private String name;
    private String address;// 地址
    private String thumb;
    private int heat;// 热度
    private int panoId;
    private double distance;// 距离

    public SceneListData(int sceneId, String name, String address, String thumb, int heat, int panoId, double distance) {
        this.sceneId = sceneId;
        this.name = name;
        this.address = address;
        this.thumb = thumb;
        this.heat = heat;
        this.panoId = panoId;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "SceneListData{" +
                "sceneId=" + sceneId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", thumb='" + thumb + '\'' +
                ", heat=" + heat +
                ", panoId=" + panoId +
                ", distance=" + distance +
                '}';
    }

    public int getSceneId() {
        return sceneId;
    }

    public void setSceneId(int sceneId) {
        this.sceneId = sceneId;
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

    public int getPanoId() {
        return panoId;
    }

    public void setPanoId(int panoId) {
        this.panoId = panoId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
