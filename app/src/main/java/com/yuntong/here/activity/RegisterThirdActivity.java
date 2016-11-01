package com.yuntong.here.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.yuntong.here.MainActivity;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;

import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;


import com.yuntong.here.util.NormalPostRequest;

import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 注册 第三步
 */
public class RegisterThirdActivity extends BaseActivity implements TextWatcher {

    private EditText passwordEdt;
    private Button completeBtn;
    private String phone, password, verifyCode;
    private Context context = RegisterThirdActivity.this;
    private View view;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        return R.layout.activity_register_third;
    }

    @Override
    protected void initData() {
        phone = getIntent().getStringExtra("phone");
        verifyCode = getIntent().getStringExtra("verifyCode");
        view = getLayoutInflater().inflate(R.layout.pop_hint, null);// Toast提示框显示时的布局
    }

    @Override
    protected void initView() {
        passwordEdt = (EditText) findViewById(R.id.edt_register_password);
        completeBtn = (Button) findViewById(R.id.btn_complete_register);
    }

    /**
     * 注册 ---> 静默登录 ---> 完善资料
     */
    @Override
    protected void bindView() {
        setTitle("注册");
        completeBtn.setEnabled(false);// 目前程序进来就要设置，否则后面的文字变化监听没有用
        completeBtn.setOnClickListener(this);
        passwordEdt.addTextChangedListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btn_complete_register:
                if(passwordEdt.getText().toString().length() >= 6){
                    onRegister();
                }else{
                    CommonUtil.showMyToast(view,RegisterThirdActivity.this,"请输入6-16位密码",R.drawable.icon_error_pwd);
                    return;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 注册
     */
    private void onRegister() {
        password = passwordEdt.getText().toString();
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("name",phone);
        map.put("password",password);
        map.put("code",verifyCode);
        map.put("deviceType","1");
        Request<JSONObject> request = new NormalPostRequest(Constants.REGISTER_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            // 存放用户信息到Shared
                            SpfUtil.put(RegisterThirdActivity.this,"name",phone);
                            SpfUtil.put(RegisterThirdActivity.this,"password",password);
                            SpfUtil.put(RegisterThirdActivity.this,"userId",JsonUtil.getStr(jsonObject,"model"));
//                            toLogiin();
                            Intent i = new Intent(RegisterThirdActivity.this,PersonalInfoEditActivity.class);
                            startActivity(i);
                        } else {
                            ToastUtil.showToast(RegisterThirdActivity.this, JsonUtil.getStr(jsonObject, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }

    /**
     * 注册成功后--->登录
     */
    private void toLogin() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("name",phone);
        map.put("password",password);
        Request<JSONObject> request = new NormalPostRequest(Constants.LOGIN_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            // 登录成功后--->完善资料
                            Intent i = new Intent(RegisterThirdActivity.this,PersonalInfoEditActivity.class);
                            startActivity(i);
                        } else {
                            ToastUtil.showToast(RegisterThirdActivity.this, JsonUtil.getStr(jsonObject, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }

    //=====================================================================================
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(passwordEdt.getText().toString().length()>0){
            completeBtn.setEnabled(true);
            completeBtn.setTextColor(getResources().getColor(R.color.white));// 为Button中的字体设置颜色
        }else{
            completeBtn.setEnabled(false);
            completeBtn.setTextColor(getResources().getColor(R.color.gray_white));// 为Button中的字体设置颜色
        }
    }
}
