package com.voole.utils.neterror;

/**
 * @author liujingwei
 * @DESC 错误码相关变量
 * @time 2017-11-9 16:47
 */

public class ErrorCode {

    public static String SERVER_ERROR="ServerError";
    public static String APK_ERROR="ApkError";
    public static String DEFAUL_ERROR_MSG="error";
    public static String DEFAUL_ERROR_CAUSEBY=APK_ERROR;
    public static String DEFAUL_ERROR_CODE="0000000";
    /**
     * 文件处理相关错误码
     * -------File IO Error-----------------
     */
    public static int ERROR_FILE_NOT_EXIST= 1000;
    public static int ERROR_FILE_NOT_EXIST_OR_ISDIR= 1001;

    /**
     * 网络传输相关错误码
     * -------Internet transfer Error--------
     */
    //无网络连接
    public static int ERROR_NET_NO_NET = 2000;
    //url错误
    public static int ERROR_NET_URL_NO_HTTP = 2001;
    public static int ERROR_NET_URL_WRONG = 2002;
    //网络io错误
    public static int ERROR_NET_IO_ERROR= 2003;
    //返回码在400-417  请求出错
    public static int ERROR_NET_REQUEST_ERROR = 2004;
    //返回码在500-505 服务器错误
    public static int ERROR_NET_SERVER_ERROR = 2005;
    //网络超时
    public static int ERROR_NET_TIMEOUT = 2006;

    //上传文件失败
    public static int ERROR_NET_UPLOAD_FILE_FAILED = 2008;
    //下载文件失败
    public static int ERROR_NET_DOWNLOAD_FILE_FAILED = 2009;
    //返回不成功
    public static int ERROR_NET_REPONSE_ERROR = 2010;
    //上传文件服务器内部错误
    public static int ERROR_NET_UPLOAD_FILE_SERVERERROR = 2011;
    public static int ERROR_NET_PARAMS_EMPTY = 2012;
    //网络检测相关错误码
    public static int CHEACK_BAIDU_ERROR = 4000;
    public static int CHEACK_BAIDU_SUCCESS=4001;
    public static int CHEACK_BAIDU_FAILED=4001;
}
