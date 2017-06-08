package com.hengda.smart.gxkjg.ui.common;

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

import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.common.tileview.BitmapProviderFile;
import com.hengda.smart.common.util.BitmapUtils;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.gxkjg.entity.MapBase;
import com.hengda.smart.gxkjg.group.MemberInfo;
import com.qozix.tileview.TileView;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/9/28 17:31
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class CommonF extends BaseFragment {

    TileView tileView;
    MapBase mapBase;
    int floor;
    MemberInfo memberInfo;

    private ImageView mark;
    int map_name;
    private Exhibit exhibit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        floor = getArguments().getInt("FLOOR");
        memberInfo = (MemberInfo) getArguments().getSerializable("memberInfo");
        map_name = Integer.parseInt(memberInfo.getMap_id());
        mark = new ImageView(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bitmap bitmapLocationBlue = BitmapUtils.decodeSampledBitmapFromResource(getResources(),
                R.mipmap.location_blue, 60, 60);

        getMapBaseInfo();
        //初始化TileView
        initTileView();
        //瓦片图层，适配屏幕
        addDetailLevel();
        mark.setImageBitmap(bitmapLocationBlue);
        if (HdAppConfig.getUserType().equals("adult")) {
            if (map_name == 4) {
                // TODO: 2017/6/5 根据autoNum得到新的坐标，添加mark
                Cursor cursor = HResDdUtil.getInstance().QueryExhibitByAutoNum(Integer.parseInt(memberInfo.getAuto_num()));
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        exhibit = Exhibit.getExhibitInfo(cursor);
                        if (exhibit.getX1()>0){
                            tileView.addMarker(mark, exhibit.getX(), exhibit.getY(), null, null);
                            Log.i("loacation","location: ---------------"+exhibit.getX1()+"  "+exhibit.getY1());
                        }
                    }
                    cursor.close();
                }

            } else {

                tileView.addMarker(mark, memberInfo.getAxis_x(), memberInfo.getAxis_y(), null, null);
            }
        } else {
            tileView.addMarker(mark, memberInfo.getAxis_x(), memberInfo.getAxis_y(), null, null);
        }


        return tileView;
    }

    public static CommonF newInstance(int floor, MemberInfo memberInfo) {
        CommonF fragment = new CommonF();
        Bundle bundle = new Bundle();
        bundle.putInt("FLOOR", floor);
        bundle.putSerializable("memberInfo", memberInfo);
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


}
