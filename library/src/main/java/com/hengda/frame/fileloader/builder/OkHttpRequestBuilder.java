package com.hengda.frame.fileloader.builder;


import com.hengda.frame.fileloader.request.RequestCall;

/**
 * Created by shiwei on 2017/1/11.
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    protected String url;
    protected Object tag;
    protected int id;

    public T id(int id) {
        this.id = id;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public abstract RequestCall build();
}