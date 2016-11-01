package com.yuntong.here.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.adapter.CityAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.CityData;
import com.yuntong.here.holder.CityHolder;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.StatusBarUtil;
import com.yuntong.here.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by me on 2016/4/25.
 * 城市列表
 */
public class CityListActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ImageView closeImg;// 关闭
    private ListView listView;// 城市列表
    private CityData cityData;
    private CityAdapter adapter;
    private List<CityData> arrayList;
    private boolean isChoice;
    private int currentPosition;// 当前点击的item项
    private JSONObject jsonObject;
    private String a ="{\"status\":1,\"error\":null,\"model\":[{\"cityId\":1,\"code\":\"hangzhou\",\"thumb\":\"http://images.duc.cn/group1/M00/2C/97/c-dlOFc1Yp6AO_d0AANZlx6ZNho735.png\",\"name\":\"杭州\",\"status\":4},{\"cityId\":2,\"code\":\"shanghai\",\"thumb\":\"http://images.duc.cn/group1/M00/2C/97/c-dlOFc1Yp6AO_d0AANZlx6ZNho735.png\",\"name\":\"上海\",\"status\":1},{\"cityId\":3,\"code\":\"beijing\",\"thumb\":\"http://images.duc.cn/group1/M00/2C/97/c-dlOFc1Yp6AO_d0AANZlx6ZNho735.png\",\"name\":\"北京\",\"status\":2},{\"cityId\":4,\"code\":\"shenzhen\",\"thumb\":\"http://images.duc.cn/group1/M00/2C/97/c-dlOFc1Yp6AO_d0AANZlx6ZNho735.png\",\"name\":\"深圳\",\"status\":3}],\"success\":true}";



    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_city_list;
    }

    @Override
    protected void initData() {
        jsonObject = JsonUtil.convertJsonObj(a);
        arrayList = new ArrayList<>();
        adapter = new CityAdapter(this);

    }

    @Override
    protected void initView() {
        closeImg = (ImageView) findViewById(R.id.img_city_close);
        closeImg.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.list_city);

    }

    @Override
    protected void bindView() {
        getCityData();
//        listView.setOnItemClickListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.img_city_close:
                CityListActivity.this.finish();
                break;
        }
    }

    /**
     * 城市列表的点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPosition = position;
        isChoice = true;
        CityHolder holder = (CityHolder) view.getTag();
        holder.relChoice.setVisibility(View.VISIBLE);
        Intent i = new Intent();
        i.putExtra("cityname",arrayList.get(position).getName());
        setResult(110, i);// 设置返回值给ContactActivity
        CityListActivity.this.finish();
    }

    /**
     * 请求城市列表数据
     */
    private void getCityData() {
        JSONArray jsonArray = JsonUtil.convertJsonArry(jsonObject,"model");// 获取数组
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject object = JsonUtil.convertJsonObj(jsonArray, i);// 获取数组中的对象
                                // 给实体类赋值
                                try {
                                    cityData = new CityData(
                                            object.getInt("cityId"),
                                            object.getInt("status"),
                                            object.getString("name"),
                                            object.getString("thumb"),
                                            object.getString("code")
                                    );
                                    arrayList.add(cityData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setData(arrayList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
