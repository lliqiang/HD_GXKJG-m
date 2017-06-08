package com.hengda.smart.play;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.ArEnty;
import com.hengda.smart.gxkjg.entity.Exhibit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class VedioAc extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, MediaPlayer.OnPreparedListener, SeekBar.OnSeekBarChangeListener {

    private SurfaceView surfaceView;
    private CheckBox checkPlay;
    private TextView txtStartTime, txtTotleTime;
    private SeekBar seekBar;
    private Exhibit exhibit;
    private ArEnty arEnty;
    private MediaPlayer mediaPlayer;
    private List<ArEnty> arEntyList;
    //纪录当前播放到进度
    private int postion = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //每隔一秒获取播放时间并且更新
                txtStartTime.setText(CommonUtil.formatTime(mediaPlayer.getCurrentPosition()));
                //更新进度条的进度
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                postion = mediaPlayer.getCurrentPosition();
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };
    private String moviePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarCompat.translucentStatusBar(VedioAc.this);
        initView();
        exhibit = (Exhibit) getIntent().getSerializableExtra("exhibit");
        arEnty = (ArEnty) getIntent().getSerializableExtra("ar");

        setLisenter();
        if (arEnty != null) {
            if (!HResourceUtil.isVedioExist(arEnty.getARID())) {
                HResourceUtil.showDownloadDialog(VedioAc.this);
                HResourceUtil.loadVedio(VedioAc.this, new ILoadListener() {
                    @Override
                    public void onLoadSucceed() {
                        Toast.makeText(VedioAc.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                        HResourceUtil.hideDownloadDialog();
                        checkPlay.setChecked(true);
                    }

                    @Override
                    public void onLoadFailed() {
                        HResourceUtil.hideDownloadDialog();
                        Toast.makeText(VedioAc.this, R.string.down_fail, Toast.LENGTH_SHORT).show();
                    }
                }, arEnty.getARID());
            } else {
                checkPlay.setChecked(true);
            }
        }
        if (exhibit != null) {
            checkPlay.setChecked(true);
        }


        // TODO: 2016/12/19 视频播放次数上传
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });
    }

    private void setLisenter() {
        checkPlay.setOnCheckedChangeListener(this);

        seekBar.setOnSeekBarChangeListener(this);


    }

    private void initView() {
        surfaceView = (SurfaceView) findViewById(R.id.surface_show);
        checkPlay = (CheckBox) findViewById(R.id.checkbox_play);

        txtStartTime = (TextView) findViewById(R.id.txt_starttime);
        txtTotleTime = (TextView) findViewById(R.id.txt_totletile);
        seekBar = (SeekBar) findViewById(R.id.seekbar_progress);
        mediaPlayer = new MediaPlayer();
        arEntyList = new ArrayList<>();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.checkbox_play:
                //获得当前播放的位置
                if (postion == 0) {
//                    //初始第一次播放


                    //每隔一秒更新设置时间
                    handler.sendEmptyMessageDelayed(1, 1000);
                    play();
                } else if (mediaPlayer.isPlaying()) {
                    //视屏暂停播放
                    mediaPlayer.pause();
                    postion = mediaPlayer.getCurrentPosition();
                } else if (isChecked && postion > 0) {
                    mediaPlayer.seekTo(postion);
                    mediaPlayer.start();
                }
                break;

        }


    }

    //视频播放方法
    private void play() {
        if (exhibit != null) {
            moviePath = "file:///" + HdAppConfig.getDefaultFileDir() + "AR_Vedio/" + exhibit.getFileNo() + "/" + exhibit.getFileNo() + ".mp4";
        } else if (arEnty != null) {
            moviePath = "file:///" + HdAppConfig.getDefaultFileDir() + "AR_Vedio/" + arEnty.getARID() + "/" + arEnty.getARID() + ".mp4";
        }
        Uri parse = Uri.parse(moviePath);

        try {

            mediaPlayer.setDataSource(this, parse);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
//        mp.reset();
        mp.start();
        //设置总时间
        mp.setDisplay(surfaceView.getHolder());
        txtTotleTime.setText(CommonUtil.formatTime(mp.getDuration()));
        seekBar.setMax(mp.getDuration());

    }

    //seekbar的监听方法
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //拖动完成之后设置当前播放的进度
        mediaPlayer.seekTo(seekBar.getProgress());
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}
