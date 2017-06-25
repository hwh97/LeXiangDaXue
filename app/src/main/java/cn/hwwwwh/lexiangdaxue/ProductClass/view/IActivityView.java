package cn.hwwwwh.lexiangdaxue.ProductClass.view;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public interface IActivityView {

    void setCateGoryView(List<Category> categories);

    void setGonggaoView(String Gonggao);
}
