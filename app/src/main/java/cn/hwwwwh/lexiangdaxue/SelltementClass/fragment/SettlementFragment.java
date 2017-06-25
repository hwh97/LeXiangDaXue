package cn.hwwwwh.lexiangdaxue.SelltementClass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.other.DownloadFreeTime;

/**
 * Created by 97481 on 2016/12/18.
 */
public class SettlementFragment extends BottomSheetDialogFragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.settlement_frg,null);
        boolean isToday=getArguments().getBoolean("getDay");
        RecyclerView timeChoose=(RecyclerView)view.findViewById(R.id.time_List);
        timeChoose.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        new DownloadFreeTime(view.getContext(),timeChoose,isToday,getActivity()).execute("http://cs.hwwwwh.cn/test_getTime.json");
        return view;
    }

}
