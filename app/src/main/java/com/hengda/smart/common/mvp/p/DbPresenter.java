package com.hengda.smart.common.mvp.p;

import android.app.Activity;
import android.widget.Toast;

import com.hengda.smart.common.http.FileCallback;
import com.hengda.smart.common.mvp.m.DbModel;
import com.hengda.smart.common.mvp.v.IDbLoadView;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 15:00
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class DbPresenter {
    private IDbLoadView iDbLoadView;

    public DbPresenter(IDbLoadView iDbLoadView) {
        this.iDbLoadView = iDbLoadView;
    }

    /**
     * 检查是否有数据库
     */
    public void checkDb(Activity context) {
        if (DbModel.isDbExist()) {
            iDbLoadView.dbYes();
        } else {
            if (NetUtil.isConnected(context)) {
                iDbLoadView.showLoadingDialog();
                DbModel.loadDb(new FileCallback(HdAppConfig.getDefaultFileDir(), "DB.zip") {
                    @Override
                    public void progress(long progress, long total) {

                    }

                    @Override
                    public void onSuccess(File file) {
                        context.runOnUiThread(() -> {
                            iDbLoadView.hideLoadingDialog();
                            iDbLoadView.dbYes();
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        context.runOnUiThread(() -> {
                            iDbLoadView.hideLoadingDialog();
                            iDbLoadView.loadFailed();
                        });
                    }
                });
            } else {
                Toast.makeText(context, R.string.net_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
