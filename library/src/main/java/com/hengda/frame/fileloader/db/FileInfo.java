package com.hengda.frame.fileloader.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiwei on 2017/1/17.
 */

public class FileInfo {
    private static FileInfo sInstance = null;

    private DbHelper mMusicDatabase = null;

    public FileInfo(final Context context) {
        mMusicDatabase = DbHelper.getInstance(context);
    }

    public static final synchronized FileInfo getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new FileInfo(context.getApplicationContext());
        }
        return sInstance;
    }

    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FileInfoColumns.TABLE_NAME + " ("
                + FileInfoColumns.NAME + " CHAR NOT NULL,"
                + FileInfoColumns.DATE + " CHAR NOT NULL); ");
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FileInfoColumns.TABLE_NAME);
        onCreate(db);
    }

    public void delete(String name) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.delete(FileInfoColumns.TABLE_NAME, FileInfoColumns.NAME + " = ?", new String[]{name});
    }

    public synchronized void insert(FileBean bean) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(2);
            values.put(FileInfoColumns.NAME, bean.getName());
            values.put(FileInfoColumns.DATE, bean.getDate());
            database.insert(FileInfoColumns.TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public synchronized void update(FileBean bean) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues(2);
            values.put(FileInfoColumns.NAME, bean.getName());
            values.put(FileInfoColumns.DATE, bean.getDate());
            database.update(FileInfoColumns.TABLE_NAME, values, FileInfoColumns.NAME + " = '" + bean.getName() + "'", null);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public List<FileBean> select(String name) {
        List<FileBean> results = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(FileInfoColumns.TABLE_NAME, null,
                    FileInfoColumns.NAME + " = ?", new String[]{name}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    FileBean fb = new FileBean();
                    fb.setName(cursor.getString(0));
                    fb.setDate(cursor.getString(1));
                    results.add(fb);
                } while (cursor.moveToNext());
            }
            return results;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }


    public interface FileInfoColumns {
        /* Table name */
        String TABLE_NAME = "file_info";
        String NAME = "name";
        String DATE = "date";
    }
}
