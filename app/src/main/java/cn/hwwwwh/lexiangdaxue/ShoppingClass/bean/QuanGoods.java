package cn.hwwwwh.lexiangdaxue.ShoppingClass.bean;

/**
 * Created by 97481 on 2017/1/27.
 */

public class QuanGoods {

    private String GoodsId;
    private String Title;
    private String D_title;
    private String Pic;
    private String Org_Price;
    private String Price;
    private boolean IsTmall;
    private String Sales_num;
    private String Dsr;
    private String Quan_price;
    private String Quan_surplus;
    private String Quan_time;
    private String Quan_link;
    private String ali_click;

    public String getGoodsId() {
        return GoodsId;
    }

    public void setGoodsId(String goodsId) {
        GoodsId = goodsId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setD_title(String d_title) {
        D_title = d_title;
    }

    public String getD_title() {
        return D_title;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getPic() {
        return Pic;
    }

    public void setOrg_Price(String org_Price) {
        Org_Price = org_Price;
    }

    public String getOrg_Price() {
        return Org_Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPrice() {
        return Price;
    }

    public void setTmall(boolean tmall) {
        IsTmall = tmall;
    }

    public boolean isTmall() {
        return IsTmall;
    }

    public void setSales_num(String sales_num) {
        Sales_num = sales_num;
    }

    public String getSales_num() {
        return Sales_num;
    }

    public void setDsr(String dsr) {
        Dsr = dsr;
    }

    public String getDsr() {
        return Dsr;
    }

    public void setQuan_price(String quan_price) {
        Quan_price = quan_price;
    }

    public String getQuan_price() {
        return Quan_price;
    }

    public void setQuan_surplus(String quan_surplus) {
        Quan_surplus = quan_surplus;
    }

    public String getQuan_surplus() {
        return Quan_surplus;
    }

    public void setQuan_time(String quan_time) {
        Quan_time = quan_time;
    }

    public String getQuan_time() {
        return Quan_time;
    }

    public String getQuan_link() {
        return Quan_link;
    }

    public void setQuan_link(String quan_link) {
        Quan_link = quan_link;
    }

    public String getAli_click() {
        return ali_click;
    }

    public void setAli_click(String ali_click) {
        this.ali_click = ali_click;
    }
}
