package com.hengda.smart.gxkjg.ui.web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hengda.smart.gxkjg.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TextWebView extends AppCompatActivity {

    @Bind(R.id.textwebview)
    WebView textwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_web_view);
        ButterKnife.bind(this);
        WebSettings settings = textwebview.getSettings();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        //适应屏幕
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
//      settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setDomStorageEnabled(true);//开启DOM缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        textwebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        //适应屏幕

        textwebview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        textwebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
                super.onShowCustomView(view, requestedOrientation, callback);

            }
        });


//http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES%2FAR_Vedio%2F827%2FH5.html
        textwebview.loadUrl("http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/AR_Vedio/827/H5.html");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
