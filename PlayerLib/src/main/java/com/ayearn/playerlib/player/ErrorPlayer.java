package com.ayearn.playerlib.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author liujingwei
 * @DESC 附带出错处理的播放器
 * @time 2018-4-4 13:59
 */

public abstract class ErrorPlayer extends NormalPlayer{
    public ErrorPlayer(@NonNull Context context) {
        super(context);
    }

    public ErrorPlayer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ErrorPlayer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onBufferStart(long currentposition) {
        //TODO
    }

    @Override
    protected void onBufferEnd(long currentposition) {
        //TODO
    }

    @Override
    protected void onPlayStart() {
        //TODO
    }

    @Override
    protected void doWorkOnInit() {
        //TODO
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        //showPlayError(what,extra);
        return super.onError(mp, what, extra);
    }

}
