package cn.hwwwwh.lexiangdaxue.FgSecondClass;

/**
 * Created by 97481 on 2017/6/17/ 0017.
 */

public class ReplyBean {

    public String Reply_id;
    public String Reply_admin;
    public String Reply_uuid;
    public String Reply_content;
    public String Reply_createtime;
    public int Reply_goodcount;
    public int Reply_commentcount;
    public String Reply_email;
    public boolean Reply_isReplys=false;
    public String Reply_postuuid;
    public boolean ispop=false;
    public String Reply_replyadmin;
    public String Repky_replyemail;

    public int getReply_commentcount() {
        return Reply_commentcount;
    }

    public void setReply_commentcount(int reply_commentcount) {
        Reply_commentcount = reply_commentcount;
    }

    public int getReply_goodcount() {
        return Reply_goodcount;
    }

    public void setReply_goodcount(int reply_goodcount) {
        Reply_goodcount = reply_goodcount;
    }

    public void setIspop(boolean ispop) {
        this.ispop = ispop;
    }

    public void setReply_admin(String reply_admin) {
        Reply_admin = reply_admin;
    }

    public void setReply_content(String reply_content) {
        Reply_content = reply_content;
    }

    public void setReply_createtime(String reply_createtime) {
        Reply_createtime = reply_createtime;
    }

    public void setReply_email(String reply_email) {
        Reply_email = reply_email;
    }

    public void setReply_id(String reply_id) {
        Reply_id = reply_id;
    }

    public void setReply_isReplys(boolean Reply_isReplys) {
        this.Reply_isReplys = Reply_isReplys;
    }

    public void setReply_postuuid(String reply_postuuid) {
        Reply_postuuid = reply_postuuid;
    }

    public void setReply_uuid(String reply_uuid) {
        Reply_uuid = reply_uuid;
    }

    public String getReply_admin() {
        return Reply_admin;
    }

    public String getReply_content() {
        return Reply_content;
    }

    public String getReply_createtime() {
        return Reply_createtime;
    }

    public String getReply_email() {
        return Reply_email;
    }

    public String getReply_id() {
        return Reply_id;
    }

    public String getReply_postuuid() {
        return Reply_postuuid;
    }

    public String getReply_uuid() {
        return Reply_uuid;
    }

    public String getRepky_replyemail() {
        return Repky_replyemail;
    }

    public String getReply_replyadmin() {
        return Reply_replyadmin;
    }

    public void setRepky_replyemail(String repky_replyemail) {
        Repky_replyemail = repky_replyemail;
    }

    public void setReply_replyadmin(String reply_replyadmin) {
        Reply_replyadmin = reply_replyadmin;
    }
}
