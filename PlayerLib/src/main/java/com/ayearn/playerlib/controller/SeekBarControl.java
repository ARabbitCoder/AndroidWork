package com.ayearn.playerlib.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.helper.GestrueBaseHelper;
import com.voole.utils.log.LogUtil;
import com.voole.utils.screen.DensityUtil;
import com.voole.utils.time.TimeUtil;


/**
 * seekBar 控制器.
 * 1.用户中间滑动显示进度,改变底部seekBar的进度,在中间显示播放器进度
 * 2.通过拖动底部seekBar进行中间进度显示.
 * 3.隐藏中间进度
 * 4.处理当前进度条，会出现两种模式:
 * @author lichao
 *
 */
public class SeekBarControl {
	private String TAG = SeekBarControl.class.getSimpleName();
	/**
	 * video 上次的进度时间
	 * The last time the progress of the time
	 */
	private int videoPreTime;
	public Context context;
    private GestrueBaseHelper mGestrueBaseHelper = null;
    private MediaViewControl mediaViewControl;
	public SeekBarControl(Context context, GestrueBaseHelper gestrueBaseHelper, MediaViewControl mediaViewControl){
		this.mGestrueBaseHelper = gestrueBaseHelper;
		this.mediaViewControl=mediaViewControl;
		this.context = context;
	}
	
	/**
	 * 
	  * @param distance 滑动距离
	  * @param isVertical 是否是垂直
	  * @description
	  * @version 1.0
	  * @author
	  * @data 2016-8-22下午3:10:40
	  * @update 下午3:10:40
	 */
	public int onGestureVideoSeekTo(  float distance , boolean isVertical ,int playedTime) {
		if (isVertical){
			return 0;
		}
		int screenWidth = (int) DensityUtil.getInstance().getWidthInPx(context);
		float distanceFloat= (distance / screenWidth) / 3;
		int videoDuration;
		int currentVideoPosition;
		int seekToPosition;
		//注意这里逻辑发生变化，这里既有直播又有影片模式判断
		videoDuration = (int) mediaViewControl.getPlayer().getDuration();
		currentVideoPosition = playedTime;
		seekToPosition = (int) ((videoDuration * distanceFloat) + currentVideoPosition);
		if (seekToPosition < 0) {
			seekToPosition = 0;
		}else if(seekToPosition >= videoDuration) {
			seekToPosition = videoDuration;
		}
		//这里当用户手势滑动时候,滑动到最后需要让其前进5s
		if (seekToPosition==mediaViewControl.bottomSeekBarControl.mSeekBar.getMax()){
			seekToPosition-=5*1000;
			LogUtil.d(TAG,"onGestureVideoSeekTo(SeekBarControl.java:69)--Info-->> is end");
		}
		//TODO 如果是预览，判断是否大于等于预览时间，如果是大于等于就让其为预览最大值
		seekToPosition = cutPreviewTime(seekToPosition);
		// 按照时间进行显示
		showCurrentDragPlayTime(seekToPosition, true);
		// 进行刷新前进后退指示符
		return seekToPosition;
		
	}

	/**
	 * 阻止预览试看时快进超过试看时间
	 * @param currentposition
	 * @return
	 */
	private int cutPreviewTime(int currentposition){
		int time = mediaViewControl.getPreviewTime();
		if(time!=-1){
			if(currentposition>=time){
				LogUtil.d(TAG,"cutPreviewTime(SeekBarControl.java:89)--Info-->>"+currentposition);
				return time;
			}
		}
		return currentposition;
	}
	/**
	 * 
	  * @param currentPlayPosition 用户拖动之后的进度
	  * @param isShowCenterTime 是否显示中间区域进度
	  * @description 当进度条被拖动的时候,在中间显示改变后的时间.
	  * @version 1.0
	  * @author lichao
	  * @data 2016-8-22下午3:18:02
	  * @update 下午3:18:02
	 */
	public void showCurrentDragPlayTime( int currentPlayPosition, boolean isShowCenterTime) {
		if (!isShowCenterTime) {
			return;
		}
		//TODO 如果是预览，判断是否大于等于预览时间，如果是大于等于就让其为预览最大值
		currentPlayPosition = cutPreviewTime(currentPlayPosition);
		//这里当用户手势滑动时候,滑动到最后需要让其前进5s
		if (currentPlayPosition==mediaViewControl.bottomSeekBarControl.mSeekBar.getMax()){
			LogUtil.d(TAG,"showCurrentDragPlayTime(SeekBarControl.java:89)--Info-->>progress is end so -- 10s");
			mediaViewControl.bottomSeekBarControl.mSeekBar.setProgress(mediaViewControl.bottomSeekBarControl.mSeekBar.getMax() - 5 * 1000);
			mediaViewControl.bottomSeekBarControl.mStartTime.setText(TimeUtil.currentPostionToPlayTime(mediaViewControl.bottomSeekBarControl.mSeekBar.getMax() - 100 * 1000));
		}else {
			LogUtil.d(TAG,"showCurrentDragPlayTime(SeekBarControl.java:93)--Info-->>progress is not end");
			mediaViewControl.bottomSeekBarControl.mSeekBar.setProgress(currentPlayPosition);
			mediaViewControl.bottomSeekBarControl.mStartTime.setText(TimeUtil.currentPostionToPlayTime(currentPlayPosition));
		}
		mGestrueBaseHelper.mImgVideoForward.setVisibility(View.VISIBLE);
		mGestrueBaseHelper.mImgVideoBack.setVisibility(View.VISIBLE);
		mGestrueBaseHelper.mTxtPercentage.setVisibility(View.VISIBLE);
		mGestrueBaseHelper.mImgBrightOrVolumn.setVisibility(View.GONE);
		////注意这里逻辑发生变化，这里既有直播又有影片模式判断
		showCenterTextOnDragingSeekBar( currentPlayPosition);
		
		Log.d(TAG, "in showCurrentTime is  " + currentPlayPosition);
		if (videoPreTime < currentPlayPosition) {
			// TODO:前进
			mGestrueBaseHelper.mImgVideoForward.setBackgroundResource(R.drawable.video_forward_selected);
			mGestrueBaseHelper.mImgVideoBack.setBackgroundResource(R.drawable.video_back_normal);
		} else if (videoPreTime > currentPlayPosition) {
			// 后退
			mGestrueBaseHelper.mImgVideoForward.setBackgroundResource(R.drawable.video_forward_normal);
			mGestrueBaseHelper.mImgVideoBack.setBackgroundResource(R.drawable.video_back_selected);
		} else {
			mGestrueBaseHelper.mImgVideoForward.setBackgroundResource(R.drawable.video_forward_normal);
			mGestrueBaseHelper.mImgVideoBack.setBackgroundResource(R.drawable.video_back_normal);
		}
		videoPreTime = currentPlayPosition;
	}

	/**
	 * @Description:在拖动seekbar时，在中间的style上显示拖动的进度或者时间  
	 * @Author:zhangnan@voole.com
	 * @Since:2016-11-22  
	 * @Version:1.1  
	 * @param currentPlayPosition
	 */
	public void showCenterTextOnDragingSeekBar(
			int currentPlayPosition) {
			mGestrueBaseHelper.mTxtPercentage.setText(TimeUtil.currentPostionToPlayTime(currentPlayPosition));
	}
	/**
	  * @description 当进度条拖动结束后隐藏时间点.
	  * @version 1.0
	  * @author lichao
	  * @data 2016-8-22下午3:20:32
	  * @update 下午3:20:32
	 */
	public void hiddenSeekbarCenterProgressLayout() {
		mGestrueBaseHelper.mTxtPercentage.setVisibility(View.GONE);
		mGestrueBaseHelper.mImgVideoForward.setVisibility(View.GONE);
		mGestrueBaseHelper.mImgVideoBack.setVisibility(View.GONE);
	}
}
