package com.ayearn.playerlib.base;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-1-8 19:17
 */

public class BasePlayItem implements Cloneable{

    private String playUrl;

    private long continueTime;


    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public long getContinueTime() {
        return continueTime;
    }

    public void setContinueTime(long continueTime) {
        this.continueTime = continueTime;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public BasePlayItem cloneOb(){
        try {
            return (BasePlayItem) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
