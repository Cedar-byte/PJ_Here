package com.yuntong.here.holder;

import android.content.Context;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by me on 2016/5/13.
 */
@PK(R.layout.item_list_here_detail)
public class HereDetailHolder extends PKHolder{

    @PK(R.id.item)
    public TextView name;//

    public HereDetailHolder(Context context) {
        super(context);
    }
}
