package com.yuntong.here.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.ImageLoaderUtil;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.PictureUtil;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.ToastUtil;
import com.yuntong.here.view.photoviews.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/18.
 * 显示大图头像
 */
public class HeadActivity extends BaseActivity {

    private Context context = HeadActivity.this;
    private PhotoView head;
    private RelativeLayout back, more;
    private String picurl;
    private ArrayList<String> paths = new ArrayList<>();
    private PopupWindow popupWindow;
    /**
     * 照片file对象
     */
    private File photoFile;
    private File file;
    /**
     * 照片名字
     */
    private static String photoFileName = null;
    /**
     * 剪裁返回图片
     */
    private Bitmap photo;

    private int width;

    String path;

    AlertDialog choiceDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_head;
    }

    @Override
    protected void initData() {
        picurl = getIntent().getStringExtra("headPic");
        popupWindow = new PopupWindow();
//        width = MyApplication.getInstance().getScreenWidthOrHeight(HeadActivity.this,true);
    }

    @Override
    protected void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);
        more = (RelativeLayout) findViewById(R.id.more);
        more.setOnClickListener(this);
        head = (PhotoView) findViewById(R.id.head);
    }

    @Override
    protected void bindView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageLoaderUtil.disPlayBig(picurl,head);
            }
        });
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.more:
                onChoice();
                break;
            default:
                break;
        }
    }

    /**
     * 选择拍照或上传照片
     */
    private void onChoice(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        choiceDialog = builder.create();
        builder.setTitle(null);
        View view = getLayoutInflater().inflate(R.layout.persionalinfo_puperwindow, null);
        Button get_camera = (Button) view.findViewById(R.id.get_camera);
        Button get_pic = (Button) view.findViewById(R.id.get_pic);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        DialogOnClick dialogClick = new DialogOnClick();
        get_camera.setOnClickListener(dialogClick);
        get_pic.setOnClickListener(dialogClick);
        cancel.setOnClickListener(dialogClick);
        choiceDialog.setView(view);
        choiceDialog.setCancelable(false);
        choiceDialog.show();
    }

    /**
     * Dialog控件的点击事件
     */
    class DialogOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.get_camera:// 拍照
                    String sdStatus = Environment.getExternalStorageState();
                    // 检测sd卡是否可用
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        showToast("请检查SD卡是否可用");
                    }
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoFileName = "here" + "EditPicture" + ".jpg";
                    // 判断存储照片的文件夹目录是否存在，如果不存在就创建该目录
                    File pathdir = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");
                    if (!pathdir.exists()) {
                        pathdir.mkdirs();
                    }
                    if (photoFile == null) {
                        photoFile = new File(pathdir, photoFileName);
                    }
                    // 指定调用相机拍照后的照片存储的路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(intent, Constants.SELECT_FROM_CAMERA);
                    choiceDialog.dismiss();
                    break;
                case R.id.get_pic:// 相册
                    Intent intentx = new Intent(Intent.ACTION_PICK, null);
                    intentx.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intentx, Constants.SELECT_FROM_PHOTO);
                    choiceDialog.dismiss();
                    break;
                case R.id.cancel://取消
                    choiceDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){
                // 拍照
                case Constants.SELECT_FROM_CAMERA:
                    String fileName = "/sdcard/DCIM/Camera/" + photoFileName;
                    File temp = new File(fileName);
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                // 相册
                case Constants.SELECT_FROM_PHOTO:
                    startPhotoZoom(data.getData());
                    break;
                // 剪裁返回
                case Constants.PIC_EDIT_REQUEST_DATA:
                    Bundle extras = data.getExtras();
                    if (extras != null){
                        photo = extras.getParcelable("data");
                        String sdStatus = Environment.getExternalStorageState();
                        // 检测sd卡是否可用
                        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                            showToast("请检查SD卡是否可用");
                        }
                        String name = "here" + "CropEditPicture" + ".jpg";
                        String pathString = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
                        String cropFilePath = pathString + name;// 裁剪完后保存的路径
                        try {
                            File fl = new File(pathString);
                            if (!fl.exists()) {
                                fl.mkdirs();
                            }
                            FileOutputStream cropFileOutputStream = new FileOutputStream(cropFilePath);
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, cropFileOutputStream);// 把数据写入文件
                            cropFileOutputStream.flush();
                            cropFileOutputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        file = new File(cropFilePath);
                        // 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
                        PictureUtil.galleryAddPic(this, cropFilePath);
//                        head.setImageBitmap(PictureUtil.getSmallBitmap(cropFilePath));
                        BitmapFactory.Options options = new BitmapFactory.Options();
//                        options.inSampleSize = 1;
                        Bitmap bm = BitmapFactory.decodeFile(cropFilePath, options);
                        head.setImageBitmap(bm);
                        HttpUtils http = new HttpUtils();
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("headpic",file);
                        http.send(HttpRequest.HttpMethod.POST, Constants.PICTURE_URL, params, new RequestCallBack<Object>() {
                            @Override
                            public void onStart() {
//                                showToast("评论中。。");
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isUploading) {
                                if (isUploading) {
//                                    showToast("图片上传中。。。");
                                }
                            }

                            @Override
                            public void onSuccess(ResponseInfo<Object> responseInfo) {
//                                showToast("评论成功");
                                String s = responseInfo.result.toString();
                                Log.i("----responseInfo----",s);
                                JSONObject object = JsonUtil.convertJsonObj(s);
                                JSONArray array = JsonUtil.convertJsonArry(object,"data");
//                                for (int i = 0;i < array.length(); i ++){
                                    try {
                                        path = array.getString(0);
                                        Log.i("----responseInfo----",path);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
//                                }
                                onHead();// 上传头像
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                showToast("上传失败，请重试");
                            }
                        });
                    }
                    break;
            }
        }
    }

    /**
     * 上传头像
     */
    private void onHead() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("headPic",path);
        Request<JSONObject> request = new NormalPostRequest(Constants.PERSONALINFO_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            SpfUtil.put(HeadActivity.this,"headPic",path);
                        } else {
//                            ToastUtil.showToast(context, JsonUtil.getStr(jsonObject, "error"));
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
     * 图片剪裁
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 360);
        intent.putExtra("outputY", 360);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, Constants.PIC_EDIT_REQUEST_DATA);
    }
}
