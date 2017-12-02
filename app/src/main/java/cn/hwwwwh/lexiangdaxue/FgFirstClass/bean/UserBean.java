package cn.hwwwwh.lexiangdaxue.FgFirstClass.bean;

/**
 * Created by 97481 on 2017/8/13/ 0013.
 */

public class UserBean {
    /**
     * error : false
     * uni : {"uu_id":32,"user_uuid":"5812210111a7d3.45980874","uu_province":"广东省","uu_city":"佛山市","uu_name":"顺德职业技术学院","university_id":1}
     */

    private boolean error;
    private UniBean uni;
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

    public UniBean getUni() {
        return uni;
    }

    public void setUni(UniBean uni) {
        this.uni = uni;
    }

    public static class UniBean {
        /**
         * uu_id : 32
         * user_uuid : 5812210111a7d3.45980874
         * uu_province : 广东省
         * uu_city : 佛山市
         * uu_name : 顺德职业技术学院
         * university_id : 1
         */

        private int uu_id;
        private String user_uuid;
        private String uu_province;
        private String uu_city;
        private String uu_name;
        private int university_id;

        public int getUu_id() {
            return uu_id;
        }

        public void setUu_id(int uu_id) {
            this.uu_id = uu_id;
        }

        public String getUser_uuid() {
            return user_uuid;
        }

        public void setUser_uuid(String user_uuid) {
            this.user_uuid = user_uuid;
        }

        public String getUu_province() {
            return uu_province;
        }

        public void setUu_province(String uu_province) {
            this.uu_province = uu_province;
        }

        public String getUu_city() {
            return uu_city;
        }

        public void setUu_city(String uu_city) {
            this.uu_city = uu_city;
        }

        public String getUu_name() {
            return uu_name;
        }

        public void setUu_name(String uu_name) {
            this.uu_name = uu_name;
        }

        public int getUniversity_id() {
            return university_id;
        }

        public void setUniversity_id(int university_id) {
            this.university_id = university_id;
        }
    }
}