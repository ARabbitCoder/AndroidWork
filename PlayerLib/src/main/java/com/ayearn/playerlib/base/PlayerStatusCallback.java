package com.ayearn.playerlib.base;

/**
 * @author liujingwei
 * @DESC 播放器的部分状态回调只供给ui层不知道的状态
 * @time 2018-1-9 18:47
 */

public interface PlayerStatusCallback {

    /**
     * 开始准备
     */
    public void startPrepared();

    /**
     * 准备完成
     */
    public void onPrepared(long current, long total);

    /**
     * 缓冲开始
     */
    public void onBufferStart(long current, long total);

    /**
     * 缓冲结束
     */
    public void onBufferEnd(long current, long total);

    /**
     * 播放完成
     */
    public void onPlayComplete(long total);

    /**
     * -1,-1 setDataSource出错
     * -2，-2 prepareAsync出错一般是不能url播放
     * 播放器出错
     */
    public void onError(int what, int extra);

    /**
     * 开始播放
     */
    public void startPlay(long current, long total);

    /**
     * 暂停播放
     */
    public void pausePlay(long current, long total);

    /**
     * 停止播放
     */
    public void stopPlay(long current, long total);
}
