package com.yuntong.here.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;

import com.lidroid.xutils.util.PreferencesCookieStore;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yuntong.here.R;
import com.yuntong.here.util.CommonUtil;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.jpush.android.api.JPushInterface;

/**
 * 应用入口
 * 注意：在Application类中一定不能出现"private"，否则在应用覆盖安装时程序会崩溃，报unable to instantiate application异常
 */
public class MyApplication extends Application{

    /**
     * 单例对象
     */
    public static MyApplication instance;

    /**
     * 应用实例对象
     */
    public static MyApplication myApplication;

    /**
     * 获取app对象
     */
    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 异步加载图片统一配置项
     */
    public DisplayImageOptions options;

    /**
     * 请求队列
     */
    public RequestQueue requestQueue;

    public DisplayMetrics displayMetrics;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        instance = this;

        JPushInterface.init(this);

//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new PersistentCookieStore(this);
        httpclient.setCookieStore(cookieStore);
        HttpStack httpStack = new HttpClientStack(httpclient);
        this.requestQueue = Volley.newRequestQueue(this, httpStack);

        // 初始化ImageLoader
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize((int) (Runtime.getRuntime().maxMemory() / 8))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCache(new UnlimitedDiskCache(CommonUtil.searchSd()))
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 60 * 1000, 60 * 1000))
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取异步加载图片统一配置项
     */
    public DisplayImageOptions getOptions() {
        if (null == options) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.img_loding)
                    .showImageForEmptyUri(R.drawable.img_loding)
                    .showImageOnFail(R.drawable.img_loding)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .resetViewBeforeLoading(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                    .displayer(new SimpleBitmapDisplayer())
                    .considerExifParams(true)
                    .build();
        }
        return options;
    }

    /**
     * @Description 获取屏幕宽高
     * @param isGetWidth 判断是获取宽还是高
     */
    public int getScreenWidthOrHeight(Context content, Boolean isGetWidth){
        if(displayMetrics==null){

            displayMetrics = content.getApplicationContext().getResources().getDisplayMetrics(); // 用于获取手机屏幕的宽高
        }
        return isGetWidth?displayMetrics.widthPixels:displayMetrics.heightPixels;

    }
}
