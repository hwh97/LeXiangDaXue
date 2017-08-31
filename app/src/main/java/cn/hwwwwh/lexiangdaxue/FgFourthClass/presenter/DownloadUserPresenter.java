package cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter;

import java.util.ArrayList;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.userBean;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.model.DownloadUserModel;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.view.IUserView;

/**
 * Created by 97481 on 2017/8/21/ 0021.
 */

public class DownloadUserPresenter implements IDownloadUserPresenter {

    public DownloadUserModel downloadUserModel;
    public IUserView iUserView;

    public DownloadUserPresenter(IUserView iUserView) {
        downloadUserModel=new DownloadUserModel(this);
        this.iUserView=iUserView;
    }

    @Override
    public void download(String url) {
        downloadUserModel.download(url);
    }

    @Override
    public void downloadSuccess(ArrayList<userBean> list) {
        iUserView.setUserView(list);
    }

    @Override
    public void downloadFail() {
        iUserView.setFailView();
    }
}
