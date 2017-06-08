package com.hengda.smart.common.mvp.p;

import android.app.Activity;

import com.hengda.smart.common.http.FileCallback;
import com.hengda.smart.common.mvp.m.DbModel;
import com.hengda.smart.common.mvp.m.ResModel;
import com.hengda.smart.common.mvp.v.IResLoadView;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 13:14
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class ResPresenter {
    private IResLoadView iResLoadView;

    public ResPresenter(IResLoadView iResLoadView) {
        this.iResLoadView = iResLoadView;
    }

    /**
     * 检查是否有资源
     *
     * @param context
     */
    public void checkRes(Activity context) {
        if (DbModel.isDbExist()) {
            if (ResModel.isResExist()) {
                iResLoadView.resYes();
            } else {
                loadRes(context);
            }
        } else {
            loadDbRes(context);
        }
    }

    /**
     * 下载资源
     *
     * @param context
     */
    private void loadRes(Activity context) {
        if (NetUtil.isConnected(context)) {
            iResLoadView.showLoadingDialog();
            ResModel.loadRes(new FileCallback(HdAppConfig.getDefaultFileDir() +
                    HdAppConfig.getLanguage(), "RES.zip") {
                @Override
                public void progress(long progress, long total) {
                    iResLoadView.updateLoadProgress(HdConstants.LOADING_RES, progress, total);
                }

                @Override
                public void onSuccess(File file) {
                    context.runOnUiThread(() -> {
                        iResLoadView.hideLoadingDialog();
                        iResLoadView.resYes();
                    });
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    context.runOnUiThread(() -> {
                        iResLoadView.hideLoadingDialog();
                        iResLoadView.loadFailed();
                    });
                }
            });
        } else {
            CommonUtil.showToast(context, R.string.net_not_available);
        }
    }

    /**
     * 下载数据库+资源
     *
     * @param context
     */
    public void loadDbRes(Activity context) {
        if (NetUtil.isConnected(context)) {
            if (HdAppConfig.getResExist()){
                iResLoadView.showLoadingDialog();
                DbModel.loadDb(new FileCallback(HdAppConfig.getDefaultFileDir(), "DB.zip") {
                    @Override
                    public void progress(long progress, long total) {
                        iResLoadView.updateLoadProgress(HdConstants.LOADING_DB, progress, total);
                    }

                    @Override
                    public void onSuccess(File file) {
                        ResModel.loadRes(new FileCallback(HdAppConfig.getDefaultFileDir() +
                                HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType(), "RES.zip") {
                            @Override
                            public void progress(long progress, long total) {
                                iResLoadView.updateLoadProgress(HdConstants.LOADING_RES, progress, total);
                            }

                            @Override
                            public void onSuccess(File file) {
                                context.runOnUiThread(() -> {
                                    iResLoadView.hideLoadingDialog();
                                    iResLoadView.resYes();
                                });
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                context.runOnUiThread(() -> {
                                    iResLoadView.hideLoadingDialog();
                                    iResLoadView.loadFailed();
                                });
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        context.runOnUiThread(() -> {
                            iResLoadView.hideLoadingDialog();
                            iResLoadView.loadFailed();
                        });
                    }
                });
            }


        } else {
            CommonUtil.showToast(context, R.string.net_not_available);
        }
    }

    /**
     * 取消下载资源
     */
    public void cancelResLoad() {
        ResModel.cancelLoad();
    }

}
