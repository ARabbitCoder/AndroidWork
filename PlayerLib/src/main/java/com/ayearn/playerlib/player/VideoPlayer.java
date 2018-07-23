package com.ayearn.playerlib.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.ayearn.playerlib.base.BasePlayItem;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-4 14:05
 */

public class VideoPlayer extends ErrorPlayer{
    public VideoPlayer(@NonNull Context context) {
        super(context);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void startPlay(BasePlayItem playItem){
        startPlay(playItem.getPlayUrl(),playItem.getContinueTime());
    }


    /**
     * you must override the method to appoint the tyoe of player,one of three types PlayerType.IJK,PlayerType.EXO,PlayerType.NATIVE
     * @return
     */
    @Override
    protected PlayerType getPlayerType() {
        return PlayerType.EXO;
    }

    /**
     * 退出播放
     */
    public void exit(){
        stop();
        releaseMediaPlayer();
    }
}
