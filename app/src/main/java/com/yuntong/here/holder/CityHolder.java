package com.yuntong.here.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by me on 2016/5/6.
 *
 */
@PK(R.layout.item_list_city)
public class CityHolder extends PKHolder{

    @PK(R.id.img_city_bg)
    public ImageView bg;// 背景图

    @PK(R.id.city_name)
    public TextView citynameText;// 城市名字

    @PK(R.id.city_status)
    public TextView statusText;// 城市是否开通

    @PK(R.id.rel_choice)
    public RelativeLayout relChoice;// 当前城市是否选中

    @PK(R.id.linear_list)
    public LinearLayout linearLayout;// item项的view

    @PK(R.id.rel_kaitong)
    public RelativeLayout relative;

    public CityHolder(Context context) {
        super(context);
    }
}
