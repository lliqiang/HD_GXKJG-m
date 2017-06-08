package com.hengda.smart.gxkjg.ui.wdg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hengda.smart.common.util.BitmapUtils;
import com.hengda.smart.common.util.ScreenUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdApplication;

import java.util.Random;

public class ParticleView extends BaseView {

    private final float startPerX;
    private final float startPerY;
    private Paint paint;
    private Random random;
    private int screenWidth;
    private int screenHeight;

    private int particleImg;
    private int particleSpd;

    private float startX;
    private float startY;

    public ParticleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        random = new Random();
        paint = new Paint();
        screenWidth = ScreenUtil.getInstance(HdApplication.mContext).getScreenW();
        screenHeight = ScreenUtil.getInstance(HdApplication.mContext).getScreenH();

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ParticleView);
        particleImg = ta.getResourceId(R.styleable.ParticleView_particleImg, -1);
        particleSpd = ta.getInteger(R.styleable.ParticleView_particleSpeed, 24);
        startPerX = ta.getFloat(R.styleable.ParticleView_startX, 0);
        startPerY = ta.getFloat(R.styleable.ParticleView_startY, 0);
        ta.recycle();

        startX = screenWidth * startPerX;
        startY = screenHeight * startPerY;

    }

    @Override
    protected void drawSub(Canvas canvas) {
        Bitmap bitmap = BitmapUtils.loadBitmapFromRes(HdApplication.mContext, particleImg);
        canvas.drawBitmap(bitmap, startX, startY, paint);

        if(bitmap != null && !bitmap.isRecycled()){
             bitmap.recycle();
             bitmap = null;
            }
        System.gc();
    }

    @Override
    protected void logic() {
        startX += particleSpd * (int) Math.pow(-1, random.nextInt());
        startY += particleSpd * (int) Math.pow(-1, random.nextInt());
        if (Math.abs(startX - screenWidth * startPerX) > 10 ||
                Math.abs(startY - screenHeight * startPerY) > 10) {
            startX = screenWidth * startPerX;
            startY = screenHeight * startPerY;
        }
    }

}
