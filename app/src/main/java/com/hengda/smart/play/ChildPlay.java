package com.hengda.smart.play;

import android.Manifest;
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
import com.hengda.smart.common.permission.Acp;
import com.hengda.smart.common.permission.AcpListener;
import com.hengda.smart.common.permission.AcpOptions;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.MessageIcon;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.Content;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.Pstatus;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.jakewharton.rxbinding.view.RxView;

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

public class ChildPlay extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.toback_childplay)
    ImageView tobackPlay;
    @Bind(R.id.input_play)
    EditText inputPlay;
//
//    @Bind(R.id.heart_play)
//    ImageView heartPlay;

    @Bind(R.id.share_play_c)
    ImageView sharePlay;
    @Bind(R.id.toggle)
    ImageView toggle;
    @Bind(R.id.imageView2)
    ImageView imageView2;
    @Bind(R.id.zhan)
    ImageView zhan;
    @Bind(R.id.name_cplayer)
    TextView nameCplayer;
    @Bind(R.id.unity_cplayer)
    TextView unityCplayer;
    @Bind(R.id.activity_play)
    LinearLayout activityPlay;
    @Bind(R.id.C_message_play)
    MessageIcon messagePlay;
    @Bind(R.id.C_heart_play)
    ImageView heartPlay;
    @Bind(R.id.txtsize)
    ImageView txtsize;

    private boolean isPause;
    private MediaPlayer mediaPlayer;
    private boolean isRestart = false;
    private Exhibit exhibit;
    private EditText input;
    private String path1;
    private boolean isHeart = true;
    Button send;
    private BottomDialog b;
    private List<Content> contentList;
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
    private String url;
    private int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setContentView(R.layout.child_play);
//
        ShareSDK.initSDK(this, "1de35fdefdf00");
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(ChildPlay.this);
        titlePlay.setTypeface(HdApplication.typefaceGxc);
        contentList = new ArrayList<>();

        seekbar.setThumb(getResources().getDrawable(R.mipmap.dot_child));
        exhibit = (Exhibit) getIntent().getSerializableExtra("exhibit");
        String exhibit_id = exhibit.getFileNo();
        String exhibit_name = exhibit.getName();
        if (NetUtil.isConnected(this)) {

            RequestApi.getInstance().getCount(exhibit.getFileNo());
        }
        if (exhibit.getType_auto() == 3) {
            videoPlay.setVisibility(View.VISIBLE);

        } else {
            videoPlay.setVisibility(View.INVISIBLE);
        }
        if (NetUtil.isConnected(ChildPlay.this)) {

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
                    HdAppConfig.setCHeart(status);
                    if (status == 1) {
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

                unityCplayer.setText(cursor.getString(0));
            }
            cursor.close();
        }
        nameCplayer.setTypeface(HdApplication.typefaceGxc);
        unityCplayer.setTypeface(HdApplication.typefaceGxc);

        nameCplayer.setText(exhibit_name);
        if (NetUtil.isConnected(ChildPlay.this)) {

            RequestApi.getInstance().lookComment(new Subscriber<List<Content>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(List<Content> contents) {
                    contentList = contents;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            messagePlay.setNum(contents.size());

                        }
                    });


                }
            }, exhibit_id);
        }


        tobackPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Acp.getInstance(ChildPlay.this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                videoPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPause();

                        if (!HResourceUtil.isVedioExist(exhibit.getFileNo())) {
                            if (NetUtil.isConnected(ChildPlay.this)) {
                                HResourceUtil.showDownloadDialog(ChildPlay.this);
                                HResourceUtil.loadVedio(ChildPlay.this, new ILoadListener() {
                                    @Override
                                    public void onLoadSucceed() {
                                        RequestApi.getInstance().playCount(exhibit_id, "video");
                                        HResourceUtil.hideDownloadDialog();
                                        CommonUtil.showToast(ChildPlay.this, getString(R.string.down_success));
                                        Intent intent = new Intent(ChildPlay.this, VedioAc.class);
                                        intent.putExtra("exhibit", exhibit);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onLoadFailed() {
                                        HResourceUtil.hideDownloadDialog();
                                        CommonUtil.showToast(ChildPlay.this, getString(R.string.down_success));
                                    }
                                }, exhibit.getFileNo());
                            } else {
                                Toast.makeText(ChildPlay.this, getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            RequestApi.getInstance().playCount(exhibit_id, "video");
                            Intent intent = new Intent(ChildPlay.this, VedioAc.class);
                            intent.putExtra("exhibit", exhibit);
                            startActivity(intent);
                        }


                    }

                });
            }

            @Override
            public void onDenied(List<String> permissions) {
                CommonUtil.showToast(ChildPlay.this, (permissions.toString() + "权限拒绝"));
            }
        });


        messagePlay.setOnClickListener(this);

        heartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetUtil.isConnected(ChildPlay.this)) {

                    RequestApi.getInstance().setAppreciate(HdAppConfig.getDeviceNo(), exhibit_id, ChildPlay.this, heartPlay);
                }

                if (HdAppConfig.getCHeart() == 1) {
                    heartPlay.setImageResource(R.mipmap.click_yes);


                } else {
                    heartPlay.setImageResource(R.mipmap.click_zan);
                }
            }
        });
        RxView.clicks(sharePlay).throttleFirst(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread()).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (NetUtil.isConnected(ChildPlay.this)) {

                    showShare(exhibit);
                    RequestApi.getInstance().getShareCount(exhibit_id);
                }
            }
        });
        toggle.setOnClickListener(v -> {
            if (isPause) {
                doPlay();
            } else {
                doPause();
            }
            isPause = !isPause;
        });



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
            if (num==3){
                num=0;
            }
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
                        if (!TextUtils.isEmpty(input.getText().toString())) {
                            if (NetUtil.isConnected(ChildPlay.this)) {

                                RequestApi.getInstance().makeComment(" ", HdAppConfig.getDeviceNo(), input.getText().toString(), exhibit.getFileNo(), ChildPlay.this);
                                finalB.dismiss();
                            }
                        } else {
                            Toast.makeText(ChildPlay.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();

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
        isRestart = true;
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onStop() {
        super.onStop();
        toggle.setImageResource(R.mipmap.childpaly_play);
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
        addSeekbarChangeListener(seekbar);
        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playPrepare();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ChildPlay.this.finish();
            }
        });

    }

    //开始播并且设置暂停图片
    public void doPlay() {
        mediaPlayer.start();
        toggle.setImageResource(R.mipmap.childplay_pause);
    }

    //暂停播放并设置播放图片
    public void doPause() {
        mediaPlayer.pause();
        toggle.setImageResource(R.mipmap.childpaly_play);
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
        oks.setTitleUrl(url);
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

        url = HdAppConfig.getDefaultFileDir() + "/" + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + ".html";
        webviewPlay.getSettings().setJavaScriptEnabled(true);
        webviewPlay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webviewPlay.requestFocus();
        webviewPlay.loadUrl("file:///" + url);
        String path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + ".mp3";

        path1 = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + ".png";
        Glide.with(ChildPlay.this)
                .load(path1)
                .placeholder(R.mipmap.xiangqing)
                .error(R.mipmap.xiangqing)
                .into(imageView2);

//        mediaPlayer.reset();
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.C_message_play:
                Intent intent = new Intent(ChildPlay.this, CommentAc.class);
                intent.putExtra("exhibit", exhibit);
                startActivity(intent);
                break;
        }
    }
}
