package com.yuntong.here.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity{

    private Context context = ChangePasswordActivity.this;
    private EditText oldPwd_Edt,newPwd_Edt,confirmPwd_Edt;
    private Button confirmBtn;
    private String oldPwd,newPwd,confirmPwd;
    private View view;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_change_password;
    }

    @Override
    protected void initData() {
        view = getLayoutInflater().inflate(R.layout.pop_hint, null);
    }

    @Override
    protected void initView() {
        oldPwd_Edt = (EditText) findViewById(R.id.edt_old_pwd);
        newPwd_Edt= (EditText) findViewById(R.id.edt_new_pwd);
        confirmPwd_Edt = (EditText) findViewById(R.id.edt_confirm_pwd);
        confirmBtn = (Button) findViewById(R.id.btn_confirm);
        confirmBtn.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        setTitleElse("修改密码");
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                onRetrieve();
                break;
        }
    }

    private void onRetrieve(){
        oldPwd = oldPwd_Edt.getText().toString();
        newPwd = newPwd_Edt.getText().toString();
        confirmPwd = confirmPwd_Edt.getText().toString();
        if(!newPwd.equals(confirmPwd)){
            CommonUtil.showMyToast(view,context,getResources().getString(R.string.different_pwd),R.drawable.icon_pwd_diffrernt);
            return;
        }
        if(oldPwd_Edt.getText().toString().length() < 6
                || newPwd_Edt.getText().toString().length() < 6
                || confirmPwd_Edt.getText().toString().length() < 6){
            CommonUtil.showMyToast(view,context,getResources().getString(R.string.num_pwd2),R.drawable.icon_error_pwd);
            return;
        }

        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("oldPwd",oldPwd);
        map.put("newPwd",newPwd);
        Request<JSONObject> request = new NormalPostRequest(Constants.MODIFYPWD_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            Intent intent = new Intent();
                            intent.setClass(context, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            JSONObject object = JsonUtil.getJsonObj(jsonObject,"error");
                            String ero = JsonUtil.getStr(object,"message");
                            CommonUtil.showMyToast(view,context,ero + "！",R.drawable.icon_error_pwd_name);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }
}
