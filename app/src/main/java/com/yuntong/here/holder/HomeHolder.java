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
 * Created by me on 2016/5/3.
 * item项、控件、初始化
 */
@PK(R.layout.item_list_home)
public class HomeHolder extends PKHolder{

    @PK(R.id.home_item)
    public RelativeLayout home_item;

    @PK(R.id.img_here_green)
    public ImageView hereImg;// Here标识

    @PK(R.id.item_home_list_name_txt)
    public TextView nameText;// 商家名字

    @PK(R.id.item_home_list_adress_txt)
    public TextView adressText;// 地址

    @PK(R.id.item_distance)
    public TextView distanceText;// 距离

    @PK(R.id.item_hot)
    public TextView hotText;// 热度

    @PK(R.id.imss)
    public LinearLayout imss;

    @PK(R.id.img_item_bg)
    public ImageView bgImg;// 背景图

    public HomeHolder(Context context) {
        super(context);
    }
}
