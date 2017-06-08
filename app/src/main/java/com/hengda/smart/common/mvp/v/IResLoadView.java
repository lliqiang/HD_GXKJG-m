package com.hengda.smart.common.mvp.v;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 13:01
 * 邮箱：tailyou@163.com
 * 描述：
 */
public interface IResLoadView {

    /**
     * 已存在资源
     */
    void resYes();

    /**
     * 显示下载对话框
     */
    void showLoadingDialog();

    /**
     * 更新下载进度
     */
    void updateLoadProgress(int fileType, long progress, long total);

    /**
     * 隐藏下载对话框
     */
    void hideLoadingDialog();

    /**
     * 下载失败
     */
    void loadFailed();

}
