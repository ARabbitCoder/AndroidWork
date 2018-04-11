package com.ayearn.playerlib.helper;

import android.os.Handler;
import android.view.View;

import com.ayearn.playerlib.controller.MediaViewControl;

/**
 * 1.显示隐藏上、左、右、中、layout布局
 * 2.显示和隐藏锁定按钮
 * 3.显示和隐藏相关控件.
 * @author lichao
 *
 */
public class MediaPlayerLayoutShowAndHideHelper {
	private static final String TAG =MediaPlayerLayoutShowAndHideHelper.class.getSimpleName();
	

	private boolean autoHide = true;//为true是需要自动隐藏，为false时不需要自动隐藏
    private View topView, bottomView  ;
	private Handler handler;
	private MediaViewControl mediaViewControl;

	public MediaPlayerLayoutShowAndHideHelper(MediaViewControl mediaViewControl) {
         this.mediaViewControl = mediaViewControl;
	}

	/**
	 * 
	  * 
	  * @description 显示上下栏,如果需要自动隐藏，则会发送一条延时自动隐藏的message
	  * @version 1.0
	  * @author lichao
	  * @data 2016-9-21下午2:08:24
	  * @update 下午2:08:24
	 */
	public void showTopAndBottomLayout(){
		if (mediaViewControl.mBottomView.getVisibility()==View.GONE){
			mediaViewControl.mBottomView.setVisibility(View.VISIBLE);
			mediaViewControl.mTopView.setVisibility(View.VISIBLE);
			mediaViewControl.mRightView.setVisibility(View.VISIBLE);
		}
	}
	public void showAndHideTopOrBottomLayout(){
		mediaViewControl.mHandlerHelper.removeMessages(HandlerHelper.HIDE_TOP_RIGHT_BOTTOM_LAYOUT);
		if (mediaViewControl.mBottomView.getVisibility() == View.GONE) {
			mediaViewControl.mBottomView.setVisibility(View.VISIBLE);
			mediaViewControl.mTopView.setVisibility(View.VISIBLE);
			mediaViewControl.mHandlerHelper.sendEmptyMessageDelayed(HandlerHelper.HIDE_TOP_RIGHT_BOTTOM_LAYOUT,5000);
		}else {
			mediaViewControl.mBottomView.setVisibility(View.GONE);
			mediaViewControl.mTopView.setVisibility(View.GONE);
		}
	}
	/**
	 * 
	  * 
	  * @description 隐藏上下栏
	  * @version 1.0
	  * @author lichao
	  * @data 2016-9-21下午2:07:57
	  * @update 下午2:07:57
	 */
	public void hideTopAndBottomLayout(){
		if (mediaViewControl.mBottomView.getVisibility() == View.VISIBLE){
			mediaViewControl.mBottomView.setVisibility(View.GONE);
			mediaViewControl.mTopView.setVisibility(View.GONE);
		}
	}

	/**
	  * @description 隐藏progressbar
	  * @version 1.0
	  * @author lichao
	  * @data 2016-9-23下午4:01:36
	  * @update 下午4:01:36
	 */
	public void hideProgressBarView(){
	}
	/**
	  * @description 显示progressbar
	  * @version 1.0
	  * @author 
	  * @data 2016-9-23下午4:02:07
	  * @update 下午4:02:07
	 */
	public void showProgressBarView(){
	}
}
