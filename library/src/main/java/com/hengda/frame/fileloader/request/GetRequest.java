package com.hengda.frame.fileloader.request;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by shiwei on 2017/1/11.
 */
public class GetRequest extends OkHttpRequest {
    public GetRequest(String url, Object tag, int id) {
        super(url, tag,id);
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        return null;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody)
    {
        return builder.get().build();
    }

}