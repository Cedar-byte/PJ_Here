package com.yuntong.here.holder;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.PK;
import com.yuntong.here.base.PKHolder;

/**
 * Created by Administrator on 2016/5/25.
 */
@PK(R.layout.item_list_search_users)
public class UserHolder extends PKHolder{
    @PK(R.id.item_users_head)
    public ImageView item_users_head;
    @PK(R.id.txt_nickname)
    public TextView txt_nickname;
    @PK(R.id.txt_number)
    public TextView txt_number;
    @PK(R.id.item_add)
    public Button item_add;

    public UserHolder(Context context) {
        super(context);
    }
}
