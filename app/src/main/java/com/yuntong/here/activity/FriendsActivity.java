package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gxy.fastscrollrecyclerview.views.FastScrollRecyclerView;
import com.yuntong.here.R;
import com.yuntong.here.adapter.SortAdapter;
import com.yuntong.here.adapter.SpaceItemDecoration;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.pinyin.PinyinComparator;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.pinyin.CharacterParser;
import com.yuntong.here.util.sortlist.SortModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 好友
 */
public class FriendsActivity extends BaseActivity{

    private Context context = FriendsActivity.this;
    private FastScrollRecyclerView recyclerView;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private SortModel model;
    private List<SortModel> sourceDateList;
    private SortAdapter adapter;
    private RelativeLayout relativeLayout;// 添加好友
    private Intent intent;
    private RequestQueue requestQueue;
    private RelativeLayout back;


    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_friends;

    }

    @Override
    protected void initData() {
        intent = new Intent();
        requestQueue = MyApplication.getInstance().requestQueue;
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sourceDateList = new ArrayList<>();
        adapter = new SortAdapter(this, sourceDateList);
    }

    @Override
    protected void initView() {
        relativeLayout = (RelativeLayout) findViewById(R.id.rel_add_friends);
        relativeLayout.setOnClickListener(this);
        back=(RelativeLayout)this.findViewById(R.id.back);
        back.setOnClickListener(this);
        recyclerView = (FastScrollRecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        recyclerView.addItemDecoration(new SpaceItemDecoration(16));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void bindView() {
        getFriendsList();
        adapter.setOnItemClickListener(new SortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                intent.setClass(FriendsActivity.this, OtherPeopleActivity.class);
                intent.putExtra("otherId",sourceDateList.get(position).getUserId());
                startActivity(intent);
            }
        });
        adapter.setOnItemLongClickListener(new SortAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                String userid = sourceDateList.get(position).getUserId();
                showcancelPopupWindow(userid,position);
            }
        });
    }

    /**
     * 删除好友
     */
    private void deleteFriends(String userid) {
        Map<String, String> map = new HashMap<>();
        map.put("delId",userid);
        Request<JSONObject> request = new NormalPostRequest(Constants.DELETEFRIENDS_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        requestQueue.add(request);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.rel_add_friends:
                intent.setClass(FriendsActivity.this, AddFriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 获取好友列表
     */
    private void getFriendsList() {
        Map<String, String> map = new HashMap<>();
        Request<JSONObject> request = new NormalPostRequest(Constants.FRIENDSLIST_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if(JsonUtil.getStr(jsonObject, "status").equals("1")){
                            JSONArray jsonArray = JsonUtil.convertJsonArry(jsonObject,"model");
                            for (int i = 0; i < jsonArray.length(); i ++){
                                JSONObject object = JsonUtil.convertJsonObj(jsonArray,i);
                                JSONArray array = JsonUtil.convertJsonArry(object,"friendList");
                                for(int j = 0; j < array.length(); j++){
                                    JSONObject object1 = JsonUtil.convertJsonObj(array,j);
                                    model = new SortModel(
                                            JsonUtil.getStr(object1,"gender"),
                                            JsonUtil.getStr(object1,"headpic"),
                                            JsonUtil.getStr(object1,"status"),
                                            JsonUtil.getStr(object1,"nickname"),
                                            JsonUtil.getStr(object1,"pinyin"),
                                            JsonUtil.getStr(object1,"name"),
                                            JsonUtil.getStr(object1,"userId"));
                                    String pinyin = JsonUtil.getStr(object1,"pinyin");
//                                    String pinyin = characterParser.getSelling(JsonUtil.getStr(object1,"nickname"));// 汉字转拼音
                                    String sortString = pinyin.substring(0, 1).toUpperCase();
                                    // 正则表达式，判断首字母是否是英文字母
                                    if (sortString.matches("[A-Z]")) {
                                        model.setSortLetters(sortString);
                                    } else {
                                        model.setSortLetters("#");
                                    }
                                    Collections.sort(sourceDateList, pinyinComparator);// 排序
                                    sourceDateList.add(model);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, null);
        requestQueue.add(request);
    }

    public void showcancelPopupWindow(final String userid,final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.popuwindow_collectioncancel, null);
        TextView invest_rightnow = (TextView) view.findViewById(R.id.cancel_collection);
        invest_rightnow.setText("删除好友");
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriends(userid);
                sourceDateList.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
}
