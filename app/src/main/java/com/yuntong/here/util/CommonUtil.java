package com.yuntong.here.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.yuntong.here.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by me on 2016/4/15.
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 根据定位结果返回定位信息的字符串
     */
//    public synchronized static String getLocationStr(AMapLocation location){
//        if(null == location){
//            return null;
//        }
//        StringBuffer sb = new StringBuffer();
//        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
//        if(location.getErrorCode() == 0){
//            sb.append("定位成功" + "\n");
//            sb.append("定位类型: " + location.getLocationType() + "\n");
//            sb.append("经    度    : " + location.getLongitude() + "\n");
//            sb.append("纬    度    : " + location.getLatitude() + "\n");
//            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
//            sb.append("提供者    : " + location.getProvider() + "\n");
//
//            if (location.getProvider().equalsIgnoreCase(
//                    android.location.LocationManager.GPS_PROVIDER)) {
//                // 以下信息只有提供者是GPS时才会有
//                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
//                sb.append("角    度    : " + location.getBearing() + "\n");
//                // 获取当前提供定位服务的卫星个数
//                sb.append("星    数    : "
//                        + location.getSatellites() + "\n");
//            } else {
//                // 提供者是GPS时是没有以下信息的
//                sb.append("国    家    : " + location.getCountry() + "\n");
//                sb.append("省            : " + location.getProvince() + "\n");
//                sb.append("市            : " + location.getCity() + "\n");
//                sb.append("城市编码 : " + location.getCityCode() + "\n");
//                sb.append("区            : " + location.getDistrict() + "\n");
//                sb.append("区域 码   : " + location.getAdCode() + "\n");
//                sb.append("地    址    : " + location.getAddress() + "\n");
//                sb.append("兴趣点    : " + location.getPoiName() + "\n");
//            }
//        } else {
//            //定位失败
//            sb.append("定位失败" + "\n");
//            sb.append("错误码:" + location.getErrorCode() + "\n");
//            sb.append("错误信息:" + location.getErrorInfo() + "\n");
//            sb.append("错误描述:" + location.getLocationDetail() + "\n");
//        }
//        return sb.toString();
//    }

//    public synchronized static String getLonStr(AMapLocation location){
//        if(null == location){
//            return null;
//        }
//        String lon = null;
//        if(location.getErrorCode() == 0){
//            lon = String.valueOf(location.getLongitude());
//        }else{
//            lon = "";
//        }
//        return lon;
//    }
//
//    public synchronized static String getLatStr(AMapLocation location){
//        if(null == location){
//            return null;
//        }
//        String lat = null;
//        if(location.getErrorCode() == 0){
//            lat = String.valueOf(location.getLatitude());
//        }else{
//            lat = "";
//        }
//        return lat;
//    }

    /**
     * 获取statusbar的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 获取SD卡缓存文件
     * @return
     */
    public static File searchSd(){
        File cache=null;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file= Environment.getExternalStorageDirectory();//sd卡的根目录
            File wFile=new File(file,"com.yuntong.here");
            if(wFile.exists()){
                cache=new File(wFile,"cache");
                return cache;
            }else{
                wFile.mkdir();
                cache=new File(wFile,"cache");
            }
        }
        return cache;
    }

    /**
     * 判断是否为手机号格式
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 判断是否为验证码格式
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("(010\\d{8})|(0[2-9]\\d{9})|(13\\d{9})|(14[57]\\d{8})|(15[0-35-9]\\d{8})|(18[0-35-9]\\d{8})"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }


    /**
     * 显示PopupWindow错误提示框
     * @param view 提示框显示的布局
     * @param s  提示的文字信息
     */
    public static void showPopupWindow(View view, final PopupWindow popupWindow, String s){
        TextView textView = (TextView) view.findViewById(R.id.txt_pop_big);
        TextView textView2 = (TextView) view.findViewById(R.id.txt_pop_big_confirm);
        popupWindow.setContentView(view);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(false);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        textView.setText(s);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 显示Toast错误提示框
     * @param view 布局文件
     * @param context 上下文对象
     * @param s 要显示的内容
     * @param resId 要显示的图片的ID
     */
    public static void showMyToast(View view,Context context,String s,int resId){
        TextView text = (TextView) view.findViewById(R.id.txt_pop);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_pop);
        imageView.setImageResource(resId);
        text.setText(s);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    private static long getFileSize(File file) {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    public static File getSmallBitmap(String filePath){
        File file = new File(filePath);
        long size = getFileSize(file);
        if(size < 500000){
            return file;
        }else{
            BitmapFactory.Options options = new BitmapFactory.Options();// 用来加载Bitmap图像,定义如何将Bitmap读入内存
            options.inJustDecodeBounds = true;// 先通知BitmapFactory类只需返回该图像的范围(加载图像的尺寸，而不是图像本身),防止图片过大导致内存溢出
            BitmapFactory.decodeFile(filePath, options);// 通过decodeFile()方法可以将手机里的图片文件转换成Bitmap对象
            options.inSampleSize = calculateInSampleSize(options, 1080, 1920);// 设置图片的缩放
            options.inJustDecodeBounds = false;// 设置完后，最后对图片进行真正的解码
            /**
             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
             */
//        int angle = readPictureDegree(filePath);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);// 图片文件转换成Bitmap
            BufferedOutputStream baos = null;// 输出流
//		bitmap = rotaingImageView(angle, bitmap);
            if (bitmap != null) {
                try {
                    baos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);// 图片压缩,以JPEG格式,压缩率30(表示压缩70%),
                    baos.flush();// 刷新
                    baos.close();// 关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }
    }

    /**
     * 图片缩放(大图显示小图控件中)
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    //计算图片的缩放值(按比例缩放)(参数: Options对象， 规定的宽 480 ， 规定的高 800)
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 先获取图片本身的宽和高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;// 缩放比例初始化

        // 如果图片本身的宽高中有一项大于我们自己规定的宽高，就进行缩放
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);// 高的比例    例如: 1024 / 800
            final int widthRatio = Math.round((float) width / (float) reqWidth);// 宽的比例    例如: 720 / 480
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;// 如果高比小于宽比则按高的比例缩放，否则按宽的比例缩放
        }
        return inSampleSize;
    }

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;// 初始化旋转的角度
        try {
            ExifInterface exifInterface = new ExifInterface(path);// 这个接口提供了图片文件的旋转，GPS，时间等信息。
            // 获得图片被系统旋转的角度
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);// 旋转
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    //时间戳转年月日
    public static String getTimetoYMD(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date = sdf.format(new Date(l));
        return date;
    }
    //时间戳转时间几点几分
    public static String getTimetotime(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("hh.mm");
        String date = sdf.format(new Date(l));
        return date;
    }
    //时间戳转时间几点几分
    public static String getTimeto(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh.mm");
        String date = sdf.format(new Date(l));
        return date;
    }

    //时间戳转时间到年
    public static String getTimetoY(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String date = sdf.format(new Date(l));
        return date;
    }

    //时间戳转时间到月
    public static String getTimetoM(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String date = sdf.format(new Date(l));
        return date;
    }

    //时间戳转时间到日
    public static String getTimetoD(long l){
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String date = sdf.format(new Date(l));
        return date;
    }

    //获取系统的年份
    public static int getSysYear(){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        return year;
    }

    public static Dialog onLoadingDialog(Context context, String msg){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);// 菊花
        TextView tipTextView = (TextView) v.findViewById(R.id.txt);// 提示文字
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_anim);// 加载动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画
        tipTextView.setText(msg);// 设置加载信息
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_style);// 创建自定义样式Dialog
        loadingDialog.setCancelable(true);// 可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }

    public static Dialog onJsDialog(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.js_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);// 菊花
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_anim);// 加载动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画
        Dialog loadingDialog = new Dialog(context);// 创建自定义样式Dialog
        loadingDialog.setCancelable(true);// 可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;
    }


    /**
     * 定义接口
     */
    public interface onPhotoDialogClickListener{
        public void onPic();
    }
    /**
     * 选择拍照或上传照片
     */
    public static void showPhotoDialog(Context context, final onPhotoDialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog photoDialog = builder.create();
        builder.setTitle(null);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_imageselector, null);
        Button get_pic = (Button) view.findViewById(R.id.get_pic);
        Button cancel = (Button) view.findViewById(R.id.cancel);
        get_pic.setOnClickListener(new View.OnClickListener() {// 相册
            @Override
            public void onClick(View v) {
                listener.onPic();
                photoDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {// 取消
            @Override
            public void onClick(View v) {
                photoDialog.dismiss();
            }
        });
        photoDialog.setView(view);
        photoDialog.setCancelable(false);
        photoDialog.show();
    }
}
