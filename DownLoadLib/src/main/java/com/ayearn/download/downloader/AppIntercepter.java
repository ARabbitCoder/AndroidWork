package com.ayearn.download.downloader;

import com.alibaba.fastjson.JSON;
import com.ayearn.db.bean.DownLoadInfo;
import com.ayearn.db.generated.GreenDaoManager;
import com.voole.utils.encrypt.MD5;
import com.voole.utils.rxjava.Result;

import java.io.File;
import java.util.List;

import io.reactivex.ObservableEmitter;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-13 17:38
 */

public class AppIntercepter extends RxInterceptor{
    private String filmname="";
    public DownLoadInfo downLoadInfo;
    private boolean isContinue = false;
    public AppIntercepter(String url, File file) {
        super(url, file);
        initDownloadInfo(url);
    }

    public AppIntercepter(String url, File file, String md5) {
        super(url, file, md5);
        initDownloadInfo(url);
    }

    public AppIntercepter(String url, File file, String md5, String filmname) {
        super(url, file, md5);
        this.filmname =filmname;
        initDownloadInfo(url);
    }

    private void initDownloadInfo(String url){
        String key = MD5.getMD5ofStr(downloadurl);
        List<DownLoadInfo> loadInfos = GreenDaoManager.getInstance().queryDownloadByUrl(key);
        if(loadInfos.size()>0){
            downLoadInfo = loadInfos.get(0);
            if(downLoadInfo.getDownloadstatus()==2){
                downLoadInfo.setDownloadstatus(-1);
            }
            isContinue = true;
        }else{
            downLoadInfo = new DownLoadInfo();
            downLoadInfo.setDownloadid(key);
            downLoadInfo.setDownloadurl(downloadurl);
            downLoadInfo.setDownloadpercent("0");
            downLoadInfo.setDownloadstatus(-1);
            downLoadInfo.setIsdownloadcomplete(false);
            downLoadInfo.setDownloadname(filmname);
            downLoadInfo.setSavefilepath(file.getAbsolutePath());
            downLoadInfo.setFilemd5(fileMd5);
            long id = GreenDaoManager.getInstance().insertDownloadInfo(downLoadInfo);
            downLoadInfo.setId(id);
            isContinue = false;
        }
    }

    @Override
    public void subscribe(ObservableEmitter e) throws Exception {
        super.subscribe(e);
        if(downLoadInfo!=null){
            sendDownloadInfo(downLoadInfo);
        }
    }

    @Override
    public boolean isContinueDownload(String downloadurl, File targetFile) {
       /*String key = MD5.getMD5ofStr(downloadurl);
        List<DownLoadInfo> loadInfos = GreenDaoManager.getInstance().queryDownloadByUrl(key);
        if(loadInfos.size()>0){
            downLoadInfo = loadInfos.get(0);
            if (!downLoadInfo.getIsdownloadcomplete()){
                sendDownloadInfo(downLoadInfo);
                return true;
            }
        }else{
            downLoadInfo = new DownLoadInfo();
            downLoadInfo.setDownloadid(key);
            downLoadInfo.setDownloadurl(downloadurl);
            downLoadInfo.setDownloadpercent("0");
            downLoadInfo.setDownloadstatus(-1);
            downLoadInfo.setIsdownloadcomplete(false);
            downLoadInfo.setDownloadname(filmname);
            downLoadInfo.setSavefilepath(targetFile.getAbsolutePath());
            downLoadInfo.setFilemd5(fileMd5);
            long id = GreenDaoManager.getInstance().insertDownloadInfo(downLoadInfo);
            downLoadInfo.setId(id);
            return false;
        }*/
        //sendDownloadInfo(downLoadInfo);
        return isContinue;
    }

    private void sendDownloadInfo(DownLoadInfo downLoadInfo){
        if(emitter==null){
            return;
        }
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(-1, JSON.toJSONString(downLoadInfo)));
        }
    }
    private void saveDownloadInfo(DownLoadInfo downLoadInfo){
        GreenDaoManager.getInstance().updateDownloadInfo(downLoadInfo);
    }

    @Override
    public void downloadPercent(int percent) {
        super.downloadPercent(percent);
        downLoadInfo.setDownloadpercent(percent+"");
        downLoadInfo.setDownloadstatus(2);
        saveDownloadInfo(downLoadInfo);
    }

    @Override
    public void downloadFailed(String downloadurl, File targetFile, String reason) {
        super.downloadFailed(downloadurl, targetFile, reason);
        isContinue = false;
        downLoadInfo.setDownloadstatus(4);
    }

    @Override
    public void downloadSuccess(String downloadurl, File targetFile) {
        super.downloadSuccess(downloadurl, targetFile);
        downLoadInfo.setDownloadstatus(3);
        downLoadInfo.setIsdownloadcomplete(true);
        saveDownloadInfo(downLoadInfo);
    }

    @Override
    public void downloadStoped(String downloadurl, File targetFile) {
        super.downloadStoped(downloadurl, targetFile);
        if(!isContinue){
            isContinue = true;
        }
        downLoadInfo.setDownloadstatus(1);
        saveDownloadInfo(downLoadInfo);
    }
}
