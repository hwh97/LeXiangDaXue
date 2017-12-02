package cn.hwwwwh.lexiangdaxue.FgSecondClass.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.activity.EditActivity;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.fragment.FgSecondPageFragment;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

import static android.app.Activity.RESULT_OK;

public class GuideTwoFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,View.OnClickListener {
    private TabLayout fg2_tabLayout;
    private fg2_FragmentPagerAdapter fg2_fragmentPagerAdapter;
    private ViewPager fg2_viewPager;
    private Fragment[] mFragments;
    private BGARefreshLayout mRefreshLayout;
    private Toolbar fg2_Toolbar;
    private boolean isLazyLoad=false;
    private ImageView edit;
    private ImageView toTop;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fgsecond,getActivity());
        fg2_tabLayout=getViewById(R.id.fg2_tabLayout);
        fg2_viewPager=getViewById(R.id.fg2_viewpager);
        mRefreshLayout=getViewById(R.id.fg2_BGARefreshLayout);
        fg2_Toolbar=getViewById(R.id.fg2_toolbar);
        toTop=getViewById(R.id.toTop);
        edit=getViewById(R.id.edit);
    }

    @Override
    protected void setListener() {
        edit.setOnClickListener(this);
        toTop.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initRefreshLayout(mRefreshLayout);
        fg2_Toolbar.setTitle("发现");
        fg2_Toolbar.setTitleTextAppearance(getContext(),R.style.WindowTitle);
    }

    //延迟加载网络数据
    @Override
    protected void lazyLoad() {
        if(!isLazyLoad){
            isLazyLoad=true;
            loadTab();
        }
    }

    public void loadTab(){
        mFragments = new Fragment[2];
        mFragments[0]  = FgSecondPageFragment.newInstance(0);
        mFragments[1] = FgSecondPageFragment.newInstance(1);
        fg2_fragmentPagerAdapter=new fg2_FragmentPagerAdapter(getFragmentManager(),mFragments);
        fg2_viewPager.removeAllViewsInLayout();
        fg2_viewPager.removeAllViews();
        fg2_viewPager.setAdapter(fg2_fragmentPagerAdapter);
        fg2_viewPager.setOffscreenPageLimit(2);
        fg2_tabLayout.setupWithViewPager(fg2_viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit:
                //if logged keep way
                if (!isLoggedIn()) {
                    logoutUser();
                }
                else {
                    HashMap<String,String> userDeatil=getUserDetails();
                    String name=userDeatil.get("name");
                    String email=userDeatil.get("email");
                    String uid=userDeatil.get("uid");
                    Intent intent = new Intent(getActivity(), EditActivity.class);
                    startActivityForResult(intent,2000);
                }
                break;
            case R.id.toTop:
                switch (fg2_viewPager.getCurrentItem()){
                    case 0:
                        ((FgSecondPageFragment) mFragments[0]).onClick(toTop);
                        break;
                    case 1:
                        ((FgSecondPageFragment) mFragments[1]).onClick(toTop);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            fg2_viewPager.setCurrentItem(0);
           beginRefreshing();
        }
    }


    public class fg2_FragmentPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT=2;
        private String tabTitles[]=new String[]{"最新","热门"};
        private Context context;
        private Fragment[] fragments;

        public fg2_FragmentPagerAdapter(FragmentManager fm, Fragment[] fragments) {
            super(fm);
            this.fragments=fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
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

    public void initRefreshLayout(BGARefreshLayout refreshLayout){
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
        //refreshViewHolder.setLoadingMoreText("正在刷新");
        // 设置整个加载更多控件的背景颜色资源 id
        // refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景 drawable 资源 id
        // refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
        // refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景 drawable 资源 id
        //refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
        //mRefreshLayout.setCustomHeaderView(mBanner, false);
        // 可选配置  -------------END
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据
        if (HttpUtils.isNetConn(getActivity())) {
            // 如果网络可用，则加载网络数据
            switch (fg2_viewPager.getCurrentItem()){
                case 0:
                    ((FgSecondPageFragment)mFragments[0]).onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                    break;
                case 1:
                    ((FgSecondPageFragment)mFragments[1]).onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                    break;
                default:
                    break;
            }

        } else {
            // 网络不可用，结束下拉刷新
            Toast.makeText(getActivity(), "网络不可用", Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

        if (HttpUtils.isNetConn(getActivity())) {
            // 如果网络可用，则异步加载网络数据，并返回 true，显示正在加载更多
            switch (fg2_viewPager.getCurrentItem()) {
                case 0:
                    return ((FgSecondPageFragment) mFragments[0]).onBGARefreshLayoutBeginLoadingMore(mRefreshLayout);
                case 1:
                    return ((FgSecondPageFragment) mFragments[1]).onBGARefreshLayoutBeginLoadingMore(mRefreshLayout);
                default:
                    return true;
            }
        } else {
            // 网络不可用，返回 false，不显示正在加载更多
            Toast.makeText(getActivity(), "网络不可用", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // 通过代码方式控制进入正在刷新状态。应用场景：某些应用在 activity 的 onStart 方法中调用，自动进入正在刷新状态获取最新数据
    public void beginRefreshing() {
        mRefreshLayout.beginRefreshing();
    }

    // 通过代码方式控制进入加载更多状态
    public void beginLoadingMore() {
        mRefreshLayout.beginLoadingMore();
    }

    public void endRefreshing() {
        mRefreshLayout.endRefreshing();
    }

    public void endLoadingMore() {
        mRefreshLayout.endLoadingMore();
    }

}
