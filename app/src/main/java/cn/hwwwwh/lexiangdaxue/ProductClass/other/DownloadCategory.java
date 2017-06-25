package cn.hwwwwh.lexiangdaxue.ProductClass.other;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.CategoryAdapter;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

import static cn.hwwwwh.lexiangdaxue.other.ParserJson.parserJsonToCategory;

/**
 * Created by 97481 on 2016/11/13.
 */
public class DownloadCategory  extends AsyncTask<String, Void , List<Category>> {
    private RecyclerView rv_Category;
    private ProductActivity activity;

    public DownloadCategory(ProductActivity activity, RecyclerView rv_Category){
        this.activity=activity;
        this.rv_Category=rv_Category;
    }
    @Override
    protected void onPostExecute(final List<Category> category_details) {
        super.onPostExecute(category_details);
        if(category_details != null && category_details.size()!=0){
            //更新lv
            CategoryAdapter adapterCategory = new CategoryAdapter(activity,category_details);
            rv_Category.setAdapter(adapterCategory);
        }else{
            ToastUtil.show("数据加载失败");
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<Category> doInBackground(String... params) {
        List<Category> list =null;
        if(HttpUtils.isNetConn(activity)){
            byte[] b=HttpUtils.downloadFromNet(params[0]);  //可变参数params当成一个数组使用，其中的params[0]就是我们传递过来的参数
            String jsonString=new String(b);
            Log.d("Tag", jsonString);
            list=parserJsonToCategory(jsonString);
            Log.d("Tag", list.toString());
        }
        return list;
    }

}
