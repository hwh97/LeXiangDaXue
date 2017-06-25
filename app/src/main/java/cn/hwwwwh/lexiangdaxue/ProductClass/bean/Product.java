package cn.hwwwwh.lexiangdaxue.ProductClass.bean;

import java.io.Serializable;

/**
 * Created by 97481 on 2016/11/14.
 */
public class Product implements Serializable {
    private int id;
    private String name;
    private String price;
    private String category_of;
    private int selectCount=0;
    private String image_Url;

    public void setImage_Url(String image_Url) {
        this.image_Url = image_Url;
    }

    public String getImage_Url() {
        return image_Url;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public String getCategory_of() {
        return category_of;
    }

    public void setSelectCount(int selectCount) {
        this.selectCount = selectCount;
    }

    public int getSelectCount() {
        return selectCount;
    }

    public void setCategory_of(String category_of) {
        this.category_of = category_of;
    }

    public void setPrice(String price){
        this.price=price;
    }

    public String getPrice(){
        return this.price;
    }
}
