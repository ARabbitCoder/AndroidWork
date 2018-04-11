package com.ayearn.playerlib.helper;

import android.os.Handler;
import android.view.View;

import com.ayearn.playerlib.controller.MediaViewControl;
import com.voole.utils.log.LogUtil;
import com.voole.utils.time.TimeUtil;

import java.lang.ref.WeakReference;

/**
 * handler 帮助类(Handler消息处理类)
 * 主要处理播放器内部消息如:seekbar更新、上下栏隐藏与显示、视频卡顿、锁定以及断网处理
 * 1.更新seekbar和开始时间.
 * 2.显示隐藏上下栏和锁屏按钮
 * 3.检测卡顿
 * 4.当用户点击重试之后有网处理和无网处理.
 * 5.当屏幕方向发生了改变后的处理
 * 6.更新浮窗大小
 * 7.播放器异常,重新启动播放器
 *
 * @author lichao
 */
public class HandlerHelper extends Handler {


    private static final String TAG = HandlerHelper.class.getSimpleName();
    public static final int SEEK_BAR_TIMER_UPDATE =1000;
    public static final int HIDE_TOP_RIGHT_BOTTOM_LAYOUT =1001;
    private boolean isFirstStrat = true;
    private WeakReference<MediaViewControl> mediaViewControl;

    private int screenWidth;
    private int screenheight;

    public HandlerHelper(MediaViewControl mediaViewControl) {
        this.mediaViewControl = new WeakReference<MediaViewControl>(mediaViewControl);
    }

    @Override
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            // seekBar和开始时间更新标识每秒更新一次
            case SEEK_BAR_TIMER_UPDATE:
                if(mediaViewControl.get()==null){
                    return;
                }
                //当用户滑动进度条时候不更新ui
                if (mediaViewControl.get().mGestrueBaseHelper.mImgVideoForward.getVisibility()== View.GONE){
                    int current = (int) mediaViewControl.get().getPlayer().getCurrentPosition();
                    LogUtil.d(TAG,"handleMessage(HandlerHelper.java:52)--Info-->>"+current);
                    mediaViewControl.get().bottomSeekBarControl.mStartTime.setText(TimeUtil.currentPostionToPlayTime(current));
                    mediaViewControl.get().bottomSeekBarControl.mSeekBar.setProgress((int) mediaViewControl.get().getPlayer().getCurrentPosition());
                }
                sendEmptyMessageDelayed(SEEK_BAR_TIMER_UPDATE,1000);
                if (isFirstStrat){
                    isFirstStrat =false;
                    sendEmptyMessageDelayed(HIDE_TOP_RIGHT_BOTTOM_LAYOUT,5000);
                }
                break;
            case HIDE_TOP_RIGHT_BOTTOM_LAYOUT:
                if(mediaViewControl.get()==null){
                    LogUtil.d(TAG,"handleMessage(HandlerHelper.java:47)--isnull-->>");
                    return;
                }
                mediaViewControl.get().mediaPlayerLayoutShowAndHideHelper.hideTopAndBottomLayout();
            default:
                break;
        }
    }

    public void destory(){
        removeCallbacksAndMessages(null);
    }
}
