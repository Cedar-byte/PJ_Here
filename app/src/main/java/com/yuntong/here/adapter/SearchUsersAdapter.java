package com.yuntong.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuntong.here.R;
import com.yuntong.here.activity.AddFriendActivity;
import com.yuntong.here.base.CommonAdapter;
import com.yuntong.here.base.ViewHolder;
import com.yuntong.here.entity.UserData;
import com.yuntong.here.util.GlideCircleTransform;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 * 查找用户
 */
public class SearchUsersAdapter extends CommonAdapter<UserData>{

    private Context context;



    /**
     * 定义接口 将添加好友方法暴露
     */
    public interface AddCallBack{
        public void addFriends(String bapplyId);
    }

    public SearchUsersAdapter(Context context, List<UserData> arrayList, int itemLayoutId) {
        super(context, arrayList, itemLayoutId);
        this.context = context;

    }

    @Override
    public void convert(ViewHolder holder, final UserData data, int position) {
        ImageView head = holder.getView(R.id.item_users_head);// 头像
        TextView nickname = holder.getView(R.id.txt_nickname);// 昵称
        TextView number = holder.getView(R.id.txt_number);// 手机号
        final Button add = holder.getView(R.id.item_add);// 添加
        if (data.getIsadd().equals("0")){
            add.setVisibility(View.VISIBLE);
        }else {
            add.setVisibility(View.GONE);
        }

        Glide.with(context).load(data.getHeadPic()).transform(new GlideCircleTransform(context)).into(head);
        nickname.setText(data.getNickname());
        number.setText(data.getName());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setVisibility(View.GONE);
                data.setIsadd("1");
                Intent i=new Intent(context, AddFriendActivity.class);
                i.putExtra("bapplyId",data.getUserId());
                context.startActivity(i);
            }
        });
    }
}
