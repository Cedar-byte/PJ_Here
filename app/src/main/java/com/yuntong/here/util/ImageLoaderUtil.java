package com.yuntong.here.util;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.yuntong.here.R;


/**
 * ImageLoader设置工具类
 */
public class ImageLoaderUtil {

    private static DisplayImageOptions options =new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.img_loding)
            .showImageOnFail(R.drawable.img_loding)
            .showImageForEmptyUri(R.drawable.img_loding)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .displayer(new SimpleBitmapDisplayer())
            .build();

    private static DisplayImageOptions optionsBig =new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.place_holder_square)
            .showImageOnFail(R.drawable.place_holder_square)
            .showImageForEmptyUri(R.drawable.place_holder_square)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .resetViewBeforeLoading(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
            .displayer(new SimpleBitmapDisplayer())
            .build();

    public static void disPlay(String url,ImageView imageView){
        ImageLoader.getInstance().displayImage(url,imageView,options);
    }
    public static void disPlayBig(String url,ImageView imageView){
        ImageLoader.getInstance().displayImage(url, imageView, optionsBig);

    }
}
