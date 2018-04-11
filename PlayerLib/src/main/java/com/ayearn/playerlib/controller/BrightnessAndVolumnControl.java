package com.ayearn.playerlib.controller;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.helper.GestrueBaseHelper;
import com.voole.utils.log.LogUtil;

/**
 * 本类是 亮度和音量的控制器类.
 * 主要是调节亮度和音量.显示隐藏亮度和音量值
 * @author lichao
 *
 */
public class BrightnessAndVolumnControl {
	private String tag =BrightnessAndVolumnControl.class.getSimpleName();
	/**
	 * 音量工具类
	 */
	private AudioManager mAudioManager;
	private Context mContext;
	/**
	 * 设置为音量图标type值
	 */
	private static final int VOLUME_IMAGE=1;
	/**
	 * 设置为亮度图标type值
	 */
	private static final int BRIGHT_IMAGE=2;
	/**
	 * The maximum volume
	 */
	private float mMaxVolume;

	private GestrueBaseHelper mGestrueBaseHelper = null;
	public BrightnessAndVolumnControl(Context context , GestrueBaseHelper gestrueHelper){
		this.mContext = context;
		this.mGestrueBaseHelper = gestrueHelper;
		initValue();
	}
	/**
	  * 
	  * @description 对属性进行初始化
	  * @version 1.0
	  * @author lichao
	  * @data 2016-8-19上午11:38:28
	  * @update 上午11:38:28
	 */
	private void initValue() {
		mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
		mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
	/**
	  * @Title: updateVolumeSlide 
	  * @Description: 调整声音UI和音效
	  * @author ZN
	  * @param percent
	  * @param isPhyKeyboard(true:调整音量跟随监听物理键音量调整，false:反之)
	  * @throws 
	  *	@date 2016-8-19
	  *	FLAG_ALLOW_RINGER_MODES 是否包括振铃模式，尽可能的选择更改卷的时候。
	  * FLAG_PLAY_SOUND 无论是改变音量时播放声音。
	  * FLAG_REMOVE_SOUND_AND_VIBRATE 除去任何声音/振动可能在队列中
	  * FLAG_SHOW_UI 显示包含当前音量。
	  * FLAG_VIBRATE 如果进入的振动响铃模式是否振动。
	 */
	public void updateVolumeSlide(int preVolumeIndex ,boolean isHorizontal, float percent, boolean isPhyKeyboard ,float distanceX ,float distanceY){
		int mVolume;
		if(isPhyKeyboard){
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		}else{
			mVolume =preVolumeIndex;
		}
		ShowTheVolumeOrBright(VOLUME_IMAGE);
		if (mVolume < 0){
			mVolume = 0;
		}
		if(!isPhyKeyboard) {
			mVolume = (int) getCurrentVolume(percent, mVolume);
		}
		if (mGestrueBaseHelper.mTxtPercentage != null) {
			mGestrueBaseHelper.mTxtPercentage.setText((int) ((float) ((float) mVolume / (float) mMaxVolume) * 100)+ "");
		}
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) mVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	}
	/**
	  * @Title: getCurrentVolume 
	  * @Description: 获取当前音量
	  * @author ZN
	  * @param percent
	  * @return
	  * @throws 
	  *	@date 2016-9-19
	 */
	private float getCurrentVolume(float percent , int mVolume){
		float index = ((float) (percent * mMaxVolume)) + mVolume;
		if (index > mMaxVolume) {
			index = mMaxVolume;
		}else if (index < 0){
			index = 0;
		}
		return index;
	}
	/**
	 *
	 * @Title: onBrightnessSlide
	 * @Description: 调整亮度UI和效果
	 * @param percent 设定文件 (初始化的时候 此值为0,并且不需要显示亮度百分比以及对应的亮度图片(小太阳))
	 * @return void 返回类型
	 * @author 
	 * @throws
	 */
	public void updateBrightnessSlide(float preBrightness, boolean isHorizontal, float percent) {
		Activity activity =(Activity)mContext;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.System.canWrite(activity)) {
            	/*去设置权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intent);*/
                return;
            }
        }
		float mBrightness = preBrightness;
		if (mBrightness <= 0.00f){
			mBrightness = 0.50f;
		}
		if (mBrightness < 0.01f){
			mBrightness = 0.01f;
		}
		if (IsAutoBrightness(mContext)) {
			stopAutoBrightness(mContext);
		}
		ShowTheVolumeOrBright(BRIGHT_IMAGE);
		if(percent ==0.0f) {
			mGestrueBaseHelper.mImgBrightOrVolumn.setVisibility(View.GONE);
		}
		WindowManager.LayoutParams lpa = activity.getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		LogUtil.d("lichao","updateBrightnessSlide(BrightnessAndVolumnControl.java:141)--lpa.screenBrightness-->>" + lpa.screenBrightness);
		if (lpa.screenBrightness > 1.0f){
			lpa.screenBrightness = 1.0f;
		}else if (lpa.screenBrightness <= 0.01f){
			lpa.screenBrightness = 0.01f;
		}
		LogUtil.d("lichao","updateBrightnessSlide(BrightnessAndVolumnControl.java:141)--Info-->>");
		activity.getWindow().setAttributes(lpa);
		if (lpa.screenBrightness == 0.01f) {
			mGestrueBaseHelper.mTxtPercentage.setText("0");
		}else {
			mGestrueBaseHelper.mTxtPercentage.setText((int) (lpa.screenBrightness * 100) + "");
		}
		Log.d(tag, "in updateBrightnessSlide leftProgress is :" + lpa.screenBrightness);
		
	}
	public static void stopAutoBrightness(Context context) {

		Settings.System.putInt(context.getContentResolver(),

				Settings.System.SCREEN_BRIGHTNESS_MODE,

				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}
	public static boolean IsAutoBrightness(Context context) {

		boolean IsAutoBrightness = false;

		try {

			IsAutoBrightness = Settings.System.getInt(
					context.getContentResolver(),

					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;

		}

		catch (Settings.SettingNotFoundException e)

		{

			e.printStackTrace();

		}

		return IsAutoBrightness;
	}
	/**
	  * @Title: hideTheVolumeOrBright
	  * @description 隐藏音量或亮度图片和数值
	  * @version 1.0
	  * @author lichao
	  * @data 2016-8-19下午1:35:04
	  * @update 下午1:35:04
	 */
	public void hideTheVolumeOrBright(){
		mGestrueBaseHelper.mImgBrightOrVolumn.setVisibility(View.GONE);
		mGestrueBaseHelper.mTxtPercentage.setVisibility(View.GONE);
	}
	/**
	 * 
	  * @Title: ShowTheVolumeOrBright
	  * @description 显示音量或亮度图片和数值
	  * @version 1.0
	  * @author lichao
	  * @data 2016-8-19下午1:36:59
	  * @update 下午1:36:59
	 */
	public void ShowTheVolumeOrBright(int type){
		if (type == VOLUME_IMAGE) {
			mGestrueBaseHelper.mImgBrightOrVolumn.setImageResource(R.drawable.video_volumn_bg);
		}else if (type == BRIGHT_IMAGE) {
			mGestrueBaseHelper.mImgBrightOrVolumn.setImageResource(R.drawable.video_brightness_bg);
		}
		mGestrueBaseHelper.mImgBrightOrVolumn.setVisibility(View.VISIBLE);
		mGestrueBaseHelper.mTxtPercentage.setVisibility(View.VISIBLE);
	}
}
