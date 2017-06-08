package com.hengda.smart.gxkjg.ui.web;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.entity.ArEnty;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.hengda.smart.common.http.HResourceUtil.wifiSSID;

public class TextWeb extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.textWeb)
    WebView textWeb;
    @Bind(R.id.webBack)
    ImageView webBack;
    private ArEnty arEnty;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_web);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
        textWeb.getSettings().setJavaScriptEnabled(true);
        textWeb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        textWeb.setLayoutParams(new LinearLayout.LayoutParams(2880,5120));
        WebSettings settings = textWeb.getSettings();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        settings.setSupportZoom(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);

        textWeb.setInitialScale(300);
        webBack.setOnClickListener(this);
        textWeb.canGoBack();
        textWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        arEnty = (ArEnty) getIntent().getSerializableExtra("ar");
        if (arEnty != null) {
            if (NetUtil.isWifi(HdApplication.mContext) &&
                    wifiSSID.contains(HdConstants.DEFAULT_SSID)){


                path=HdConstants.DEFAULT_RES_INNER+"AR_Vedio/" + arEnty.getARID() + "/h5.html";
            }else {

                path=HdConstants.DEFAULT_RES_OUTTER+"AR_Vedio/" + arEnty.getARID() + "/h5.html";
            }
        }

        textWeb.loadUrl(path);
    }

    @Override
    public void onClick(View view) {
        finish();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && textWeb.canGoBack()) {
            textWeb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (textWeb != null) {
            textWeb.destroy();
            textWeb = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
