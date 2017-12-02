package cn.hwwwwh.lexiangdaxue.FgThirdClass.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

/**
 * Created by Administrator on 2017/10/12.
 */

public class tabAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT=4;
    public String TabTitles[]={"全部","待支付","进行中","取消/退款"};
    private Context context;
    private Fragment[] fragments;

    public tabAdapter(FragmentManager fm,Fragment[] fragments) {
        super(fm);
        this.fragments=fragments;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
