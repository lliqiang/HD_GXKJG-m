package com.hengda.smart.gxkjg.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HtmlActivity extends AppCompatActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    //    private SwipeRefreshWebView swipeRefreshWebView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uri);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
//        String uri = ((URIResult) getIntent().getSerializableExtra(Intents.Scan.RESULT)).getUri();
        String uri=getIntent().getStringExtra("html");
        Log.i("uri", "uri------------" + uri);
        webView = (WebView) findViewById(R.id.webview_sq);
        titleTxt.setVisibility(View.GONE);
        ivBack.setOnClickListener(view -> finish());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        webView.canGoBack();
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadUrl(uri);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.destroy();
            webView = null;
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
