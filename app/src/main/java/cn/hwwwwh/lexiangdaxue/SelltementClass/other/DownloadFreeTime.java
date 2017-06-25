package cn.hwwwwh.lexiangdaxue.SelltementClass.other;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.SelltementClass.adapter.TimeChooseAdapter;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.Time;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

import static cn.hwwwwh.lexiangdaxue.other.ParserJson.ParserJsonToSelectTime;
import static cn.hwwwwh.lexiangdaxue.other.ParserJson.ParserJsonToTime;

/**
 * Created by 97481 on 2016/12/10.
 */
public class DownloadFreeTime extends AsyncTask<String,Void,List<Time>> {

    private Context context;
    private int hour;
    private int min;
    private RecyclerView recyclerView;
    private boolean isToday;
    private Activity activity;
    public DownloadFreeTime (Context context,RecyclerView recyclerView,boolean isToday,Activity activity){
        this.context=context;
        this.recyclerView=recyclerView;
        this.isToday=isToday;
        this.activity=activity;
    }
    @Override
    protected List<Time> doInBackground(String... params) {
        String Url=params[0];
        //获取当前时间
        List<String> timeNow= null;
        if(HttpUtils.isNetConn(context)){
            byte[] b=HttpUtils.downloadFromNet("http://cs.hwwwwh.cn/getTime.php");
            String jsonString =new String(b);
            timeNow= ParserJsonToTime(jsonString);
        }
        if(timeNow!=null && timeNow.size()!=0){
            //更新UI
            hour=Integer.parseInt(String.valueOf(timeNow.get(0)));
            min=Integer.parseInt(String.valueOf(timeNow.get(1)));
        }else{
            Toast.makeText(context, "服务器时间加载失败", Toast.LENGTH_SHORT).show();
        }
        List<Time> time= null;
        //加载选择时间
        if(HttpUtils.isNetConn(context)){
            byte[] b=HttpUtils.downloadFromNet(Url);
            String jsonString =new String(b);
            time= ParserJsonToSelectTime(jsonString);
        }
        return time;
      }

    @Override
    protected void onPostExecute(List<Time>time) {
        super.onPostExecute(time);
        Toast.makeText(context, hour+":"+min, Toast.LENGTH_SHORT).show();
        if(time!=null && time.size()!=0){
            List<String> timeList = new ArrayList<>();
            if(isToday) {
                timeList.add("尽快送达");
                for (int i = 0; i < time.size(); i++) {
                    int beginTime = time.get(i).getBeginTime();
                    int keepTime = time.get(i).getKeepTime();
                    if (beginTime > hour) {
                        for (int j = 0; j < keepTime; j++) {
                            for (int k = 0; k < 3; k++) {
                                if (k == 0) {
                                    //ex:7:00
                                    timeList.add(beginTime + j + ":" + "00");
                                } else {
                                    //ex:7:20
                                    timeList.add(beginTime + j + ":" + 20 * k);
                                }
                            }
                        }
                        timeList.add(beginTime + keepTime + ":" + "00");
                    } else if ((beginTime + keepTime) > hour) {
                        //8:15取9：00~11：00
                        if (min < 30) {
                            //避免10：15与11：00
                            if (hour + 1 < (beginTime + keepTime)) {
                                for (int j = 0; j < (beginTime + keepTime) - hour - 1; j++) {
                                    for (int k = 0; k < 3; k++) {
                                        if (k == 0) {
                                            //ex:7:00
                                            timeList.add(hour + j + 1 + ":" + "00");
                                        } else {
                                            //ex:7:20
                                            timeList.add(hour + j + 1 + ":" + 20 * k);
                                        }
                                    }
                                }
                            }
                            timeList.add(beginTime + keepTime + ":" + "00");
                        }
                        //8:35取9：40
                        else {
                            if (hour + 1 < (beginTime + keepTime)) {
                                timeList.add(hour + 1 + ":" + "40");
                                for (int j = 0; j < (beginTime + keepTime) - hour - 2; j++) {
                                    for (int k = 0; k < 3; k++) {
                                        if (k == 0) {
                                            //ex:7:00
                                            timeList.add(hour + 2 + j + ":" + "00");
                                        } else {
                                            //ex:7:20
                                            timeList.add(hour + 2 + j + ":" + 20 * k);
                                        }
                                    }
                                }
                                timeList.add(beginTime + keepTime + ":" + "00");
                            }
                        }
                    } else {
                    }
                }
            }
            //如果是明天，全部加载
            else{
                //加载全部时间代码
                for (int i = 0; i < time.size(); i++) {
                    int beginTime = time.get(i).getBeginTime();
                    int keepTime = time.get(i).getKeepTime();
                        for (int j = 0; j < keepTime; j++) {
                            for (int k = 0; k < 3; k++) {
                                if (k == 0) {
                                    //ex:7:00
                                    timeList.add(beginTime + j + ":" + "00");
                                } else {
                                    //ex:7:20
                                    timeList.add(beginTime + j + ":" + 20 * k);
                                }
                            }
                        }
                        timeList.add(beginTime + keepTime + ":" + "00");
                }
            }
            //添加Adapter
            TimeChooseAdapter adapter=new TimeChooseAdapter(timeList,context,isToday,activity);
            recyclerView.setAdapter(adapter);
        }else{
            Toast.makeText(context, "服务器时间加载失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}