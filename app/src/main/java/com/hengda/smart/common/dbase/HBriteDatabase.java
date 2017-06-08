package com.hengda.smart.common.dbase;

import com.orhanobut.logger.Logger;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;


/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/30 15:41
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class HBriteDatabase {
    private BriteDatabase db;
    private static volatile HBriteDatabase mInstance = new HBriteDatabase();

    private HBriteDatabase() {
        initDb();
    }

    /**
     * 单例
     *
     * @return
     */
    public static HBriteDatabase getInstance() {
        HBriteDatabase hBriteDatabase = mInstance;
        if (mInstance == null) {
            synchronized (HBriteDatabase.class) {
                hBriteDatabase = mInstance;
                if (mInstance == null) {
                    hBriteDatabase = new HBriteDatabase();
                    mInstance = hBriteDatabase;
                }
            }
        }
        return hBriteDatabase;
    }

    /**
     * 初始化数据库
     */
    private void initDb() {
        SqlBrite sqlBrite = SqlBrite.create(message -> Logger.t("DATABASE").i(message));
        db = sqlBrite.wrapDatabaseHelper(new HSQLiteOpenHelper(), Schedulers.io());
        db.setLoggingEnabled(true);
    }

    /**
     * 获取数据库
     *
     * @return
     */
    public BriteDatabase getDb() {
        if (db == null)
            initDb();
        return db;
    }

}
