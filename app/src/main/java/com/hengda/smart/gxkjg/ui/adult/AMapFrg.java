package com.hengda.smart.gxkjg.ui.adult;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.AutoNum;
import com.hengda.smart.gxkjg.entity.MapBase;
import com.hengda.smart.gxkjg.entity.Model;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;
import com.hengda.smart.play.Play;
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
public class AMapFrg extends BaseFragment {

    TileView tileView;
    MapBase mapBase;
    int floor;
    private int n = 0;
    private List<Exhibit> exhibits;
    private Intent intent;
    private List<String> ints = new ArrayList<>();
    private List<View> viewList = new ArrayList<>();
    private List<Exhibit> modelList;
    private InputMethodManager im;
    private int lastNum;
    private Intent serviceIntent;
    private Exhibit exhibit;
    private int foreAuto;
    String path;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        serviceIntent = new Intent(getActivity(), BleNoService.class);
        super.onCreate(savedInstanceState);


        if (HdAppConfig.getAutoPlay()) {
            EventBus.getDefault().register(this);
//            getActivity().startService(serviceIntent);
        }
        exhibits = new ArrayList<>();
        getMapBaseInfo();
        floor = getArguments().getInt("FLOOR");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        im = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //初始化TileView
        initTileView();
        //瓦片图层，适配屏幕
        addDetailLevel();

        addMarks(tileView);

        return tileView;
    }

    public static AMapFrg newInstance(int floor) {
        AMapFrg fragment = new AMapFrg();
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
        tileView.setTransitionsEnabled(true);
        tileView.setSize((int) mapBase.getWidth(), (int) mapBase.getHeight());
        tileView.defineBounds(0, 0, mapBase.getWidth(), mapBase.getHeight());
        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);
        tileView.setScaleLimits(0, mapBase.getScale());

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

    /**
     * 添加各级瓦片+底图
     */
    private void addDetailLevel() {
        //添加各级瓦片

        tileView.setBitmapProvider(new BitmapProviderFile());
        String baseMapPath = HdAppConfig.getMapFilePath() + "/" + floor;
        Log.i("info", "base:  -------dd-------" + baseMapPath);
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
            while (cursor.moveToNext()) {
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
        if (floor == 4) {

            Cursor cursor1 = HResDdUtil.getInstance().loadLocsByFloor(floor + 1);
            if (cursor1 != null) {
                while (cursor1.moveToNext()) {
                    Exhibit exhibit = Exhibit.getExhibitInfo(cursor1);

                    if (exhibit != null) {

                        exhibits.add(exhibit);
                    }
                }
            }
            cursor1.close();
        }
    }

    private void addMarks(final TileView tileView) {
        getLocationByFloor(floor);

        Bitmap bitmapLocationBlue = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_blue, 60, 60);
        Bitmap bitmapLocationRed = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_red, 60, 60);
        Bitmap bitmapLocationYellow = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_yellow, 60, 60);
        Bitmap bitmapLocationVideo = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_video, 60, 60);

        for (Exhibit exhibit : exhibits) {
            final ImageView marker = new ImageView(getActivity());
            marker.setTag(exhibit);
            if (exhibit.getType() == 1) {
                marker.setImageBitmap(bitmapLocationBlue);
            } else if (exhibit.getType() == 2) {
                marker.setImageBitmap(bitmapLocationYellow);
            } else if (exhibit.getType() == 3) {
                marker.setImageBitmap(bitmapLocationVideo);
            }

            marker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tileView.slideToAndCenter(exhibit.getX(), exhibit.getY());
                    // TODO: 2016/12/24 判断展品是否存在
                    if (!SingleDown.isExhibitExist(exhibit.getFileNo())) {

                        HResourceUtil.showDownloadDialog(getActivity());
                        HResourceUtil.loadSingleExist(getActivity(), new ILoadListener() {
                            @Override
                            public void onLoadSucceed() {
                                Toast.makeText(getActivity(), R.string.down_success, Toast.LENGTH_SHORT).show();
                                HResourceUtil.hideDownloadDialog();
                                Intent intent = new Intent(getActivity(), Play.class);
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

                    } else {
                        if (exhibit.getType() == 1) {
                            intent = new Intent(getActivity(), Play.class);
                            intent.putExtra("exhibit", exhibit);

                        } else if (exhibit.getType() == 3) {

                            intent = new Intent(getActivity(), VedioAc.class);
                        }
                        startActivity(intent);

                    }
                }
            });


            if (exhibit.getX() != 0 && exhibit.getType() == 1) {
                tileView.addMarker(marker, exhibit.getX(), exhibit.getY(), null, null);

            }


        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

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
            if (im.isActive()) {
                im.hideSoftInputFromWindow(tileView.getWindowToken(), 0);
            }
            viewList.add(marker);
            if (exhibit.getX() != 0 && exhibit.getFloor() == floor) {

                TileViewUtil.fix(tileView, marker, exhibit.getX(), exhibit.getY());
            } else {
                if (exhibit.getX() != 0 && exhibit.getFloor() == 5) {
                    TileViewUtil.fix(tileView, marker, exhibit.getX(), exhibit.getY());
                }
            }

        }
        modelList.clear();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(AutoNum num) {
        Bitmap bitmapLocationBlue = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_blue, 60, 60);
        Bitmap bitmapLocationRed = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_red, 60, 60);
        int temp_num = num.getBeaconNum();

        final ImageView marker = new ImageView(getActivity());
        marker.setImageBitmap(bitmapLocationRed);
        for (int i = 0; i < exhibits.size(); i++) {
            if (exhibits.get(i).getAutoNo() == temp_num && isReplay(temp_num)) {
                addMarks(tileView);
                tileView.removeMarker(marker);

                RequestApi.getInstance().putPositonInfo(HdAppConfig.getDeviceNo(), 1, exhibits.get(i).getAutoNo(), getActivity());

                if (exhibits.get(i).getX() != 0&&exhibits.get(i).getType()!=0) {

                    TileViewUtil.move(tileView, marker, exhibits.get(i).getX(), exhibits.get(i).getY(), exhibits.get(i).getX(), exhibits.get(i).getY());
                }


            }


        }

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        if (serviceIntent!=null){

            getActivity().stopService(serviceIntent);
        }
    }
}
