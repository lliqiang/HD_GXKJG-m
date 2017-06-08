package com.hengda.smart.common.http;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/26 18:46
 * 邮箱：tailyou@163.com
 * 描述：Retrofit文件下载接口
 */
public interface FileService {

    /**
     * 单段下载展品
     *
     * @param lang
     * @param fileNo
     * @return
     */

    @GET("HD-GXKJG-RES%2F{lang}/{fileNo}.zip")
    Call<ResponseBody> loadExhibit(@Path("lang") String lang, @Path("fileNo") String fileNo);

    /**
     * 下载数据库、资源
     *
     * @param fileName
     * @return
     */

    @GET("HD-GXKJG-RES%2F{fileName}")
    Call<ResponseBody> loadFile(@Path("fileName") String fileName);

}
