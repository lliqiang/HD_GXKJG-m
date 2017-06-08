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
import android.widget.Toast;

import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.dialog.DialogClickListener;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
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
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.ui.adult.MainAdultActivity;
import com.hengda.smart.gxkjg.ui.child.MainChildActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SleUserActivity extends CheckUpdateActivity implements IDbLoadView {

    @Bind(R.id.ibEnter)
    ImageButton ibEnter;
    @Bind(R.id.ivBallAdult)
    ImageView ivBallAdult;
    @Bind(R.id.rlAdult)
    RelativeLayout rlAdult;
    @Bind(R.id.ivBallChild)
    ImageView ivBallChild;
    @Bind(R.id.rlChild)
    RelativeLayout rlChild;
    DbPresenter dbPresenter;
    NetStateMonitor netStateMonitor;
    @Bind(R.id.ivAdult)
    ImageView ivAdult;
    @Bind(R.id.ivChild)
    ImageView ivChild;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sle_user);
        ButterKnife.bind(this);
        HdApplication.addActivity(this);
        StatusBarCompat.translucentStatusBar(SleUserActivity.this);

       if (HdAppConfig.getLanguage().equals("CHINESE")){

           ivAdult.setImageResource(R.mipmap.bg_user_type_adult);
           ivChild.setImageResource(R.mipmap.bg_user_type_child);
       }else {
           ivAdult.setImageResource(R.mipmap.txt_adult);
           ivChild.setImageResource(R.mipmap.txt_child);
       }
        dbPresenter = new DbPresenter(SleUserActivity.this);
        rlChild.setOnClickListener(view -> {
            HdAppConfig.setUserType(HdConstants.CHILD);

            if (!HdAppConfig.isFontExist()) {
                if (NetUtil.isConnected(SleUserActivity.this)) {
                    if (!HdAppConfig.isLoading()) {
                        HdAppConfig.setIsLoading(true);
                    }
                    HResourceUtil.showDownloadProgressDialog(SleUserActivity.this, getString(R.string
                            .downloading_res));
                    SingleDown.LoadFontRes(new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadProgressDialog();
                            openActivity(SleUserActivity.this, MainChildActivity.class);
                        }

                        @Override
                        public void onLoadFailed() {
                            HdAppConfig.setIsLoading(false);
                            HResourceUtil.hideDownloadProgressDialog();
                            Toast.makeText(SleUserActivity.this, getString(R.string.down_fail), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
                }

            } else {
                openActivity(SleUserActivity.this, MainChildActivity.class);
            }

        });

        ibEnter.setOnClickListener(v -> {
            //      如果是儿童的话判断儿童字体是否存在
            if (HdAppConfig.getUserType().equals("child")) {
                if (!HdAppConfig.isFontExist()) {
                    if (!HdAppConfig.isLoading()) {
                        HdAppConfig.setIsLoading(true);
                        HResourceUtil.showDownloadProgressDialog(SleUserActivity.this, getString(R.string
                                .downloading_res));
                        SingleDown.LoadFontRes(new ILoadListener() {
                            @Override
                            public void onLoadSucceed() {
                                HdAppConfig.setIsLoading(false);
                                HResourceUtil.hideDownloadProgressDialog();
                                openActivity(SleUserActivity.this, MainChildActivity.class);
                            }

                            @Override
                            public void onLoadFailed() {
                                HdAppConfig.setIsLoading(false);
                                HResourceUtil.hideDownloadProgressDialog();
                                Toast.makeText(SleUserActivity.this, getString(R.string.down_fail), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    openActivity(SleUserActivity.this, MainChildActivity.class);
                }
            } else {
                openActivity(SleUserActivity.this, MainAdultActivity.class);
            }


        });


        rlAdult.setOnClickListener(v -> {
            setUserType(HdConstants.ADULT);
            openActivity(SleUserActivity.this, MainAdultActivity.class);
        });

        if (TextUtils.equals(HdConstants.ADULT, HdAppConfig.getUserType()))
            setUserType(HdConstants.ADULT);
        else
            setUserType(HdConstants.CHILD);


    }


    @Override
    protected void onResume() {
        super.onResume();
        netStateMonitor = new NetStateMonitor() {
            @Override
            public void onConnected() {
                //检查数据库

                Acp.getInstance(SleUserActivity.this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
                    @Override
                    public void onGranted() {


                        dbPresenter.checkDb(SleUserActivity.this);
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
                        CommonUtil.showToast(SleUserActivity.this, "权限拒绝");
                    }

                });


            }

            @Override
            public void onDisconnected() {
                CommonUtil.showToast(SleUserActivity.this, R.string.net_not_available);
            }
        };
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netStateMonitor, mFilter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(netStateMonitor);
    }

    /**
     * 设置用户身份
     *
     * @param userType
     */
    private void setUserType(String userType) {

        switch (userType) {
            case HdConstants.ADULT:
                ivBallAdult.setVisibility(View.VISIBLE);
                ivBallChild.setVisibility(View.INVISIBLE);
                break;
            case HdConstants.CHILD:
                ivBallAdult.setVisibility(View.INVISIBLE);
                ivBallChild.setVisibility(View.VISIBLE);
                break;
        }
        HdAppConfig.setUserType(userType);
    }

    @Override
    public void dbYes() {
        ibEnter.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingDialog() {
        DialogCenter.showProgressDialog(SleUserActivity.this, R.string.initializing, false);
    }

    @Override
    public void hideLoadingDialog() {
        DialogCenter.hideProgressDialog();
    }

    @Override
    public void loadFailed() {
        DialogCenter.showDialog(SleUserActivity.this, new DialogClickListener() {
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setContentView(R.layout.activity_sle_user);
        ButterKnife.bind(this);
        HdApplication.addActivity(this);
        StatusBarCompat.translucentStatusBar(SleUserActivity.this);
        dbPresenter = new DbPresenter(SleUserActivity.this);

        //机器号获取



        ibEnter.setOnClickListener(v -> {
                    //      如果是儿童的话判断儿童字体是否存在
                    if (HdAppConfig.getUserType().equals("child")) {
                        if (!HdAppConfig.isFontExist()) {
                            if (NetUtil.isConnected(SleUserActivity.this)) {
                                if (!HdAppConfig.isLoading()) {
                                    HdAppConfig.setIsLoading(true);
                                    HResourceUtil.showDownloadProgressDialog(SleUserActivity.this, getString(R.string
                                            .downloading_res));
                                    SingleDown.LoadFontRes(new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            HdAppConfig.setIsLoading(false);
                                            HResourceUtil.hideDownloadProgressDialog();
                                            openActivity(SleUserActivity.this, MainChildActivity.class);
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            HdAppConfig.setIsLoading(false);
                                            HResourceUtil.hideDownloadProgressDialog();
                                            Toast.makeText(SleUserActivity.this, getString(R.string.down_fail), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } else {
                            openActivity(SleUserActivity.this, MainChildActivity.class);
                        }
                    } else {
                        openActivity(SleUserActivity.this, MainAdultActivity.class);
                    }


                }

        );

        rlAdult.setOnClickListener(v -> {
            setUserType(HdConstants.ADULT);

            openActivity(SleUserActivity.this, MainAdultActivity.class);
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
