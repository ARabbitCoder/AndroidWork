package tv.danmaku.ijk.media.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;
import tv.danmaku.ijk.media.player.AbstractMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-1-17 21:18
 */

public class NativeMediaPlayer extends AbstractMediaPlayer implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnInfoListener,MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener{
    private MediaPlayer mediaPlayer;
    private Context context;
    private String dataSource;
    private Surface mSurface;
    public NativeMediaPlayer(Context context){
        this.context = context;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnInfoListener(this);
    }

    @Override
    public void setDisplay(SurfaceHolder sh) {
        if (sh == null){
            setSurface(null);
        }
        else{
            setSurface(sh.getSurface());
        }
    }

    @Override
    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        dataSource = uri.toString();
        mediaPlayer.setDataSource(context,uri);
    }

    @Override
    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        dataSource = uri.toString();
        mediaPlayer.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        mediaPlayer.setDataSource(fd);
    }

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        dataSource = path;
        mediaPlayer.setDataSource(path);
    }

    @Override
    public String getDataSource() {
        return dataSource;
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        mediaPlayer.prepareAsync();
    }

    @Override
    public void start() throws IllegalStateException {
        mediaPlayer.start();
    }

    @Override
    public void stop() throws IllegalStateException {
        mediaPlayer.stop();
    }

    @Override
    public void pause() throws IllegalStateException {
        mediaPlayer.pause();
    }

    @Override
    public void setScreenOnWhilePlaying(boolean screenOn) {
        mediaPlayer.setScreenOnWhilePlaying(screenOn);
    }

    @Override
    public int getVideoWidth() {

        return mediaPlayer.getVideoWidth();
    }

    @Override
    public int getVideoHeight() {
        return mediaPlayer.getVideoHeight();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long msec) throws IllegalStateException {
        mediaPlayer.seekTo(Integer.parseInt(msec+""));
    }

    @Override
    public long getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public void release() {
        mediaPlayer.release();
    }

    @Override
    public void reset() {
        mediaPlayer.reset();
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @Override
    public int getAudioSessionId() {
        return  mediaPlayer.getAudioSessionId();
    }

    @Override
    public MediaInfo getMediaInfo() {

        return null;
    }

    @Override
    public void setLogEnabled(boolean enable) {

    }

    @Override
    public boolean isPlayable() {
        return false;
    }

    @Override
    public void setAudioStreamType(int streamtype) {
        mediaPlayer.setAudioStreamType(streamtype);
    }

    @Override
    public void setKeepInBackground(boolean keepInBackground) {

    }

    @Override
    public int getVideoSarNum() {
        return 0;
    }

    @Override
    public int getVideoSarDen() {
        return 0;
    }

    @Override
    public void setWakeMode(Context context, int mode) {

    }

    @Override
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    @Override
    public boolean isLooping() {
        return mediaPlayer.isLooping();
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        return new ITrackInfo[0];
    }

    @Override
    public void setSurface(Surface surface) {
        mSurface = surface;
        if(mediaPlayer!=null){
            mediaPlayer.setSurface(mSurface);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        notifyOnBufferingUpdate(i);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        notifyOnCompletion();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        notifyOnError(i,i1);
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i1) {
        notifyOnInfo(i,i1);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        notifyOnPrepared();
    }
}
