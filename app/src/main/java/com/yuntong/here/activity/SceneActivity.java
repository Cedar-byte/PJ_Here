
package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mob.tools.utils.UIHandler;
import com.yongchun.library.view.ImageSelectorActivity;
import com.yuntong.here.R;
import com.yuntong.here.app.AppManager;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GetImg;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;


/**
 * Created by me on 2016/5/6.
 * 场景界面
 */
public class SceneActivity extends BaseActivity implements PlatformActionListener, Handler.Callback, CommonUtil.onPhotoDialogClickListener {

    private Context context = SceneActivity.this;
    private ImageButton descriptionBtn, moreBtn;
    private RelativeLayout back, top, cancel;
    private Intent intent;
    private FrameLayout frameLayout;
    private WebView webView;
    private View view;
    private RequestQueue requestQueue;
    private View view_collection;
    private LinearLayout biaoqian;
    private Button confirm;
    private TextView num_pic, sceneName;// 照片张数,场景名字
    private ImageView bg;
    private ArrayList<String> list = new ArrayList<>();
    private HttpUtils http;
    private RequestParams params;
    private ArrayList<String> smallList = new ArrayList<>();
    // 子场景ID,HereId,场景Id,H5坐标(x,y), 图片服务器返回的网络图片地址(picpath)
    private String subpanoId, fhereId, panoId, name, thumb, summary = "", x, y, picpath, text, sceneId;
    private Dialog dialog,dialog2,dialogJs;
    // 全局音乐和重力感应、是否登录、是否是从EditActivity点击"发送"后返回到SceneActivity、 、 是否已收藏
    private boolean isMusicOpen, isGravityOpen, isLogin, isEditActivityPressedSend = false, ischeck=true, isCollection = false;
    private AlertDialog shareDialog;
    private Bundle extras;// 从EditActivity点击"发送"后传递过来的Bundle

    /**
     * 横竖屏切换回调
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            top.setVisibility(View.GONE);
            webView.loadUrl("javascript:B.pano.webvr()");
        }else{
            top.setVisibility(View.VISIBLE);
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return R.layout.activity_scene;
    }

    @Override
    protected void initData() {
        ShareSDK.initSDK(context);// 初始化ShareSDK
        isLogin = (boolean) SpfUtil.get(context,"isLogin",false);
        isMusicOpen = (boolean) SpfUtil.get(context,"isMusicOpen",false);
        isGravityOpen = (boolean) SpfUtil.get(context,"isGravityOpen",false);
        http = new HttpUtils();
        params = new RequestParams();
        view_collection = getLayoutInflater().inflate(R.layout.pop_hint, null);
        intent = new Intent();
        sceneId = getIntent().getStringExtra("sceneId");
        panoId = getIntent().getStringExtra("panoId");
        name = getIntent().getStringExtra("name");
        thumb = getIntent().getStringExtra("thumb");
        requestQueue = MyApplication.getInstance().requestQueue;
        dialogJs = CommonUtil.onJsDialog(context);
        dialog = CommonUtil.onLoadingDialog(context, "正在加载中...");
        dialog.show();
    }

    @Override
    protected void initView() {
        sceneName = (TextView) findViewById(R.id.text_scene_name);
        back = (RelativeLayout) findViewById(R.id.rel_back_scene);
        back.setOnClickListener(this);
        view=getLayoutInflater().inflate(R.layout.pop_hint,null);
        descriptionBtn = (ImageButton) findViewById(R.id.img_btn_scene_description);
        descriptionBtn.setOnClickListener(this);
        moreBtn = (ImageButton) findViewById(R.id.img_btn_scene_more);
        moreBtn.setOnClickListener(this);
        frameLayout=(FrameLayout)findViewById(R.id.web_scene);
        webView = new WebView(getApplicationContext());
        frameLayout.addView(webView);
        top = (RelativeLayout) findViewById(R.id.top);
        biaoqian = (LinearLayout) findViewById(R.id.biaoqian);
        confirm = (Button) findViewById(R.id.queding);
        confirm.setOnClickListener(this);
        num_pic = (TextView) findViewById(R.id.num_pic);
        cancel = (RelativeLayout) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        bg = (ImageView) findViewById(R.id.bg);
    }

    @Override
    protected void bindView() {
        onNum();// 后台统计场景浏览数
        sceneName.setText(name);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true); //设置WebView属性，能够执行Javascript脚本
        settings.setDefaultTextEncodingName("utf-8");
        settings.setAllowFileAccess(true);//设置可以访问文件
        settings.setBuiltInZoomControls(true);//设置支持缩放
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        settings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        settings.setDatabaseEnabled(true);//开启 database storage API 功能
        settings.setAppCacheEnabled(true);//开启 Application Caches 功能
        settings.setUseWideViewPort(true);
        webView.loadUrl("http://testc.panocooker.com/pano/view"); //加载需要显示的网页
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(webView != null){
                    webView.loadUrl("javascript:Pano.isWeb=false"); //加载需要显示的网页
                    webView.loadUrl("javascript:Pano.createPano" + "(" + panoId + ")");
                }
                dialog.dismiss();
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        // 创建广播接收器
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction(Constants.ISEDITACTIVITYFINISH_BROADCAST_ACTION);
        SceneActivity.this.registerReceiver(forIsEditActivityFinishReceiver,intentfilter);
        initJs();// 与JS交互的相关定义
    }

    /**
     * 统计场景浏览数
     */
    private void onNum() {
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", sceneId);
        Request<JSONObject> request = new NormalPostRequest(Constants.SCENENUM_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            // 返回
            case R.id.rel_back_scene:
                SceneActivity.this.finish();
//                ImageSelectorActivity.start(SceneActivity.this,9,1,true,true,false);// 跳转到图片选择界面
                break;
            // 查看详情
            case R.id.img_btn_scene_description:
                intent.setClass(context, SceneDescriptionActivity.class);
                intent.putExtra("sceneId", sceneId);
                startActivity(intent);
                break;
            // 更多(分享、收藏、复制链接)
            case R.id.img_btn_scene_more:
                shareDialog(isCollection);
                break;
            // 编辑标签、确定按钮
            case R.id.queding:
                // 点击确定后将isEditActivityPressedSend的状态置为false，否则此时点击返回键时会重新跳转到EditActivity。
                // 同时将EditActivity finish掉
                isEditActivityPressedSend = false;
                AppManager.getInstance().destoryActivity("EditActivity");
                webView.loadUrl("javascript:B.pano.removeViewChange()");
                dialog2 = CommonUtil.onLoadingDialog(context,"数据加载中...");
                dialog2.show();
                for (int i = 0; i < list.size(); i++) {
                    String imgPath = list.get(i);
                    if(imgPath.contains("camera_default")) {
                        imgPath = imgPath.replace("camera_default", "");
                    }
//                    File file = CommonUtil.getSmallBitmap(imgPath);// 图片压缩
                      File file = new File(imgPath);
                      getImg(file);
					}
                break;
            // 编辑标签、取消
            // 点击取消后重新跳转到EditActivity
            case R.id.cancel:
                toEditActivity();
                break;
        }
    }

    private void getImg(File file) {
        params.addBodyParameter("path",file,"application/octet-stream");
        http.send(HttpRequest.HttpMethod.POST, Constants.PICTURE_URL, params, new RequestCallBack<Object>() {
            @Override
            public void onStart() {}
            @Override
            public void onLoading(long total, long current, boolean isUploading) {}
            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                String s = responseInfo.result.toString();
                JSONObject object = JsonUtil.convertJsonObj(s);
                JSONArray array = JsonUtil.convertJsonArry(object,"data");
                try {
                    picpath = array.getString(0);
                    smallList.add(picpath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(list.size() < 9 && smallList.size() == list.size() - 1
                        || list.size() == 9 && smallList.size() == list.size()){
                    onNet();
                }
            }
            @Override
            public void onFailure(HttpException error, String msg) {}
        });
    }

    private void onNet(){
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < smallList.size(); i++){
            map.put("pic" + (i+1),smallList.get(i));
        }
        map.put("text",text);
        map.put("type","1");
        map.put("sceneId",sceneId);
        map.put("subpanoId",subpanoId);
        map.put("ath",x);
        map.put("atv",y);
        Request<JSONObject> request = new NormalPostRequest(Constants.HERE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            biaoqian.setVisibility(View.GONE);
                            webView.loadUrl("javascript:B.pano.showPanoObject()");
                            cancel.setVisibility(View.GONE);
                            top.setVisibility(View.VISIBLE);
                            // 上传成功后就将集合中的数据清除（一定要执行这个操作，不然接着再发布一个here时会失败，因为集合数据没清空，请求接口时参数中的数据多于9条）
                            smallList.clear();
                            list.clear();
                            dialog2.dismiss();
                        } else {
//                            ToastUtil.showToast(context, JsonUtil.getStr(jsonObject, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    // 从相册
    @Override
    public void onPic() {
        // 照片最大数，1 多选模式，2 单选模式，是否显示拍照图片，true，是否跳裁剪
        ImageSelectorActivity.start(SceneActivity.this,9,1,true,true,false);// 跳转到图片选择界面
    }

    private BroadcastReceiver forIsEditActivityFinishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isEditActivityPressedSend = false;// 收到EditActivity结束的广播后将isEditActivityPressedSend的状态置为false，否则此时点击返回键时会重新跳转到EditActivity。
            top.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.GONE);
            biaoqian.setVisibility(View.GONE);
            webView.loadUrl("javascript:B.pano.showPanoObject()");
        }
    };

    /**
     * 使用Flags标识跳转到EditActivity，(将EditA
     * ctivity拉回到栈顶)
     */
    private void toEditActivity(){
        Intent intent = new Intent(context, EditActivity.class);
        // 使用Intent.FLAG_ACTIVITY_REORDER_TO_FRONT 标志，将已经开启的EditActivity加到栈顶
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Bundle b = new Bundle();
        b.putStringArrayList("list",list);// 照片路径集合
        b.putString("text",text);// 内容
        intent.putExtras(b);
        startActivity(intent);
    }

    /**
     * 获取图片选择的数据集合并跳转到编辑界面
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            // 获取图片选择界面返回的图片路径
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            Intent i = new Intent();
            i.setClass(context,EditActivity.class);
            i.putExtra("extraImages",images);// 从初始界面跳转到编辑界面   并把返回的图片集合传递过去
            startActivity(i);
        }
    }

    /**
     * 如果某个activity已存在，然后又重新回到栈顶，若此时你要执行某些操作，则在onNewIntent()方法中执行
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 获取EditActivity传递过来的数据
        extras = intent.getExtras();
        if(extras != null){
            isEditActivityPressedSend = true;
            list = null;// 为了防止出错，现将集合置为null
            list = extras.getStringArrayList("dataList");
            text = extras.getString("text");
            onUi();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isLogin = (boolean) SpfUtil.get(context,"isLogin",false);
        if (isLogin&&ischeck){
            isFavorite();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        // 取消广播注册，不然会has leaked IntentReceiver
        unregisterReceiver(forIsEditActivityFinishReceiver);
        frameLayout.removeAllViews();
        if (webView!=null){
            webView.destroy();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            // 如果此时是从EditActivity点击"发送"之后过来了，则按下返回键时重新跳转到EditActivity
            if(isEditActivityPressedSend){
//                isEditActivityPressedSend = false;// 防止快速操作时出现错误，在这将布尔值还原成false
                toEditActivity();
            }else{
                SceneActivity.this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onUi() {
        if(isEditActivityPressedSend){
            top.setVisibility(View.GONE);
            cancel.setVisibility(View.VISIBLE);
            biaoqian.setVisibility(View.VISIBLE);
            webView.loadUrl("javascript:B.pano.hidePanoObject()");
            webView.loadUrl("javascript:B.pano.registerViewchange()");
            if(list.size()<9){
                num_pic.setText(String.valueOf(list.size() - 1));
            }else if(list.size()==9){
                num_pic.setText("9");
            }
            ImageLoaderUtil.disPlay("file://" + list.get(0),bg);
        }
    }

    private void  isFavorite(){
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", sceneId);
        Request<JSONObject> request = new NormalPostRequest(Constants.GERCOLLECTION_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")){
                            ischeck=false;
                            JSONObject jsonObject1=JsonUtil.getJsonObj(jsonObject,"model");
                            if (JsonUtil.getStr(jsonObject1,"favorite").equals("1")){
                                isCollection=true;
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    private void initJs(){
        JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface();
        webView.addJavascriptInterface(myJavaScriptInterface, "jsforBoolean");
        JsIsLoginInterface jslogin = new JsIsLoginInterface();
        webView.addJavascriptInterface(jslogin, "jsforLogin");
        JavaScriptIntent javaScriptIntent=new JavaScriptIntent();
        webView.addJavascriptInterface(javaScriptIntent,"jsforintent");//js跳转here详情页面
        JavaScriptHereIntent javaScriptHereIntent=new JavaScriptHereIntent();
        webView.addJavascriptInterface(javaScriptHereIntent,"jsforHere");
        JavaScriptIDIntent javaScriptIDIntent=new JavaScriptIDIntent();
        webView.addJavascriptInterface(javaScriptIDIntent,"jsforID");// 取子场景id
        JavaScriptHereIDIntent javaScriptHereIDIntent=new JavaScriptHereIDIntent();
        webView.addJavascriptInterface(javaScriptHereIDIntent,"jsforHereID");// 取Hereid
        JSLandscapeIntent jsLandscapeIntent = new JSLandscapeIntent();
        webView.addJavascriptInterface(jsLandscapeIntent,"jsforLandscape");// 横屏
        JSPortraitIntent jsPortraitIntent = new JSPortraitIntent();
        webView.addJavascriptInterface(jsPortraitIntent,"jsforPortrait");// 竖屏
        JSMusicIntent jsMusic = new JSMusicIntent();
        webView.addJavascriptInterface(jsMusic, "jsforStatus");
        JsshowInterface jsshowInterface = new JsshowInterface();
        webView.addJavascriptInterface(jsshowInterface, "jsforShowToast");
        JSLoadingShowInterface show = new JSLoadingShowInterface();
        webView.addJavascriptInterface(show, "jsforShow");// 显示等待菊花
        JSLoadingDisInterface dismiss = new JSLoadingDisInterface();
        webView.addJavascriptInterface(dismiss, "jsforDismiss");// 取消等待菊花
    }

    /** 与js交互调试用 */
    public class JsshowInterface {
        public JsshowInterface() {}
        @JavascriptInterface
        public void showToast(String s) {
            showToast(s);
        }
    }

    /** 判断是否登录与js交互 */
    public class JavaScriptInterface {
        public JavaScriptInterface() {}
        @JavascriptInterface
        public boolean onBoolean() {
            return isLogin;
        }
    }

    /** 选择图片 */
    public class JsIsLoginInterface{
        public JsIsLoginInterface() {}
        @JavascriptInterface
        public void onLogin(boolean code) {
            if(code){// 已登录
                CommonUtil.showPhotoDialog(context,SceneActivity.this);
            }else{
                intent.setClass(context, LoginActivity.class);
                startActivity(intent);
            }
        }
    }

    /** 到Here详情 */
    public class  JavaScriptIntent{
        public  JavaScriptIntent(){}
        @JavascriptInterface
        public void onShow(String hereid){
            gotoHereDetail(hereid);
        }
    }
    public void gotoHereDetail(String hereid){
        Bundle b = new Bundle();
        b.putBoolean("isThere",true);
        b.putString("hereId",hereid);
        Intent i = new Intent(context,HereDetailActivity.class);
        i.putExtras(b);
        startActivity(i);
    }

    /** 取坐标 */
    public class  JavaScriptHereIntent{
        public  JavaScriptHereIntent(){}
        @JavascriptInterface
        public void onHere(String x, String y){
            getXy(x,y);
        }
    }
    private void getXy(String x, String y) {
        this.x = x;
        this.y = y;
    }

    /** 取子场景ID */
    public class  JavaScriptIDIntent{
        public  JavaScriptIDIntent(){}
        @JavascriptInterface
        public void onID(String subpanoId){
            getSubpanoId(subpanoId);
        }
    }
    private void getSubpanoId(String subpanoId) {
        this.subpanoId = subpanoId;
    }

    /** 取场景ID */
    public class  JavaScriptHereIDIntent{
        public  JavaScriptHereIDIntent(){}
        @JavascriptInterface
        public void onHereId(String hereId){
            getHereId(hereId);
        }
    }

    /** 验证Here */
    private void getHereId(String hereId) {
        fhereId = hereId;
        Map<String, String> map = new HashMap<>();
        map.put("hereId", hereId);
        Request<JSONObject> request = new NormalPostRequest(Constants.VERIFYHERE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            webView.loadUrl("javascript:Pano.event['triggerLongpress']" + "(" + fhereId + ")");
                        } else {
//                            ToastUtil.showToast(context, JsonUtil.getStr(jsonObject, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    /** 横屏 */
    public class  JSLandscapeIntent{
        public  JSLandscapeIntent(){}
        @JavascriptInterface
        public void onLandscape(){
            landScape();
        }
    }
    private void landScape() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
    }

    /** 竖屏 */
    public class  JSPortraitIntent{
        public  JSPortraitIntent(){}
        @JavascriptInterface
        public void onPortrait(){
            setPortrait();
        }
    }
    private void setPortrait() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
    }

    /** 全局音乐和重力感应 */
    public class  JSMusicIntent{
        public  JSMusicIntent(){}
        @JavascriptInterface
        public boolean onStatus(String index){
            if(index.equals("0")){
                return isMusicOpen;
            }else {
                return isGravityOpen;
            }
        }
    }

    /** 长按标签显示等待菊花 */
    public class  JSLoadingShowInterface{
        public  JSLoadingShowInterface(){}
        @JavascriptInterface
        public void onLoadingShow(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogJs.show();
                }
            });
        }
    }

    /** 长按标签取消等待菊花 */
    public class  JSLoadingDisInterface{
        public  JSLoadingDisInterface(){}
        @JavascriptInterface
        public void onLoadingDismiss(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialogJs.dismiss();
                }
            });
        }
    }

    /** 分享 */
    private void shareDialog(boolean isCollection){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        shareDialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.sharepopupwindow, null);
        TextView collection = (TextView) view.findViewById(R.id.collection);
        if (isCollection) {
            collection.setText(getString(R.string.collectioned));
        } else {
            collection.setText(getString(R.string.collection));
        }
        LinearLayout webchat = (LinearLayout) view.findViewById(R.id.share_webchat);
        LinearLayout webchat_friend = (LinearLayout) view.findViewById(R.id.share_webchat_friend);
        LinearLayout qq = (LinearLayout) view.findViewById(R.id.share_qq);
        LinearLayout weibo = (LinearLayout) view.findViewById(R.id.share_weibo);
        Button cancel = (Button) view.findViewById(R.id.share_cancel);
        Button copy_link = (Button) view.findViewById(R.id.copy_link);
        DialogOnClick c = new DialogOnClick();
        cancel.setOnClickListener(c);
        webchat.setOnClickListener(c);
        collection.setOnClickListener(c);
        webchat_friend.setOnClickListener(c);
        qq.setOnClickListener(c);
        weibo.setOnClickListener(c);
        copy_link.setOnClickListener(c);
        shareDialog.setView(view);
        shareDialog.setCancelable(false);
        shareDialog.show();
    }

    /** PopupWindow控件的点击事件 */
    class DialogOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.share_webchat://分享
                    share_WxFriend();
                    shareDialog.dismiss();
                    break;
                case R.id.share_webchat_friend://分享朋友圈
                    share_WeChatFriend();
                    shareDialog.dismiss();
                    break;
                case R.id.share_qq://分享qq
                    share_QQFriend();
                    shareDialog.dismiss();
                    break;
                case R.id.share_weibo://分享微博
                    share_SinaWeibo();
                    shareDialog.dismiss();
                    break;
                case R.id.copy_link://复制链接
                    shareDialog.dismiss();
                    break;
                case R.id.share_cancel://取消
                    shareDialog.dismiss();
                    break;
                case R.id.collection://收藏
                    shareDialog.dismiss();
                    if ((boolean)SpfUtil.get(SceneActivity.this,"isLogin",false)){
                        if (isCollection) {
                            showCollectionDialog();
                        } else {
                            collection();
                        }
                    }else {
                        Intent i= new Intent(SceneActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void showCollectionDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SceneActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(SceneActivity.this).inflate(R.layout.popuwindow_cancelcollection, null);
        Button cancel = (Button) view.findViewById(R.id.cancel_collection);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cancelCollection();
            }
        });
        Button invest_rightnow = (Button) view.findViewById(R.id.cancel_dialog);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    /** 收藏 */
    private void collection() {
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", sceneId);
        Request<JSONObject> request = new NormalPostRequest(Constants.COLLECTION_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        isCollection=true;
                        CommonUtil.showMyToast(view_collection, context, "收藏成功，请在我的收藏里面查看。", R.drawable.icon_collection_success);// 显示错误提示框
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    /** 取消收藏 */
    private void cancelCollection() {
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", sceneId);
        Request<JSONObject> request = new NormalPostRequest(Constants.COLLECTIONCANCEL_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        isCollection=false;
                        CommonUtil.showMyToast(view,SceneActivity.this,"收藏已取消",R.drawable.icon_collection_cancel);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    /** 分享到微信好友 */
    private void share_WxFriend() {
        Platform circle = ShareSDK.getPlatform(Wechat.NAME);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(name);
        sp.setText(summary);
        sp.setImageData(null);
        sp.setImageUrl(thumb);
        sp.setImagePath("");
        sp.setUrl("http://172.16.180.46/pano/view?panoId=" + panoId);
        circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

    /** 分享到朋友圈 */
    private void share_WeChatFriend() {
        Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(name);
        if(summary.equals("")){
            summary="默认内容";
        }
        sp.setText(summary);
        sp.setImageData(null);
        sp.setImageUrl(thumb);
        sp.setImagePath("");
        sp.setUrl("http://172.16.180.46/pano/view?panoId=" + panoId);
        circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

    /** 分享到QQ好友 */
    private void share_QQFriend() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(name);
        sp.setTitleUrl("http://172.16.180.46/pano/view?panoId=" + panoId);
        if(summary.equals("")){
            summary="默认内容";
        }
        sp.setText(summary);
        sp.setImageUrl(thumb);
        sp.setImagePath("");
        Platform circle = ShareSDK.getPlatform( QQ.NAME);
        circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

    /** 分享到新浪微博 */
    private void share_SinaWeibo() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);// 一定要设置分享属性
        sp.setTitle(name);
        sp.setTitleUrl("http://172.16.180.46/pano/view?panoId=" + panoId);
        if(summary.equals("")){
            summary="默认内容";
        }
        sp.setText(summary);
        sp.setImageUrl(thumb);
        sp.setImagePath("");
        Platform circle = ShareSDK.getPlatform( SinaWeibo.NAME);
        circle.setPlatformActionListener(this); // 设置分享事件回调
        // 执行图文分享
        circle.share(sp);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public void onCancel(Platform platform, int action) {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        String text = actionToString(msg.arg2);
        switch (msg.arg1) {
            case 1:
                // 成功
                text = "分享成功";
                showToast(text);
                SceneActivity.this.finish();
                break;
            case 2:
                // 失败
                text = "分享失败";
                break;
            case 3:
                // 取消
                text = "分享已取消";
                break;
        }
        showToast(text);
        return false;
    }

    /** 将action转换为String */
    public static String actionToString(int action) {
        switch (action) {
            case Platform.ACTION_AUTHORIZING:
                return "ACTION_AUTHORIZING";
            case Platform.ACTION_GETTING_FRIEND_LIST:
                return "ACTION_GETTING_FRIEND_LIST";
            case Platform.ACTION_FOLLOWING_USER:
                return "ACTION_FOLLOWING_USER";
            case Platform.ACTION_SENDING_DIRECT_MESSAGE:
                return "ACTION_SENDING_DIRECT_MESSAGE";
            case Platform.ACTION_TIMELINE:
                return "ACTION_TIMELINE";
            case Platform.ACTION_USER_INFOR:
                return "ACTION_USER_INFOR";
            case Platform.ACTION_SHARE:
                return "ACTION_SHARE";
            default: {
                return "UNKNOWN";
            }
        }
    }
}
