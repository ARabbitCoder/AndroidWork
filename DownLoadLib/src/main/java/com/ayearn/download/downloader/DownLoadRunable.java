package com.ayearn.download.downloader;

import com.voole.utils.net.NetUtil;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-11 16:25
 */

public class DownLoadRunable implements Runnable{
    private BaseInterceptor baseDownloadInterceptor;
    public DownLoadRunable(BaseInterceptor baseDownloadInterceptor){
        this.baseDownloadInterceptor = baseDownloadInterceptor;
    }
    @Override
    public void run() {
        NetUtil.getInstance().downLoadFileWithInterceptorSyn(baseDownloadInterceptor);
    }
}
