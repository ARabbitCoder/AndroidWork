package com.voole.utils.device;

import android.content.Context;
import android.media.AudioManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Play Util
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 上午 10:25
 */

public class PlayUtil {
    /**
     * get PidOfAgent
     * @param pName
     * @return
     */
    public static String findPidOfAgent(String pName) {
        String pid;
        String temp;
        pid = "";
        try {
            Process p = Runtime.getRuntime().exec("ps");
            p.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            while ((temp = stdInput.readLine()) != null) {
                if (temp.contains(pName)) {
                    String[] cmdArray = temp.split(" +");
                    pid = cmdArray[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return pid;
    }

    /**
     * set play state
     * @param context
     * @param state
     */
    public static void setPlayerMute(Context context, boolean state) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, state);
    }
}
