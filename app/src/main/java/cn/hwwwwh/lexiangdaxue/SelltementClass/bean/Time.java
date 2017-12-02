package cn.hwwwwh.lexiangdaxue.SelltementClass.bean;

import java.util.List;

/**
 * Created by 97481 on 2017/10/14/ 0014.
 */

public class Time  {

    /**
     * hour : 22
     * min : 51
     */

    private String hour;
    private String min;
    private SelectTime Time;

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public SelectTime getTime() {
        return Time;
    }

    public void setTime(SelectTime time) {
        Time = time;
    }
}
