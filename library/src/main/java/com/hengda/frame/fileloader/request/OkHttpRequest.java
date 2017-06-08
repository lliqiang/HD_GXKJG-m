package com.hengda.frame.fileloader.request;


import com.hengda.frame.fileloader.callback.Callback;
import com.hengda.frame.fileloader.exception.Exceptions;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by shiwei on 2017/1/11.
 */
public abstract class OkHttpRequest {
    protected String url;
    protected Object tag;
    protected int id;

    protected Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(String url, Object tag,int id)
    {
        this.url = url;
        this.tag = tag;
        this.id = id ;
        if (url == null) {
            Exceptions.illegalArgument("url can not be null.");
        }
        initBuilder();
    }

    /**
     * 初始化一些基本参数 url , tag
     */
    private void initBuilder()
    {
        builder.url(url).tag(tag);
    }

    protected abstract RequestBody buildRequestBody();

    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        return requestBody;
    }

    protected abstract Request buildRequest(RequestBody requestBody);

    public RequestCall build()
    {
        return new RequestCall(this);
    }

    public Request generateRequest(Callback callback) {
        RequestBody requestBody = buildRequestBody();
        RequestBody wrappedRequestBody = wrapRequestBody(requestBody, callback);
        Request request = buildRequest(wrappedRequestBody);
        return request;
    }

    public int getId()
    {
        return id  ;
    }

}