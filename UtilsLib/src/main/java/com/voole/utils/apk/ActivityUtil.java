package com.voole.utils.apk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * 应用启动工具
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 下午 03:02
 */

public class ActivityUtil {
    /**
     * 通过包名启动activity
     * @param mContext  应用上下文
     * @param packName  启动应用的包名
     * @param className 启动activity的类名
     * @param mode      activity启动模式
     */
    public static void startActivityByPack(Context mContext, String packName,
                                           String className, int mode) {
        Intent mIntent = new Intent("android.intent.action.MAIN");
        ComponentName comp = new ComponentName(packName, className);
        mIntent.setComponent(comp);
        mIntent.setFlags(mode);
        mIntent.addCategory("android.intent.category.LAUNCHER");
        mContext.startActivity(mIntent);
    }

    /**
     * 通过包名启动activity
     * @param mContext  应用上下文
     * @param packName  启动应用的包名
     * @param className 启动activity的类名
     * @param mode      activity启动模式
     * @param extras    参数
     */
    public static void startActivityByPack(Context mContext, String packName,
                                           String className, Bundle extras, int mode) {
        Intent mIntent = new Intent("android.intent.action.MAIN");
        ComponentName comp = new ComponentName(packName, className);
        mIntent.setComponent(comp);
        if(extras!=null){
            mIntent.putExtras(extras);
        }
        mIntent.setFlags(mode);
        mIntent.addCategory("android.intent.category.LAUNCHER");
        mContext.startActivity(mIntent);
    }

    /**
     * 判断包名是否存在
     * @param mContext
     * @param packageName
     * @return
     */
    public static boolean isPackageExist(Context mContext, String packageName) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 通过ACTION启动Activity
     * @param mcontext
     */
    public static void startActivityByAction(Context mcontext, String action) {
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        try {
            mcontext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mcontext, "程序没安装", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
    /**
     * 通过ACTION启动Activity
     * 可以传入一个key 和一个url
     * @param mcontext
     */
    public static void startActivityByAction(Context mcontext, String action,Bundle extras) {
        Intent intent = new Intent(action);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtras(extras);
        try {
            mcontext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mcontext, "程序没安装", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    /**
     * 类名跳转
     * @param context
     * @param className
     */
    public static void  startActivityByClass(Context context, Class className) {
        Intent intent = new Intent(context, className);
        context.startActivity(intent);
    }
}
