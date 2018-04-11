package com.ayearn.playerlib.helper;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.controller.BrightnessAndVolumnControl;
import com.ayearn.playerlib.controller.MediaViewControl;
import com.ayearn.playerlib.controller.SeekBarControl;
import com.ayearn.playerlib.controller.progress.CircularProgressBar;

/**
 * 	最基本的手势帮助类
 * 1.用户在屏幕左边上下滑动会调节亮度 (BrightnessAndVolumnControl)
 * 2.用户在屏幕右边上下滑动会调节声音 (BrightnessAndVolumnControl)
 * 3.用户在中间左右滑动会调节进度条 (mSeekBarControl)
 * 4.用户点击锁屏按钮,屏幕手势关闭
 * 5.用户点击屏幕,隐藏显示上下栏和锁屏按钮,当用户不操作会自动消失.(MediaPlayerLayoutShowAndHideHelper)
 * @author lichao
 *
 */
public  class GestrueBaseHelper implements GestrueListenerCallBack{
	private String tag = GestrueBaseHelper.class.getSimpleName();
	protected Context context;
	/**
	 * 系统的手势处理类
	 */
	protected GestureDetector mGestureDetector;
	/**
	 * 音量和亮度调节控制器
	 */
	protected BrightnessAndVolumnControl brightnessAndVolumnControl;
	/** 是否横向 */
	protected boolean isHorizontal = false;
	/**
	 * 用户手指摁下获取videoView当前播放时间
	 */
	protected int userDownGetVideoViewCurrentTime = 0;
	/**
	 * 用户手指抬起videoView seekto到的时间
	 */
	protected int videoViewSeekToPosition;
	/**
	 * 用户上次滑动的距离
	 */
	protected float prePercent ;
	/**
	 * 用户是否滑动完成标志
	 */
	public  boolean userSeekToComplete=false;
	/**
	 * 用户手指Down时的亮度值
	 */
	protected float preBrightnessIndex = -1f;
	/**
	 * 用户手指Down时的音量值
	 */
	protected int preVolumeIndex = -1;
	protected boolean isUserGestrue = false;

	public TextView mTxtPercentage;
	public ImageView mImgBrightOrVolumn;
	public ImageView mImgVideoForward;
	public ImageView mImgVideoBack;
	public SeekBarControl mSeekBarControl ;
	public MediaViewControl mediaViewControl;
	public CircularProgressBar circularProgressBar;
	public ImageView mStartAndPause;

	public GestrueBaseHelper(Context context, View view, MediaViewControl mediaViewControl){
		this.context = context;
		this.mediaViewControl = mediaViewControl;
		initView(view);
		//初始化手势监听
		initGestrue(view);
	}

	private void initView(View view) {
		mTxtPercentage = (TextView) view.findViewById(R.id.percentage);
		mImgBrightOrVolumn = (ImageView) view.findViewById(R.id.operation_bg);
		mImgVideoForward = (ImageView) view.findViewById(R.id.videoForward);
		mImgVideoBack = (ImageView) view.findViewById(R.id.videoBack);
		circularProgressBar = (CircularProgressBar) view.findViewById(R.id.circleProgressBar);
		mStartAndPause = (ImageView) view.findViewById(R.id.stat_and_pause);
		mStartAndPause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(tag,"mImgBrightOrVolumn onClick");
				startAndPause();
			}
		});

	}
	/**
	 * 显示和隐藏progressBar
	 * @param isShow true为显示.false 隐藏
	 */
	public void showAndHideProgressBar(boolean isShow){
		if (mStartAndPause.getVisibility() == View.VISIBLE){
			return;
		}
		if (isShow){
			if (circularProgressBar.getVisibility() == View.GONE){
				circularProgressBar.setVisibility(View.VISIBLE);
			}
		}else {
			if (circularProgressBar.getVisibility() == View.VISIBLE){
				circularProgressBar.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 试看时间到了不能继续播放
	 */
	private boolean isPreviewFilmOutTime = false;

	public boolean getPreviewMode(){
		return isPreviewFilmOutTime;
	}
	/**
	 * 试看模式播放控制打开
	 */
	public void openPreviewMode(){
		this.isPreviewFilmOutTime = true;
	}

	/**
	 * 试看模式播放控制关闭
	 */
	public void offPreviewMode(){
		this.isPreviewFilmOutTime = false;
	}

	public  void startAndPause(){
		if(isPreviewFilmOutTime){
			return;
		}
		if (mediaViewControl.getPlayer().isPlaying()){
			mStartAndPause.setImageResource(R.drawable.play_pause);
			mediaViewControl.getPlayer().pause();
			mStartAndPause.setVisibility(View.VISIBLE);
		}else {
			mediaViewControl.getPlayer().start();
			mStartAndPause.setVisibility(View.GONE);
		}
	}

	/**
	 * 正在播放就不做处理
	 */
	public  void resumePlay(){
		if(isPreviewFilmOutTime){
			return;
		}
		if (mediaViewControl.getPlayer().isPlaying()){
			return;
		}else {
			mediaViewControl.getPlayer().start();
			mStartAndPause.setVisibility(View.GONE);
		}
	}

	/**
	 * 封装接口供上层activity使用
	 */
	public void pause(){
		if (mediaViewControl.getPlayer().isPlaying()){
			mStartAndPause.setImageResource(R.drawable.play_pause);
			mediaViewControl.getPlayer().pause();
			mStartAndPause.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 封装接口供上层activity使用不显示暂停ui
	 */
	public void pauseWithoutUI(){
		if (mediaViewControl.getPlayer().isPlaying()){
			mediaViewControl.getPlayer().pause();
		}
	}
	/**
	 * 
	  * 
	  * @description 初始化手势监听器
	  * @version 1.0
	  * @author lichao
	  * @data 2016-9-7下午4:28:31
	  * @update 下午4:28:31
	 */
	@SuppressWarnings("deprecation")
	public void initGestrue(View view){
		brightnessAndVolumnControl = new BrightnessAndVolumnControl(context,this);
		mSeekBarControl = new SeekBarControl(context, this,mediaViewControl);
		mGestureDetector = new GestureDetector(new DefaultGestureListener(context,this));
	}
	/**
	 * 
	  * @param event
	  * @description 给手势监听器设置motionEvent
	  * @version 1.0
	  * @author lichao
	  * @data 2016-9-7下午5:26:00
	  * @update 下午5:26:00
	 */
	public void setOnTouchEvent(MotionEvent event ,boolean isScreenLock){
		//如果试看时间结束不再处理手势
		if(isPreviewFilmOutTime){
			return;
		}
		//给手势设置touchEvent事件,如果是锁屏.不让用户进行手势操作
		if (!isScreenLock) {
				mGestureDetector.onTouchEvent(event);
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			onKeyUp(isScreenLock);
		}
	}
		
	/**
	 * 
	  * 
	  * @description 用户手指抬起后seekto到相应位置.中间区域隐藏.
	  * @version 1.0
	  * @author
	  * @data 2016-9-14下午4:21:41
	  * @update 下午4:21:41
	 */
	public void onKeyUp(boolean isScreenLock) {
		Log.d(tag, "onKeyUp isScreenLock is :" + isScreenLock);
		//隐藏中间亮度或音量图标
		//隐藏seekBar中间进度
		mSeekBarControl.hiddenSeekbarCenterProgressLayout();
		brightnessAndVolumnControl.hideTheVolumeOrBright();
		//用户拖动完成后手指抬起设置seekTo位置和seekBar位置
		videoViewSeekTo();
		//重置数值
		resetValue();
		Log.d(tag, "mediaPlayer is seekTo seekToPosition  " + videoViewSeekToPosition);
		// 当用户多次调用onKeyUp时.先移除以前发送的handler 重新发送一个handler
	}

	private void videoViewSeekTo() {
		//用户滑动完成,这时标志变为true
		if (userSeekToComplete) {
			//设置videoview seekTo 到的位置
			mediaViewControl.getPlayer().seek(videoViewSeekToPosition);
			//设置seekBar进度位置
			userSeekToComplete = false;
		}
	}

	/**
	 * 
	  * 
	  * @description 用户手指抬起重置音量和亮度值与用户摁下时候获取的当前播放时间
	  * @version 1.0
	  * @author lichao
	  * @data 2016-9-21上午9:43:47
	  * @update 上午9:43:47
	 */
	protected void resetValue() {
		//用户手指抬起重置音量和亮度值与用户摁下时候获取的当前播放时间
		preBrightnessIndex = -1f;
		preVolumeIndex = -1;
		userDownGetVideoViewCurrentTime = -1;
		isUserGestrue = false;
	}
	@Override
	public void onDown() {
		Log.d(tag, "in onDown ");
		Activity activity = (Activity) context;
		userDownGetVideoViewCurrentTime = (int) mediaViewControl.getPlayer().getCurrentPosition();
		preBrightnessIndex = activity.getWindow().getAttributes().screenBrightness;
		preVolumeIndex=(int)((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getStreamVolume(AudioManager.STREAM_MUSIC);
	}
	@Override
	public void onSingleTapUp() {
		Log.d(tag, "in onSingleTapUp ");
		mediaViewControl.mediaPlayerLayoutShowAndHideHelper.showAndHideTopOrBottomLayout();
	}
	@Override
	public void onSingleTapConfirmed() {
		Log.d(tag, "in onSingleTapConfirmed ");
		
	}
	@Override
	public void onDoubleTapConfirmed() {
		Log.d(tag, "in onDoubleTapConfirmed ");
       startAndPause();
	}
	@Override
	public void moveDirection(int flag) {
		Log.d(tag, "in moveDirection ");

	}
	@Override
	public void onVideoSeekTo(float position) {
		Log.d(tag, "in onVideoSeekTo ");
		if (prePercent < position) {
			userDownGetVideoViewCurrentTime += 1000;
		}else if(prePercent > position) {
			userDownGetVideoViewCurrentTime -= 1000;
		}
		videoViewSeekToPosition= mSeekBarControl.onGestureVideoSeekTo(position, false,userDownGetVideoViewCurrentTime);

		Log.d(tag, "in onVideoSeekTo percent is " + position);
		prePercent = position;
		userSeekToComplete = true;
		isUserGestrue = true;
	}
	@Override
	public void updateVolumeSlide(float percent, boolean isPhyKeyboard,float distanceX, float distanceY) {
		isUserGestrue = true;
		brightnessAndVolumnControl.updateVolumeSlide(preVolumeIndex, isHorizontal, percent, isPhyKeyboard,distanceX,distanceY);
		Log.d(tag, "in updateVolumeSlide percent is " + percent);
		
	}
	@Override
	public void updateBrightnessSlide(float percent) {//判断当前vooleMediaPlayer是否允许播放器改变亮度
		isUserGestrue = true;
		brightnessAndVolumnControl.updateBrightnessSlide(preBrightnessIndex,isHorizontal, percent);
		Log.d(tag, "in updateBrightnessSlide percent is " + percent);
		
	}
	
}
