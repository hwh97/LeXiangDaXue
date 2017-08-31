package cn.hwwwwh.lexiangdaxue.selectSchoolClass.bean;


import cn.hwwwwh.lexiangdaxue.selectSchoolClass.other.CityInterface;

/**
 * Author: Blincheng.
 * Date: 2017/5/9.
 * Description:
 */

public class City implements CityInterface {
    private String Name;
    public int schoolId;
    public int isOpen=0;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    @Override
    public String getCityName() {
        return Name;
    }

    public void setOpen(int open) {
        isOpen = open;
    }

    public int getIsOpen() {
        return isOpen;
    }
}
