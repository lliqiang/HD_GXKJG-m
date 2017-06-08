package com.hengda.smart.common.tileview;

import com.qozix.tileview.widgets.ZoomPanLayout;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/8/29 15:14
 * 邮箱：tailyou@163.com
 * 描述：
 */
public abstract class HZoomPanListener implements ZoomPanLayout.ZoomPanListener {

    @Override
    public void onPanBegin(int x, int y, Origination origin) {

    }

    @Override
    public void onPanUpdate(int x, int y, Origination origin) {

    }

    @Override
    public void onPanEnd(int x, int y, Origination origin) {

    }

    @Override
    public void onZoomBegin(float scale, Origination origin) {

    }

    @Override
    public void onZoomUpdate(float scale, Origination origin) {

    }

    @Override
    public abstract void onZoomEnd(float scale, Origination origin);

}
