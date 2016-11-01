package com.yuntong.here.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mob.tools.gui.ViewPagerAdapter;
import com.yuntong.here.MainActivity;
import com.yuntong.here.R;
import com.yuntong.here.adapter.ViewPaperAdapter;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.ExampleUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/5/16.
 * 启动页(定位)
 */
public class StartupActivity extends BaseActivity  {


    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private ViewPager startup_paper;
    private View  view1;
    private View  view2;
    private View  view3;
    private List<View> views;
    private ViewPaperAdapter viewPaperAdapter;
    private ImageView click_view3;


    private LocationClient mLocationClient = null;
    private MyLocationListenner myListener;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏f
        return R.layout.activity_statrt_up;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyLocationListenner();
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
        startup_paper=(ViewPager)findViewById(R.id.startup_paper);
        view1=getLayoutInflater().from(this).inflate(R.layout.start_view1,null);
        view2=getLayoutInflater().from(this).inflate(R.layout.start_view2, null);
        view3=getLayoutInflater().from(this).inflate(R.layout.start_view3,null);
        click_view3=(ImageView)view3.findViewById(R.id.start_view3);
        views=new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        viewPaperAdapter=new ViewPaperAdapter(views);
        startup_paper.setAdapter(viewPaperAdapter);
        if ((boolean)SpfUtil.get(StartupActivity.this,"isFirst",true)==false){
            startup_paper.setVisibility(View.GONE);
            getUpdata();
        }else {
            JPushInterface.init(getApplicationContext());
            registerMessageReceiver();
            SpfUtil.put(StartupActivity.this, "isFirst", false);
        }
        click_view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartupActivity.this, MainActivity.class));
            }
        });


    }

    @Override
    protected void bindView() {

    }

    private void sleep() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    JPushInterface.init(getApplicationContext());
                    registerMessageReceiver();
                    SpfUtil.put(StartupActivity.this,"isFirst",false);
                    startActivity(new Intent(StartupActivity.this, MainActivity.class));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getUpdata() {
        SpfUtil.put(StartupActivity.this,"isUpdata",false);
        RequestQueue mQueue = Volley.newRequestQueue(this);
        Map<String, String> map = new HashMap<>();
        map.put("versionNum", "1.0");
        map.put("type", "1");
        Request<JSONObject> request = new NormalPostRequest(Constants.UPDATA_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONObject jsonObject1=JsonUtil.getJsonObj(jsonObject,"model");
                            if (JsonUtil.getStr(jsonObject1,"lastVersionNum").equals("1.1")){
                               sleep();
                            }else {
                                SpfUtil.put(StartupActivity.this,"isUpdata",true);
                                SpfUtil.put(StartupActivity.this,"upUrl",JsonUtil.getStr(jsonObject1,"lastDownUrl"));
                                sleep();
                            }
                        } else {
                            sleep();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                sleep();
            }
        }, map);
        mQueue.add(request);
    }

    @Override
    protected void widgetClick(View v) {

    }

    Handler mHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 0x110:
//                    AMapLocation loc = (AMapLocation) msg.obj;
////                    String result = CommonUtil.getLocationStr(loc);
//                    String lon = CommonUtil.getLonStr(loc);// 精度
//                    String lat = CommonUtil.getLatStr(loc);// 纬度
//                    SpfUtil.put(StartupActivity.this,"lon",lon);
//                    SpfUtil.put(StartupActivity.this,"lat",lat);
                    break;
                default:
                    break;
            }
        }
    };

//    /**
//     * 定位监听
//     *
//     * @param aMapLocation
//     */
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (null != aMapLocation) {
//            Message msg = mHandler.obtainMessage();
//            msg.obj = aMapLocation;
//            msg.what = 0x110;
//            mHandler.sendMessage(msg);
//        }
//    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        if (mLocationClient != null) {
            if (myListener != null) {
                // 移除定位监听函数
                mLocationClient.unRegisterLocationListener(myListener);
                myListener = null;
            }
            if (mLocationClient.isStarted()) {
                // 停止百度定位
                mLocationClient.stop();
            }
            mLocationClient = null;
        }
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        isForeground = false;
        super.onPause();
    }


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }

            }
        }
    }

    class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            SpfUtil.put(StartupActivity.this,"lon",location.getLongitude());
            SpfUtil.put(StartupActivity.this,"lat", location.getLatitude());
            if (location.getLatitude() > 0 &&location.getLongitude() > 0) {
                // 停止定位
                if (mLocationClient != null) {
                    if (myListener != null) {
                        // 移除定位监听函数
                        mLocationClient.unRegisterLocationListener(myListener);
                        myListener = null;
                    }
                    if (mLocationClient.isStarted()) {
                        // 停止百度定位
                        mLocationClient.stop();
                    }
                    mLocationClient = null;
                }
            }

        }

        @Override
        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


}
