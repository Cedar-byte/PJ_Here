package com.yuntong.here.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.yuntong.here.R;

import java.util.ArrayList;

/**
 * Created by me on 2016/5/10.
 * 场景编辑、显示选择的照片的RecyclerView的适配器
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<String> paths;

    public PhotoAdapter(Context context, ArrayList<String> paths) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.paths = paths;
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_recycler_grid, null);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.ViewHolder holder, int position) {
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        if(position < paths.size()){
            Glide.with(context)
                    .load(paths.get(position))
                    .centerCrop()
                    .into(holder.imageView);
        }else{
            holder.imageView.setImageResource(R.drawable.add);
        }
        if(position == 9){
            holder.imageView.setVisibility(View.GONE);
            holder.itemView.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return paths == null ? 1 : paths.size() + 1;//返回数目加1
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_recycler_select_image);
        }
    }

    private OnRecyclerItemClickListener listener = null;

    public void setOnItemClickListener(OnRecyclerItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnRecyclerItemClickListener {
        void onItemClick(View view ,int position);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            //注意这里使用getTag方法获
            // 取数据
            listener.onItemClick(v, (Integer) v.getTag());
        }
    }
}
