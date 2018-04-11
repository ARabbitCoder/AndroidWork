package com.voole.utils.prop;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.File;


public class LocalManager {
	private static LocalManager instance = new LocalManager();
	private SharedPreferences preferencesLocalInfo = null;
	private static final String PREFERENCES_NAME_LOCAL_INFO = "localInfo";

	private LocalManager() {
	}

	public static LocalManager GetInstance() {
		return instance;
	}
	
	public void init(Context context){
		preferencesLocalInfo = context.getSharedPreferences(PREFERENCES_NAME_LOCAL_INFO, Context.MODE_PRIVATE);
	}
	
	public void saveLocal(String key, String value){
		Editor editor = preferencesLocalInfo.edit();
		editor.putString(key, value);
		editor.commit();
	}
	public void saveLocal(String key, boolean value) {
		Editor editor = preferencesLocalInfo.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public String getLocal(String key, String defaultValue){
		return preferencesLocalInfo.getString(key, defaultValue);
	}
	public Boolean getLocal(String key, Boolean value){
		return preferencesLocalInfo.getBoolean(key, value);
	}
	public void deleteFiles(Context context){
		preferencesLocalInfo.edit().clear().commit();
		File appFiles = new File(getFilePath(context));
		deleteFile(appFiles);
	}
	
	private void deleteFile(File file){
		if(file.exists()){
			if(file.isDirectory()){
				File[] children = file.listFiles();
				if(children != null){
					for(File child : children){
						deleteFile(child);
					}
				}
			}else{
				file.delete();
			}
		}
	}
	
	private String getAppPath(Context context){
		String appPath = context.getFilesDir().getParent();
		if (appPath == null || appPath.length() == 0) {
			appPath = "/data/data/" + context.getPackageName();
		}
		return appPath;
	}

	public String getFilePath(Context context){
		return getAppPath(context) + "/files";
	}
}
