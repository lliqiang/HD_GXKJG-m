package com.hengda.smart.common.mvp.v;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 13:01
 * 邮箱：tailyou@163.com
 * 描述：
 */
public interface IDbLoadView {

    /**
     * 已存在数据库
     */
    void dbYes();

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
