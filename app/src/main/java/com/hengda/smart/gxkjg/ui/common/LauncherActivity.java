package com.hengda.smart.gxkjg.ui.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.HVideoView;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LauncherActivity extends BaseActivity {

    @Bind(R.id.videoView)
    HVideoView videoView;
    private int currentPost;
    private Set h=new HashSet();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ButterKnife.bind(this);
        initJpush();
        StatusBarCompat.translucentStatusBar(this);
        HdAppConfig.setAutoPlay(true);
        CommonUtil.configLanguage(LauncherActivity.this, HdAppConfig.getLanguage());
        initVideoView();
        initCrash();
    }

    private void initCrash() {
        Context context = getApplicationContext();
// 获取当前包名
        String packageName = context.getPackageName();
// 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
// 初始化Bugly
        CrashReport.initCrashReport(context, "c566e26eae", false, strategy);
    }

    private void initVideoView() {
        videoView.setVideoURI(Uri.parse("android.resource://" + LauncherActivity.this
                .getPackageName() + "/" + R.raw.start_video));
        videoView.setOnCompletionListener(mp -> enter());
    }

    private void initJpush() {
        h.add("phone");
        JPushInterface.init(this);
        JPushInterface.setAliasAndTags(this, HdAppConfig.getDeviceNo(), h, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(currentPost);
        videoView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentPost = videoView.getCurrentPosition();
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView = null;
        ButterKnife.unbind(this);
    }

    private void enter() {
        openActivity(LauncherActivity.this, SleLangActivity.class);
        finish();
    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
