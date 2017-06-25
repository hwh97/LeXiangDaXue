package cn.hwwwwh.lexiangdaxue.other;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgSecondClass.postData;
import cn.hwwwwh.lexiangdaxue.HomeData;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.Time;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;

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
            JSONArray arr=obj.getJSONArray("HomeData");
            obj=arr.getJSONObject(0);
            HomeData homeData=new HomeData();
            homeData.setPic(obj.getString("Pic"));
            homeData.setD_title(obj.getString("D_title"));
            String s=obj.getString("Price");
            homeData.setPrice(s);
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


}
