package com.yuntong.here.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 注册 第一步
 */
public class RegisterFirstActivity extends BaseActivity implements TextWatcher {

    private LinearLayout linearLayout;
    private ImageView imageView;
    private TextView agreementText, txt1;// 用户协议
    private EditText phoneEditText;
    private Button getverifyCodeButton;
    private Intent intent;
    private String phone;// 获取输入的手机号
    private View view, views;
    private boolean isSelect = true;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        return R.layout.activity_register_first;
    }

    @Override
    protected void initData() {
        popupWindow = new PopupWindow();
        intent = new Intent();
        view = getLayoutInflater().inflate(R.layout.pop_hint, null);// Toast提示框显示时的布局
        views = getLayoutInflater().inflate(R.layout.pop_hint_big, null);// Toast提示框显示时的布局
    }

    @Override
    protected void initView() {
        agreementText = (TextView) findViewById(R.id.txt_user_agreement);
        txt1 = (TextView) findViewById(R.id.txt1);
        getverifyCodeButton = (Button) findViewById(R.id.btn_get_verifyCode);
        phoneEditText = (EditText) findViewById(R.id.edt_register_phone_number);
        linearLayout = (LinearLayout) findViewById(R.id.linear_gou);
        imageView = (ImageView) findViewById(R.id.img_gou);
    }

    @Override
    protected void bindView() {
        setTitle("注册");
        getverifyCodeButton.setEnabled(false);// 目前程序进来就要设置，否则后面的文字变化监听没有用
        getverifyCodeButton.setOnClickListener(this);
        agreementText.setOnClickListener(this);
        txt1.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        phoneEditText.addTextChangedListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            // 跳转到下一界面获取验证码
            case R.id.btn_get_verifyCode:
                getverifyCode();
                break;
            // 点击勾选
            case R.id.txt1:
            case R.id.linear_gou:
                if (isSelect) {
                    imageView.setVisibility(View.INVISIBLE);
                    isSelect = false;
                } else {
                    imageView.setVisibility(View.VISIBLE);
                    isSelect = true;
                }
                break;
            // 用户协议
            case R.id.txt_user_agreement:
                intent.setClass(this, UserAgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void getverifyCode() {
        getverifyCodeButton.setClickable(false);
        phone = phoneEditText.getText().toString();
        // 如果当前输入的手机号没有11位则弹出错误提示框
        if (phoneEditText.getText().toString().length() < 11 || !CommonUtil.isMobile(phone)) {
            CommonUtil.showMyToast(view, RegisterFirstActivity.this, getResources().getString(R.string.please_edit_phone), R.drawable.icon_phone_11);
            getverifyCodeButton.setClickable(true);
            return;
        }
        // 如果没有勾选同意用户协议，弹出错误提示框
        if (isSelect == false) {
            CommonUtil.showPopupWindow(views, popupWindow, getResources().getString(R.string.choice_agreement));
            getverifyCodeButton.setClickable(true);
            return;
        }
        // 请求验证码接口
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("type", "1");
        Request<JSONObject> request = new NormalPostRequest(Constants.GETVERIFYCODE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (JsonUtil.getStr(response, "status").equals("1")) {
                            intent.setClass(RegisterFirstActivity.this, RegisterSecondActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        }else if (JsonUtil.getStr(response, "status").equals("2")){
                            if (JsonUtil.getStr(JsonUtil.getJsonObj(response,"error"),"code").equals("10012")){
                                showRegisterWindow(popupWindow);
                            }else if (JsonUtil.getStr(JsonUtil.getJsonObj(response,"error"),"code").equals("10018")){
                                CommonUtil.showMyToast(view, RegisterFirstActivity.this, getResources().getString(R.string.triargin), R.drawable.icon_phone_11);
                            }
                            getverifyCodeButton.setClickable(true);
                        }else {
                            getverifyCodeButton.setClickable(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getverifyCodeButton.setClickable(true);
            }
        }, map);
        requestQueue.add(request);
    }

//=====================================================================================

    /**
     * Edittext文字状态变化监听(当Edittext为空时设置Button不可点击)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (phoneEditText.getText().toString().length() > 0) {
            getverifyCodeButton.setEnabled(true);
            getverifyCodeButton.setTextColor(getResources().getColor(R.color.white));// 为Button中的字体设置颜色
        } else {
            getverifyCodeButton.setEnabled(false);
            getverifyCodeButton.setTextColor(getResources().getColor(R.color.gray_white));
        }
    }

    private   void showRegisterWindow(final PopupWindow popupWindow) {
        View view5=getLayoutInflater().from(this).inflate(R.layout.popuwindow_registered,null);
        TextView textView = (TextView) view5.findViewById(R.id.txt_pop_big_cancel);
        TextView textView2 = (TextView) view5.findViewById(R.id.txt_pop_big_confirm1);
        popupWindow.setContentView(view5);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(view5, Gravity.CENTER, 0, 0);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });
    }
}
