package com.hengda.smart.common.dbase;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;


/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/30 15:35
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HSQLiteOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public HSQLiteOpenHelper() {
        super(HdApplication.mContext, HdAppConfig.getDbFilePath(), null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
