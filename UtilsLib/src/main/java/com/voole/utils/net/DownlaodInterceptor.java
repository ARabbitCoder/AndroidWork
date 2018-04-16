package com.voole.utils.net;

import java.io.File;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-11 10:37
 */

public interface DownlaodInterceptor {
    public String getDownloadUrl();
    /**
     * 下载的路径文件
     * 这里需要对文件合法性进行校验
     * 返回false则不会进行后面的下载操作
     * @return
     */
    public File getTargetFile();

    /**
     * 是否断点下载
     * @param downloadurl
     * @return
     */
    public boolean isContinueDownload(String downloadurl,File targetFile);

    /**
     * 下载进度
     * @return
     */
    public void downloadPercent(int percent);

    /**
     *
     * @param targetFile
     * @param md5 下载文件的MD5值
     * @return
     */
    public boolean cheackFileMd5(File targetFile,String md5);

    /**
     * 下载成功
     * @param downloadurl
     * @param targetFile
     */
    public void downloadSuccess(String downloadurl,File targetFile);

    /**
     * 暂停下载
     * @param downloadurl
     * @param targetFile
     */
    public void downloadStoped(String downloadurl,File targetFile);

    /**
     * 下载失败
     * @param downloadurl
     * @param targetFile
     * @param reason
     */
    public void downloadFailed(String downloadurl,File targetFile,String reason);
    /**
     * 是否暂停下载
     * @return
     */
    public boolean isStopDownload();

}
