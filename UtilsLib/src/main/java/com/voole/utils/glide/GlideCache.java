package com.voole.utils.glide;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.GlideModule;
import com.voole.utils.log.LogUtil;

import java.io.File;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/3 10:45
 * 邮箱：lichao@voole.com
 */

public class GlideCache implements GlideModule {
    public static String mPath ="" ;
    private static String TAG = "lichao";
    private static final  String NAME ="/VooleGlideCache";
    public  void setmPath(String path){
        mPath = path;
        LogUtil.d(TAG,"setmPath  mPath is  " + mPath);
    }
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        LogUtil.d(TAG,"applyOptions   begin");
        LogUtil.d(TAG,"applyOptions  path is  " + mPath);
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置磁盘缓存目录（和创建的缓存目录相同）
        File storageDirectory = new File(context.getApplicationContext().getFilesDir().getAbsolutePath());
        LogUtil.d(TAG,"applyOptions  first storageDirectory is " + storageDirectory);
        String downloadDirectoryPath = "";
        if (!TextUtils.isEmpty(mPath)&&!"".equals(mPath)){
            downloadDirectoryPath = mPath + NAME;
        }else {
            downloadDirectoryPath = storageDirectory + NAME;
        }
        LogUtil.d(TAG,"applyOptions   downloadDirectoryPath is " + downloadDirectoryPath);
        //设置缓存的大小为30M
        int cacheSize = 1024 * 1024 * 30;
        builder.setDiskCache(new DiskLruCacheFactory(downloadDirectoryPath, cacheSize));
        //设置BitmapPool缓存内存大小
//        builder.setBitmapPool(new LruBitmapPool(1024 * 1024 * 2));
    }
    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
