package cn.hwwwwh.lexiangdaxue.ProductClass.presenter;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public interface IDownloadActivityPresenter {

    /**
     * @param url
     */
    void downloadCategory(String url);


    /**
     * @param categories
     */
    void downloadCategorySuccess(List<Category> categories);

    /**
     *
     */
    void downloadCategoryFail();

    /**
     * @param url
     */
    void downloadGonggao(String url);


    /**
     * @param Gonggao
     */
    void downloadGonggaoSuccess(String Gonggao);

    /**
     *
     */
    void downloadGonggaoFail();
}
