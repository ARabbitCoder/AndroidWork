package com.ayearn.playerlib.player;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.ayearn.playerlib.base.PlayerStatusCallback;
import com.voole.utils.log.LogUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author liujingwei
 * @DESC 简单的播放器只提供了播放功能和一些基础回调
 * @time 2018-1-8 12:08
 */

public abstract class NormalPlayer extends BasePlayer {
    private static final String TAG = "VooleNormalPlayer";
    /**
     * 是否是在surface创建成功之前调用
     */
    private boolean isSufaceCreatedBefour = false;
    /**
     * 播放开始时快进的位置
     */
    protected long firstSeekPosition = 0;

    protected String currentPlayurl ="";
    /**
     * 汇报层需要
     * @param currentposition
     */
    protected abstract void onBufferStart(long currentposition);

    /**
     * 汇报层需要
     * @param currentposition
     */
    protected abstract void onBufferEnd(long currentposition);

    /**
     * 汇报层需要
     * @param
     */
    protected abstract void onPlayStart();


    public NormalPlayer(@NonNull Context context) {
        super(context);
    }

    public NormalPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NormalPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private PlayerStatusCallback playerStatusCallback;

    public void setPlayerStatusCallback(PlayerStatusCallback playerStatusCallback) {
        this.playerStatusCallback = playerStatusCallback;
    }

    /**
     * 播放
     *
     * @param path
     */
    protected void startPlay(String path) throws IOException {
        startPlay(path, 0);
    }


    /**
     * 播放并快进
     *
     * @param path
     * @param seekPosition
     */
    protected void startPlay(String path, long seekPosition) {
        currentPlayurl = path;
        firstSeekPosition = seekPosition;
        isSufaceCreatedBefour = false;
        resetMediaPlayer();
        if(TextUtils.isEmpty(path)){
            LogUtil.d(TAG,"startPlay(VooleNormalPlayer.java:105)--Info-->>play url is empty return");
            onError(mMediaPlayer,-1,-1);
            return;
        }
        try {
            setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            onError(mMediaPlayer,-2,-2);
            LogUtil.e(TAG,"startPlay(VooleNormalPlayer.java:114)--Error-->>",e);
            return;
        }
        if (playerStatusCallback != null) {
            playerStatusCallback.startPrepared();
        }
        //如果surface已经创建直接准备
        if (surfaceIsExist) {
            LogUtil.d("MainTest", "startPlay(VooleNormalPlayer.java:122)--Info-->>surfaceIsExist");
            try {
                prepareAsync();
            }catch (IllegalStateException e){
                onError(mMediaPlayer,-3,-3);
                LogUtil.e(TAG,"startPlay(VooleNormalPlayer.java:127)--Error-->>",e);
            }
        } else {
            LogUtil.d("MainTest", "startPlay(VooleNormalPlayer.java:130)--Info-->>surfaceNotExist");
            //等创建成功在准备
            isSufaceCreatedBefour = true;
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
        //如果在surface创建之前调用准备,等创建完成再调用
        if (isSufaceCreatedBefour) {
            try {
                prepareAsync();
            }catch (IllegalStateException e){
                onError(mMediaPlayer,-3,-3);
                LogUtil.e(TAG,"surfaceCreated(VooleNormalPlayer.java:146)--Error-->>",e);
            }
        }
    }

    @Override
    public void onPrepared(IMediaPlayer mp) {
        super.onPrepared(mp);
        if (playerStatusCallback != null) {
            playerStatusCallback.onPrepared(mp.getCurrentPosition(),mp.getDuration());
        }
        start();
        if (firstSeekPosition > 0) {
            if(firstSeekPosition>=mp.getDuration()){
                firstSeekPosition = firstSeekPosition - 5000;
            }
            seek(firstSeekPosition);
        }
    }

    @Override
    public void start() {
        if (playerStatusCallback != null) {
            playerStatusCallback.startPlay(mMediaPlayer.getCurrentPosition(),mMediaPlayer.getDuration());
        }
        onPlayStart();
        super.start();
    }

    @Override
    public void pause() {
        if (playerStatusCallback != null) {
            playerStatusCallback.pausePlay(mMediaPlayer.getCurrentPosition(),mMediaPlayer.getDuration());
        }
        super.pause();
    }

    @Override
    public void stop() {
        if (playerStatusCallback != null) {
            playerStatusCallback.stopPlay(mMediaPlayer.getCurrentPosition(),mMediaPlayer.getDuration());
        }
        super.stop();
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        if (playerStatusCallback != null) {
            playerStatusCallback.onError(what, extra);
        }
        return super.onError(mp, what, extra);
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        if (playerStatusCallback != null) {
            playerStatusCallback.onPlayComplete(mMediaPlayer.getDuration());
        }
        super.onCompletion(mp);
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case 10005:
            case 10006:
            case 10007:
            case 10001:
            case 10004:
            case 701:
                if (playerStatusCallback != null) {
                    playerStatusCallback.onBufferStart(mp.getCurrentPosition(),mp.getDuration());
                }
                if(what==701){
                    onBufferStart(mp.getCurrentPosition());
                }
                break;
            case 702:
            case 3:
                if (playerStatusCallback != null) {
                    playerStatusCallback.onBufferEnd(mp.getCurrentPosition(),mp.getDuration());
                }
                if(what==702){
                    onBufferEnd(mp.getCurrentPosition());
                }
                break;
            default:
                break;
        }
        return super.onInfo(mp, what, extra);
    }
}
