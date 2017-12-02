package cn.hwwwwh.lexiangdaxue.FgSecondClass.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/9/23/ 0023.
 */

public class ReplyBean {

    /**
     * error : false
     * replyData : [{"Reply_id":1,"Reply_admin":"_hello世界1997","Reply_adminuid":"5812210111a7d3.45980874","Reply_uuid":"reply1","Reply_content":"回复测试111111111111111111111111111111111111111111111111111111111111111","Reply_createtime":"6月前","Reply_goodcount":70,"Reply_commentcount":0,"Reply_email":"123456@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"ispop":"1","headPic":"https://i.loli.net/2017/09/20/59c1fdd519deb.jpg"},{"Reply_id":13,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply13","Reply_content":"回复hwh的12","Reply_createtime":"3月前","Reply_goodcount":67,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":0,"Reply_postuuid":"testuuid","Reply_replyuuid":"reply12\r\n","ispop":"1","Reply_replyadmin":"hwh123","headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":3,"Reply_admin":"_hello世界1997","Reply_adminuid":"5812210111a7d3.45980874","Reply_uuid":"reply3","Reply_content":"我是回复测试3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333","Reply_createtime":"6月前","Reply_goodcount":60,"Reply_commentcount":0,"Reply_email":"123456@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"ispop":"1","headPic":"https://i.loli.net/2017/09/20/59c1fdd519deb.jpg"},{"Reply_id":13,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply13","Reply_content":"回复hwh的12","Reply_createtime":"3月前","Reply_goodcount":67,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":0,"Reply_postuuid":"testuuid","Reply_replyuuid":"reply12\r\n","Reply_replyadmin":"hwh123","headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":12,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply12","Reply_content":"回复自己12","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":11,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply11","Reply_content":"回复自己11","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":10,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply10","Reply_content":"回复自己10","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":9,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply9","Reply_content":"回复自己9","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":8,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply8","Reply_content":"回复自己8","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":7,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply7","Reply_content":"回复自己7","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":6,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply6","Reply_content":"回复自己6","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":5,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply5","Reply_content":"回复自己5","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"},{"Reply_id":4,"Reply_admin":"hwh123","Reply_adminuid":"58026809e4e157.30048462","Reply_uuid":"reply4","Reply_content":"回复自己44444444","Reply_createtime":"6月前","Reply_goodcount":0,"Reply_commentcount":0,"Reply_email":"974815768@qq.com","Reply_isposts":1,"Reply_postuuid":"testuuid","Reply_replyuuid":null,"headPic":"https://i.loli.net/2017/08/21/599a6cb274ac5.jpg"}]
     */

    private boolean error;
    private List<ReplyDataBean> replyData;
    private String error_msg;

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getError_msg() {
        return error_msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ReplyDataBean> getReplyData() {
        return replyData;
    }

    public void setReplyData(List<ReplyDataBean> replyData) {
        this.replyData = replyData;
    }

    public static class ReplyDataBean {
        /**
         * Reply_id : 1
         * Reply_admin : _hello世界1997
         * Reply_adminuid : 5812210111a7d3.45980874
         * Reply_uuid : reply1
         * Reply_content : 回复测试111111111111111111111111111111111111111111111111111111111111111
         * Reply_createtime : 6月前
         * Reply_goodcount : 70
         * Reply_commentcount : 0
         * Reply_email : 123456@qq.com
         * Reply_isposts : 1
         * Reply_postuuid : testuuid
         * Reply_replyuuid : null
         * ispop : 1
         * headPic : https://i.loli.net/2017/09/20/59c1fdd519deb.jpg
         * Reply_replyadmin : hwh123
         * Reply_isZan:1
         */

        private int Reply_id;
        private String Reply_admin;
        private String Reply_adminuid;
        private String Reply_uuid;
        private String Reply_content;
        private String Reply_createtime;
        private int Reply_goodcount;
        private int Reply_commentcount;
        private String Reply_email;
        private int Reply_isposts;
        private String Reply_postuuid;
        private Object Reply_replyuuid;
        private String ispop="0";
        private String headPic;
        private String Reply_replyadmin;
        private int Reply_isZan=0;

        public int getReply_isZan() {
            return Reply_isZan;
        }

        public void setReply_isZan(int reply_isZan) {
            Reply_isZan = reply_isZan;
        }

        public int getReply_id() {
            return Reply_id;
        }

        public void setReply_id(int Reply_id) {
            this.Reply_id = Reply_id;
        }

        public String getReply_admin() {
            return Reply_admin;
        }

        public void setReply_admin(String Reply_admin) {
            this.Reply_admin = Reply_admin;
        }

        public String getReply_adminuid() {
            return Reply_adminuid;
        }

        public void setReply_adminuid(String Reply_adminuid) {
            this.Reply_adminuid = Reply_adminuid;
        }

        public String getReply_uuid() {
            return Reply_uuid;
        }

        public void setReply_uuid(String Reply_uuid) {
            this.Reply_uuid = Reply_uuid;
        }

        public String getReply_content() {
            return Reply_content;
        }

        public void setReply_content(String Reply_content) {
            this.Reply_content = Reply_content;
        }

        public String getReply_createtime() {
            return Reply_createtime;
        }

        public void setReply_createtime(String Reply_createtime) {
            this.Reply_createtime = Reply_createtime;
        }

        public int getReply_goodcount() {
            return Reply_goodcount;
        }

        public void setReply_goodcount(int Reply_goodcount) {
            this.Reply_goodcount = Reply_goodcount;
        }

        public int getReply_commentcount() {
            return Reply_commentcount;
        }

        public void setReply_commentcount(int Reply_commentcount) {
            this.Reply_commentcount = Reply_commentcount;
        }

        public String getReply_email() {
            return Reply_email;
        }

        public void setReply_email(String Reply_email) {
            this.Reply_email = Reply_email;
        }

        public int getReply_isposts() {
            return Reply_isposts;
        }

        public void setReply_isposts(int Reply_isposts) {
            this.Reply_isposts = Reply_isposts;
        }

        public String getReply_postuuid() {
            return Reply_postuuid;
        }

        public void setReply_postuuid(String Reply_postuuid) {
            this.Reply_postuuid = Reply_postuuid;
        }

        public Object getReply_replyuuid() {
            return Reply_replyuuid;
        }

        public void setReply_replyuuid(Object Reply_replyuuid) {
            this.Reply_replyuuid = Reply_replyuuid;
        }

        public String getIspop() {
            return ispop;
        }

        public void setIspop(String ispop) {
            this.ispop = ispop;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getReply_replyadmin() {
            return Reply_replyadmin;
        }

        public void setReply_replyadmin(String Reply_replyadmin) {
            this.Reply_replyadmin = Reply_replyadmin;
        }
    }
}
