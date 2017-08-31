package cn.hwwwwh.lexiangdaxue.ShoppingClass.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.fragment.PageFragment;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ObservableScrollView;

public class ShoppingActivity extends BaseActivity implements
        XBanner.XBannerAdapter ,BGARefreshLayout.BGARefreshLayoutDelegate{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private ViewPager viewPager;
    private XBanner xBanner;
    private List<String> imgesUrl;
    private ObservableScrollView observableScrollView;
    private RelativeLayout sp_parent;
    private BGARefreshLayout mRefreshLayout;
    private ScrollView scrollView;
    private Fragment[] mFragments;
    private  List<QuanGoods> list=null;
    private ImageView imageView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping);
        toolbar=(Toolbar)findViewById(R.id.toolbar_shopping);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mFragments = new Fragment[3];
        mFragments[0]  = PageFragment.newInstance(0);
        mFragments[1] = PageFragment.newInstance(1);
        mFragments[2] = PageFragment.newInstance(2);
        fragmentPagerAdapter=new cn.hwwwwh.lexiangdaxue.ShoppingClass.adapter.FragmentPagerAdapter
                (getSupportFragmentManager(),mFragments);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
//        observableScrollView=(ObservableScrollView)findViewById(R.id.sp_OS);
//        observableScrollView.setFillViewport(true);
//        observableScrollView.setScrollViewListener(this);
        xBanner=(XBanner)findViewById(R.id.banner_2);
        intiBanner();
        initRefreshLayout(mRefreshLayout);
        imageView=(ImageView)findViewById(R.id.toTop);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (viewPager.getCurrentItem()) {
                    case 0:
                        ((PageFragment) mFragments[0]).onClick(imageView);
                        break;
                    case 1:
                        ((PageFragment) mFragments[1]).onClick(imageView);
                        break;
                    case 2:
                        ((PageFragment) mFragments[2]).onClick(imageView);
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void intiBanner(){
        //xbanner
        imgesUrl = new ArrayList<>();
        //使用接口控制轮播图
        //imgesUrl.add("http://pic36.nipic.com/20131204/13933127_191106308385_2.jpg");
        //imgesUrl.add("http://pic.58pic.com/58pic/17/71/73/67n58PICcfh_1024.jpg");
        imgesUrl.add("http://pic.90sjimg.com/back_pic/qk/back_origin_pic/00/02/18/f41fe46d542cebac3521b0a510c80965.jpg");
        imgesUrl.add("http://pic.90sjimg.com/back_pic/qk/back_origin_pic/00/02/02/14f733a0b5e00c5c3c1f4051d6e8a490.jpg");
        xBanner.setData(imgesUrl, null);
        xBanner.setmAdapter(this);
        xBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                Toast.makeText(ShoppingActivity.this, "点击了第" + position + "图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadBanner(XBanner banner, View view, int position) {
        Glide.with(this).load(imgesUrl.get(position)).into((ImageView) view);
    }

    public void initRefreshLayout(BGARefreshLayout refreshLayout){
        mRefreshLayout=(BGARefreshLayout)findViewById(R.id.rl_shopping);
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this, true);
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
        if (HttpUtils.isNetConn(this)) {
            // 如果网络可用，则加载网络数据
            switch (viewPager.getCurrentItem()){
                case 0:
                    ((PageFragment)mFragments[0]).onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                    break;
                case 1:
                    ((PageFragment)mFragments[1]).onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                    break;
                case 2:
                    ((PageFragment)mFragments[2]).onBGARefreshLayoutBeginRefreshing(mRefreshLayout);
                    break;
                default:
                break;
            }

        } else {
            // 网络不可用，结束下拉刷新
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以

        if (HttpUtils.isNetConn(this)) {
            // 如果网络可用，则异步加载网络数据，并返回 true，显示正在加载更多
            switch (viewPager.getCurrentItem()) {
                case 0:
                    return ((PageFragment) mFragments[0]).onBGARefreshLayoutBeginLoadingMore(mRefreshLayout);
                case 1:
                    return ((PageFragment) mFragments[1]).onBGARefreshLayoutBeginLoadingMore(mRefreshLayout);
                case 2:
                    return ((PageFragment) mFragments[2]).onBGARefreshLayoutBeginLoadingMore(mRefreshLayout);
                default:
                    return true;
            }
        } else {
            // 网络不可用，返回 false，不显示正在加载更多
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
