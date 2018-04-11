package com.voole.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.voole.utils.downloader.DownlaodInterceptor;
import com.voole.utils.encrypt.MD5;
import com.voole.utils.neterror.ErrorCode;
import com.voole.utils.log.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * @author liujingwei
 * @DESC 网络传输相关处理类
 * @time 2017-11-9 17:00
 */
public class NetUtil {
    private static NetUtil netUtil;
    private static String TAG = "NetUtil";
    /**
     * 任何文件类型
     */
    private static String CONTENT_TYPE_STREAM = "application/octet-stream";
    /**
     * 图片文件类型
     */
    private static String CONTENT_TYPE_IMAGE = "image/*";
    /**
     * 文本类型
     */
    private static String CONTENT_TYPE_TXT = "text/*";
    /**
     * 视频类型
     */
    private static String CONTENT_TYPE_VIDEO = "video/*";

    private static String HTTP_URL_HEADER = "http://";
    private OkHttpClient client;

    public static NetUtil getInstance() {
        if (netUtil == null) {
            synchronized (NetUtil.class) {
                if (netUtil == null) {
                    netUtil = new NetUtil();
                    return netUtil;
                }
            }
        }
        return netUtil;
    }

    /**
     * 创建一个OKHttpClient
     *
     * @param connectTimeout 单位seconds 连接超时时间如果为0或者小于0，会默认为5秒
     * @param readTimeOut    单位seconds 读取超时时间如果为0或者小于0，会默认为5秒
     * @param writeTimeOut   单位seconds 写入超时时间如果为0或者小于0，会默认为5秒
     * @return
     */
    public OkHttpClient createOKHttpClient(int connectTimeout, int readTimeOut, int writeTimeOut) {
        if (connectTimeout == 0 || connectTimeout < -1) {
            connectTimeout = 10;
        }
        if (readTimeOut == 0 || readTimeOut < -1) {
            readTimeOut = 10;
        }

        if (writeTimeOut == 0 || writeTimeOut < -1) {
            writeTimeOut = 10;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS);
        if (isDebug) {
            builder.addInterceptor(new LogInterceptor());
        }
        return builder.build();

    }

    /**
     * 非特殊情况建议不要使用该方法
     *
     * @return true 有网  false 无网
     * @desc 检测网络连接, 检测百度, 该方法为同步执行注意区分线程
     */
    public boolean checkNetConnection() {
        Runtime runtime = Runtime.getRuntime();
        Process ipProcess = null;
        try {
            ipProcess = runtime.exec("ping -c 1 www.baidu.com");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "checkNetConnection(NetUtil.java:116)--Error-->>", e);
        } finally {
            if (ipProcess != null) {
                ipProcess.destroy();
            }
        }
        return false;
    }

    /**
     * 建议根据文件大小设置超时时间 默认是10,10,50 秒
     * 上传文件不带参数，就是普通的文件上传
     *
     * @param actionURL 上传地址
     * @param filepath  文件绝对路径
     * @param callback  回调
     */
    public void upLoadFileNoParm(String actionURL, String filepath, final NetCallback callback) {
        File file = new File(filepath);
        if (!file.exists()) {
            callback.isFailed(ErrorCode.ERROR_FILE_NOT_EXIST);
            return;
        }
        if (!actionURL.startsWith(HTTP_URL_HEADER)) {
            callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            LogUtil.d(TAG, "upLoadFileNoParm(NetUtils.java:136)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse(CONTENT_TYPE_STREAM), file))
                .build();
        Request request = new Request.Builder().url(actionURL).post(requestBody).build();
        OkHttpClient client = createOKHttpClient(10, 10, 50);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "onFailure(NetUtil.java:148)--Error-->>", e);
                callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.isSuccessed(response.message());
                } else {
                    LogUtil.d(TAG, "onResponse(NetUtils.java:157)--IOError-->>" + ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                    callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                }
            }
        });
    }

    /**
     * @param actionURL 上传地址
     * @param params    附加参数和文件（是文件和字符串的键值对）
     * @param timeout   writetimeout 写入超时单位秒根据文件大小传入，越大时间应该越长
     * @param callback  回调
     */
    public void upLoadFileWithParm(final String actionURL, final Map<String, Object> params, final int timeout, final NetCallback callback) {
        MultipartBody.Builder mbuilder = new MultipartBody.Builder();
        mbuilder.setType(MultipartBody.FORM);
        for (String key : params.keySet()) {
            Object ob = params.get(key);
            if (ob instanceof File) {
                File tempFile = (File) ob;
                if (!tempFile.exists() || tempFile.isDirectory()) {
                    LogUtil.d(TAG, "upLoadFileWithParm(NetUtils.java:178)--Error-->>" + ErrorCode.ERROR_FILE_NOT_EXIST_OR_ISDIR);
                    callback.isFailed(ErrorCode.ERROR_FILE_NOT_EXIST_OR_ISDIR);
                    return;
                } else {
                    RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE_STREAM), tempFile);
                    mbuilder.addFormDataPart(key, tempFile.getName(), requestBody);
                    LogUtil.d(TAG, "upLoadFileWithParmWithProgress(NetUtils.java:184)--Info-->>add");
                }
            } else {
                mbuilder.addFormDataPart(key, ob.toString());
            }
        }
        RequestBody requestBody = mbuilder.build();
        Request request = new Request.Builder().url(actionURL).post(requestBody).build();
        //----------------设置上传超时------------------
        OkHttpClient client = createOKHttpClient(10, 10, timeout);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "onFailure(NetUtil.java:197)--Error-->>", e);
                callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.isSuccessed(response.body().string());
                } else {
                    LogUtil.d(TAG, "onResponse(NetUtils.java:206)--IOError-->>reponsecode" + response.code() + "---" + ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                    callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                }
            }
        });
    }


    /**
     * 带进度的requestBody
     *
     * @param contentType
     * @param file
     * @param callback
     * @param <T>
     * @return
     */
    public <T> RequestBody createRequestBody(final MediaType contentType, final File file, final NetCallback callback) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() throws IOException {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long totallength = contentLength();
                    long curentlength = 0;
                    for (long readcount; (readcount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readcount);
                        curentlength += readcount;
                        int currentPercent = (int) (curentlength / totallength) * 100;
                        callback.isUploading(currentPercent);
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, "createRequestBody(NetUtils.java:249)--Error-->>" + ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                    callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    /**
     * 同步上传文件带进度
     *
     * @param actionURL 上传地址
     * @param filepath  文件绝对路径
     * @param timeout   写入超时单位秒
     * @param callback  回调
     */
    public void upLoadFileNoParmwWithProgress(final String actionURL, final String filepath, final int timeout, final NetCallback callback) {
        File file = new File(filepath);
        if (!file.exists()) {
            callback.isFailed(ErrorCode.ERROR_FILE_NOT_EXIST);
            return;
        }

        if (!actionURL.startsWith(HTTP_URL_HEADER)) {
            callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            LogUtil.d(TAG, "upLoadFileNoParm(NetUtils.java:274)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return;
        }
        RequestBody progressBody = createRequestBody(MediaType.parse(CONTENT_TYPE_STREAM), file, callback);
        Request request = new Request.Builder().url(actionURL).post(progressBody).build();

        OkHttpClient client = createOKHttpClient(10, 10, timeout);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "onFailure(NetUtil.java:284)--Error-->>", e);
                callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.isSuccessed(response.message());
                } else {
                    LogUtil.d(TAG, "onResponse(NetUtils.java:292)--IOError-->>" + ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                    callback.isFailed(ErrorCode.ERROR_NET_UPLOAD_FILE_FAILED);
                }
            }
        });
    }

    /**
     * 异步方式
     *
     * @param downlaodurl  下载地址
     * @param filepath     下载文件路径
     * @param filename     下载文件名称
     * @param isforceclear 是否清除已存在文件,如果不清除已存在文件，文件存在时会直接回调下载成功
     * @param callback     回调
     * @param <T>
     */
    public <T> void downloadFileWithProgress(final String downlaodurl, String filepath, String filename, boolean isforceclear, final NetCallback callback) {
        final File file = new File(filepath, filename);
        if (file.exists() && !isforceclear) {
            callback.isSuccessed("file exist");
            return;
        }
        LogUtil.d(TAG, "downloadFileWithProgress(NetUtils.java:314)--url-->>" + downlaodurl);
        Request request = new Request.Builder().url(downlaodurl).build();
        createOKHttpClient(5, 3000, 5).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.e(TAG, "onFailure(NetUtil.java:319)--Error-->>", e);
                callback.isFailed(ErrorCode.ERROR_NET_DOWNLOAD_FILE_FAILED);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    LogUtil.d(TAG, "onResponse(NetUtil.java:331)--Info-->>total " + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    long time = System.currentTimeMillis();
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        LogUtil.d(TAG, "onResponse(NetUtil.java:337)--Info-->>current " + current);
                        fos.write(buf, 0, len);
                        long ctime = System.currentTimeMillis();
                        //大于一秒才回调
                        if ((ctime - time) >= 1000 || current == total) {
                            time = ctime;
                            int percent = (int) ((current * 1.0f / total) * 100);
                            callback.isDownloading(percent);
                        }
                    }
                    fos.flush();
                    LogUtil.d(TAG, "downloadFileWithProgress(NetUtils.java:341)--Success-->>" + downlaodurl);
                    callback.isSuccessed("download success");
                } catch (IOException e) {
                    LogUtil.e(TAG, "onResponse(NetUtil.java:344)--Error-->>", e);
                    callback.isFailed(ErrorCode.ERROR_NET_DOWNLOAD_FILE_FAILED);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        LogUtil.e(TAG, "onResponse--finally--(NetUtil.java:355)--Error-->>", e);
                    }
                }
            }
        });
    }

    /**
     * 线程同步方式下载文件,支持断点下载
     * @param downloadurl
     * @param downlaodInterceptor
     */
    public void downLoadFileWithInterceptorSyn(String downloadurl, DownlaodInterceptor downlaodInterceptor) {
        if (downlaodInterceptor == null) {
            return;
        }
        File file = downlaodInterceptor.getTargetFile();
        if (file.exists() && file.isDirectory()) {
            downlaodInterceptor.downloadFailed(downloadurl, file, "target is directory");
            return;
        }
        boolean isContinue = downlaodInterceptor.isContinueDownload(downloadurl, file);
        Request.Builder requestbuilder = new Request.Builder().url(downloadurl);
        long current = 0;
        if (isContinue) {
            long total = getContentLength(downloadurl);
            current = file.length();
            LogUtil.d(TAG,"downLoadFileWithInterceptorSyn(NetUtil.java:406)--Info-->>broken potion download "+current);
            requestbuilder.header("RANGE", "bytes=" + current + "-" + total).build();
        }
        Call call = createOKHttpClient(5, 3000, 5).newCall(requestbuilder.build());
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            Response response = call.execute();
            byte[] buf = new byte[2048];
            int len = 0;
            long total = response.body().contentLength();
            LogUtil.d(TAG,"downLoadFileWithInterceptorSyn(NetUtil.java:406)--Info-->>broken potion download "+total);
            LogUtil.d(TAG, "onResponse(NetUtil.java:331)--Info-->>total " + total);
            is = response.body().byteStream();
            fos = new FileOutputStream(file);
            long time = System.currentTimeMillis();
            while ((len = is.read(buf)) != -1&&!downlaodInterceptor.isStopDownload()) {
                current += len;
                LogUtil.d(TAG, "onResponse(NetUtil.java:337)--Info-->>current " + current);
                fos.write(buf, 0, len);
                long ctime = System.currentTimeMillis();
                //大于一秒才回调
                if ((ctime - time) >= 1000 || current == total) {
                    time = ctime;
                    int percent = (int) ((current * 1.0f / total) * 100);
                    downlaodInterceptor.downloadPercent(percent);
                }
            }
            fos.flush();
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                LogUtil.e(TAG, "onResponse--finally--(NetUtil.java:355)--Error-->>", e);
            }
            if(downlaodInterceptor.isStopDownload()){
                call.cancel();
                downlaodInterceptor.downloadStoped(downloadurl,file);
            }else {
                String md5 = MD5.getFileMD5(file);
                boolean check = downlaodInterceptor.cheackFileMd5(file,md5);
                if(check){
                    downlaodInterceptor.downloadSuccess(downloadurl,file);
                }else {
                    downlaodInterceptor.downloadFailed(downloadurl,file,"md5 not match");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 异步以字符形式获取服务器数据 GET方式
     *
     * @param actionurl 请求地址
     * @param callback  回调，可以为空
     * @return
     */
    public void getDataFromServer(final String actionurl, final SimpleCallback callback) {
        LogUtil.d(TAG, "getDataFromServer(NetUtils.java:369)--url-->>" + actionurl);
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            }
            LogUtil.d(TAG, "getDataFromServer(NetUtils.java:374)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return;
        }
        final StringBuilder responseBuilder = new StringBuilder();
        Request request = new Request.Builder().url(actionurl).build();
        OkHttpClient client = createOKHttpClient(0, 0, 0);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.isFailed(ErrorCode.ERROR_NET_IO_ERROR);
                }
                LogUtil.e(TAG, "onFailure(NetUtil.java:386)--Error-->>", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream in = null;
                    try {
                        in = response.body().byteStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        if (isDebug) {
                            LogUtil.d(TAG, "onResponse(NetUtils.java:400)--info-->>" + responseBuilder.toString());
                        }
                        if (callback != null) {
                            callback.isSuccessed(responseBuilder.toString());
                        }
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                } else {
                    LogUtil.d(TAG, "getDataFromServer(NetUtils.java:411)--Error-->>" + ErrorCode.ERROR_NET_REPONSE_ERROR);
                    if (callback != null) {
                        callback.isFailed(ErrorCode.ERROR_NET_REPONSE_ERROR);
                    }
                }
            }
        });
    }

    /**
     * application/octet-stream 格式与服务端保持一致
     * 将参数字符串以流(utf-8)的方式传给服务器
     *
     * @param actionurl
     * @param params
     * @param callback
     */
    public void postStreamDataToServer(String actionurl, String params, final SimpleCallback callback) {
        postStreamDataToServer(actionurl, params, null, callback);
    }

    /**
     * application/octet-stream 格式与服务端保持一致
     * 将参数字符串以流(utf-8)的方式传给服务器
     *
     * @param actionurl
     * @param params
     * @param headers   请求头
     * @param callback
     */
    public void postStreamDataToServer(String actionurl, String params, Map<String, String> headers, final SimpleCallback callback) {
        LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:440)--url-->>" + actionurl);
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            }
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:445)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return;
        }
        if (null == params) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_PARAMS_EMPTY);
            }
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:452)--Error-->>" + ErrorCode.ERROR_NET_PARAMS_EMPTY);
            return;
        }
        final StringBuilder responseBuilder = new StringBuilder();
        OkHttpClient client = createOKHttpClient(5, 5, 5);
        byte[] pByte = null;
        try {
            pByte = params.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == pByte) {
            pByte = params.getBytes();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE_STREAM), pByte);
        Request.Builder reBuilder = new Request.Builder();
        if (null != headers) {
            Headers.Builder headers1 = new Headers.Builder();
            for (String key : headers.keySet()) {
                headers1.add(key, headers.get(key));
            }
            reBuilder.headers(headers1.build());
        }
        Request request = reBuilder.url(actionurl).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.isFailed(ErrorCode.ERROR_NET_IO_ERROR);
                }
                LogUtil.e(TAG, "onFailure(NetUtil.java:482)--Error-->>", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        InputStream in = null;
                        try {
                            in = response.body().byteStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                responseBuilder.append(line);
                            }
                            if (isDebug) {
                                LogUtil.d(TAG, "onResponse(NetUtils.java:498)--info-->>" + responseBuilder.toString());
                            }
                            if (callback != null) {
                                callback.isSuccessed(responseBuilder.toString());
                            }
                        } finally {
                            if (in != null) {
                                in.close();
                            }
                        }
                    } else {
                        LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:509)--Error-->>" + ErrorCode.ERROR_NET_REPONSE_ERROR);
                        if (callback != null) {
                            callback.isFailed(ErrorCode.ERROR_NET_REPONSE_ERROR);
                        }
                    }
                }
            }
        });
    }

    /**
     * 将参数以post表单形式的方式传给服务器
     *
     * @param actionurl
     * @param params
     * @param callback
     */
    public void postFormDataToServer(String actionurl, Map<String, String> params, final SimpleCallback callback) {
        LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:526)--url-->>" + actionurl);
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            }
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:531)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return;
        }
        if (null == params) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_PARAMS_EMPTY);
            }
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:538)--Error-->>" + ErrorCode.ERROR_NET_PARAMS_EMPTY);
            return;
        }
        final StringBuilder responseBuilder = new StringBuilder();
        OkHttpClient client = createOKHttpClient(5, 5, 5);
        FormBody.Builder formBody = new FormBody.Builder();
        if (null != params) {
            for (String key : params.keySet()) {
                formBody.add(key, params.get(key));
            }
        }
        Request request = new Request.Builder().url(actionurl).post(formBody.build()).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.isFailed(ErrorCode.ERROR_NET_IO_ERROR);
                }
                LogUtil.e(TAG, "onFailure(NetUtil.java:556--Error-->>", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    InputStream in = null;
                    try {
                        in = response.body().byteStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        if (isDebug) {
                            LogUtil.d(TAG, "onResponse(NetUtils.java:570)--info-->>" + responseBuilder.toString());
                        }
                        if (callback != null) {
                            callback.isSuccessed(responseBuilder.toString());
                        }
                    } finally {
                        if (in != null) {
                            in.close();
                        }
                    }
                } else {
                    LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:581--Error-->>" + ErrorCode.ERROR_NET_REPONSE_ERROR);
                    if (callback != null) {
                        callback.isFailed(ErrorCode.ERROR_NET_REPONSE_ERROR);
                    }
                }
            }
        });

    }

    /**
     * 同步以字符形式获取服务器数据 如果获取不到返回 ""   GET方式
     *
     * @param actionurl 请求地址
     * @param callback  回调，可以为空
     * @return
     */
    public String getDataFromServerSyn(final String actionurl, final SimpleCallback callback) {
        LogUtil.d(TAG, "getDataFromServerSyn(NetUtils.java:598)--url-->>" + actionurl);
        String result = "";
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            }
            LogUtil.d(TAG, "getDataFromServer(NetUtils.java:604)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return "-1";
        }
        final StringBuilder responseBuilder = new StringBuilder();
        Request request = new Request.Builder().url(actionurl).build();
        OkHttpClient client = createOKHttpClient(0, 0, 0);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream in = null;
                try {
                    in = response.body().byteStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    result = responseBuilder.toString();
                    if (isDebug) {
                        LogUtil.d(TAG, "onResponse(NetUtils.java:623)--info-->>" + result);
                    }
                    if (callback != null) {
                        callback.isSuccessed(result);
                    }
                } finally {
                    if (in != null) {
                        in.close();
                    }
                }
                return result;
            } else {
                LogUtil.d(TAG, "getDataFromServer(NetUtils.java:635)--Error-->>" + ErrorCode.ERROR_NET_REPONSE_ERROR);
                callback.isFailed(ErrorCode.ERROR_NET_REPONSE_ERROR);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "getDataFromServerSyn(NetUtil.java:641)--Error-->>", e);
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_IO_ERROR);
            }
            return result;
        }
    }

    /**
     * 同步以流形式获取服务器数据 如果获取不到返回 ""   GET方式
     *
     * @param actionurl 请求地址
     * @param callback  回调，可以为空
     * @return
     */
    public InputStream getStreamFromServerSyn(final String actionurl, final SimpleCallback callback) {
        LogUtil.d(TAG, "getDataFromServer(NetUtils.java:655)--url-->>" + actionurl);
        String result = "";
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_URL_NO_HTTP);
            }
            LogUtil.d(TAG, "getDataFromServer(NetUtils.java:661)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return null;
        }
        Request request = new Request.Builder().url(actionurl).build();
        OkHttpClient client = createOKHttpClient(0, 0, 0);
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                InputStream in = response.body().byteStream();
                return in;
            } else {
                LogUtil.d(TAG, "getDataFromServer(NetUtils.java:672)--Error-->>" + ErrorCode.ERROR_NET_REPONSE_ERROR);
                callback.isFailed(ErrorCode.ERROR_NET_REPONSE_ERROR);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "getDataFromServerSyn(NetUtil.java:678)--Error-->>", e);
            if (callback != null) {
                callback.isFailed(ErrorCode.ERROR_NET_IO_ERROR);
            }
            return null;
        }
    }

    /**
     * post同步方式请求服务器，结果将在result参数返回
     *
     * @param actionurl 请求地址
     * @param params    post参数
     * @param result    返回结果
     * @return
     */
    public boolean postStringToServerSyn(String actionurl, String params, String result) {
        return postStringToServerSyn(actionurl, params, null, result);
    }

    /**
     * post同步方式请求服务器，结果将在result参数返回
     *
     * @param actionurl 请求地址
     * @param params    post参数
     * @param headers   请求头
     * @param result    返回结果
     * @return
     */
    public boolean postStringToServerSyn(String actionurl, String params, Map<String, String> headers, String result) {
        LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:705)--url-->>" + actionurl);
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:707)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return false;
        }
        if (null == params) {
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:711)--Error-->>" + ErrorCode.ERROR_NET_PARAMS_EMPTY);
            return false;
        }
        final StringBuilder responseBuilder = new StringBuilder();
        OkHttpClient client = createOKHttpClient(5, 5, 5);
        byte[] pByte = null;
        try {
            pByte = params.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == pByte) {
            pByte = params.getBytes();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE_STREAM), pByte);
        Request.Builder reBuilder = new Request.Builder();
        if (null != headers) {
            Headers.Builder headers1 = new Headers.Builder();
            for (String key : headers.keySet()) {
                headers1.add(key, headers.get(key));
            }
            reBuilder.headers(headers1.build());
        }
        Request request = reBuilder.url(actionurl).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "postStringToServerSyn(NetUtil.java:743)--Error-->>", e);
        }
        return false;
    }

    /**
     * post同步方式请求服务器
     *
     * @param actionurl
     * @param params
     * @return
     */
    public String postStringToServerSyn(String actionurl, String params) {
        LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:755)--url-->>" + actionurl);
        if (!actionurl.startsWith(HTTP_URL_HEADER)) {
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:757)--Error-->>" + ErrorCode.ERROR_NET_URL_NO_HTTP);
            return "";
        }
        if (null == params) {
            LogUtil.d(TAG, "postStreamDataToServer(NetUtils.java:761)--Error-->>" + ErrorCode.ERROR_NET_PARAMS_EMPTY);
            return "";
        }
        final StringBuilder responseBuilder = new StringBuilder();
        OkHttpClient client = createOKHttpClient(5, 5, 5);
        byte[] pByte = null;
        try {
            pByte = params.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null == pByte) {
            pByte = params.getBytes();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE_STREAM), pByte);
        Request.Builder reBuilder = new Request.Builder();
        Request request = reBuilder.url(actionurl).post(requestBody).build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "postStringToServerSyn(NetUtil.java:785)--Error-->>", e);
        }
        return "";
    }

    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    private long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = createOKHttpClient(5, 5, 5).newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                response.close();
                return contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断当前是否有网络连接
     *
     * @param
     */
    public boolean isNetWorkConnected() {
        if (!isInit) {
            LogUtil.d(TAG, "Use this mothed must ensure NetUtils is init,NetUtils.getInstance().init(Context)");
            return false;
        }
        if (mcontext != null) {
            return isNetWorkConnected(mcontext);
        }
        return false;
    }

    /**
     * 判断当前是否有网络连接
     *
     * @param context
     */
    public boolean isNetWorkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mconnect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = mconnect.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断当前wifi网络是否可用
     *
     * @return
     */
    public boolean isWifiConnected() {
        if (!isInit) {
            LogUtil.d(TAG, "Use this mothed must ensure NetUtils is init,NetUtils.getInstance().init(Context)");
            return false;
        }
        if (mcontext != null) {
            return isWifiConnected(mcontext);
        }
        return false;
    }

    /**
     * 判断当前wifi网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mconnect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = mconnect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 返回当前手机网络是否可用
     *
     * @return
     */
    public boolean isMobileConnected() {
        if (!isInit) {
            LogUtil.d(TAG, "Use this mothed must ensure NetUtils is init,NetUtils.getInstance().init(Context)");
            return false;
        }
        if (mcontext != null) {
            return isMobileConnected(mcontext);
        }
        return false;
    }

    /**
     * 返回当前手机网络是否可用
     *
     * @param context
     * @return
     */
    public boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mconnect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = mconnect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 返回当前网络连接类型 -1:获取失败   3:cmnet   2:cmwap(移动已弃用？)  1:wifi
     *
     * @return
     */
    public int getNetConnectType() {
        if (!isInit) {
            LogUtil.d(TAG, "Use this mothed must ensure NetUtils is init,NetUtils.getInstance().init(Context)");
            return -1;
        }
        if (mcontext != null) {
            return getNetConnectType(mcontext);
        }
        return -1;
    }

    /**
     * 返回当前网络连接类型 -1:获取失败   3:cmnet   2:cmwap(移动已弃用？)  1:wifi
     *
     * @param context
     * @return
     */
    public int getNetConnectType(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
                netType = 3;
            } else {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }


    private Context mcontext;
    private boolean isInit = false;

    /**
     * 初始化Utils
     *
     * @param context
     */
    public void init(Context context) {
        if (context != null) {
            this.mcontext = context;
            this.isInit = true;
        }
    }

    /**
     * 是否打开调试模式，打开状态下http请求会被okhttp拦截器会被打印出来
     */
    private boolean isDebug = false;

    public void setDebugModel(boolean debug) {
        this.isDebug = debug;
    }
}
