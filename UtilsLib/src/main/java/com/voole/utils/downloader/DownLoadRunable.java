package com.voole.utils.downloader;

import com.voole.utils.net.NetUtil;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-11 16:25
 */

public class DownLoadRunable implements Runnable{
    private String runadbleId;
    private BaseDownloadInterceptor baseDownloadInterceptor;
    public DownLoadRunable(String runadbleId,BaseDownloadInterceptor baseDownloadInterceptor){
        this.runadbleId =runadbleId;
        this.baseDownloadInterceptor = baseDownloadInterceptor;
    }
    @Override
    public void run() {
        NetUtil.getInstance().downLoadFileWithInterceptorSyn(runadbleId,baseDownloadInterceptor);
    }
}
