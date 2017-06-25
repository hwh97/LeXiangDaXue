package cn.hwwwwh.lexiangdaxue.ProductClass.presenter;

import java.util.List;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.model.DownloadActivityModel;
import cn.hwwwwh.lexiangdaxue.ProductClass.model.IDownloadActivityModel;
import cn.hwwwwh.lexiangdaxue.ProductClass.view.IActivityView;
import cn.hwwwwh.lexiangdaxue.other.BasePresenter;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public class DownloadActivityPresenter extends BasePresenter implements IDownloadActivityPresenter {

    private IActivityView iProductView;
    private IDownloadActivityModel iDownloadActivityModel;

    public DownloadActivityPresenter(IActivityView iProductView){
        this.iProductView=iProductView;
        iDownloadActivityModel=new DownloadActivityModel(this);
    }

    @Override
    public void downloadCategory(String url) {
        iDownloadActivityModel.downloadCategory(url);
    }

    @Override
    public void downloadCategorySuccess(List<Category> categories) {
        iProductView.setCateGoryView(categories);
    }

    @Override
    public void downloadCategoryFail() {

    }

    @Override
    public void downloadGonggao(String url) {
        iDownloadActivityModel.downloadGonggao(url);
    }

    @Override
    public void downloadGonggaoSuccess(String Gonggao) {
        iProductView.setGonggaoView(Gonggao);
    }

    @Override
    public void downloadGonggaoFail() {

    }

}
