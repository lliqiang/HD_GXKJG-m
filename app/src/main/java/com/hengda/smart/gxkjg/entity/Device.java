package com.hengda.smart.gxkjg.entity;

/**
 * Created by lenovo on 2016/12/12.
 */

public class Device {

    /**
     * status : 1
     * data : AND1000000013
     * msg : 操作成功
     */

    private int status;
    private String data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Device{" +
                "status=" + status +
                ", data='" + data + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
