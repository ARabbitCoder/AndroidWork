package com.ayearn.download.downloader;

import com.voole.utils.encrypt.MD5;
import com.voole.utils.log.LogUtil;
import com.voole.utils.thread.CachedThreadPool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-11 16:07
 */

public class DownLoadManager {
    private static final String TAG = "DownLoadManager";
    private static DownLoadManager instance;
    private Map<String,AppIntercepter> interceptorMap;
    private DownLoadManager(){
        initMap();
    }

    private void initMap(){
        if(interceptorMap==null){
            interceptorMap = new HashMap<String, AppIntercepter>();
        }
    }
    /**
     * 单例
     */
    public static DownLoadManager getInstance(){
        if(instance==null){
            synchronized (DownLoadManager.class){
                if(instance==null){
                    instance = new DownLoadManager();
                }
                return instance;
            }
        }
        return instance;
    }

    public void startDownload(String downloadurl, File downloadfile,String filemd5,String filmname,DownloadViewIml iml){
        String key = MD5.getMD5ofStr(downloadurl);
        if(interceptorMap!=null){
            initMap();
        }
        AppIntercepter tempinterceptor = interceptorMap.get(key);
        if(tempinterceptor!=null){
            if(iml!=null){
                if(tempinterceptor.emitter==null){
                    Observable.create(tempinterceptor).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxDownloadViewObserver(iml));
                }else {
                    if(tempinterceptor.emitter.isDisposed()){
                        Observable.create(tempinterceptor).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxDownloadViewObserver(iml));
                    }
                }
            }
            if(tempinterceptor.isDownloading){
                LogUtil.d(TAG,"startDownload(DownLoadManager.java:52)--Info-->>already in downloading");
            }
            return;
        }
        tempinterceptor = new AppIntercepter(downloadurl,downloadfile,filemd5,filmname);
        interceptorMap.put(key,tempinterceptor);
        if(iml!=null){
            if(tempinterceptor.emitter==null){
                Observable.create(tempinterceptor).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxDownloadViewObserver(iml));
            }else {
                if(tempinterceptor.emitter.isDisposed()){
                    Observable.create(tempinterceptor).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxDownloadViewObserver(iml));
                }
            }
        }
        CachedThreadPool.getInstance().execute(new DownLoadRunable(tempinterceptor));
    }

    public void stopDownload(String url){
        if(interceptorMap==null){
            return;
        }
        String key = MD5.getMD5ofStr(url);
        BaseInterceptor interceptor = interceptorMap.get(key);
        if(interceptor!=null){
            interceptorMap.remove(key);
            interceptor.stopDownload();
        }
    }
}
