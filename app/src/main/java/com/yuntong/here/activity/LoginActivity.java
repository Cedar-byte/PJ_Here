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
import com.yuntong.here.MainActivity;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;

import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.ToastUtil;

import com.yuntong.here.util.NormalPostRequest;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements TextWatcher {

    private EditText numberEditText, passwordEditText;// 手机号和密码输入框
    private String number, password;
    private Button loginButton, registerButton;
    private TextView forgotText;
    private Intent intent;
    private Context context = LoginActivity.this;
    private View view;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        intent = new Intent();
        view = getLayoutInflater().inflate(R.layout.pop_hint, null);// 自定义Toast错误提示框的布局文件
    }

    @Override
    protected void initView() {
        numberEditText = (EditText) findViewById(R.id.edt_phone_number);
        passwordEditText = (EditText) findViewById(R.id.edt_password);
        loginButton = (Button) findViewById(R.id.btn_login);
        registerButton = (Button) findViewById(R.id.btn_register);
        forgotText = (TextView) findViewById(R.id.text_forgot_password);
    }

    @Override
    protected void bindView() {
        setTitle("");
        loginButton.setEnabled(false);// 目前程序进来就要设置，否则后面的文字变化监听没有用
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        forgotText.setOnClickListener(this);
        // 为两个输入框添加文字变化监听，用来判断Button的点击可否
        numberEditText.addTextChangedListener(this);
        passwordEditText.addTextChangedListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:// 登录
                number = numberEditText.getText().toString();
                if (numberEditText.getText().toString().length() < 11 || !CommonUtil.isMobile(number)) {
                    CommonUtil.showMyToast(view, LoginActivity.this, "请输入合法手机号", R.drawable.icon_error_pwd_name);// 显示错误提示框
                    return;
                }
                doLogin(number);
                break;
            case R.id.text_forgot_password:// 忘记密码
                intent.setClass(context, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:// 注册
                intent.setClass(context, RegisterFirstActivity.class);
                startActivity(intent);
                break;
        }

    }

    /**
     * 登录
     */
    private void doLogin(String number) {
        password = passwordEditText.getText().toString();
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("name", number);
        map.put("password", password);
        map.put("deviceType","1");
        Request<JSONObject> request = new NormalPostRequest(Constants.LOGIN_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            Log.i("--------", jsonObject.toString());
                            getData(jsonObject);// 获取接口返回的数据
                            // 获取个人资料信息
                            JSONObject jsonObject1 = JsonUtil.getJsonObj(jsonObject, "model");
                            String status=JsonUtil.getStr(jsonObject1,"status");
                            // 2、没有完善资料
                            if (status.equals("2")){
                                Intent i = new Intent(LoginActivity.this,PersonalInfoEditActivity.class);
                                startActivity(i);
                            }else {
                                String gender = JsonUtil.getStr(jsonObject1, "gender");
                                String headPic = JsonUtil.getStr(jsonObject1, "headPic");
                                String nickname = JsonUtil.getStr(jsonObject1, "nickname");
                                String name = JsonUtil.getStr(jsonObject1, "name");
                                String userid = JsonUtil.getStr(jsonObject1, "userId");
                                SpfUtil.put(context, "gender", gender);// 性别
                                SpfUtil.put(context, "headPic", headPic);// 头像地址
                                SpfUtil.put(context, "nickname", nickname);// 昵称
                                SpfUtil.put(context, "name", name);// 电话号码
                                SpfUtil.put(context, "isLogin", true);
                                SpfUtil.put(context, "userId", userid);// id
                                JPushInterface.setAlias(LoginActivity.this, "a"+userid, null);
                                LoginActivity.this.finish();
                            }
                        } else {
                            ToastUtil.showToast(context, JsonUtil.getStr(jsonObject, "error"));
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
     * 获取接口返回的数据
     *
     * @param jsonObject 返回的json数据
     */
    private void getData(JSONObject jsonObject) {
        // 如果返回error
        if (JsonUtil.getStr(jsonObject, "status").equals("2")) {
            JSONObject object = JsonUtil.getJsonObj(jsonObject, "error");
            String msg = JsonUtil.getStr(object, "message");
            if (msg.equals("该用户不存在")) {
                CommonUtil.showMyToast(view, LoginActivity.this, msg, R.drawable.icon_phone_not);// 显示错误提示框
                return;
            }
            CommonUtil.showMyToast(view, LoginActivity.this, "账号或密码错误！", R.drawable.icon_error_pwd_name);// 显示错误提示框
        }
    }

    // =================================EditText的文字变化的监听，三个方法=============================
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (numberEditText.getText().toString().length() > 0 && passwordEditText.getText().toString().length() > 0) {
            loginButton.setEnabled(true);
            loginButton.setTextColor(getResources().getColor(R.color.white));// 为Button中的字体设置颜色
        } else {
            loginButton.setEnabled(false);
            loginButton.setTextColor(getResources().getColor(R.color.gray_white));// 为Button中的字体设置颜色
        }
    }
}
