package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.adapter.MyAdapter;
import com.yuntong.here.adapter.MyHereAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.MyhereData;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.holder.MyHereHolder;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 我的Here
 */
public class MyHereActivity extends BaseActivity {

    private ListView myhere_list;
    private MyHereAdapter myHereAdapter;
    private RequestQueue mQueue;// 请求队列
    private ArrayList<MyhereData> myhereDatas = new ArrayList<MyhereData>();
    private TextView no_here;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_my_here;
    }

    @Override
    protected void initData() {
        mQueue = MyApplication.getInstance().requestQueue;// 初始化请求队列
        popupWindow=new PopupWindow();
        getMyhereList();
    }

    @Override
    protected void initView() {
        setTitleElse(getString(R.string.myhere));
        myhere_list = (ListView) findViewById(R.id.my_here_list);
        no_here = (TextView) findViewById(R.id.no_here);
        myHereAdapter = new MyHereAdapter(this);
        no_here.setOnClickListener(this);

    }

    @Override
    protected void bindView() {
        myhere_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MyHereActivity.this,HereDetailActivity.class);
                i.putExtra("hereId",myhereDatas.get(position).getHereId());
                startActivity(i);
            }
        });
        myhere_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showcancelPopupWindow(myhereDatas.get(position).getHereId(), position);
                return true;
            }
        });

    }

    @Override
    protected void widgetClick(View v) {

    }
    //我的here收藏列表
    private void getMyhereList() {
        Map<String, String> map = new HashMap<>();
        Request<JSONObject> request = new NormalPostRequest(Constants.MYHERE_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONArray jsonArray = JsonUtil.convertJsonArry(jsonObject, "model");
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MyhereData myhereData = new MyhereData();
                                    JSONObject json = JsonUtil.convertJsonObj(jsonArray, i);
                                    JSONArray array = JsonUtil.convertJsonArry(json, "hereMediaDTOList");
                                    myhereData.setMy_here_comment(JsonUtil.getStr(json, "commentNum"));
                                    myhereData.setMy_here_date(CommonUtil.getTimetoYMD(Long.valueOf(JsonUtil.getStr(json, "createTime"))));
                                    myhereData.setMy_here_time(CommonUtil.getTimetotime(Long.valueOf(JsonUtil.getStr(json, "createTime"))));
                                    myhereData.setMy_here_imagenum("共" + array.length() + "张");
                                    myhereData.setMyhere_name(JsonUtil.getStr(json, "sceneName"));
                                    myhereData.setMyhere_content(JsonUtil.getStr(json, "text"));
                                    myhereData.setMy_here_zan(JsonUtil.getStr(json, "upvoteNum"));
                                    myhereData.setMy_here_image(JsonUtil.getStr(JsonUtil.convertJsonObj(array, 0), "url"));
                                    myhereData.setHereId(JsonUtil.getStr(json, "hereId"));
                                    myhereDatas.add(myhereData);
                                }
                                myHereAdapter.setData(myhereDatas);
                                myhere_list.setAdapter(myHereAdapter);
                            }
                        } else if (JsonUtil.getStr(jsonObject, "status").equals("3")){
                            no_here.setVisibility(View.VISIBLE);
                        }else {
                            showToast(JsonUtil.getStr(jsonObject, "error"));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        mQueue.add(request);
    }

    public void showcancelPopupWindow(final String hereId, final int i) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyHereActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(MyHereActivity.this).inflate(R.layout.popuwindow_cancelhere, null);
        TextView invest_rightnow = (TextView) view.findViewById(R.id.cancel_here);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelHere(hereId, i);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    //删除here
    private void cancelHere(String sceneId, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("hereId", sceneId);
        Request<JSONObject> request = new NormalPostRequest(Constants.MYHERECANCEL_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            myHereAdapter.removeData(myhereDatas.get(i));
                            myhereDatas.remove(i);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        mQueue.add(request);
    }
}
