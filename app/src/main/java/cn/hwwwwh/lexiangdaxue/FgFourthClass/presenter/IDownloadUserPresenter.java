package cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter;

import java.util.ArrayList;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.userBean;

/**
 * Created by 97481 on 2017/8/21/ 0021.
 */

public interface IDownloadUserPresenter {

    /**
     *
     * @param url
     */
    void download(String url);

    /**
     * @param list
     */
    void downloadSuccess(ArrayList<userBean> list);

    /**
     *
     */
    void downloadFail();

}
