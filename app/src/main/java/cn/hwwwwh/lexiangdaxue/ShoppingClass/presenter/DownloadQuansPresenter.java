package cn.hwwwwh.lexiangdaxue.ShoppingClass.presenter;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.model.DownloadQuansModel;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.model.IDownloadQuansModel;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.view.IQuansView;

/**
 * Created by 97481 on 2017/3/26/ 0026.
 */

public class DownloadQuansPresenter implements IDownloadQuansPresenter{

    private IQuansView iQuansView;
    private IDownloadQuansModel iDownloadQuansModel;

    public DownloadQuansPresenter(IQuansView iQuansView){
        this.iQuansView=iQuansView;
        iDownloadQuansModel=new DownloadQuansModel(this);
    }

    @Override
    public void download(String url) {
        iDownloadQuansModel.download(url);
    }

    @Override
    public void downloadSuccess(List<QuanGoods> quanGoodses) {
        iQuansView.setView(quanGoodses);
    }


    @Override
    public void downloadFail() {

    }
}

