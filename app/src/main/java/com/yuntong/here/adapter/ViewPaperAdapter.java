package com.yuntong.here.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

public class ViewPaperAdapter extends PagerAdapter {

    List<View> viewLists;
    private Context context;

    public ViewPaperAdapter(List<View> lists)
    {
        viewLists = lists;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewLists.size();
    }

    @Override
    public void startUpdate(View view) {

    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {

    }

    @Override
    public void destroyItem(View view, int position, Object object)
    {
        ((ViewPager) view).removeView(viewLists.get(position));
    }

    @Override
    public void finishUpdate(View view) {

    }

    @Override
    public Object instantiateItem(View view, int position)
    {

        ((ViewPager) view).addView(viewLists.get(position), 0);
        viewLists.get(position).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

            }
        });
        return viewLists.get(position);
    }

}