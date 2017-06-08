package com.hengda.smart.play;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.MessageIcon;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.Content;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.Exhibition;
import com.hengda.smart.gxkjg.entity.Pstatus;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import me.shaohui.bottomdialog.BottomDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class Play extends BaseActivity {

    @Bind(R.id.title_play)
    TextView titlePlay;
    @Bind(R.id.video_play)
    ImageView videoPlay;
    @Bind(R.id.webview_play)
    WebView webviewPlay;
    @Bind(R.id.play_time)
    TextView playTime;
    @Bind(R.id.seekbar)
    SeekBar seekbar;
    @Bind(R.id.total_time)
    TextView totalTime;
    @Bind(R.id.toback_play)
    ImageView tobackPlay;
    @Bind(R.id.input_play)
    EditText inputPlay;
    @Bind(R.id.message_play)
    MessageIcon messagePlay;
    @Bind(R.id.heart_play)
    ImageView heartPlay;

    @Bind(R.id.share_play)
    ImageView sharePlay;
    @Bind(R.id.toggle)
    ImageView toggle;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.activity_play)
    LinearLayout activityPlay;
    @Bind(R.id.name_aplayer)
    TextView nameAplayer;
    @Bind(R.id.unitname_aplayer)
    TextView unitnameAplayer;
    @Bind(R.id.txtsize)
    ImageView txtsize;

    private boolean isPause;
    private MediaPlayer mediaPlayer;
    private boolean isRestart = false;
    private Exhibit exhibit;
    private EditText input;
    private String path1;
    private boolean isHeart = true;
    private List<Content> contentList;
    private List<Exhibition> exhibitionList = new ArrayList<>();
    private List<String> stringList = new ArrayList<>();
    private int num = 0;

    @Override
    protected void onUserLeaveHint() {

    }

    Button send;
    private BottomDialog b;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    int current = mediaPlayer.getCurrentPosition();
                    seekbar.setProgress(current);
                    current /= 1000;
                    int minute = current / 60;
                    int second = current % 60;
                    playTime.setText(String.format("%02d:%02d", minute, second));
                    //每隔500ms通过handler回传一次数据
                    sendEmptyMessageDelayed(1, 500);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_play);
        ShareSDK.initSDK(this, "1de35fdefdf00");
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(Play.this);
        if (HdAppConfig.getLanguage().equals("ENGLISH")) {
            inputPlay.setHint("Make Comments...");
            titlePlay.setText("What is this product about?");
        } else {
            inputPlay.setHint("写评论...");
            titlePlay.setText("请问这个产品讲的是什么");
        }

        exhibit = (Exhibit) getIntent().getSerializableExtra("exhibit");
        ;
        //展品浏览次数上传
        String exhibit_id = exhibit.getFileNo();
        String exhibit_name = exhibit.getName();
        if (NetUtil.isConnected(Play.this)) {

            RequestApi.getInstance().getCount(exhibit.getFileNo());
        }
        if (exhibit.getType_auto() == 3) {
            videoPlay.setVisibility(View.VISIBLE);
//            doPause();

        } else {
            videoPlay.setVisibility(View.INVISIBLE);
        }
        if (NetUtil.isConnected(Play.this)) {

            RequestApi.getInstance().lookAppreciate(new Subscriber<Pstatus>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Pstatus pstatus) {
                    int status = pstatus.getPstatus();
                    HdAppConfig.setAHeart(status);
                    if (HdAppConfig.getAHeart() == 1) {
                        heartPlay.setImageResource(R.mipmap.click_zan);
                    } else {
                        heartPlay.setImageResource(R.mipmap.click_yes);
                    }
                }
            }, HdAppConfig.getDeviceNo(), exhibit_id);
        }
        Cursor cursor = HResDdUtil.getInstance().QueryName(exhibit.getFloor(), exhibit.getUnitNo());
        if (cursor != null) {
            while (cursor.moveToNext()) {

                unitnameAplayer.setText(cursor.getString(0));
            }
            cursor.close();
        }
        nameAplayer.setText(exhibit_name);

        contentList = new ArrayList<>();

        if (NetUtil.isConnected(Play.this)) {

            RequestApi.getInstance().lookComment(new Subscriber<List<Content>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Content> contents) {
//                contentList = contents;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messagePlay.setNum(contents.size());
                        }
                    });

                }
            }, exhibit.getFileNo());


        }

        txtsize.setOnClickListener(view -> {
            switch (num) {
                case 0:
                    webviewPlay.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                    break;
                case 1:
                    webviewPlay.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    break;
                case 2:
                    webviewPlay.getSettings().setTextSize(WebSettings.TextSize.LARGER);
                    break;


            }
            num++;
            if (num == 3) {
                num = 0;
            }
        });
        tobackPlay.setOnClickListener(v -> finish());
        videoPlay.setOnClickListener(v -> {
            doPause();

            if (!HResourceUtil.isVedioExist(exhibit.getFileNo())) {
                HResourceUtil.showDownloadDialog(Play.this);
                if (NetUtil.isConnected(Play.this)) {
                    HResourceUtil.loadVedio(Play.this, new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            RequestApi.getInstance().playCount(exhibit_id, "video");
                            HResourceUtil.hideDownloadDialog();
                            CommonUtil.showToast(Play.this, getString(R.string.down_success));
                            Intent intent = new Intent(Play.this, VedioAc.class);
                            intent.putExtra("exhibit", exhibit);
                            startActivity(intent);
                        }

                        @Override
                        public void onLoadFailed() {
                            HResourceUtil.hideDownloadDialog();
                            CommonUtil.showToast(Play.this, getString(R.string.down_fail));
                        }
                    }, exhibit.getFileNo());
                } else {
                    Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
                }


            } else {
                RequestApi.getInstance().playCount(exhibit_id, "video");
                Intent intent = new Intent(Play.this, VedioAc.class);
                intent.putExtra("exhibit", exhibit);
                startActivity(intent);
            }


        });
        messagePlay.setOnClickListener(v -> {
            Intent intent = new Intent(Play.this, CommentAc.class);
            intent.putExtra("exhibit", exhibit);
            startActivity(intent);
        });
        heartPlay.setOnClickListener(v -> {
            if (NetUtil.isConnected(Play.this)) {

                RequestApi.getInstance().setAppreciate(HdAppConfig.getDeviceNo(), exhibit_id, Play.this, heartPlay);
            } else {
                Toast.makeText(this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
            }

        });

        //TODO 集成ShareSDk
        RxView.clicks(sharePlay).throttleFirst(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showShare(exhibit);
                if (NetUtil.isConnected(Play.this)) {
                    RequestApi.getInstance().getShareCount(exhibit_id);
                } else {
                    Toast.makeText(Play.this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
                }
            }
        });
//        sharePlay.setOnClickListener(v -> {
//
//            showShare(exhibit);
//            //// TODO: 2016/12/19 做分享次数上传
//
//
//        });


        toggle.setOnClickListener(v -> {
            if (isPause) {
                doPlay();
            } else {
                doPause();
            }
            isPause = !isPause;
        });
        inputPlay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(b, true);
                    inputPlay.clearFocus();


                }

            }
        });

        initUI();
        loadExhibit(exhibit, handler);
    }


    private void showDialog(BottomDialog b, boolean flag) {
        b = BottomDialog.create(getSupportFragmentManager());


        BottomDialog finalB = b;
        b.setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                v.findViewById(R.id.comment_dialog);
                input = (EditText) v.findViewById(R.id.comment_dialog);

                send = (Button) v.findViewById(R.id.send_dialog);


                send.setOnClickListener(v1 -> {
                    if (finalB.getShowsDialog()) {
                        // TODO: 2016/12/19 评论
                        if (!TextUtils.isEmpty(input.getText().toString())) {

                            RequestApi.getInstance().makeComment(" ", HdAppConfig.getDeviceNo(), input.getText().toString(), exhibit.getFileNo(), Play.this);
                            finalB.dismiss();
                        } else {
                            Toast.makeText(Play.this, getString(R.string.no_empty), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        })
                .setLayoutRes(R.layout.dialog_normal)
                .setDimAmount(0.1f)
                .setTag("BottomDialog");
        if (flag) {
            b.show();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        toggle.setImageResource(R.mipmap.play_icon);


    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onStop() {
        super.onStop();
        toggle.setImageResource(R.mipmap.play_icon);
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //添加seekBar的监听事件
    private void addSeekbarChangeListener(SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initUI() {
        seekbar.setProgressDrawable(getResources().getDrawable(R.drawable
                .seek_bar_progress_drawable));
        addSeekbarChangeListener(seekbar);
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mp == mediaPlayer) {
                    playPrepare();
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp == mediaPlayer) {
                    Play.this.finish();
                }
            }
        });
    }

    //开始播并且设置暂停图片
    public void doPlay() {
        mediaPlayer.start();
        toggle.setImageResource(R.mipmap.pause_m);
    }

    //暂停播放并设置播放图片
    public void doPause() {
        mediaPlayer.pause();
        toggle.setImageResource(R.mipmap.play_icon);
    }

    public void playPrepare() {
        //设置播放时长
        int duration = mediaPlayer.getDuration();
        seekbar.setMax(duration);
        int minute = duration / 1000 / 60;
        int second = (duration / 1000) % 60;
        //将分秒格式化
        totalTime.setText(String.format("%02d:%02d", minute, second));
        playTime.setText(String.format("%02d:%02d", 0, 0));
    }

    private void showShare(Exhibit exhibit) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("广西科技馆");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        // text是分享文本，所有平台都需要这个字段
        oks.setText(exhibit.getName());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(path1);//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://www.gxkjg.com");
// 启动分享GUI
        oks.show(this);
    }

    public void loadExhibit(Exhibit exhibit, Handler handler) {
        //获取当前展品的信息后，设置展品的title、图片和对应的WebView，播放视频

        String url = HdAppConfig.getDefaultFileDir() + "/" + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + ".html";
//
        WebSettings settings = webviewPlay.getSettings();


        settings.setSupportZoom(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
//textWeb.setInitialScale();


//        //适应屏幕
//        settings.setUseWideViewPort(true);
//        settings.setSupportZoom(true);
////      settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setLoadWithOverviewMode(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setJavaScriptEnabled(true);
//        settings.setAppCacheEnabled(true);
//        settings.setDatabaseEnabled(true);

        webviewPlay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });


//        settings.setLoadWithOverviewMode(true);
//        settings.setJavaScriptCanOpenWindowsAutomatically(true);
//        settings.setJavaScriptEnabled(true);
//        settings.setBuiltInZoomControls(true);
//        settings.setSupportZoom(true);
//
//        settings.setBuiltInZoomControls(true);
//        settings.setUseWideViewPort(true);
        webviewPlay.setInitialScale(1);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webviewPlay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        webviewPlay.requestFocus();
        // 设置背景色
//        webviewPlay.setBackgroundColor(Color.TRANSPARENT);
        webviewPlay.loadUrl("file:///" + url);
        String path = HdAppConfig.getDefaultFileDir() + "/" + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + File.separator + exhibit.getFileNo() + ".mp3";
        path1 = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + ".png";
        Glide.with(Play.this)
                .load(path1)
                .into(imageView2);

        mediaPlayer.reset();
        try {
            //设置播放资源
            mediaPlayer.setDataSource(path);
            //准备播放
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doPlay();
        handler.sendEmptyMessage(1);
    }


}
