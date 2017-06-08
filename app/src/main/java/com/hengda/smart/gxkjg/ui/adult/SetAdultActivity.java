package com.hengda.smart.gxkjg.ui.adult;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.dialog.DialogClickListener;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.p.ResPresenter;
import com.hengda.smart.common.mvp.v.IResLoadView;
import com.hengda.smart.common.update.CheckUpdateActivity;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.DataManager;
import com.hengda.smart.common.util.FileUtils;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.HProgressDialog;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.ui.common.SleLangActivity;
import com.hengda.smart.gxkjg.ui.common.SleUserActivity;
import com.hengda.smart.gxkjg.ui.web.EditActivity;
import com.kyleduo.switchbutton.SwitchButton;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetAdultActivity extends CheckUpdateActivity implements IResLoadView {

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
    @Bind(R.id.switch_on)
    SwitchButton aSwitch;
    TextView txtLoadProgress;
    ResPresenter resPresenter;
    @Bind(R.id.tv_userchoose)
    TextView tvUserchoose;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private HProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_set);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(SetAdultActivity.this);
        resPresenter = new ResPresenter(SetAdultActivity.this);

        tvTitle.setTypeface(HdApplication.typefaceGxa);
        tvAuto.setTypeface(HdApplication.typefaceGxa);
        tvResLoad.setTypeface(HdApplication.typefaceGxa);
        tvVersion.setTypeface(HdApplication.typefaceGxa);
        tvLang.setTypeface(HdApplication.typefaceGxa);
        tvResDle.setTypeface(HdApplication.typefaceGxa);
        tvUserchoose.setTypeface(HdApplication.typefaceGxa);
        tvAuto.setSelected(true);
        tvResLoad.setSelected(true);
        ivBack.setOnClickListener(v -> finish());

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


        tvResLoad.setOnClickListener(v -> {

           if (NetUtil.isConnected(SetAdultActivity.this)){
               if (HdAppConfig.isResLoad()) {
                   new AlertDialog.Builder(SetAdultActivity.this).setTitle(getString(R.string.res_download))
                           .setIcon(R.mipmap.lauch)
                           .setMessage(getString(R.string.res_exist))
                           .setNegativeButton(getString(R.string.cancel), null)
                           .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   HResourceUtil.showDownloadDialog(SetAdultActivity.this);
                                   HResourceUtil.loadResShow(SetAdultActivity.this, new ILoadListener() {
                                       @Override
                                       public void onLoadSucceed() {
                                           HResourceUtil.hideDownloadDialog();
                                           CommonUtil.showToast(SetAdultActivity.this, getString(R.string.down_success));
                                       }

                                       @Override
                                       public void onLoadFailed() {
                                           HResourceUtil.hideDownloadDialog();
                                           CommonUtil.showToast(SetAdultActivity.this, getString(R.string.down_fail));
                                       }
                                   });

                               }
                           }).show();
               } else {
                   new AlertDialog.Builder(SetAdultActivity.this).setTitle(getString(R.string.download))
                           .setIcon(R.mipmap.lauch)
                           .setMessage(getString(R.string.down_all))
                           .setNegativeButton(getString(R.string.cancel), null)
                           .setPositiveButton(getString(R.string.download), new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   HResourceUtil.showDownloadDialog(SetAdultActivity.this);
                                   HResourceUtil.loadResShow(SetAdultActivity.this, new ILoadListener() {
                                       @Override
                                       public void onLoadSucceed() {
                                           HResourceUtil.hideDownloadDialog();
                                           CommonUtil.showToast(SetAdultActivity.this, getString(R.string.down_success));
                                       }

                                       @Override
                                       public void onLoadFailed() {
                                           HResourceUtil.hideDownloadDialog();
                                           CommonUtil.showToast(SetAdultActivity.this, getString(R.string.down_fail));
                                       }
                                   });
                               }
                           }).show();


               }
           }else {
               Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
           }

        });


        tvVersion.setOnClickListener(v -> {
            openActivity(SetAdultActivity.this, EditActivity.class);
        });


        tvLang.setOnClickListener(v ->
                openActivity(SetAdultActivity.this, SleLangActivity.class));
        tvResDle.setOnClickListener(v -> {
            DialogCenter.showProgressDialog(SetAdultActivity.this, R.string.waiting, false);
            new Handler().postDelayed(() -> {
                FileUtils.deleteFile(HdAppConfig.getDefaultFileDir() + "CHINESE");
                FileUtils.deleteFile(HdAppConfig.getDefaultFileDir() + "ENGLISH");
                DialogCenter.hideProgressDialog();
                CommonUtil.showToast(SetAdultActivity.this, R.string.clear_res_succeed);
            }, 2000L);
        });
        tvUserchoose.setOnClickListener(v -> openActivity(SetAdultActivity.this, SleUserActivity.class));
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void resYes() {
        CommonUtil.showToast(SetAdultActivity.this, R.string.load_succeed);
    }

    @Override
    public void showLoadingDialog() {
        txtLoadProgress = (TextView) getLayoutInflater().inflate(R.layout
                .dialog_custom_view_txt, null);
        txtLoadProgress.setTypeface(HdApplication.typefaceGxa);
        txtLoadProgress.setText(R.string.downloading);
        DialogCenter.showDialog(SetAdultActivity.this, txtLoadProgress,
                new DialogClickListener() {
                    @Override
                    public void p() {
                        resPresenter.cancelResLoad();
                        DialogCenter.hideDialog();
                    }
                },
                new int[]{R.string.download, R.string.cancel});
    }

    @Override
    public void updateLoadProgress(int fileType, long progress, long total) {
        runOnUiThread(() -> {
            Logger.e(progress + "/" + total);
            if (progress == total && fileType == HdConstants.LOADING_RES) {
                txtLoadProgress.setText(R.string.unzipping);
            } else {
                txtLoadProgress.setText(getString(fileType == HdConstants.LOADING_RES ?
                        R.string.downloading_res : R.string.downloading_db) +
                        String.format("(%s/%s)",
                                DataManager.getFormatSize(progress),
                                DataManager.getFormatSize(total)));
            }
        });
    }

    @Override
    public void hideLoadingDialog() {
        DialogCenter.hideDialog();
    }

    @Override
    public void loadFailed() {
        CommonUtil.showToast(SetAdultActivity.this, R.string.load_failed);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SetAdult Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}