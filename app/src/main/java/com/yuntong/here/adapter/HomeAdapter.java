package com.yuntong.here.adapter;

import android.content.Context;

import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.HomeData;
import com.yuntong.here.holder.HomeHolder;
import com.yuntong.here.util.ImageLoaderUtil;

/**
 * Created by me on 2016/5/3.
 * 首页ListView的数据适配器
 */
public class HomeAdapter extends PKAdapter<HomeData, HomeHolder>{

    private Context context;

    public HomeAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onBindData(HomeHolder holder, int position, HomeData data) {
        holder.nameText.setText(data.getName());
        ImageLoaderUtil.disPlay("http://www.bz55.com/uploads/allimg/141202/139-141202103039.jpg", holder.bgImg);
    }
}
