package cn.hwwwwh.lexiangdaxue.ProductClass.presenter;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public interface IDownloadProductPresenter {

    /**
     * @param url
     */
    void downloadProduct(String url);


    /**
     * @param products
     */
    void downloadProductSuccess(List<Product> products);

    /**
     *
     */
    void downloadProductFail();
}
