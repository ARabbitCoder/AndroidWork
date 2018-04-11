package com.voole.utils.prop;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * sp的工具类
 * 
 * @author 李叶
 * 
 */
public class SharedPreferencesUtil {

	// 将参数保存到SharedPreferences中
	public static void putString(Context context, String name, String key,
			String value) {
		SharedPreferences pref = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString(key, value);
		edit.commit();
	}
	// 将参数保存到SharedPreferences中
	public static void putBoolean(Context context, String name, String key,
			boolean value) {
		SharedPreferences pref = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	// 获取SharedPreferences中保存的参数
	public static String getString(Context context, String name, String key) {
		SharedPreferences pref = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return pref.getString(key, "");
	}
	// 获取SharedPreferences中保存的参数
	public static boolean getBoolean(Context context, String name, String key) {
		SharedPreferences pref = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		return pref.getBoolean(key, false);
	}
	
	public static void clear(Context context, String name){
		SharedPreferences pref = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.clear() ;
		edit.commit() ;
	}
}
