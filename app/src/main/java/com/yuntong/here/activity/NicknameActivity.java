package com.yuntong.here.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 昵称
 */
public class NicknameActivity extends BaseActivity{

    private EditText nickEdt;
    private RelativeLayout back;
    private TextView save;
    private String nickname, nicm;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_nickname;
    }

    @Override
    protected void initData() {
        nickname = getIntent().getStringExtra("nickname");
    }

    @Override
    protected void initView() {
        nickEdt = (EditText) findViewById(R.id.edit_nickname);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        save = (TextView) findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        nickEdt.setText(nickname);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.back:
                NicknameActivity.this.finish();
                break;
            case R.id.save:
                onSave();
                break;
        }
    }

    /**
     * 更改昵称
     */
    private void onSave() {
        nicm = nickEdt.getText().toString();
        if(nicm.equals(nickname)){
            showToast("当前没有做任何更改");
        }else{
            RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
            Map<String, String> map = new HashMap<>();
            map.put("nickname",nicm);
            Request<JSONObject> request = new NormalPostRequest(Constants.PERSONALINFO_URL,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                                SpfUtil.put(NicknameActivity.this,"nickname",nicm);
                                finish();
                            } else {
                                ToastUtil.showToast(NicknameActivity.this, JsonUtil.getStr(jsonObject, "error"));
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
}
