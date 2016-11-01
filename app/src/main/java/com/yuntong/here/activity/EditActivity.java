package com.yuntong.here.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.yongchun.library.view.ImageSelectorActivity;
import com.yuntong.here.R;
import com.yuntong.here.adapter.GridImageAdapter;
import com.yuntong.here.app.AppManager;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.photopreview.PhotoPreviewActivity;
import com.yuntong.here.photopreview.PhotoPreviewIntent;
import com.yuntong.here.util.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/29.
 * 编辑界面
 */
public class EditActivity extends BaseActivity {

    private RelativeLayout back;
    private Button titleSend;
    private EditText edittext;
    private GridView gridview;
    private Context context = EditActivity.this;
    private ArrayList<String> images;// 赋值给适配器的集合
    private ArrayList<String> images01;// 初始的存放图片路径的集合---从SceneActivity传递过来的集合数据
    private ArrayList<String> images02;// 后面再选择的图片路径集合---图片选择界面返回的图片集合数据
    private ArrayList<String> images03;// 在SceneActivity点击取消后传递过来的集合
    private GridImageAdapter adapter;
    private Bundle extrasSceneCancel;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private AlertDialog exitDialog;

    @Override
    protected int getLayoutId() {
        AppManager.getInstance().addDestoryActivity(EditActivity.this,"EditActivity");// 将本Activity添加到需要手动finish的集合中
        return R.layout.activity_edit;
    }

    @Override
    protected void initData() {
        images = new ArrayList<>();
        images01 = new ArrayList<>();
        images02 = new ArrayList<>();
        images03 = new ArrayList<>();
        images01 = (ArrayList<String>) getIntent().getSerializableExtra("extraImages");// 获取初始从SceneActivity传递过来的数据
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        titleSend = (Button) findViewById(R.id.title_send);
        titleSend.setAlpha(0.3f);
        titleSend.setOnClickListener(this);
        gridview = (GridView) findViewById(R.id.gridview_image);
        edittext = (EditText) findViewById(R.id.edittext);
    }

    @Override
    protected void bindView() {
        adapter = new GridImageAdapter(this, images);
        gridview.setAdapter(adapter);
        onNotify();// 更新适配器
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = images.get(position);
                // 如果当前点击的是"add"图片 则跳转到图片选择界面
                if (path.contains("default") && position == images.size() -1 && images.size() -1 != 9) {
                    int size = images.size();
                    if(size == 0){
                        ImageSelectorActivity.start(EditActivity.this,9,1,true,true,false);// 只能选择9张图片
                    }else if(size > 0){
                        ImageSelectorActivity.start(EditActivity.this,9 - (images.size() - 1),1,true,true,false);// 只能选择9 - (images.size - 1)张
                    }
                }else{// 跳转到图片预览界面
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(context);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(images);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
    }

    /** 将不同的集合数据更新到适配器 */
    private void onNotify() {
        if (images01 != null) {
            for (int i = 0; i < images01.size(); i++) {
                String s = images01.get(i);
                images.add(s);
            }
            if (images.size() < 9) {
                images.add("camera_default");
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.back:
                showExitDialog();
                break;
            // TODO 发送，带图
            case R.id.title_send:
                if(images.size() == 1){
                    showToast("一无所有，不能发表");
                } else {
                    Intent intent = new Intent(context, SceneActivity.class);
                    // 使用 Intent.FLAG_ACTIVITY_REORDER_TO_FRONT 标志，将已经开启的SceneActivity加到栈顶
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("dataList",images);
                    bundle.putString("text",edittext.getText().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        exitDialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_edit, null);
        Button exit = (Button) view.findViewById(R.id.scene_exit_dialog);
        Button cancel = (Button) view.findViewById(R.id.scene_cancel_dialog);
        DialogOnClick diaClick = new DialogOnClick();
        exit.setOnClickListener(diaClick);
        cancel.setOnClickListener(diaClick);
        exitDialog.setView(view);
        exitDialog.setCancelable(false);
        exitDialog.show();
    }

    /**
     * 退出dialog的点击事件
     */
    class DialogOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.scene_exit_dialog:// 退出
                    Intent mIntent = new Intent(Constants.ISEDITACTIVITYFINISH_BROADCAST_ACTION);
                    sendBroadcast(mIntent);
                    exitDialog.dismiss();
                    EditActivity.this.finish();
                    break;
                case R.id.scene_cancel_dialog:// 取消
                    exitDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 图片选择界面返回的数据 */
        if (resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            // 获取图片选择界面返回的图片集合
            images02 = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            // 将此集合数据添加到适配器的集合中
            if (images02 != null) {
                // 先将初始集合中的"add"图片移除掉
                for (int i = 0; i < images.size(); i++) {
                    String path = images.get(i);
                    if (path.contains("camera_default")) {
                        images.remove(images.size() - 1);
                    }
                }
                // 再将新集合的数据添加到适配器的集合中
                images.addAll(images02);
                // 再判断添加完成后的集合size是否小于9   如果是则继续显示"add"图
                if (images.size() < 9) {
                    images.add("camera_default");
                }
                adapter.notifyDataSetChanged();
            }
        }
        /** 图片预览界面返回的数据 */
        if (resultCode == RESULT_OK && requestCode == REQUEST_PREVIEW_CODE){
            ArrayList<String> list = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
            if (images != null && images.size()>0){
                images.clear();
            }
            // 先移除 "camera_default" 再增加 "camera_default"
            if (list.contains("camera_default")){
                list.remove("camera_default");
            }
            if(list.size()<9){
                list.add("camera_default");
            }
            images.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            showExitDialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        extrasSceneCancel = intent.getExtras();
        if(extrasSceneCancel != null){
            images03 = extrasSceneCancel.getStringArrayList("list");// 获取从SceneActivity点击取消后传递过来的集合数据
            if (images03 != null) {
                images.clear();// 一定要清除，否则会出现重复的数据
               if(images03.size() < 9){
                   // 先将初始集合中的"add"图片移除掉 --- 一定要移除,否则会出现重复的数据
                   for (int i = 0; i < images03.size(); i++) {
                       String path = images03.get(i);
                       if (path.contains("camera_default")) {
                           images03.remove(images03.size() - 1);
                       }
                   }
               }
                // 再将新集合的数据添加到适配器的集合中
                for (int i = 0; i < images03.size(); i++) {
                    String s = images03.get(i);
                    images.add(s);
                }
                // 再判断添加完成后的集合size是否小于9   如果是则继续显示"add"图
                if (images.size() < 9) {
                    images.add("camera_default");
                }
                adapter.notifyDataSetChanged();
            }
            String text = extrasSceneCancel.getString("text");// 获取从SceneActivity点击取消后传递过来文本的数据
            edittext.setText(text);
        }
    }
}
