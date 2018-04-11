package com.voole.utils.webview;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.voole.utils.device.DeviceUtil;
import com.voole.utils.log.LogUtil;

/**
 * @author fengyanjie
 * @desc 标准WebView
 * @time 2017-11-24 17:59
 */

public class StandardWebView extends WebView {
    private static final String TAG = "StandardWebView";
    private Context mContext;

    public StandardWebView(Context context) {
        super(context);
        mContext = context;
        initWebView();
    }

    public StandardWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initWebView();
    }

    public StandardWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initWebView();
    }
    private WebViewLoadListener webViewLoadListener;
    public WebViewLoadListener getWebViewLoadListener() {
        return webViewLoadListener;
    }

    public void setWebViewLoadListener(WebViewLoadListener webViewLoadListener) {
        this.webViewLoadListener = webViewLoadListener;
    }

    /**
     * 初始化并设置WebView
     */
    private void initWebView() {
        initWebSettings();
        // 初始化WebViewClient
        setWebViewClient(new VooleWebViewClient());
        // 初始化WebChromeClient
        setWebChromeClient(new VooleWebChromeClient());
    }
    /**
     * WebView设置
     */
    private void initWebSettings() {
        WebSettings settings = getSettings();
        //支持获取手势焦点
        requestFocusFromTouch();
        // 支持JS
        settings.setJavaScriptEnabled(true);
        //支持插件
        settings.setPluginState(WebSettings.PluginState.ON);
        //设置适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // 支持缩放
        settings.setSupportZoom(false);
        // 隐藏原生的缩放控件
        settings.setDisplayZoomControls(true);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.supportMultipleWindows();
        settings.setSupportMultipleWindows(true);
        // 设置缓存模式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        if (mContext != null) {
            String absolutePath = mContext.getCacheDir().getAbsolutePath();
            settings.setAppCachePath(absolutePath);
        }
        // 设置可访问文件
        settings.setAllowFileAccess(true);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        if (DeviceUtil.getSDKVersionNumber() >= 19) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }
        settings.setNeedInitialFocus(true);
        //设置编码格式
        settings.setDefaultTextEncodingName("UTF-8");
    }

    private class VooleWebViewClient extends WebViewClient {
        // 页面开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            LogUtil.d("VooleWebViewClient","(StandardWebView.java:115) "+ "onPageStarted() called with: view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
            super.onPageStarted(view, url, favicon);
            if(webViewLoadListener != null) {
                webViewLoadListener.onPageStarted(view, url, favicon);
            }
        }

        //是否在WebView内加载新页面，可以在这里判断URL做相应操作
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            LogUtil.d("VooleWebViewClient","(StandardWebView.java:122) "+ "shouldOverrideUrlLoading() called with: view = [" + view + "], request = [" + request + "]");
            view.loadUrl(request.toString());
            return true;
        }

        // 网络错误时回掉方法
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            LogUtil.d("VooleWebViewClient","(StandardWebView.java:130) "+ "onReceivedError() called with: view = [" + view + "], request = [" + request + "], error = [" + error + "]");
            super.onReceivedError(view, request, error);
            if(webViewLoadListener != null) {
                webViewLoadListener.onReceivedError(view, request, error);
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            LogUtil.d("VooleWebViewClient","(StandardWebView.java:137) "+ "onReceivedHttpError() called with: view = [" + view + "], request = [" + request + "], errorResponse = [" + errorResponse + "]");
            super.onReceivedHttpError(view, request, errorResponse);
        }

        // 页面完成加载时
        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.d("VooleWebViewClient","(StandardWebView.java:144) "+ "onPageFinished() called with: view = [" + view + "], url = [" + url + "]");
            super.onPageFinished(view, url);
            if(webViewLoadListener != null) {
                webViewLoadListener.onPageFinished(view, url);
            }
        }
    }

    /**
     * * 重写WebChromeClient 1.重写javascript中的prompt,alert,confirm对话框 2.扩充数据库的容量(*2)
     * 3.扩充缓存容量(*2)
     */
    private class VooleWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            LogUtil.d("VooleWebChromeClient","(StandardWebView.java:156) "+ "onJsPrompt() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], defaultValue = [" + defaultValue + "], result = [" + result + "]");
            Builder builder = new Builder(mContext);
            builder.setTitle("提示");
            final EditText et = new EditText(view.getContext());
            et.setSingleLine();
            et.setText(defaultValue);
            builder.setView(et);
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok,
                    new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }
                    }).setNeutralButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            LogUtil.d("VooleWebChromeClient","(StandardWebView.java:186) "+ "onJsAlert() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], result = [" + result + "]");
            Builder builder = new Builder(mContext);
            builder.setTitle("提示：");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok,
                    new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   final JsResult result) {
            LogUtil.d("VooleWebChromeClient","(StandardWebView.java:206) "+ "onJsConfirm() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], result = [" + result + "]");
            Builder builder = new Builder(mContext);
            builder.setTitle("提示：");
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok,
                    new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    });
            builder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    });
            builder.setCancelable(false);
            builder.create();
            builder.show();
            return true;
        }
    }

}
