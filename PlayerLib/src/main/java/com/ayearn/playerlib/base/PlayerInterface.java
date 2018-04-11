package com.ayearn.playerlib.base;

import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * @author liujingwei
 * @DESC 播放器抽象接口
 * @time 2018-1-8 10:42
 */

public interface PlayerInterface {
    /**
     * 设置播放路径
     * @param path
     */
    public void setDataSource(String path) throws IOException;

    /**
     * 停止播放
     */
    public void stop();

    /**
     * 暂停播放
     */
    public void pause();

    /**
     * 开始播放
     */
    public void start();

    /**
     * 重新设置MediaPlayer
     */
    public void resetMediaPlayer();

    /**
     * 释放MediaPlayer资源
     */
    public void releaseMediaPlayer();

    /**
     * 准备播放 异步
     */
    public void prepareAsync();

    /**
     * 设置播放时是否屏幕常量
     */
    public void setScreenOnWhilePlaying(boolean screenOn);

    /**
     * 是否正在播放
     * @return
     */
    public boolean isPlaying();

    /**
     * 快进/快退
     * @param position
     */
    public void seek(long position);

    /**
     * 获取总时长
     * @return
     */
    public long getDuration();

    /**
     * 获取当前播放位置
     * @return
     */
    public long getCurrentPosition();

    /**
     * 设置播放显示
     * @param surfaceHolder
     */
    public void setDisplay(SurfaceHolder surfaceHolder);

    /**
     * 设置循环播放
      * @param looping
     */
    public void setLooping(boolean looping);
}
