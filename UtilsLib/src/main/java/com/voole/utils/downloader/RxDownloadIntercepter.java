package com.voole.utils.downloader;

import com.voole.utils.rxjava.Result;
import com.voole.utils.rxjava.StatusError;

import java.io.File;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author liujingwei
 * @DESC 使用rxjav进行线程通信，子类需要重写父类的各种状态方法进行实现自己的下载记录的逻辑
 * @time 2018-4-11 13:58
 */

public class RxDownloadIntercepter extends BaseDownloadInterceptor implements ObservableOnSubscribe<Result> {
    private ObservableEmitter emitter;
    @Override
    public boolean checkDownLoad(String url, File targetFile) {
        return false;
    }

    public RxDownloadIntercepter(File targetFile) {
        super(targetFile);
    }

    public RxDownloadIntercepter(File targetFile, String md5) {
        super(targetFile, md5);
    }


    @Override
    public void subscribe(ObservableEmitter e) throws Exception {
        this.emitter = e;
    }

    @Override
    public void downloadPercent(int percent) {
        super.downloadPercent(percent);
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(1,percent+""));
        }
    }

    @Override
    public void downloadSuccess(String downloadurl, File targetFile) {
        super.downloadSuccess(downloadurl,targetFile);
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(0,"downlaod Success"));
            emitter.onComplete();
        }
    }

    @Override
    public void downloadStoped(String downloadurl, File targetFile) {
        super.downloadStoped(downloadurl,targetFile);
        long length = targetFile.length();
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(2,"downlaod stoped"));
            emitter.onComplete();
        }
    }

    @Override
    public void downloadFailed(String downloadurl, File targetFile, String reason) {
        super.downloadFailed(downloadurl,targetFile,reason);
        if(!emitter.isDisposed()){
            StatusError statusError = new StatusError.ErrorBuilder().setErrorMessage(reason).create();
            emitter.onError(statusError);
        }
    }
}
