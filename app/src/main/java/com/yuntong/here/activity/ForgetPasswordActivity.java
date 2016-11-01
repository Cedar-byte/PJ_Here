package com.yuntong.here.activity;

import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;


import com.yuntong.here.R;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.TimeCount;
import org.json.JSONObject;
import android.os.Handler;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by me on 2016/4/25.
 * 忘记密码
 */
public class ForgetPasswordActivity extends BaseActivity implements TextWatcher {

    private EditText phoneEdt, verifyCodeEdt, newpasswordEdt;// (输入框) 手机号，验证码，新密码
    private Button commitBtn,getcodeBtn;// 提交，获取验证码
    private TimeCount timeCount;// 按钮计时
    private String phone,verifyCode,password;// 手机号，验证码，新密码
    private View view;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initData() {
        view = getLayoutInflater().inflate(R.layout.pop_hint, null);// 提示框显示时的布局
    }

    @Override
    protected void initView() {
        phoneEdt = (EditText) findViewById(R.id.edt_forgot_phone_number);
        verifyCodeEdt = (EditText) findViewById(R.id.edt_forgot_verifyCode);
        newpasswordEdt = (EditText) findViewById(R.id.edt_forgot_new_password);
        phoneEdt.addTextChangedListener(this);
        verifyCodeEdt.addTextChangedListener(this);
        newpasswordEdt.addTextChangedListener(this);
        getcodeBtn = (Button) findViewById(R.id.btn_forgot_get_verifyCode);
        commitBtn = (Button) findViewById(R.id.btn_commit);
    }

    @Override
    protected void bindView() {
        setTitle("重置密码");
        commitBtn.setEnabled(false);
        getcodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btn_forgot_get_verifyCode:// 获取验证码
                phone = phoneEdt.getText().toString();
                // 如果手机号没有输入就提示，并在1秒钟后提示自动消失
                if(phoneEdt.getText().toString().length() < 11 || !CommonUtil.isMobile(phone)){
                    CommonUtil.showMyToast(view,ForgetPasswordActivity.this,"请输入合法手机号",R.drawable.icon_phone_11);
                    return;
                }
                getVerifyCode(phone);
                break;
            case R.id.btn_commit:// 提交
                phone = phoneEdt.getText().toString();
                // 一级判断，手机号是否为空或者小于11个数或者大于11个数
                if(phoneEdt.getText().toString().equals("")
                        && verifyCodeEdt.getText().toString().equals("")
                        && newpasswordEdt.getText().toString().equals("")
                        || phoneEdt.getText().toString().length() < 11 || !CommonUtil.isMobile(phone)){
                    CommonUtil.showMyToast(view,ForgetPasswordActivity.this,"请输入合法手机号",R.drawable.icon_error_pwd_name);
                    return;
                }
                // 二级判断，已输入正确格式的手机号，但是没有输入验证码
                if(phoneEdt.getText().toString() != null
                        && phoneEdt.getText().toString().length() == 11
                        && verifyCodeEdt.getText().toString().equals("")
                        && newpasswordEdt.getText().toString().equals("")){
                    CommonUtil.showMyToast(view,ForgetPasswordActivity.this,"请输入验证码",R.drawable.icon_error_verifycode);
                    return;
                }
                // 二级判断，已输入正确格式的手机号和验证码，但是没有输入密码或者输入的密码不符合规则
                if(phoneEdt.getText().toString() != null
                        && verifyCodeEdt.getText().toString() != null
                        && newpasswordEdt.getText().toString().length() < 6){
                    CommonUtil.showMyToast(view,ForgetPasswordActivity.this,"请输入6-16位密码",R.drawable.icon_error_pwd);
                    return;
                }
                verifyCode = verifyCodeEdt.getText().toString();
                password = newpasswordEdt.getText().toString();
                onRetrieve(phone, verifyCode, password);// 提交（重置密码）
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode(String phone) {
        // 计时
        timeCount = new TimeCount(60000, 1000, getcodeBtn);
        timeCount.start();
        // 请求接口

    }

    /**
     * 提交（重置密码）
     * @param phone 手机号
     * @param verifyCode 验证码
     * @param password 新密码
     */
    private void onRetrieve(String phone, String verifyCode, String password) {
        // 请求接口

    }

    //==================================================================================
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(phoneEdt.getText().toString().length()>0 && verifyCodeEdt.getText().toString().length()>0 && newpasswordEdt.getText().length() > 0){
            commitBtn.setEnabled(true);
            commitBtn.setTextColor(getResources().getColor(R.color.white));// 为Button中的字体设置颜色
        }else{
            commitBtn.setEnabled(false);
            commitBtn.setTextColor(getResources().getColor(R.color.gray_white));// 为Button中的字体设置颜色
        }
    }
}
