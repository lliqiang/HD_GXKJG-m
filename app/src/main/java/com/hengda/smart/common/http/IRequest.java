package com.hengda.smart.common.http;


import com.hengda.smart.common.update.CheckResponse;
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

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

import retrofit2.http.POST;

import retrofit2.http.Query;
import rx.Observable;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 16:17
 * 邮箱：tailyou@163.com
 * 描述：
 */
public interface IRequest {

    /**
     * 检查App版本更新
     *
     * @param appKey
     * @param appSecret
     * @param appKind
     * @param versionCode
     * @param deviceId
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?g=&m=Api&a=checkVersion")
    Observable<CheckResponse> checkVersion(@Field("appKey") String appKey,
                                           @Field("appSecret") String appSecret,
                                           @Field("appKind") int appKind,
                                           @Field("versionCode") int versionCode,
                                           @Field("deviceId") String deviceId);

    /**
     * 请求机器号
     *
     * @param app_kind
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=positions&a=request_deviceno")
    Call<Device> getResult(@Field("app_kind") int app_kind);
/*
* 展品浏览次数上传
*
* */
    @GET("index.php?g=mapi&m=exhibit&a=look_inc")
    Call<TextClass> getCount(@Query("exhibit_id") String exhibit_id);


    /*
    * 点赞与取消点赞上传
    *
    * */
    @GET("index.php?g=mapi&m=exhibit&a=praise")
    Call<TextClass> getAppreciate(@Query("user_login") String user_login,
                                  @Query("exhibit_id") String exhibit_id,
                                  @Query("language") int language);




    /*
    * 查询点赞状态
    * */
    @GET("index.php?g=mapi&m=exhibit&a=pstatus")
    Observable<Response<Pstatus>> lookAppreciate(@Query("exhibit_id")String exhibit_id,
                                           @Query("user_login") String user_login);

   /*
   *
   * 获取点赞次数
   * */
    @GET("index.php?g=mapi&m=exhibit&a=pra_num")
    Observable<Response<LikeNum>> preCount(@Query("exhibit_id")String exhibit_id);


    /*
   *
   * 获取群组号
   * */
    @GET("index.php?g=mapi&m=exhibit&a=pra_num")
    Observable<Response<GroupNo>> fetchNo(@Query("user_login")String user_login);





    /*
*
*资源播放次数
*
* */
    @GET("index.php?g=mapi&m=exhibit&a=play_inc")
    Call<TextClass> playCountInfo(@Query("exhibit_id") String exhibit_id,
                                  @Query("type")String type);


    /*
    *
    * 做资源分享次数上传
    *
    * */
    @GET("index.php?g=mapi&m=exhibit&a=share_inc")
    Call<TextClass> shareCount(@Query("exhibit_id") String exhibit_id);


    /*
    *
    * 作评论
    * */

    @FormUrlEncoded
    @POST("index.php?g=mapi&m=exhibit&a=index")
   Call<TextClass> makeComment(
            @Field("token") String token,
            @Field("user_login")String user_login,
            @Field("content") String content,
            @Field("exhibit_id") String exhibit_id,
            @Field("language") int language
    );

    /*
    * 做心跳上传
    * */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=positions&a=heartbeat")
    Call<Heart> postHeart(
            @Field("deviceno")String deviceno,
            @Field("app_kind") int app_kind

    );




    /*
    * 查看用户评论Observle
    * */

    @GET("index.php?g=mapi&m=exhibit&a=comments")
    Observable<Response<List<Content>>> lookComment(@Query("id") String id,@Query("language")String language);



    /*
    * 修改昵称
    * */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=group&a=edit_nicename")
    Call<Device> modifyName(
            @Field("user_login") String user_login,
            @Field("nicename")String nicename
            );


    /*
    * 加入群组
    * */
//      @FormUrlEncoded
//    @POST("index.php?g=mapi&m=group&a=add_group")
//    Observable<Response<Device>> joinGroup(
//            @Field("user_login") String user_login,
//            @Field("group") int group);
/*
* 加入群组
* */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=group&a=add_group")
    Call<AddModel> joinGroup(
            @Field("user_login") String user_login,
         @Field("group") int group,
         @Field("language") int language);




    /**
     * 创建组群
     *
     * @param user_login
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=group&a=request_group")
    Observable<Response<GroupInfo>> createGroup(@Field("user_login") String user_login,
                                                 @Field("g_name")String g_name,
                                                 @Field("language")String language );


/*
* 获取主题活动信息
*
* */
    @GET("index.php?g=mapi&m=activity&a=index")
    Observable<Response<List<AcInfo>>>getThemeInfo();

/*
* 退出群组
* */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=group&a=del_group")
    Call<TextClass> existGroup(
            @Field("user_login") String user_login,
            @Field("language") int language);

    /**
     * 查询组员
     *
     * @param user_login
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=group&a=select_group")
    Observable<Response<List<MemberInfo>>> queryGroupMember(@Field("user_login") String user_login);


    /**
     * 查询组员位置
     *
     * @param user_login
     * @return
     */
    @FormUrlEncoded
    @POST("/index.php?g=mapi&m=group&a=select_friend")
    Observable<Response<MemberInfo>> queryPositionMember(@Field("user_login") String user_login);




    /*
    * 作用户留言
    *
    * */
    @FormUrlEncoded
    @POST("index.php?g=mapi&m=lmessage&a=send")
    Call<Message> makeMessage(@Field("deviceno") String deviceno,
                              @Field("content")String content,
                              @Field("language")int language);

    /*
   *http://192.168.10.20/gxkjg/index.php?g=project&m=question&a=index&deviceno=AND1000000225
   * 获取问卷调查
   * */
    @GET("index.php?g=mapi&m=questionnaire&a=index")
    Observable<Response<Ques>> FetchQues(@Query("language")int language);


/*
上传位置信息
                                      );
 */
@FormUrlEncoded
@POST("index.php?g=mapi&m=positions&a=positions")
Call<Message> putPostionInfo(@Field("deviceno") String deviceno,
                          @Field("app_kind") int app_kind,
                          @Field("auto_num") int auto_num);

/*AR播放次数上传
* http://192.168.10.20/gxkjg/index.php?g=mapi&m=exhibit&a=ar_play
* */
@GET("index.php?g=mapi&m=exhibit&a=ar_play")
Call<TextClass> ARplay(@Query("num") String num);

}
