package com.hengda.smart.common.mvp.p;

import android.app.Activity;

import com.hengda.smart.common.http.FileCallback;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.entity.Exhibit;
import com.hengda.smart.common.mvp.m.ExhibitModel;
import com.hengda.smart.common.mvp.v.IExhibitView;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 16:16
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class ExhibitPresenter {
    private IExhibitView iExhibitView;

    public ExhibitPresenter(IExhibitView iExhibitView) {
        this.iExhibitView = iExhibitView;
    }

    /**
     * 检查展品是否存在，是则渲染数据，否则单段下载展品后渲染数据
     *
     * @param context
     * @param exhibit
     */
    public void checkExhibit(Activity context, Exhibit exhibit) {
        if (ExhibitModel.isExhibitExist(exhibit)) {
            iExhibitView.exhibitYes(exhibit);
        } else {
            if (NetUtil.isConnected(context)) {
                iExhibitView.showLoadingDialog();
                ExhibitModel.loadExhibit(exhibit.getFileNo(), new FileCallback(HdAppConfig
                        .getDefaultFileDir() + HdAppConfig.getLanguage(), "EXHIBIT.zip") {
                    @Override
                    public void progress(long progress, long total) {
                    }

                    @Override
                    public void onSuccess(File file) {
                        context.runOnUiThread(() -> {
                            iExhibitView.hideLoadingDialog();
                            iExhibitView.exhibitYes(exhibit);
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        context.runOnUiThread(() -> {
                            iExhibitView.hideLoadingDialog();
                            iExhibitView.loadFailed();
                        });
                    }
                });
            } else {
                CommonUtil.showToast(context, R.string.net_not_available);
            }
        }
    }

    /**
     * 检查展品是否存在，是则渲染数据，否则单段下载展品后渲染数据
     *
     * @param context
     * @param autoNo
     */
    public void checkExhibit(Activity context, int autoNo) {
        Exhibit exhibit = ExhibitModel.loadExhibitByAutoNo(autoNo);
        checkExhibit(context, exhibit);
    }
    /**
     * 根据文件号从服务器单段下载展品
     *
     * @param fileNo
     * @param fileCallback
     */


}
