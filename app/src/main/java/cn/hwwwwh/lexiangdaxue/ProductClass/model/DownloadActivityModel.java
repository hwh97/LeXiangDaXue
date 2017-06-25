package cn.hwwwwh.lexiangdaxue.ProductClass.model;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.CategoryAdapter;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.presenter.IDownloadActivityPresenter;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.presenter.IDownloadQuansPresenter;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

import static cn.hwwwwh.lexiangdaxue.other.ParserJson.parserJsonToCategory;
import static cn.hwwwwh.lexiangdaxue.other.ParserJson.parserJsonTogongGao;

/**
 * Created by 97481 on 2017/4/4/ 0004.
 */

public class DownloadActivityModel implements IDownloadActivityModel {

    public IDownloadActivityPresenter iDownloadActivityPresenter;

    public DownloadActivityModel(IDownloadActivityPresenter iDownloadActivityPresenter){
        this.iDownloadActivityPresenter=iDownloadActivityPresenter;
    }

    @Override
    public void downloadCategory(String url) {
        new DownloadCategory().execute(url);
    }

    @Override
    public void downloadGonggao(String url) {
        new DownloadGongGao().execute(url);
    }

    class DownloadCategory  extends AsyncTask<String, Void , List<Category>> {

        public DownloadCategory() {

        }

        @Override
        protected void onPostExecute(final List<Category> category_details) {
            super.onPostExecute(category_details);
            if (category_details != null && category_details.size() != 0) {
                iDownloadActivityPresenter.downloadCategorySuccess(category_details);
            } else {
                iDownloadActivityPresenter.downloadCategoryFail();
                //ToastUtil.show("数据加载失败");
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Category> doInBackground(String... params) {
            List<Category> list = null;
            byte[] b = HttpUtils.downloadFromNet(params[0]);  //可变参数params当成一个数组使用，其中的params[0]就是我们传递过来的参数
            String jsonString = new String(b);
            Log.d("Tag", jsonString);
            list = parserJsonToCategory(jsonString);
            Log.d("Tag", list.toString());
            return list;
        }
    }

    class DownloadGongGao extends AsyncTask<String,Void,String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s != null){
               iDownloadActivityPresenter.downloadGonggaoSuccess(s);
            }
            else{
                iDownloadActivityPresenter.downloadGonggaoFail();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String gongGao=null;
            byte[] b=HttpUtils.downloadFromNet(params[0]);
            String jsonString=new String(b);
            Log.d("Tag", jsonString);
            gongGao= parserJsonTogongGao(jsonString);
            return gongGao;
        }
    }

}
