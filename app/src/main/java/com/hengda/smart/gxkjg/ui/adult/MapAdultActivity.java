package com.hengda.smart.gxkjg.ui.adult;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.hengda.frame.easyqr.QrActivity;
import com.hengda.frame.easyqr.QrActivity;
import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.autono.NumService;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.permission.Acp;
import com.hengda.smart.common.permission.AcpListener;
import com.hengda.smart.common.permission.AcpOptions;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.FragmentUtil;
import com.hengda.smart.common.util.KeyboardUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.ListDialog;
import com.hengda.smart.common.widget.TipDialog;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.AutoNum;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.Model;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.smart.gxkjg.ui.common.HtmlActivity;
import com.hengda.smart.play.ChildPlay;
import com.hengda.smart.play.Play;
import com.hengda.zwf.autonolibrary.BleNumService;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.shaohui.bottomdialog.BottomDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MapAdultActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ivBack)
    ImageView ivBack;


    @Bind(R.id.edtSearch)
    EditText edtSearch;
    int floor;
    @Bind(R.id.flMapContainer)
    FrameLayout flMapContainer;
    @Bind(R.id.ivSearch)
    ImageView ivSearch;
    @Bind(R.id.ivScan)
    ImageView ivScan;

    @Bind(R.id.video_childmap)
    ImageView videoChildmap;
    @Bind(R.id.Img_child4F)
    ImageView ImgChild4F;
    @Bind(R.id.Img_child3F)
    ImageView ImgChild3F;
    @Bind(R.id.Img_child2F)
    ImageView ImgChild2F;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    private boolean flag;
    private boolean videoFlag;
    public AMapFrg aMapFrg;
    private Intent serviceIntent;
    private AMapFrgAR aMapFrgAR;
    private BottomDialog bottomDialog;
    private AMapFrgVedio aMapFrgVedio;
    private FragmentTransaction ft;
    private Timer timer;
    private int lastNum;
    private int lastFloorNum;
    private List<Exhibit> allExhibitList = new ArrayList<>();
    private List<Exhibit> exhibitList = new ArrayList<>();
    private ListDialog listDialog;
    private TipDialog tipDialog;
    private String result;
    private Exhibit exhibit;
    private String lastFileNO;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_map);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        serviceIntent = new Intent(this, BleNoService.class);
        ImgChild2F.setOnClickListener(this);
        ImgChild3F.setOnClickListener(this);
        ImgChild4F.setOnClickListener(this);
        timer = new Timer();
        listDialog = new ListDialog(MapAdultActivity.this);
        tipDialog = new TipDialog(MapAdultActivity.this);
        tipDialog.setCancelable(false);
        StatusBarCompat.translucentStatusBar(MapAdultActivity.this);

        String map_name = getIntent().getStringExtra("map_name");
        if (!TextUtils.isEmpty(map_name)) {
            if (map_name.equals("2")) {
                floor = 2;


            } else if (map_name.equals("3")) {
                floor = 3;
            } else {
                floor = 4;
            }


        } else {
            floor = getIntent().getIntExtra("FLOOR", 2);
        }
        if (!HdAppConfig.isMapResExist()) {
            // TODO: 2016/12/24 下载地图资源
            HResourceUtil.showDownloadDialog(MapAdultActivity.this);
            HResourceUtil.loadMapRes(MapAdultActivity.this, new ILoadListener() {
                @Override
                public void onLoadSucceed() {
                    HResourceUtil.hideDownloadDialog();
                    CommonUtil.showToast(MapAdultActivity.this, getString(R.string.down_success));

                }

                @Override
                public void onLoadFailed() {
                    HResourceUtil.hideDownloadDialog();
                    CommonUtil.showToast(MapAdultActivity.this, getString(R.string.down_fail));
                }
            });
        }

        tipDialog.pBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        }).show();
        tvTitle.setTypeface(HdApplication.typefaceGxa);
        switch (floor) {
            case 2:
                tvTitle.setText("2F");
                ImgChild2F.setImageResource(R.drawable.f2_yes);
                ImgChild3F.setImageResource(R.drawable.f3_not);
                ImgChild4F.setImageResource(R.drawable.f4_not);
                break;
            case 3:
                tvTitle.setText("3F");
                ImgChild2F.setImageResource(R.drawable.f2_not);
                ImgChild3F.setImageResource(R.drawable.f3_yes);
                ImgChild4F.setImageResource(R.drawable.f4_not);
                break;
            case 4:
                tvTitle.setText("4F");
                ImgChild2F.setImageResource(R.drawable.f2_not);
                ImgChild3F.setImageResource(R.drawable.f3_not);
                ImgChild4F.setImageResource(R.drawable.f4_yes);
                break;

        }

        videoChildmap.setOnClickListener(v -> {
            if (videoFlag) {
                videoChildmap.setImageResource(R.drawable.video_on);
                if (tvTitle.getText().equals("2F")) {
                    showcMap(2);
                } else if (tvTitle.getText().equals("3F")) {
                    showcMap(3);
                } else if (tvTitle.getText().equals("4F")) {

                    showcMap(4);
                }
            } else {
                videoChildmap.setImageResource(R.drawable.vedio_on);
                if (tvTitle.getText().equals("2F")) {

                    showcMapVedio(2);
                } else if (tvTitle.getText().equals("3F")) {

                    showcMapVedio(3);
                } else if (tvTitle.getText().equals("4F")) {
                    showcMapVedio(4);
                }

            }
            videoFlag = !videoFlag;
        });
        ivBack.setOnClickListener(v -> finish());
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA).build(), new AcpListener() {
                @Override
                public void onGranted() {
                    ivScan.setOnClickListener(v -> {
                        KeyboardUtil.closeKeyboard(edtSearch, MapAdultActivity.this);
                        Intent i = new Intent(MapAdultActivity.this, QrActivity.class);
                        i.putExtra(QrActivity.NUMFILTER, false);
                        startActivityForResult(i, QrActivity.REQUST_QR);
                    });
                }

                @Override
                public void onDenied(List<String> permissions) {

                }
            });
        } else {
            if (cameraIsCanUse()) {
                ivScan.setOnClickListener(v -> {
                    KeyboardUtil.closeKeyboard(edtSearch, MapAdultActivity.this);
                    Intent i = new Intent(MapAdultActivity.this, QrActivity.class);
                    i.putExtra(QrActivity.NUMFILTER, false);
                    startActivityForResult(i, QrActivity.REQUST_QR);
                });
            } else {
                Toast.makeText(this, "请在设置中打开照相机权限", Toast.LENGTH_SHORT).show();
            }
        }


        RxTextView.textChangeEvents(edtSearch)
                .debounce(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TextViewTextChangeEvent>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String changedMessage = textViewTextChangeEvent.text().toString();
                        if (TextUtils.isEmpty(changedMessage)) {
//                            ivPpEyes.setImageResource(R.mipmap.ic_pp_open_eyes);
                        } else {
//                            ivPpEyes.setImageResource(R.mipmap.ic_pp_close_eyes);

                            if (tvTitle.getText().equals("2F")) {
                                LoadExhibitByInput(2);

                            } else if (tvTitle.getText().equals("3F")) {
                                LoadExhibitByInput(3);
                            } else if (tvTitle.getText().equals("4F")) {
                                LoadExhibitByInput(4);
                            }


                            EventBus.getDefault().post(new Model(exhibitList));


                        }
                    }
                });
        showcMap(floor);
    }

    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }

    /*
      * 隔一复收
      * */
    private boolean isReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != lastNum) {
            lastNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    /*
      * 收号切换地图
      * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum num) {
        int temp = num.getBeaconNum();
        Log.i("info", "qieguan-----------" + temp);
        if (isReplay(temp)) {
            allExhibitList.clear();
            loadExhibitByAutoNum(temp);


            for (int i = 0; i < allExhibitList.size(); i++) {


                if (tvTitle.getText().equals("2F")) {
                    //先弹框，切换楼层
                    if (allExhibitList.get(i).getFloor() != 2 && !listDialog.isShowing() && isFloorReplay(allExhibitList.get(i).getFloor())) {
                        int finalI3 = i;
//                        listDialog = new ListDialog(MapAdultActivity.this);
                        if (allExhibitList.get(i).getFloor() == 5) {
                            listDialog.message(getString(R.string.location_where) + "2F" + getString(R.string.swipfloor) + "4F")
                                    .nBtnText(getString(R.string.cancel))
                                    .nBtnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            listDialog.dismiss();
                                        }
                                    })
                                    .pBtnText(getString(R.string.submit)).pBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    listDialog.dismiss();
                                    showcMap(4);
                                    switch (allExhibitList.get(finalI3).getFloor()) {
                                        case 2:
                                            ImgChild2F.setImageResource(R.drawable.f2_yes);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 3:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_yes);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 4:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_yes);
                                            break;
                                    }

                                }
                            }).show();
                        } else {
                            listDialog.message(getString(R.string.location_where) + "2F" + getString(R.string.swipfloor) + allExhibitList.get(i).getFloor() + "F")
                                    .nBtnText(getString(R.string.cancel))
                                    .nBtnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            listDialog.dismiss();
                                        }
                                    })
                                    .pBtnText(getString(R.string.submit)).pBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    listDialog.dismiss();
                                    showcMap(allExhibitList.get(finalI3).getFloor());
                                    switch (allExhibitList.get(finalI3).getFloor()) {
                                        case 2:
                                            ImgChild2F.setImageResource(R.drawable.f2_yes);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 3:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_yes);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 4:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_yes);
                                            break;
                                    }

                                }
                            }).show();
                        }


                    }

                } else if (tvTitle.getText().equals("3F")) {
                    if (allExhibitList.get(0).getFloor() != 3 && !listDialog.isShowing() && isFloorReplay(allExhibitList.get(i).getFloor())) {
                        int finalI2 = i;
                        if (allExhibitList.get(i).getFloor() == 5) {
                            listDialog.message(getString(R.string.location_where) + "3F," + getString(R.string.swipfloor) + "4F?")
                                    .nBtnText(getString(R.string.cancel))
                                    .nBtnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            listDialog.dismiss();
                                        }
                                    })
                                    .pBtnText(getString(R.string.submit)).pBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    listDialog.dismiss();
                                    showcMap(4);
                                    switch (allExhibitList.get(finalI2).getFloor()) {
                                        case 2:
                                            ImgChild2F.setImageResource(R.drawable.f2_yes);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 3:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_yes);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 4:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_yes);
                                            break;
                                    }

                                }
                            }).show();
                        } else {
                            listDialog.message(getString(R.string.location_where) + "3F," + getString(R.string.swipfloor) + allExhibitList.get(i).getFloor() + "F?")
                                    .nBtnText(getString(R.string.cancel))
                                    .nBtnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            listDialog.dismiss();
                                        }
                                    })
                                    .pBtnText(getString(R.string.submit)).pBtnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    listDialog.dismiss();
                                    showcMap(allExhibitList.get(finalI2).getFloor());
                                    switch (allExhibitList.get(finalI2).getFloor()) {
                                        case 2:
                                            ImgChild2F.setImageResource(R.drawable.f2_yes);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 3:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_yes);
                                            ImgChild4F.setImageResource(R.drawable.f4_not);
                                            break;
                                        case 4:
                                            ImgChild2F.setImageResource(R.drawable.f2_not);
                                            ImgChild3F.setImageResource(R.drawable.f3_not);
                                            ImgChild4F.setImageResource(R.drawable.f4_yes);
                                            break;
                                    }

                                }
                            }).show();
                        }


                    }
                } else if (tvTitle.getText().equals("4F")) {
                    if (allExhibitList.get(i).getFloor() != 4 && allExhibitList.get(i).getFloor() != 5 && !listDialog.isShowing() && isFloorReplay(allExhibitList.get(i).getFloor())) {
                        int finalI = i;

//                        listDialog = new ListDialog(MapAdultActivity.this);

                        listDialog.message(getString(R.string.location_where) + "4F," + getString(R.string.swipfloor) + allExhibitList.get(i).getFloor() + "F?")
                                .nBtnText(getString(R.string.cancel))
                                .nBtnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        listDialog.dismiss();
                                    }
                                })
                                .pBtnText(getString(R.string.submit)).pBtnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listDialog.dismiss();
                                showcMap(allExhibitList.get(finalI).getFloor());
                                switch (allExhibitList.get(finalI).getFloor()) {
                                    case 2:
                                        ImgChild2F.setImageResource(R.drawable.f2_yes);
                                        ImgChild3F.setImageResource(R.drawable.f3_not);
                                        ImgChild4F.setImageResource(R.drawable.f4_not);
                                        break;
                                    case 3:
                                        ImgChild2F.setImageResource(R.drawable.f2_not);
                                        ImgChild3F.setImageResource(R.drawable.f3_yes);
                                        ImgChild4F.setImageResource(R.drawable.f4_not);
                                        break;
                                    case 4:
                                        ImgChild2F.setImageResource(R.drawable.f2_not);
                                        ImgChild3F.setImageResource(R.drawable.f3_not);
                                        ImgChild4F.setImageResource(R.drawable.f4_yes);
                                        break;
                                }

                            }
                        }).show();


                    }
                }

            }
        }

    }

    private void loadExhibitByAutoNum(int autoNum) {
        Cursor cursor = HResDdUtil.getInstance().loadExhibitByAutoNo(autoNum);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor);
                allExhibitList.add(exhibit);
            }
        } else {
            cursor.close();
        }
    }


    /**
     * 显示视频地图
     */
    private void showcMapVedio(int floor) {
        ft = getFragmentManager().beginTransaction();
        if (aMapFrg != null) {
            ft.hide(aMapFrg);

        }
        if (aMapFrgVedio != null) {
            FragmentUtil.removeFragment(getFragmentManager(), aMapFrgVedio);
            aMapFrgVedio = null;

        }
        if (aMapFrgVedio == null) {
            aMapFrgVedio = AMapFrgVedio.newInstance(floor);
            FragmentUtil.addFragment(getFragmentManager(), R.id.flMapContainer, aMapFrgVedio, "cMapFrgVedio", false, false);
        }
        ft.show(aMapFrgVedio);
        ft.commitAllowingStateLoss();
    }


    /**
     * 显示正常地图
     */
    private void showcMap(int floor) {
        tvTitle.setText(floor + "F");
        ft = getFragmentManager().beginTransaction();
        if (aMapFrgVedio != null) {

            ft.hide(aMapFrgVedio);

        }
        if (aMapFrg != null) {

            FragmentUtil.removeFragment(getFragmentManager(), aMapFrg);
            aMapFrg = null;

        }

        if (aMapFrg == null) {
            aMapFrg = AMapFrg.newInstance(floor);
            FragmentUtil.addFragment(getFragmentManager(), R.id.flMapContainer, aMapFrg, "cMapFrg", false, false);
        }
        ft.show(aMapFrg);
        ft.commitAllowingStateLoss();
    }

    private void LoadExhibitByInput(int floor) {
        Cursor cursor = HResDdUtil.getInstance().search(HdAppConfig.getLanguage(), floor, edtSearch.getText().toString());
        if (cursor != null) {

            while (cursor.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor);
                exhibitList.add(exhibit);
            }
        } else {
            cursor.close();
        }
        Cursor cursor1 = HResDdUtil.getInstance().search(HdAppConfig.getLanguage(), floor + 1, edtSearch.getText().toString());
        if (cursor1 != null) {

            while (cursor1.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor1);
                exhibitList.add(exhibit);
            }
        } else {
            cursor1.close();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        timer.cancel();
        EventBus.getDefault().unregister(this);
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
    }

    @Override
    public void onClick(View view) {
        if (ImgChild3F.getVisibility() == View.INVISIBLE) {
            showClick(view.getId());
        } else {
            hideClick(view.getId());
        }

    }

    private void hideClick(int ResId) {
        videoFlag = false;
        switch (ResId) {
            case R.id.Img_child2F:
                ImgChild3F.setVisibility(View.INVISIBLE);
                ImgChild4F.setVisibility(View.INVISIBLE);
                showcMap(2);
                tvTitle.setText("2F");
                videoChildmap.setImageResource(R.drawable.video_on);
                break;
            case R.id.Img_child3F:
                ImgChild3F.setVisibility(View.INVISIBLE);
                ImgChild4F.setVisibility(View.INVISIBLE);
                ImgChild2F.setImageResource(R.drawable.f3_yes);
                tvTitle.setText("3F");
                showcMap(3);
                videoChildmap.setImageResource(R.drawable.video_on);
                break;
            case R.id.Img_child4F:
                ImgChild3F.setVisibility(View.INVISIBLE);
                ImgChild4F.setVisibility(View.INVISIBLE);
                ImgChild2F.setImageResource(R.drawable.f4_yes);
                showcMap(4);
                tvTitle.setText("4F");
                videoChildmap.setImageResource(R.drawable.video_on);
                break;

        }
    }

    private void showClick(int ResId) {
        switch (ResId) {

            case R.id.Img_child2F:
                ;
                ImgChild3F.setVisibility(View.VISIBLE);
                ImgChild4F.setVisibility(View.VISIBLE);

                if (tvTitle.getText().equals("2F")) {
                    ImgChild2F.setImageResource(R.drawable.f2_yes);
                    ImgChild3F.setImageResource(R.drawable.f3_not);
                    ImgChild4F.setImageResource(R.drawable.f4_not);


                } else if (tvTitle.getText().equals("3F")) {
                    ImgChild2F.setImageResource(R.drawable.f2_not);
                    ImgChild3F.setImageResource(R.drawable.f3_yes);
                    ImgChild4F.setImageResource(R.drawable.f4_not);

                } else if (tvTitle.getText().equals("4F")) {
                    ImgChild2F.setImageResource(R.drawable.f2_not);
                    ImgChild3F.setImageResource(R.drawable.f3_not);
                    ImgChild4F.setImageResource(R.drawable.f4_yes);

                } else if (tvTitle.getText().equals("4FL")) {
                    ImgChild2F.setImageResource(R.drawable.f2_not);
                    ImgChild3F.setImageResource(R.drawable.f3_not);
                    ImgChild4F.setImageResource(R.drawable.f4_not);

                }
                break;
            case R.id.Img_child3F:
                ImgChild3F.setVisibility(View.VISIBLE);
                ImgChild4F.setVisibility(View.VISIBLE);
                tvTitle.setText("3F");
                break;
            case R.id.Img_child4F:
                ImgChild3F.setVisibility(View.VISIBLE);
                ImgChild4F.setVisibility(View.VISIBLE);
                tvTitle.setText("4F");

                break;

        }

    }


    /*
   * 隔一复收
   * */
    private boolean isFloorReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != lastFloorNum) {
            lastFloorNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {

            result = data.getStringExtra("RESULT");
        }
        if (data == null || resultCode != RESULT_OK) {
            CommonUtil.showToast(MapAdultActivity.this, getString(R.string.not_recognise));
        } else {

            boolean isHtml = data.getStringExtra("RESULT").contains("http");
            boolean isNum = data.getStringExtra("RESULT").matches("[0-9]+");
            if (isNum) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Cursor cursor = HResDdUtil.getInstance().QueryExhibit(data.getStringExtra("RESULT").toString());

                        if (cursor.getCount() != 0) {
                            while (cursor.moveToNext()) {
                                exhibit = Exhibit.getExhibitInfo(cursor);
                            }
                            cursor.close();
                            if (exhibit != null) {
                                //做资源是否存在的判断

                                if (SingleDown.isExhibitExist(data.getStringExtra("RESULT").toString())) {

                                    if (HdAppConfig.getUserType().equals("adult")) {
                                        intent = new Intent(MapAdultActivity.this, Play.class);
                                    } else {
                                        intent = new Intent(MapAdultActivity.this, ChildPlay.class);
                                    }


                                    intent.putExtra("exhibit", exhibit);
                                    RequestApi.getInstance().getCount(data.getStringExtra("RESULT"));
                                    startActivity(intent);
                                } else {
                                    // TODO: 2017/4/6
                                    HResourceUtil.showDownloadDialog(MapAdultActivity.this);
                                    HResourceUtil.loadSingleExist(MapAdultActivity.this, new ILoadListener() {
                                        @Override
                                        public void onLoadSucceed() {
                                            Toast.makeText(MapAdultActivity.this, R.string.down_success, Toast.LENGTH_SHORT).show();
                                            HResourceUtil.hideDownloadDialog();
                                            if (HdAppConfig.getUserType().equals("child")) {
                                                intent = new Intent(MapAdultActivity.this, ChildPlay.class);
                                            } else {
                                                intent = new Intent(MapAdultActivity.this, Play.class);
                                            }

                                            intent.putExtra("exhibit", exhibit);
                                            //做展品浏览次数上传
                                            RequestApi.getInstance().getCount(exhibit.getFileNo());
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onLoadFailed() {
                                            CommonUtil.showToast(MapAdultActivity.this, getString(R.string.down_fail));
                                            HResourceUtil.hideDownloadDialog();
                                        }
                                    }, exhibit.getFileNo());

                                }

                            }
                            //做展品浏览次数上传

                        } else {
                            CommonUtil.showToast(MapAdultActivity.this, getString(R.string.not_recognise));
                        }
                    }
                });

            } else if (isHtml) {
                Intent intent = new Intent(MapAdultActivity.this, HtmlActivity.class);
                intent.putExtra("html", result);
                startActivity(intent);

            } else {
                CommonUtil.showToast(MapAdultActivity.this, getString(R.string.not_recognise));
            }


        }


        if (data != null && resultCode == RESULT_OK) {
            Log.i("1111", "onActivityResult: " + data.getStringExtra(QrActivity.RESULT));
            ;
        }
    }

}
