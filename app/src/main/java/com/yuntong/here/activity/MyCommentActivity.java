package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.adapter.MyCommentAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.MyCommentData;
import com.yuntong.here.entity.MyhereData;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/16.
 */
public class MyCommentActivity extends BaseActivity {
    private ArrayList<MyCommentData> myCommentDatas;
    private ListView comment_list;
    private MyCommentAdapter myCommentAdapter;
    private PopupWindow popupWindow;
    private RequestQueue requestQueue;
    private View view_yes;
    private TextView no_comment;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_mycomment;
    }

    @Override
    protected void initData() {
        myCommentDatas = new ArrayList<>();
        myCommentAdapter = new MyCommentAdapter(this);
        popupWindow = new PopupWindow();
        requestQueue = MyApplication.getInstance().requestQueue;
        getMyComment();

    }

    @Override
    protected void initView() {
        setTitleElse(getString(R.string.mycomment));
        comment_list = (ListView) findViewById(R.id.mycomment_list);
        view_yes=getLayoutInflater().inflate(R.layout.pop_hint,null);
        no_comment=(TextView)findViewById(R.id.no_comment);

    }

    @Override
    protected void bindView() {
        comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (myCommentDatas.get(position).getIsDelete().equals("0")) {
                    Intent i = new Intent(MyCommentActivity.this, HereDetailActivity.class);
                    i.putExtra("hereId", myCommentDatas.get(position).getHereId());
                    startActivity(i);
                } else {
                    CommonUtil.showMyToast(view_yes,MyCommentActivity.this,"该HERE已被删除",R.drawable.icon_here_already_delete);
                }
            }
        });
        comment_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showcancelPopupWindow(position);
                return true;
            }
        });

    }

    @Override
    protected void widgetClick(View v) {

    }

    private void getMyComment() {
        Request<JSONObject> request = new NormalPostRequest(Constants.MYCOMMENT_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (JsonUtil.getStr(response, "status").equals("1")) {
                            JSONArray jsonArray = JsonUtil.convertJsonArry(response, "model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = JsonUtil.convertJsonObj(jsonArray, i);
                                MyCommentData myCommentData = new MyCommentData();
                                myCommentData.setHereId(JsonUtil.getStr(jsonObject, "hereId"));
                                myCommentData.setComment_content(JsonUtil.getStr(jsonObject, "text"));
                                myCommentData.setHere_name(JsonUtil.getStr(jsonObject, "name"));
                                myCommentData.setComment_time(JsonUtil.getStr(jsonObject, "createTime"));
                                myCommentData.setIsDelete(JsonUtil.getStr(jsonObject, "isDelete"));
                                myCommentData.setCommentId(JsonUtil.getStr(jsonObject, "id"));
                                myCommentDatas.add(myCommentData);
                            }
                            myCommentAdapter.setData(myCommentDatas);
                            comment_list.setAdapter(myCommentAdapter);

                        } else if (JsonUtil.getStr(response, "status").equals("3")){
                            no_comment.setVisibility(View.VISIBLE);
                        } else {
                            showToast(JsonUtil.getStr(response, "error"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, null);
        requestQueue.add(request);
    }

    public void showcancelPopupWindow(final int i) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MyCommentActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(MyCommentActivity.this).inflate(R.layout.popuwindow_cancelcomment, null);
        TextView invest_rightnow = (TextView) view.findViewById(R.id.cancel_comment);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelComment(i);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    //删除评论
    private void cancelComment(final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("hereId", myCommentDatas.get(i).getHereId());
        map.put("id", myCommentDatas.get(i).getCommentId());
        Request<JSONObject> request = new NormalPostRequest(Constants.MYCOMMENTCANCEL_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            myCommentAdapter.removeData(myCommentDatas.get(i));
                            myCommentDatas.remove(i);
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
