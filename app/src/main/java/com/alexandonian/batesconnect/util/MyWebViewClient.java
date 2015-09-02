package com.alexandonian.batesconnect.util;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 8/24/2015.
 */
public class MyWebViewClient extends WebViewClient {

    WebView mWebView;

    public MyWebViewClient() {
        super();
    }

    public MyWebViewClient(WebView view) {
        super();
        mWebView = view;
        mWebView.setVisibility(View.INVISIBLE);
    }

    String javascript = "javascript:(function() {" +
            "document.getElementsByTagName('header')[0].style.display = 'none'; " +
            "document.getElementById('globalNav').style .display = 'none'; " +
            "document.getElementsByClassName('breadcrumb')[0].style.display = 'none'; " +
            "document.getElementsByTagName('footer')[0].style.display = 'none'; " +
            "document.getElementsByClassName('sidebar')[0].style.display = 'none';})()";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl(javascript);
        mWebView.setVisibility(View.VISIBLE);
    }
}