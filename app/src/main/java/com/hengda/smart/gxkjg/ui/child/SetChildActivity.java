package com.hengda.smart.gxkjg.ui.child;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.update.CheckUpdateActivity;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.FileUtils;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.ui.common.SleLangActivity;
import com.hengda.smart.gxkjg.ui.common.SleUserActivity;
import com.hengda.smart.gxkjg.ui.web.EditActivity;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetChildActivity extends CheckUpdateActivity {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvAuto)
    TextView tvAuto;
    @Bind(R.id.tvResLoad)
    TextView tvResLoad;
    @Bind(R.id.tvVersion)
    TextView tvVersion;
    @Bind(R.id.tvLang)
    TextView tvLang;
    @Bind(R.id.tvResDle)
    TextView tvResDle;
    @Bind(R.id.c_userselect_txt)
    TextView cUserselectTxt;
    @Bind(R.id.switch_on)
    SwitchButton aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_set);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(SetChildActivity.this);
        tvTitle.setTypeface(HdApplication.typefaceGxc);
        tvAuto.setTypeface(HdApplication.typefaceGxc);
        tvResLoad.setTypeface(HdApplication.typefaceGxc);
        tvVersion.setTypeface(HdApplication.typefaceGxc);
        tvLang.setTypeface(HdApplication.typefaceGxc);
        tvResDle.setTypeface(HdApplication.typefaceGxc);
        cUserselectTxt.setTypeface(HdApplication.typefaceGxc);
        ivBack.setOnClickListener(v -> finish());
        tvResLoad.setSelected(true);
        tvAuto.setSelected(true);
        tvResDle.setOnClickListener(v -> {
            DialogCenter.showProgressDialog(SetChildActivity.this, R.string.waiting, false);
            new Handler().postDelayed(() -> {
                FileUtils.deleteFile(HdAppConfig.getDefaultFileDir() + "CHINESE");
                FileUtils.deleteFile(HdAppConfig.getDefaultFileDir() + "ENGLISH");
                DialogCenter.hideProgressDialog();
                CommonUtil.showToast(SetChildActivity.this, R.string.clear_res_succeed);
            }, 2000L);
        });
        tvResLoad.setOnClickListener(v -> {
            if (NetUtil.isConnected(SetChildActivity.this)) {
                if (HdAppConfig.isResLoad()) {
                    new AlertDialog.Builder(SetChildActivity.this).setTitle(getString(R.string.res_download))
                            .setIcon(R.mipmap.lauch)
                            .setMessage(getString(R.string.res_exist))
                            .setNegativeButton(getString(R.string.cancel), null)
                            .setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HResourceUtil.showDownloadDialog(SetChildActivity.this);
                                    HResourceUtil.loadResShow(SetChildActivity.this, new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            HResourceUtil.hideDownloadDialog();
                                            CommonUtil.showToast(SetChildActivity.this, getString(R.string.down_success));
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            HResourceUtil.hideDownloadDialog();
                                            CommonUtil.showToast(SetChildActivity.this, getString(R.string.down_fail));
                                        }
                                    });
                                }
                            }).show();
                } else {
                    new AlertDialog.Builder(SetChildActivity.this).setTitle(getString(R.string.res_download))
                            .setIcon(R.mipmap.lauch)
                            .setMessage(getString(R.string.down_all))
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HResourceUtil.showDownloadDialog(SetChildActivity.this);
                                    HResourceUtil.loadResShow(SetChildActivity.this, new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            HResourceUtil.hideDownloadDialog();
                                            CommonUtil.showToast(SetChildActivity.this, getString(R.string.down_success));
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            HResourceUtil.hideDownloadDialog();
                                            CommonUtil.showToast(SetChildActivity.this, getString(R.string.down_fail));
                                        }
                                    });
                                }
                            }).show();


                }
            } else {
                Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
            }

        });
        aSwitch.setChecked(true);

        if (HdAppConfig.getAutoPlay()) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HdAppConfig.setAutoPlay(true);
                } else {
                    HdAppConfig.setAutoPlay(false);
                }
            }
        });
        tvVersion.setOnClickListener(view -> openActivity(SetChildActivity.this, EditActivity.class));
        tvLang.setOnClickListener(v -> openActivity(SetChildActivity.this,
                SleLangActivity.class));
        cUserselectTxt.setOnClickListener(v -> openActivity(SetChildActivity.this, SleUserActivity.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
