package cn.hwwwwh.lexiangdaxue.other;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.AddressBean;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.userBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.DetailPostBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.ReplyBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.postData;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.HomeData;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.Time;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.picData;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.bean.School;

/**
 * Created by 97481 on 2016/11/14.
 */

public class ParserJson {
    //第二个参数为分类名字用于获取不同的json,商品
    public static List<Product> parserJsonToProduct(String jsonString, String categoryName){
        List<Product> list=null;
        list=new ArrayList<>();
        try {
            JSONObject obj=new JSONObject(jsonString);
            JSONArray arr=obj.getJSONArray(categoryName);
            for(int i=0;i<arr.length();i++){
                JSONObject obj1 = arr.getJSONObject(i);
                Product product = new Product();
                    product.setId(obj1.getInt("id"));
                    product.setName(obj1.getString("name"));
                    product.setPrice(obj1.getString("price"));
                    product.setImage_Url(obj1.getString("image_Url"));
                    list.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //公告
    public static String parserJsonTogongGao(String jsonString){
        String gongGao=null;
        try{
            JSONObject obj=new JSONObject(jsonString);
            //newsList 是json标识符
            JSONArray arr=obj.getJSONArray("gonggao");
            JSONObject obj1=arr.getJSONObject(0);
            gongGao=obj1.getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gongGao;
    }

    //分类
    public static List<Category> parserJsonToCategory(String jsonString){
        List<Category> list=null;
        try{
            list=new ArrayList<>();
            JSONObject obj=new JSONObject(jsonString);
            //newsList 是json标识符
            JSONArray arr=obj.getJSONArray("category_List");
            for(int i=0;i<arr.length();i++){
                JSONObject obj1=arr.getJSONObject(i);
                Category categoryDetail=new Category();
                categoryDetail.setId(obj1.getInt("id"));
                categoryDetail.setSummary(obj1.getString("summary"));
                list.add(categoryDetail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //服务器时间
    public static  List<String> ParserJsonToTime(String jsonString){
        List<String> time= new ArrayList<String>();
        try {
            JSONObject obj=new JSONObject(jsonString);
            time.add(obj.getString("hour"));
            time.add(obj.getString("min"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return time;
    }

    //送货时间
    public static  List<Time> ParserJsonToSelectTime(String jsonString){
        List<Time> time= null;
        time=new ArrayList<>();
        try {
            JSONObject obj=new JSONObject(jsonString);
            JSONArray arr=obj.getJSONArray("getFreeTime");
            for(int i=0;i<arr.length();i++){
                JSONObject obj1 = arr.getJSONObject(i);
                Time time1 = new Time();
                time1.setBeginTime(Integer.parseInt(obj1.getString("BeginTime")));
                time1.setKeepTime(Integer.parseInt(obj1.getString("KeepTime")));
                time.add(time1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return time;
    }

    //购物券商品
    public static List<QuanGoods> ParserJsonToQuanGoods(String jsonString){
        List<QuanGoods> list=new ArrayList<>();;
        try {
            JSONObject obj=new JSONObject(jsonString);
            JSONArray arr=obj.getJSONArray("QuansData");
            for(int i=0;i<arr.length();i++){
                Log.d("TagLength",i+"");
                JSONObject obj1=arr.getJSONObject(i);
                QuanGoods quanGoods=new QuanGoods();
                quanGoods.setGoodsId(obj1.getString("GoodsID"));
                quanGoods.setD_title(obj1.getString("D_title"));
                quanGoods.setDsr(obj1.getString("Dsr"));
                quanGoods.setOrg_Price(obj1.getString("Org_Price"));
                quanGoods.setPic(obj1.getString("Pic"));
                quanGoods.setPrice(obj1.getString("Price"));
                quanGoods.setQuan_price(obj1.getString("Quan_price"));
                quanGoods.setQuan_surplus(obj1.getString("Quan_surplus"));
                quanGoods.setSales_num(obj1.getString("Sales_num"));
                quanGoods.setTitle(obj1.getString("Title"));
                quanGoods.setQuan_time(obj1.getString("Quan_time"));
                quanGoods.setQuan_link(obj1.getString("Quan_link"));
                quanGoods.setAli_click(obj1.getString("ali_click"));
                if(obj1.getString("IsTmall").equals("1")){
                    quanGoods.setTmall(true);
                }else {
                    quanGoods.setTmall(false);
                }
                list.add(quanGoods);
            }
        } catch (JSONException e) {
            Log.d("TagLength","jsomException");
            e.printStackTrace();
        }
        return list;
    }

    //首页数据
    public static List<HomeData> getHomeData(String jsonString){
        List<HomeData> list=new ArrayList<HomeData>();
        try {
            JSONObject obj=new JSONObject(jsonString);
            //领卷购数据
            JSONArray arr=obj.getJSONArray("QuanData");
            //活动图数据
            JSONArray arr2=obj.getJSONArray("PicData");

            JSONObject obj2=arr.getJSONObject(0);
            HomeData homeData=new HomeData();
            homeData.setPic(obj2.getString("Pic"));
            homeData.setD_title(obj2.getString("D_title"));
            homeData.setPrice(obj2.getString("Price"));
            List<picData> dataList=new ArrayList<>();
            for(int i=0;i<arr2.length();i++){
                JSONObject obj3=arr2.getJSONObject(i);
                picData picDatas=new picData();
                picDatas.setApppic_id(obj3.getInt("apppic_id"));
                picDatas.setApppic_level(obj3.getInt("apppic_level"));
                picDatas.setApppic_linkurl(obj3.getString("apppic_linkurl"));
                picDatas.setApppic_type(obj3.getInt("apppic_type"));
                picDatas.setApppic_url((obj3.getString("apppic_url")));
                dataList.add(picDatas);
            }
            homeData.setPicData(dataList);

            if(obj.has("UserUniversity")){
                JSONArray arr3=obj.getJSONArray("UserUniversity");
                JSONObject obj4=arr3.getJSONObject(0);
                School school=new School();
                school.setProvince(obj4.getString("uu_province"));
                school.setCity(obj4.getString("uu_city"));
                school.setName(obj4.getString("uu_name"));
                school.setUniversity_id(obj4.getInt("university_id"));
                school.setUid(obj4.getString("user_uuid"));
                homeData.setSchoolData(school);
            }
            list.add(homeData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    //帖子数据
    public static List<postData> getPostData(String jsonString){
        List<postData> list=new ArrayList<>();
        try {
            JSONObject obj=new JSONObject(jsonString);
            JSONArray arr=obj.getJSONArray("postsData");
            for(int i=0;i<arr.length();i++){
                postData postData=new postData();
                JSONObject obj1=arr.getJSONObject(i);
                postData.setId(obj1.getString("Post_id"));
                postData.setPostAdmin(obj1.getString("Post_admin"));
                postData.setPostContent(obj1.getString("Post_content"));
                postData.setCreateTime(obj1.getString("Post_createtime"));
                postData.setGoodCount(obj1.getString("Post_goodcount"));
                postData.setCommentCount(obj1.getString("Post_commentcount"));
                postData.setImgNum(obj1.getString("Post_imgnum"));
                postData.setPostUuid(obj1.getString("Post_uuid"));
                int Post_commentcount=Integer.parseInt(obj1.getString("Post_commentcount"));
                if(Post_commentcount>0){
                    if(Post_commentcount==1){
                        postData.setUser1(obj1.getString("user1"));
                        postData.setComment1(obj1.getString("comment1"));
                    }else if(Post_commentcount==2){
                        postData.setUser1(obj1.getString("user1"));
                        postData.setComment1(obj1.getString("comment1"));
                        postData.setUser2(obj1.getString("user2"));
                        postData.setComment2(obj1.getString("comment2"));
                    }else if(Post_commentcount>=3){
                        postData.setUser1(obj1.getString("user1"));
                        postData.setComment1(obj1.getString("comment1"));
                        postData.setUser2(obj1.getString("user2"));
                        postData.setComment2(obj1.getString("comment2"));
                        postData.setUser3(obj1.getString("user3"));
                        postData.setComment3(obj1.getString("comment3"));
                    }
                }
                int Post_picNum=Integer.parseInt(obj1.getString("Post_imgnum"));
                if(Post_picNum>0){
                    if(Post_picNum==1){
                        postData.setPictureUrl1(obj1.getString("picture1"));
                        postData.setPicture1Height(obj1.getString("picture1Height"));
                        postData.setPicture1Width(obj1.getString("picture1Width"));
                    }else if(Post_picNum>=2) {
                        List<String> list2=new ArrayList<>();
                        for(int j=0;j<Post_picNum;j++){
                            list2.add(obj1.getString("picture"+(j+1)));
                        }
                        postData.setPicsData(list2);
                    }
                }
                if(obj1.getString("isZan").equals("1")){
                    postData.setIsZan(true);
                }else {
                    postData.setIsZan(false);
                }
                list.add(postData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //单个帖子数据

    public static List<DetailPostBean> getSinglePostData(String jsonString){
        List<DetailPostBean> list = new ArrayList<>();
        Log.d("lexiangdaxueTag", jsonString);
        try {
            JSONArray arr = new JSONArray(jsonString);
            JSONObject obj = arr.getJSONObject(0);
            DetailPostBean detailPostBean = new DetailPostBean();
            detailPostBean.setId(obj.getString("Post_id"));
            detailPostBean.setPostAdmin(obj.getString("Post_admin"));
            detailPostBean.setPostContent(obj.getString("Post_content"));
            detailPostBean.setCreateTime(obj.getString("Post_createtime"));
            detailPostBean.setGoodCount(obj.getString("Post_goodcount"));
            detailPostBean.setCommentCount(obj.getString("Post_commentcount"));
            detailPostBean.setImgNum(obj.getString("Post_imgnum"));
            detailPostBean.setPostUuid(obj.getString("Post_uuid"));
            int Post_picNum = Integer.parseInt(obj.getString("Post_imgnum"));
            if (Post_picNum > 0) {
                if (Post_picNum == 1) {
                    detailPostBean.setPictureUrl1(obj.getString("picture1"));
                    detailPostBean.setPicture1Height(obj.getString("picture1Height"));
                    detailPostBean.setPicture1Width(obj.getString("picture1Width"));
                } else if (Post_picNum >= 2) {
                    List<String> list2 = new ArrayList<>();
                    for (int j = 0; j < Post_picNum; j++) {
                        list2.add(obj.getString("picture" + (j + 1)));
                    }
                    detailPostBean.setPicsData(list2);
                }
            }
            if (obj.has("Post_isZan")) {
                detailPostBean.setZan(true);
            }
            list.add(detailPostBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    //回复数据
    public static ArrayList<ReplyBean> getReplyData(String jsonString){
        ArrayList<ReplyBean> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(jsonString);
            for(int i=0;i<arr.length();i++) {
                JSONObject obj = arr.getJSONObject(i);
                ReplyBean replyBean = new ReplyBean();
                replyBean.setReply_id(obj.getString("Reply_id"));
                replyBean.setReply_admin(obj.getString("Reply_admin"));
                replyBean.setReply_uuid(obj.getString("Reply_uuid"));
                replyBean.setReply_content(obj.getString("Reply_content"));
                replyBean.setReply_createtime(obj.getString("Reply_createtime"));
                replyBean.setReply_email(obj.getString("Reply_email"));
                replyBean.setReply_postuuid(obj.getString("Reply_postuuid"));
                replyBean.setReply_commentcount(obj.getInt("Reply_commentcount"));
                replyBean.setReply_goodcount(obj.getInt("Reply_goodcount"));
                if(obj.has("Reply_isposts")){
                    if(obj.getInt("Reply_isposts")==0){
                        replyBean.setReply_isReplys(true);
                    }
                }
                if(obj.has("ispop")){
                    replyBean.setIspop(true);
                }
                if(replyBean.Reply_isReplys && obj.has("Reply_replyadmin") && obj.has("Reply_replyemail") ){
                    replyBean.setReply_replyadmin(obj.getString("Reply_replyadmin"));
                    replyBean.setRepky_replyemail(obj.getString("Reply_replyemail"));
                }
                if(obj.has("Reply_isZan")){
                    replyBean.setZan(true);
                }
                list.add(replyBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    //用户信息
    public static ArrayList<userBean> getUserInfo(String jsonString){
        ArrayList<userBean> list=new ArrayList<>();
        JSONObject jobj= null;
        try {
            userBean userBean=new userBean();
            jobj = new JSONObject(jsonString);
            //user 实例
            JSONArray userdata = jobj.getJSONArray("userdata");
            list.add(new Gson().fromJson(userdata.get(0).toString(),userBean.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<AddressBean> getUserAddress(String jsonString){
        ArrayList<AddressBean> list=new ArrayList<>();
        JSONObject jobj= null;
        try {
            userBean userBean=new userBean();
            jobj = new JSONObject(jsonString);
            //user 实例
            JSONArray address = jobj.getJSONArray("address");
            list.add(new Gson().fromJson(address.get(0).toString(),AddressBean.class));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
