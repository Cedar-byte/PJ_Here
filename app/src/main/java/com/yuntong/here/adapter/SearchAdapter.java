package com.yuntong.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.yuntong.here.activity.SceneActivity;
import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.holder.HomeHolder;
import com.yuntong.here.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2016/5/9.
 */
public class SearchAdapter extends PKAdapter<SceneListData,HomeHolder> {
    private Context context;
    public SearchAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onBindData(HomeHolder holder, int position, final SceneListData data) {
        holder.nameText.setText(data.getName());// 名字
        holder.adressText.setText(data.getAddress());// 地址
        String distance = String.valueOf(data.getDistance());
        holder.distanceText.setText(distance);// 距离
        String hot = String.valueOf(data.getHeat());
        double d = data.getDistance();
        if(d < 1000&&d>-1){
            holder.distanceText.setText(String.valueOf(d) + "m");// 距离
        }else if(d > 1000){
            double d2 = d/1000;
            holder.distanceText.setText(new java.text.DecimalFormat("#.0").format(d2)+"km");// 距离
        }else {
            holder.imss.setVisibility(View.GONE);
        }
        if(d < 500&&d>-1){
            holder.hereImg.setVisibility(View.VISIBLE);
        }else{
            holder.hereImg.setVisibility(View.GONE);
        }
        ImageLoaderUtil.disPlayBig(data.getThumb(),holder.bgImg);
        holder.hotText.setText(hot);// 热度
        holder.home_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SceneActivity.class);
                i.putExtra("sceneId", data.getSceneId());
                i.putExtra("panoId", data.getPanoId());
                i.putExtra("name", data.getName());
                i.putExtra("thumb",data.getThumb());
                context.startActivity(i);
            }
        });
    }
}
