package cn.hwwwwh.lexiangdaxue.SelltementClass.adapter;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.activity.SettlementActivity;

/**
 * Created by 97481 on 2016/12/4.
 */
public class TimeChooseAdapter extends RecyclerView.Adapter<TimeChooseAdapter.ViewHolder>  {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private String choose_time;
    private boolean isToday;
    private boolean selectDay;
    private Activity activity;

    public TimeChooseAdapter(List<String> list,Context context,boolean isToday,Activity activity){
        this.list=list;
        this.context=context;
        inflater=LayoutInflater.from(context);
        SharedPreferences pref=context.getSharedPreferences("SettlementData",Context.MODE_PRIVATE);
        choose_time=pref.getString("time","尽快送达");
        selectDay=pref.getBoolean("isToday", true);
        this.isToday=isToday;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.time_recycleview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String a=list.get(position);
        holder.bindData(a);
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView time_Text;
        private ImageView Check;
        public ViewHolder(View itemView) {
            super(itemView);
            time_Text = (TextView) itemView.findViewById(R.id.time_Text);
            Check=(ImageView)itemView.findViewById(R.id.time_img);
            time_Text.setOnClickListener(this);
        }

        public void bindData(String a){
            time_Text.setText(a);
            if(choose_time.equals(a) && (selectDay==isToday)){
                Check.setVisibility(View.VISIBLE);
            }
            else{
                Check.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            //click bottom even
            SharedPreferences.Editor editor = context.getSharedPreferences("SettlementData", Context.MODE_PRIVATE).edit();
            editor.putBoolean("isToday", isToday);
            editor.putString("time", time_Text.getText().toString());
            editor.commit();
            onback();
            SettlementActivity settlementActivity= (SettlementActivity) activity;
            settlementActivity.RefreshUi();
        }
        public void  onback(){
            new Thread(){
                public void run(){
                    try{
                        Instrumentation inst=new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }catch (Exception e){

                    }
                }
            }.start();
        }
    }
}
