package com.ayearn.playerlib;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.ayearn.playerlib.base.PlayItem;
import com.ayearn.playerlib.base.PlayerStatusCallback;
import com.ayearn.playerlib.newcontroller.VideoBehaviorView;
import com.ayearn.playerlib.newcontroller.VideoControllerView;
import com.ayearn.playerlib.newcontroller.VideoProgressOverlay;
import com.ayearn.playerlib.newcontroller.VideoSystemOverlay;
import com.ayearn.playerlib.player.VideoPlayer;
import com.voole.utils.net.NetUtil;
import com.voole.utils.screen.DensityUtil;

/**
 * 视频播放器View
 */
public class StanardPlayerView extends VideoBehaviorView {

    private View mLoading;
    private VideoControllerView mediaController;
    private VideoSystemOverlay videoSystemOverlay;
    private VideoProgressOverlay videoProgressOverlay;
    private VideoPlayer mMediaPlayer;
    private Activity parent;
    private int initWidth;
    private int initHeight;

    public boolean isLock() {
        return mediaController.isLock();
    }

    public StanardPlayerView(Context context) {
        super(context);
        init();
    }

    public StanardPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StanardPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(width>=0&&initWidth==0){
            initWidth=width;
            initHeight=height;
        }
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.video_view, this);
        mLoading = findViewById(R.id.video_loading);
        mMediaPlayer = findViewById(R.id.video_player);
        mediaController = (VideoControllerView) findViewById(R.id.video_controller);
        videoSystemOverlay = (VideoSystemOverlay) findViewById(R.id.video_system_overlay);
        videoProgressOverlay = (VideoProgressOverlay) findViewById(R.id.video_progress_overlay);
        initPlayer();
        registerNetChangedReceiver();
        if(getContext() instanceof Activity){
            parent = (Activity) getContext();
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void initPlayer() {
        PlayerStatusCallback playerStatusCallback = new PlayerStatusCallback() {
            @Override
            public void startPrepared() {
                showLoading();
            }

            @Override
            public void onPrepared(long current, long total) {
                mediaController.show();
                hideLoading();
                mediaController.hideErrorView();
            }

            @Override
            public void onBufferStart(long current, long total) {
                showLoading();
            }

            @Override
            public void onBufferEnd(long current, long total) {
                hideLoading();
            }

            @Override
            public void onPlayComplete(long total) {
                mediaController.updatePausePlay();
            }

            @Override
            public void onError(int what, int extra) {
                hideLoading();
                if(mediaController!=null){
                    mediaController.checkShowError(false);
                }
            }

            @Override
            public void startPlay(long current, long total) {

            }

            @Override
            public void pausePlay(long current, long total) {

            }

            @Override
            public void stopPlay(long current, long total) {

            }
        };
        mMediaPlayer.setPlayerStatusCallback(playerStatusCallback);
        mediaController.setMediaPlayer(mMediaPlayer);
        mediaController.setControllerCallback(new VideoControllerView.ControllerCallBack() {
            @Override
            public void onBackClick() {
                onBackPress();
            }

            @Override
            public void onFullScreen() {
                DensityUtil.toggleScreenOrientation(parent);
            }
        });
    }
    private void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }


    private boolean isBackgroundPause;

    public void onStop() {
        if (mMediaPlayer.isPlaying()) {
            // 如果已经开始且在播放，则暂停同时记录状态
            isBackgroundPause = true;
            mMediaPlayer.pause();
        }
    }

    public void onStart() {
        if (isBackgroundPause) {
            // 如果切换到后台暂停，后又切回来，则继续播放
            isBackgroundPause = false;
            mMediaPlayer.start();
        }
    }

    public void onDestroy() {
        mMediaPlayer.exit();
        mediaController.release();
        unRegisterNetChangedReceiver();
    }

    /**
     * 开始播放
     */
    public void startPlay(PlayItem playItem) {
        if (playItem == null) {
            return;
        }
        mediaController.setVideoInfo(playItem);
        mMediaPlayer.startPlay(playItem);
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mediaController.toggleDisplay();
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        mediaController.doPauseResume();
        return super.onDoubleTap(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (isLock()) {
            return false;
        }
        return super.onDown(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (isLock()) {
            return false;
        }
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    protected void endGesture(int behaviorType) {
        switch (behaviorType) {
            case VideoBehaviorView.FINGER_BEHAVIOR_BRIGHTNESS:
            case VideoBehaviorView.FINGER_BEHAVIOR_VOLUME:
                Log.i("DDD", "endGesture: left right");
                videoSystemOverlay.hide();
                break;
            case VideoBehaviorView.FINGER_BEHAVIOR_PROGRESS:
                Log.i("DDD", "endGesture: bottom");
                mMediaPlayer.seek(videoProgressOverlay.getTargetProgress());
                videoProgressOverlay.hide();
                break;
        }
    }

    @Override
    protected void updateSeekUI(int delProgress) {
        videoProgressOverlay.show(delProgress,(int) mMediaPlayer.getCurrentPosition(),(int) mMediaPlayer.getDuration());
    }

    @Override
    protected void updateVolumeUI(int max, int progress) {
        videoSystemOverlay.show(VideoSystemOverlay.SystemType.VOLUME, max, progress);
    }

    @Override
    protected void updateLightUI(int max, int progress) {
        videoSystemOverlay.show(VideoSystemOverlay.SystemType.BRIGHTNESS, max, progress);
    }

    public void setControllerListener(VideoControllerView.ControllerCallBack controlListener) {
        mediaController.setControllerCallback(controlListener);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getLayoutParams().width = initWidth;
            getLayoutParams().height = initHeight;
        } else {
            getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
            getLayoutParams().height = FrameLayout.LayoutParams.MATCH_PARENT;
        }
    }

    public void onBackPress(){
        if(parent!=null){
            if (!DensityUtil.isPortrait(parent)) {
                if(!mediaController.isLock()) {
                    DensityUtil.toggleScreenOrientation(parent);
                }
            }else{
                parent.finish();
            }
        }
    }

    private NetChangedReceiver netChangedReceiver;

    public void registerNetChangedReceiver() {
        if (netChangedReceiver == null) {
            netChangedReceiver = new NetChangedReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            activity.registerReceiver(netChangedReceiver, filter);
        }
    }

    public void unRegisterNetChangedReceiver() {
        if (netChangedReceiver != null) {
            activity.unregisterReceiver(netChangedReceiver);
        }
    }

    private class NetChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Parcelable extra = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (extra != null && extra instanceof NetworkInfo) {
                NetworkInfo netInfo = (NetworkInfo) extra;

                if (NetUtil.getInstance().isNetWorkConnected(context) && netInfo.getState() != NetworkInfo.State.CONNECTED) {
                    // 网络连接的情况下只处理连接完成状态
                    return;
                }

                mediaController.checkShowError(true);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(parent!=null) {
                if (!DensityUtil.isPortrait(parent)) {
                    if (!mediaController.isLock()) {
                        DensityUtil.toggleScreenOrientation(parent);
                        return true;
                    }
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
