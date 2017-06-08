package com.hengda.smart.gxkjg.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.multidex.MultiDexApplication;

import com.hengda.frame.fileloader.HDFileLoader;
import com.hengda.smart.common.autono.SerialPort;
import com.hengda.smart.common.map.LocationService;
import com.hengda.smart.common.util.CrashHandler;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;


/**
 * 作者：Tailyou
 * 时间：2016/1/8 10:42
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HdApplication extends MultiDexApplication {
    /*
    * */
    private static SerialPort mSerialPort;
    /**
     * 开发状态枚举
     */
    public static DevelopStatus status = DevelopStatus.DEVELOPING;
    /**
     * 全局上下文环境
     */
    public static Context mContext;
    /**
     * 全局字体样式
     */
    public static Typeface typefaceGxa;
    public static Typeface typefaceGxc;
    private static List<Activity> activities = new ArrayList<>();
    public LocationService locationService;
    public Vibrator mVibrator;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
//        SDKInitializer.initialize(getApplicationContext());
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new com.hengda.frame.fileloader.log.LoggerInterceptor("TAG"))
                .build();
        HDFileLoader.initClient(getApplicationContext(), okHttpClient);

        CrashReport.initCrashReport(getApplicationContext(), "c566e26eae", false);
        Logger.init("HD_SMART");
        mContext = getApplicationContext();

        typefaceGxa = Typeface.createFromAsset(getAssets(), "fonts/gxa.ttf");
        typefaceGxc = Typeface.createFromAsset(getAssets(), "fonts/gxc.ttf");

        if (status == DevelopStatus.TEST) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        CrashReport.initCrashReport(getApplicationContext(), "900055531", false);
    }

    /**
     * 往Activity栈加入Activity
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 从Activity栈移除Activity
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 清空Activity栈
     */
    public static void clearActivity() {
        Observable.from(activities)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(activity -> {
                    activity.finish();
                });
    }

    /**
     * 获取串口
     *
     * @return
     * @throws SecurityException
     * @throws IOException
     * @throws InvalidParameterException
     */
    public static SerialPort getSerialPort() throws
            SecurityException,
            IOException,
            InvalidParameterException {
        if (mSerialPort == null) {
            String path = "/dev/s3c2410_serial3";
            int baudrate = 57600;
            /* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }
            /* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    /**
     * 关闭串口
     */
    public static void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
