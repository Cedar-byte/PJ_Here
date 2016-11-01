package com.yuntong.here.holder;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by Administrator on 2016/5/18.
 */
@PK(R.layout.item_heredetail)
public class HereDetailCommentHolder extends PKHolder {

    @PK(R.id.item_heredetail_image)
    public ImageView item_heredetail_image;

    @PK(R.id.item_heredetail_sex)
    public ImageView item_heredetail_sex;

    @PK(R.id.item_heredetail_name)
    public TextView item_heredetail_name;

    @PK(R.id.item_heredetail_time)
    public TextView item_heredetail_time;

    @PK(R.id.item_heredetail_content)
    public TextView item_heredetail_content;

    public HereDetailCommentHolder(Context context) {
        super(context);
    }
}
