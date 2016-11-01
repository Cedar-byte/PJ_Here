package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.yuntong.here.MainActivity;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.service.UpdateService;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.DataCleanManager;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 设置
 */
public class SettingActivity extends BaseActivity{

    private Context context = SettingActivity.this;
    private CheckBox music,gravity;// 背景音乐、重力感应
    // 版本更新、意见反馈、关于我们、用户协议、修改密码、清除缓存、退出当前账号
    private Button versionUpdate,feedback,aboutUs,userAgreement,modifyPwd,clearCache,exit;
    private Intent intent;
    private TextView cache;
    private long a,a2;// 缓存大小
    boolean isMusicOpen,isGravityOpen;
    boolean isLogin;
    private RelativeLayout exitRel;
    private View view;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        intent = new Intent();
        try {
            a = DataCleanManager.getFolderSize(new File("/storage/emulated/0/com.yuntong.here/cache"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        isMusicOpen = (boolean) SpfUtil.get(context,"isMusicOpen",false);
        isGravityOpen = (boolean) SpfUtil.get(context,"isGravityOpen",false);
        isLogin = (boolean) SpfUtil.get(context,"isLogin",false);
    }

    @Override
    protected void initView() {
        view=getLayoutInflater().from(this).inflate(R.layout.pop_hint,null);
        music = (CheckBox) findViewById(R.id.checkbox_music);
        gravity = (CheckBox) findViewById(R.id.checkbox_gravity);
        versionUpdate = (Button) findViewById(R.id.btn_version_update);
        versionUpdate.setOnClickListener(this);
        feedback = (Button) findViewById(R.id.btn_feedback);
        feedback.setOnClickListener(this);
        aboutUs = (Button) findViewById(R.id.btn_about_us);
        aboutUs.setOnClickListener(this);
        userAgreement = (Button) findViewById(R.id.btn_user_agreement);
        userAgreement.setOnClickListener(this);
        modifyPwd = (Button) findViewById(R.id.btn_modify_pwd);
        modifyPwd.setOnClickListener(this);
        clearCache = (Button) findViewById(R.id.btn_clear_cache);
        clearCache.setOnClickListener(this);
        exit = (Button) findViewById(R.id.btn_exit);
        exit.setOnClickListener(this);
        cache = (TextView) findViewById(R.id.cacha_txt);
        exitRel = (RelativeLayout) findViewById(R.id.exit);
    }

    @Override
    protected void bindView() {
        setTitleElse("设置");
        cache.setText("清除缓存" + "(" + DataCleanManager.getFormatSize(a) + ")");
        if(isMusicOpen){
            music.setChecked(true);
        }else{
            music.setChecked(false);
        }
        if(isGravityOpen){
            gravity.setChecked(true);
        }else{
            gravity.setChecked(false);
        }
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpfUtil.put(context,"isMusicOpen",true);
                }else{
                    SpfUtil.put(context,"isMusicOpen",false);
                }
            }
        });
        gravity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SpfUtil.put(context,"isGravityOpen",true);
                }else{
                    SpfUtil.put(context,"isGravityOpen",false);
                }
            }
        });
        if(isLogin == false){
            exitRel.setVisibility(View.GONE);
        }else if(isLogin == true){
            exitRel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
           // 版本升级
            case R.id.btn_version_update:
                getUpdata();
                break;
            // 意见反馈
            case R.id.btn_feedback:
                intent.setClass(context, FeedbackActivity.class);
                startActivity(intent);
                break;
            // 关于我们
            case R.id.btn_about_us:
                intent.setClass(context, AboutUsActivity.class);
                startActivity(intent);
                break;
            // 用户协议
            case R.id.btn_user_agreement:
                intent.setClass(context, UserAgreementActivity.class);
                startActivity(intent);
                break;
            // 修改密码
            case R.id.btn_modify_pwd:
                if(isLogin == false){
                    intent.setClass(context, LoginActivity.class);
                }else{
                    intent.setClass(context, ChangePasswordActivity.class);
                }
                startActivity(intent);
                break;
            // 清除缓存
            case R.id.btn_clear_cache:
                DataCleanManager.cleanInternalCache(this);
                DataCleanManager.cleanCustomCache("/storage/emulated/0/com.yuntong.here/cache");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            a2 = DataCleanManager.getFolderSize(new File("/storage/emulated/0/com.yuntong.here/cache"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                String bs = DataCleanManager.getFormatSize(a2);
                cache.setText("清除缓存" + "(" + bs + ")");
                View view = getLayoutInflater().inflate(R.layout.cleanup_bg, null);// Toast提示框显示时的布局
                CommonUtil.showMyToast(view,context,getResources().getString(R.string.clean_cache_success),R.drawable.icon_cache_cleanup);
                break;
            // 退出登录
            case R.id.btn_exit:
                showMyDialog();
                break;
            default:
                break;
        }
    }

    private void onLoginOut() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Request<JSONObject> request = new NormalPostRequest(Constants.LOGINOUT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            SpfUtil.put(context,"isLogin",false);
                            SpfUtil.remove(context,"gender");
                            SpfUtil.remove(context,"headPic");
                            SpfUtil.remove(context,"email");
                            SpfUtil.remove(context,"nickname");
                            SpfUtil.remove(context,"name");
                            finish();
                        } else {


                            ToastUtil.showToast(context, JsonUtil.getStr(jsonObject, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, null);
        requestQueue.add(request);
    }

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.dialog_loginout, null);
        Button exit = (Button) view.findViewById(R.id.exit_dialog);
        Button cancel = (Button) view.findViewById(R.id.cancel_dialog);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginOut();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = (boolean) SpfUtil.get(context,"isLogin",false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isLogin == false) {
                    exitRel.setVisibility(View.GONE);
                } else if (isLogin == true) {
                    exitRel.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getUpdata() {
        SpfUtil.put(SettingActivity.this,"isUpdata",false);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        Map<String, String> map = new HashMap<>();
        map.put("versionNum", "1.0");
        map.put("type", "1");
        Request<JSONObject> request = new NormalPostRequest(Constants.UPDATA_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONObject jsonObject1=JsonUtil.getJsonObj(jsonObject,"model");
                            if (JsonUtil.getStr(jsonObject1,"lastVersionNum").equals("1.1")){
                                 CommonUtil.showMyToast(view,SettingActivity.this,"当前没有新版本",R.drawable.icon_comment_success);
                            }else {
                                showUpDataPopupWindow();
                            }
                        } else {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        mQueue.add(request);
    }

    public void showUpDataPopupWindow() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(SettingActivity.this).inflate(R.layout.popuwindow_updata, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel_updata);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView invest_rightnow = (TextView) view.findViewById(R.id.updata);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, UpdateService.class);
                intent.putExtra("App_Name", "here");
//                intent.putExtra("url", (String)SpfUtil.get(MainActivity.this,"upUrl",""));
                intent.putExtra("url", "http://hot.m.shouji.360tpcdn.com/160520/60bf7f1642995b6c8c81706c67daee14/com.qihoo.appstore_300050113.apk");
                startService(intent);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
}
