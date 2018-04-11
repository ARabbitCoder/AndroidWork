package com.voole.utils.net;

import com.voole.utils.log.LogUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * @author liujingwei
 * @DESC okhttp网络拦截器，主要用来监控http请求状态，如果为所用的okhttpclient的添加会打印所有的请求信息
 * @time 2017-11-17 15:06
 */

public class LogInterceptor implements Interceptor {

    public static String TAG = "NetRequestLog";

    @Override
    public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        StringBuilder rsb = new StringBuilder();
        String method=request.method();
        StringBuilder postsb = new StringBuilder();
        if("POST".equals(method)){
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    postsb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                postsb.delete(postsb.length() - 1, postsb.length());
            }
        }
        rsb.append("Requset:{url:")
                .append(request.url())
                .append(" method:").append(method)
                .append(" params:")
                .append(postsb.toString())
                .append(" cost:")
                .append(duration+"msec")
                .append("}")
                .append("\nResponse:{")
                .append(content.trim())
                .append("}");
        LogUtil.d(TAG,"intercept(LogInterceptor.java:50)--Info-->>"+rsb.toString());
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }
}
