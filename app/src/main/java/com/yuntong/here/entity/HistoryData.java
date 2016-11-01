package com.yuntong.here.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/6.
 */
public class HistoryData implements Serializable {
    private String history_item;

    public String getHistory_item() {
        return history_item;
    }

    public void setHistory_item(String history_item) {
        this.history_item = history_item;
    }
}
