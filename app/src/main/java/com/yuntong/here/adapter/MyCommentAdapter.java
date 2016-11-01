package com.yuntong.here.adapter;

import android.content.Context;

import com.yuntong.here.base.PKAdapter;
import com.yuntong.here.entity.MyCommentData;
import com.yuntong.here.holder.MyCommentHolder;
import com.yuntong.here.util.CommonUtil;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyCommentAdapter extends PKAdapter<MyCommentData,MyCommentHolder> {
    private Context context;
    public MyCommentAdapter(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onBindData(MyCommentHolder holder, int position, MyCommentData data) {
        holder.comment_content.setText(data.getComment_content());
        holder.comment_time.setText(CommonUtil.getTimetoYMD(Long.valueOf(data.getComment_time())));
        holder.here_name.setText(data.getHere_name());
    }
}
