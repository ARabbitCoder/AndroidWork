package com.ayearn.download.downloader;

import android.text.TextUtils;

import com.voole.utils.net.DownlaodInterceptor;

import java.io.File;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-13 17:17
 */

public class BaseInterceptor implements DownlaodInterceptor {
    public String downloadurl;
    public File file;
    public String fileMd5;
    public boolean isStopDownload = false;
    public boolean isDownloading = false;
    public BaseInterceptor(String url,File file){
        this(url,file,"");
    }
    public BaseInterceptor(String url,File file,String md5){
        this.downloadurl = url;
        this.file = file;
        this.fileMd5="";
    }

    @Override
    public String getDownloadUrl() {
        return downloadurl;
    }

    @Override
    public File getTargetFile() {
        return file;
    }

    @Override
    public boolean isContinueDownload(String downloadurl, File targetFile) {
        return false;
    }

    @Override
    public void downloadPercent(int percent) {
        isDownloading = true;
    }

    @Override
    public boolean cheackFileMd5(File targetFile, String md5) {
        if(TextUtils.isEmpty(fileMd5)){
            return true;
        }else {
            return fileMd5.equals(md5);
        }
    }

    @Override
    public void downloadSuccess(String downloadurl, File targetFile) {
        isDownloading = false;
        isStopDownload = false;
    }

    @Override
    public void downloadStoped(String downloadurl, File targetFile) {
        isStopDownload =false;
        isDownloading = false;
    }

    @Override
    public void downloadFailed(String downloadurl, File targetFile, String reason) {
        isStopDownload =false;
        isDownloading = false;
    }

    @Override
    public boolean isStopDownload() {
        return isStopDownload;
    }

    public void stopDownload(){
        isStopDownload = true;
    }
}
