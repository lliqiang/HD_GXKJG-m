package com.hengda.frame.fileloader.download;

import android.content.Context;
import android.view.View;

import com.hengda.frame.fileloader.HDFileLoader;
import com.hengda.frame.fileloader.callback.FileCallBack;
import com.hengda.frame.fileloader.request.RequestCall;
import com.hengda.frame.fileloader.util.UnzipUtil;

import java.io.File;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by shiwei on 2017/1/19.
 */

public class Downloader {

    private static Builder builder;

    public static Builder getBuilder() {
        return builder;
    }

    public static class Builder {
        private String mUrl;
        private DownloadDialog mDownloadDialog;
        private RequestCall call;
        private FileCallBack callBack;
        private String mSrc, mDes, mName, mTitle = " ";
        private boolean mCheck, mShowCancelBtn,mUnzip=true;
        private DownloadCallBackListener mListener;
        private boolean connected;

        public Builder(Context context) {
            mDownloadDialog = new DownloadDialog(context);
        }

        public static Builder create(Context context) {
            builder = new Builder(context);
            return builder;
        }

        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        public Builder src(String src) {
            this.mSrc = src;
            return this;
        }

        public Builder des(String des) {
            this.mDes = des;
            return this;
        }

        public Builder name(String name) {
            this.mName = name;
            return this;
        }

        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder ckeck(boolean check) {
            this.mCheck = check;
            return this;
        }
        public Builder unzip(boolean zip) {
            this.mUnzip = zip;
            return this;
        }

        public Builder listener(DownloadCallBackListener listener) {
            this.mListener = listener;
            return this;
        }

        public Builder showCancel(boolean cancel) {
            this.mShowCancelBtn = cancel;
            return this;
        }

        public void start() {
            call.execute(callBack);
        }

        private void cancel() {
            call.cancel();
        }

        public Builder build() {
            call = HDFileLoader.get().url(mUrl).build();
            callBack = new FileCallBack(mSrc, mDes, mName, mCheck) {
                @Override
                public void pending(Request request, int id) {
                    super.pending(request, id);
                    //just keep
                }
                @Override
                public void connected(long total, int id) {
                    connected = true;
                    mDownloadDialog.setTitle(mTitle)
                            .showCancleButton(mShowCancelBtn)
                            .outsideCancelable(false)
                            .cancelListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    call.cancel();
                                    mDownloadDialog.dismiss();
                                }
                            })
                            .show();
                    mDownloadDialog.setSize((int) total);
                    mDownloadDialog.process(0);
                }

                @Override
                public void inProgress(float progress, long total, int id) {
                    mDownloadDialog.process((int) (progress * 100));
                }

                @Override
                public void completed(File file, int id) {
                    //这里应该异步解压
                    if (mUnzip)
                        unzipFile(file, mDes);
                    /****************/
                    mDownloadDialog.dismiss();
                    if (null != mListener) {
                        mListener.completed(file);
                    }
                }

                @Override
                public void justPlay(String fileDir) {
                    if (null != mListener) {
                        mListener.justPlay(fileDir);
                    }
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    if (null != mDownloadDialog && connected)
                        mDownloadDialog.dismiss();
                    if (null != mListener) {
                        mListener.error(e);
                    }
                }
            };
            return this;
        }
    }

    public static void unzipFile(File file, String path) {
        try {
            UnzipUtil.unZipFolder(file.getAbsolutePath(), path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            file.delete();
        }
    }
}
