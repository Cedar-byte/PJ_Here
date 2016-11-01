package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.yuntong.here.R;
import com.yuntong.here.adapter.MessageAdapter;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.entity.MessageData;
import com.yuntong.here.sql.MessageDao;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/24.
 * 未读消息
 */
public class UnMessagedActivity extends BaseActivity implements MessageAdapter.AddCallBack {
    private ListView unmessaged_list;
    private View view;
    private Button btn_allmessage;
    private LinearLayout null_message;
    private RequestQueue requestQueue;
    private List<MessageData> messageDatas;
    private List<MessageData> allmessageDatas;
    private boolean succse = false;

    private MessageAdapter messageAdapter;
    private MessageDao messageDao;
    private String time;

    @Override
    protected int getLayoutId() {
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green_), 80);// 设置顶部栏的颜色和透明度
//        StatusBarUtil.setColor(this, getResources().getColor(R.color.green));// 设置顶部栏的颜色和透明度
        return R.layout.activity_unmessaged;
    }

    @Override
    protected void initData() {
        requestQueue = MyApplication.getInstance().requestQueue;
        messageDao = new MessageDao(this);
        messageDatas = new ArrayList<MessageData>();
        allmessageDatas = new ArrayList<MessageData>();
        getUnread();
    }

    @Override
    protected void initView() {
        setTitleElse(getString(R.string.listview_loading));
        unmessaged_list = (ListView) findViewById(R.id.unmessaged_list);
        view = getLayoutInflater().inflate(R.layout.allmessage_look, null);
        btn_allmessage = (Button) view.findViewById(R.id.all_msg);
        null_message = (LinearLayout) findViewById(R.id.null_message);
        btn_allmessage.setOnClickListener(this);
        unmessaged_list.addFooterView(view);
        btn_allmessage.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String agreeded = (String) SpfUtil.get(UnMessagedActivity.this, "agreeded", "");
        if (!agreeded.equals("")) {
            int potion = (int) SpfUtil.get(UnMessagedActivity.this, "agreededid", 0);
            if (agreeded.equals("1")) {
                messageDatas.get(potion).setIsClick("1");
                messageDao.updata(messageDatas.get(potion).getUserId(), messageDatas.get(potion).getYuntongid(), "1", messageDatas.get(potion).getType());
                SpfUtil.put(UnMessagedActivity.this, "agreeded", "");
            } else if (agreeded.equals("2")) {
                messageDatas.get(potion).setIsClick("2");
                messageDao.updata(messageDatas.get(potion).getUserId(), messageDatas.get(potion).getYuntongid(), "2", messageDatas.get(potion).getType());
                SpfUtil.put(UnMessagedActivity.this, "agreeded", "");
            }
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void bindView() {

    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.all_msg:
                setTitleElse(getString(R.string.allmessaged));
                if (succse) {
                    messageDatas.clear();
                    allmessageDatas = messageDao.getAll((String) SpfUtil.get(UnMessagedActivity.this, "userId", ""));
                    for (int i = allmessageDatas.size() - 1; i >= 0; i--) {
                        messageDatas.add(allmessageDatas.get(i));
                    }
                    messageAdapter.notifyDataSetChanged();
                    btn_allmessage.setVisibility(View.GONE);
                } else {
                    allmessageDatas = messageDao.getAll((String) SpfUtil.get(UnMessagedActivity.this, "userId", ""));
                    List<MessageData> messageDatas1 = new ArrayList<MessageData>();
                    for (int i = allmessageDatas.size() - 1; i >= 0; i--) {
                        messageDatas1.add(allmessageDatas.get(i));
                    }
                    messageDatas.addAll(messageDatas1);
                    messageAdapter.notifyDataSetChanged();
                    btn_allmessage.setVisibility(View.GONE);
                }
                break;

        }
    }

    private void getUnread() {
        Map<String, String> map = new HashMap<>();
        Request<JSONObject> request = new NormalPostRequest(Constants.GETUNREAD,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            messageDatas.clear();
                            JSONObject jsonObject1 = JsonUtil.getJsonObj(jsonObject, "model");
                            time = JsonUtil.getStr(jsonObject1, "selectTime");
                            JSONArray jsonArray = JsonUtil.convertJsonArry(jsonObject1, "adviceList");
                            if (jsonArray.length() != 0) {
                                setTitleElse(getString(R.string.unmessaged));
                                for (int i = 0; i < jsonArray.length(); i++) {
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
                                    messageData.setYuntongid((String) SpfUtil.get(UnMessagedActivity.this, "userId", ""));
                                    messageDatas.add(messageData);
                                }
                                messageAdapter = new MessageAdapter(UnMessagedActivity.this, UnMessagedActivity.this, messageDatas);
                                unmessaged_list.setAdapter(messageAdapter);
                                messageAdapter.notifyDataSetChanged();
                                getReceived(JsonUtil.getStr(jsonObject1, "selectTime"));
                            } else {
                                unmessaged_list.removeFooterView(view);
                                setTitleElse(getString(R.string.allmessaged));
                                String a=(String) SpfUtil.get(UnMessagedActivity.this, "userId", "");
                                allmessageDatas = messageDao.getAll((String) SpfUtil.get(UnMessagedActivity.this, "userId", ""));
                                if (allmessageDatas.size() == 0) {
                                    null_message.setVisibility(View.VISIBLE);
                                    unmessaged_list.setVisibility(View.GONE);
                                } else {
                                    unmessaged_list.setVisibility(View.VISIBLE);
                                    null_message.setVisibility(View.GONE);
                                    for (int i = allmessageDatas.size() - 1; i >= 0; i--) {
                                        messageDatas.add(allmessageDatas.get(i));
                                    }
                                    messageAdapter = new MessageAdapter(UnMessagedActivity.this, UnMessagedActivity.this, messageDatas);
                                    unmessaged_list.setAdapter(messageAdapter);
                                    messageAdapter.notifyDataSetChanged();
                                }

                            }
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }


    private void getReceived(String lastSelectTime) {
        Map<String, String> map = new HashMap<>();
        map.put("lastSelectTime", lastSelectTime);
        Request<JSONObject> request = new NormalPostRequest(Constants.GETRECEIVED,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            SpfUtil.put(UnMessagedActivity.this, "selectTime", time);
                            succse = true;
                            for (int i = messageDatas.size() - 1; i >= 0; i--) {
                                messageDao.add(messageDatas.get(i));
                            }
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast(error.toString());
            }
        }, map);
        requestQueue.add(request);
    }


    @Override
    public void addFriends(String adviceId, final int i) {
        Map<String, String> map = new HashMap<>();
        map.put("applyId", adviceId);
        map.put("status", "1");
        Request<JSONObject> request = new NormalPostRequest(Constants.ADDFRIEND,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        showToast(jsonObject.toString());
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            messageDao.updata(messageDatas.get(i).getAdviceId(), messageDatas.get(i).getYuntongid(), "1",messageDatas.get(i).getType());
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, map);
        requestQueue.add(request);
    }

    @Override
    public void updata(String userid, String yuntongid, String type) {
        messageDao.updata(userid, yuntongid, "1", type);
    }

    @Override
    public void deleteMessage(final String adviceId, final int i) {
        showdeletePopupWindow(adviceId, i);
    }

    public void showdeletePopupWindow(final String adviceId, final int i) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(UnMessagedActivity.this);
        final AlertDialog dialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(UnMessagedActivity.this).inflate(R.layout.popuwindow_message, null);
        TextView invest_rightnow = (TextView) view.findViewById(R.id.delete_message);
        invest_rightnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(adviceId, i);
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void delete(final String adviceId, final int i) {
        Map<String, String> map = new HashMap<>();
        Request<JSONObject> request = new NormalPostRequest(Constants.DELETEMESSAGE_URL+adviceId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            messageDao.deleteitem(adviceId, messageDatas.get(i).getYuntongid());
                            messageDatas.remove(i);
                            messageAdapter.notifyDataSetChanged();
                            if (messageDatas.size()==0){
                                unmessaged_list.setVisibility(View.GONE);
                                null_message.setVisibility(View.VISIBLE);
                            }
                        } else {
                            showToast(JsonUtil.getStr(jsonObject, "errer"));
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
