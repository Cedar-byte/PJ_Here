package com.yuntong.here.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.yuntong.here.R;
import com.yuntong.here.entity.OtherHereData;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.SpfUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/5/19.
 * 他人主页列表适配器
 */
public class OtherHereAdapter extends RecyclerView.Adapter<OtherHereAdapter.ViewHolder>{

    public List<OtherHereData> datalist = null;
    public Context context;
    private ItemClickListener listener;
    private String isFriend;

    /**
     * 定义接口 设置item中的Button的点击事件,方法暴露给Activity
     */
    public interface ItemClickListener{
        void lookUpHere(String hereId);// 查看here详情
    }

    public OtherHereAdapter(Context context, List<OtherHereData> datalist, ItemClickListener listener) {
        this.datalist = datalist;
        this.context = context;
        this.listener = listener;
        isFriend = (String) SpfUtil.get(context,"isFriendNum","");
//        Log.i("他人主页列表适配器", "OtherHereAdapter: " + isFriend);
//        if(isFriend.equals("1")){
//            Log.i("他人主页列表适配器", "OtherHereAdapter: " + "是好友");
//        }else if(isFriend.equals("0")){
//            Log.i("他人主页列表适配器", "OtherHereAdapter: " + "不是好友");
//        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_otherhere,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.sceneName.setText(datalist.get(position).getSceneName());
        holder.textall.setText(datalist.get(position).getText());
        holder.upvoteNum.setText(datalist.get(position).getUpvoteNum());
        holder.commentNum.setText(datalist.get(position).getCommentNum());
        String year = CommonUtil.getTimetoY(Long.valueOf(datalist.get(position).getCreateTime()));// 年
        String mounth = CommonUtil.getTimetoM(Long.valueOf(datalist.get(position).getCreateTime()));// 月
        String date = CommonUtil.getTimetoD(Long.valueOf(datalist.get(position).getCreateTime()));// 日
        String sysyear = String.valueOf(CommonUtil.getSysYear());// 获取系统年份
        String[] imgs = datalist.get(position).getHereMediaDTOList();// 得到照片url集合
        holder.imgNum.setText("共" + String.valueOf(imgs.length) + "张");// 显示照片张数
        if(imgs.length > 0){
            ImageLoaderUtil.disPlay(datalist.get(position).getFirstImg(),holder.photo);
        }else{
            holder.photo.setImageResource(R.drawable.icon_loding_small);
        }

        //根据position获取年月日
        String time_ymd = getSectionForPosition(position);

        //如果当前position等于年月日第一次出现出现的位置 ，则认为是第一次出现
        if(position == getPositionForSection(time_ymd)){
            holder.timeBg.setVisibility(View.VISIBLE);
            if(year.equals(sysyear)){// 如果当前年份等于系统年份就只显示月
                holder.year.setText(mounth);
            }else{
                holder.year.setText(year + mounth);
            }
            holder.date.setText(date);
        }else{
            holder.timeBg.setVisibility(View.INVISIBLE);
        }
        if(isFriend.equals("0") && datalist.size() > 5 && position == 4){
            holder.foot.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.GONE);
        }
        holder.here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.lookUpHere(datalist.get(position).getHereId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(isFriend.equals("0") && datalist.size() > 5){
            return 5;
        }else{
            return datalist.size();
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView sceneName;// 场景名称
        public TextView textall;// here内容
        public TextView upvoteNum;// 点赞数
        public TextView commentNum;// 评论数
        public TextView imgNum;// 照片张数
        public ImageView photo;// 显示的第一张照片
        public TextView year;// 年
        public TextView date;// 日
        public RelativeLayout timeBg;// 时间框
        public RelativeLayout foot;// 底部视图
        public TextView line;// 底线
        public Button here;// 点击查看here详情

        public ViewHolder(View view){
            super(view);
            sceneName = (TextView) view.findViewById(R.id.scene_name);
            textall = (TextView) view.findViewById(R.id.text);
            upvoteNum = (TextView) view.findViewById(R.id.upvoteNum);
            commentNum = (TextView) view.findViewById(R.id.commentNum);
            year = (TextView) view.findViewById(R.id.year_mounth);
            date = (TextView) view.findViewById(R.id.date);
            timeBg = (RelativeLayout) view.findViewById(R.id.timebg);
            foot = (RelativeLayout) view.findViewById(R.id.foot);
            line = (TextView) view.findViewById(R.id.line);
            here = (Button) view.findViewById(R.id.btn_here);
            imgNum = (TextView) view.findViewById(R.id.img_num);
            photo = (ImageView) view.findViewById(R.id.photo);
        }
    }

    /**
     * 根据position获取年月日
     */
    public String getSectionForPosition(int position) {
        return CommonUtil.getTimetoYMD(Long.valueOf(datalist.get(position).getCreateTime()));
    }

    /**
     * 根据年月日获取其第一次出现出现的位置
     */
    public int getPositionForSection(String time_ymd) {
        for (int i = 0; i < getItemCount(); i++) {
            String time = CommonUtil.getTimetoYMD(Long.valueOf(datalist.get(i).getCreateTime()));
            if (time.equals(time_ymd)) {
                return i;
            }
        }
        return -1;
    }
}
