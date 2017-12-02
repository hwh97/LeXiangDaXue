package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgSecondClass.adapter.postAdapter;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.postData;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;

/**
 * Created by 97481 on 2017/3/7/ 0007.
 */

public class DownloadPostData extends AsyncTask<String,Void,List<postData>> {

    private Context context;
    private RecyclerView recyclerView;
    private int page;
    private postAdapter adapter;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;

    public DownloadPostData(Context context, RecyclerView recyclerView,postAdapter adapter,int page){
        this.context=context;
        this.recyclerView=recyclerView;
        this.page=page;
        this.adapter=adapter;
        sessionManager=new SessionManager(context);
        sqLiteHandler=new SQLiteHandler(context);

    }

    @Override
    protected List<postData> doInBackground(String... params) {
        List<postData> list=null;
        String Url=params[0];
        if(HttpUtils.isNetConn(context)){
            byte[]b=null;
            if(sessionManager.isLoggedIn()){
                HashMap<String,String> hashMap=sqLiteHandler.getUserDetails();
                String email=hashMap.get("email");
                b=HttpUtils.downloadFromNet(Url+"&&email="+email);
            }else{
                b =HttpUtils.downloadFromNet(Url);
            }
            String jsonString=new String(b);
            Log.d("lexiangdaxueTag",jsonString);
            list= ParserJson.getPostData(jsonString);
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<postData> postDatas) {
        super.onPostExecute(postDatas);
        if(postDatas != null && postDatas.size()!=0 && page ==1){
           // adapter.setData(postDatas);
            recyclerView.setAdapter(adapter);
        }else if(postDatas != null && postDatas.size()!=0 && page>1){
            //adapter.addMoreData(postDatas);
        } else{
            Toast.makeText(context, "没有更多数据了", Toast.LENGTH_SHORT).show();
        }

    }

}
