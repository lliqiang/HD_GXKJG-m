package com.hengda.smart.common.mvp.m;

import android.util.Log;

import com.hengda.smart.common.http.FileApi;
import com.hengda.smart.common.http.FileCallback;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import java.io.File;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 13:01
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class DbModel {
    public static  String wifiSSID1 = NetUtil.getWifiSSID(HdApplication.mContext);
    /**
     * 判断数据库是否存在
     *
     * @return
     */
    public static boolean isDbExist() {
        File file = new File(HdAppConfig.getDbFilePath());
        return file.exists();
    }

    /**
     * 下载数据库
     *
     * @param fileCallback
     */
    public static void loadDb(FileCallback fileCallback) {
        String url;
//        if (NetUtil.isWifi(HdApplication.mContext) &&
//                wifiSSID1.contains(HdConstants.DEFAULT_SSID)){
//            url="http://192.168.10.27/12345/";
//        }else {
            url="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/";
//        }
        Log.i("url","url-------------data: "+url);
        FileApi.getInstance().loadFileByName("DATABASE.zip",
                fileCallback);
    }

}
