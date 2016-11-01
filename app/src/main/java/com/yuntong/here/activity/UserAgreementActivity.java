package com.yuntong.here.activity;

import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.yuntong.here.R;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.StatusBarUtil;

/**
 * Created by me on 2016/4/25.
 * 用户协议
 */
public class UserAgreementActivity extends BaseActivity{

    private RelativeLayout titleRel;// 顶部标题栏所在的布局

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        titleRel = (RelativeLayout) findViewById(R.id.title_rel_all);
    }

    @Override
    protected void bindView() {
        titleRel.setBackgroundResource(R.color.green);// 为标题栏设置背景颜色
        setTitle(getResources().getString(R.string.user_agreement_title));
    }

    @Override
    protected void widgetClick(View v) {

    }
}
