package cn.hwwwwh.lexiangdaxue.ProductClass.model;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public interface IDownloadActivityModel {

    /**
     * download category
     * @param url
     */
    void downloadCategory(String url);

    /**
     * download 公告
     * @param url
     */
    void downloadGonggao(String url);
}
