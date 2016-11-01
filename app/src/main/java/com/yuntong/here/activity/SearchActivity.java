package com.yuntong.here.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.adapter.HistoryAdapter;
import com.yuntong.here.adapter.SearchAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.HistoryData;
import com.yuntong.here.entity.SceneListData;
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
 * 搜索
 */
public class SearchActivity extends BaseActivity {

    private ArrayAdapter<String> adapter_history;
    private EditText auto;
    private ImageView ok;
    private ImageView image_edt_delete;
    private ListView search_list;
    private EditText edt_search;
    private TextView clear_his;
    private String[] hisdata;
    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryData> historys = new ArrayList<HistoryData>();
    private ArrayList<String> newString = new ArrayList<String>();
    private ArrayList<String> arrayList = new ArrayList<>();//
    private View view;
    private LinearLayout lay_search_result;
    private TextView search_num;
    private TextView search_name;
    private TextView cancel;
    private SceneListData sceneData;
    private List<SceneListData> sceneList;
    private SearchAdapter searchAdapter;
    private RequestQueue mQueue;// 请求队列

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        mQueue = MyApplication.getInstance().requestQueue;// 初始化请求队列
        sceneList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        auto = (EditText) findViewById(R.id.edt_search);
        ok = (ImageView) findViewById(R.id.image_ok);
        edt_search = (EditText) findViewById(R.id.edt_search);
        image_edt_delete = (ImageView) findViewById(R.id.image_det_delete);
        search_list = (ListView) findViewById(R.id.search_list);
        lay_search_result = (LinearLayout) findViewById(R.id.lay_search_result);
        search_num = (TextView) findViewById(R.id.search_num);
        search_name = (TextView) findViewById(R.id.search_name);
        lay_search_result.setVisibility(View.GONE);
        LayoutInflater inflater = getLayoutInflater();
        view = (LinearLayout) inflater.inflate(R.layout.clear_history, null);
        clear_his = (TextView) view.findViewById(R.id.clear_his);
        historyAdapter = new HistoryAdapter(this);
        searchAdapter = new SearchAdapter(this);
        cancel=(TextView)findViewById(R.id.cancel);
        search_list.setAdapter(historyAdapter);
        hisdata = getSharedPreference("history");
        if (hisdata != null) {
            search_list.addFooterView(view);
            if (hisdata.length > 0 && hisdata.length <= 5) {
                for (int i = 0; i < hisdata.length; i++) {
                    HistoryData history = new HistoryData();
                    history.setHistory_item(hisdata[i]);
                    newString.add(hisdata[i]);
                    historys.add(history);
                }
                historyAdapter.setData(historys);
                search_list.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();
            } else if (hisdata.length > 5) {
                for (int i = hisdata.length; i>hisdata.length-5; i--) {
                    HistoryData history = new HistoryData();
                    history.setHistory_item(hisdata[i-1]);
                    newString.add(hisdata[i-1]);
                    historys.add(history);
                }
                historyAdapter.setData(historys);
                search_list.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();
            }
        }
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (edt_search.getText().toString() != null) {
                        addshareprefrence();
                        getSceneList();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void bindView() {
        auto.setOnClickListener(this);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        image_edt_delete.setOnClickListener(this);
        clear_his.setOnClickListener(this);
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(hisdata != null){
                    edt_search.setText(hisdata[position]);
                }
            }
        });
//        edt_search.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    // 先隐藏键盘
//                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                    if (edt_search.getText().toString() != null) {
//                        addshareprefrence();
//                        getSceneList();
//                    }
//                }
//                return false;
//            }
//        });
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.clear_his:
                search_list.setVisibility(View.GONE);
                clear_his.setVisibility(View.GONE);
                deleteSharePredPreference("history");
                break;
            case R.id.image_det_delete:
                edt_search.setText("");
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.image_ok:
                getSceneList();
                break;
        }
    }

    public String[] getSharedPreference(String key) {
        String regularEx = "#";
        String[] str = null;
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        String values;
        values = sp.getString(key, "");
        str = values.split(regularEx);
        if (str[0].equals("")) {
            return null;
        }
        return str;
    }

    public void setSharedPreference(String key, String[] values) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
            SharedPreferences.Editor et = sp.edit();
            et.putString(key, str);
            et.commit();
        }
    }

    public void deleteSharePredPreference(String key) {
        String regularEx = "#";
        String str = "";
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.clear().commit();
    }

    private void addshareprefrence() {
        if (edt_search.getText().toString() != null) {
            if (newString.size() != 0) {
                Boolean add = true;
                for (int i = 0; i < newString.size(); i++) {
                    if (edt_search.getText().toString().equals(newString.get(i))) {
                        add = false;
                    }
                }
                if (add){
                    newString.add(edt_search.getText().toString());
                }
            } else {
                newString.add(edt_search.getText().toString());
            }
            String[] arrString = (String[]) newString.toArray(new String[0]);
            setSharedPreference("history", arrString);
        }
    }


    private void getSceneList() {
        Map<String, String> map = new HashMap<>();
        map.put("cityCode", "hangzhou");
        map.put("fuzzy", edt_search.getText().toString());
        map.put("lng", (String) SpfUtil.get(SearchActivity.this, "lon", "120.12479"));
        map.put("lat", (String) SpfUtil.get(SearchActivity.this,"lat","30.335589"));
        Request<JSONObject> request = new NormalPostRequest(Constants.SCENELIST_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        sceneList.clear();
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONObject jsonObject1 = JsonUtil.getJsonObj(jsonObject,"model");
                            JSONObject jsonObject2=JsonUtil.getJsonObj(jsonObject1,"sceneModel");
                            JSONArray jsonArray=JsonUtil.convertJsonArry(jsonObject2,"data");// 获取数组
                            for (int i = 0;i< jsonArray.length();i++) {
                                JSONObject object = JsonUtil.convertJsonObj(jsonArray, i);// 获取数组中的对象
                                try {
                                    sceneData = new SceneListData(
                                            object.getInt("sceneId"),
                                            object.getString("name"),
                                            object.getString("address"),
                                            object.getString("thumb"),
                                            object.getInt("heat"),
                                            object.getInt("panoId"),
                                            object.getDouble("distance"));
                                    sceneList.add(sceneData);// 给集合填充数据
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            lay_search_result.setVisibility(View.VISIBLE);
                            search_num.setText(sceneList.size() + "");
                            search_name.setText("“" + edt_search.getText().toString() + "”");
                            search_list.setVisibility(View.VISIBLE);
                            search_list.removeFooterView(view);
                            searchAdapter.setData(sceneList);
                            search_list.setAdapter(searchAdapter);
                            searchAdapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }, map);
        mQueue.add(request);
    }
}
