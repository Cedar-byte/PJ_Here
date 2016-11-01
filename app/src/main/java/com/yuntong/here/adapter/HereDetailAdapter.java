package com.yuntong.here.adapter;

import android.content.Context;

import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.HereDateilData;
import com.yuntong.here.holder.HereDetailHolder;

/**
 * Created by Administrator on 2016/5/13.
 * here详情
 */
public class HereDetailAdapter extends PKAdapter<HereDateilData, HereDetailHolder> {

    public HereDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindData(HereDetailHolder holder, int position, HereDateilData data) {
        holder.name.setText(data.getName());
    }
}
