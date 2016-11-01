package com.yuntong.here.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuntong.here.R;
import com.yuntong.here.activity.HereDetailActivity;
import com.yuntong.here.activity.MessageDetailActivity;
import com.yuntong.here.activity.WebViewActivity;
import com.yuntong.here.entity.MessageData;
import com.yuntong.here.entity.UserData;
import com.yuntong.here.sql.MessageDao;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.SpfUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class MessageAdapter extends BaseAdapter{
    private Context context;
    private AddCallBack callBack;
    private List<MessageData> messageDatas=new ArrayList<MessageData>();


    /**
     * 定义接口 将添加好友方法暴露
     */
    public interface AddCallBack{
        public void addFriends(String bapplyId,int i);
        public void  deleteMessage(String  id,int i);
        public void  updata(String userid,String yuntongid,String type);
    }



    public MessageAdapter(Context context, AddCallBack callBack,List<MessageData> messageDatas) {
        this.context = context;
        this.callBack=callBack;
        this.messageDatas=messageDatas;
    }

    @Override
    public int getCount() {
        return messageDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return messageDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MessageData messageData=messageDatas.get(position);
       if (messageData.getType().equals("303")){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.item_unmessaged3, parent, false);
           LinearLayout lay_messageitem=(LinearLayout)convertView.findViewById(R.id.lay_messageitem3);
           TextView title=(TextView)convertView.findViewById(R.id.message3_title);
           ImageView message_pic=(ImageView)convertView.findViewById(R.id.message3_pic);
           TextView message3_time=(TextView)convertView.findViewById(R.id.message3_time);
           TextView message3_conent2=(TextView)convertView.findViewById(R.id.message3_conent2);
           Glide.with(context).load(messageData.getAdvicePic()).transform(new GlideCircleTransform(context)).into(message_pic);
           title.setText(messageData.getTitle());
           message3_conent2.setText(messageData.getIntroduction());
           message3_time.setText(CommonUtil.getTimeto(Long.valueOf(messageData.getCreateTime())));
           lay_messageitem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(context, WebViewActivity.class);
                   i.putExtra("url", messageData.getLink());
                   context.startActivity(i);
               }
           });
           lay_messageitem.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   callBack.deleteMessage(messageData.getAdviceId(),position);
                   return false;
               }
           });
       }else if (messageData.getType().equals("302")){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.item_unmessaged3, parent, false);
           TextView title=(TextView)convertView.findViewById(R.id.message3_title);
           ImageView message_pic=(ImageView)convertView.findViewById(R.id.message3_pic);
           TextView message3_time=(TextView)convertView.findViewById(R.id.message3_time);
           TextView message3_conent2=(TextView)convertView.findViewById(R.id.message3_conent2);
           Glide.with(context).load(messageData.getAdvicePic()).transform(new GlideCircleTransform(context)).into(message_pic);
           title.setText(messageData.getTitle());
           message3_conent2.setText(messageData.getIntroduction());
           message3_time.setText(CommonUtil.getTimeto(Long.valueOf(messageData.getCreateTime())));
       }else if (messageData.getType().equals("301")){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.item_unmessaged3, parent, false);
           LinearLayout lay_messageitem=(LinearLayout)convertView.findViewById(R.id.lay_messageitem3);
           TextView title=(TextView)convertView.findViewById(R.id.message3_title);
           ImageView message_pic=(ImageView)convertView.findViewById(R.id.message3_pic);
           TextView message3_time=(TextView)convertView.findViewById(R.id.message3_time);
           TextView message3_conent2=(TextView)convertView.findViewById(R.id.message3_conent2);
           Glide.with(context).load(messageData.getAdvicePic()).transform(new GlideCircleTransform(context)).into(message_pic);
           title.setText(messageData.getTitle());
           message3_conent2.setText(messageData.getIntroduction());
           message3_time.setText(CommonUtil.getTimeto(Long.valueOf(messageData.getCreateTime())));
           lay_messageitem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(context, MessageDetailActivity.class);
                   i.putExtra("pic", messageData.getAdvicePic());
                   i.putExtra("type", messageData.getType());
                   SpfUtil.put(context, "agreededid", position);
                   i.putExtra("content", messageData.getIntroduction());
                   i.putExtra("time", messageData.getCreateTime());
                   context.startActivity(i);
               }
           });
           lay_messageitem.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   callBack.deleteMessage(messageData.getAdviceId(), position);
                   return true;
               }
           });
       }else if (messageData.getType().equals("202")||messageData.getType().equals("201")) {
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.item_unmessaged1, parent, false);
           ImageView message_pic=(ImageView)convertView.findViewById(R.id.message1_pic);
           LinearLayout lay_messageitem=(LinearLayout)convertView.findViewById(R.id.lay_messageitem1);
           TextView message3_time=(TextView)convertView.findViewById(R.id.message1_time);
           TextView message1_name=(TextView)convertView.findViewById(R.id.message1_name);
           TextView message1_conent1=(TextView)convertView.findViewById(R.id.message1_conent1);
           TextView message1_conent2=(TextView)convertView.findViewById(R.id.message1_conent2);
           Glide.with(context).load(messageData.getAdvicePic()).transform(new GlideCircleTransform(context)).into(message_pic);
           message3_time.setText(CommonUtil.getTimeto(Long.valueOf(messageData.getCreateTime())));
           message1_name.setText(messageData.getUserName());
           if (messageData.getType().equals("202")){
               message1_conent1.setText("赞了你在");
           }else {
               message1_conent1.setText("评论了你在");
           }
           message1_conent2.setText(messageData.getSceneName());
           lay_messageitem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(context, HereDetailActivity.class);
                   i.putExtra("hereId", messageData.getHereId());
                   context.startActivity(i);
               }
           });
           lay_messageitem.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   callBack.deleteMessage(messageData.getAdviceId(), position);
                   return true;
               }
           });
       }else if (messageData.getType().equals("102")||messageData.getType().equals("103")){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.item_unmessaged2, parent, false);
           ImageView message_pic=(ImageView)convertView.findViewById(R.id.message2_pic);
           TextView message2_name=(TextView)convertView.findViewById(R.id.message2_name);
           LinearLayout lay_messageitem=(LinearLayout)convertView.findViewById(R.id.lay_messageitem2);
           TextView message2_conent1=(TextView)convertView.findViewById(R.id.message2_conent1);
           TextView message2_time=(TextView)convertView.findViewById(R.id.message2_time);
           Glide.with(context).load(messageData.getAdvicePic()).transform(new GlideCircleTransform(context)).into(message_pic);
           message2_time.setText(CommonUtil.getTimeto(Long.valueOf(messageData.getCreateTime())));
           message2_name.setText(messageData.getUserName());
           if (messageData.getType().equals("103")){
               message2_conent1.setText("拒绝了你的好友申请");
           }else {
               message2_conent1.setText("同意了你的好友申请");
           }
           lay_messageitem.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   callBack.deleteMessage(messageData.getAdviceId(), position);
                   return true;
               }
           });
       }else if (messageData.getType().equals("101")){
           LayoutInflater inflater = LayoutInflater.from(parent.getContext());
           convertView = inflater.inflate(R.layout.item_unmessaged, parent, false);
           ImageView message_pic=(ImageView)convertView.findViewById(R.id.message_pic);
           TextView message_name=(TextView)convertView.findViewById(R.id.message_name);
           LinearLayout lay_messageitem=(LinearLayout)convertView.findViewById(R.id.lay_messageitem);
           TextView message_conent1=(TextView)convertView.findViewById(R.id.message_conent1);
           final TextView message_agreed=(TextView)convertView.findViewById(R.id.message_agreed);
           final TextView message_agreeded=(TextView)convertView.findViewById(R.id.message_agreeded);
           TextView message_time=(TextView)convertView.findViewById(R.id.message_time);
           Glide.with(context).load(messageData.getAdvicePic()).transform(new GlideCircleTransform(context)).into(message_pic);
           message_time.setText(CommonUtil.getTimeto(Long.valueOf(messageData.getCreateTime())));
           if (messageData.getIsClick().equals("0")){
               message_agreed.setVisibility(View.VISIBLE);
               message_agreeded.setVisibility(View.GONE);
           }else if (messageData.getIsClick().equals("1")){
               message_agreed.setVisibility(View.GONE);
               message_agreeded.setText("已同意");
               message_agreed.setClickable(false);
               message_agreeded.setVisibility(View.VISIBLE);
           }else {
               message_agreed.setVisibility(View.GONE);
               message_agreeded.setText("已拒绝");
               message_agreed.setClickable(false);
               message_agreeded.setVisibility(View.VISIBLE);
           }
           message_name.setText(messageData.getUserName());
           message_conent1.setText(messageData.getContent());
           lay_messageitem.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent i = new Intent(context, MessageDetailActivity.class);
                   i.putExtra("pic", messageData.getAdvicePic());
                   i.putExtra("type", messageData.getType());
                   SpfUtil.put(context, "agreededid", position);
                   i.putExtra("isClick",messageData.getIsClick());
                   i.putExtra("content", messageData.getContent());
                   i.putExtra("applyId", messageData.getUserId());
                   i.putExtra("time", messageData.getCreateTime());
                   context.startActivity(i);
               }
           });
           message_agreed.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   messageData.setIsClick("1");
                   message_agreed.setVisibility(View.GONE);
                   message_agreeded.setVisibility(View.VISIBLE);
                   callBack.updata(messageData.getUserId(), messageData.getYuntongid(),messageData.getType());
                   callBack.addFriends(messageData.getUserId(), position);
               }
           });
           lay_messageitem.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   callBack.deleteMessage(messageData.getAdviceId(), position);
                   return true;
               }
           });
       }
        return convertView;
    }
}
