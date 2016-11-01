package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by me on 2016/5/5.
 * 城市列表数据
 */
public class CityData implements Serializable{

    private int cityId;// 1
    private String code;// "hangzhou"
    private String thumb;// "www.baidu.com/1.png"
    private String name;// "杭州"
    private int status;// "1.暂未开通 2.计划开通 3.正在开通 4.已开通"

    public CityData(int cityId, int status, String name, String thumb, String code) {
        this.cityId = cityId;
        this.status = status;
        this.name = name;
        this.thumb = thumb;
        this.code = code;
    }

    @Override
    public String toString() {
        return "CityData{" +
                "cityId=" + cityId +
                ", code='" + code + '\'' +
                ", thumb='" + thumb + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
