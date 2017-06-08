package com.hengda.smart.gxkjg.ui.child;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.http.HResourceUtil;
import com.hengda.smart.common.http.RequestApi;
import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.mvp.m.SingleDown;
import com.hengda.smart.common.tileview.BitmapProviderFile;
import com.hengda.smart.common.util.BitmapUtils;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.MapBase;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;
import com.hengda.smart.play.ChildPlay;
import com.hengda.smart.play.Play;
import com.hengda.smart.play.VedioAc;
import com.qozix.tileview.TileView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 17:31
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CMapFrgAR extends BaseFragment {

    TileView tileView;
    MapBase mapBase;
    int floor;
    private Intent intent;
private List<Exhibit> exhibits;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exhibits=new ArrayList<>();
        getMapBaseInfo();
        floor = getArguments().getInt("FLOOR");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //初始化TileView
        initTileView();
        //瓦片图层，适配屏幕
        addDetailLevel();
        addMarks(tileView);
        return tileView;
    }

    public static CMapFrgAR newInstance(int floor) {
        CMapFrgAR fragment = new CMapFrgAR();
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
    private void getLocationByFloor( int floor) {
        Cursor cursor = HResDdUtil.getInstance().loadLocsByFloor( floor);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Exhibit exhibit = Exhibit.getExhibitInfo(cursor);

                if (exhibit!=null){

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
            if (exhibit.getType_ar()==2){
                marker.setImageBitmap(bitmapLocationYellow);
            }


            marker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tileView.slideToAndCenter(exhibit.getX(), exhibit.getY());
                    // TODO: 2016/12/24 判断展品是否存在
                    if (!SingleDown.isExhibitExist(exhibit.getFileNo())){

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

                    }else {
                        if (exhibit.getType() == 1) {
                            intent = new Intent(getActivity(), ChildPlay.class);
                            intent.putExtra("exhibit", exhibit);

                        } else if (exhibit.getType() == 2) {

                          //  intent = new Intent(getActivity(), GetDataActivity.class);

                        } else if (exhibit.getType() == 3) {

                            intent = new Intent(getActivity(), VedioAc.class);
                        }
                        startActivity(intent);

                    }
                }
            });
            if (exhibit.getX()!=0){

                tileView.addMarker(marker, exhibit.getX(), exhibit.getY(), null, null);
            }

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
