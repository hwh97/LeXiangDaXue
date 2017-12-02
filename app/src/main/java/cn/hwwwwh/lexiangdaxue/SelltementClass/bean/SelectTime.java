package cn.hwwwwh.lexiangdaxue.SelltementClass.bean;

import java.util.List;

/**
 * Created by 97481 on 2016/12/4.
 */
public class SelectTime {

    /**
     * error : false
     * Time : [{"BeginTime":9,"KeepTime":3},{"BeginTime":14,"KeepTime":3},{"BeginTime":18,"KeepTime":4}]
     */

    private boolean error;
    private List<TimeBean> Time;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<TimeBean> getTime() {
        return Time;
    }

    public void setTime(List<TimeBean> Time) {
        this.Time = Time;
    }

    public static class TimeBean {
        /**
         * BeginTime : 9
         * KeepTime : 3
         */

        private int BeginTime;
        private int KeepTime;

        public int getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(int BeginTime) {
            this.BeginTime = BeginTime;
        }

        public int getKeepTime() {
            return KeepTime;
        }

        public void setKeepTime(int KeepTime) {
            this.KeepTime = KeepTime;
        }
    }
}
