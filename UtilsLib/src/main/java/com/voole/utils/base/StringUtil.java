package com.voole.utils.base;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

/**
 * String 工具
 * @version V1.0
 * @author guo.rui.qing
 * @time 2013-4-17下午3:03:43
 */
public class StringUtil {

	/**
	 *  功能：检测字符串是不为空
	 * @param str String
	 * @return boolean
	 */
	public static boolean isNotNull(String str) {
		boolean result = false;
		if (str != null && !str.equals("") && !"null".equals(str)
				&& str.trim().length() > 0) {
			result = true;
		}
		return result;
	}

	/**
	 *功能：检测字符串是为空
	 * @param str String
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		if (str == null || "".equals(str) || " ".equals(str)
				|| "null".equals(str) || "NULL".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  变大写
	 * @param str
	 * @return String
	 */
	public static String strToUpper(String str) {
		String result = null;
		if (str != null && !str.equals("") && !"null".equals(str)) {
			result = str.toUpperCase(Locale.ENGLISH);
		}
		return result;
	}

	/**
	 *  变小写
	 * @param str
	 * @return String
	 */
	public static String strToLower(String str) {
		String result = null;
		if (str != null && !str.equals("") && !"null".equals(str)) {
			result = str.toLowerCase(Locale.ENGLISH);
		}
		return result;
	}

	/**
	 * 字符串转换为GB2312编码
	 * @param str
	 * @return String
	 */
	public static String getEncodeGB2312AsStr(String str) {
		String resultStr = "";
		try {
			resultStr = URLEncoder.encode(str, "gb2312");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return resultStr;
	}

	/**
	 * 字符串转换为utf-8编码
	 * @param str
	 * @return String
	 */
	public static String getEncodeUTF8AsStr(String str) {
		String resultStr = "";
		try {
			resultStr = URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return resultStr;
	}
	/**
	 * 去除换行符
	 * @param str
	 * @return String
	 */
	public static String removeNewLineSymbol(String str) {
		if (isNotNull(str)) {
			if (str.endsWith("\n")) {
				str = str.substring(0, str.length() - 1);
			}
		}
		return str;
	}

	/**
	 * 流转字符串
	 *
	 * @param is
	 * @return
	 */
	public static String streamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "/n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	/**
	 * 字符串转int
	 * @param str
	 * @return
	 */
	public static int string2Int(String str){
		int i = -1;
		if(!TextUtils.isEmpty(str)){
			try {
				i = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return i;
	}
}
