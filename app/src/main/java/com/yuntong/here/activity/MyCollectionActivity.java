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
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.adapter.CollectionAdapter;
import com.yuntong.here.adapter.SearchAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.MyhereData;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity{
    private ListView mycollection_list;
    private CollectionAdapter  myCollectionAdapter;
    private SceneListData collectionData;
    private List<SceneListData> collectionList;
    private RequestQueue mQueue;// 请求队列
    private TextView no_collection;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_my_collection;
    }

    @Override
    protected void initData() {
        mQueue = MyApplication.getInstance().requestQueue;// 初始化请求队列
        collectionList = new ArrayList<>();
        popupWindow = new PopupWindow();
        getMyCollectionList();
    }

    @Override
    protected void initView() {
        setTitleElse(getString(R.string.mycollectioned));
        mycollection_list=(ListView)findViewById(R.id.mycollection_list);
        no_collection=(TextView)findViewById(R.id.no_collection);
        myCollectionAdapter=new CollectionAdapter(this);
    }

    @Override
    protected void bindView() {
        mycollection_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i =new Intent(MyCollectionActivity.this,SceneActivity.class);
                i.putExtra("sceneId",collectionList.get(position).getSceneId());
                i.putExtra("panoId",collectionList.get(position).getPanoId());
                i.putExtra("name",collectionList.get(position).getName());
                i.putExtra("thumb",collectionList.get(position).getThumb());
                startActivity(i);
            }
        });
        mycollection_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showcancelPopupWindow(collectionList.get(position).getSceneId(),position);
                return true;
            }
        });
    }

    @Override
    protected void widgetClick(View v) {

    }


    private void getMyCollectionList() {
        Map<String, String> map = new HashMap<>();
        map.put("lng", (String) SpfUtil.get(MyCollectionActivity.this, "lon", "120.12479"));
        map.put("lat", (String) SpfUtil.get(MyCollectionActivity.this,"lat","30.335589"));
        Request<JSONObject> request = new NormalPostRequest(Constants.MYCOLLECTION_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONArray jsonArray=JsonUtil.convertJsonArry(jsonObject,"model");// 获取数组
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = JsonUtil.convertJsonObj(jsonArray, i);// 获取数组中的对象
                                    try {
                                        collectionData = new SceneListData(
                                                object.getInt("sceneId"),
                                                object.getString("sceneName"),
                                                object.getString("address"),
                                                object.getString("thumb"),
                                                object.getInt("heat"),
                                                object.getInt("panoId"),
                                                object.getDouble("distance"));
                                        collectionList.add(collectionData);// 给集合填充数据
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                myCollectionAdapter.setData(collectionList);
                                mycollection_list.setAdapter(myCollectionAdapter);
                                myCollectionAdapter.notifyDataSetChanged();
                            }
                        } else if (JsonUtil.getStr(jsonObject, "status").equals("3")){
                            no_collection.setVisibility(View.VISIBLE);
                        }else {
                            showToast(JsonUtil.getStr(jsonObject, "error"));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        mQueue.add(request);
    }

    public void showcancelPopupWindow(final int sceneId,final int i) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyCollectionActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.popuwindow_collectioncancel, null);
        Button invest_rightnow = (Button) view.findViewById(R.id.cancel_collection);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelCollection(sceneId,i);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    //取消收藏
    private void cancelCollection(int sceneId, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("sceneId", String.valueOf(sceneId));
        map.put("userId", (String)SpfUtil.get(this,"userId",""));
        Request<JSONObject> request = new NormalPostRequest(Constants.COLLECTIONCANCEL_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            myCollectionAdapter.removeData(collectionList.get(i));
                            collectionList.remove(i);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        mQueue.add(request);
    }
}
