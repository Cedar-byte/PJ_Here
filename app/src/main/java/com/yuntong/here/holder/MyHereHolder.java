package com.yuntong.here.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by Administrator on 2016/5/10.
 */
@PK(R.layout.item_myhere)
public class MyHereHolder extends PKHolder {

    @PK(R.id.myhere_name)
    public TextView myhere_name;

    @PK(R.id.myhere_content)
    public TextView myhere_content;

    @PK(R.id.my_here_date)
    public TextView my_here_date;

    @PK(R.id.my_here_time)
    public TextView my_here_time;

    @PK(R.id.my_here_zan)
    public TextView my_here_zan;

    @PK(R.id.my_here_comment)
    public TextView my_here_comment;

    @PK(R.id.my_here_imagenum)
    public TextView my_here_imagenum;

    @PK(R.id.my_here_image)
    public ImageView my_here_image;



    public MyHereHolder(Context context) {
        super(context);
    }
}
