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
    public List<Data.ActivityPicBean> picData;
    public List<Data.RotationPicBean> rotationPic;
    public UserBean userBean;
    public String uniError;

    public void setRotationPic(List<Data.RotationPicBean> rotationPic) {
        this.rotationPic = rotationPic;
    }

    public List<Data.RotationPicBean> getRotationPic() {
        return rotationPic;
    }

    public void setUniError(String uniError) {
        this.uniError = uniError;
    }

    public String getUniError() {
        return uniError;
    }

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

    public void setPicData(List<Data.ActivityPicBean> picData) {
        this.picData = picData;
    }

    public List<Data.ActivityPicBean> getPicData() {
        return picData;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }


}
