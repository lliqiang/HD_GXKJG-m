package com.hengda.smart.common.dialog;

import android.content.Context;
import android.view.View;

import com.hengda.smart.common.widget.HDialogBuilder;
import com.hengda.smart.common.widget.HProgressDialog;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/26 19:03
 * 邮箱：tailyou@163.com
 * 描述：Dialog中心
 */
public class DialogCenter {

    static HProgressDialog progressDialog;
    static HDialogBuilder hDialogBuilder;

    /**
     * 显示ProgressDialog
     *
     * @param message
     * @param cancelable
     */
    public static void showProgressDialog(Context context, int message, boolean cancelable) {
        hideProgressDialog();
        progressDialog = new HProgressDialog(context);
        progressDialog
                .setTypeface(HdAppConfig.getUserType().equals(HdConstants.ADULT) ?
                        HdApplication.typefaceGxa : HdApplication.typefaceGxc)
                .message(message)
                .tweenAnim(R.mipmap.progress_roate, R.anim.progress_rotate)
                .outsideCancelable(cancelable)
                .cancelable(cancelable)
                .show();
    }

    /**
     * 显示ProgressDialog
     *
     * @param message
     * @param cancelable
     */
    public static void showProgressDialog(Context context, String message, boolean cancelable) {
        hideProgressDialog();
        progressDialog = new HProgressDialog(context);
        progressDialog
                .setTypeface(HdAppConfig.getUserType().equals(HdConstants.ADULT) ?
                        HdApplication.typefaceGxa : HdApplication.typefaceGxc)
                .message(message)
                .tweenAnim(R.mipmap.progress_roate, R.anim.progress_rotate)
                .outsideCancelable(cancelable)
                .cancelable(cancelable)
                .show();
    }

    /**
     * 隐藏ProgressDialog
     */
    public static void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 显示Dialog-CustomView
     *
     * @param context
     * @param view
     * @param dialogClickListener
     * @param txt
     */
    public static void showDialog(Context context,
                                  View view,
                                  DialogClickListener dialogClickListener,
                                  int... txt) {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(context);
        hDialogBuilder
                .withIcon(R.mipmap.lauch)
                .setTypeface(HdAppConfig.getUserType().equals(HdConstants.ADULT) ?
                        HdApplication.typefaceGxa : HdApplication.typefaceGxc)
                .title(txt[0])
                .setCustomView(view)
                .pBtnText(txt[1])
                .pBtnClickListener(v -> dialogClickListener.p())
                .cancelable(false);
        if (txt.length == 3) {
            hDialogBuilder
                    .nBtnText(txt[2])
                    .nBtnClickListener(v -> dialogClickListener.n());
        }
        hDialogBuilder.show();
    }

    /**
     * 显示Dialog-CustomView
     *
     * @param context
     * @param view
     * @param dialogClickListener
     * @param txt
     */
    public static void showDialog(Context context,
                                  View view,
                                  DialogClickListener dialogClickListener,
                                  String... txt) {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(context);
        hDialogBuilder
                .withIcon(R.mipmap.lauch)
                .setTypeface(HdAppConfig.getUserType().equals(HdConstants.ADULT) ?
                        HdApplication.typefaceGxa : HdApplication.typefaceGxc)
                .title(txt[0])
                .setCustomView(view)
                .pBtnText(txt[1])
                .pBtnClickListener(v -> dialogClickListener.p())
                .cancelable(false);
        if (txt.length == 3) {
            hDialogBuilder
                    .nBtnText(txt[2])
                    .nBtnClickListener(v -> dialogClickListener.n());
        }
        hDialogBuilder.show();
    }

    /**
     * 显示Dialog-Message
     *
     * @param context
     * @param dialogClickListener
     * @param txt
     */
    public static void showDialog(Context context,
                                  DialogClickListener dialogClickListener,
                                  int... txt) {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(context);
        hDialogBuilder
                .withIcon(R.mipmap.lauch)
                .setTypeface(HdAppConfig.getUserType().equals(HdConstants.ADULT) ?
                        HdApplication.typefaceGxa : HdApplication.typefaceGxc)
                .title(txt[0])
                .message(txt[1])
                .pBtnText(txt[2])
                .pBtnClickListener(v -> dialogClickListener.p())
                .cancelable(false);
        if (txt.length == 4) {
            hDialogBuilder
                    .nBtnText(txt[3])
                    .nBtnClickListener(v -> dialogClickListener.n());
        }
        hDialogBuilder.show();
    }

    /**
     * 显示Dialog-Message
     *
     * @param context
     * @param dialogClickListener
     * @param txt
     */
    public static void showDialog(Context context,
                                  DialogClickListener dialogClickListener,
                                  String... txt) {
        hideDialog();
        hDialogBuilder = new HDialogBuilder(context);
        hDialogBuilder
                .withIcon(R.mipmap.lauch)
                .setTypeface(HdAppConfig.getUserType().equals(HdConstants.ADULT) ?
                        HdApplication.typefaceGxa : HdApplication.typefaceGxc)
                .title(txt[0])
                .message(txt[1])
                .pBtnText(txt[2])
                .pBtnClickListener(v -> dialogClickListener.p())
                .cancelable(false);
        if (txt.length == 4) {
            hDialogBuilder
                    .nBtnText(txt[3])
                    .nBtnClickListener(v -> dialogClickListener.n());
        }
        hDialogBuilder.show();
    }

    /**
     * 隐藏Dialog
     */
    public static void hideDialog() {
        if (hDialogBuilder != null) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }

}
