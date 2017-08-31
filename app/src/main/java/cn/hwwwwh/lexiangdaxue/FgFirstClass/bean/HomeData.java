package cn.hwwwwh.lexiangdaxue.FgFirstClass.bean;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.selectSchoolClass.bean.School;

/**
 * Created by 97481 on 2017/2/10.
 */

public class HomeData {

    public String Pic;
    public String Price;
    public String D_title;
    public List<cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.picData> picData;
    public School schoolData;

    public void setPrice(String price) {
        Price = price;
    }

    public String getPrice () {
        return Price;
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

    public void setPicData(List<cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.picData> picData) {
        this.picData = picData;
    }

    public List<cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.picData> getPicData() {
        return picData;
    }

    public void setSchoolData(School schoolData) {
        this.schoolData = schoolData;
    }

    public School getSchoolData() {
        return schoolData;
    }
}
