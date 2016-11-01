package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GlideCircleTransform;
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
 * 个人资料
 */
public class PersonalInfoActivity extends BaseActivity{

    private Context context = PersonalInfoActivity.this;
    private ImageView head;// 头像
    private TextView nick,sex,zhanghao;// 昵称、性别、账号
    private String gender, headPic, email, nickname, name;
    private Intent intent;
    private Button toNick,toSexy,toHead;
    private boolean isBoy;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initData() {
        intent = new Intent();
    }

    @Override
    protected void initView() {
        toHead = (Button) findViewById(R.id.btn_head);
        toHead.setOnClickListener(this);
        head = (ImageView) findViewById(R.id.img_head);
        nick = (TextView) findViewById(R.id.nickname);
        sex = (TextView) findViewById(R.id.sex);
        zhanghao = (TextView) findViewById(R.id.zhanghao);
        toNick = (Button) findViewById(R.id.btn_tonickname);
        toNick.setOnClickListener(this);
        toSexy = (Button) findViewById(R.id.btn_sexy);
        toSexy.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        setTitleElse("个人信息");
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            // 头像
            case R.id.btn_head:
                intent.setClass(context,HeadActivity.class);
                intent.putExtra("headPic",headPic);
                startActivity(intent);
                break;
            // 昵称
            case R.id.btn_tonickname:
                intent.setClass(context,NicknameActivity.class);
                intent.putExtra("nickname",nickname);
                startActivity(intent);
                break;
            // 性别
            case R.id.btn_sexy:
                onChoiceSexy();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gender = (String) SpfUtil.get(context,"gender","");// 性别
                headPic = (String) SpfUtil.get(context,"headPic","");// 头像地址
                email = (String) SpfUtil.get(context,"email","");
                nickname = (String) SpfUtil.get(context,"nickname","");// 昵称
                name = (String) SpfUtil.get(context,"name","");// 电话号码
                // 显示个人信息
                nick.setText(nickname);
                Glide.with(context)
                        .load(headPic)
                        .transform(new GlideCircleTransform(context))
                        .into(head);
                if(gender.equals("1")){
                    sex.setText("男");
                    isBoy = true;
                }else{
                    sex.setText("女");
                    isBoy = false;
                }
                zhanghao.setText(name);
            }
        });
    }

    /**
     * 性别选择dialog
     */
    private void onChoiceSexy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.dialog_sexy, null);
        RelativeLayout relboy = (RelativeLayout) view.findViewById(R.id.rel_boy);
        RelativeLayout relgirl = (RelativeLayout) view.findViewById(R.id.rel_girl);
        ImageView boy = (ImageView) view.findViewById(R.id.img_boy);
        ImageView girl = (ImageView) view.findViewById(R.id.img_girl);
        if(isBoy){
            boy.setImageResource(R.drawable.icon_sexy_choice_on);
            girl.setImageResource(R.drawable.icon_sexy_choice_off);
        }else{
            boy.setImageResource(R.drawable.icon_sexy_choice_off);
            girl.setImageResource(R.drawable.icon_sexy_choice_on);
        }
        relboy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoy == true && sex.getText().equals("男")){
                    dialog.dismiss();
                    Log.i("--------","当前没有变化");
                }else{
                    isBoy = true;
                    String xb = "1";
                    sex.setText("男");
                    dialog.dismiss();
                    changeSexy(xb);
                    Log.i("--------","当前变化了");
                }
            }
        });
        relgirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBoy == false && sex.getText().equals("女")){
                    dialog.dismiss();
                    Log.i("--------","当前没有变化");
                }else{
                    isBoy = false;
                    String xb = "0";
                    sex.setText("女");
                    dialog.dismiss();
                    changeSexy(xb);
                    Log.i("--------","当前变化了");
                }
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /**
     * 更改性别
     */
    private void changeSexy(final String xb) {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("gender",xb);
        Request<JSONObject> request = new NormalPostRequest(Constants.PERSONALINFO_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            SpfUtil.put(context,"gender",xb);
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
}
