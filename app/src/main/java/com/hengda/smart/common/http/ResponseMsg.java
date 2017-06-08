package com.hengda.smart.common.http;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 16:47
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class ResponseMsg<T> {
    private String status;
    private T msg;


    public String getCode() {
        return status;
    }

    public T getMsg() {
        return msg;
    }



}
