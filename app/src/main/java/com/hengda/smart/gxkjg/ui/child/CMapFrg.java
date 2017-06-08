package com.hengda.smart.gxkjg.ui.child;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hengda.frame.fileloader.callback.FileCallBack;
import com.hengda.smart.common.autono.BleNoService;
import com.hengda.smart.common.autono.NumService;
import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.tileview.BitmapProviderFile;
import com.hengda.smart.common.tileview.TileViewUtil;
import com.hengda.smart.common.util.BitmapUtils;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.widget.HDialogBuilder;
import com.hengda.smart.common.widget.TipDialog;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.AutoNum;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.MapBase;
import com.hengda.smart.gxkjg.entity.Model;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;
import com.hengda.smart.gxkjg.ui.wdg.HProgressDialog;
import com.hengda.smart.play.ChildPlay;
import com.hengda.smart.play.VedioAc;

import com.hengda.zwf.autonolibrary.BleNumService;
import com.qozix.tileview.TileView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 17:31
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CMapFrg extends BaseFragment {

    TileView tileView;
    MapBase mapBase;
    int floor;
    private List<Exhibit> exhibits;
    private Intent intent;
    private List<View> viewList;
    private List<Exhibit> modelList;
//    private InputMethodManager im;
    private Intent serviceIntent;
    private int lastNum;
    private Exhibit exhibit;
    private ImageView imgF;
    private String path;
    private FileCallBack callback;
    private HProgressDialog progressDialog;
    private TextView ptextView;
    private HDialogBuilder hDialogBuilder;
    private TipDialog tipDialog;
    String wifiSSID;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        wifiSSID= NetUtil.getWifiSSID(HdApplication.mContext);

        tipDialog = new TipDialog(getActivity());
        tipDialog.setCancelable(false);
        tipDialog.message("").pBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipDialog.dismiss();
            }
        }).show();
        if (HdAppConfig.getAutoPlay()) {
//            getActivity().startService(serviceIntent);
    }

        exhibits = new ArrayList<>();
        viewList = new ArrayList<>();
        modelList = new ArrayList<>();
        imgF = new ImageView(getActivity());
        path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/map/" + "0003F.png";
        Glide.with(getActivity()).load(path).into(imgF);


        floor = getArguments().getInt("FLOOR");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ptextView = (TextView) getActivity().getLayoutInflater().inflate(R.layout
                .layout_hd_dialog_custom_tv, null);
        //初始化TileView
        getMapBaseInfo();
        initTileView();
        //瓦片图层，适配屏幕
        addDetailLevel();
        addMarks(tileView);




        return tileView;
    }

    public static CMapFrg newInstance(int floor) {
        CMapFrg fragment = new CMapFrg();
        Bundle bundle = new Bundle();
        bundle.putInt("FLOOR", floor);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 初始化TileView
     */
    private void initTileView() {
        tileView = new TileView(getActivity());
        tileView.setTransitionsEnabled(false);
        tileView.setSize((int) mapBase.getWidth(), (int) mapBase.getHeight());
        tileView.defineBounds(0, 0, mapBase.getWidth(), mapBase.getHeight());
        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
        tileView.setScaleLimits(0, mapBase.getScale());

    }

    /**
     * 添加各级瓦片+底图
     */
    private void addDetailLevel() {
        //添加各级瓦片

        tileView.setBitmapProvider(new BitmapProviderFile());
        String baseMapPath = HdAppConfig.getMapFilePath() + "/" + floor;
        tileView.addDetailLevel(1.000f, baseMapPath + "/1000/%d_%d.png");
        tileView.addDetailLevel(0.500f, baseMapPath + "/500/%d_%d.png");
        tileView.addDetailLevel(0.250f, baseMapPath + "/250/%d_%d.png");
        tileView.addDetailLevel(0.125f, baseMapPath + "/125/%d_%d.png");
        //适配屏幕
        tileView.post(() -> {
            if (tileView != null) {
                tileView.setScale(0);
            }
        });
        ImageView downSample = new ImageView(getActivity());
        Bitmap bitmap = BitmapUtils.loadBitmapFromFile(baseMapPath + "/img.png");
        downSample.setImageBitmap(bitmap);
        tileView.addView(downSample, 0);

    }

    /**
     * 获取 MapBaseInfo
     */
    private void getMapBaseInfo() {
        Cursor cursor = HResDdUtil.getInstance().loadMapInfo(2);
        if (cursor != null) {

            if (cursor.moveToNext()) {
                mapBase = new MapBase();
                mapBase.setWidth(cursor.getDouble(0));
                mapBase.setHeight(cursor.getDouble(1));
                mapBase.setScale(cursor.getInt(2));
            }
            cursor.close();
        }
    }

    /**
     * 获取指定楼层的点位信息
     */
    private void getLocationByFloor(int floor) {
        Cursor cursor = HResDdUtil.getInstance().loadLocsByFloor(floor);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor);

                if (exhibit != null) {

                    exhibits.add(exhibit);
                }

            }
        }
        cursor.close();
    }

    private void addMarks(final TileView tileView) {

        Bitmap bitmapLocationBlue = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_blue, 60, 60);
        Bitmap bitmapLocationRed = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_red, 60, 60);
        Bitmap bitmapLocationYellow = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_yellow, 60, 60);
        Bitmap bitmapLocationVideo = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_video, 60, 60);

        getLocationByFloor(floor);

        for (Exhibit exhibit : exhibits) {
            final ImageView marker = new ImageView(getActivity());
            marker.setTag(exhibit);
            marker.setImageBitmap(bitmapLocationBlue);
            if (exhibit.getType() == 1) {
                marker.setImageBitmap(bitmapLocationBlue);
            }

            marker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    tileView.slideToAndCenter(exhibit.getX(), exhibit.getY());

                    if (!SingleDown.isExhibitExist(exhibit.getFileNo())) {

                        HResourceUtil.showDownloadDialog(getActivity());
                        HResourceUtil.loadSingleExist(getActivity(), new ILoadListener() {
                            @Override
                            public void onLoadSucceed() {
                                Toast.makeText(getActivity(), R.string.down_success, Toast.LENGTH_SHORT).show();
                                HResourceUtil.hideDownloadDialog();
                                Intent intent = new Intent(getActivity(), ChildPlay.class);
                                intent.putExtra("exhibit", exhibit);
                                //做展品浏览次数上传
                                RequestApi.getInstance().getCount(exhibit.getFileNo());
                                startActivity(intent);
                            }

                            @Override
                            public void onLoadFailed() {
                                CommonUtil.showToast(getActivity(), getString(R.string.down_fail));
                                HResourceUtil.hideDownloadDialog();
                            }
                        }, exhibit.getFileNo());

                    }else {
                        if (exhibit.getType() == 1) {
                            intent = new Intent(getActivity(), ChildPlay.class);
                            intent.putExtra("exhibit", exhibit);

                        } else if (exhibit.getType() == 3) {

                            intent = new Intent(getActivity(), VedioAc.class);
                        }
                        startActivity(intent);
                    }

                }

            });
            if (exhibit.getX1()!=0&&exhibit.getType()==1){
                tileView.addMarker(marker, exhibit.getX1(), exhibit.getY1(), null, null);
            }else {

                if (exhibit.getX() != 0&&exhibit.getType()==1) {
                    tileView.addMarker(marker, exhibit.getX(), exhibit.getY(), null, null);
                }
            }

        }

    }


//搜索放大
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(Model model) {

        if (viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                tileView.removeMarker(viewList.get(i));

            }
            viewList.clear();
        }

        modelList = model.getList();
        Bitmap bitmapLocationBlue = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_blue, 60, 60);
        for (Exhibit exhibit : modelList) {
            final ImageView marker = new ImageView(getActivity());
            marker.setTag(exhibit);
            marker.setImageBitmap(bitmapLocationBlue);
//            if (im.isActive()) {
//                im.hideSoftInputFromWindow(tileView.getWindowToken(), 0);
//            }
            viewList.add(marker);


            if (exhibit.getX() != 0&&exhibit.getFloor()==floor) {

                TileViewUtil.fix(tileView, marker, exhibit.getX(), exhibit.getY());
            } else {

            }

        }
        modelList.clear();


    }


//收号
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum num) {
        Bitmap bitmapLocationRed = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_red, 60, 60);
        int temp_num = num.getBeaconNum();
        final ImageView marker = new ImageView(getActivity());
        marker.setImageBitmap(bitmapLocationRed);
        for (int i = 0; i < exhibits.size(); i++) {
            if (exhibits.get(i).getAutoNo() == temp_num && isReplay(temp_num)) {
                RequestApi.getInstance().putPositonInfo(HdAppConfig.getDeviceNo(), 1, exhibits.get(i).getAutoNo(), getActivity());
                addMarks(tileView);
                if (exhibits.get(i).getX1()!=0&&exhibits.get(i).getType()!=0){
                    TileViewUtil.move(tileView, marker, exhibits.get(i).getX1(), exhibits.get(i).getY1(), exhibits.get(i).getX1(), exhibits.get(i).getY1());
                }else {
                    if (exhibits.get(i).getX() != 0&&exhibits.get(i).getType()!=0) {
                        TileViewUtil.move(tileView, marker, exhibits.get(i).getX(), exhibits.get(i).getY(), exhibits.get(i).getX(), exhibits.get(i).getY());
                    }
                }



            }


        }

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

    @Override
    public void onPause() {
        super.onPause();
        if (tipDialog!=null){

            tipDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (tipDialog!=null){
            tipDialog.dismiss();
        }
        ButterKnife.unbind(this);
    }


}
