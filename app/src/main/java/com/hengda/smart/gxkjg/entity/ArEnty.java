package com.hengda.smart.gxkjg.entity;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by lenovo on 2017/1/12.
 */

public class ArEnty implements Serializable{
    private String ARID;
    private String name;
    private int type;

    public String getARID() {
        return ARID;
    }

    public void setARID(String ARID) {
        this.ARID = ARID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ArEnty getAREntyInfo(Cursor cursor) {
        ArEnty arEnty=new ArEnty();
        arEnty.setARID(cursor.getString(0));
        arEnty.setName(cursor.getString(1));
        arEnty.setType(cursor.getInt(2));
        return arEnty;
    }
}
