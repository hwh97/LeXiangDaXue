package cn.hwwwwh.lexiangdaxue.ProductClass.model;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.ProductAdapter;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.ProductClass.presenter.IDownloadProductPresenter;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.ArithUtil;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public class DownloadProductModel implements IDownloadProductModel {

    public IDownloadProductPresenter iDownloadProductPresenter;

    public DownloadProductModel(IDownloadProductPresenter iDownloadProductPresenter){
        this.iDownloadProductPresenter=iDownloadProductPresenter;
    }

    @Override
    public void downloadProduct(String url) {

        new DownloadProduct().execute(url);
    }

    class DownloadProduct extends AsyncTask<String,Void,List<Product>> {

        public DownloadProduct(){

        }


        @Override
        protected void onPostExecute(List<Product> list) {
            super.onPostExecute(list);
            if(list != null && list.size() != 0){
               iDownloadProductPresenter.downloadProductSuccess(list);
            }else{
                iDownloadProductPresenter.downloadProductFail();
            }
        }

        @Override
        protected  List<Product> doInBackground(String... params) {
            List<Product> list=null;
            String jsonString=null;
            Log.d("lexiangdaxueTag",params[0]);
            byte[] b=HttpUtils.downloadFromNet(params[0]);
            jsonString=new String(b);
            Log.d("Tag",jsonString);
            if(ParserJson.isError(jsonString)){
                list= ParserJson.parserJsonToProduct(jsonString);
            }
            return list;
        }
    }

}
