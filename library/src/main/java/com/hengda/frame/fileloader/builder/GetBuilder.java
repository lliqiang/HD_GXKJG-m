package com.hengda.frame.fileloader.builder;


import com.hengda.frame.fileloader.request.GetRequest;
import com.hengda.frame.fileloader.request.RequestCall;

/**
 * Created by shiwei on 2017/1/11.
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> {
    @Override
    public RequestCall build() {
        return new GetRequest(url, tag, id).build();
    }
}