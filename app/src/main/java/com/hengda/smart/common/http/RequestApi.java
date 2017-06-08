package com.hengda.smart.common.http;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.hengda.smart.common.dialog.DialogCenter;
import com.hengda.smart.common.update.CheckResponse;
import com.hengda.smart.common.util.AppUtil;
import com.hengda.smart.common.util.NetUtil;
import com.hengda.smart.gxkjg.R;
import com.hengda.smart.gxkjg.app.HdConstants;
import com.hengda.smart.gxkjg.app.HdAppConfig;
import com.hengda.smart.gxkjg.app.HdApplication;
import com.hengda.smart.gxkjg.entity.AcInfo;
import com.hengda.smart.gxkjg.entity.AddModel;
import com.hengda.smart.gxkjg.entity.Content;
import com.hengda.smart.gxkjg.entity.Device;
import com.hengda.smart.gxkjg.entity.GroupNo;
import com.hengda.smart.gxkjg.entity.Heart;
import com.hengda.smart.gxkjg.entity.LikeNum;
import com.hengda.smart.gxkjg.entity.Message;
import com.hengda.smart.gxkjg.entity.Pstatus;
import com.hengda.smart.gxkjg.entity.Ques;
import com.hengda.smart.gxkjg.entity.TextClass;
import com.hengda.smart.gxkjg.group.GroupInfo;
import com.hengda.smart.gxkjg.group.MemberInfo;
import com.hengda.smart.gxkjg.ui.adult.PeerMainAdultActivity;
import com.hengda.smart.gxkjg.ui.child.PeerMainChildActivity;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 16:17
 * 邮箱：tailyou@163.com
 * 描述：
 */
public class RequestApi {

    private static final int DEFAULT_TIMEOUT = 5;
    private Retrofit retrofit;
    private IRequest iRequest;
    private Context context;
    private volatile static RequestApi instance;
    private static Hashtable<String, RequestApi> mRequestApiTable;

    static {
        mRequestApiTable = new Hashtable<>();
    }

    private Call<TextClass> call;


    /**
     * 私有构造函数
     *
     * @param baseHttpUrl
     */
    private RequestApi(String baseHttpUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                .baseUrl(baseHttpUrl)
                .build();
        iRequest = retrofit.create(IRequest.class);

    }

    private RequestApi(String baseHttpUrl, Context context) {
        this.context = context;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())

                .baseUrl(baseHttpUrl)
                .build();
        iRequest = retrofit.create(IRequest.class);

    }

    /**
     * 获取单例
     *
     * @return
     */
    public static RequestApi getInstance() {
        String baseHttpUrl = getBaseHttpUrl();
        instance = mRequestApiTable.get(baseHttpUrl);
        if (instance == null) {
            synchronized (RequestApi.class) {
                if (instance == null) {
                    instance = new RequestApi(baseHttpUrl);
                    mRequestApiTable.put(baseHttpUrl, instance);
                }
            }
        }
        return instance;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static RequestApi getInstance(String baseHttpUrl) {
        instance = mRequestApiTable.get(baseHttpUrl);
        if (instance == null) {
            synchronized (RequestApi.class) {
                if (instance == null) {
                    instance = new RequestApi(baseHttpUrl);
                    mRequestApiTable.put(baseHttpUrl, instance);
                }
            }
        }
        return instance;
    }

    /**
     * 根据内外网，获取网络请求基地址
     *
     * @return
     */
    public static String getBaseHttpUrl() {


/*
* static NSString *requestAdress = @"http://101.200.234.14:8091/gxkjg/index.php?g=mapi&";
static NSString *neiWangJiaoHouDiZhi = @"http://192.168.10.27/index.php?g=mapi&";
*
* */
        String baseHttpUrl;
        String wifiSSID = NetUtil.getWifiSSID(HdApplication.mContext);
        if (NetUtil.isWifi(HdApplication.mContext) &&
                wifiSSID.contains(HdConstants.DEFAULT_SSID)) {
            baseHttpUrl = "http://192.168.2.239/";
        } else {
            //馆方外网
//            baseHttpUrl = "http://116.10.199.106:65521/";
            baseHttpUrl = HdConstants.DEFAULT_IP_PORT_E;
//            baseHttpUrl = "http://192.168.10.20/gxkjg/";
        }


        //外网地址
        return baseHttpUrl;
    }

    /**
     * 检查App版本更新
     *
     * @param subscriber
     */
    public void checkVersion(Subscriber<CheckResponse> subscriber) {
        Observable<CheckResponse> observable = iRequest.checkVersion(HdConstants.APP_KEY,
                HdConstants.APP_SECRET, 1, AppUtil.getVersionCode(HdApplication.mContext),
                HdAppConfig.getDeviceNo());
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 请求机器号
     */
    public void getDeviceNo() {
        Call<Device> call = iRequest.getResult(1);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, retrofit2.Response<Device> response) {
                if (response.isSuccessful()) {
                    Device device = response.body();
                    Log.e("tag", "onResponse: " + device.toString());
                    String deviceNo = device.getData();
                    HdAppConfig.setDeviceNo(deviceNo);
                }

            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

            }
        });
    }

    /*
    * 退出群组
    * */
    public void existGroup(String user_login, Context context) {
if (HdAppConfig.getLanguage().equals("CHINESE")){
    call = iRequest.existGroup(user_login,1);
}else {
    call = iRequest.existGroup(user_login, 2);
}

        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {

            }
        });
    }

    /*
    * 上传展品浏览次数
    * */
    public void getCount(String exhibit_id) {
        Call<TextClass> call = iRequest.getCount(exhibit_id);
        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {
                if (response.isSuccessful()) {
                    TextClass device = response.body();
                    HdAppConfig.setText(response.body().getMsg());

                }
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {

            }
        });


    }

    /*
    * 点赞与取消点赞
    *
    * */
    public void setAppreciate(String user_login, String exhibit_id, Context context, ImageView heartPlay) {
        Call<TextClass> call;
        if (HdAppConfig.getLanguage().equals("CHINESE")){

        call = iRequest.getAppreciate(HdAppConfig.getDeviceNo(), exhibit_id, 1);
        }else {
             call = iRequest.getAppreciate(HdAppConfig.getDeviceNo(), exhibit_id,2);
        }
        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {
                if (response.isSuccessful()) {
                    RequestApi.getInstance().lookAppreciate(new Subscriber<Pstatus>() {
                        @Override
                        public void onCompleted() {
                            if (HdAppConfig.getAHeart() == 1) {
                                heartPlay.setImageResource(R.mipmap.click_zan);
                            } else {
                                heartPlay.setImageResource(R.mipmap.click_yes);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Pstatus pstatus) {
                            int status = pstatus.getPstatus();
                            HdAppConfig.setAHeart(status);

                        }
                    }, HdAppConfig.getDeviceNo(), exhibit_id);
                }
                TextClass device = response.body();
                Toast.makeText(context, device.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {

            }
        });
    }


    /*
    *
    * 查询点赞状态
    * */
    public void lookAppreciate(Subscriber<Pstatus> subscriber, String user_login, String exhibit_id) {
        Observable<Response<Pstatus>> observable = iRequest.lookAppreciate(exhibit_id, user_login);
        doSubscribe(subscriber, observable);
    }



 /*
 * 用户留言
 *
 * deviceno	string
机器号

content	string
留言内容

contact 可选	string
联系方式
 * */

    public void makeMessage(String deviceno, String content, Context context) {
        Call<Message> call;
        if (HdAppConfig.getLanguage().equals("CHINESE")){
            call = iRequest.makeMessage(deviceno, content,1);
        }else {

            call = iRequest.makeMessage(HdAppConfig.getDeviceNo(), content,2);
        }
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, retrofit2.Response<Message> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    /*位置上传
    * */

    public void putPositonInfo(String deviceno, int app_kind, int auto_num, Context context) {
        Call<Message> call = iRequest.putPostionInfo(HdAppConfig.getDeviceNo(), app_kind, auto_num);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, retrofit2.Response<Message> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(context, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }


    /*
    * 资源播放次数
    * */
    public void playCount(String exhibit_id, String type) {
        Call<TextClass> call = iRequest.playCountInfo(exhibit_id, type);
        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {
                TextClass Model = response.body();
                Log.i("info", "playcont------" + Model.toString());
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {
                Log.i("info", "------执行失败 playcont: " + t);
            }
        });
    }

    /*
    * 做分享次数上传
    *
    * */
    public void getShareCount(String exhibit_id) {
        Call<TextClass> call = iRequest.shareCount(exhibit_id);
        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {
                TextClass Share = response.body();
                Log.i("info", "shareCount: ---------- " + Share.toString());
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {
                Log.i("info", "shareCount: ---------- " + t);
            }
        });
    }

    /*
    *
    * */
    public void makeComment(String token, String user_login, String content, String exhibit_id, Context context) {

        if (HdAppConfig.getLanguage().equals("CHINESE")){
            call = iRequest.makeComment(token, user_login, content, exhibit_id,1);
        }else {
            call = iRequest.makeComment(token, user_login, content, exhibit_id,2);
        }
        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {

                if (response.isSuccessful()) {
                    TextClass Comment = response.body();
                    Toast.makeText(context, Comment.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {

            }
        });
    }

    /*
    * 做心跳上传
    * */
    public void postHeart(String deviceno, int app_kind, Context context) {

        Call<Heart> call = iRequest.postHeart(deviceno, app_kind);
        call.enqueue(new Callback<Heart>() {
            @Override
            public void onResponse(Call<Heart> call, retrofit2.Response<Heart> response) {
                if (response.isSuccessful()) {
                    Heart heart = response.body();
               Log.i("heart","heart:------------------------"+heart.getMsg()+heart.getStatus());

                }
            }

            @Override
            public void onFailure(Call<Heart> call, Throwable t) {

            }
        });

    }


    /*
    * 查看用户评论Observle
    * */

    public void lookComment(Subscriber<List<Content>> subscriber, String id) {
        Observable<Response<List<Content>>> observable = iRequest.lookComment(id, HdAppConfig.getLanguage());
        doSubscribe(subscriber, observable);
    }
/*
* 查看点赞次数
*
* */

    public void preCount(Subscriber<LikeNum> subscriber, String exhibit_id) {
        Observable<Response<LikeNum>> observable = iRequest.preCount(exhibit_id);
        doSubscribe(subscriber, observable);
    }

    /*
    * 获取群组号
    * */
    public void fetchNo(Subscriber<GroupNo> subscriber, String user_login) {
        Observable<Response<GroupNo>> observable = iRequest.fetchNo(user_login);
        doSubscribe(subscriber, observable);
    }


    public void getThemeInfo(Subscriber<List<AcInfo>> subscriber) {
        Observable<Response<List<AcInfo>>> observable = iRequest.getThemeInfo();
        doSubscribe(subscriber, observable);
    }

    /**
     * 创建组群
     *
     * @param
     * @param user_login
     */
    public void createGroup(Subscriber<GroupInfo> subscriber, String user_login, String g_name) {
        Log.i("info", HdAppConfig.getLanguage() + "lan------------");
        Observable<Response<GroupInfo>> observable = iRequest.createGroup(user_login, g_name, HdAppConfig.getLanguage());
        doSubscribe(subscriber, observable);
    }


    /*
    * Call的加入群组
    *
    * */
    public void joinGroup(String user_login, int group, Context context) {
        Call<AddModel> call;

        if (HdAppConfig.getLanguage().equals("CHINESE")){
            call  = iRequest.joinGroup(user_login, group,1);
        }else {
            call  = iRequest.joinGroup(user_login, group,2);
        }

        call.enqueue(new Callback<AddModel>() {
            @Override
            public void onResponse(Call<AddModel> call, retrofit2.Response<AddModel> response) {
                if (response.isSuccessful()) {
                    AddModel device = response.body();
                    Toast.makeText(context, device.getMsg(), Toast.LENGTH_SHORT).show();
                    if (device.getData() != null) {
                        String groupId = (String) device.getData().getGroup();
                        HdAppConfig.setAGroupNo(Integer.parseInt(groupId));
                        HdAppConfig.setGroupName(device.getData().getG_name());
                        if (HdAppConfig.getUserType().equals("adult")) {
                            Intent intent = new Intent(context, PeerMainAdultActivity.class);
                            context.startActivity(intent);
                        } else if (HdAppConfig.getUserType().equals("child")) {
                            Intent intent = new Intent(context, PeerMainChildActivity.class);
                            context.startActivity(intent);
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<AddModel> call, Throwable t) {

            }
        });
    }


    /**
     * 查询组员
     *
     * @param subscriber
     * @param user_login
     */
    public void queryGroupMember(Subscriber<List<MemberInfo>> subscriber, String user_login) {
        Observable<Response<List<MemberInfo>>> observable = iRequest.queryGroupMember
                (user_login);
        doSubscribe(subscriber, observable);
    }



    /**
     * 查询组员位置
     *
     * @param subscriber
     * @param user_login
     */
    public void queryPositionMember(Subscriber<MemberInfo> subscriber, String user_login) {
        Observable<Response<MemberInfo>> observable = iRequest.queryPositionMember(user_login);
        doSubscribe(subscriber, observable);
    }

/*
* 问卷调查
* */

    public void QueryQues(Subscriber<Ques> subscriber, int language) {
        Observable<Response<Ques>> observable = iRequest.FetchQues(language);
        doSubscribe(subscriber, observable);
    }


    public void modifyNickname(String user_login, String nicename) {
        Call<Device> call = iRequest.modifyName(user_login, nicename);
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, retrofit2.Response<Device> response) {
                if (response.isSuccessful()) {
                    HdAppConfig.setText(response.body().getMsg());


                    DialogCenter.hideDialog();

                    HdAppConfig.setNickname(nicename);


                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

            }
        });
    }


    /**
     * 订阅（抽取公共部分）
     *
     * @param subscriber
     * @param observable
     */
    private <T> void doSubscribe(Subscriber<T> subscriber, Observable<Response<T>> observable) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (TextUtils.equals(HdConstants.HTTP_STATE, response.getCode())) {
                        return response.getInfo();
                    } else {
                        throw new RequestException(response.getMsg());
                    }
                })
                .subscribe(subscriber);
    }


    private <T> void doSubscribeAll(Subscriber<T> subscriber, Observable<Response<T>> observable) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(response -> {
                    if (TextUtils.equals(HdConstants.HTTP_STATE, response.getCode())) {
                        Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                        return response.getInfo();
                    } else {
                        Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                        throw new RequestException(response.getMsg());
                    }
                })
                .subscribe(subscriber);
    }

    public void ARPlayCount(String num, Context context) {
        Call<TextClass> call = iRequest.ARplay(num);
        call.enqueue(new Callback<TextClass>() {
            @Override
            public void onResponse(Call<TextClass> call, retrofit2.Response<TextClass> response) {
                if (response.isSuccessful()) {
//                    CommonUtil.showToast(context, response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<TextClass> call, Throwable t) {

            }
        });
    }
}




