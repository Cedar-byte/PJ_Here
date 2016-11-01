package com.yuntong.here.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuntong.here.R;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.StatusBarUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by me on 2016/4/25.
 * 关于我们
 */
public class AboutUsActivity extends BaseActivity {

    // 版本号、内容
    private TextView version, txt;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_about_us;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        version = (TextView) findViewById(R.id.txt_version_aboutus);
        txt = (TextView) findViewById(R.id.txt_about_us);
    }

    @Override
    protected void bindView() {
        setTitleElse("关于我们");
    }

    @Override
    protected void widgetClick(View v) {

    }
}
