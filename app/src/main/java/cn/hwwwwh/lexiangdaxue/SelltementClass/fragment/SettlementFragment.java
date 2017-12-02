package cn.hwwwwh.lexiangdaxue.SelltementClass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.activity.SettlementActivity;
import cn.hwwwwh.lexiangdaxue.SelltementClass.adapter.TimeChooseAdapter;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.SelectTime;
import cn.hwwwwh.lexiangdaxue.SelltementClass.bean.Time;
import cn.hwwwwh.lexiangdaxue.SelltementClass.presenter.DownloadTimePre;
import cn.hwwwwh.lexiangdaxue.SelltementClass.view.ITimeView;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

/**
 * Created by 97481 on 2016/12/18.
 */
public class SettlementFragment extends BottomSheetDialogFragment implements ITimeView{

    private View view;
    private boolean isToday;
    private RecyclerView timeChoose;
    private DownloadTimePre downloadTimePre;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.settlement_frg,null);
        isToday=getArguments().getBoolean("getDay");
        timeChoose=(RecyclerView)view.findViewById(R.id.time_List);
        timeChoose.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        downloadTimePre=new DownloadTimePre(this, (SettlementActivity) getActivity());
        downloadTimePre.download();
        return view;
    }

    @Override
    public void setTime(Time time) {
        List<String> timeList = new ArrayList<>();
        int hour= Integer.parseInt(time.getHour());
        int min= Integer.parseInt(time.getMin());
        List<SelectTime.TimeBean> timeBean=time.getTime().getTime();
        if(isToday) {
            timeList.add("尽快送达");
            for (int i = 0; i < timeBean.size(); i++) {
                int beginTime = timeBean.get(i).getBeginTime();
                int keepTime = timeBean.get(i).getKeepTime();
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
            for (int i = 0; i < timeBean.size(); i++) {
                int beginTime = timeBean.get(i).getBeginTime();
                int keepTime = timeBean.get(i).getKeepTime();
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
        TimeChooseAdapter adapter=new TimeChooseAdapter(timeList,view.getContext(),isToday,getActivity());
        timeChoose.setAdapter(adapter);
    }


    @Override
    public void setFail(String msg) {
        ToastUtil.show(msg);
    }

}
