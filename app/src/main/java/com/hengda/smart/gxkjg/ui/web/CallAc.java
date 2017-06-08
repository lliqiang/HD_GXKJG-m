package com.hengda.smart.gxkjg.ui.web;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.ArEnty;
import com.hengda.smart.play.VedioAc;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CallAc extends AppCompatActivity {

    @Bind(R.id.webview_ac)
    WebView webviewAc;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    private String url;
    private ArEnty arEnty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
        webviewAc.getSettings().setJavaScriptEnabled(true);
        webviewAc.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webviewAc.requestFocus();
        webviewAc.canGoBack();
        webviewAc.setBackgroundColor(Color.TRANSPARENT);
        titleTxt.setTypeface(HdApplication.typefaceGxa);
        arEnty = (ArEnty) getIntent().getSerializableExtra("ar");
        titleTxt.setText(arEnty.getName());


        if (!HResourceUtil.isVedioExist(arEnty.getARID())) {

            HResourceUtil.showDownloadDialog(CallAc.this);
            HResourceUtil.loadVedio(CallAc.this, new ILoadListener() {
                @Override
                public void onLoadSucceed() {
                    Toast.makeText(CallAc.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                    HResourceUtil.hideDownloadDialog();
                    Intent intent = new Intent(CallAc.this, VedioAc.class);
                    intent.putExtra("ar", arEnty);
                    startActivity(intent);
                }

                @Override
                public void onLoadFailed() {
                    HResourceUtil.hideDownloadDialog();
                    Toast.makeText(CallAc.this, R.string.down_fail, Toast.LENGTH_SHORT).show();
                }
            },arEnty.getARID());
        }
        else {
            url = "file:///" + HdAppConfig.getDefaultFileDir() + "AR_Vedio/" + arEnty.getARID()+"/"+arEnty.getARID() + "/" + "h5.html";
            Log.i("url",url+"-----------------url");
            webviewAc.loadUrl( url);
        }

        ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
