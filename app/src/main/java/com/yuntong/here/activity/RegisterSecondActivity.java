package com.yuntong.here.activity;

import android.content.Context;
import android.content.Intent;
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
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.TimeCount;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by me on 2016/4/25.
 * 注册 第二步
 */
public class RegisterSecondActivity extends BaseActivity implements TextWatcher {

    private Context context = RegisterSecondActivity.this;
    private EditText verifyCodeEditText;
    private Button sendverifyCodeButton,timeCountButton;
    private Intent intent;
    private TimeCount timeCount;
    private String phone, verifyCode;
    private View view;
    private TextView promptText;
    RequestQueue requestQueue;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        return R.layout.activity_register_second;
    }

    @Override
    protected void initData() {
        requestQueue = MyApplication.getInstance().requestQueue;
        intent = new Intent();
        phone = getIntent().getStringExtra("phone");// 获取上一个界面传递过来的手机号
        view = getLayoutInflater().inflate(R.layout.pop_hint, null);// Toast提示框显示时的布局
    }

    @Override
    protected void initView() {
        verifyCodeEditText = (EditText) findViewById(R.id.edt_register_verifyCode);
        sendverifyCodeButton = (Button) findViewById(R.id.btn_send_verifyCode);
        timeCountButton = (Button) findViewById(R.id.btn_timecount);
        promptText = (TextView) findViewById(R.id.txt_prompt);
    }

    @Override
    protected void bindView() {
        setTitle("注册");
        promptText.setText(getResources().getString(R.string.verifyCode_to_number) + phone);
        sendverifyCodeButton.setEnabled(false);// 目前程序进来就要设置，否则后面的文字变化监听没有用
        timeCount = new TimeCount(60000, 1000, timeCountButton);
        timeCount.start();
        timeCountButton.setOnClickListener(this);
        sendverifyCodeButton.setOnClickListener(this);
        verifyCodeEditText.addTextChangedListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            // 提交验证码
            case R.id.btn_send_verifyCode:
                if(verifyCodeEditText.getText().toString().length() == 6){
                    verifyCode = verifyCodeEditText.getText().toString();
                    onCode();
                }else{
                    CommonUtil.showMyToast(view,context,"请输入6位数验证码",R.drawable.icon_error_pwd_name);
                }
                break;
            case R.id.btn_timecount:
                timeCount.start();
                getverifyCode();// 获取验证码
                break;
        }
    }

    private void getverifyCode() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("type", "1");
        Request<JSONObject> request = new NormalPostRequest(Constants.GETVERIFYCODE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    private void onCode() {
        Map<String, String> map = new HashMap<>();
        map.put("name", phone);
        map.put("code", verifyCode);
        Request<JSONObject> request = new NormalPostRequest(Constants.YANZHENGCODE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // 验证码正确
                        if(JsonUtil.getStr(jsonObject, "status").equals("1")){
                            intent.setClass(RegisterSecondActivity.this, RegisterThirdActivity.class);
                            intent.putExtra("phone", phone);
                            intent.putExtra("verifyCode", verifyCode);
                            startActivity(intent);
                        }
                        // 验证码超时
                        if (JsonUtil.getStr(jsonObject, "status").equals("2")) {
                            JSONObject object1 = JsonUtil.getJsonObj(jsonObject, "error");
                            String msg = JsonUtil.getStr(object1,"message");
                            CommonUtil.showMyToast(view,context,msg + "!",R.drawable.icon_error_verifycode);
                        }
                        // 验证码错误
                        if(JsonUtil.getStr(jsonObject, "status").equals("3")){
                            JSONObject object2 = JsonUtil.getJsonObj(jsonObject, "error");
                            String msg = JsonUtil.getStr(object2,"message");
                            CommonUtil.showMyToast(view,context,msg + "!",R.drawable.icon_error_verifycode);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    //=====================================================================================
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if(verifyCodeEditText.getText().toString().length()>0){
            sendverifyCodeButton.setEnabled(true);
            sendverifyCodeButton.setTextColor(getResources().getColor(R.color.white));// 为Button中的字体设置颜色
        }else{
            sendverifyCodeButton.setEnabled(false);
            sendverifyCodeButton.setTextColor(getResources().getColor(R.color.gray_white));// 为Button中的字体设置颜色
        }
    }
}
