package com.hengda.smart.gxkjg.ui.child;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.FragmentUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.guangxitech.GetDataActivity;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.ArEnty;
import com.hengda.smart.gxkjg.ui.common.AndroidBug54971Workaround;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.web.TextWeb;
import com.hengda.smart.play.VedioAc;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainChildActivity extends BaseActivity {

    @Bind(R.id.ivSet)
    ImageView ivSet;
    @Bind(R.id.ivSearch)
    ImageView ivSearch;

    @Bind(R.id.rgMenuMain)
    RadioGroup rgMenuMain;
    @Bind(R.id.rbGuide)
    RadioButton rbGuide;
    @Bind(R.id.tvAr)
    TextView tvAr;
    @Bind(R.id.rbRaiders)
    RadioButton rbRaiders;

    @Bind(R.id.ivPpJump)
    ImageView ivPpJump;

    CGuideEntryFrg cGuideEntryFrg;
    CRaidersEntryFrg cRaidersEntryFrg;

    AnimationDrawable adAll;
    AnimationDrawable adJump;

    long mStartJumpTime;
    long mExitTime;
    final int JUMP_ANIM_DURATION = 640;
    @Bind(R.id.flMainContainer)
    FrameLayout flMainContainer;
    @Bind(R.id.linear_main_child)
    LinearLayout linearMainChild;
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
        setContentView(R.layout.activity_child_main);
        ButterKnife.bind(this);
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
            }
        } else {
            Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
        }

        StatusBarCompat.translucentStatusBar(MainChildActivity.this);
        EventBus.getDefault().register(this);
        if (!HdAppConfig.isDataBaseExist()) {
            if (NetUtil.isConnected(this)) {
                HResourceUtil.showDownloadDialog(MainChildActivity.this);
                HResourceUtil.loadDataBaseRes(this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        Toast.makeText(MainChildActivity.this, getString(R.string.down_success), Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                    }

                    @Override
                    public void onLoadFailed() {
                        Toast.makeText(MainChildActivity.this, getString(R.string.down_fail), Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                    }
                });
            } else {
                Toast.makeText(this, "请在有网的情况下下载数据库资源", Toast.LENGTH_SHORT).show();
            }
        } else {
            ivSet.setOnClickListener(v ->
                    openActivity(MainChildActivity.this, SetChildActivity.class));
            ivSearch.setOnClickListener(v ->
                    openActivity(MainChildActivity.this, SearchChildActivity.class));

            ivPpJump.setOnClickListener(v -> {
                if (System.currentTimeMillis() - mStartJumpTime > JUMP_ANIM_DURATION) {
                    startAnimJump();
                }
            });
            tvAr.setTypeface(HdApplication.typefaceGxc);
            rbGuide.setTypeface(HdApplication.typefaceGxc);
            rbRaiders.setTypeface(HdApplication.typefaceGxc);
            tvAr.setOnClickListener(v -> {
                //TODO 跳转至AR界面
                openActivity(MainChildActivity.this, GetDataActivity.class);
            });
            rgMenuMain.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.rbGuide:
                        if (cGuideEntryFrg == null)
                            cGuideEntryFrg = CGuideEntryFrg.newInstance();
                        FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                                cGuideEntryFrg, true);
                        break;
                    case R.id.rbRaiders:
                        if (cRaidersEntryFrg == null)
                            cRaidersEntryFrg = CRaidersEntryFrg.newInstance();
                        FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                                cRaidersEntryFrg, true);
                        break;
                }
            });
            rbGuide.setChecked(true);
            startAnimAll();
        }
    }

    /**
     * 开发跳跃动作
     */
    private void startAnimJump() {
        ivPpJump.setImageResource(R.drawable.anim_pp_jump);
        adJump = (AnimationDrawable) ivPpJump.getDrawable();
        adJump.start();
        mStartJumpTime = System.currentTimeMillis();
        handler.postDelayed(() -> startAnimAll(), JUMP_ANIM_DURATION);
    }

    /**
     * 开始全套动画
     */
    private void startAnimAll() {
        ivPpJump.setImageResource(R.drawable.anim_pp_all);
        adAll = (AnimationDrawable) ivPpJump.getDrawable();
        adAll.start();
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

    /**
     * 双击返回退出
     */
    private void exitBy2Click() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            CommonUtil.showToast(MainChildActivity.this, getString(R.string.click_again));
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        ButterKnife.bind(this);
//        HdApplication.clearActivity();
        if (cGuideEntryFrg != null) {
            FragmentUtil.removeFragment(getFragmentManager(), cGuideEntryFrg);
        }
        StatusBarCompat.translucentStatusBar(MainChildActivity.this);
        if (HdAppConfig.getLanguage().equals("ENGLISH")) {
            rbGuide.setText("Visit");
            rbRaiders.setText("Tips");
        } else {
            rbGuide.setText(getString(R.string.visit));
            rbRaiders.setText(getString(R.string.railder));
        }
        ivSet.setOnClickListener(v ->
                openActivity(MainChildActivity.this, SetChildActivity.class));
        ivSearch.setOnClickListener(v ->
                openActivity(MainChildActivity.this, SearchChildActivity.class));

        ivPpJump.setOnClickListener(v -> {
            if (System.currentTimeMillis() - mStartJumpTime > JUMP_ANIM_DURATION) {
                startAnimJump();
            }
        });
        tvAr.setTypeface(HdApplication.typefaceGxc);
        rbGuide.setTypeface(HdApplication.typefaceGxc);
        rbRaiders.setTypeface(HdApplication.typefaceGxc);
        tvAr.setOnClickListener(v -> {
            //TODO 跳转至AR界面
            openActivity(MainChildActivity.this, GetDataActivity.class);
        });

        rgMenuMain.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rbGuide:
                    if (cGuideEntryFrg == null) {

                        cGuideEntryFrg = CGuideEntryFrg.newInstance();
                    }
                    FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                            cGuideEntryFrg, true);
                    break;
                case R.id.rbRaiders:
                    if (cRaidersEntryFrg == null)
                        cRaidersEntryFrg = CRaidersEntryFrg.newInstance();
                    FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                            cRaidersEntryFrg, true);
                    break;
            }
        });
        cGuideEntryFrg = CGuideEntryFrg.newInstance();
        FragmentUtil.replaceFragment(getFragmentManager(), R.id.flMainContainer,
                cGuideEntryFrg, true);
        rbGuide.setChecked(true);
        startAnimAll();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(String msg) {
        Cursor cursor = HResDdUtil.getInstance().QueryAR(msg);
        RequestApi.getInstance().ARPlayCount(msg, MainChildActivity.this);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                ArEnty arEnty = ArEnty.getAREntyInfo(cursor);
                if (arEnty.getType() == 2) {
                    Intent intent = new Intent(MainChildActivity.this, TextWeb.class);
                    intent.putExtra("ar", arEnty);
                    startActivity(intent);
                } else if (arEnty.getType() == 1) {
                    Intent intent = new Intent(MainChildActivity.this, VedioAc.class);
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
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
