package com.voole.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;


/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/3 09:58
 * 邮箱：lichao@voole.com
 */

public class ImageLoadUtil {
    public static ImageLoadUtil mImageLoadUtil = null;

    public  GlideCache mGlideCache = null;
    private ImageLoadUtil() {
        mGlideCache= new GlideCache();
    }
    /**
     * 设置图片缓存路径
     * @param path
     */
    public  void initImagePath(String path){
        mGlideCache.setmPath(path);
    }
    public static ImageLoadUtil getInstance() {
        if (mImageLoadUtil == null) {
            synchronized (ImageLoadUtil.class) {
                if (mImageLoadUtil == null) {
                    mImageLoadUtil = new ImageLoadUtil();
                }
            }
        }
        return mImageLoadUtil;
    }

    /**
     * 默认加载图片
     *
     * @param mContext
     * @param path       url
     * @param mImageView
     */
    public void loadImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().into(mImageView);
    }

    /**
     * 默认加载图片附带转换效果
     *
     * @param mContext
     * @param path       url
     * @param mImageView
     */
    public void loadImageViewWithTrans(Context mContext, String path, ImageView mImageView, Transformation transformation) {
        Glide.with(mContext).load(path).bitmapTransform(transformation).into(mImageView);
    }

    /**
     * 加载指定大小图片
     *
     * @param mContext
     * @param path
     * @param width
     * @param height
     * @param mImageView
     */
    public void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        Glide.with(mContext).load(path).centerCrop().override(width, height).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片
     *
     * @param mContext
     * @param path
     * @param mImageView
     * @param lodingImage
     * @param errorImageView
     */
    public void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).centerCrop().placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    /**
     * 设置加载中以及加载失败图片并且指定大小
     *
     * @param mContext
     * @param path
     * @param width
     * @param height
     * @param mImageView
     * @param lodingImage
     * @param errorImageView
     */
    public void loadImageViewLodingSize(Context mContext, String path, int width, int height, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext).load(path).centerCrop().override(width, height).placeholder(lodingImage).error(errorImageView).into(mImageView);
    }

    /**
     * 设置ViewGroup 背景图
     *
     * @param context
     * @param errorimg
     * @param url
     * @param bgLayout
     */
    public void showImageView(Context context, int errorimg, String url,
                              final RelativeLayout bgLayout) {
        //设置错误图片
        Glide.with(context).load(url).asBitmap().error(errorimg)
                // 缓存修改过的图片
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                // 设置占位图
                .placeholder(errorimg)
                .into(new SimpleTargetView(bgLayout));

    }

    /**
     * 设置动态GIF加载方式
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asGif().into(mImageView);
    }

    /**
     * 设置静态GIF加载方式
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewStaticGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).asBitmap().into(mImageView);
    }

    /**
     * 设置监听请求接口
     * 设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘
     *
     * @param mContext
     * @param path
     * @param mImageView
     * @param requstlistener
     */
    public void loadImageViewListener(Context mContext, String path, ImageView mImageView, RequestListener<String, GlideDrawable> requstlistener) {
        Glide.with(mContext).load(path).listener(requstlistener).into(mImageView);
    }

    /**
     * 设置缓存策略 all:缓存源资源和转换后的资源  none:不作任何磁盘缓存 source:缓存源资源 result：缓存转换后的资源
     *
     * @param mContext
     * @param path
     * @param mImageView
     */
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImageView);
    }

    /**
     * 清理磁盘缓存
     * 清理内存缓存
     *
     * @param mContext
     */
    public void GuideClearDiskCache(final Context mContext) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Glide.get(mContext).clearDiskCache();
                Glide.get(mContext).clearMemory();
            }
        }.start();
    }

    /**
     * 通过url转换成bitmap.然后给viewGroup设置背景
     */
    public class SimpleTargetView extends SimpleTarget<Bitmap> {
        public View mView;

        public SimpleTargetView(View view) {
            mView = view;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {
            BitmapDrawable bd = new BitmapDrawable(loadedImage);
            if (this.mView != null) {
                this.mView.setBackground(bd);
            }
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            if (this.mView != null) {
                this.mView.setBackgroundDrawable(errorDrawable);
            }
        }
    }
}
