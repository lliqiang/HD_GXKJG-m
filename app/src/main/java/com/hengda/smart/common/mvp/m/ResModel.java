package com.hengda.smart.common.mvp.m;

import com.hengda.smart.common.http.FileApi;
import com.hengda.smart.common.http.FileCallback;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import java.io.File;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 13:01
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class ResModel {

    /**
     * 判断资源是否存在
     *
     * @return
     */
    public static boolean isResExist() {
        File file = new File(HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage());
        return file.exists();
    }

    /**
     * 下载资源
     *
     * @param fileCallback
     */
    public static void loadRes(FileCallback fileCallback) {
        FileApi.getInstance(HdConstants.RES_LOAD_URL).loadFileByName(HdAppConfig.getLanguage()+
                ".zip", fileCallback);
    }

    /**
     * 取消下载
     */
    public static void cancelLoad() {
        FileApi.cancelLoading();
    }

}
