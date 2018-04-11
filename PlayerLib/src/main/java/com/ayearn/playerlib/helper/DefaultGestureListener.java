/**   
 * @Title: DefaultGestureListener.java 
 * @Package com.voole.player.listener 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author Sunny   
 * @date 2015-1-20 下午4:34:15 
 * @version V1.0   
 */
package com.ayearn.playerlib.helper;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.voole.utils.screen.DensityUtil;

/**
 * @ClassName: DefaultGestureListener
 * @Description:  手势监听接口,继承自系统的手势监听,做了进一步的封装.
 * 				1.监听用户手势在屏幕左边上下滑动还是右边上下滑动,回调出去
 * 				2.监听用户左右滑动,回调出去
 * 				3.单击事件
 * 				4.双击事件
 * @author lichao
 * @date 2016-10-28 下午4:34:15
 * 
 */
public class DefaultGestureListener extends SimpleOnGestureListener {

	private String tag = DefaultGestureListener.class.getSimpleName();
	private Context context;
	private int viewWidth;
	private int viewHeight;
	/** Video lower sensitivity */
	public static final int VIDEOSENSITIVITY = 1;
	/** Lower luminance sensitivity */
	public static final int BSENSITIVITY = 1;
	/** The volume lower sensitivity*/
	public static final float VSENSITIVITY = 1f;
	/**
	 * Gestures to the left
	 */
	public static final int LEFT = 0x10000;//
	/**
	 * Gestures to the UP
	 */
	public static final int UP = LEFT + 1;
	/**
	 * Gestures to the RIGHT
	 */
	public static final int RIGHT = LEFT + 2;
	/**
	 * Gestures to the DOWN
	 */
	public static final int DOWN = LEFT + 3;
	private GestrueListenerCallBack callback;
	/**
	 * 用户开始滑动是否是上下滑动
	 */
	public boolean userIsVerticalDirection = false;
	/**
	 * 用户开始滑动是否是左右滑动
	 */
	
	public boolean userIsHorizontalDirection = false;
	public DefaultGestureListener(Context context,GestrueListenerCallBack callback) {
		this.callback = callback;
		this.context = context;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		Log.d(tag, "onSingleTapUp");
		callback.onSingleTapUp();
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		Log.d(tag, "onSingleTapConfirmed");
		callback.onSingleTapConfirmed();
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		Log.d(tag, "onDoubleTap");
		callback.onDoubleTapConfirmed();
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		Log.d(tag, "onDoubleTapEvent");
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		Log.d(tag, "onDown");
		callback.onDown();
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		Log.d(tag, "onFling");
		userIsVerticalDirection = false;
		userIsHorizontalDirection = false;
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		Log.d(tag, "onLongPress");
		super.onLongPress(e);

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		Log.d(tag, "onScroll" + e1.getAction() + "" + e2.getAction());
		gestureSliding(e1, e2 , distanceX, distanceY);
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		Log.d(tag, "onShowPress");
		super.onShowPress(e);
	}
	
	/**
	 * 
	  * @param e1
	  * @param e2
	  * @description 判断用户进行了什么手势操作并计算成值,如:左右滑动,上下滑动(左边还是右边)
	  * @version 1.0
	  * @author
	  * @data 2016-8-24下午5:18:01
	  * @update 下午5:18:01
	 */
	public void gestureSliding(MotionEvent e1, MotionEvent e2 ,float distanceX, float distanceY) {
		float mOldX = e1.getX(), mOldY = e1.getY();
		int y = (int) e2.getY();
		int windowHeight =(int) DensityUtil.getInstance().getHeightInPx(context);
		int windowWidth = (int) DensityUtil.getInstance().getWidthInPx(context);
		// TODO:进行左右上下滑动内部默认处理
//		if (!context.isFullScreen) {
//			windowHeight = windowWidth;
//		}
		float x_abs = Math.abs(e2.getX() - e1.getX());
		float y_abs = Math.abs(e2.getY() - e1.getY());
		//如果用户是左右滑动,把上下滑动设置为不触发.
		Log.d(tag, "gestureSliding x_abs is " + x_abs + " y_abs is " +y_abs + "x_abs >= y_abs is" + (x_abs >= y_abs) );
//		Log.d(tag, "gestureSliding userIsHorizontalDirection is " + userIsHorizontalDirection);
		if (x_abs >= y_abs && !userIsHorizontalDirection) {
			// TODO:左右滑动
			Log.d(tag, "onVideoSeekTo callBack");
			this.callback.onVideoSeekTo((e2.getX() - e1.getX()) / VIDEOSENSITIVITY);
			userIsVerticalDirection = true;
		} else {
			//如果用户是上下滑动,把左右滑动设置为不触发.
			if (mOldX > (windowWidth / 2.0) && !userIsVerticalDirection) {
				Log.d(tag, "updateVolumeSlide callBack");
				this.callback.updateVolumeSlide((((mOldY - y) / windowHeight)/ BSENSITIVITY),false ,distanceX,distanceY);
				userIsHorizontalDirection = true;
			} else if (mOldX < (windowWidth / 2.0)&& !userIsVerticalDirection) {
				Log.d(tag, "updateBrightnessSlide callBack");
				this.callback.updateBrightnessSlide(((mOldY - y) / windowHeight)/ BSENSITIVITY);
				userIsHorizontalDirection = true;
			}
		}
	}
	/**
	 * 
	  * @param e1
	  * @param e2
	  * @description 计算用户手指滑动方向
	  * @version 1.0
	  * @author
	  * @data 2016-8-24下午5:24:58
	  * @update 下午5:24:58
	 */
	public void calculateDirection(MotionEvent e1, MotionEvent e2) {
		float x = e2.getX() - e1.getX();
		float y = e2.getY() - e1.getY();
		// 限制必须得划过屏幕的1/4才能算划过
		float x_limit = viewWidth / 15;
		float y_limit = viewHeight / 15;
		float x_abs = Math.abs(x);
		float y_abs = Math.abs(y);
		if (x_abs >= y_abs) {
			// TODO:手势操作左右操作
			if (x > x_limit || x < -x_limit) {
				if (x > 0) {
					this.callback.moveDirection(RIGHT);
					}
				} else if (x <= 0) {
					this.callback.moveDirection(LEFT);
				}
		} else {
			// TODO:手势上下操作
			if (y > y_limit || y < -y_limit) {
				if (y > 0) {
					this.callback.moveDirection(DOWN);
				} else if (y <= 0) {
					this.callback.moveDirection(UP);
				}
			}
		}
	}
}