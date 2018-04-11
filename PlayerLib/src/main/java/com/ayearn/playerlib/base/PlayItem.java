package com.ayearn.playerlib.base;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-4 14:23
 */

public class PlayItem extends BasePlayItem {
    /**
     * 单集类型
     */
    public static final int TYPE_SINGLE_FILM = 0;
    /**
     * 多集类型
     */
    public static final int TYPE_MULTIPLE_FILM = 1;
    /**
     * 广告类型
     */
    public static final int TYPE_AD = 2;
    /**
     * 播放影片类型
     */
    private int filmType=TYPE_SINGLE_FILM;
    /**
     * 名称
     */
    private String filmName;

    /**
     * 当filmType=TYPE_MULTIPLE_FILM 时下面属性有效
     */
    private int playIndex;
    private int totalPlayCount;

    public int getFilmType() {
        return filmType;
    }

    public void setFilmType(int filmType) {
        this.filmType = filmType;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public int getPlayIndex() {
        return playIndex;
    }

    public void setPlayIndex(int playIndex) {
        this.playIndex = playIndex;
    }

    public int getTotalPlayCount() {
        return totalPlayCount;
    }

    public void setTotalPlayCount(int totalPlayCount) {
        this.totalPlayCount = totalPlayCount;
    }
}
