package com.yuntong.here.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.yuntong.here.adapter.OtherHereAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.OtherHereData;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;
import com.yuntong.here.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/19.
 * 他人主页
 */
public class OtherPeopleActivity extends BaseActivity implements OtherHereAdapter.ItemClickListener {

    private Context context = OtherPeopleActivity.this;
    private RelativeLayout back;// 返回
    private ImageView sexyImg;// 头像、性别
    private TextView nameText;// 名字
    private Button add;// 加为好友
    private CircleImageView headImg;// 圆形头像
    private RecyclerView recyclerView;
    private OtherHereAdapter adapter;
    private OtherHereData data;
    private List<OtherHereData> datalist;
    private String isFriend;// 是否好友
    private String nickname;// 昵称
    private String headPic;// 头像
    private String otherId;// 他人的userid

    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_people;
    }

    @Override
    protected void initData() {
        datalist = new ArrayList<>();
        otherId = getIntent().getStringExtra("otherId");
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        headImg = (CircleImageView) findViewById(R.id.headpic);
        sexyImg = (ImageView) findViewById(R.id.sexy);
        nameText = (TextView) findViewById(R.id.name);
        add = (Button) findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);// 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
    }

    @Override
    protected void bindView() {
        getData();// 获取他人主页数据
        // Context、数据源、点击事件的listener
        adapter = new OtherHereAdapter(this,datalist,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            // 添加好友
            case R.id.btn_add:
                Intent i = new Intent();
                i.setClass(context,AddFriendActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    /**
     * 点击查看here详情
     * @param hereId
     */
    @Override
    public void lookUpHere(String hereId) {
        Intent i = new Intent(context,HereDetailActivity.class);
        i.putExtra("hereId",hereId);
        startActivity(i);
    }

    private void getData() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("otherId",otherId);
        Request<JSONObject> request = new NormalPostRequest(Constants.OTHERHERE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONObject object = JsonUtil.getJsonObj(jsonObject,"model");
                            isFriend = JsonUtil.getStr(object,"isFriend");// 1：是好友；0：不是好友
                            SpfUtil.put(context,"isFriendNum",isFriend);
                            nickname = JsonUtil.getStr(object,"nickname");
                            headPic = JsonUtil.getStr(object,"headPic");
                            JSONArray array = JsonUtil.convertJsonArry(object,"myHereDTOList");
                            if(array != null){
                                for(int i = 0; i < array.length(); i++){
                                    data = new OtherHereData();
                                    JSONObject object1 = JsonUtil.convertJsonObj(array,i);
                                    data.setUpvoteNum(JsonUtil.getStr(object1,"upvoteNum"));
                                    data.setCommentNum(JsonUtil.getStr(object1,"commentNum"));
                                    data.setSceneName(JsonUtil.getStr(object1,"sceneName"));
                                    data.setSceneId(JsonUtil.getStr(object1,"sceneId"));
                                    data.setHereId(JsonUtil.getStr(object1,"hereId"));
                                    data.setText(JsonUtil.getStr(object1,"text"));
                                    data.setCreateTime(JsonUtil.getStr(object1,"createTime"));
                                    JSONArray array1 = JsonUtil.convertJsonArry(object1,"hereMediaDTOList");
                                    String[] imgs = new String[array1.length()];
                                    for(int j = 0; j < array1.length(); j++){
                                        JSONObject object2 = JsonUtil.convertJsonObj(array1,j);
                                        String img = JsonUtil.getStr(object2,"url");
                                        imgs[j] = img;
                                    }
                                    if(imgs.length > 0){
                                        data.setFirstImg(imgs[0]);
                                    }
                                    data.setHereMediaDTOList(imgs);
                                    datalist.add(data);
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nameText.setText(nickname);
                                    if(isFriend.equals("1")){
                                        add.setVisibility(View.GONE);
                                    }else{
                                        add.setVisibility(View.VISIBLE);
                                    }
                                    ImageLoaderUtil.disPlayBig(headPic,headImg);
                                    adapter.notifyDataSetChanged();
                                }
                            });
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
