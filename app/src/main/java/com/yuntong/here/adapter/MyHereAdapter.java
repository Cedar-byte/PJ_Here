package com.yuntong.here.adapter;

import android.content.Context;

import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.MyhereData;
import com.yuntong.here.holder.MyHereHolder;
import com.yuntong.here.util.ImageLoaderUtil;

/**
 * Created by Administrator on 2016/5/10.
 */
public class MyHereAdapter extends PKAdapter<MyhereData,MyHereHolder> {
    private Context context;
    public MyHereAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onBindData(MyHereHolder holder, int position, MyhereData data) {
        holder.myhere_name.setText(data.getMyhere_name());
        holder.myhere_content.setText(data.getMyhere_content());
        holder.my_here_date.setText(data.getMy_here_date());
        holder.my_here_time.setText(data.getMy_here_time());
        holder.my_here_zan.setText(data.getMy_here_zan());
        holder.my_here_comment.setText(data.getMy_here_comment());
        holder.my_here_imagenum.setText(data.getMy_here_imagenum());
        ImageLoaderUtil.disPlay(data.getMy_here_image(),holder.my_here_image);
    }
}
