package com.voole.utils.url;

import android.net.Uri;

/**
 * @author fengyanjie
 * @DESC Url工具类
 * @time 2017-11-9 17:00
 */
public class UrlUtil {
	/**
	 * 获取Url 链接中参数
	 * http://Tets.php?spid=20151019&epgid=900044
	 * @param url
	 * @param param spid
	 * @return
	 */
	public static String getValueFromUrl(String url, String param) {
		Uri uri = Uri.parse(url);
		return uri.getQueryParameter(param);
	}
}
