package cn.hwwwwh.lexiangdaxue.other;

import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.Data;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.UserBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.PostBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.PostsBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.ReplyBean;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.bean.DetailOrder;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.bean.Order;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.OrderBean;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.ServerTime;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.SelectTime;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 97481 on 2017/9/19/ 0019.
 */

public interface ApiServices {

    @GET("HomeApi.php")
    Observable<Data> getHomeData(@Query("method") String method);

    @GET("UvApi.php")
    Observable<UserBean> getUserBean(@Query("token") String token,@Query("deviceId") String deviceId);

    @GET("postsApi.php")

    Observable<PostsBean> getPostsBean(@Query("page") int page,@Query("method")
            int method,@Query("token") String token,@Query("deviceId") String deviceId);

    @GET("SinglePostApi.php")
    Observable<PostBean> getSinglePost(@Query("post_uuid") String post_uuid
            , @Query("token") String token, @Query("deviceId") String deviceId);

    @GET("ReplyApi.php")
    Observable<ReplyBean> getReplyBean(@Query("post_uuid") String post_uuid,@Query("Page") int page
            , @Query("token") String token, @Query("deviceId") String deviceId);

    @GET("test_getTime.json")
    Observable<SelectTime> getSelectTime();

    @GET("getTime.php")
    Observable<ServerTime> getServerTime();


    @POST("admin/HandleOrder.php")
    @FormUrlEncoded
    Observable<OrderBean> postOrder(@Field("order_shipping_fee") int order_shipping_fee,@Field("order_payway") int order_payway
            ,@Field("order_name") String order_name,@Field("order_phone") String order_phone,@Field("order_address") String order_address
            ,@Field("order_note") String order_note,@Field("order_type") int order_type,@Field("order_totalprice") double order_totalprice
            ,@Field("token") String token ,@Field("deviceId") String deviceId,@Field("order_arriveTime") String order_arriveTime
            ,@Field("OrderData") String OrderData ,@Field("order_totalGoodsNum") int order_totalGoodsNum);

    @GET("api/order/OrderApi.php")
    Observable<Order> getOrder(@Query("token") String token,@Query("deviceId") String deviceId,@Query("page") int page);

    @GET("api/order/OrderApi.php")
    Observable<Order> getOrder(@Query("token") String token,@Query("deviceId") String deviceId,@Query("page") int page,@Query("method") int method);

    @GET("api/order/DetailOrderApi.php")
    Observable<DetailOrder> getDetailOrder(@Query("token") String token,@Query("deviceId") String deviceId,@Query("orderSn") String orderSn);

}
