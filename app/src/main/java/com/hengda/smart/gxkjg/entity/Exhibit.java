package com.hengda.smart.gxkjg.entity;

import android.database.Cursor;

import java.io.Serializable;

/**
 * 作者：Tailyou
 * 时间：2016/3/7 09:06
 * 邮箱：tailyou@163.com
 * 描述：实体-展品
 */
public class Exhibit implements Serializable {

    /**
     * 展品多模卡号
     */
    private int autoNo;
    /**
     * 展品名称
     */
    private String name;
    /**
     * 展品文件编号
     */
    private String fileNo;
    /**
     * 是否有声音
     */
    private boolean hasVoice;
    /**
     * 网页路径
     */
    private String pagePath;
    /**
     * 声音路径
     */
    private String voicePath;
    /**
     * 图片数量
     */
    private int picCount;
    private int type;
    private int type_ar;
    private int type_auto;
    private int floor;
    private int UnitNo;
private String unitName;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public int getType_ar() {
        return type_ar;
    }

    public void setType_ar(int type_ar) {
        this.type_ar = type_ar;
    }

    public int getType_auto() {
        return type_auto;
    }

    public void setType_auto(int type_auto) {
        this.type_auto = type_auto;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 图片基础路径
     */
    private String picPath;

    private double x;
    private double y;
    private double x1;
    private double y1;

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getAutoNo() {
        return autoNo;
    }

    public String getName() {
        return name;
    }

    public String getFileNo() {
        return fileNo;
    }

    public boolean isHasVoice() {
        return hasVoice;
    }

    public String getPagePath() {
        return pagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }

    public int getPicCount() {
        return picCount;
    }

//    public String getPicPath() {
//        return picPath + ".png";
//    }

    public String getThumbPicPath() {
        return picPath + "_icon.png";
    }

    public String getPicPath(int index) {
        if (index == 0) {
            return picPath + ".png";
        } else {
            return picPath + "_" + index + ".png";
        }
    }

    public void setAutoNo(int autoNo) {
        this.autoNo = autoNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileNo(String fileNo) {
        this.fileNo = fileNo;
    }

    public void setHasVoice(boolean hasVoice) {
        this.hasVoice = hasVoice;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public void setVoicePath(String voicePath) {
        this.voicePath = voicePath;
    }

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }

//    public void setPicPath(String picPath) {
//        this.picPath = picPath;
//    }

    public static Exhibit getExhibitInfo(Cursor cursor) {
        Exhibit exhibit = new Exhibit();
        exhibit.setAutoNo(cursor.getInt(0));
        exhibit.setFileNo(cursor.getString(1));
//        exhibit.setPicPath(cursor.getString(2));
        exhibit.setName(cursor.getString(4));
        exhibit.setFloor(cursor.getInt(5));
        exhibit.setUnitNo(cursor.getInt(6));
        exhibit.setX(cursor.getDouble(7));
        exhibit.setY(cursor.getDouble(8));
        exhibit.setType(cursor.getInt(9));
//        exhibit.setType_ar(cursor.getInt(10));
        exhibit.setType_auto(cursor.getInt(11));
        exhibit.setX1(cursor.getDouble(12));
        exhibit.setY1(cursor.getDouble(13));
        return exhibit;
    }


    @Override
    public String toString() {
        return "Exhibit{" +
                "autoNo=" + autoNo +
                ", name='" + name + '\'' +
                ", fileNo='" + fileNo + '\'' +
                ", hasVoice=" + hasVoice +
                ", pagePath='" + pagePath + '\'' +
                ", voicePath='" + voicePath + '\'' +
                ", picCount=" + picCount +
                ", type=" + type +
                ", type_ar=" + type_ar +
                ", type_auto=" + type_auto +
                ", floor=" + floor +
                ", UnitNo=" + UnitNo +
                ", unitName='" + unitName + '\'' +
                ", picPath='" + picPath + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", x1=" + x1 +
                ", y1=" + y1 +
                '}';
    }
}
