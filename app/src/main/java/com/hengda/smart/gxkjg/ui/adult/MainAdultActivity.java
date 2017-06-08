package com.hengda.smart.gxkjg.ui.adult;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.FragmentUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.HProgressDialog;
import com.hengda.smart.guangxitech.GetDataActivity;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.ArEnty;
import com.hengda.smart.gxkjg.ui.common.AndroidBug54971Workaround;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.common.SleUserActivity;
import com.hengda.smart.gxkjg.ui.web.TextWeb;
import com.hengda.smart.play.VedioAc;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

//import com.hengda.smart.guangxitech.ar.video.VideoPlayback;

public class MainAdultActivity extends BaseActivity {

    @Bind(R.id.rbGuide)
    RadioButton rbGuide;
    @Bind(R.id.rgMenuMain)
    RadioGroup rgMenuMain;
    @Bind(R.id.ivSet)
    ImageView ivSet;
    @Bind(R.id.ivSearch)
    ImageView ivSearch;
    @Bind(R.id.tvAr)
    TextView tvAr;

    AGuideEntryFrg aGuideEntryFrg;
    ARaidersEntryFrg aRaidersEntryFrg;
    HProgressDialog hProgressDialog;
    @Bind(R.id.rbRaiders)
    RadioButton rbRaiders;
    @Bind(R.id.linear_main_adult)
    LinearLayout linearMainAdult;
    Intent serviceIntent;
    int SwitchF;
    private Timer timer;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //心跳上传
            if (msg.what == 1) {
                RequestApi.getInstance().postHeart(HdAppConfig.getDeviceNo(), 1, getApplicationContext());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_main);
        ButterKnife.bind(this);
        serviceIntent = new Intent(MainAdultActivity.this, BleNoService.class);
        startService(serviceIntent);
        StatusBarCompat.translucentStatusBar(MainAdultActivity.this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        EventBus.getDefault().register(this);
        HdApplication.clearActivity();
        timer = new Timer();
        if (NetUtil.isConnected(this)) {
            if (TextUtils.isEmpty(HdAppConfig.getDeviceNo())) {
                RequestApi.getInstance().getDeviceNo();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                }, 0, 60 * 2000);
            } else {

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                }, 0, 60 * 2000);
            }
        } else {
            Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
        }

        if (!HdAppConfig.isDataBaseExist()) {
            if (NetUtil.isConnected(this)) {
                HResourceUtil.showDownloadDialog(MainAdultActivity.this);
                HResourceUtil.loadDataBaseRes(this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        Toast.makeText(MainAdultActivity.this, getString(R.string.down_success), Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                    }

                    @Override
                    public void onLoadFailed() {
                        Toast.makeText(MainAdultActivity.this, getString(R.string.down_fail), Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                    }
                });
            } else {
                Toast.makeText(this, "请在有网的情况下下载数据库资源", Toast.LENGTH_SHORT).show();
            }
        } else {

            ivSet.setOnClickListener(v ->
                    openActivity(MainAdultActivity.this, SetAdultActivity.class));
            ivSearch.setOnClickListener(v ->
                    openActivity(MainAdultActivity.this, SearchAdultActivity.class));
            tvAr.setOnClickListener(view -> {
                openActivity(MainAdultActivity.this, GetDataActivity.class);
            });


            rgMenuMain.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.rbGuide:
                        if (aGuideEntryFrg == null)
                            aGuideEntryFrg = AGuideEntryFrg.newInstance();
                        FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                                aGuideEntryFrg, true);
                        break;
                    case R.id.rbRaiders:
                        if (aRaidersEntryFrg == null)
                            aRaidersEntryFrg = ARaidersEntryFrg.newInstance();
                        FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                                aRaidersEntryFrg, true);
                        break;
                }
            });
            rbGuide.setChecked(true);
        }


    }

    /**
     * 监听点击返回
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    long mExitTime;

    /**
     * 双击返回退出
     */
    private void exitBy2Click() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            CommonUtil.showToast(MainAdultActivity.this, getString(R.string.click_again));
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        hProgressDialog = new HProgressDialog(this);
        if (HdAppConfig.getLanguage().equals("ENGLISH")) {
            rbGuide.setText("Visit");
            rbRaiders.setText("Tips");
        } else {
            rbGuide.setText(getString(R.string.visit));
            rbRaiders.setText(getString(R.string.railder));
        }

        StatusBarCompat.translucentStatusBar(MainAdultActivity.this);

        ivSet.setOnClickListener(v ->
                openActivity(MainAdultActivity.this, SetAdultActivity.class));
        ivSearch.setOnClickListener(v ->
                openActivity(MainAdultActivity.this, SearchAdultActivity.class));
        tvAr.setOnClickListener(v -> {
            openActivity(MainAdultActivity.this, GetDataActivity.class);
        });
        rgMenuMain.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbGuide:
                    if (aGuideEntryFrg == null)
                        aGuideEntryFrg = AGuideEntryFrg.newInstance();
                    FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                            aGuideEntryFrg, true);
                    break;
                case R.id.rbRaiders:
                    if (aRaidersEntryFrg == null)
                        aRaidersEntryFrg = ARaidersEntryFrg.newInstance();
                    FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                            aRaidersEntryFrg, true);
                    break;
            }
        });
        if (aGuideEntryFrg == null) {

            aGuideEntryFrg = AGuideEntryFrg.newInstance();
        }
        FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                aGuideEntryFrg, true);
        rbGuide.setChecked(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(String msg) {

        Cursor cursor = HResDdUtil.getInstance().QueryAR(msg);
        RequestApi.getInstance().ARPlayCount(msg, MainAdultActivity.this);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ArEnty arEnty = ArEnty.getAREntyInfo(cursor);
                if (arEnty.getType() == 2) {
                    Intent intent = new Intent(MainAdultActivity.this, TextWeb.class);
                    intent.putExtra("ar", arEnty);
                    startActivity(intent);
                } else if (arEnty.getType() == 1) {
                    Intent intent = new Intent(MainAdultActivity.this, VedioAc.class);
                    intent.putExtra("ar", arEnty);
                    startActivity(intent);

                }
            }
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
        stopService(serviceIntent);
    }
}
