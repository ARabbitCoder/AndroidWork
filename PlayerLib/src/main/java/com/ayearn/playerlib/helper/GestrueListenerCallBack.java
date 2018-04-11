/**   
* @Title: GestrueListenerCallBack.java 
* @Package com.voole.player.listener 
* @Description: TODO(用一句话描述该文件做什么) 
* @author Sunny   
* @date 2015-1-20 下午4:39:57 
* @version V1.0   
*/
package com.ayearn.playerlib.helper;

/** 
 * 1.定义用户左右滑动方法回调
 * 2.定义用户上下滑动是在左侧还是在右侧方法回调
 * 3.定义用户按下方法回调
 * @ClassName: GestrueListenerCallBack 
 * @Description: TODO 手势监听接口
 * @author Sunny 
 * @date 2015-1-20 下午4:39:57 
 *  
 */
public interface GestrueListenerCallBack {
	/**
	 * 
	  * 
	  * @description 监听用户按下
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:57:33
	  * @update 下午1:57:33
	 */
	public void onDown();
	/**
	 * 
	  * 
	  * @description 只要按下就会调用此方法，当双击时，第一次按下时会调用此方法，而第二次不会调用 
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:57:30
	  * @update 下午1:57:30
	 */
	public void onSingleTapUp();
	/**
	 * 
	  * 
	  * @description 和onSingleTapup不一样，
	  * 			 当监听器确定没有第二次按下事件时，才调用此方法，
	  * 			 也就是onSingleTapUp不能确定是单击还是双击，而此方法响应可以确定一定是单击 
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:57:26
	  * @update 下午1:57:26
	 */
	public void onSingleTapConfirmed();
	/**
	 * 
	  * 
	  * @description 双击两下的时候调用
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:56:09
	  * @update 下午1:56:09
	 */
	public void onDoubleTapConfirmed();
	/**
	 * 
	  * @param flag 监听用户手指所处位置(上下左右)
	  * @description 获取到用户手指所在位置
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:49:59
	  * @update 下午1:49:59
	 */
	public void moveDirection(int flag);
	/**
	 * 
	  * @param position
	  * @description 显示中间进度条并设置videoView的seekTo位置
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:49:08
	  * @update 下午1:49:08
	 */
	public void onVideoSeekTo(float position);
	/**
	 * 
	  * @param percent 滑动距离
	  * @param isPhyKeyboard 是否是物理按键
	  * @description 改变声音
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:47:40
	  * @update 下午1:47:40
	 */
	public void updateVolumeSlide(float percent, boolean isPhyKeyboard, float distanceX, float distanceY);
	/**
	 * 
	  * @param percent 滑动距离
	  * @description 改变亮度
	  * @version 1.0
	  * @author
	  * @data 2016-9-9下午1:46:54
	  * @update 下午1:46:54
	 */
	public void updateBrightnessSlide(float percent);
}
