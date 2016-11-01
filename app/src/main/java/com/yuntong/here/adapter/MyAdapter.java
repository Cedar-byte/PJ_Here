package com.yuntong.here.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.entity.HomeData;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.util.ImageLoaderUtil;

import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by me on 15/11/26.
 * 首页列表adapter
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {

    public List<SceneListData> arrayList = null;

//    private MyItemClickListener listener;

    /**
     * 定义接口 设置item中的Button的点击事件,方法暴露给Activity
     */
//    public interface MyItemClickListener{
//        public void onItemClick(int position);//
//    }

    public MyAdapter(List<SceneListData> arrayList) {
        this.arrayList = arrayList;
//        this.listener = listener;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_home,viewGroup,false);
        if(view != null){
            view.setOnClickListener(this);
        }
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        viewHolder.item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick(position);
//            }
//        });
        viewHolder.itemView.setTag(arrayList.get(position));
        viewHolder.nameText.setText(arrayList.get(position).getName());// 名字
        viewHolder.adressText.setText(arrayList.get(position).getAddress());// 地址
        double d = arrayList.get(position).getDistance();
        if(d< 1000&&d>-1){
            viewHolder.distanceText.setText(String.valueOf(d) + "m");// 距离
        }else if(d > 1000){
            double d2 = d/1000;
            viewHolder.distanceText.setText(new java.text.DecimalFormat("#.0").format(d2)+"km");// 距离
        }else {
            viewHolder.imss.setVisibility(View.GONE);
        }
        if(d< 500&&d>-1){
            viewHolder.hereImg.setVisibility(View.VISIBLE);
        }else{
            viewHolder.hereImg.setVisibility(View.GONE);
        }
        String hot = String.valueOf(arrayList.get(position).getHeat());
        viewHolder.hotText.setText(hot);// 热度
        ImageLoaderUtil.disPlay(arrayList.get(position).getThumb(),viewHolder.bgImg);
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView hereImg;// Here标识

        public TextView nameText;// 商家名字

        public TextView adressText;// 地址

        public TextView distanceText;// 距离

        public TextView hotText;// 热度

        public ImageView bgImg;// 背景图

        public LinearLayout imss;

        public RelativeLayout item;

        public ViewHolder(View view){
            super(view);
            hereImg = (ImageView) view.findViewById(R.id.img_here_green);
            nameText = (TextView) view.findViewById(R.id.item_home_list_name_txt);
            adressText = (TextView) view.findViewById(R.id.item_home_list_adress_txt);
            distanceText = (TextView) view.findViewById(R.id.item_distance);
            hotText = (TextView) view.findViewById(R.id.item_hot);
            bgImg = (ImageView) view.findViewById(R.id.img_item_bg);
            item = (RelativeLayout) view.findViewById(R.id.home_item);
            imss=(LinearLayout)view.findViewById(R.id.imss);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , SceneListData data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(SceneListData)v.getTag());
        }
    }
}
