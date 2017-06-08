package com.hengda.smart.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * 作者：Tailyou
 * 时间：2016/1/6 13:33
 * 邮箱：tailyou@163.com
 * 描述：通用工具类
 */
public class CommonUtil {
    /**
     * 显示Toast
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToast(Context context, int msg) {
        showToast(context, context.getResources().getString(msg));
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * 是否通过WiFi连接
     *
     * @param context
     * @return
     */
    public static boolean isOnlineByWifi(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 获取WIFI SSID
     *
     * @param context
     * @return
     */
    public static String getWifiSSID(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID();
    }

    /**
     * 配置语言
     *
     * @param mContext
     * @param language
     */
    public static void configLanguage(Context mContext, String language) {
        Configuration config = mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (language.equals("CHINESE")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else if (language.equals("ENGLISH")) {
                config.locale = Locale.US;
            } else if (language.equals("JAPANESE")) {
                config.locale = Locale.JAPAN;
            } else if (language.equals("KOREAN")) {
                config.locale = Locale.KOREA;
            } else {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            }
        } else {
            if (language.equals("CHINESE")) {
                config.locale = Locale.CHINESE;
            } else if (language.equals("ENGLISH")) {
                config.locale = Locale.ENGLISH;
            } else if (language.equals("JAPANESE")) {
                config.locale = Locale.JAPAN;
            } else if (language.equals("KOREAN")) {
                config.locale = Locale.KOREAN;
            } else {
                config.locale = Locale.CHINESE;
            }
        }
        mContext.getResources().updateConfiguration(config, null);
    }


    /**
     * 获取版本名称
     *
     * @param mContext
     * @return
     */
    public static String getAppVersionName(Context mContext) {
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pInfo.versionName;
    }


    /**
     * 格式化文件大小 单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
    public static CharSequence formatTime(int millsTime){
        return DateFormat.format("mm:ss",millsTime);
    }

}
