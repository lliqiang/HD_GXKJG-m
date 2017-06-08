package com.hengda.frame.fileloader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shiwei on 2017/1/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASENAME = "fileloader.db";
    private static final int VERSION = 1;
    private static DbHelper sInstance = null;
    private final Context mContext;

    public DbHelper(final Context context) {
        super(context, DATABASENAME, null, VERSION);
        mContext = context;
    }

    public static final synchronized DbHelper getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new DbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        FileInfo.getInstance(mContext).onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FileInfo.getInstance(mContext).onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        FileInfo.getInstance(mContext).onDowngrade(db, oldVersion, newVersion);
    }
}
