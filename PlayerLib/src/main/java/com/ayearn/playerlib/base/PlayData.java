package com.ayearn.playerlib.base;

import java.util.List;

/**
 * @author liujingwei
 * @DESC 包含了一个list 和一个PlayItem 产品信息PlayerQueryInfo
 * @time 2018-1-9 19:40
 */

public class PlayData {
    private PlayItem playItem;
    private List<PlayItem> list;
    public PlayData() {
    }

    public PlayData(PlayItem playItem, List<PlayItem> list) {
        this.playItem = playItem;
        this.list = list;
    }

    public PlayItem getPlayItem() {
        return playItem;
    }

    public void setPlayItem(PlayItem playItem) {
        this.playItem = playItem;
    }

    public List<PlayItem> getList() {
        return list;
    }

    public void setList(List<PlayItem> list) {
        this.list = list;
    }


}
