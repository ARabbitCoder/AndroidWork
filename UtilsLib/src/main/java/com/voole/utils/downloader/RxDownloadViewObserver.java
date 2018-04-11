package com.voole.utils.downloader;
import com.voole.utils.log.LogUtil;
import com.voole.utils.rxjava.Result;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-11 14:16
 */

public class RxDownloadViewObserver  implements Observer<Result> {
    private DownloadViewIml downloadViewIml;

    public RxDownloadViewObserver(DownloadViewIml downloadViewIml){
        this.downloadViewIml = downloadViewIml;
    }
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Result o) {
        LogUtil.d("RxDownloadViewObserver","onNext(RxDownloadViewObserver.java:30)--Info-->>"+o.toString());
        if(downloadViewIml==null){
            return;
        }
        int code = o.getResultCode();
        switch (code){
            case 0:
                downloadViewIml.downloadSuccess();
                break;
            case 1:
                downloadViewIml.setDownloadPercent(o.getResultMessage());
                break;
            case 2:
                downloadViewIml.downloadStoped();
                break;
            default:
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.e("RxDownloadViewObserver","onError(RxDownloadViewObserver.java:50)--Error-->>",e);
        if(downloadViewIml!=null){
            downloadViewIml.downloadFailed();
        }
    }

    @Override
    public void onComplete() {
    }
}
