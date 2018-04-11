package com.voole.utils.prop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class PropertiesUtil {

	private static Properties prop = null;

	public PropertiesUtil(Context context, String filePath){
		InputStream in = null;
		try {
			in = context.getAssets().open(filePath);
			prop = new Properties();
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getProperty(String name, String defValue) {
		if(prop != null){
			return prop.getProperty(name, defValue);
		}
		return "";
	}
}
