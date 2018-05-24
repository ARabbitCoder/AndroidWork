package com.ayearn.playerlib.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ayearn.playerlib.base.PlayerInterface;
import com.voole.utils.log.LogUtil;

import java.io.IOException;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author liujingwei
 * @DESC 最基础的player 只提供方法和对象不提供逻辑
 * @time 2018-1-8 10:42
 */

public abstract class BasePlayer extends FrameLayout implements PlayerInterface,SurfaceHolder.Callback,IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener{
    private static final String TAG = "VooleBasePlayer";
    private int playerHeight = 0;
    private int playerWidth = 0;
    protected SurfaceView surfaceView;
    protected IMediaPlayer mMediaPlayer;
    protected Context mContext;
    protected boolean surfaceIsExist = false;
    protected long curentPlayPosition = 0;
    private PlayerType currentPlayerType = PlayerType.IJK;
    public enum PlayerType{
        IJK,EXO,NATIVE
    };
    /**
     * 方便子类在初始化时做些其他工作
     */
    protected abstract void doWorkOnInit();
    /**
     * 加载so库是否成功
     */
    private static boolean loadlibSuccess = true;
    /**
     * 使用播放器类型 IJK EXO 2种
     * @return
     */
    protected abstract PlayerType getPlayerType();
    public BasePlayer(Context context) {
        super(context);
        initViewAndPlayer(context);
    }

    public BasePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewAndPlayer(context);
    }

    public BasePlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewAndPlayer(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BasePlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViewAndPlayer(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        playerWidth = MeasureSpec.getSize(widthMeasureSpec);
        playerHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    private void initViewAndPlayer(Context context){
        this.mContext = context;
        PlayerType type = getPlayerType();
        if(PlayerType.IJK==type){
            //加载so文件
            try {
                IjkMediaPlayer.loadLibrariesOnce(null);
                IjkMediaPlayer.native_profileBegin("libijkplayer.so");
            } catch (Exception e) {
                loadlibSuccess = false;
            }
        }
        if(loadlibSuccess){
            LogUtil.d(TAG,"initViewAndPlayer(VooleBasePlayer.java:86)--Info-->>initViewAndPlayer");
            addSurfaceView();
            createMediaPlayer();
        }else {
            TextView error = new TextView(context);
            error.setText("error! load ijklib failed,you should check the lib isexist,or use exoplayer");
            LayoutParams errorLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            errorLayoutParams.gravity = Gravity.CENTER;
            this.addView(error);
        }
        doWorkOnInit();
    }

    private void addSurfaceView(){
        if(mContext==null){
            return;
        }
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(this);
        LayoutParams suLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        surfaceView.setLayoutParams(suLayoutParams);
        this.addView(surfaceView);
    }
    /**
     * 创建MediaPlayer
     */
    private void createMediaPlayer(){
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        PlayerType type = getPlayerType();
        if(PlayerType.IJK==type){
            mMediaPlayer = new IjkMediaPlayer();
            currentPlayerType = PlayerType.IJK;
            /*//开启硬解码
            mMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);
            videotoolbox
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);*/
        }else if(PlayerType.EXO==type){
            mMediaPlayer = new IjkExoMediaPlayer(mContext);
            currentPlayerType = PlayerType.EXO;
        }else {
            mMediaPlayer = new AndroidMediaPlayer();
            currentPlayerType = PlayerType.NATIVE;
        }
        LogUtil.d(TAG,"MediaPlayer--Type--Info-->>"+String.valueOf(type));
        mMediaPlayer.setScreenOnWhilePlaying(true);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnSeekCompleteListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    /**======================接口实现方法========================================**/

    @Override
    public void setDataSource(String path) throws IOException{
        if(TextUtils.isEmpty(path)){
            return;
        }
        if(mMediaPlayer!=null) {
            mMediaPlayer.setDataSource(path);
        }
    }

    @Override
    public void stop() {
        if(mMediaPlayer!=null) {
            try {
                mMediaPlayer.stop();
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pause() {
        if(mMediaPlayer!=null) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void start() {
        if(mMediaPlayer!=null) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void resetMediaPlayer() {
        if(mMediaPlayer!=null) {
            mMediaPlayer.reset();
        }
    }

    @Override
    public void releaseMediaPlayer() {
        if(mMediaPlayer!=null) {
            mMediaPlayer.release();
        }
    }

    @Override
    public void prepareAsync() {
        LogUtil.d("MainTest","prepareAsync(VooleBasePlayer.java:180)--Info-->>prepareAsync");
        if(mMediaPlayer!=null) {
            mMediaPlayer.prepareAsync();
        }
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {
        if(mMediaPlayer!=null) {
            mMediaPlayer.setScreenOnWhilePlaying(screenOn);
        }
    }

    @Override
    public boolean isPlaying() {
        if(mMediaPlayer!=null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void seek(long position) {
        if(mMediaPlayer!=null) {
            mMediaPlayer.seekTo(position);
        }
    }

    @Override
    public long getDuration() {
        if(mMediaPlayer!=null){
            return mMediaPlayer.getDuration();
        }
        return -1;
    }

    @Override
    public long getCurrentPosition() {
        if(mMediaPlayer!=null){
            //LogUtil.d(TAG,"getCurrentPosition(VooleBasePlayer.java:238)--Info-->>"+mMediaPlayer.getCurrentPosition());
            curentPlayPosition = mMediaPlayer.getCurrentPosition();
            return curentPlayPosition;
        }
        return -1;
    }

    @Override
    public void setDisplay(SurfaceHolder surfaceHolder) {
        if(mMediaPlayer!=null){
            mMediaPlayer.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void setLooping(boolean looping) {
        if(mMediaPlayer!=null){
            mMediaPlayer.setLooping(looping);
        }
    }

    /**===========================Surface Holder===================================================*/
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        LogUtil.d(TAG,"surfaceCreated(VooleBasePlayer.java:241)--Info-->>surfaceCreated");
        surfaceIsExist = true;
        if(mMediaPlayer!=null){
            mMediaPlayer.setDisplay(surfaceHolder);
            mMediaPlayer.setScreenOnWhilePlaying(true);
        }
        mSurfaceHolder = surfaceHolder;
    }
    protected SurfaceHolder mSurfaceHolder;
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        LogUtil.d(TAG,"surfaceChanged(VooleBasePlayer.java:251)--Info-->>surfaceChanged--format:"+format+"--width:"+width+"--height:"+height);
        mSurfaceHolder = surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        LogUtil.d(TAG,"surfaceDestroyed(VooleBasePlayer.java:257)--Info-->>surfaceDestroyed");
        surfaceIsExist = false;
        if(mMediaPlayer!=null){
            try {
                mMediaPlayer.setDisplay(null);
            }catch (Exception e){
                LogUtil.e(TAG,e);
            }
        }
        mSurfaceHolder = surfaceHolder;
    }


    /**=======================播放器监听==================================*/
    @Override
    public void onPrepared(IMediaPlayer mp) {
        LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:268)--Info-->>Media onPrepared");
        //surfaceView.setBackgroundColor(Color.TRANSPARENT);
        setDisplay(mSurfaceHolder);
    }

    @Override
    public void onCompletion(IMediaPlayer mp) {
        //surfaceView.setBackgroundColor(Color.BLACK);
        LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:274)--Info-->>Media onCompletion");
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        //一直打印先注掉
        //LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:280)--Info-->>Media onBufferingUpdate:"+percent);
    }

    @Override
    public void onSeekComplete(IMediaPlayer mp) {
        LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:285)--Info-->>Media onSeekComplete");
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
        LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:290)--Info-->>Media onVideoSizeChanged--width:"+"height:"+height+"sar_num:"+sar_num+"sar_den:"+sar_den);
    }

    @Override
    public boolean onError(IMediaPlayer mp, int what, int extra) {
        LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:295)--Info-->>Media onError--what:"+what+"--extra:"+extra);
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        LogUtil.d(TAG,"BasePlayer(VooleBasePlayer.java:301)--Info-->>Media onInfo--what:"+what+"--extra:"+extra);
        return false;
    }
}
