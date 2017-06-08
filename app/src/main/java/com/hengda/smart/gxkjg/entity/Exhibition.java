package com.hengda.smart.gxkjg.entity;

import android.database.Cursor;

/**
 * Created by lenovo on 2017/1/4.
 */

public class Exhibition {
    private String name;
    private int UnitNo;
    private int floor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitNo() {
        return UnitNo;
    }

    public void setUnitNo(int unitNo) {
        UnitNo = unitNo;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
    public static Exhibition getExhibitInfo(Cursor cursor) {
        Exhibition exhibition = new Exhibition();
        exhibition.setName(cursor.getString(0));
        exhibition.setUnitNo(cursor.getInt(1));
        exhibition.setFloor(cursor.getInt(2));

        return exhibition;
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "name='" + name + '\'' +
                ", UnitNo=" + UnitNo +
                ", floor=" + floor +
                '}';
    }
}
