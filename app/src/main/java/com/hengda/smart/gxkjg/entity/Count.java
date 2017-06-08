package com.hengda.smart.gxkjg.entity;

/**
 * Created by lenovo on 2016/12/24.
 */

public class Count {

    /**
     * status : 1
     * data : {"like_num":"235"}
     * msg :
     */

    private int status;
    /**
     * like_num : 235
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
        private String like_num;

        public String getLike_num() {
            return like_num;
        }

        public void setLike_num(String like_num) {
            this.like_num = like_num;
        }
    }
}
