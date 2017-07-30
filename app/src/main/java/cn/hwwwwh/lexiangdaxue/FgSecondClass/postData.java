package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import java.util.List;

/**
 * Created by 97481 on 2017/3/7/ 0007.
 */

public class postData {
    public String postAdmin;
    public String postContent;
    public String createTime;
    public String goodCount;
    public String commentCount;
    public String imgNum;
    public String user1;
    public String comment1;
    public String user2;
    public String comment2;
    public String user3;
    public String comment3;
    public String Id;
    public String pictureUrl1;
    public String picture1Height;
    public String picture1Width;
    public String postUuid;
    public boolean isZan=false;
    public List<String> PicsData;
    //0为未判定过，1为显示显示全部按钮，2为不显示
    public int isShowAll=0;

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

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser3(String user3) {
        this.user3 = user3;
    }

    public String getUser3() {
        return user3;
    }

    public String getComment1() {
        return comment1;
    }

    public void setComment1(String comment1) {
        this.comment1 = comment1;
    }

    public String getComment2() {
        return comment2;
    }

    public void setComment2(String comment2) {
        this.comment2 = comment2;
    }

    public String getComment3() {
        return comment3;
    }

    public void setComment3(String comment3) {
        this.comment3 = comment3;
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

    public boolean getIsZan(){
        return isZan;
    }

    public void setIsZan(boolean zan) {
        isZan = zan;
    }

    public void setPicsData(List<String> picsData) {
        PicsData = picsData;
    }

    public List<String> getPicsData() {
        return PicsData;
    }

    public void setIsShowAll(int isShowAll) {
        this.isShowAll = isShowAll;
    }

    public int getIsShowAll() {
        return isShowAll;
    }
}
