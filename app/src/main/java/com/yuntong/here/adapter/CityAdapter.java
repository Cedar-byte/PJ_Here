package com.yuntong.here.adapter;

import android.content.Context;
import android.view.View;

import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.CityData;
import com.yuntong.here.holder.CityHolder;
import com.yuntong.here.util.ImageLoaderUtil;


/**
 * Created by me on 2016/5/6.
 * 城市列表适配器
 */
public class CityAdapter extends PKAdapter<CityData, CityHolder> {

    public CityAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindData(CityHolder holder, int position, CityData data) {

        holder.relative.setAlpha(0.6f);

        holder.citynameText.setText(data.getName());
        if(data.getStatus() == 1){
            holder.statusText.setText("暂未开通");
            holder.linearLayout.setEnabled(false);
        }
        if(data.getStatus() == 2){
            holder.statusText.setText("计划开通");
        }
        if(data.getStatus() == 3){
            holder.statusText.setText("正在开通");
        }
        if(data.getStatus() == 4){
            holder.statusText.setText("已开通");
            holder.relChoice.setVisibility(View.VISIBLE);
            holder.relative.setVisibility(View.GONE);
        }
        ImageLoaderUtil.disPlay(data.getThumb(), holder.bg);
    }
}
