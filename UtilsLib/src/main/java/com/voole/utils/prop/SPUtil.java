package com.voole.utils.prop;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences tool
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-9 下午 04:44
 */

public class SPUtil {
    /**
     * first install apk clear all
     * @param context
     * @return
     */
    public static final void clear(Context context,String name){
        SharedPreferences.Editor _edit = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        _edit.clear();
        _edit.commit();
    }
    /**
     * first install apk clear value of one key
     * @param context
     * @return
     */
    public static final void clearKey(Context context,String name,String key){
        SharedPreferences.Editor _edit = context.getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        _edit.remove(key);
        _edit.commit();
    }
    /**
     * first install apk
     * @param context
     * @return
     */
    public static final boolean isFormZip(Context context){
        boolean isFromZip = context.getSharedPreferences("splash", Context.MODE_PRIVATE).getBoolean("isFromZip", true);
        return isFromZip;
    }

    /**
     * save sp Config
     * @param context
     * @return
     */
    public static void saveConfig(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * get sp Config of key
     * @param context
     * @return
     */
    public static String getConfig(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("config",
                Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }

    // 将参数保存到SharedPreferences中
    public static void saveShortcut(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences("shortcut",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    // 将参数保存到SharedPreferences中
    public static void delShortcut(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("shortcut",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.remove(key);
        edit.commit();
    }

    public static String getShortcut(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences("shortcut",
                Context.MODE_PRIVATE);
        return pref.getString(key, null);
    }
    public static void putString(Context context, String name, String key,
                                 String value) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putBoolean(Context context, String name, String key,
                                  boolean value) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static String getString(Context context, String name, String key) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static boolean getBoolean(Context context, String name, String key,
                                     boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return pref.getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String name, String key,
                             int defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return pref.getInt(key, defaultValue);
    }


    public static void putStringMultiProcess(Context context, String name,
                                             String key, String value) {
        // aidl_binder模式
        // 将参数保存到SharedPreferences中
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void putBooleanMultiProcess(Context context, String name,
                                              String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static String getStringMultiProcess(Context context, String name,
                                               String key) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_MULTI_PROCESS);
        return pref.getString(key, "");
    }

    public static boolean getBooleanMultiProcess(Context context, String name,
                                                 String key, boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(name,
                Context.MODE_MULTI_PROCESS);
        return pref.getBoolean(key, defaultValue);
    }
}
