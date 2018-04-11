package com.voole.utils.rxjava;

import com.voole.utils.log.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author liujingwei
 * @DESC Observable.retryWhen(new RetryWithDelay(3,1000))
 * @time 2018-3-9 11:58
 */

public class RetryWithDelay implements Function<Observable<Throwable>, ObservableSource<?>> {
    /**
     * 最多重试次数
     */

    private final int maxRetries;
    /**
     * 延迟多少时间进行重试 单位毫秒
     */
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }


    @Override
    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable
                .flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (++retryCount <= maxRetries) {
                            LogUtil.d("RetryWithDelay","onError Retry times>>"+retryCount);
                            LogUtil.e("RetryWithDelay","onError--Occur--ErrorReason--is-->>",throwable);
                            return Observable.timer(retryDelayMillis,
                                    TimeUnit.MILLISECONDS);
                        }
                        LogUtil.d("RetryWithDelay","onError--RetryFailed-->>Send the error");
                        return Observable.error(throwable);
                    }
                });
    }
}
