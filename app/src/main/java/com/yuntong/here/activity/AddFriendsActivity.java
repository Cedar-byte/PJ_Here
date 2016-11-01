package com.yuntong.here.activity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.MainActivity;
import com.yuntong.here.R;
import com.yuntong.here.adapter.SearchUsersAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.SceneListData;
import com.yuntong.here.entity.UserData;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 添加好友
 */
public class AddFriendsActivity extends BaseActivity {

    private ImageView ok,delete;// 搜索，删除搜索框中的内容
    private EditText search;// 搜索框
    private TextView cancel;// 取消
    private ListView listView;// 显示搜索结果列表
    private TextView name, num;
    private LinearLayout linearLayout;
    private List<UserData> arrayList;
    private SearchUsersAdapter adapter;
    private UserData userData;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_add_friends;
    }

    @Override
    protected void initData() {
        arrayList = new ArrayList<>();
        adapter = new SearchUsersAdapter(this, arrayList, R.layout.item_list_search_users);
    }

    @Override
    protected void initView() {
        linearLayout = (LinearLayout) findViewById(R.id.lay_search_result);
        name = (TextView) findViewById(R.id.search_name);
        num = (TextView) findViewById(R.id.search_num);
        listView = (ListView) findViewById(R.id.list_search_user);
        search = (EditText) findViewById(R.id.edt_search_friends);
        cancel = (TextView) findViewById(R.id.txt_cancel);
        cancel.setOnClickListener(this);
        ok = (ImageView) findViewById(R.id.image_ok);
        ok.setOnClickListener(this);
        delete = (ImageView) findViewById(R.id.img_delete_edt);
        delete.setOnClickListener(this);
    }

    @Override
    protected void bindView() {
        listView.setAdapter(adapter);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (search.getText().toString() != null) {
                        arrayList.clear();
                        searchUsers();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            // 取消
            case R.id.txt_cancel:
                AddFriendsActivity.this.finish();
                break;
            // 搜索
            case R.id.image_ok:
                arrayList.clear();
                searchUsers();
                break;
            // 删除
            case R.id.img_delete_edt:
                search.setText("");
                break;
        }
    }

    private void searchUsers() {
        String fuzzy = search.getText().toString();
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("fuzzy",fuzzy);
        Request<JSONObject> request = new NormalPostRequest(Constants.SEARCHUSERS_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONArray jsonArray=JsonUtil.convertJsonArry(jsonObject,"model");// 获取数组
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = JsonUtil.convertJsonObj(jsonArray, i);// 获取数组中的对象
                                userData = new UserData(
                                        JsonUtil.getStr(object,"userId"),
                                        JsonUtil.getStr(object,"name"),
                                        JsonUtil.getStr(object,"nickname"),
                                        JsonUtil.getStr(object,"headPic"),
                                        JsonUtil.getStr(object,"gender"),
                                        JsonUtil.getStr(object,"friend"),"0");
                                arrayList.add(userData);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    num.setText(arrayList.size() + "");
                                    name.setText("“" + search.getText().toString() + "”");
                                    if(arrayList.size() > 0){
                                        adapter.notifyDataSetChanged();
                                    }
                                    if(arrayList.size() == 0){
                                        arrayList.clear();
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }

    /**
     * 添加好友
     */

}
