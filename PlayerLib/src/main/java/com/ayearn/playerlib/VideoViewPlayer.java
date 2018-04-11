package com.ayearn.playerlib;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.ayearn.playerlib.base.PlayData;
import com.ayearn.playerlib.base.PlayItem;
import com.ayearn.playerlib.base.PlayerStatusCallback;
import com.ayearn.playerlib.controller.MediaViewControl;
import com.ayearn.playerlib.controller.RightViewControl;
import com.ayearn.playerlib.player.VideoPlayer;

import java.util.List;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-4 15:38
 */

public class VideoViewPlayer extends FrameLayout {
    private Context mContext ;
    private MediaViewControl mediaViewControl;
    private VideoPlayer mVooleEpgPlayer;
    public VideoViewPlayer(@NonNull Context context) {
        super(context);
        this.mContext = context;
        addView();
    }

    public VideoViewPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        addView();
    }

    public VideoViewPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        addView();
    }

    private void addView(){
        mVooleEpgPlayer = new VideoPlayer(mContext);
        mediaViewControl = new MediaViewControl(mContext);
        mediaViewControl.setPlayer(mVooleEpgPlayer);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mediaViewControl.setLayoutParams(params);
        mVooleEpgPlayer.setLayoutParams(params);
        addView(mVooleEpgPlayer);
        addView(mediaViewControl);
        setPlayerStatusCallback();
    }

    @Override
    public void addOnLayoutChangeListener(OnLayoutChangeListener listener) {
        super.addOnLayoutChangeListener(listener);
    }

    /**
     * 播放器的部分状态回调这里不要处理ui相关的控制操作(初始化数据可以),数据操作
     */
    private void setPlayerStatusCallback(){
        PlayerStatusCallback callback = new PlayerStatusCallback() {
            @Override
            public void onPrepared(long current,long total) {
                if(mediaViewControl!=null){
                    mediaViewControl.bottomSeekBarControl.initData();
                    mediaViewControl.mGestrueBaseHelper.showAndHideProgressBar(false);
                }
            }

            @Override
            public void startPrepared() {
                mediaViewControl.mGestrueBaseHelper.showAndHideProgressBar(true);
            }

            /**
             * 缓冲开始
             */
            @Override
            public void onBufferStart(long current,long total) {
                mediaViewControl.mGestrueBaseHelper.showAndHideProgressBar(true);
            }
            /**
             * 缓冲结束
             */
            @Override
            public void onBufferEnd(long current,long total) {
                mediaViewControl.mGestrueBaseHelper.showAndHideProgressBar(false);
            }

            /**
             * 播放结束
             * 请求播放数据管理器发送下一个数据
             */
            @Override
            public void onPlayComplete(long total) {

            }

            /**
             * 播放器出错
             * @param what
             * @param extra
             */
            @Override
            public void onError(int what, int extra) {

            }

            /**
             * 暂停播放
             */
            @Override
            public void pausePlay(long current,long total){

            }

            /**
             * 开始播放
             */
            @Override
            public void startPlay(long current,long total) {

            }

            /**
             * 停止播放
             * @param current
             * @param total
             */
            @Override
            public void stopPlay(long current, long total) {

            }
        };
        if(mVooleEpgPlayer!=null){
            mVooleEpgPlayer.setPlayerStatusCallback(callback);
        }
        RightViewControl.OnItemClickListener onItemClickListener = new RightViewControl.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        };
        setRecycleOnItemClickListener(onItemClickListener);
    }
    /**
     * 节目剧集点击事件回调
     * @param onItemClickListener
     */
    public void setRecycleOnItemClickListener(RightViewControl.OnItemClickListener onItemClickListener){
        mediaViewControl.setRecycleOnItemClickListener(onItemClickListener);
    }


    public void startPlayFilm(PlayData playData){
        if(playData!=null) {
            if(playData.getList()!=null&&playData.getPlayItem()!=null) {
                mVooleEpgPlayer.startPlay(playData.getPlayItem());
                setTopViewFileName(playData.getPlayItem().getFilmName());
                setRightViewAdapter(playData.getList(),playData.getPlayItem().getPlayIndex());
            }
        }
    }

    /**
     * 设置播放器头部名称
     * @param fileName
     */
    public void setTopViewFileName(String fileName){
        if (mediaViewControl.mTopViewControl.mFilmName!=null){
            mediaViewControl.mTopViewControl.mFilmName.setText(fileName+"");
        }
    }
    /**
     * 设置右边数据源
     * @param list
     */
    public void setRightViewAdapter(List<PlayItem> list, int current){
        mediaViewControl.mRightViewControl.setAdapter(list,current);
    }
}
