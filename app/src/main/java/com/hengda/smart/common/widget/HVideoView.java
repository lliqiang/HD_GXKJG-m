package com.hengda.smart.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 作者：Tailyou
 * 时间：2016/2/4 17:10
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HVideoView extends VideoView {

    public HVideoView(Context context) {
        super(context);
    }

    public HVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

}
