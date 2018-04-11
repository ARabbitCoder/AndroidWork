package com.voole.utils.webview;

import android.graphics.Bitmap;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

public interface WebViewLoadListener {
	public void onPageStarted(WebView view, String url, Bitmap favicon) ;
	public void onPageFinished(WebView view, String url) ;
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
	public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
}
