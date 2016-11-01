package com.yuntong.here.activity;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yuntong.here.R;
import com.yuntong.here.base.BaseActivity;

/**
 * Created by Administrator on 2016/5/30.
 * 活动页
 */
public class WebViewActivity extends BaseActivity {

    private WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        setTitleElse("活动");
        webView=(WebView)findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //设置WebView属性，能够执行Javascript脚本
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setBuiltInZoomControls(true);//设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void widgetClick(View v) {

    }
}
