package com.voole.utils.downloader;

import android.text.TextUtils;

import com.voole.utils.log.LogUtil;

import java.io.File;

/**
 * @author liujingwei
 * @DESC 下载拦截器对下载的转态进行拦截以及下载后文件的MD5合法性校验
 * @time 2018-4-11 10:48
 */

public class BaseDownloadInterceptor implements DownlaodInterceptor{
    private File targetFile;
    private boolean isStop = false;
    private String fileMd5;
    protected boolean isDownloading = false;
    /**
     * 根据业务逻辑进行对下载地址判断
     * 子类必须重写
     * @param url
     * @return true 已存在下载任务 false 不存在下载任务 已存在下载将进行断点下载否则重新下载
     */
    public boolean checkDownLoad(String url,File targetFile){
        return false;
    }
    public BaseDownloadInterceptor(File targetFile){
        this.targetFile = targetFile;
    }

    public BaseDownloadInterceptor(File targetFile,String md5){
        this.targetFile = targetFile;
        this.fileMd5 = md5;
    }

    @Override
    public File getTargetFile() {
        //TODO
        //返回下载的文件
        return targetFile;
    }

    @Override
    public boolean isContinueDownload(String downloadurl, File targetFile) {
        //交给子类实现自己的是否已经下载或者在下载中的判断
        boolean isExist = checkDownLoad(downloadurl,targetFile);
        //不存在但是文件已存在删除文件
        //存在则进行断点下载
        if(!isExist){
            if(targetFile.exists()){
                targetFile.delete();
            }
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void downloadPercent(int percent) {
        isDownloading = true;
    }

    @Override
    public boolean cheackFileMd5(File targetFile,String md5) {
        LogUtil.d("Base","cheackFileMd5(BaseDownloadInterceptor.java:68)--Info-->>"+md5);
        if(TextUtils.isEmpty(fileMd5)){
            return true;
        }else {
            return fileMd5.equals(md5);
        }
    }

    @Override
    public void downloadSuccess(String downloadurl, File targetFile) {
        isDownloading = false;
    }

    @Override
    public void downloadStoped(String downloadurl, File targetFile) {
        isStop = false;
        isDownloading = false;
    }

    @Override
    public void downloadFailed(String downloadurl, File targetFile, String reason) {
        isDownloading = false;
    }


    @Override
    public boolean isStopDownload() {
        return isStop;
    }

    public void stopDownload(){
        isDownloading = false;
        isStop = true;
    }
}
