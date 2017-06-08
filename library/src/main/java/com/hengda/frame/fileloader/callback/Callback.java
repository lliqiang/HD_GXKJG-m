package com.hengda.frame.fileloader.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shiwei on 2017/1/11.
 * baseic callback
 */
public abstract class Callback<T> {

    /**
     * UI Thread
     *
     * @param request
     * @param id
     */
    public void pending(Request request, int id) {
    }

    /**
     * UI Thread
     *
     * @param total
     * @param id
     */
    public void connected(long total, int id) {

    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress, long total, int id) {
    }

    /**
     * UI Thread
     */
    public void justPlay(String fileDir) {
    }

    /**
     * @param response
     * @return
     */
    public boolean validateReponse(Response response, int id) {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;

    public abstract void onError(Call call, Exception e, int id);

    public abstract void completed(T response, int id);


    public static Callback CALLBACK_DEFAULT = new Callback() {

        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void completed(Object response, int id) {

        }
    };

}