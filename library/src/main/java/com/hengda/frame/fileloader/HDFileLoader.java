package com.hengda.frame.fileloader;

import android.content.Context;

import com.hengda.frame.fileloader.builder.GetBuilder;
import com.hengda.frame.fileloader.callback.Callback;
import com.hengda.frame.fileloader.db.FileBean;
import com.hengda.frame.fileloader.db.FileInfo;
import com.hengda.frame.fileloader.request.RequestCall;
import com.hengda.frame.fileloader.util.Platform;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by shiwei on 2017/1/11.
 */

public class HDFileLoader {
    public static final String TAG = HDFileLoader.class.getSimpleName();
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static HDFileLoader mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;
    //-------db-----------
    private FileBean mFileBean;
    private FileInfo mFileInfo;

    public HDFileLoader(Context context, OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mPlatform = Platform.get();
        mFileInfo = FileInfo.getInstance(context);
    }

    public static HDFileLoader initClient(Context context, OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (HDFileLoader.class) {
                if (mInstance == null) {
                    mInstance = new HDFileLoader(context, okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static HDFileLoader getInstance() {
        return mInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public void execute(final RequestCall requestCall, Callback callback) {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }
                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }
                    Object o = finalCallback.parseNetworkResponse(response, id);
                    if (null != o) {
                        sendSuccessResultCallback(o, finalCallback, id);
                        if (null != mFileBean)
                            insert(mFileBean);
                    }
                } catch (Exception e) {
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                    if (response.body() != null)
                        response.body().close();
                }
            }
        });
    }

    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.completed(object, id);
            }
        });
    }

    public void insert(FileBean bean) {
        mFileInfo.insert(bean);
    }

    public List<FileBean> select(String name) {
        return mFileInfo.select(name);
    }

    public void update(FileBean bean) {
        mFileInfo.update(bean);
    }

    public void delete(String name) {
        mFileInfo.delete(name);
    }

    public void setFileBean(FileBean fileBean) {
        mFileBean = fileBean;
    }
}
