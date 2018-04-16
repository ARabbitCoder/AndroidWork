package com.ayearn.download.downloader;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-11 14:37
 */

public interface DownloadViewIml {
    public void initDownloadView(String json);
    public void setDownloadPercent(String percent);
    public void downloadSuccess();
    public void downloadStoped();
    public void downloadFailed();
}
