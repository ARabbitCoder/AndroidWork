package com.voole.utils.net;

/**
 * @author liujingwei
 * @DESC 网络请求回调
 * @time 2017-11-9 17:01
 */

public interface NetCallback {
    /**
     * 上传进度
     * @param currentPercent
     */
    public void isUploading(int currentPercent);

    /**
     * 下载进度
     * @param currentPercent
     */
    public void isDownloading(int currentPercent);

    /**
     * 失败回调
     * @param errorCode
     */
    public void isFailed(int errorCode);

    /**
     * 成功回调
     * @param result
     */
    public void isSuccessed(String result);
}
