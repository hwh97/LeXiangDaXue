package cn.hwwwwh.lexiangdaxue.ProductClass.Category;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.ProductClass.ProductFragment;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;

import static cn.hwwwwh.lexiangdaxue.other.ParserJson.parserJsonToCategory;

/**
 * Created by 97481 on 2016/11/13.
 */
public class DownloadCategory  extends AsyncTask<String, Void , List<Category>> {
    private ListView lv;
    private Context context;
    private Spinner sp;
    private ImageView imageView;
    private View view2;
    private View tempView;
    private int tempPostion=0;
    private ProductFragment productFragment;
    public DownloadCategory(Context context,ListView lv){
        this.context=context;
        this.lv=lv;
    }
    @Override
    protected void onPostExecute(final List<Category> category_details) {
        super.onPostExecute(category_details);
        if(category_details != null && category_details.size()!=0){
            //更新lv
            Adapter_Category adapterCategory = new Adapter_Category(context,category_details);
            lv.setAdapter(adapterCategory);
        }else{
            Toast.makeText(context, "数据加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<Category> doInBackground(String... params) {
        List<Category> list =null;
        if(HttpUtils.isNetConn(context)){
            byte[] b=HttpUtils.downloadFromNet(params[0]);  //可变参数params当成一个数组使用，其中的params[0]就是我们传递过来的参数
            String jsonString=new String(b);
            Log.d("Tag", jsonString);
            list=parserJsonToCategory(jsonString);
            Log.d("Tag", list.toString());
        }
        return list;
    }

    private void intiview(){
        //productFragment=new ProductFragment();

    }
}
