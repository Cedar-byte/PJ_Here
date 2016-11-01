package com.yuntong.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yuntong.here.R;
import com.yuntong.here.activity.OtherPeopleActivity;
import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.HereDetailCommentData;
import com.yuntong.here.holder.HereDetailCommentHolder;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.JsonUtil;

/**
 * Created by Administrator on 2016/5/18.
 */
public class HereDtailCommentAdapter extends PKAdapter<HereDetailCommentData,HereDetailCommentHolder>{
    private Context context;
    public HereDtailCommentAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onBindData(HereDetailCommentHolder holder, final int position, final HereDetailCommentData data) {
        holder.item_heredetail_content.setText(data.getItem_heredetail_content());
        holder.item_heredetail_time.setText(CommonUtil.getTimeto(Long.valueOf(data.getItem_heredetail_time())));
        holder.item_heredetail_name.setText(data.getItem_heredetail_name());
        if (data.getItem_heredetail_sex().equals("1")){
            holder.item_heredetail_sex.setImageResource(R.drawable.icon_man_small);
        }else {
            holder.item_heredetail_sex.setImageResource(R.drawable.icon_women_small);
        }
        Glide.with(context)
                .load(data.getItem_heredetail_image())
                .transform(new GlideCircleTransform(context))
                .into(holder.item_heredetail_image);
        // 点击头像查看他人here
        holder.item_heredetail_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, OtherPeopleActivity.class);
//                intent.putExtra("otherId",data.getUserId());
//                startActivity(intent);
            }
        });
    }
}
