package cn.hwwwwh.lexiangdaxue.SelltementClass.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.other.MyBottomSheetDialog;

/**
 * Created by 97481 on 2016/12/18.
 */
public class DialogTimeChoose extends BottomSheetDialogFragment implements View.OnClickListener {
    private TextView today;
    private TextView tomorrow;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new MyBottomSheetDialog(getContext());
    }

    public static DialogTimeChoose newInstance() {
        DialogTimeChoose fragment = new DialogTimeChoose();
        return fragment;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.settlement_buttonsheet,container,false);
        today=(TextView)view.findViewById(R.id.today);
        tomorrow=(TextView)view.findViewById(R.id.tomorrow);
        today.setOnClickListener(this);
        tomorrow.setOnClickListener(this);
        fragmentCommit(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.today:
                fragmentCommit(true);
                break;
            case R.id.tomorrow:
                fragmentCommit(false);
                break;
        }
    }

    private void fragmentCommit(boolean isToday){
        SettlementFragment settlementFragment=new SettlementFragment();
        FragmentTransaction fragmentTransaction= getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.settlement_container, settlementFragment);
        Bundle bundle=new Bundle();

        bundle.putBoolean("getDay", isToday);
        settlementFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }
}
