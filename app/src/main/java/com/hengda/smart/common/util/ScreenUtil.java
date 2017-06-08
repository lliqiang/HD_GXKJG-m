package com.hengda.smart.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 作者：Tailyou
 * 时间：2016/1/6 12:36
 * 邮箱：tailyou@163.com
 * 描述：屏幕处理工具，截屏，获取屏幕宽、高、密度等
 */
public class ScreenUtil {

    private int screenW;
    private int screenH;
    private float screenDensity;

    private volatile static ScreenUtil sInstance;

    /**
     * 单例模式：私有构造方法
     *
     * @param mContext
     */
    private ScreenUtil(Context mContext) {
        DisplayMetrics metric = mContext.getResources()
                .getDisplayMetrics();
        screenW = metric.widthPixels;
        screenH = metric.heightPixels;
        screenDensity = metric.density;
    }

    /**
     * 获取类实例
     *
     * @param mContext
     * @return
     */
    public static ScreenUtil getInstance(Context mContext) {
        if (sInstance == null) {
            synchronized (ScreenUtil.class) {
                if (sInstance == null) {
                    sInstance = new ScreenUtil(mContext);
                }
            }
        }
        return sInstance;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenW() {
        return screenW;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getScreenH() {
        return screenH;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public float getScreenDensity() {
        return screenDensity;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 截屏，去掉了statusBar和smartBar
     *
     * @param activity
     * @return
     */
    public Bitmap snapShotPure(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmapFull = view.getDrawingCache();

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        Bitmap bitmap = Bitmap.createBitmap(bitmapFull, 0, frame.top, screenW,
                frame.bottom - frame.top);
        view.destroyDrawingCache();
        return bitmap;
    }

    /**
     * 截屏，全屏截取
     *
     * @param activity
     * @return
     */
    public Bitmap snapShotFull(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmapFull = view.getDrawingCache();
        return bitmapFull;
    }

}
