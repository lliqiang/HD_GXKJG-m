package com.hengda.smart.gxkjg.ui.common;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.dialog.DialogClickListener;
import com.hengda.smart.common.mvp.p.DbPresenter;
import com.hengda.smart.common.mvp.v.IDbLoadView;
import com.hengda.smart.common.permission.Acp;
import com.hengda.smart.common.permission.AcpListener;
import com.hengda.smart.common.permission.AcpOptions;
import com.hengda.smart.common.update.CheckCallback;
import com.hengda.smart.common.update.CheckResponse;
import com.hengda.smart.common.update.CheckUpdateActivity;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetStateMonitor;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.ui.adult.MainAdultActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SleLangActivity extends CheckUpdateActivity implements IDbLoadView {

    @Bind(R.id.ibEnter)
    ImageButton ibEnter;
    @Bind(R.id.ivBallChinese)
    ImageView ivBallChinese;
    @Bind(R.id.rlChinese)
    RelativeLayout rlChinese;
    @Bind(R.id.ivBallEnglish)
    ImageView ivBallEnglish;
    @Bind(R.id.rlEnglish)
    RelativeLayout rlEnglish;
    DbPresenter dbPresenter;
    NetStateMonitor netStateMonitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sle_lang);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(SleLangActivity.this);
        HdApplication.addActivity(this);

        dbPresenter = new DbPresenter(SleLangActivity.this);
        initListner();
        if (TextUtils.equals(HdConstants.LANG_CHINESE, HdAppConfig.getLanguage()))
            setLanguage(HdConstants.LANG_CHINESE);
        else
            setLanguage(HdConstants.LANG_ENGLISH);

    }

    private void initListner() {
        ibEnter.setOnClickListener(v -> {
            if (TextUtils.equals(HdAppConfig.getUserType(), HdConstants.CHILD))
                openActivity(SleLangActivity.this, SleUserActivity.class);
            else
                openActivity(SleLangActivity.this, SleUserActivity.class);
        });
        rlChinese.setOnClickListener(v -> {
            setLanguage(HdConstants.LANG_CHINESE);
            if (TextUtils.equals(HdAppConfig.getUserType(), HdConstants.CHILD))
                openActivity(SleLangActivity.this, SleUserActivity.class);
            else
                openActivity(SleLangActivity.this, SleUserActivity.class);
        });
        rlEnglish.setOnClickListener(v -> {
            setLanguage(HdConstants.LANG_ENGLISH);
            if (TextUtils.equals(HdAppConfig.getUserType(), HdConstants.CHILD))
                openActivity(SleLangActivity.this, SleUserActivity.class);
            else
                openActivity(SleLangActivity.this, SleUserActivity.class);
        });
    }

    /**
     * 设置语言
     *
     * @param lang
     */
    private void setLanguage(String lang) {
        switch (lang) {
            case HdConstants.LANG_CHINESE:
                ivBallChinese.setVisibility(View.VISIBLE);
                ivBallEnglish.setVisibility(View.INVISIBLE);
                break;
            case HdConstants.LANG_ENGLISH:
                ivBallChinese.setVisibility(View.INVISIBLE);
                ivBallEnglish.setVisibility(View.VISIBLE);
                break;
        }
        CommonUtil.configLanguage(SleLangActivity.this, lang);
        HdAppConfig.setLanguage(lang);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ibEnter.setOnClickListener(v -> {
            if (TextUtils.equals(HdAppConfig.getUserType(), HdConstants.CHILD))

                openActivity(SleLangActivity.this, SleUserActivity.class);

            else
                openActivity(SleLangActivity.this, SleUserActivity.class);
        });
        rlChinese.setOnClickListener(v -> {
            setLanguage(HdConstants.LANG_CHINESE);
            if (TextUtils.equals(HdAppConfig.getUserType(), HdConstants.CHILD))
                openActivity(SleLangActivity.this, SleUserActivity.class);
            else
                openActivity(SleLangActivity.this, SleUserActivity.class);
        });
        rlEnglish.setOnClickListener(v -> {
            setLanguage(HdConstants.LANG_ENGLISH);
            if (TextUtils.equals(HdAppConfig.getUserType(), HdConstants.CHILD))
                openActivity(SleLangActivity.this, SleUserActivity.class);
            else
                openActivity(SleLangActivity.this, SleUserActivity.class);
        });
        if (TextUtils.equals(HdConstants.LANG_CHINESE, HdAppConfig.getLanguage()))
            setLanguage(HdConstants.LANG_CHINESE);
        else
            setLanguage(HdConstants.LANG_ENGLISH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {
                //检查数据库

                Acp.getInstance(SleLangActivity.this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
                    @Override
                    public void onGranted() {


                        dbPresenter.checkDb(SleLangActivity.this);
                        //请求设备号
//


                        //检查新版本
                        checkNewVersion(new CheckCallback() {
                            @Override
                            public void hasNewVersion(CheckResponse checkResponse) {
                                showHasNewVersionDialog(checkResponse);
                            }
                        });


                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        CommonUtil.showToast(SleLangActivity.this, "权限拒绝");
                    }

                });


            }

            @Override
            public void onDisconnected() {
                CommonUtil.showToast(SleLangActivity.this, R.string.net_not_available);
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }

    @Override
    public void dbYes() {
        ibEnter.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingDialog() {
        DialogCenter.showProgressDialog(SleLangActivity.this, R.string.initializing, false);
    }

    @Override
    public void hideLoadingDialog() {
        DialogCenter.hideProgressDialog();
    }

    @Override
    public void loadFailed() {
        DialogCenter.showDialog(SleLangActivity.this, new DialogClickListener() {
            @Override
            public void p() {
                super.p();
            }

            @Override
            public void n() {
                finish();
            }
        }, new int[]{R.string.warm_tip, R.string.init_failed, R.string.retry, R.string.close});
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(netStateMonitor);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
