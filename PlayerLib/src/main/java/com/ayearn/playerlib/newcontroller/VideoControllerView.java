package com.ayearn.playerlib.newcontroller;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.base.PlayItem;
import com.ayearn.playerlib.player.VideoPlayer;
import com.voole.utils.net.NetUtil;
import com.voole.utils.screen.DensityUtil;
import com.voole.utils.time.TimeUtil;


/**
 * 视频控制器，可替换或自定义样式
 */
public class VideoControllerView extends FrameLayout {

    public static final int DEFAULT_SHOW_TIME = 5000;

    private View mControllerBack;
    private View mControllerTitle;
    private TextView mVideoTitle;
    private View mControllerBottom;
    private SeekBar mPlayerSeekBar;
    private ImageView mVideoPlayState;
    private ImageView mVideoPlayPause;
    private TextView mVideoProgress;
    private TextView mVideoDuration;
    private ImageView mVideoFullScreen;
    private ImageView mScreenLock;
    private VideoErrorView mErrorView;
    private VideoPlayer mPlayer;
    private PlayItem mplayItem;
    private boolean isScreenLock;
    private boolean mShowing;
    private boolean mAllowUnWifiPlay;

    public VideoControllerView(Context context) {
        super(context);
        init();
    }

    public VideoControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoControllerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_media_controller, this);

        initControllerPanel();
    }
    public interface ControllerCallBack{
        void onBackClick();
        void onFullScreen();

    }
    private ControllerCallBack controllerBack;
    public void setControllerCallback(ControllerCallBack controllerCallback){
        this.controllerBack=controllerCallback;
    }

    private void initControllerPanel() {
        // back
        mControllerBack = findViewById(R.id.video_back);
        mControllerBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controllerBack != null) {
                    controllerBack.onBackClick();
                }
            }
        });
        // top
        mControllerTitle = findViewById(R.id.video_controller_title);
        mVideoTitle = (TextView) mControllerTitle.findViewById(R.id.video_title);
        // bottom
        mControllerBottom = findViewById(R.id.video_controller_bottom);
        mPlayerSeekBar = (SeekBar) mControllerBottom.findViewById(R.id.player_seek_bar);
        mVideoPlayState = (ImageView) mControllerBottom.findViewById(R.id.player_pause);
        mVideoProgress = (TextView) mControllerBottom.findViewById(R.id.player_progress);
        mVideoDuration = (TextView) mControllerBottom.findViewById(R.id.player_duration);
        mVideoFullScreen = (ImageView) mControllerBottom.findViewById(R.id.video_full_screen);
        mVideoPlayState.setOnClickListener(mOnPlayerPauseClick);
        mVideoPlayState.setImageResource(R.drawable.ic_video_pause);
        mPlayerSeekBar.setOnSeekBarChangeListener(mSeekListener);
        mVideoFullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controllerBack != null) {
                    controllerBack.onFullScreen();
                }
            }
        });

        // lock
        mScreenLock = (ImageView) findViewById(R.id.player_lock_screen);
        mScreenLock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isScreenLock) {
                    unlock();
                }
                else {
                    lock();
                }
                show();
            }
        });
        //center_pause
        mVideoPlayPause = findViewById(R.id.player_pause_status);
        mVideoPlayPause.setOnClickListener(mOnPlayerPauseClick);
        // error
        mErrorView = (VideoErrorView) findViewById(R.id.video_controller_error);
        mErrorView.setOnVideoControlListener(new VideoErrorView.OnErrorCallback() {
            @Override
            public void onRetry(int status) {
                retry(status);
            }
        });
        mPlayerSeekBar.setMax(1000);
    }

    public void setMediaPlayer(VideoPlayer player) {
        mPlayer = player;
        //updatePausePlay();
    }

    public void setVideoInfo(PlayItem playItem) {
        this.mplayItem = playItem;
        mVideoTitle.setText(playItem.getFilmName());
    }

    public void toggleDisplay() {
        if (mShowing) {
            hide();
        } else {
            show();
        }
    }

    public void show() {
        show(DEFAULT_SHOW_TIME);
    }

    public void show(int timeout) {
        setProgress();

        if (!isScreenLock) {
            mControllerBack.setVisibility(VISIBLE);
            mControllerTitle.setVisibility(VISIBLE);
            mControllerBottom.setVisibility(VISIBLE);
        } else {
            if (!DensityUtil.isPortrait(getContext())) {
                mControllerBack.setVisibility(GONE);
            }
            mControllerTitle.setVisibility(GONE);
            mControllerBottom.setVisibility(GONE);
        }

        if (!DensityUtil.isPortrait(getContext())) {
            mScreenLock.setVisibility(VISIBLE);
        }

        mShowing = true;

        updatePausePlay();

        post(mShowProgress);

        if (timeout > 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    private void hide() {
        if (!mShowing) {
            return;
        }

        if (!DensityUtil.isPortrait(getContext())) {
            // 横屏才消失
            mControllerBack.setVisibility(GONE);
        }
        mControllerTitle.setVisibility(GONE);
        mControllerBottom.setVisibility(GONE);
        mScreenLock.setVisibility(GONE);

        removeCallbacks(mShowProgress);

        mShowing = false;
    }

    private final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private boolean mDragging;
    private long mDraggingProgress;
    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (!mDragging && mShowing && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();
        if (mPlayerSeekBar != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mPlayerSeekBar.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercent();
            mPlayerSeekBar.setSecondaryProgress(percent * 10);
        }

        mVideoProgress.setText(TimeUtil.currentPostionToPlayTime((int)position));
        mVideoDuration.setText(TimeUtil.currentPostionToPlayTime((int)duration));

        return (int)position;
    }

    /**
     * 判断显示错误类型
     */
    public void checkShowError(boolean isNetChanged) {
        boolean isConnect = NetUtil.getInstance().isNetWorkConnected(getContext());
        boolean isMobileNet = NetUtil.getInstance().isMobileConnected(getContext());
        boolean isWifiNet = NetUtil.getInstance().isWifiConnected(getContext());

        if (isConnect) {
            // 如果已经联网
            if (mErrorView.getCurStatus() == VideoErrorView.STATUS_NO_NETWORK_ERROR && !(isMobileNet && !isWifiNet)) {
                // 如果之前是无网络 TODO 应该提示“网络已经重新连上，请重试”，这里暂不处理
            } else if (mplayItem == null) {
                // 优先判断是否有video数据
                showError(VideoErrorView.STATUS_VIDEO_DETAIL_ERROR);
            } else if (isMobileNet && !isWifiNet && !mAllowUnWifiPlay) {
                // 如果是手机流量，且未同意过播放，且非本地视频，则提示错误
                mErrorView.showError(VideoErrorView.STATUS_UN_WIFI_ERROR);
                mPlayer.pause();
            } else if (isWifiNet && isNetChanged && mErrorView.getCurStatus() == VideoErrorView.STATUS_UN_WIFI_ERROR) {
                // 如果是wifi流量，且之前是非wifi错误，则恢复播放
                playFromUnWifiError();
            } else if (!isNetChanged) {
                showError(VideoErrorView.STATUS_VIDEO_SRC_ERROR);
            }
        } else {
            mPlayer.pause();
            showError(VideoErrorView.STATUS_NO_NETWORK_ERROR);
        }
    }

    public void hideErrorView() {
        mErrorView.hideError();
    }

    private void reload() {
        mPlayer.retryPlay();
    }

    public void release() {
        removeCallbacks(mShowProgress);
        removeCallbacks(mFadeOut);
    }

    private void retry(int status) {
        Log.i("DDD", "retry " + status);

        switch (status) {
            case VideoErrorView.STATUS_VIDEO_DETAIL_ERROR:
                // 传递给activity
                /*if (onVideoControlListener != null) {
                    onVideoControlListener.onRetry(status);
                }*/
                break;
            case VideoErrorView.STATUS_VIDEO_SRC_ERROR:
                reload();
                break;
            case VideoErrorView.STATUS_UN_WIFI_ERROR:
                allowUnWifiPlay();
                break;
            case VideoErrorView.STATUS_NO_NETWORK_ERROR:
                // 无网络时
                if (NetUtil.getInstance().isNetWorkConnected(getContext())) {
                    if (mplayItem == null) {
                        // 如果video为空，重新请求详情
                        retry(VideoErrorView.STATUS_VIDEO_DETAIL_ERROR);
                    }  else {
                        // 视频未准备好，重新加载
                        reload();
                    }
                } else {
                    Toast.makeText(getContext(), "网络未连接", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;

            removeCallbacks(mShowProgress);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = mPlayer.getDuration();
            mDraggingProgress = (duration * progress) / 1000L;

            if (mVideoProgress != null) {
                mVideoProgress.setText(TimeUtil.currentPostionToPlayTime((int) mDraggingProgress));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mPlayer.seek((int) mDraggingProgress);
            play();
            mDragging = false;
            mDraggingProgress = 0;

            post(mShowProgress);
        }
    };

    private void showError(int status) {
        mErrorView.showError(status);
        hide();

        // 如果提示了错误，则看需要解锁
        if (isScreenLock) {
            unlock();
        }
    }

    public boolean isLock() {
        return isScreenLock;
    }

    private void lock() {
        Log.i("DDD", "lock");
        isScreenLock = true;
        mScreenLock.setImageResource(R.drawable.video_locked);
    }

    private void unlock() {
        Log.i("DDD", "unlock");
        isScreenLock = false;
        mScreenLock.setImageResource(R.drawable.video_unlock);
    }

    private void allowUnWifiPlay() {
        Log.i("DDD", "allowUnWifiPlay");

        mAllowUnWifiPlay = true;

        playFromUnWifiError();
    }

    private void playFromUnWifiError() {
        Log.i("DDD", "playFromUnWifiError");
        // TODO: 2017/6/19 check me
        /*if (mPlayer.isInPlaybackState()) {
            mPlayer.start();
        } else {
            mPlayer.restart();
        }*/
    }

    private OnClickListener mOnPlayerPauseClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
        }
    };

    public void updatePausePlay() {
        if (mPlayer.isPlaying()) {
            mVideoPlayPause.setVisibility(View.GONE);
            mVideoPlayState.setImageResource(R.drawable.ic_video_pause);
        } else {
            mVideoPlayPause.setVisibility(View.VISIBLE);
            mVideoPlayState.setImageResource(R.drawable.ic_video_play);
        }
    }


    public void doPauseResume() {
        if (mPlayer.isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    private void pause() {
        mPlayer.pause();
        updatePausePlay();
        removeCallbacks(mFadeOut);
    }

    private void play() {
        mPlayer.start();
        show();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggleVideoLayoutParams();
    }

    void toggleVideoLayoutParams() {
        final boolean isPortrait = DensityUtil.isPortrait(getContext());

        if (isPortrait) {
            mControllerBack.setVisibility(VISIBLE);
            mVideoFullScreen.setVisibility(View.VISIBLE);
            mScreenLock.setVisibility(GONE);
        } else {
            mVideoFullScreen.setVisibility(View.GONE);
            if (mShowing) {
                mScreenLock.setVisibility(VISIBLE);
            }
        }
    }

}
