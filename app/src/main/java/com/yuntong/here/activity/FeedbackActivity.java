package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by me on 2016/4/25.
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity implements TextWatcher {

    private Context context = FeedbackActivity.this;
    private EditText editText;
    private RelativeLayout back;
    private Button send;
    TextView tip;// 用来显示剩余字数
    private final int charMaxNum = 140; // 允许输入的字数
    private CharSequence temp; // 监听前的文本
    private int editStart; // 光标开始位置
    private int editEnd; // 光标结束位置
    private boolean isEdit;
    private String text, userId;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
        return R.layout.activity_feedback;
    }

    @Override
    protected void initData() {
        userId = (String) SpfUtil.get(context, "userId", "");
    }

    @Override
    protected void initView() {
        editText = (EditText) findViewById(R.id.edit_feedback);
        back = (RelativeLayout) findViewById(R.id.back);
        send = (Button) findViewById(R.id.send_feedback);
        tip = (TextView) findViewById(R.id.txt_num);
    }

    @Override
    protected void bindView() {
        send.setAlpha(0.3f);
        // 为两个输入框添加文字变化监听，用来判断Button的点击可否
        editText.addTextChangedListener(this);
        back.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                if (isEdit == true) {
                    showMyDialog();
                } else {
                    isEdit = false;
                    FeedbackActivity.this.finish();
                }
                break;
            case R.id.send_feedback:
                onSend();
                break;
            default:
                break;
        }
    }

    /**
     * 发送
     */
    private void onSend() {
        text = editText.getText().toString();
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("versionNum", "1.0");
        map.put("type", "1");
        map.put("userId", userId);
        map.put("text", text);
        Request<JSONObject> request = new NormalPostRequest(Constants.FEEDBACK_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            showToast("反馈信息已发送");
                            finish();
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

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.dialoglayout, null);
        Button exit = (Button) view.findViewById(R.id.exit_dialog);
        Button cancel = (Button) view.findViewById(R.id.cancel_dialog);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = false;
                dialog.dismiss();
                FeedbackActivity.this.finish();
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

    // 输入文本之前的状态
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }

    // 输入文字中的状态，count是一次性输入字符数
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tip.setText((s.length()) + "/" + charMaxNum);
    }

    // 输入文字后的状态
    @Override
    public void afterTextChanged(Editable s) {
        editStart = editText.getSelectionStart();
        editEnd = editText.getSelectionEnd();
        if (temp.length() > charMaxNum) {
//				Toast.makeText(getApplicationContext(), 最多输入140个字符, Toast.LENGTH_SHORT).show();
            s.delete(editStart - 1, editEnd);
            editText.setText(s);
            editText.setSelection(s.length());
        }
        if (editText.getText().toString().length() > 0) {
            isEdit = true;
        } else {
            isEdit = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
