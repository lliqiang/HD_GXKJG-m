package com.hengda.smart.gxkjg.app;


import android.app.Activity;
import android.widget.TextView;

import com.hengda.smart.common.util.SDCardUtil;
import com.hengda.smart.common.util.SharedPrefUtil;
import com.hengda.smart.common.widget.HDialogBuilder;
import com.hengda.smart.common.widget.HProgressDialog;
import com.hengda.smart.gxkjg.R;

import java.io.File;


/**
 * 作者：Tailyou
 * 时间：2016/1/11 10:05
 * 邮箱：tailyou@163.com
 * 描述：恒达App配置文件
 */
public class HdAppConfig {
    static HProgressDialog progressDialog;
    static HDialogBuilder hDialogBuilder;
    static TextView textView;

    private static SharedPrefUtil appConfigShare = new SharedPrefUtil(HdApplication.mContext,
            HdConstants.SHARED_PREF_NAME);

    //    SharedPref字段

    public static final String AUTO_PLAY = "AUTO_PLAY";//自动讲解
    public static final String LANGUAGE = "LANGUAGE";//语种
    public static final String USER_TYPE = "USER_TYPE";//用户类型
    public static final String GROUP_NO = "GROUP_NO";//我的同伴-群组号
    public static final String AGROUP_NO = "AGROUP_NO";//我的同伴-群组号
    public static final String NICKNAME = "NICKNAME";//我的同伴-昵称
    public static final String JOIN_GROUP_TIME = "JOIN_GROUP_TIME";//加入群组的时间
    public static final String PASSWORD = "PASSWORD";//管理员密码
    public static final String RSSI = "RSSI";//RSSI门限
    public static final String AUTO_FLAG = "AUTO_FLAG";//自动讲解：0关闭，1开启
    public static final String SMART_SERVICE = "SMART_SERVICE";//智慧服务：0关闭，1开启
    public static final String AUTO_MODE = "AUTO_MODE";//讲解方式：0隔一，1连续
    public static final String STC_MODE = "STC_MODE";//报警方式：0直接报警，1间接报警
    public static final String RECEIVE_NO_MODE = "RECEIVE_NO_MODE";//收号方式：0蓝牙，1RFID，2混合
    public static final String SCREEN_MODE = "SCREEN_MODE";//节能模式：0关闭，1开启
    public static final String POWER_MODE = "POWER_MODE";//关机权限：0禁止，1允许
    public static final String POWER_PERMI = "POWER_PERMI";//禁止关机下是否获取到关机权限：0无，1有
    public static final String IS_SHOW_MSG_DIALOG = "IS_SHOW_MSG_DIALOG";//是否显示收到消息弹框
    public static final String IP_PORT = "IP_PORT";//服务器IP和端口
    public static final String MSG_ID = "MSG_ID";//聊天消息唯一标识
    public static final String HANDLER = "TEXT";
    public static final String GROUP_NM = "GROUP_NM";
    public static final String GROUP_NMC = "GROUP_NMC";
    public static final String GroupText = "GroupText";
    public static final String PICRES = "PICRES";

    public static final String IS_RES = "IS_RES";
    public static final String EA_IS_RES = "EA_IS_RES";
    public static final String EC_IS_RES = "EC_IS_RES";
    public static final String CC_IS_RES = "CC_IS_RES";
    public static final String APRECIATE = "APRECIATE";
    //判断是否在下载
    public static final String IS_LOADING = "IS_LOADING";
    public static final String Path = getDefaultFileDir() + "/" + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/" + "0065/0065.png";
    private static final String DEVICE = "DEVICE";
    private static final String AHEART = "HEART";
    private static final String CHEART = "HEART";







    public static void setStatePreciate(boolean isRes) {
        appConfigShare.setPrefBoolean(APRECIATE, isRes);

    }

    public static boolean isStatePreciate() {
        return appConfigShare.getPrefBoolean(APRECIATE, false);
    }

    public static void setPicRes(boolean isRes) {
        appConfigShare.setPrefBoolean(PICRES, isRes);

    }

    public static boolean isPicLoad() {
        return appConfigShare.getPrefBoolean(PICRES, false);
    }


    public static void setAHeart(int flag) {
        appConfigShare.setPrefInt(AHEART, flag);

    }

    public static int getAHeart() {
        return appConfigShare.getPrefInt(CHEART);
    }

    public static void setCHeart(int flag) {
        appConfigShare.setPrefInt(AHEART, flag);

    }

    public static int getCHeart() {
        return appConfigShare.getPrefInt(CHEART);
    }
    public static void setResLoad(boolean isRes) {
        appConfigShare.setPrefBoolean(IS_RES, isRes);

    }

    public static boolean isResLoad() {
        return appConfigShare.getPrefBoolean(IS_RES, false);
    }

    //判断中文的儿童下载
    public static void setCCResLoad(boolean isRes) {
        appConfigShare.setPrefBoolean(CC_IS_RES, isRes);

    }

    public static boolean isCCResLoad() {
        return appConfigShare.getPrefBoolean(CC_IS_RES, false);
    }

//判断英文的成人下载
    public static void setEAResLoad(boolean isRes) {
        appConfigShare.setPrefBoolean(EA_IS_RES, isRes);

    }

    public static boolean isEAResLoad() {
        return appConfigShare.getPrefBoolean(EA_IS_RES, false);
    }
    //判断英文的儿童下载
    public static void setECResLoad(boolean isRes) {
        appConfigShare.setPrefBoolean(EC_IS_RES, isRes);

    }

    public static boolean isECResLoad() {
        return appConfigShare.getPrefBoolean(EC_IS_RES, false);
    }


    public static void setText(String handler) {
        appConfigShare.setPrefString(HANDLER, handler);
    }

    public static String getHandler() {
        return appConfigShare.getPrefString(HANDLER);
    }

    public static void setGroupText(String groupText) {
        appConfigShare.setPrefString(GroupText, groupText);
    }

    public static String getGroupText() {
        return appConfigShare.getPrefString(GroupText);
    }


    public static void setLanguage(String language) {
        appConfigShare.setPrefString(LANGUAGE, language);
    }

    public static void setAutoPlay(boolean flag) {
        appConfigShare.setPrefBoolean(AUTO_PLAY, flag);

    }

    public static boolean getAutoPlay() {
        return appConfigShare.getPrefBoolean(AUTO_PLAY, false);
    }

    public static String getLanguage() {
        return appConfigShare.getPrefString(LANGUAGE, HdConstants.LANG_DEFAULT);
    }

    public static void setUserType(String userType) {
        appConfigShare.setPrefString(USER_TYPE, userType);
    }

    public static String getUserType() {
        return appConfigShare.getPrefString(USER_TYPE, HdConstants.ADULT);
    }

    public static void setGroupNo(int groupNo) {
        appConfigShare.setPrefInt(GROUP_NO, groupNo);
    }

    public static int getGroupNo() {
        return appConfigShare.getPrefInt(GROUP_NO);
    }


    public static void setAGroupNo(int groupNo) {
        appConfigShare.setPrefInt(AGROUP_NO, groupNo);
    }

    public static int getAGroupNo() {
        return appConfigShare.getPrefInt(AGROUP_NO);
    }




    public static void setJoinGroupTime(long joinGroupTime) {
        appConfigShare.setPrefLong(JOIN_GROUP_TIME, joinGroupTime);
    }

    public static long getJoinGroupTime() {
        return appConfigShare.getPrefLong(JOIN_GROUP_TIME);
    }

    public static void setNickname(String nickname) {
        appConfigShare.setPrefString(NICKNAME, nickname);
    }

    public static String getNickname() {
        return appConfigShare.getPrefString(NICKNAME, getDeviceNo());
    }

    public static void setIsLoading(boolean isLoading) {
        appConfigShare.setPrefBoolean(IS_LOADING, isLoading);
    }

    public static boolean isLoading() {
        return appConfigShare.getPrefBoolean(IS_LOADING, false);
    }

    public static void setDeviceNo(String deviceNo) {
        appConfigShare.setPrefString(DEVICE, deviceNo);
    }

    public static String getDeviceNo() {
        return appConfigShare.getPrefString(DEVICE);
    }


//    public static void setDeviceNo(String deviceNo) {
//        appConfigShare.setPrefString(DEVICE, deviceNo);
//    }
//
//    public static String getDeviceNo() {
//        return appConfigShare.getPrefString(DEVICE);
//    }



    public static void setPassword(String password) {
        appConfigShare.setPrefString(PASSWORD, password);
    }

    public static String getPassword() {
        return appConfigShare.getPrefString(PASSWORD, HdConstants.DEFAULT_PWD);
    }

    public static void setRssi(int rssi) {
        appConfigShare.setPrefInt(RSSI, rssi);
    }

    public static int getRssi() {
        return appConfigShare.getPrefInt(RSSI, HdConstants.BLE_RSSI_THRESHOLD);
    }

    public static void setSmartService(int smartService) {
        appConfigShare.setPrefInt(SMART_SERVICE, smartService);
    }

    public static int getSmartService() {
        return appConfigShare.getPrefInt(SMART_SERVICE, 1);
    }

    public static void setAutoFlag(int autoFlag) {
        appConfigShare.setPrefInt(AUTO_FLAG, autoFlag);
    }

    public static int getAutoFlag() {
        return appConfigShare.getPrefInt(AUTO_FLAG, 1);
    }

    public static void setAutoMode(int autoMode) {
        appConfigShare.setPrefInt(AUTO_MODE, autoMode);
    }

    public static int getAutoMode() {
        return appConfigShare.getPrefInt(AUTO_MODE, 0);
    }

    public static void setSTCMode(int flag) {
        appConfigShare.setPrefInt(STC_MODE, flag);
    }

    public static int getSTCMode() {
        return appConfigShare.getPrefInt(STC_MODE, 1);
    }

    public static void setReceiveNoMode(int receiveNoMode) {
        appConfigShare.setPrefInt(RECEIVE_NO_MODE, receiveNoMode);
    }

    public static int getReceiveNoMode() {
        return appConfigShare.getPrefInt(RECEIVE_NO_MODE, 1);
    }

    public static void setScreenMode(int flag) {
        appConfigShare.setPrefInt(SCREEN_MODE, flag);
    }

    public static int getScreenMode() {
        return appConfigShare.getPrefInt(SCREEN_MODE, 1);
    }

    public static void setPowerMode(int flag) {
        appConfigShare.setPrefInt(POWER_MODE, flag);
    }

    public static int getPowerMode() {
        return appConfigShare.getPrefInt(POWER_MODE, 1);
    }

    public static void setPowerPermi(int flag) {
        appConfigShare.setPrefInt(POWER_PERMI, flag);
    }

    public static int getPowerPermi() {
        return appConfigShare.getPrefInt(POWER_PERMI, 0);
    }

    public static void setIsShowMsgDialog(boolean isShowMsgDialog) {
        appConfigShare.setPrefBoolean(IS_SHOW_MSG_DIALOG, isShowMsgDialog);
    }

    public static boolean isShowMsgDialog() {
        return appConfigShare.getPrefBoolean(IS_SHOW_MSG_DIALOG, true);
    }

    public static void setDefaultInnerNetIp(String ipPort) {
        appConfigShare.setPrefString(IP_PORT, ipPort);
    }

    public static String getDefaultInnerNetIp() {
        return appConfigShare.getPrefString(IP_PORT, HdConstants.DEFAULT_IP_PORT_I);
    }

    public static String getMsgId() {
        return appConfigShare.getPrefString(MSG_ID, "");
    }

    public static void setMsgId(String msgId) {
        appConfigShare.setPrefString(MSG_ID, msgId);
    }

    public static void setNtcReaded(String ntcUrl, boolean value) {
        appConfigShare.setPrefBoolean(ntcUrl, value);
    }

    public static boolean isNtcReaded(String ntcUrl) {
        return appConfigShare.getPrefBoolean(ntcUrl, true);
    }

    //    获取默认文件存储目录
    public static String getDefaultFileDir() {
        return SDCardUtil.getSDCardPath() + ".GuangXiTech_Res/";
    }

    public static boolean getResExist() {
        File file = new File(Path);
        return file.exists();
    }

    public static String getGroupName() {
        return appConfigShare.getPrefString(GROUP_NM, "");
    }

    public static void setCGroupName(String groupnm) {
        appConfigShare.setPrefString(GROUP_NMC, groupnm);
    }

    public static String getCGroupName() {
        return appConfigShare.getPrefString(GROUP_NMC, "");
    }

    public static void setGroupName(String groupnm) {
        appConfigShare.setPrefString(GROUP_NM, groupnm);
    }


    //获取儿童文件夹路径
    public static String getcCFilePath() {
        return getDefaultFileDir() + "/" + getLanguage() + "/child/";
    }

    //成人文件夹路径
    public static String getcAFilePath() {
        return getDefaultFileDir() + getLanguage() + "/adult/";
    }


    public static boolean isFontExist() {
//        String path=HdAppConfig.getDefaultFileDir()+HdAppConfig.getLanguage()+"/"+"child/"+""
        File file = new File(getcCFilePath() + "HappyFont.ttf");
        return file.exists();
    }
//判断数据库是否存在
public static boolean isDataBaseExist() {

    File file = new File(getDbFilePath());
    return file.exists();
}
    //获取成人地图的路径
    public static String getcAMapFilePath() {
        return getDefaultFileDir() + getLanguage() + "/adult/map";
    }

    //    获取数据库文件路径
    public static String getDbFilePath() {

        return getDefaultFileDir() + HdConstants.DB_FILE_NAME;
    }

    //    获取地图文件路径
    public static String getMapFilePath() {
        return getDefaultFileDir() + getLanguage() + "/" + HdAppConfig.getUserType() + "/map";
    }
//http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES%2FCHINESE%2Fadult%2Fmap.zip
//        获取地图文件路径

    public static void clear() {
        appConfigShare.clearPreference();
    }

    //判断地图资源是否存在
    public static boolean isMapResExist() {
        String path = HdAppConfig.getDefaultFileDir() + HdAppConfig.getLanguage() + "/" + HdAppConfig.getUserType() + "/map"+"/4";
        File file = new File(path);
        return file.exists();
    }
    //判断缩略图是否存在
    public static boolean isPictureExist(String fileNO) {
        String path = HdAppConfig.getDefaultFileDir() +"picture" + "/" + fileNO;
        File file = new File(path);
        return file.exists();
    }

    //    获取默认文件存储目录
    public static String getPicturePath(String fileNo) {
        return getDefaultFileDir()+"picture/"+fileNo+"/"+fileNo+"_s_icon.png";
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
}
