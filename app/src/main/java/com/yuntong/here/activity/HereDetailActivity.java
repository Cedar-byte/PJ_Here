package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.yuntong.here.R;
import com.yuntong.here.adapter.HereDtailCommentAdapter;
import com.yuntong.here.adapter.ImgAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.HereDetailCommentData;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GlideCircleTransform;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.view.GalleryFlow;
import com.yuntong.here.view.ICallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/12.
 * here详情
 */
public class HereDetailActivity extends BaseActivity {

    private Context context = HereDetailActivity.this;
    private RelativeLayout relative;// 整个标题栏
    private TextView titleName;// 标题的文字显示
    private RelativeLayout title_back;//返回
    private ListView listView;// here的评论列表
    private View view;
    private TextView herer_name;
    private TextView here_time;
    private ImageView here_sex;
    private ImageView here_image;
    private TextView here_content;
    private TextView scene_name1;
    private TextView heredetail_zan;
    private TextView heredetail_comment;
    private LinearLayout upvote;
    private ImageView   simple_image;
    private View popuView;
    private LinearLayout comment;
    private ImageView upvote_image;
    private ImgAdapter imgAdapter;
    private ArrayList<String> imgData = new ArrayList<String>(); // 滚动数据
    private GalleryFlow galleryFlow; // 横向滚动图片
    private boolean isTouch = false; // 是否触摸滚动图片
    private long perWaitTime = 150000; // 每次等待时间
    private boolean isCanwork = true;// 控制自动滑动
    private boolean isThere = false;// 判断是否是从场景页面进入的
    private Bundle extras;
    private String hereId;
    private String thumb;
    Handler autoGalleryHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 0) {
                galleryFlow.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
            } else {
            }
        }
    };
    private HereDtailCommentAdapter hereDtailCommentAdapter;
    private List<HereDetailCommentData> array = new ArrayList<>();
    private RequestQueue mQueue;// 请求队列
    private int j = 0;
    private String sceneId, panoId, sceneName;
    private LinearLayout here_null;
    private TextView bg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_heredetail;
    }

    @Override
    protected void initData() {
        extras = getIntent().getExtras();
        hereDtailCommentAdapter = new HereDtailCommentAdapter(this);
        mQueue = MyApplication.getInstance().requestQueue;// 初始化请求队列
        isThere = extras.getBoolean("isThere");
        hereId = extras.getString("hereId");
        getHereDetail();
    }

    @Override
    protected void initView() {
        bg = (TextView) findViewById(R.id.bg);
        listView = (ListView) findViewById(R.id.mylistview);
        titleName = (TextView) findViewById(R.id.title_text1);
        popuView = getLayoutInflater().inflate(R.layout.pop_hint, null);
        view = getLayoutInflater().inflate(R.layout.heredatail_dead, null);
        relative = (RelativeLayout) findViewById(R.id.rel_title_hd);
        galleryFlow = (GalleryFlow) view.findViewById(R.id.galleryFlow);
        title_back = (RelativeLayout) findViewById(R.id.title_back1);
        herer_name = (TextView) view.findViewById(R.id.herer_name);
        here_time = (TextView) view.findViewById(R.id.here_time);
        here_sex = (ImageView) view.findViewById(R.id.herer_sex);
        here_image = (ImageView) view.findViewById(R.id.here_image);
        here_content = (TextView) view.findViewById(R.id.here_content);
        scene_name1 = (TextView) view.findViewById(R.id.scene_name1);
        simple_image=(ImageView)view.findViewById(R.id.simple_image);
        simple_image.setOnClickListener(this);
        scene_name1.setOnClickListener(this);
        heredetail_zan = (TextView) view.findViewById(R.id.heredetail_zan);
        heredetail_comment = (TextView) view.findViewById(R.id.heredetail_comment);
        upvote = (LinearLayout) view.findViewById(R.id.upvote);
        comment = (LinearLayout) view.findViewById(R.id.comment);
        upvote_image = (ImageView) view.findViewById(R.id.upvote_image);
        here_null = (LinearLayout) findViewById(R.id.here_null);
        comment.setOnClickListener(this);
        upvote.setOnClickListener(this);
        title_back.setOnClickListener(this);
        bg.setAlpha(0.0f);
    }

    @Override
    protected void bindView() {
        listView.addHeaderView(view);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到顶部
                        if (listView.getFirstVisiblePosition() == 0) {
                            relative.setBackground(null);
                            titleName.setText(j + "/" + imgData.size());
                        }
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollHeight = getScrollY();
                if (scrollHeight > getResources().getDimension(R.dimen.head_view_hight)) {
                    relative.setBackground(getResources().getDrawable(R.color.green));
                    titleName.setTextColor(getResources().getColor(R.color.white));
                    titleName.setText(getResources().getString(R.string.here_detail));
                }
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.title_back1:
                HereDetailActivity.this.finish();
                break;
            case R.id.upvote:
                setUpvote();
                break;
            case R.id.comment:
                showcommentPopupWindow();
                break;
            case R.id.simple_image:
                Intent i1 = new Intent(HereDetailActivity.this, ImageShower.class);
                i1.putStringArrayListExtra("imgData", imgData);
                i1.putExtra("i", "1");
                startActivity(i1);
                break;
            //  点击标签跳全景
            case R.id.scene_name1:
                if (isThere == true) {
                    HereDetailActivity.this.finish();
                } else {
                    Intent i = new Intent();
                    i.setClass(context, SceneActivity.class);
                    i.putExtra("sceneId", sceneId);
                    i.putExtra("panoId", panoId);
                    i.putExtra("name", sceneName);
                    i.putExtra("thumb",thumb);
                    startActivity(i);
                }
                break;
        }

    }

    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    //获取here详情
    private void getHereDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("hereId", hereId);
        Request<JSONObject> request = new NormalPostRequest(Constants.HEREDETAIL_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONObject jsonObject1 = JsonUtil.getJsonObj(jsonObject, "model");
                            sceneId = JsonUtil.getStr(jsonObject1, "sceneId");
                            sceneName = JsonUtil.getStr(jsonObject1, "sceneName");
                            panoId = JsonUtil.getStr(jsonObject1, "panoId");
                            thumb = JsonUtil.getStr(jsonObject1, "thumb");
                            JSONArray jsonArray = JsonUtil.convertJsonArry(jsonObject1, "hereMediaDTOList");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject bannerjson = JsonUtil.convertJsonObj(jsonArray, i);
                                String imageurl = JsonUtil.getStr(bannerjson, "url");
                                imgData.add(imageurl);
                            }
                            if (imgData.size()==1){
                                simple_image.setVisibility(View.VISIBLE);
                                galleryFlow.setVisibility(View.GONE);
                                titleName.setText("1/1");
                                ImageLoaderUtil.disPlayBig(imgData.get(0), simple_image);
                            }else {
                                simple_image.setVisibility(View.GONE);
                                titleName.setText("1/" + imgData.size());
                                getHeadView();
                            }

                            herer_name.setText(JsonUtil.getStr(jsonObject1, "masterName"));
                            here_content.setText(JsonUtil.getStr(jsonObject1, "text"));
                            Glide.with(HereDetailActivity.this)
                                    .load(JsonUtil.getStr(jsonObject1, "masterPic"))
                                    .transform(new GlideCircleTransform(HereDetailActivity.this))
                                    .into(here_image);
                            if (JsonUtil.getStr(jsonObject1, "gender").equals("1")) {
                                here_sex.setImageResource(R.drawable.icon_man_small);
                            }
                            here_time.setText(CommonUtil.getTimeto(Long.valueOf(JsonUtil.getStr(jsonObject1, "createTime"))));
                            scene_name1.setText(sceneName);
                            heredetail_zan.setText(JsonUtil.getStr(jsonObject1, "upvoteNum"));
                            heredetail_comment.setText(JsonUtil.getStr(jsonObject1, "commentNum"));
                            if (JsonUtil.getStr(jsonObject1, "isUpvote").equals("0")) {
                                upvote.setClickable(false);
                                upvote_image.setImageResource(R.drawable.icon_zan_green);
                            }
                            array.clear();
                            JSONArray commentarray = JsonUtil.convertJsonArry(jsonObject1, "commentDetailDTOList");
                            for (int i = 0; i < commentarray.length(); i++) {
                                JSONObject commentjson = JsonUtil.convertJsonObj(commentarray, i);
                                HereDetailCommentData hereDetailCommentData = new HereDetailCommentData();
                                hereDetailCommentData.setItem_heredetail_content(JsonUtil.getStr(commentjson, "text"));
                                hereDetailCommentData.setItem_heredetail_time(JsonUtil.getStr(commentjson, "createTime"));
                                hereDetailCommentData.setItem_heredetail_sex(JsonUtil.getStr(commentjson, "gender"));
                                hereDetailCommentData.setItem_heredetail_image(JsonUtil.getStr(commentjson, "namePic"));
                                hereDetailCommentData.setItem_heredetail_name(JsonUtil.getStr(commentjson, "name"));
                                array.add(hereDetailCommentData);
                            }
                            hereDtailCommentAdapter.setData(array);
                            listView.setAdapter(hereDtailCommentAdapter);
                            hereDtailCommentAdapter.notifyDataSetChanged();
                        } else if (JsonUtil.getStr(jsonObject, "status").equals("2")) {
                            listView.setVisibility(View.GONE);
                            here_null.setVisibility(View.VISIBLE);
                            relative.setBackground(getResources().getDrawable(R.color.green));
                            titleName.setTextColor(getResources().getColor(R.color.white));
                            titleName.setText(getResources().getString(R.string.here_detail));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }, map);
        mQueue.add(request);
    }

    //开始图片轮播
    protected void getHeadView() {
        imgAdapter = new ImgAdapter(HereDetailActivity.this, imgData);
        new Thread() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(0, new ICallback() {
                    @Override
                    public void run() {
                        if (imgData == null || imgData.size() == 0) {
                            return;
                        }
                        galleryFlow.setAdapter(imgAdapter);
                        galleryFlow.setSelection(imgData.size() * 100);
                        galleryFlow.setOnItemClickListener(onItemClickListener);
                        galleryFlow.setOnItemSelectedListener(imgOnItemSelectedListener);

                        new Thread() {
                            @Override
                            public void run() {
                                while (isCanwork) {
                                    try {
                                        Thread.sleep(perWaitTime);
                                        if (!isTouch) {
                                            autoGalleryHandler.sendEmptyMessage(0);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            ;
                        }.start();
                    }
                });
                handler.sendMessage(msg);
            }
        }.start();

        galleryFlow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    isTouch = true;
                } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
                    isTouch = false;
                }
                return false;
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ICallback callback = (ICallback) msg.obj;
            if (callback != null) {
                callback.run();
            }
        }
    };
    //轮播图片点击事件
    protected AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (imgData.size() > 0) {
                Intent i = new Intent(HereDetailActivity.this, ImageShower.class);
                i.putStringArrayListExtra("imgData", imgData);
                i.putExtra("i", String.valueOf(j));
                startActivity(i);
            }
        }
    };
    //图片切换响应事件
    protected AdapterView.OnItemSelectedListener imgOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            j = position % imgData.size() + 1;
            titleName.setText(j + "/" + imgData.size());
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    };

    //点赞
    private void setUpvote() {
        Map<String, String> map = new HashMap<>();
        map.put("hereId", getIntent().getStringExtra("hereId"));
        Request<JSONObject> request = new NormalPostRequest(Constants.UPVOTE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            upvote.setClickable(false);
                            heredetail_zan.setText((Integer.valueOf(heredetail_zan.getText().toString()) + 1) + "");
                            upvote_image.setImageResource(R.drawable.icon_zan_green);
                            CommonUtil.showMyToast(popuView, HereDetailActivity.this, "点赞成功!", R.drawable.icon_comment_success);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        mQueue.add(request);
    }

    //评论
    private void setComment(final String content) {
        Map<String, String> map = new HashMap<>();
        map.put("hereId", getIntent().getStringExtra("hereId"));
        map.put("text", content);
        Request<JSONObject> request = new NormalPostRequest(Constants.COMMENT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            heredetail_comment.setText((Integer.valueOf(heredetail_comment.getText().toString()) + 1) + "");
                            CommonUtil.showMyToast(popuView, HereDetailActivity.this, "评论成功!", R.drawable.icon_comment_success);
                            HereDetailCommentData hereDetailCommentData = new HereDetailCommentData();
                            hereDetailCommentData.setItem_heredetail_name((String) SpfUtil.get(HereDetailActivity.this, "nickname", ""));
                            hereDetailCommentData.setItem_heredetail_image((String) SpfUtil.get(HereDetailActivity.this, "headPic", ""));
                            hereDetailCommentData.setItem_heredetail_sex((String) SpfUtil.get(HereDetailActivity.this, "gender", ""));
                            hereDetailCommentData.setItem_heredetail_time(String.valueOf(System.currentTimeMillis()));
                            hereDetailCommentData.setItem_heredetail_content(content);
                            hereDtailCommentAdapter.addData(hereDetailCommentData);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        mQueue.add(request);
    }

    public void showcommentPopupWindow() {
        final Dialog dialog = new Dialog(this, R.style.DialogStyle);
        View view = LayoutInflater.from(HereDetailActivity.this).inflate(R.layout.comentedt, null);
        Button invest_rightnow = (Button) view.findViewById(R.id.btn_commnet);
        final EditText comment = (EditText) view.findViewById(R.id.edt_comment);
        openInputMethod(comment);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setComment(comment.getText().toString());
            }
        });
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth());    //宽度设置为屏幕的0.5
        dialog.getWindow().setAttributes(p);     //设置生效
    }

    //调用软键盘
    public void openInputMethod(final EditText editText) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }
        }, 200);
    }
}
