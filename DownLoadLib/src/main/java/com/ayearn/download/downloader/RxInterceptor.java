package com.ayearn.download.downloader;

import com.voole.utils.rxjava.Result;
import com.voole.utils.rxjava.StatusError;

import java.io.File;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-13 17:29
 */

public class RxInterceptor extends BaseInterceptor implements ObservableOnSubscribe<Result> {
    public ObservableEmitter emitter;
    public RxInterceptor(String url, File file) {
        super(url, file);
    }

    public RxInterceptor(String url, File file, String md5) {
        super(url, file, md5);
    }

    @Override
    public void subscribe(ObservableEmitter<Result> e) throws Exception {
        this.emitter = e;
    }

    @Override
    public void downloadPercent(int percent) {
        super.downloadPercent(percent);
        if(emitter==null){
            return;
        }
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(1,percent+""));
        }
    }

    @Override
    public void downloadSuccess(String downloadurl, File targetFile) {
        super.downloadSuccess(downloadurl,targetFile);
        if(emitter==null){
            return;
        }
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(0,"downlaod Success"));
            emitter.onComplete();
        }
    }

    @Override
    public void downloadStoped(String downloadurl, File targetFile) {
        super.downloadStoped(downloadurl,targetFile);
        long length = targetFile.length();
        if(emitter==null){
            return;
        }
        if(!emitter.isDisposed()){
            emitter.onNext(new Result(2,"downlaod stoped"));
            emitter.onComplete();
        }
    }

    @Override
    public void downloadFailed(String downloadurl, File targetFile, String reason) {
        super.downloadFailed(downloadurl,targetFile,reason);
        if(emitter==null){
            return;
        }
        if(!emitter.isDisposed()){
            StatusError statusError = new StatusError.ErrorBuilder().setErrorMessage(reason).create();
            emitter.onError(statusError);
        }
    }
}
