package com.hengda.smart.gxkjg.ui.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdviceActivity extends BaseActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.title_txt)
    TextView titleTxt;
    @Bind(R.id.edit_advice)
    EditText editAdvice;
    @Bind(R.id.bt_advice)
    Button btAdvice;
    @Bind(R.id.activity_advice)
    LinearLayout activityAdvice;
    @Bind(R.id.navigate)
    ImageButton navigate;
    private String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(this);
        titleTxt.setTypeface(HdApplication.typefaceGxa);
        ivBack.setOnClickListener(v -> finish());
        if (HdAppConfig.getUserType().equals("child")) {
            titleTxt.setTypeface(HdApplication.typefaceGxc);
            activityAdvice.setBackground(getResources().getDrawable(R.mipmap.bg_peer_c));
        }else {
            titleTxt.setTypeface(HdApplication.typefaceGxa);
            activityAdvice.setBackground(getResources().getDrawable(R.mipmap.common));
        }
        if (HdAppConfig.getLanguage().equals("ENGLISH")){
            btAdvice.setBackground(getResources().getDrawable(R.mipmap.e_submit));
        }else {
            btAdvice.setBackground(getResources().getDrawable(R.mipmap.submit_advice));
        }
        btAdvice.setOnClickListener(v -> {
            if (NetUtil.isConnected(AdviceActivity.this)) {
                if (TextUtils.isEmpty(editAdvice.getText().toString())){
                    CommonUtil.showToast(AdviceActivity.this,getString(R.string.no_empty));
                }else {

                    RequestApi.getInstance().makeMessage(HdAppConfig.getDeviceNo(), editAdvice.getText().toString(), this);
                    finish();
                }
            } else {
                CommonUtil.showToast(AdviceActivity.this, R.string.net_not_available);
            }

        });
        navigate.setOnClickListener(view -> {
            openActivity(AdviceActivity.this,NaviGativeActivity.class);

        });
    }
}
