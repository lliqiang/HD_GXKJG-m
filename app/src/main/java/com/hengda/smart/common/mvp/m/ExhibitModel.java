package com.hengda.smart.common.mvp.m;

import android.database.Cursor;
import android.text.TextUtils;

import com.hengda.smart.common.dbase.HResDdUtil;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.common.http.FileApi;
import com.hengda.smart.common.http.FileCallback;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import java.io.File;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 15:43
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class ExhibitModel {

    /**
     * 判断展品是否存在
     *
     * @param exhibit
     * @return
     */
    public static boolean isExhibitExist(Exhibit exhibit) {
        File file = new File(HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" +
                exhibit.getFileNo());
        return file.exists();
    }

    /**
     * 根据AutoNo从本地数据库读取相应的展品
     *
     * @param autoNo
     * @return
     */
    public static Exhibit loadExhibitByAutoNo(int autoNo) {
        Exhibit exhibit = null;
        Cursor cursor = HResDdUtil.getInstance().loadExhibitByAutoNo(autoNo);
        if (cursor != null && cursor.moveToNext()) {
            exhibit = cursorToExhibit(cursor);
            cursor.close();
            return exhibit;
        }
        return exhibit;
    }

    /**
     * Cursor 转 HExhibit
     *
     * @param cursor
     * @return
     */
    public static Exhibit cursorToExhibit(Cursor cursor) {
        Exhibit exhibit = new Exhibit();
        exhibit.setAutoNo(cursor.getInt(0));
        exhibit.setFileNo(cursor.getString(1));
        exhibit.setType_auto(cursor.getInt(11));
        exhibit.setFloor(cursor.getInt(5));
        exhibit.setUnitNo(cursor.getInt(6));

        exhibit.setPagePath(HdAppConfig.getDefaultFileDir() + cursor.getString(2) + "/" + cursor
                .getString(1) + ".html");
//        exhibit.setPicPath(HdAppConfig.getDefaultFileDir() + cursor.getString(2) + "/" + cursor
//                .getString(1));
        if (TextUtils.equals("MP3", cursor.getString(3))) {
            exhibit.setHasVoice(true);
            exhibit.setVoicePath(HdAppConfig.getDefaultFileDir() + cursor.getString(2) + "/" +
                    cursor.getString(1) + ".mp3");
        } else {
            exhibit.setHasVoice(false);
        }
        exhibit.setName(cursor.getString(4));
        return exhibit;
    }
    public static void loadExhibit(String fileNo, FileCallback fileCallback) {
        FileApi.getInstance(HdConstants.RES_LOAD_URL).loadFileByName(fileNo, fileCallback);
    }


}
