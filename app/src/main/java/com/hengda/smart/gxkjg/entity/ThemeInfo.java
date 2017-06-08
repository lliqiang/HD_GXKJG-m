package com.hengda.smart.gxkjg.entity;

import java.util.List;

/**
 * Created by lenovo on 2016/12/26.
 */

public class ThemeInfo {

    /**
     * status : 1
     * data : [{"id":"1","title":"第三届广西青少年科学节","holdy":"2015","holdm":"9","qrimg":"http://192.168.10.20/gxkjg/data/upload/activity/20161223/585cccb6084ec.png","addtime":"2016-12-23 15:05:27"},{"id":"2","title":"第三届全区乡村少年宫素质教育技能竞赛","holdy":"2015","holdm":"10","qrimg":"http://192.168.10.20/gxkjg/data/upload/activity/20161223/585ccc9373602.png","addtime":"2016-12-23 15:04:53"},{"id":"3","title":"第六届全国青少年科学影像节前期宣传\u2014\u2014影像盛典","holdy":"2015","holdm":"11","qrimg":"http://192.168.10.20/gxkjg/data/upload/activity/20161223/585ccc747dfdc.png","addtime":"2016-12-23 15:04:22"}]
     * msg :
     */

    private int status;
    private String msg;
    /**
     * id : 1
     * title : 第三届广西青少年科学节
     * holdy : 2015
     * holdm : 9
     * qrimg : http://192.168.10.20/gxkjg/data/upload/activity/20161223/585cccb6084ec.png
     * addtime : 2016-12-23 15:05:27
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String title;
        private String holdy;
        private String holdm;
        private String qrimg;
        private String addtime;

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
}
