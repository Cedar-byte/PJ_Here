package com.yuntong.here.entity;

/**
 * Created by Administrator on 2016/5/25.
 * 手机中的图片的信息
 */
public class PictureInfo {

    public String path;// 图片路径
    public String name;// 图片名字
    public long time;// 时间

    public boolean isSelector;// 是否选择

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    public PictureInfo(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }
}
