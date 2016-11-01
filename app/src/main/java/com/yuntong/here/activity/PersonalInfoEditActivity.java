package com.yuntong.here.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.yuntong.here.MainActivity;
import com.yuntong.here.R;
import com.yuntong.here.app.MyApplication;
import com.yuntong.here.base.BaseActivity;
import com.yuntong.here.util.CommonUtil;
import com.yuntong.here.util.Constants;
import com.yuntong.here.util.GetImg;
import com.yuntong.here.util.JsonUtil;
import com.yuntong.here.util.NormalPostRequest;
import com.yuntong.here.util.PictureUtil;
import com.yuntong.here.util.SpfUtil;
import com.yuntong.here.util.ToastUtil;
import com.yuntong.here.view.RoundImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by me on 2016/4/25.
 * 完善个人信息
 */
public class PersonalInfoEditActivity extends BaseActivity implements TextWatcher {

    private Context context = PersonalInfoEditActivity.this;
    private RoundImageView image_user_info;
    private RoundImageView image_btn_camera;
    private EditText edt_name_info;
    private TextView sexy;
    private TextView sexy_women;
    private TextView sexy_man;
    private Button btn_pesonnalinfo;// 点击访问接口
    private String picPath = null;
    private String sexytype = "2";
    private PopupWindow popupWindow;
    private GetImg getImg;
    AlertDialog choiceDialog;
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

    /**
     * 图片服务器返回的图片地址
     */
    String path;
    private View view5;

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏f
        return R.layout.activity_personal_info_edit;
    }

    @Override
    protected void initData() {
        popupWindow = new PopupWindow();
        getImg = new GetImg(this);
    }

    @Override
    protected void initView() {
        image_user_info = (RoundImageView) findViewById(R.id.image_round_pic);
        image_btn_camera = (RoundImageView) findViewById(R.id.image_getcamera);
        edt_name_info = (EditText) findViewById(R.id.edt_name_info);
        sexy = (TextView) findViewById(R.id.sexy);
        sexy_man = (TextView) findViewById(R.id.btn_sexy_man);
        sexy_women = (TextView) findViewById(R.id.btn_sexy_women);
        btn_pesonnalinfo = (Button) findViewById(R.id.btn_personalinfo);
        view5 = getLayoutInflater().from(this).inflate(R.layout.pop_hint, null);
    }

    @Override
    protected void bindView() {
        btn_pesonnalinfo.setEnabled(false);
        setTitle(getString(R.string.persional_title_info));
        image_btn_camera.setOnClickListener(this);
        sexy_man.setOnClickListener(this);
        sexy_women.setOnClickListener(this);
        btn_pesonnalinfo.setOnClickListener(this);
        edt_name_info.addTextChangedListener(this);
    }

    @Override
    protected void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.image_getcamera:
                onChoice();
                break;
            case R.id.btn_sexy_man:
                sexy_man.setBackgroundResource(R.drawable.persional_info_sexy_man);
                sexy_women.setBackgroundResource(R.drawable.personal_info_sexyed_btn);
                sexy.setText(getString(R.string.man));
                sexytype = "1";
                break;
            case R.id.btn_sexy_women:
                sexy_man.setBackgroundResource(R.drawable.personal_info_sexyed_btn);
                sexy_women.setBackgroundResource(R.drawable.personal_info_sexy_btn);
                sexy.setText(getString(R.string.women));
                sexytype = "0";
                break;
            case R.id.btn_personalinfo:
                upPersionalInfo();
                break;
        }
    }

    /**
     * 选择拍照或上传照片
     */
    private void onChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
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
                    if (extras != null) {
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
//                        PictureUtil.galleryAddPic(this, cropFilePath);// 可不设置
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        Bitmap bm = BitmapFactory.decodeFile(cropFilePath, options);
                        image_user_info.setImageBitmap(bm);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 图片剪裁
     *
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

    /**
     * 先获取图片服务器中的地址，，，再请求接口上传资料
     */
    private void upPersionalInfo() {
        btn_pesonnalinfo.setClickable(false);
        if (file == null) {
            CommonUtil.showMyToast(view5, PersonalInfoEditActivity.this, "请选择头像！", R.drawable.icon_error_pwd);
            btn_pesonnalinfo.setClickable(true);
            return;
        }
        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("headpic", file);
        http.send(HttpRequest.HttpMethod.POST, Constants.PICTURE_URL, params, new RequestCallBack<Object>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
            }

            @Override
            public void onSuccess(ResponseInfo<Object> responseInfo) {
                btn_pesonnalinfo.setClickable(true);
                String s = responseInfo.result.toString();
                JSONObject object = JsonUtil.convertJsonObj(s);
                JSONArray array = JsonUtil.convertJsonArry(object, "data");
                try {
                    path = array.getString(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                onUp();// 上传
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                btn_pesonnalinfo.setClickable(true);
                showToast("上传失败，请重试");
            }
        });
    }

    /**
     * 上传资料
     */
    private void onUp() {
        RequestQueue requestQueue = MyApplication.getInstance().requestQueue;
        Map<String, String> map = new HashMap<>();
        map.put("nickname", edt_name_info.getText().toString());
        map.put("headPic", path);
        map.put("gender", sexytype);
        Request<JSONObject> request = new NormalPostRequest(Constants.PERSONALINFO_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        if (JsonUtil.getStr(jsonObject, "status").equals("1")) {
                            JSONObject object = JsonUtil.getJsonObj(jsonObject, "model");
                            SpfUtil.put(context, "isEdit", true);
                            SpfUtil.put(context, "isLogin", true);
                            SpfUtil.put(context, "nickname", JsonUtil.getStr(object, "nickname"));// 昵称
                            SpfUtil.put(context, "headPic", JsonUtil.getStr(object, "headPic"));// 头像地址
                            SpfUtil.put(context, "gender", JsonUtil.getStr(object, "gender"));// 性别
                            Intent i = new Intent(context, MainActivity.class);
                            startActivity(i);
                        } else {
                            ToastUtil.showToast(context, JsonUtil.getStr(jsonObject, "error"));
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (edt_name_info.getText().toString().length() > 0) {
            btn_pesonnalinfo.setEnabled(true);
        } else {
            btn_pesonnalinfo.setEnabled(false);
        }
    }
}
