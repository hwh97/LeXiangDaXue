package cn.hwwwwh.lexiangdaxue.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.hwwwwh.lexiangdaxue.R;

public class GuideThirdFragment extends Fragment {
    //不加载下一页数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //fragment可见时执行加载数据或者进度条等

        } else {
            //不可见时不执行操作

        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fg3,
                null);

        return view;
    }

}