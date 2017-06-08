package com.hengda.smart.gxkjg.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NaviGativeActivity extends AppCompatActivity implements View.OnClickListener {

    public static  String wifiSSID1 = NetUtil.getWifiSSID(HdApplication.mContext);
    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.web_navigate)
    WebView webNavigate;
    private int lan=1;
private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_gative);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
        webNavigate.getSettings().setJavaScriptEnabled(true);
        webNavigate.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webNavigate.requestFocus();
//        webNavigate.setBackgroundColor(Color.TRANSPARENT);
        titleTxt.setText(R.string.survey);
        ivBack.setOnClickListener(this);
        if (HdAppConfig.getUserType().equals("child")) {
            titleTxt.setTypeface(HdApplication.typefaceGxc);
        }
        if (HdAppConfig.getLanguage().equals("ENGLISH")){
            lan=2;
        }else {
            lan=1;
        }

        if (NetUtil.isConnected(NaviGativeActivity.this)&&wifiSSID1.contains(HdConstants.DEFAULT_SSID)){
            url="http://192.168.2.239/index.php?g=project&m=question&a=index&deviceno="+HdAppConfig.getDeviceNo()+"&language="+lan;
            webNavigate.loadUrl(url);
        }else {
            webNavigate.loadUrl("http://116.10.199.106:65521/index.php?g=project&m=question&a=index&deviceno=" + HdAppConfig.getDeviceNo() + "&language="+lan);
        }

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
