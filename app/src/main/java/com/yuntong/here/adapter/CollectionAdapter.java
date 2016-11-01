package com.yuntong.here.adapter;

import android.content.Context;
import android.view.View;

import com.yuntong.here.R;
import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.holder.HomeHolder;
import com.yuntong.here.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2016/5/11.
 */
public class CollectionAdapter extends PKAdapter<SceneListData,HomeHolder> {
    private Context context;
    public CollectionAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onBindData(HomeHolder holder, int position, final SceneListData data) {
        holder.nameText.setText(data.getName());// 名字
        holder.adressText.setText(data.getAddress());// 地址
        double d = data.getDistance();
        if(d < 1000&&d>-1){
            holder.distanceText.setText(String.valueOf(d) + "m");// 距离
        }else if(d > 1000){
            double d2 = d/1000;
            holder.distanceText.setText(new java.text.DecimalFormat("#.0").format(d2) + "km");// 距离
        }else {
            holder.imss.setVisibility(View.GONE);
        }
        if(d < 500&&d>-1){
            holder.hereImg.setVisibility(View.VISIBLE);
        }else{
            holder.hereImg.setVisibility(View.GONE);
        }
        int heat = data.getHeat();
        holder.hotText.setText(String.valueOf(heat));// 热度
        ImageLoaderUtil.disPlayBig(data.getThumb(),holder.bgImg);
    }
}
