package com.hengda.smart.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hengda.smart.common.util.ScreenUtil;

/**
 * Created by lenovo on 2017/2/17.
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int height=ScreenUtil.getInstance(getContext()).getScreenH();
        int tempWindowHeight = ScreenUtil.getInstance(getContext()).getScreenH() - ScreenUtil.getStatusHeight(getContext());
        if(tempWindowHeight != ScreenUtil.getInstance(getContext()).getScreenH()) {
            height = tempWindowHeight;

        }
    }
}
