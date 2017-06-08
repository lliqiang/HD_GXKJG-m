package com.hengda.smart.common.http;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hengda.smart.common.mvp.m.ILoadListener;
import com.hengda.smart.common.util.CommonUtil;
import com.hengda.smart.common.util.FileUtils;
import com.hengda.smart.common.util.HFileCallBack;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.common.widget.HDialogBuilder;
import com.hengda.smart.common.widget.HProgressDialog;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;



public class HResourceUtil {

    static HProgressDialog progressDialog;
    static HDialogBuilder hDialogBuilder;
    static TextView textView;
    public static  String wifiSSID = NetUtil.getWifiSSID(HdApplication.mContext);

    /**
     * 判断数据库是否存在
     *
     * @return
     */
    public static boolean isDbExist() {
        File file = new File(HdAppConfig.getDbFilePath());
        return file.exists();
    }

    /**
     * 判断当前语种资源是否存在
     *
     * @return
     */
    public static boolean isResExist() {
        File file = new File(HdAppConfig.getMapFilePath());
        return file.exists();
    }
    /*
    *
    * 修改下载的资源显示*/

    public static void loadResShow(final Activity context,
                                 final ILoadListener iLoadListener) {
        String url;


        if (NetUtil.isWifi(HdApplication.mContext) &&
                wifiSSID.contains(HdConstants.DEFAULT_SSID)){

            url=HdConstants.DEFAULT_RES_INNER+HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType()+".zip";
        }else {

             url=HdConstants.DEFAULT_RES_OUTTER+HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType()+".zip";
        }

        String destFileDir = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType();
        String destFileName = "EXHIBIT.zip";
//        String destFileName = "DB.zip";

        FileUtils.makeDirs(destFileDir);
        OkHttpUtils.getInstance().cancelTag("LOAD_RES");

        OkHttpUtils
                .get()
                .tag("LOAD_RES")
                .url(url)
                .build()
                .execute(new HFileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total) {
                        textView.setText(context.getString(R.string.res_download) +
                                String.format("(%s/%s)",
                                        CommonUtil.getFormatSize(progress * total),
                                        CommonUtil.getFormatSize(total)));
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        iLoadListener.onLoadFailed();
                    }

                    @Override
                    public void onResponse(File response) {
                        iLoadListener.onLoadSucceed();
                    }
                });
    }




    /**
     * 显示加载 ProgressDialog，圆形进度
     *
     * @param context
     */
    public static void showDownloadProgressDialog(Activity context, String msg) {
        hideDownloadProgressDialog();
        progressDialog = new HProgressDialog(context);
        progressDialog
                .message(msg)
                .tweenAnim(R.mipmap.progress_roate, R.anim.progress_rotate)
                .outsideCancelable(false)
                .cancelable(false)
                .show();
    }


    /**
     * 隐藏 ProgressDialog
     */
    public static void hideDownloadProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    /**
     * 显示下载 Dialog，文字进度
     *
     * @param context
     */
    public static void showDownloadDialog(Activity context) {
        hideDownloadDialog();
        hDialogBuilder = new HDialogBuilder(context);
        textView = (TextView) context.getLayoutInflater().inflate(R.layout
                .layout_hd_dialog_custom_tv, null);
        textView.setText(context.getString(R.string.res_download) + "...");
        hDialogBuilder
                .withIcon(R.mipmap.lauch)
                .title(context.getString(R.string.res_download))
                .setCustomView(textView)
                .nBtnText(context.getString(R.string.cancel))
                .nBtnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HdAppConfig.setIsLoading(false);
                        OkHttpUtils.getInstance().cancelTag("LOAD_RES");
                        hideDownloadDialog();
                    }
                })
                .cancelable(false)
                .show();
    }


    /**
     * 隐藏下载Dialog，带下载进度
     */
    public static void hideDownloadDialog() {
        if (hDialogBuilder != null) {
            hDialogBuilder.dismiss();
            hDialogBuilder = null;
        }
    }


 //单包下载地图资源

    public static void loadMapRes(final Activity context,
                                  final ILoadListener iLoadListener) {


        String url;
        if (NetUtil.isConnected(context)){
            if (NetUtil.isWifi(HdApplication.mContext) &&
                    wifiSSID.contains(HdConstants.DEFAULT_SSID)){

                url=HdConstants.DEFAULT_RES_INNER+HdAppConfig.getLanguage()+"/" +HdAppConfig.getUserType()+ "/map.zip";
            }else {
//
                url=HdConstants.DEFAULT_RES_OUTTER+HdAppConfig.getLanguage()+"/" +HdAppConfig.getUserType()+ "/map.zip";;
            }


            String destFileDir = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType()+"/map";
            String destFileName = "EXHIBIT.zip";
            FileUtils.makeDirs(destFileDir);
            OkHttpUtils.getInstance().cancelTag("LOAD_RES");

            OkHttpUtils
                    .get()
                    .tag("LOAD_RES")
                    .url(url)
                    .build()
                    .execute(new HFileCallBack(destFileDir, destFileName) {
                        @Override
                        public void inProgress(float progress, long total) {
                            textView.setText(context.getString(R.string.res_download) +
                                    String.format("(%s/%s)",
                                            CommonUtil.getFormatSize(progress * total),
                                            CommonUtil.getFormatSize(total)));
                        }

                        @Override
                        public void onError(okhttp3.Call call, Exception e) {
                            iLoadListener.onLoadFailed();
                        }

                        @Override
                        public void onResponse(File response) {
                            iLoadListener.onLoadSucceed();
                        }
                    });

        }else {
            Toast.makeText(context, context.getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
        }


    }



    //下载数据库

    public static void loadDataBaseRes(final Activity context,
                                  final ILoadListener iLoadListener) {
        String url;
        if (NetUtil.isConnected(context)){
            if (NetUtil.isWifi(HdApplication.mContext) &&
                    wifiSSID.contains(HdConstants.DEFAULT_SSID)){
                url=HdConstants.DEFAULT_RES_INNER+"/DATABASE.zip";
            }else {
            url=HdConstants.DEFAULT_RES_OUTTER+"/DATABASE.zip";
            }
            String destFileDir = HdAppConfig.getDefaultFileDir();
            String destFileName = "EXHIBIT.zip";
            FileUtils.makeDirs(destFileDir);
            OkHttpUtils.getInstance().cancelTag("LOAD_RES");

            OkHttpUtils
                    .get()
                    .tag("LOAD_RES")
                    .url(url)
                    .build()
                    .execute(new HFileCallBack(destFileDir, destFileName) {
                        @Override
                        public void inProgress(float progress, long total) {
                            textView.setText(context.getString(R.string.res_download) +
                                    String.format("(%s/%s)",
                                            CommonUtil.getFormatSize(progress * total),
                                            CommonUtil.getFormatSize(total)));
                        }

                        @Override
                        public void onError(okhttp3.Call call, Exception e) {
                            iLoadListener.onLoadFailed();
                        }

                        @Override
                        public void onResponse(File response) {
                            iLoadListener.onLoadSucceed();
                        }
                    });

        }else {
            Toast.makeText(context, context.getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
        }


    }





    public static void loadSingleExist(final Activity context,
                                   final ILoadListener iLoadListener,String fileNo) {
        String url;
        if (NetUtil.isConnected(context)){
            if (NetUtil.isWifi(HdApplication.mContext) &&
                    wifiSSID.contains(HdConstants.DEFAULT_SSID)){
                url=HdConstants.DEFAULT_RES_INNER+HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + fileNo + ".zip";
            }
            else {
                url=HdConstants.DEFAULT_RES_OUTTER+ HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + fileNo + ".zip";
            }
            String destFileDir = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType();
            String destFileName = "EXHIBIT.zip";
            FileUtils.makeDirs(destFileDir);
            OkHttpUtils.getInstance().cancelTag("LOAD_RES");
            OkHttpUtils
                    .get()
                    .tag("LOAD_RES")
                    .url(url)
                    .build()
                    .execute(new HFileCallBack(destFileDir, destFileName) {
                        @Override
                        public void inProgress(float progress, long total) {
                            textView.setText(context.getString(R.string.res_download) +
                                    String.format("(%s/%s)",
                                            CommonUtil.getFormatSize(progress * total),
                                            CommonUtil.getFormatSize(total)));
                        }

                        @Override
                        public void onError(okhttp3.Call call, Exception e) {
                            iLoadListener.onLoadFailed();
                        }

                        @Override
                        public void onResponse(File response) {
                            iLoadListener.onLoadSucceed();
                        }
                    });
        }else {
            Toast.makeText(context, context.getString(R.string.net_not_available), Toast.LENGTH_SHORT).show();
        }




    }


    /**
     * 单包下载視頻
     *http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES%2FAR_Vedio%2F803.zip
     * @param fileNo
     */

    public static void loadVedio(final Activity context,
                                       final ILoadListener iLoadListener,String fileNo) {

        String path=null;
        if (NetUtil.isWifi(HdApplication.mContext)&&wifiSSID.contains(HdConstants.DEFAULT_SSID)){
            path=HdConstants.DEFAULT_RES_INNER+"AR_Vedio/"+fileNo+".zip";
        }else {

            path=HdConstants.DEFAULT_RES_OUTTER+ "AR_Vedio/"+fileNo+".zip";
        }

        String destFileDir = HdAppConfig.getDefaultFileDir() + "AR_Vedio/"+fileNo;
        String destFileName = "EXHIB.zip";
        FileUtils.makeDirs(destFileDir);
        OkHttpUtils.getInstance().cancelTag("LOAD_RES");
        OkHttpUtils
                .get()
                .tag("LOAD_RES")
                .url(path)
                .build()
                .execute(new HFileCallBack(destFileDir, destFileName) {
                    @Override
                    public void inProgress(float progress, long total) {
                        textView.setText(context.getString(R.string.res_download) +
                                String.format("(%s/%s)",
                                        CommonUtil.getFormatSize(progress * total),
                                        CommonUtil.getFormatSize(total)));
                    }

                    @Override
                    public void onError(okhttp3.Call call, Exception e) {
                        iLoadListener.onLoadFailed();
                    }

                    @Override
                    public void onResponse(File response) {
                        iLoadListener.onLoadSucceed();

                    }
                });
    }





    //判断单包展品资源是否存在
    public static boolean isExhibitExist(String fileNo) {
        File file = new File(HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" +HdAppConfig.getUserType()+"/"+
                fileNo);
        return file.exists();
    } //判断視頻资源是否存在
    public static boolean isVedioExist(String fileNo) {
        File file = new File(HdAppConfig.getDefaultFileDir()+"AR_Vedio/"+
                fileNo+"/"+fileNo+".mp4");
        return file.exists();
    }
    //判断缩略图资源是否存在
    public static boolean isPicRes() {
        File file = new File(HdAppConfig.getDefaultFileDir()+ "picture/"+"0282");
        return file.exists();
    }

}
