package cn.hwwwwh.lexiangdaxue.ShoppingClass.presenter;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;

/**
 * Created by 97481 on 2017/3/26/ 0026.
 */

public interface IDownloadQuansPresenter{

    /**
     * @param url
     */
    void download(String url);


    /**
     * @param quanGoodses
     */
    void downloadSuccess(List<QuanGoods> quanGoodses);

    /**
     *
     */
    void downloadFail();
}