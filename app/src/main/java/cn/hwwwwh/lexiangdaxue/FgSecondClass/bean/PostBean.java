package cn.hwwwwh.lexiangdaxue.FgSecondClass.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/9/23/ 0023.
 */

public class PostBean {


    /**
     * error : false
     * SinglePost : {"Post_id":40,"Post_admin":"_hello世界1997","Post_adminuid":"5812210111a7d3.45980874","Post_content":"发帖","Post_createtime":"1月前","Post_goodcount":1,"Post_commentcount":3,"Post_email":"123456@qq.com","Post_imgnum":2,"Post_uuid":"599c207fc1f7a1.78338744","picture":["https://ooo.0o0.ooo/2017/08/22/599c2080e339a.jpg","https://i.loli.net/2017/08/22/599c20816d5d0.jpg"],"headPic":"https://i.loli.net/2017/09/20/59c1fdd519deb.jpg","isZan":"1"}
     */

    private boolean error;
    private SinglePostBean SinglePost;
    private String error_msg;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public SinglePostBean getSinglePost() {
        return SinglePost;
    }

    public void setSinglePost(SinglePostBean SinglePost) {
        this.SinglePost = SinglePost;
    }

    public static class SinglePostBean {
        /**
         * Post_id : 40
         * Post_admin : _hello世界1997
         * Post_adminuid : 5812210111a7d3.45980874
         * Post_content : 发帖
         * Post_createtime : 1月前
         * Post_goodcount : 1
         * Post_commentcount : 3
         * Post_email : 123456@qq.com
         * Post_imgnum : 2
         * Post_uuid : 599c207fc1f7a1.78338744
         * picture : ["https://ooo.0o0.ooo/2017/08/22/599c2080e339a.jpg","https://i.loli.net/2017/08/22/599c20816d5d0.jpg"]
         * headPic : https://i.loli.net/2017/09/20/59c1fdd519deb.jpg
         * isZan : 1
         */

        private int Post_id;
        private String Post_admin;
        private String Post_adminuid;
        private String Post_content;
        private String Post_createtime;
        private int Post_goodcount;
        private int Post_commentcount;
        private String Post_email;
        private int Post_imgnum;
        private String Post_uuid;
        private String headPic;
        private String isZan;
        private List<String> picture;

        public int getPost_id() {
            return Post_id;
        }

        public void setPost_id(int Post_id) {
            this.Post_id = Post_id;
        }

        public String getPost_admin() {
            return Post_admin;
        }

        public void setPost_admin(String Post_admin) {
            this.Post_admin = Post_admin;
        }

        public String getPost_adminuid() {
            return Post_adminuid;
        }

        public void setPost_adminuid(String Post_adminuid) {
            this.Post_adminuid = Post_adminuid;
        }

        public String getPost_content() {
            return Post_content;
        }

        public void setPost_content(String Post_content) {
            this.Post_content = Post_content;
        }

        public String getPost_createtime() {
            return Post_createtime;
        }

        public void setPost_createtime(String Post_createtime) {
            this.Post_createtime = Post_createtime;
        }

        public int getPost_goodcount() {
            return Post_goodcount;
        }

        public void setPost_goodcount(int Post_goodcount) {
            this.Post_goodcount = Post_goodcount;
        }

        public int getPost_commentcount() {
            return Post_commentcount;
        }

        public void setPost_commentcount(int Post_commentcount) {
            this.Post_commentcount = Post_commentcount;
        }

        public String getPost_email() {
            return Post_email;
        }

        public void setPost_email(String Post_email) {
            this.Post_email = Post_email;
        }

        public int getPost_imgnum() {
            return Post_imgnum;
        }

        public void setPost_imgnum(int Post_imgnum) {
            this.Post_imgnum = Post_imgnum;
        }

        public String getPost_uuid() {
            return Post_uuid;
        }

        public void setPost_uuid(String Post_uuid) {
            this.Post_uuid = Post_uuid;
        }

        public String getHeadPic() {
            return headPic;
        }

        public void setHeadPic(String headPic) {
            this.headPic = headPic;
        }

        public String getIsZan() {
            return isZan;
        }

        public void setIsZan(String isZan) {
            this.isZan = isZan;
        }

        public List<String> getPicture() {
            return picture;
        }

        public void setPicture(List<String> picture) {
            this.picture = picture;
        }
    }
}
