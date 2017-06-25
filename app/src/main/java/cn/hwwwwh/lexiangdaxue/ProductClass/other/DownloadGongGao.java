package cn.hwwwwh.lexiangdaxue.ProductClass.other;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

import static cn.hwwwwh.lexiangdaxue.other.ParserJson.parserJsonTogongGao;

/**
 * Created by 97481 on 2016/11/27.
 */
public class DownloadGongGao extends AsyncTask<String,Void,String> {
    private Context context;
    private ProductActivity activity;
    private TextView notification;
    public DownloadGongGao(Context context,ProductActivity activity){
        this.context=context;
        this.activity=activity;
        notification=(TextView)activity.findViewById(R.id.notification);
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(s != null){
            notification.setText(s);
        }
        else{
            notification.setText("");
            ToastUtil.show("数据加载失败");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String gongGao=null;
        if(HttpUtils.isNetConn(context)){
            byte[] b=HttpUtils.downloadFromNet(params[0]);
            String jsonString=new String(b);
            Log.d("Tag", jsonString);
            gongGao= parserJsonTogongGao(jsonString);
        }
        return gongGao;
    }
}
