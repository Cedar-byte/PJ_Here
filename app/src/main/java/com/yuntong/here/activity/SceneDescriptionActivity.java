package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.view.RoundCornerImageView;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 场景介绍
 */
public class SceneDescriptionActivity extends BaseActivity{

    private Context context = SceneDescriptionActivity.this;
    private ImageView thumbImg,back;// 背景缩略图、小图标
    private RoundCornerImageView businessPic;
    // 距离、热度、电话号码、地址、呼叫、导航、介绍
    private TextView distanceTxt, hotTxt, phoneTxt, adressTxt, callTxt, daohangTxt, descripTxt, nameTxt;
    private String sceneId, phone, adress;
    private double distance;// 距离
    private int heat;// 热度
    private RequestQueue mQueue;// 请求队列
    private JSONObject object;
    private String dlat,dlon,slat,slon;
    private String lon,lat;
    private AlertDialog choiceDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scene_description;
    }

    @Override
    protected void initData() {
        mQueue = MyApplication.getInstance().requestQueue;
        sceneId = getIntent().getStringExtra("sceneId");
        lon = (String) SpfUtil.get(context,"lon","");
        lat = (String) SpfUtil.get(context,"lat","");
    }

    @Override
    protected void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        nameTxt = (TextView) findViewById(R.id.text_name);
        thumbImg = (ImageView) findViewById(R.id.img_scene_bg);
        businessPic = (RoundCornerImageView) findViewById(R.id.img_scene_head_small);
        distanceTxt = (TextView) findViewById(R.id.text_distance);
        hotTxt = (TextView) findViewById(R.id.text_hot);
        phoneTxt = (TextView) findViewById(R.id.text_phone_num);
        adressTxt = (TextView) findViewById(R.id.text_adress);
        callTxt = (TextView) findViewById(R.id.text_call);
        callTxt.setOnClickListener(this);
        daohangTxt = (TextView) findViewById(R.id.text_daohang);
        daohangTxt.setOnClickListener(this);
        descripTxt = (TextView) findViewById(R.id.text_description);
    }

    @Override
    protected void bindView() {
        getSceneData();
    }

    private void getSceneData() {
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", sceneId);
        map.put("lng", lon);
        map.put("lat", lat);
        Request<JSONObject> request = new NormalPostRequest(Constants.SCENEINTRODUCE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(JsonUtil.getStr(jsonObject, "status").equals("1")){
                            object = JsonUtil.getJsonObj(jsonObject, "model");
                            dlat = JsonUtil.getStr(object,"lat");
                            dlon = JsonUtil.getStr(object,"lng");
                            phone = JsonUtil.getStr(object, "mobile");
                            adress = JsonUtil.getStr(object, "address");
                            distance = Double.valueOf(JsonUtil.getStr(object, "distance"));
                            heat = Integer.valueOf(JsonUtil.getStr(object, "heat"));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    nameTxt.setText(JsonUtil.getStr(object, "name"));
                                    if(heat < 1000){
                                        hotTxt.setText(String.valueOf(heat));
                                    }else if(heat > 1000){
                                        int h2 = heat/1000;
                                        hotTxt.setText(new java.text.DecimalFormat("#.0").format(h2) + "k");
                                    }
                                    phoneTxt.setText(phone);
                                    adressTxt.setText(adress);
                                    descripTxt.setText(JsonUtil.getStr(object, "about"));
                                    if(distance < 1000.0){
                                        distanceTxt.setText(String.valueOf(distance));
                                    }else if(distance > 1000.0){
                                        double d2 = distance/1000.0;
                                        distanceTxt.setText(new java.text.DecimalFormat("#.0").format(d2) + "km");// 距离
                                    }
                                    ImageLoaderUtil.disPlay(JsonUtil.getStr(object,"businessPic"),businessPic);
                                    ImageLoaderUtil.disPlay(JsonUtil.getStr(object,"thumb"),thumbImg);
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        mQueue.add(request);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.back:
                SceneDescriptionActivity.this.finish();
                break;
            // 呼叫
            case R.id.text_call:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
                break;
            // 导航
            /**
             * route 服务类型
             * sourceApplication 第三方调用应用名称
             * slat 起点纬度、slon 起点经度、sname 起点名称
             * dlat 终点纬度、dlon 终点经度、dname 终点名称
             * dev 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
             * m 驾车方式 =0（速度快）=1（费用少） =2（路程短）=3 不走高速 =4（躲避拥堵）=5（不走高速且避免收费） =6（不走高速且躲避拥堵） =7（躲避收费和拥堵） =8（不走高速躲避收费和拥堵）。
             *   公交 =0（速度快）=1（费用少） =2（换乘较少）=3（步行少）=4（舒适）=5（不乘地铁）
             * t t = 1(公交) =2（驾车） =4(步行)
             */
            case R.id.text_daohang:
                //移动APP调起Android高德地图方式举例
//                Intent intent = new Intent("android.intent.action.VIEW",
//                        android.net.Uri.parse("androidamap://route?sourceApplication=softname&slat=30.335589&slon=120.12479&sname=我的位置&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + adress + "&dev=0&m=0&t=1"));
//                intent.setPackage("com.autonavi.minimap");
//                startActivity(intent);
                //移动APP调起Android百度地图方式
                Intent intent = null;
                try {
                    intent = Intent.getIntent("intent://map/direction?origin=latlng:" + lat + "," + lon + "|name:我的位置&destination=" + adress + "&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                startActivity(intent); // 启动调用
                choiceMap();
                break;
        }

    }

    private void choiceMap() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        choiceDialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.mapdialog, null);
    }
}
