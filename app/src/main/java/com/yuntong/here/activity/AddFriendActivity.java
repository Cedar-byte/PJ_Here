package com.yuntong.here.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/1.
 * 添加好友
 */
public class AddFriendActivity extends BaseActivity implements TextWatcher {

    @Bind(R.id.edit_feedback) EditText editFeedback;
    @Bind(R.id.txt_num) TextView txtNum;// 用来显示剩余字数
    @Bind(R.id.back) RelativeLayout back;
    @Bind(R.id.send1) Button send;
    //    private EditText editText;
//    private RelativeLayout back;
//    private Button send;
    //    TextView tip;// 用来显示剩余字数
    private final int charMaxNum = 50; // 允许输入的字数
    private CharSequence temp; // 监听前的文本
    private int editStart; // 光标开始位置
    private int editEnd; // 光标结束位置
    private boolean isEdit;
    private RequestQueue requestQueue;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_addfriend;
    }

    @Override
    protected void initData() {
        requestQueue = MyApplication.getInstance().requestQueue;
    }

    @Override
    protected void initView() {
//        editText = (EditText) findViewById(R.id.edit_feedback);
//        back = (RelativeLayout) findViewById(R.id.back);
//        send = (Button) findViewById(R.id.send1);
//        tip = (TextView) findViewById(R.id.txt_num);
//        back.setOnClickListener(this);
//        send.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        send.setAlpha(0.3f);
        // 为两个输入框添加文字变化监听，用来判断Button的点击可否
        editFeedback.addTextChangedListener(this);
    }

    @Override
    protected void widgetClick(View v) {
//        switch (v.getId()) {
//            case R.id.back:
////                AddFriendActivity.this.finish();
//                break;
//            case R.id.send1:
////                addFriend();
//                break;
//            default:
//                break;
//        }
    }


    // 输入文本之前的状态
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }

    // 输入文字中的状态，count是一次性输入字符数
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        txtNum.setText((s.length()) + "/" + charMaxNum);
    }

    // 输入文字后的状态
    @Override
    public void afterTextChanged(Editable s) {
        editStart = editFeedback.getSelectionStart();
        editEnd = editFeedback.getSelectionEnd();
        if (temp.length() > charMaxNum) {
//				Toast.makeText(getApplicationContext(), 最多输入140个字符, Toast.LENGTH_SHORT).show();
            s.delete(editStart - 1, editEnd);
            editFeedback.setText(s);
            editFeedback.setSelection(s.length());
        }
        if (editFeedback.getText().toString().length() > 0) {
            isEdit = true;
        } else {
            isEdit = false;
        }
    }


    private void addFriend() {
        Map<String, String> map = new HashMap<>();
        map.put("bapplyId", getIntent().getStringExtra("bapplyId"));
        map.put("applyId", (String) SpfUtil.get(AddFriendActivity.this, "userId", ""));
        map.put("content", editFeedback.getText().toString());
        Request<JSONObject> request = new NormalPostRequest(Constants.ADDFRIENDADD,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            finish();
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }, map);
        requestQueue.add(request);
    }

    @OnClick({R.id.back, R.id.send1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                AddFriendActivity.this.finish();
                break;
            case R.id.send1:
                addFriend();
                break;
        }
    }
}
