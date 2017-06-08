package com.hengda.smart.gxkjg.entity;

/**
 * Created by lenovo on 2017/2/4.
 */

public class AddModel {

    /**
     * status : 1
     * data : {"group":"1","g_name":"辽宁参观团"}
     * msg : 加入成功
     */

    private int status;
    /**
     * group : 1
     * g_name : 辽宁参观团
     */

    private DataBean data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String group;
        private String g_name;

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getG_name() {
            return g_name;
        }

        public void setG_name(String g_name) {
            this.g_name = g_name;
        }
    }
}
