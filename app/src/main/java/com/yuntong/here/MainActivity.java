package com.yuntong.here;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.yuntong.here.activity.CityListActivity;
import com.yuntong.here.activity.FriendsActivity;
import com.yuntong.here.activity.LoginActivity;
import com.yuntong.here.activity.MyCollectionActivity;
import com.yuntong.here.activity.MyCommentActivity;
import com.yuntong.here.activity.MyHereActivity;
import com.yuntong.here.activity.PersonalInfoActivity;
import com.yuntong.here.activity.SceneActivity;
import com.yuntong.here.activity.SettingActivity;
import com.yuntong.here.activity.UnMessagedActivity;
import com.yuntong.here.activity.WebViewActivity;
import com.yuntong.here.adapter.MyAdapter;
import com.yuntong.here.adapter.SpaceItemDecoration;
import com.yuntong.here.app.AppManager;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.activity.SearchActivity;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.AdData;
import com.yuntong.here.entity.MessageData;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.service.UpdateService;
import com.yuntong.here.sql.MessageDao;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.view.SlideShowView;
import com.yuntong.here.view.xrecyclerview.ProgressStyle;
import com.yuntong.here.view.xrecyclerview.XRecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements SlideShowView.OnBranchClickListener{

    private static final String TAG = "MainActivity";// logt
    private Context context = MainActivity.this;
    private Intent intent;
    private RelativeLayout menuRel, cityRel;// 侧滑菜单键、城市
    private TextView cityTxt;// 城市
    private ImageView searchImg;// 搜索
    // 轮播
    private SlideShowView slideShowView;// 广告轮播
    private AdData adData;// 广告数据实体类
    private List<AdData> adDataList;// 存放广告数据的集合
    private List<String> imgList = new ArrayList<>();// 存放广告图片地址的集合
    private SceneListData sceneData;
    private List<SceneListData> sceneList;
    private DrawerLayout drawerLayout;// 用于侧滑
    private Button btn_login;// 侧滑菜单中的登录按钮
    private TextView username;// 显示名字
    private ImageView headimg;// 显示头像
    private String city;
    private View view;
    //我的here,我的收藏,设置
    private LinearLayout my_here,my_collection,setting,friends,my_comment,linear_message;
    private XRecyclerView recyclerView;
    private MyAdapter mAdapter;
    private int page_int, pagetotal;// 页数,总页数
    private long mExitTime = 0;// 退出时间
    private RequestQueue mQueue;// 请求队列
    private MessageDao messageDao;
    private ImageView unmessaged;
    // 判断是否正在刷新,判断是否正在加载更多
    private boolean isRefresh, isLoadingMore = false, isUnmessage=false, isLogin, isgetread = true, isfirstgetread = false;
    private String lon,lat,code;// 经纬度,判断是否登录
    private LocationClient mLocationClient = null;
    private MyLocationListenner myListener;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        lon = (String) SpfUtil.get(context,"lon","");
        lat = (String) SpfUtil.get(context,"lat","");
        isLogin = (boolean) SpfUtil.get(context, "isLogin", false);
        messageDao = new MessageDao(this);
        mQueue = MyApplication.getInstance().requestQueue;// 初始化请求队列
        sceneList = new ArrayList<>();
        intent = new Intent();
        adDataList = new ArrayList<>();
        view = getLayoutInflater().inflate(R.layout.list_add_head_view, null);
    }

    @Override
    protected void initView() {
        slideShowView = (SlideShowView) view.findViewById(R.id.slideshowview_deadview);
        unmessaged=(ImageView)findViewById(R.id.unmessaged);
        JSONObject jsonObject = JsonUtil.convertJsonObj(getIntent().getStringExtra("json"));
        if (jsonObject != null) {
            if (JsonUtil.getStr(jsonObject, "pushType").equals("SQ") || JsonUtil.getStr(jsonObject, "pushTye").equals("PL")) {
                isUnmessage=true;
                Intent i = new Intent(MainActivity.this, UnMessagedActivity.class);
                startActivity(i);
            }
        }
        if ((boolean)SpfUtil.get(MainActivity.this,"isUpdata",false)==true){
            showUpDataPopupWindow();
        }
        headimg = (ImageView) findViewById(R.id.touxiang);
        headimg.setOnClickListener(this);
        username = (TextView) findViewById(R.id.username);
        username.setOnClickListener(this);
        menuRel = (RelativeLayout) findViewById(R.id.relative_home_menu);
        cityRel = (RelativeLayout) findViewById(R.id.relative_home_city);
        cityTxt = (TextView) findViewById(R.id.txt_home_city);
        searchImg = (ImageView) findViewById(R.id.img_home_search);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
//        StatusBarUtil.setColorForDrawerLayout(this, drawerLayout, getResources().getColor(R.color.green));
        btn_login = (Button) findViewById(R.id.btn_login_sliding);
        my_here = (LinearLayout) findViewById(R.id.my_here);
        my_collection = (LinearLayout) findViewById(R.id.my_collection);
        setting = (LinearLayout) findViewById(R.id.linear_setting);
        friends = (LinearLayout) findViewById(R.id.linear_friends);
        recyclerView = (XRecyclerView) findViewById(R.id.recyclerview);
        my_comment = (LinearLayout) findViewById(R.id.my_comment);
        linear_message = (LinearLayout) findViewById(R.id.linear_message);
        linear_message.setOnClickListener(this);
        menuRel.setOnClickListener(this);
        cityRel.setOnClickListener(this);
        cityTxt.setOnClickListener(this);
        searchImg.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        my_here.setOnClickListener(this);
        my_collection.setOnClickListener(this);
        setting.setOnClickListener(this);
        my_comment.setOnClickListener(this);
        friends.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);// 如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);// BallSpinFadeLoader
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setArrowImageView(R.drawable.icon_pull_refresh);
        recyclerView.addHeaderView(view);
//        recyclerView.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.spacesize)));
    }

    @Override
    protected void bindView() {
        if(isLogin){
            btn_login.setVisibility(View.VISIBLE);
            username.setVisibility(View.GONE);
        }else{
            btn_login.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
        }
        page_int = 1;
        getSceneList(page_int);
        mAdapter = new MyAdapter(sceneList);
        recyclerView.setAdapter(mAdapter);
        /**
         * 商家概览列表的点击事件
         */
        mAdapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, SceneListData data) {
                String sceneId = String.valueOf(data.getSceneId());
                String panoId = String.valueOf(data.getPanoId());
                String name = String.valueOf(data.getName());
                String thumb = String.valueOf(data.getThumb());
                Intent i = new Intent();
                i.setClass(MainActivity.this, SceneActivity.class);
                i.putExtra("sceneId", sceneId);
                i.putExtra("panoId", panoId);
                i.putExtra("name", name);
                i.putExtra("thumb",thumb);
                startActivity(i);
            }
        });

        /**
         * 使用 RecyclerView加下拉刷新的时候，如果绑定的List对象在更新数据之前进行了clear，而这时紧接着迅速上滑RecyclerView，就会造成崩溃
         * 所以不能在新数据拿到之前就将集合清空
         */
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        isRefresh = true;
//                        adDataList.clear();
//                        imgList.clear();
//                        sceneList.clear();
                        page_int = 1;
                        recyclerView.setNoMore(false);// 刷新时设置当前可加载更多
                        getSceneList(page_int);
                        recyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        isLoadingMore = true;// 表示当前的操作是正在加载更多
                        if (page_int == pagetotal) {
                            recyclerView.noMoreLoading();
                        } else {
                            adDataList.clear();
                            imgList.clear();
                            page_int++;
                            getSceneList(page_int);
                            recyclerView.loadMoreComplete();
                        }
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            // 侧滑菜单键
            case R.id.relative_home_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            // 选择城市
            case R.id.relative_home_city:
            case R.id.txt_home_city:
                intent.setClass(MainActivity.this, CityListActivity.class);
                startActivityForResult(intent, 110);
                break;
            // 搜索
            case R.id.img_home_search:
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            // 侧滑页面中的登录
            case R.id.btn_login_sliding:
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            // 个人信息
            case R.id.touxiang:
            case R.id.username:
                if(isLogin == false){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(MainActivity.this, PersonalInfoActivity.class);
                }
                startActivity(intent);
                break;
            //我的here
            case R.id.my_here:
                if(isLogin == false){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(MainActivity.this, MyHereActivity.class);
                }
                startActivity(intent);
                break;
            //我的收藏
            case R.id.my_collection:
                if(isLogin == false){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(MainActivity.this, MyCollectionActivity.class);
                }
                startActivity(intent);
                break;
            //我的评论
            case R.id.my_comment:
                if(isLogin == false){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(MainActivity.this, MyCommentActivity.class);
                }
                startActivity(intent);
                break;
            // 侧滑--"设置"
            case R.id.linear_setting:
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            // 侧滑--"好友"
            case R.id.linear_friends:
                if(isLogin == false){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(MainActivity.this, FriendsActivity.class);
                }
                startActivity(intent);
                break;
            //消息
            case R.id.linear_message:
                if(isLogin == false){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else{
                    unmessaged.setVisibility(View.GONE);
                    intent.setClass(MainActivity.this, UnMessagedActivity.class);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 获取商家列表
     */
    private void getSceneList(int page) {
        String pages = String.valueOf(page);
        mQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("cityCode", "hangzhou");
        map.put("lng", lon);
        map.put("lat", lat);
        map.put("page", pages);
        map.put("pageSize", "20");
        Request<JSONObject> request = new NormalPostRequest(Constants.SCENELIST_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        isfirstgetread = true;
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            if(isRefresh){
                                adDataList.clear();
                                imgList.clear();
                                sceneList.clear();
                            }
                            JSONObject object1 = JsonUtil.getJsonObj(jsonObject, "model");
                            code = JsonUtil.getStr(object1, "code");// 1:已登陆、0:未登录
                            if (JsonUtil.getStr(object1,"adviceNum").equals("0")){
                                unmessaged.setVisibility(View.GONE);
                            }else if (isUnmessage==false){
                                unmessaged.setVisibility(View.VISIBLE);
                            }
                            //======================================================================
                            JSONObject object2 = JsonUtil.getJsonObj(object1, "sceneModel");// 商家列表
                            pagetotal = Integer.valueOf(JsonUtil.getStr(object2, "pageTotal"));// 获取总页数
                            JSONArray array = JsonUtil.convertJsonArry(object2, "data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = JsonUtil.convertJsonObj(array, i);
                                try {
                                    sceneData = new SceneListData(
                                            object.getInt("sceneId"),
                                            object.getString("name"),
                                            object.getString("address"),
                                            object.getString("thumb"),
                                            object.getInt("heat"),
                                            object.getInt("panoId"),
                                            object.getDouble("distance"));
                                    sceneList.add(sceneData);// 商家列表数据集合
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            //======================================================================
                            JSONArray array1 = JsonUtil.convertJsonArry(object1, "banners");// 广告轮播
                            for (int j = 0; j < array1.length(); j++) {
                                JSONObject object3 = JsonUtil.convertJsonObj(array1, j);
                                adData = new AdData(
                                        JsonUtil.getStr(object3, "name"),
                                        JsonUtil.getStr(object3, "picUrl"),
                                        JsonUtil.getStr(object3, "clickLink"),
                                        JsonUtil.getStr(object3, "type"));
                                adDataList.add(adData);// 广告轮播数据集合
                            }
                            // 将广告轮播数据集合中的图片地址存放到String集合中
                            for (int k = 0; k < adDataList.size(); k++) {
                                imgList.add(adDataList.get(k).getPicUrl());
                            }
                            // 将集合数据赋值给轮播视图View
                            slideShowView.setImageUris(imgList);
                            //======================================================================
                            mAdapter.notifyDataSetChanged();
                            isRefresh = false;
                            if(code.equals("0")){// 未登录
                                isLogin=false;
                                SpfUtil.put(context,"isLogin",false);
                                btn_login.setVisibility(View.VISIBLE);
                                username.setVisibility(View.GONE);
                            }else if(code.equals("1")){// 已登录
                                if ((boolean)(SpfUtil.get(MainActivity.this,"isEdit",false))==true){
                                    isLogin=true;
                                    SpfUtil.put(context,"isLogin",true);
                                    btn_login.setVisibility(View.GONE);
                                    username.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 如果当前网络加载失败，并且处于加载更多的操作，则底部视图显示“无网络”
                if(isLoadingMore){
                    recyclerView.noNet();
                    page_int = 1;// 在这里将页数置为1是为了不让其迭加，否则迭加到8时到达总页数，底部视图会显示"没有更多了"
                    isLoadingMore = false;
                }
                // 如果当前网络加载失败，并且处于下拉刷新的操作，则顶部视图显示“无网络”
                if(isRefresh){
                    recyclerView.refreshNoNet();
                    isRefresh = false;
                }
                if(mQueue.getCache().get(Constants.SCENELIST_URL)!=null){
                    //response exists  获取volley的缓存数据
                    String cachedResponse = new String(mQueue.getCache().get(Constants.SCENELIST_URL).data);
                    Log.i(TAG, "onErrorResponse: cachedResponse" + cachedResponse);
                    try {
                        JSONObject ob = new JSONObject(cachedResponse);
                        JSONObject ob1 = JsonUtil.getJsonObj(ob, "model");
//                        JSONObject object2 = JsonUtil.getJsonObj(ob1, "sceneModel");// 商家列表
//                        pagetotal = Integer.valueOf(JsonUtil.getStr(object2, "pageTotal"));// 获取总页数
//                        JSONArray array = JsonUtil.convertJsonArry(object2, "data");
//                        for (int i = 0; i < array.length(); i++) {
//                            JSONObject object = JsonUtil.convertJsonObj(array, i);
//                            try {
//                                sceneData = new SceneListData(
//                                        object.getInt("sceneId"),
//                                        object.getString("name"),
//                                        object.getString("address"),
//                                        object.getString("thumb"),
//                                        object.getInt("heat"),
//                                        object.getInt("panoId"),
//                                        object.getDouble("distance"));
//                                sceneList.add(sceneData);// 商家列表数据集合
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        //======================================================================
//                        JSONArray array1 = JsonUtil.convertJsonArry(ob1, "banners");// 广告轮播
//                        for (int j = 0; j < array1.length(); j++) {
//                            JSONObject object3 = JsonUtil.convertJsonObj(array1, j);
//                            adData = new AdData(
//                                    JsonUtil.getStr(object3, "name"),
//                                    JsonUtil.getStr(object3, "picUrl"),
//                                    JsonUtil.getStr(object3, "clickLink"),
//                                    JsonUtil.getStr(object3, "type"));
//                            adDataList.add(adData);// 广告轮播数据集合
//                        }
//                        // 将广告轮播数据集合中的图片地址存放到String集合中
//                        for (int k = 0; k < adDataList.size(); k++) {
//                            imgList.add(adDataList.get(k).getPicUrl());
//                        }
//                        // 将集合数据赋值给轮播视图View
//                        slideShowView.setImageUris(imgList);
//                        //======================================================================
//                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, map);
        mQueue.add(request);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUnmessage=false;
        isLogin = (boolean) SpfUtil.get(context, "isLogin", false);
        if (isLogin) {
            btn_login.setVisibility(View.GONE);
            username.setVisibility(View.VISIBLE);
            String gender = (String) SpfUtil.get(MainActivity.this, "gender", "");// 性别
            String headPic = (String) SpfUtil.get(MainActivity.this, "headPic", "");// 头像地址
            String email = (String) SpfUtil.get(MainActivity.this, "email", "");
            String nickname = (String) SpfUtil.get(MainActivity.this, "nickname", "");// 昵称
            String name = (String) SpfUtil.get(MainActivity.this, "name", "");// 电话号码
            // 显示昵称和头像
            username.setText(nickname);
            Glide.with(MainActivity.this)
                    .load(headPic)
                    .transform(new GlideCircleTransform(MainActivity.this))
                    .into(headimg);
            if (isgetread && isfirstgetread) {
                getReadrd((String) SpfUtil.get(this, "lastSelectTime", ""));
            }
        } else {
            btn_login.setVisibility(View.VISIBLE);
            username.setVisibility(View.GONE);
            headimg.setImageResource(R.drawable.icon_head_bg);
        }
        mLocationClient = new LocationClient(getApplicationContext());
        myListener = new MyLocationListenner();
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
        LocationClientOption option = new LocationClientOption();
        option.setAddrType("all");
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        cycleView.pushImageCycle();
    }

    @Override
    protected void onDestroy() {
//        cycleView.pushImageCycle();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (intent == null) {
//            return;
//        }
        switch (requestCode) {
            case 110:
//                String name = intent.getStringExtra("cityname");// 得到返回的城市名字
//                if (name != null) {
//                    cityTxt.setText(name);
//                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitApp() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            showToast(getString(R.string.ones_more));
        } else {
            AppManager.getInstance().appExit(context);
        }
        mExitTime = System.currentTimeMillis();
    }


    private void getReadrd(String lastSelectTime) {
        Map<String, String> map = new HashMap<>();
        map.put("lastSelectTime", lastSelectTime);
        Request<JSONObject> request = new NormalPostRequest(Constants.GETREADED,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            isgetread = false;
                            JSONObject jsonObject1 = JsonUtil.getJsonObj(jsonObject, "model");
                            SpfUtil.put(MainActivity.this, "lastSelectTime", JsonUtil.getStr(jsonObject1, "selectTime"));
                            JSONArray jsonArray = JsonUtil.convertJsonArry(jsonObject1, "adviceList");
                            if (jsonArray.length() != 0) {
                                for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                    JSONObject advice = JsonUtil.convertJsonObj(jsonArray, i);
                                    MessageData messageData = new MessageData();
                                    messageData.setIsClick(JsonUtil.getStr(advice, "fStatus"));
                                    messageData.setAdviceId(JsonUtil.getStr(advice, "adviceId"));
                                    messageData.setAdvicePic(JsonUtil.getStr(advice, "advicePic"));
                                    messageData.setBid(JsonUtil.getStr(advice, "bId"));
                                    messageData.setContent(JsonUtil.getStr(advice, "content"));
                                    messageData.setDesc(JsonUtil.getStr(JsonUtil.getJsonObj(advice, "adviceType"), "desc"));
                                    messageData.setTitle(JsonUtil.getStr(advice, "title"));
                                    messageData.setCreateTime(JsonUtil.getStr(advice, "createTime"));
                                    messageData.setType(JsonUtil.getStr(JsonUtil.getJsonObj(advice, "adviceType"), "type"));
                                    messageData.setIntroduction(JsonUtil.getStr(advice, "introduction"));
                                    messageData.setLink(JsonUtil.getStr(advice, "link"));
                                    messageData.setUserId(JsonUtil.getStr(advice, "userId"));
                                    messageData.setUserName(JsonUtil.getStr(advice, "userName"));
                                    messageData.setSceneName(JsonUtil.getStr(advice, "sceneName"));
                                    messageData.setHereId(JsonUtil.getStr(advice, "hereId"));
                                    messageData.setYuntongid((String) SpfUtil.get(MainActivity.this, "userId", ""));
                                    messageDao.add(messageData);
                                }
                            }
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        mQueue.add(request);
    }

    /**
     * 广告轮播的点击事件
     * @param position
     * 活动页(0)或者全景页(1)
     */
    @Override
    public void OnBranchClick(int position) {
        String clickLink = adDataList.get(position).getClickLink();
        String type = adDataList.get(position).getType();
        String name = adDataList.get(position).getName();// 场景名字
        if(type.equals("0")){
            Intent i = new Intent();
            i.setClass(MainActivity.this, WebViewActivity.class);
            i.putExtra("url", clickLink);
            startActivity(i);
        }
        if(type.equals("1")){
            // 168|19903
            String[] str_=clickLink.split("\\|");
            String scene = str_[0];// secneId
            String pano = str_[1];// panoId
            Intent i = new Intent();
            i.setClass(MainActivity.this, SceneActivity.class);
            i.putExtra("sceneId", scene);
            i.putExtra("panoId", pano);
            i.putExtra("name", name);
            startActivity(i);
        }
    }



    public void showUpDataPopupWindow() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.popuwindow_updata, null);
        TextView cancel = (TextView) view.findViewById(R.id.cancel_updata);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView invest_rightnow = (TextView) view.findViewById(R.id.updata);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateService.class);
                intent.putExtra("App_Name", "here");
//                intent.putExtra("url", (String)SpfUtil.get(MainActivity.this,"upUrl",""));
                intent.putExtra("url", "http://hot.m.shouji.360tpcdn.com/160520/60bf7f1642995b6c8c81706c67daee14/com.qihoo.appstore_300050113.apk");
                startService(intent);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            SpfUtil.put(MainActivity.this,"lon",location.getLongitude());
            SpfUtil.put(MainActivity.this,"lat", location.getLatitude());
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
