package com.hengda.smart.common.http;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 17:31
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class RequestException extends RuntimeException {
    public RequestException(String detailMessage) {
        super(detailMessage);
    }
}
