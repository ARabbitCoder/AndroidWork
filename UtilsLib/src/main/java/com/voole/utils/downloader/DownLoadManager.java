package com.voole.utils.downloader;

import com.voole.utils.encrypt.MD5;
import com.voole.utils.log.LogUtil;
import com.voole.utils.thread.CachedThreadPool;
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
    private Map<String,BaseDownloadInterceptor> interceptorMap;
    private DownLoadManager(){
        initMap();
    }

    public void initMap(){
        if(interceptorMap==null){
            interceptorMap = new HashMap<String, BaseDownloadInterceptor>();
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

    public void startDownload(String url,RxDownloadIntercepter interceptor,DownloadViewIml iml){
        String key = MD5.getMD5ofStr(url);
        if(interceptorMap!=null){
            initMap();
        }
        BaseDownloadInterceptor tempinterceptor = interceptorMap.get(key);
        if(tempinterceptor!=null){
            if(tempinterceptor.isDownloading){
                LogUtil.d(TAG,"startDownload(DownLoadManager.java:52)--Info-->>already in downloading");
                return;
            }
        }
        interceptorMap.put(key,interceptor);
        Observable.create(interceptor).observeOn(AndroidSchedulers.mainThread()).subscribe(new RxDownloadViewObserver(iml));
        CachedThreadPool.getInstance().execute(new DownLoadRunable(url,interceptor));
    }

    public void stopDownload(String url){
        if(interceptorMap==null){
            return;
        }
        String key = MD5.getMD5ofStr(url);
        BaseDownloadInterceptor interceptor = interceptorMap.get(key);
        if(interceptor!=null){
            interceptorMap.remove(key);
            interceptor.stopDownload();
        }
    }
}
