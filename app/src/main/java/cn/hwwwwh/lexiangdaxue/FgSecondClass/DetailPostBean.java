package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import java.util.List;

/**
 * Created by 97481 on 2017/6/6/ 0006.
 */

public class DetailPostBean {
    public String postAdmin;
    public String postContent;
    public String createTime;
    public String goodCount;
    public String commentCount;
    public String imgNum;
    public String Id;
    public String pictureUrl1;
    public String picture1Height;
    public String picture1Width;
    public String postUuid;
    public List<String> PicsData;

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public String getPostAdmin() {
        return postAdmin;
    }

    public void setPostAdmin(String postAdmin) {
        this.postAdmin = postAdmin;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(String goodCount) {
        this.goodCount = goodCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public void setImgNum(String imgNum) {
        this.imgNum = imgNum;
    }

    public String getImgNum() {
        return imgNum;
    }



    public String getPictureUrl1() {
        return pictureUrl1;
    }

    public void setPictureUrl1(String pictureUrl1) {
        this.pictureUrl1 = pictureUrl1;
    }

    public String getPicture1Height() {
        return picture1Height;
    }

    public void setPicture1Height(String picture1Height) {
        this.picture1Height = picture1Height;
    }

    public String getPicture1Width() {
        return picture1Width;
    }

    public void setPicture1Width(String picture1Width) {
        this.picture1Width = picture1Width;
    }

    public String getPostUuid() {
        return postUuid;
    }

    public void setPostUuid(String postUuid) {
        this.postUuid = postUuid;
    }


    public void setPicsData(List<String> picsData) {
        PicsData = picsData;
    }

    public List<String> getPicsData() {
        return PicsData;
    }
}
