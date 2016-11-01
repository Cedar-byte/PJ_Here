package com.yuntong.here.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.yuntong.here.R;
import com.yuntong.here.util.ImageLoaderUtil;

import java.util.ArrayList;


public class ImgAdapter extends BaseAdapter {
	private Context context;
	private int defaultResId;
	private ArrayList<String> arrayList;
	
	public int getDefaultResId() {
		return defaultResId;
	}

	public void setDefaultResId(int defaultResId) {
		this.defaultResId = defaultResId;
	}

	public ImgAdapter(Context context,ArrayList<String> arrayList) {
		super();
		this.context = context;
		this.arrayList=arrayList;

	}

	@Override
	public View getView(int pos, View view, ViewGroup parentView) {
		boolean bCreate = true;
		if (view != null) {
			int iPos = (Integer) view.getTag();
			if (iPos == pos) {
				bCreate = false;
			}
		}
		if (bCreate) {
			LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
			view = inflater.inflate(R.layout.list_msg, parentView, false);
			ImageView ad_img = (ImageView) view.findViewById(R.id.grid_image);
			int i=pos%(arrayList.size());
			String integer = arrayList.get(i);
			ImageLoaderUtil.disPlay(integer, ad_img);
			view.setTag(pos);
		}
		return view;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position % arrayList.size());
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
