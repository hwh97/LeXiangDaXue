package cn.hwwwwh.lexiangdaxue.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.hwwwwh.lexiangdaxue.fragment.GuideFourthFragment;
import cn.hwwwwh.lexiangdaxue.fragment.GuideOneFragment;
import cn.hwwwwh.lexiangdaxue.fragment.GuideThirdFragment;
import cn.hwwwwh.lexiangdaxue.fragment.GuideTwoFragment;

public class GuideFragmentAdapter  extends FragmentPagerAdapter {
    private Fragment[] fragments = new Fragment[] { new GuideOneFragment(),
            new GuideTwoFragment(), new GuideThirdFragment(),new GuideFourthFragment() };

    public GuideFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}