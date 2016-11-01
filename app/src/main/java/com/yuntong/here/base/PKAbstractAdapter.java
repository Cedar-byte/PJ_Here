package com.yuntong.here.base;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;



/**
 * 通用适配器相关
 * @Mark 数据处理
 */
public abstract class PKAbstractAdapter<T> extends BaseAdapter {

    protected final List<T> mData;
    protected final Context mContext;

    public PKAbstractAdapter(Context context) {
        this(context, null);
    }

    public PKAbstractAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<T> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addData(List<T> data) {
        if (data != null && data.size() > 0) {
            mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addData(T... datas) {
        if (datas != null && datas.length > 0) {
            for (T data : datas) {

                mData.add(data);
            }
            notifyDataSetChanged();
        }
    }

    public void addData(T data, int position) {

        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void removeData(T data) {
        if (data != null && mData.contains(data)) {
            mData.remove(data);
            notifyDataSetChanged();
        }
    }


}
