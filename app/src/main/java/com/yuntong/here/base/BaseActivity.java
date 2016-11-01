package com.yuntong.here.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuntong.here.R;
import com.yuntong.here.app.AppManager;
import com.yuntong.here.util.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import butterknife.ButterKnife;

/**
 *
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener{

    Context c;// 上下文对象
    private boolean screenHorizontal;  //是否允许横屏
    private TextView textView, textView2;
    private RelativeLayout relativeLayout, relativeLayout2;
    protected Toast mToast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(screenHorizontal){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        AppManager.getInstance().addActivity(this);// 将本Activity添加到堆栈中
        c = this;

        // 设置界面
        int layoutId = getLayoutId();

        if (layoutId != -1) {
            setContentView(layoutId);
        }
        ButterKnife.bind(this);

        // 2.初始化数据
        initData();

        // 3.初始化控件（M）
        initView();

        // 4.为控件注册监听器(C)
        bindView();

        // 检查网络
        if(!isNetConnected()){
            ToastUtil.showToast(c,"网络连接异常，请检查网络");
            return;
        }
    }

    protected boolean isNetConnected(){
        if (c != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) c
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 布局文件ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 定义初始化数据的抽象方法
     */
    protected abstract void initData();

    /**
     * 定义初始化界面的抽象方法
     */
    protected abstract void initView();

    /**
     * 定义数据和控件发生关系的抽象方法
     */
    protected abstract void bindView();

    /**
     * 设置是否允许横屏
     * @param screenHorizontal
     */
    public void setScreenHorizontal(boolean screenHorizontal) {
        this.screenHorizontal = screenHorizontal;
    }

    /**
     * 定义控件的点击事件
     * @param v
     */
    protected abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * 设置标题的点击事件和显示的文字
     * @param s 显示的文字
     */
    public void setTitle(String s){
        textView = (TextView) findViewById(R.id.title_txt);
        textView.setText(s);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative_title);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 设置标题的点击事件和显示的文字
     * @param s 显示的文字
     */
    public void setTitleElse(String s){
        textView2 = (TextView) findViewById(R.id.title_txt_else);
        textView2.setText(s);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative_title_else);
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    protected void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

//    public <T extends View> T $(@IdRes int resId){
//        return (T)super.findViewById(resId);
//    }
    //BaseFragment
//    public <T extends View> T $(View layoutView, @IdRes int resId){
//        return (T)layoutView.findViewById(resId);
//    }
//  使用  TextView tvName=$(R.id.tv_name);

}
