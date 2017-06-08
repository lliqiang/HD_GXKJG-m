package com.hengda.smart.gxkjg.ui.web;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TransAc extends AppCompatActivity {

    @Bind(R.id.webview_ac)
    WebView webviewAc;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;

    private String url;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
        webviewAc.getSettings().setJavaScriptEnabled(true);
        webviewAc.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webviewAc.requestFocus();
        webviewAc.setBackgroundColor(Color.TRANSPARENT);
        titleTxt.setTypeface(HdApplication.typefaceGxa);
        titleTxt.setText(getString(R.string.raider_route));

        if (HdAppConfig.getUserType().equals("child")) {
            titleTxt.setTypeface(HdApplication.typefaceGxc);
        }
        // TODO: 2016/12/24 首先判断网页资源是否存在
        if (SingleDown.isExhibitExist("1302")) {
            url = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "1302/" + "1302.html";
            path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "1302/" + "1302_1.PNG";
            webviewAc.loadUrl("file:///" + url);
        } else {

                HResourceUtil.showDownloadDialog(TransAc.this);
                HResourceUtil.loadSingleExist(TransAc.this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        Toast.makeText(TransAc.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                        url = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage()+"/" +HdAppConfig.getUserType()+"/"+ "1302/" + "1302.html";
                        webviewAc.loadUrl("file:///" + url);
                    }

                    @Override
                    public void onLoadFailed() {
                        CommonUtil.showToast(TransAc.this, getString(R.string.down_fail));
                        HResourceUtil.hideDownloadDialog();
                    }
                }, "1302");




//                HResourceUtil.loadExhibit(new ILoadListener() {
//                    @Override
//                    public void onLoadSucceed() {
//                        HdAppConfig.setIsLoading(false);
//                        HResourceUtil.hideDownloadProgressDialog();
//                        url = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "1302/" + "1302.html";
//                        path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "1302/" + "1302_1.PNG";
//                        Glide.with(TransAc.this).load(path).into(imgTransline);
//                        webviewAc.loadUrl("file:///" + url);
//                    }
//
//                    @Override
//                    public void onLoadFailed() {
//
//                    }
//                }, "1302");



        }

        ivBack.setOnClickListener(v -> finish());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
