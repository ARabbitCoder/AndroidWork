package com.voole.utils.apk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * get apk baseInfo
 * @author guo.rui.qing
 * @time 2017-11-9 下午 02:57
 */
public class ApkUtil {
    /**
     * get apk build time
     * @param context
     * @return build time
     */
    public static String getBuildTime(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf;
            zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("classes.dex");
            long time = ze.getTime();
            String s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get apk versionName
     * @param context
     * @return versionName
     */
    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get apk packname
     * @param context
     * @return packname
     */
    public static String getAppName(Context context) {
        return context.getPackageName();
    }

    /**
     * get apk versionCode
     * @param context
     * @return versionCode
     */
    public static int getAppVersionCode(Context context){
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
//          return -1;
        }
    }

    /**
     * install apk
     * @param context
     * @param apkFile
     */
    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
