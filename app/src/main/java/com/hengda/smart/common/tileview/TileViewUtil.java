package com.hengda.smart.common.tileview;

import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.qozix.tileview.TileView;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/4/14 16:53
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class TileViewUtil {

    /**
     * 定位
     *
     * @param tileView
     * @param movingImg
     * @param locX
     * @param locY
     */
    public static void fix(TileView tileView, ImageView movingImg, double
            locX, double locY) {
        tileView.removeMarker(movingImg);
        Animation animation=new ScaleAnimation(1.0f,2.0f,1.0f,2.0f,0.5f,0.5f);
//        Animation animation=new ScaleAnimation()
        animation.setDuration(2000L);
        movingImg.startAnimation(animation);
        tileView.addMarker(movingImg, locX, locY, null, null);

        tileView.slideToAndCenter(locX, locY);
    }

    /**
     * 平滑移动
     *
     * @param tileView
     * @param movingImg
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public static void move(final TileView tileView, final ImageView movingImg, double startX,
                            double startY, final double endX, final double endY) {
        if (startX == 0 || startY == 0 || (startX == endX && startY == endY)) {

            /*
             fix(TileView tileView, ImageView movingImg, double
            locX, double locY)


            *  tileView.removeMarker(movingImg);
        Animation animation=new ScaleAnimation(1.0f,2.0f,1.0f,2.0f,0.5f,0.5f);
//        Animation animation=new ScaleAnimation()
        animation.setDuration(2000L);
//        movingImg.startAnimation(animation);
        tileView.addMarker(movingImg, locX, locY, null, null);

        tileView.slideToAndCenter(locX, locY);
            *
            * */
            tileView.removeMarker(movingImg);
            tileView.addMarker(movingImg, endX, endY, null, null);

            tileView.slideToAndCenter(endX, endY);
//            fix(tileView, movingImg, endX, endY);
        } else {
            float scale = tileView.getScale();

            Animation animation = new TranslateAnimation(0, (float) (scale * (endX - startX)), 0,
                    (float) (scale * (endY - startY)));

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tileView.removeMarker(movingImg);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            tileView.removeMarker(movingImg);
            tileView.addMarker(movingImg, startX, startY, null, null);
            animation.setDuration(3000);
//            tileView.setAnimation(animation);
//            tileView.setAnimationDuration(3000);
//            tileView.scrollTo(endX,endY);

//            tileView.setSlideDuration(3000);
            movingImg.startAnimation(animation);
            tileView.slideToAndCenter(endX,endY);
        }
    }

}
