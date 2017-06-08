package com.hengda.smart.gxkjg.app;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/27 17:19
 * 邮箱：tailyou@163.com
 * 描述：全局常量
 */
public class HdConstants {

    //    App更新相关常量
    public static String APP_KEY = "860927979df074e2f13a07a9eb2eb665";
    public static String APP_SECRET = "c3d5903f6f2eb7878003422b832457de";
    public static String APP_UPDATE_URL = "http://101.200.234.14/APPCloud/";

    //    阿里云资源下载
//    public static String RES_LOAD_URL = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs" +
//            ".com/GuangXiTech_Res/";
    public static String RES_LOAD_URL = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/";
    public static String DB_LOAD_URL = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/";

    public static String DB_URL="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/DATABASE.zip";
    public static String RES_URL="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/CHINESE/CHINESE.zip";
    public static String ADULT_URL="http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES/"+HdAppConfig.getLanguage()+"/"+HdAppConfig.getUserType()+".zip";
//

    //    AR相关常量
    public static String AR_LICENCE_KEY =
            "AU4Hp4v/////AAAAGWrQfjhosELOjagCG9CbNkRfSQG9l6pD1t1cn+MZ" +
                    "/+62e5Dy0UxDLhXLPVOMo9FmLWxKnRzwzUOHWQD2" +
                    "/3QwDEaz2LqVLX3Sfpt7izreAV4MrgyK1o0fXtqRYll+wPLzMqJUszPCx8N1kQedi" +
                    "/hCw1NccI23ldhdzcTzctAvMUIwQeBAwGr" +
                    "/YHRgTf4dP1Bdvov7nGeyHmWMJMC9FUJydfB5yfAEqfJw87" +
                    "+4KsM0NGKyewyFZmNoRSHsWtODF1IXzt59OdQ" +
                    "+uTJA6PgPQIGMwVG0QUqcj6aNG5H4yLRBS4S6G1sJokhEzpm7GVC4BswnKYix8FR7gry+xt" +
                    "+t1LuxSbmkjcJQw38qywVwqjwFqVbM";

    //    语言
    public static final String LANG_CHINESE = "CHINESE";
    public static final String LANG_ENGLISH = "ENGLISH";
    public static final String LANG_DEFAULT = "CHINESE";

    //    用户类型
    public static final String ADULT = "adult";
    public static final String CHILD = "child";
    //    SharedPrf配置文件名称
    public static final String SHARED_PREF_NAME = "HD_GXT_PREF";

    //    数据库文件名称
    public static final String DB_FILE_NAME = "filemanage.s3db";

    //    蓝牙RSSI门限
    public static final int BLE_RSSI_THRESHOLD = -69;

    //    请求成功状态码
    public static final String HTTP_STATUS_SUCCEED = "000";
    //请求状态
    public static final String HTTP_STATE = "1";

    //    默认管理员密码
    public static final String DEFAULT_PWD = "9999";
    //    默认设备号
    public static final String DEFAULT_DEVICE_NO = "AG10000000000";

    //    默认IP和端口，默认端口80可以省略
    //    馆方内网-默认网络请求服务器地址
    public static final String DEFAULT_IP_PORT_I = "http://192.168.2.239/";
    //    馆方外网-默认网络请求服务器地址
    public static final String DEFAULT_IP_PORT_E = "http://116.10.199.106:65521/";

//    //    馆方WiFi SSID
    public static final String DEFAULT_SSID = "GXKJG-FREE";

    //馆方内网下载地址                                   http://192.168.10.27/12345/HD-GXKJG-RES%2F
    public static final String DEFAULT_RES_INNER = "http://192.168.2.239/12345/HD-GXKJG-RES/";
    //馆方外网下载地址
//    public static final String DEFAULT_RES_OUTTER = "http://hengdawb-res.oss-cn-hangzhou.aliyuncs.com/HD-GXKJG-RES%2F";
    public static final String DEFAULT_RES_OUTTER = "http://116.10.199.106:65521/12345/HD-GXKJG-RES/";
    //   http://116.10.199.106:65521/12345

    public static final int LOADING_DB = 11;
    public static final int LOADING_RES = 12;

}
