package com.yuntong.here.adapter;

import android.content.Context;

import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.base.PKHolder;
import com.yuntong.here.entity.HistoryData;
import com.yuntong.here.holder.HistoryHolder;

/**
 * Created by Administrator on 2016/5/6.
 */
public class HistoryAdapter extends PKAdapter<HistoryData,HistoryHolder> {
    private Context context;
    public HistoryAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onBindData(HistoryHolder holder, int position, HistoryData data) {
        holder.history_item.setText(data.getHistory_item());
    }


}
