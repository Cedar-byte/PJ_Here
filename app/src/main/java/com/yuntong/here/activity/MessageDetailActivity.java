package com.yuntong.here.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/24.
 */
public class MessageDetailActivity extends BaseActivity {
    private TextView time;
    private TextView conent;
    private ImageView pic;
    private TextView  agreed;
    private TextView  refuce;
    private RequestQueue requestQueue;
    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_messagedetail;
    }

    @Override
    protected void initData() {
        requestQueue = MyApplication.getInstance().requestQueue;
    }

    @Override
    protected void initView() {
        agreed=(TextView)findViewById(R.id.agreed);
        refuce=(TextView)findViewById(R.id.refuce);
        agreed.setOnClickListener(this);
        refuce.setOnClickListener(this);
        if (getIntent().getStringExtra("type").equals("101")){
            setTitleElse("好友请求");
            agreed.setVisibility(View.VISIBLE);
            refuce.setVisibility(View.VISIBLE);
        }else {
            setTitleElse("系统消息");
            agreed.setVisibility(View.GONE);
            refuce.setVisibility(View.GONE);
        }
        if (getIntent().getStringExtra("isClick").equals("2")||getIntent().getStringExtra("isClick").equals("1")){
            agreed.setClickable(false);
            refuce.setClickable(false);
        }
        time=(TextView)findViewById(R.id.time);
        time.setText(CommonUtil.getTimeto(Long.valueOf(getIntent().getStringExtra("time"))));
        conent=(TextView)findViewById(R.id.conent);
        conent.setText(getIntent().getStringExtra("content"));
        pic=(ImageView)findViewById(R.id.pic);
        Glide.with(this).load(getIntent().getStringExtra("pic")).transform(new GlideCircleTransform(this)).into(pic);
    }

    @Override
    protected void bindView() {}

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.agreed://同意
                agreedAddFriend("1");
                break;
            case R.id.refuce:// 拒绝
                refuceAddFriend("2");
                break;
        }
    }

    private void agreedAddFriend(String status){
        Map<String, String> map = new HashMap<>();
        map.put("applyId",getIntent().getStringExtra("applyId") );
        map.put("status", status);
        Request<JSONObject> request = new NormalPostRequest(Constants.ADDFRIEND,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            SpfUtil.put(MessageDetailActivity.this,"agreeded","1");
                             finish();
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }

    private void refuceAddFriend(String status){
        Map<String, String> map = new HashMap<>();
        map.put("applyId",getIntent().getStringExtra("applyId") );
        map.put("status", status);
        Request<JSONObject> request = new NormalPostRequest(Constants.ADDFRIEND,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            SpfUtil.put(MessageDetailActivity.this,"agreeded","2");
                            finish();
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
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
