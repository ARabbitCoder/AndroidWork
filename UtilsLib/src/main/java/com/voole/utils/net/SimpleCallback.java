package com.voole.utils.net;


import com.voole.utils.log.LogUtil;

/**
 * @author liujingwei
 * @DESC 简单的网络回调，不实现上传及下载的方法
 * @time 2017-11-9 17:04
 */

public abstract class SimpleCallback implements NetCallback{

    @Override
    public void isUploading(int currentPercent) {
        LogUtil.d("SimpleCallback","isUploading(SimpleCallback.java:14)--Info-->>no need");
    }

    @Override
    public void isDownloading(int currentPercent) {
        LogUtil.d("SimpleCallback","isUploading(SimpleCallback.java:20)--Info-->>no need");
    }
}
