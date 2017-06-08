package com.hengda.smart.common.mvp.m;

import com.hengda.smart.common.util.FileUtils;
import com.hengda.smart.common.util.HFileCallBack;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

import static com.hengda.smart.common.http.HResourceUtil.wifiSSID;

/**
 * Created by lenovo on 2016/12/22.
 */

public class SingleDown {

private static String url;
//单包下载字体
public static void LoadFontRes(final ILoadListener iLoadListener){


    if (NetUtil.isWifi(HdApplication.mContext)&&wifiSSID.contains(HdConstants.DEFAULT_SSID)){
         url=HdConstants.DEFAULT_RES_INNER+"HappyFont.zip";
    }else {

         url=HdConstants.DEFAULT_RES_OUTTER+ "HappyFont.zip";
    }

    String destFileDir = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage()+"/child";
    String destFileName = "EXHIBIT.zip";

    FileUtils.makeDirs(destFileDir);
    OkHttpUtils.getInstance().cancelTag("LOAD_RES");

    OkHttpUtils.get()
            .tag("LOAD_RES")
            .url(url)
            .build()
            .execute(new HFileCallBack(destFileDir, destFileName) {
                @Override
                public void inProgress(float progress, long total) {

                }

                @Override
                public void onError(okhttp3.Call call, Exception e) {
                    iLoadListener.onLoadFailed();
                }

                @Override
                public void onResponse(File file) {
                    iLoadListener.onLoadSucceed();
                }
            });
}

    //判断单包展品资源是否存在
    public static boolean isExhibitExist(String fileNo) {
        File file = new File(HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/"+HdAppConfig.getUserType()+"/" +
                fileNo);
        return file.exists();
    }
}
