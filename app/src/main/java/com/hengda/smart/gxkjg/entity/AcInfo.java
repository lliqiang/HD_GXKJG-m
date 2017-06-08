package com.hengda.smart.gxkjg.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2016/12/27.
 */

public class AcInfo implements Serializable{

    /**
     * id : 2
     * title : 2015年10月第三届全区乡村少年宫素质教育技能竞赛微信页面
     * holdy : 2015
     * holdm : 10
     * qrimg : http://192.168.10.20/gxkjg/data/upload/activity/20161223/585ccc9373602.png
     * addtime : 2016-12-27 10:56:34
     */

    private String id;
    private String title;
    private String holdy;
    private String holdm;
    private String qrimg;
    private String addtime;
private int isLast;

    public int getIsLast() {
        return isLast;
    }

    public void setIsLast(int isLast) {
        this.isLast = isLast;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHoldy() {
        return holdy;
    }

    public void setHoldy(String holdy) {
        this.holdy = holdy;
    }

    public String getHoldm() {
        return holdm;
    }

    public void setHoldm(String holdm) {
        this.holdm = holdm;
    }

    public String getQrimg() {
        return qrimg;
    }

    public void setQrimg(String qrimg) {
        this.qrimg = qrimg;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
