package com.ayearn.download.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.ayearn.download.downloader.DownLoadManager;
import com.voole.utils.log.LogUtil;

import java.io.File;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-16 13:42
 */

public class DownLoadService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("DownLoadService","onStartCommand(DownLoadService.java:29)--Info-->>onStartCommand");
        praseIntent(intent);
        return START_STICKY;
    }

    private void praseIntent(Intent intent){
        if(intent==null){
            return;
        }
        String url = intent.getStringExtra("downloadurl");
        if(TextUtils.isEmpty(url)){
            return;
        }
        String downloadPath = intent.getStringExtra("downloadpath");
        String filemd5 = intent.getStringExtra("filemd5");
        String filmname = intent.getStringExtra("filmname");
        DownLoadManager.getInstance().startDownload(url,new File(downloadPath),filemd5,filmname,null);
    }

    @Override
    public void onDestroy() {
        LogUtil.d("DownLoadService","onStartCommand(DownLoadService.java:51)--Info-->>onDestroy");
        super.onDestroy();
    }
}
