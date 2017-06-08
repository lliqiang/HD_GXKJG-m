package com.hengda.smart.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId,
                                                         int reqWidth,
                                                         int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static Bitmap decodeSampledBitmapFromFile(String pathName,
                                                     int reqWidth,
                                                     int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth,
                                            int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    /**
     * 从res加载Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap loadBitmapFromRes(Context context, int resId) {
        InputStream is = context.getResources().openRawResource(resId);
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 根据文件路径加载Bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap loadBitmapFromFile(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, opt);
        return bitmap;
    }


    /**
     * Bitmap 和 ImageView 适配指定宽度
     *
     * @param imageView
     * @param bitmap
     * @param specifyWidth
     */
    public static void picFitSpecifyWidth(ImageView imageView,
                                          Bitmap bitmap,
                                          int specifyWidth) {
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        int width = specifyWidth;
        int height = width * bHeight / bWidth;
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.width = width;
        para.height = height;
        imageView.setLayoutParams(para);
    }

    /**
     * Bitmap 和 ImageView 适配指定高度
     *
     * @param imageView
     * @param bitmap
     * @param specifyHeight
     */
    public static void picFitSpecifyHeight(ImageView imageView,
                                           Bitmap bitmap,
                                           int specifyHeight) {
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();
        int height = specifyHeight;
        int width = height * bWidth / bHeight;
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        para.width = width;
        para.height = height;
        imageView.setLayoutParams(para);
    }

    /**
     * 缩放Bitmap
     *
     * @param bm
     * @param scale
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bm, float scale) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // 得到新的图片   www.2cto.com
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

}
