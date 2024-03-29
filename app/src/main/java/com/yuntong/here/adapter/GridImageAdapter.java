package com.yuntong.here.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuntong.here.R;

import java.io.File;
import java.util.ArrayList;

public class GridImageAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<String> images;
    
	public GridImageAdapter(Context context, ArrayList<String> images) {
		this.context = context;
		this.images = images;
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_edit, parent,false);
			holder = new ViewHolder();
			holder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String path;// 当前图片的路径
		if (images != null && position < images.size()){
			path = images.get(position);
		}else{
			path = "camera_default";
		}
		// 如果当前图片的路径为"camera_default"，则显示"add"图片
		if (path.contains("camera_default")){
			holder.imageview.setImageResource(R.drawable.add);
		}else{// 否则显示正常的图片
			Glide.with(context)
					.load(new File(images.get(position)))
					.centerCrop()
					.into(holder.imageview);
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView imageview;
	}
}
