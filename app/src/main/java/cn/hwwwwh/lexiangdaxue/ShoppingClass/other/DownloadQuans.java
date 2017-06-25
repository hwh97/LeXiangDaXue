package cn.hwwwwh.lexiangdaxue.ShoppingClass.other;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ShoppingClass.adapter.ShoppingAdapter;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;

/**
 * Created by 97481 on 2017/1/27.
 */

public class DownloadQuans extends AsyncTask<String,Void,List<QuanGoods>> {
    private Context context;
    private RecyclerView recyclerView;
    private int page;
    private ShoppingAdapter adapter;

    public DownloadQuans(Context context, RecyclerView recyclerView,int page,ShoppingAdapter adapter){

        this.context=context;
        this.recyclerView=recyclerView;
        this.page=page;
        this.adapter=adapter;
    }

    @Override
    protected void onPostExecute(List<QuanGoods> quanGoodses) {
        super.onPostExecute(quanGoodses);
        if(quanGoodses != null && quanGoodses.size()!=0&&page==1){
            adapter.setData(quanGoodses);
            recyclerView.setAdapter(adapter);
        }else if(quanGoodses != null && quanGoodses.size()!=0 && page>1){
            adapter.addMoreData(quanGoodses);
        }
        else{
            Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected List<QuanGoods> doInBackground(String... params) {
        List<QuanGoods> list=null;
        String Url=params[0];
        if(HttpUtils.isNetConn(context)){
            byte[] b=HttpUtils.downloadFromNet(Url);
            String jsonString=new String(b);
            Log.d("Tag",jsonString);
            list= ParserJson.ParserJsonToQuanGoods(jsonString);
        }
        return list;
    }
}
