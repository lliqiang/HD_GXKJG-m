package com.hengda.smart.common.mvp.v;

import com.hengda.smart.gxkjg.entity.Exhibit;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 16:16
 * 邮箱：tailyou@163.com
 * 描述：
 */
public interface IExhibitView {

    /**
     * 已存在展品资源
     *
     * @param exhibit
     */
    void exhibitYes(Exhibit exhibit);

    /**
     * 显示下载对话框
     */
    void showLoadingDialog();

    /**
     * 隐藏下载对话框
     */
    void hideLoadingDialog();

    /**
     * 下载失败
     */
    void loadFailed();

}
