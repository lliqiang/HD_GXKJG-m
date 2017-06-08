package com.hengda.smart.gxkjg.ui.adult;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.autono.NumService;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.dialog.DialogClickListener;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.p.ResPresenter;
import com.hengda.smart.common.mvp.v.IResLoadView;
import com.hengda.smart.common.rxbus.RxBus;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.DataManager;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.util.StatusBarCompat;
import com.hengda.smart.common.widget.ListDialog;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.entity.AutoNum;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.Exhibition;
import com.hengda.smart.gxkjg.tool.TabAdapter;
import com.hengda.smart.gxkjg.ui.common.BaseActivity;
import com.hengda.zwf.autonolibrary.BleNumService;
import com.jakewharton.rxbinding.view.RxView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListF4AdultActivity extends BaseActivity implements IResLoadView {

    @Bind(R.id.ivBack)
    ImageView ivBack;
    @Bind(R.id.ivMap)
    ImageView ivMap;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private ListDialog listDialog;
    List<Fragment> fragments = new ArrayList<>();
    List<String> tabTitles = new ArrayList<>();
    private List<Exhibition> exhibitionList = new ArrayList<>();
    private List<Exhibit> exhibitList = new ArrayList<>();
    TextView txtLoadProgress;
    ResPresenter resPresenter;
    @Bind(R.id.floor_txt)
    ImageView floorTxt;
    private Intent serviceIntent;
    private int lastNum = -1;
    private int lastUnit = -1;
    private Exhibit exhibitF;
    private int FloorNum = -1;
    private AGuideListFrgF4 aGuideListFrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_list);
        listDialog = new ListDialog(ListF4AdultActivity.this);
        ButterKnife.bind(this);
        StatusBarCompat.translucentStatusBar(ListF4AdultActivity.this);
//        serviceIntent = new Intent(ListF4AdultActivity.this, BleNumService.class);
        if (HdAppConfig.getAutoPlay()) {
//            startService(serviceIntent);
            EventBus.getDefault().register(this);
        }
        QureyArea(4);
        QueryExhibitByFloor(4);
        ivBack.setOnClickListener(v -> finish());
        floorTxt.setImageResource(R.drawable.txt_4f);


        ivMap.setOnClickListener(v -> {
            if (!HdAppConfig.isMapResExist()) {
                if (!HdAppConfig.isMapResExist()) {
                    // TODO: 2016/12/24 下载地图资源
                    HResourceUtil.showDownloadDialog(ListF4AdultActivity.this);
                    HResourceUtil.loadMapRes(ListF4AdultActivity.this, new ILoadListener() {
                        @Override
                        public void onLoadSucceed() {
                            CommonUtil.showToast(ListF4AdultActivity.this, getString(R.string.down_success));
                            HResourceUtil.hideDownloadDialog();
                            toMapAtc(4);
                        }

                        @Override
                        public void onLoadFailed() {
                            CommonUtil.showToast(ListF4AdultActivity.this, getString(R.string.down_fail));
                            HResourceUtil.hideDownloadDialog();
                        }
                    });
                }
            } else {
                toMapAtc(4);
            }

        });

        resYes();
    }

    @Override
    public void resYes() {

        for (int i = 0; i < exhibitionList.size(); i++) {
            if (exhibitionList.get(i).getFloor() == 4) {

                aGuideListFrg = AGuideListFrgF4.getInstance(exhibitionList.get(i).getUnitNo(), 4);
            } else {
                aGuideListFrg = AGuideListFrgF4.getInstance(exhibitionList.get(i).getUnitNo(), 5);
            }
            fragments.add(aGuideListFrg);
            tabTitles.add(exhibitionList.get(i).getName());
            tabLayout.addTab(tabLayout.newTab().setText(exhibitionList.get(i).getName()));
        }

        //初始化ViewPager
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(),
                fragments, tabTitles));


        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showLoadingDialog() {
        txtLoadProgress = (TextView) getLayoutInflater().inflate(R.layout
                .dialog_custom_view_txt, null);
        txtLoadProgress.setTypeface(HdApplication.typefaceGxa);
        txtLoadProgress.setText(R.string.downloading);
        DialogCenter.showDialog(ListF4AdultActivity.this, txtLoadProgress,
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
            if (progress == total) {
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
        CommonUtil.showToast(ListF4AdultActivity.this, R.string.load_failed);
    }

    /**
     * 跳转至地图界面
     *
     * @param floor
     */
    private void toMapAtc(int floor) {
        Bundle bundle = new Bundle();
        bundle.putInt("FLOOR", floor);
        openActivity(ListF4AdultActivity.this, MapAdultActivity.class, bundle);
    }

    public void QureyArea(int floor) {
        Cursor cursor = HResDdUtil.getInstance().loadAreaByFloor(floor);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibition exhibition = Exhibition.getExhibitInfo(cursor);
                exhibitionList.add(exhibition);

            }
            cursor.close();
        }
        Cursor cursor1 = HResDdUtil.getInstance().loadAreaByFloor(floor + 1);
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                Exhibition exhibition = Exhibition.getExhibitInfo(cursor1);
                exhibitionList.add(exhibition);

            }
            cursor1.close();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum event) {
        for (int i = 0; i < exhibitList.size() - 1; i++) {

            Cursor cursor = HResDdUtil.getInstance().QueryExhibitByAutoNum(event.getBeaconNum());
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    exhibitF = Exhibit.getExhibitInfo(cursor);
                    if (exhibitF.getFloor() != exhibitList.get(i).getFloor() && exhibitF.getFloor() != 4 && exhibitF.getFloor() != 5 && !listDialog.isShowing() && isFloorReplay(exhibitF.getFloor())) {

                        listDialog.message(getString(R.string.location_where) + "4F" + getString(R.string.swipfloor) + exhibitF.getFloor() + "F")
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
                                if (exhibitF != null) {
//                                    RxBus.getDefault().post(exhibitF);
                                    EventBus.getDefault().post(exhibitF);
                                    listDialog.hide();
                                    finish();
                                }

                            }
                        }).show();


                    }
                }
                cursor.close();

            }

            if (event.getBeaconNum() == exhibitList.get(i).getAutoNo() && isReplay(event.getBeaconNum()) && isUnitReplay(exhibitList.get(i).getUnitNo())) {

                if (NetUtil.isConnected(ListF4AdultActivity.this)) {

                    RequestApi.getInstance().putPositonInfo(HdAppConfig.getDeviceNo(), 1, exhibitList.get(i).getAutoNo(), ListF4AdultActivity.this);
                }

                int finalI = i;
                if (exhibitList.get(finalI).getFloor() != 5) {
                    viewPager.setCurrentItem(exhibitList.get(finalI).getUnitNo() - 1);
                    tabLayout.setScrollPosition(exhibitList.get(finalI).getUnitNo() - 1, 0, true);
                } else {
                    viewPager.setCurrentItem(exhibitList.get(finalI).getUnitNo() - 1 + 8);
                    tabLayout.setScrollPosition(exhibitList.get(finalI).getUnitNo() - 1 + 8, 0, true);
                }


            }
        }

    }

    //判断    隔一复收
    private boolean isReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != lastNum) {
            //如果不是-1的话返回true同时将蓝牙号置为-1
            lastNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    private boolean isFloorReplay(int num) {
        boolean temp_flag = false;
        if (num != 0 && num != FloorNum) {
            //如果不是-1的话返回true同时将蓝牙号置为-1
            FloorNum = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    //根据楼层查询所有的展品
    public void QueryExhibitByFloor(int floor) {
        Cursor cursor = HResDdUtil.getInstance().loadLocsByFloor(floor);
        while (cursor.moveToNext()) {
            Exhibit exhibit = Exhibit.getExhibitInfo(cursor);
            exhibitList.add(exhibit);
        }
        cursor.close();

        Cursor cursor1 = HResDdUtil.getInstance().loadLocsByFloor(floor + 1);
        while (cursor1.moveToNext()) {
            Exhibit exhibit = Exhibit.getExhibitInfo(cursor1);
            exhibitList.add(exhibit);
        }
        cursor1.close();
    }

    //判断    隔一复收
    private boolean isUnitReplay(int num) {
        boolean temp_flag = false;

        if (num != 0 && num != lastUnit) {
            //如果不是-1的话返回true同时将蓝牙号置为-1
            lastUnit = num;
            temp_flag = true;
        }
        return temp_flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        if (listDialog!=null){
            if (listDialog.isShowing()){
                listDialog.hide();
                listDialog=null;
            }
        }
//        if (serviceIntent!=null){
//
//            stopService(serviceIntent);
//        }

    }
}