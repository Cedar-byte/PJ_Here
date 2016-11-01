package com.yuntong.here.holder;

import android.content.Context;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by Administrator on 2016/5/6.
 */
@PK(R.layout.history_item)
public class HistoryHolder extends PKHolder {
    public HistoryHolder(Context context) {
        super(context);
    }


    @PK(R.id.history_item1)
    public TextView  history_item;//搜索界面  历史记录item


}
