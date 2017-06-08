package com.hengda.smart.gxkjg.ui.adult;

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

import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.tileview.BitmapProviderFile;
import com.hengda.smart.common.tileview.TileViewUtil;
import com.hengda.smart.common.util.BitmapUtils;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.MapBase;
import com.hengda.smart.gxkjg.entity.Model;
import com.hengda.smart.gxkjg.ui.common.BaseFragment;
import com.hengda.smart.play.Play;
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
public class AMapFrgAR extends BaseFragment {

    TileView tileView;
    MapBase mapBase;
    int floor;
private List<Exhibit> exhibits;
    private List<View> viewList=new ArrayList<>();
    private List<Exhibit> modelList;
    private InputMethodManager im;
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
        EventBus.getDefault().register(this);
        im = (InputMethodManager)getActivity(). getSystemService(getActivity().INPUT_METHOD_SERVICE);
        //初始化TileView
        initTileView();
        //瓦片图层，适配屏幕
        addDetailLevel();
        addMarks(tileView);
        return tileView;
    }

    public static AMapFrgAR newInstance(int floor) {
        AMapFrgAR fragment = new AMapFrgAR();
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
        Cursor cursor = HResDdUtil.getInstance().loadMapInfo(1);
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
                    Intent intent=new Intent(getActivity(), Play.class);
                    intent.putExtra("exhibit",exhibit);
                    startActivity(intent);
                }
            });
            tileView.addMarker(marker, exhibit.getX(), exhibit.getY(), null, null);

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainEventBus(Model model) {

        if (viewList.size()>0){
            for (int i = 0; i < viewList.size(); i++) {
                tileView.removeMarker(viewList.get(i));

            }
            viewList.clear();
        }

        modelList = model.getList();
        Bitmap bitmapLocationRed = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_red, 60, 60);
        for (Exhibit exhibit : modelList) {
            final ImageView marker = new ImageView(getActivity());
            marker.setTag(exhibit);
            marker.setImageBitmap(bitmapLocationRed);
            if (im.isActive()){
                im.hideSoftInputFromWindow(tileView.getWindowToken(), 0);
            }
            viewList.add(marker);
            TileViewUtil.move(tileView,marker,exhibit.getX(),exhibit.getY(),exhibit.getX(),exhibit.getY());
            Log.i("info", "list.size() ------" + modelList.size());
            modelList.clear();
        }




    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
    }
}
