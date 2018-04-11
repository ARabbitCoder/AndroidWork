package com.voole.utils.device;

import android.content.Context;
import android.media.AudioManager;

/**
 * set voice util
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 上午 10:16
 */

public class VoiceUtil {

    /**
     * Set mute
     * @param context
     */
    public static void doPlayMute(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if (getCurrentPlayerVolume(context) <= 0) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
    }
    /**
     * Get the current play volume
     * @param context
     * @return
     */
    public static int getCurrentPlayerVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    /**
     * Increase in volume
     * @param context
     */
    public static void increasePlayerVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }
    /**
     * Lower the volume
     * @param context
     */
    public static void decreasePlayerVolume(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }
}
