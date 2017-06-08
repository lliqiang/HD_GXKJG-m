package com.hengda.smart.gxkjg.ui.web;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OpenAc extends AppCompatActivity {

    @Bind(R.id.webview_ac)
    WebView webviewAc;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.ticket_linear)
    LinearLayout ticketLinear;
    private String url;

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
        titleTxt.setText(R.string.open_time);
        if (HdAppConfig.getUserType().equals("child")) {
            titleTxt.setTypeface(HdApplication.typefaceGxc);
            ticketLinear.setBackground(getResources().getDrawable(R.mipmap.bg_peer_c));
        } else {
            titleTxt.setTypeface(HdApplication.typefaceGxa);
            ticketLinear.setBackground(getResources().getDrawable(R.mipmap.common));
        }
        if (SingleDown.isExhibitExist("1303")) {
            url = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "1303/" + "1303.html";

            webviewAc.loadUrl("file:///" + url);
        } else {

            if (NetUtil.isConnected(OpenAc.this)) {
                HResourceUtil.showDownloadDialog(OpenAc.this);
                HResourceUtil.loadSingleExist(OpenAc.this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        Toast.makeText(OpenAc.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                        url = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "1303/" + "1303.html";
                        webviewAc.loadUrl("file:///" + url);
                    }

                    @Override
                    public void onLoadFailed() {
                        CommonUtil.showToast(OpenAc.this, getString(R.string.down_fail));
                        HResourceUtil.hideDownloadDialog();
                    }
                }, "1303");
            } else {
                Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
            }


        }
        ivBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
