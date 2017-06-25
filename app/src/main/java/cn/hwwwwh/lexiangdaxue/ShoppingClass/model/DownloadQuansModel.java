package cn.hwwwwh.lexiangdaxue.ShoppingClass.model;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.presenter.IDownloadQuansPresenter;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;

/**
 * Created by 97481 on 2017/3/26/ 0026.
 */

public class  DownloadQuansModel implements IDownloadQuansModel {

    public IDownloadQuansPresenter iDownloadQuansPresenter;

    public DownloadQuansModel(IDownloadQuansPresenter iDownloadQuansPresenter){
        this.iDownloadQuansPresenter=iDownloadQuansPresenter;
    }

    @Override
    public void download(String url) {
        new DownloadQuans().execute(url);
    }

    class DownloadQuans extends AsyncTask<String,Void,List<QuanGoods>> {

        public DownloadQuans(){

        }

        @Override
        protected void onPostExecute(List<QuanGoods> quanGoodses) {
            super.onPostExecute(quanGoodses);
            if(quanGoodses != null && quanGoodses.size()!=0){
                iDownloadQuansPresenter.downloadSuccess(quanGoodses);
            }else {
                iDownloadQuansPresenter.downloadFail();
            }
        }

        @Override
        protected List<QuanGoods> doInBackground(String... params) {
            List<QuanGoods> list=null;
            String Url=params[0];
            byte[] b=HttpUtils.downloadFromNet(Url);
            String jsonString=new String(b);
            Log.d("Tag",jsonString);
            list= ParserJson.ParserJsonToQuanGoods(jsonString);
            return list;
        }
    }

}

