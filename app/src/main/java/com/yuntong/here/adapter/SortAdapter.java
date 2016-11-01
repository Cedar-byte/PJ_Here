package com.yuntong.here.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.gxy.fastscrollrecyclerview.views.FastScrollRecyclerView;
import com.yuntong.here.R;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.pinyin.BubbleTextGetter;
import com.yuntong.here.util.sortlist.SortModel;
import com.yuntong.here.view.CircleImageView;
import com.yuntong.here.view.RoundImageView;

import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter{

	private List<SortModel> list;
	private Context context;

	public SortAdapter(Context context, List<SortModel> sourceDateList) {
		this.context = context;
		this.list = sourceDateList;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_friends,parent,false);
		return new ViewHolder(view, mOnItemClickListener, mOnItemLongClickListener);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.tvLetter.setVisibility(View.VISIBLE);
			holder.setLetterText(list.get(position).getSortLetters());
		}else{
			holder.tvLetter.setVisibility(View.INVISIBLE);
		}
		holder.setNameText(list.get(position).getNickname());
		Glide.with(context)
				.load(list.get(position).getHeadpic())
				.transform(new GlideCircleTransform(context))
				.into(holder.headImg);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < list.size(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@NonNull
	@Override
	public String getSectionName(int position) {
		return list.get(position).getSortLetters();
	}

	//自定义的ViewHolder，持有每个Item的的所有界面元素
	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

		TextView tvLetter;// 首字母
		TextView tvName;// 名字
		ImageView headImg;// 头像
		OnRecyclerViewItemClickListener clickListener;
		OnRecyclerViewItemLongClickListener longClickListener;

		public ViewHolder(View itemView,OnRecyclerViewItemClickListener clickListener,OnRecyclerViewItemLongClickListener longClickListener) {
			super(itemView);
			tvName = (TextView) itemView.findViewById(R.id.text_friends_name);
			headImg = (ImageView) itemView.findViewById(R.id.img_friends_head);
			tvLetter = (TextView) itemView.findViewById(R.id.text_zimu);
			this.clickListener = clickListener;
			this.longClickListener = longClickListener;
			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		public void setNameText(CharSequence text) {
			tvName.setText(text);
		}

		public void setLetterText(CharSequence text) {
			tvLetter.setText(text);
		}

		@Override
		public void onClick(View v) {
			if(clickListener != null){
				clickListener.onItemClick(v, getLayoutPosition());
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if(longClickListener != null){
				longClickListener.onItemLongClick(v, getLayoutPosition());
			}
			return true;
		}
	}

	public interface OnRecyclerViewItemClickListener {
		void onItemClick(View view,int postion);
	}

	public interface OnRecyclerViewItemLongClickListener {
		void onItemLongClick(View view , int position);
	}

	private OnRecyclerViewItemClickListener mOnItemClickListener = null;

	private OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
		this.mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener){
		this.mOnItemLongClickListener = listener;
	}
}