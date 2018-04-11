package com.voole.utils.screen;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

/**
 * 屏幕测量工具类
 * @author Administrator
 *
 */
public class DensityUtil {
	
	public static DensityUtil densityUtil = null;
	private Display mDisplay;
	private DensityUtil(){} 
	public static DensityUtil getInstance(){
		if(densityUtil == null){
			synchronized (DensityUtil.class) {
				if(densityUtil == null){
					densityUtil = new DensityUtil();
				}
			}
		}
		return densityUtil;
	}
	
	/**
	 * 
	  * @param context
	  * @return int
	  * @description 获得屏幕的高以Px为计量单位
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:31:38
	  * @update 上午9:31:38
	 */
	public  float getHeightInPx(Context context) {
		final float height = context.getResources().getDisplayMetrics().heightPixels;
		return height;
	}
	/**
	 * 
	  * @param context
	  * @return int
	  * @description 获得屏幕的宽以Px为计量单位
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:32:24
	  * @update 上午9:32:24
	 */
	public  float getWidthInPx(Context context) {
		final float width = context.getResources().getDisplayMetrics().widthPixels;
		return width;
	}
	/**
	 * 
	  * @param context
	  * @return int
	  * @description 获得屏幕的高度 以Dp为计量单位
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:33:02
	  * @update 上午9:33:02
	 */
	public  int getHeightInDp(Context context) {
		final float height = context.getResources().getDisplayMetrics().heightPixels;
		int heightInDp = px2dip(context, height);
		return heightInDp;
	}
	/**
	 * 
	  * @param context
	  * @return int
	  * @description 获得屏幕的宽 以Dp为计量单位
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:33:45
	  * @update 上午9:33:45
	 */
	public  int getWidthInDp(Context context) {
		final float height = context.getResources().getDisplayMetrics().heightPixels;
		int widthInDp = px2dip(context, height);
		return widthInDp;
	}
	/**
	 * 
	  * @param context
	  * @param dpValue dp值
	  * @return int
	  * @description dip转换成px
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:34:16
	  * @update 上午9:34:16
	 */
	public  int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	/**
	 * 
	  * @param context 
	  * @param pxValue px的值
	  * @return int
	  * @description px 转换成 dip
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:18:18
	  * @update 上午9:18:18
	 */
	public  int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 
	  * @param context
	  * @param pxValue  px 值
	  * @return int
	  * @description 把px 转换成 sp
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:18:52
	  * @update 上午9:18:52
	 */
	public  int px2sp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 
	  * @param context
	  * @param spValue sp 值
	  * @return int
	  * @description 把sp 转换成 px
	  * @version 1.0
	  * @author
	  * @data 2016-8-23上午9:31:08
	  * @update 上午9:31:08
	 */
	public  int sp2px(Context context, float spValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}
	/**
	 * @Description:获取屏幕密度 
	 * @Author:zhangnan@voole.com
	 * @Since:2016-9-19  
	 * @Version:1.1  
	 * @param context
	 * @return
	 */
	private Display getScreenSize(Activity context){
		if(mDisplay == null){
			mDisplay = context.getWindowManager().getDefaultDisplay();
		}
		return mDisplay;
	}
	/**
	 * @Description: 获取屏幕宽度 
	 * @Author:zhangnan@voole.com
	 * @Since:2016-9-19  
	 * @Version:1.1  
	 * @param context
	 * @return
	 */
	public int getScreenWidthSize(Activity context) {
		return getScreenSize(context).getWidth();
	}
	
	/**
	 * @Description:  获取屏幕高度
	 * @Author:zhangnan@voole.com
	 * @Since:2016-9-19  
	 * @Version:1.1  
	 * @param context
	 * @return
	 */
	public int getScreenHeightSize(Activity context) {
		return  getScreenSize(context).getHeight();
	}
	
}
