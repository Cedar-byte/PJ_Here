package com.yuntong.here.holder;

import android.content.Context;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by Administrator on 2016/5/16.
 */
@PK(R.layout.item_mtcomment)
public class MyCommentHolder extends PKHolder {

    @PK(R.id.comment_content)
    public TextView comment_content;
    @PK(R.id.here_name)
    public TextView here_name;
    @PK(R.id.comment_time)
    public TextView comment_time;
    public MyCommentHolder(Context context) {
        super(context);
    }
}
