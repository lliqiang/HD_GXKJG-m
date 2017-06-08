package com.hengda.smart.gxkjg.entity;

import java.util.List;

/**
 * Created by lenovo on 2016/12/19.
 */

public class TextClass {

    /**
     * status : 1
     * data : []
     * msg : 操作成功
     */

    private int status;
    private String msg;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TextClass{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
