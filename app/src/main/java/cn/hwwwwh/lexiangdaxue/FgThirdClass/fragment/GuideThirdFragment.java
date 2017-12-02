package cn.hwwwwh.lexiangdaxue.FgThirdClass.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import cn.hwwwwh.lexiangdaxue.FgThirdClass.adapter.tabAdapter;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

public class GuideThirdFragment extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Fragment[] fragments;
    private Toolbar toolbar;
    boolean isLazyLoad =false;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fg3fragment,getActivity());
        tabLayout=getViewById(R.id.tab);
        viewPager=getViewById(R.id.viewPaper);
        toolbar=getViewById(R.id.fg3_toolbar);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        toolbar.setTitle("我的订单");
        toolbar.setTitleTextAppearance(getContext(),R.style.WindowTitle);
    }

    public void initTab(){
        fragments=new Fragment[4];
        fragments[0]= PageFragment.newInstance(0);
        fragments[1]=PageFragment.newInstance(1);
        fragments[2]=PageFragment.newInstance(2);
        fragments[3]=PageFragment.newInstance(3);
        tabAdapter fragmentPagerAdapter=new tabAdapter(getChildFragmentManager(),fragments);
        viewPager.removeAllViews();
        viewPager.removeAllViewsInLayout();
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void lazyLoad() {
        if(!isLazyLoad) {
            isLazyLoad = true;
            initTab();
        }
    }

}