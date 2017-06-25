package cn.hwwwwh.lexiangdaxue.ProductClass.presenter;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.ProductClass.model.DownloadProductModel;
import cn.hwwwwh.lexiangdaxue.ProductClass.model.IDownloadProductModel;
import cn.hwwwwh.lexiangdaxue.ProductClass.view.IProductView;
import cn.hwwwwh.lexiangdaxue.other.BasePresenter;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public class DownloadProductPresenter extends BasePresenter implements IDownloadProductPresenter {

    private IProductView iProductView;
    private IDownloadProductModel iDownloadProductModel;

    public DownloadProductPresenter(IProductView iProductView){
        this.iProductView=iProductView;
        iDownloadProductModel=new DownloadProductModel(this);
    }

    @Override
    public void downloadProduct(String url,String category) {
        iDownloadProductModel.downloadProduct(url,category);
    }

    @Override
    public void downloadProductSuccess(List<Product> products) {
        iProductView.setProductView(products);
    }

    @Override
    public void downloadProductFail() {

    }

}
