package cn.hwwwwh.lexiangdaxue.ShoppingClass.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.activity.ShoppingActivity;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.RecyclerViewScrollDetector;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.SpacesItemDecoration;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.adapter.ShoppingAdapter;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.presenter.DownloadQuansPresenter;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.view.IQuansView;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

/**
 * Created by 97481 on 2017/1/23.
 */
public class PageFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,View.OnClickListener,IQuansView {

    public static final String PAGE="PAGE";
    public int mPage;
    public RecyclerView recyclerView;
    private BGARefreshLayout mRefreshLayout;
    private int page=1;
    private ShoppingAdapter adapter;
    private ImageView imageView;
    private ViewPager viewPager;
    //method为排序方法，0为最新上架，1最高销量，2为价格从低至高排序
    private int method=1;
    private boolean isLoad=false;
    private DownloadQuansPresenter downloadQuansPresenter;

    public static  PageFragment newInstance(int page){
        Bundle args=new Bundle();
        args.putInt(PAGE, page);
        PageFragment pageFragment=new PageFragment();
        pageFragment.setArguments(args);
        return  pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage=getArguments().getInt(PAGE);
        method=mPage;
        downloadQuansPresenter=new DownloadQuansPresenter(this);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fg_shopping);
        imageView=(ImageView) getActivity().findViewById(R.id.toTop);
        viewPager=(ViewPager)getActivity().findViewById(R.id.viewpager);
        recyclerView=getViewById(R.id.sp_recycleView);
    }

    @Override
    protected void setListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                imageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerViewScrollDetector() {
            @Override
            public void onScrollUp() {
                if(imageView.getVisibility()==View.VISIBLE){
                    imageView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScrollDown() {
                if(imageView.getVisibility()!=View.VISIBLE){
                    imageView.setVisibility(View.VISIBLE);
                }
            }
            //以滑进顶部识别区1000
            @Override
            public void onScrollTop() {
                if(imageView.getVisibility()==View.VISIBLE){
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //设置瀑布流的RecycleView
        // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new GridLayoutManager(mApp, 2, GridLayoutManager.VERTICAL, false));
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(10);
        recyclerView.addItemDecoration(decoration);
        adapter=new ShoppingAdapter(mApp);
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            isLoad = true;
            downloadQuansPresenter.download("http://cs.hwwwwh.cn/admin/quan_api.php?page=1&&method=" + method);
            //new DownloadQuans(mApp, recyclerView, page, adapter).execute("http://cs.hwwwwh.cn/admin/quan_api.php?page=1&&method=" + method);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        // 在这里加载最新数据
        if (HttpUtils.isNetConn(getContext())) {
            // 如果网络可用，则加载网络数据
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(Void aVoid) {
                    // 加载完毕后在 UI 线程结束下拉刷新
                    page=1;
                    downloadQuansPresenter.download("http://cs.hwwwwh.cn/admin/quan_api.php?page=1&&method=" + method);
                    ((ShoppingActivity)getActivity()).endRefreshing();
                }
            }.execute();
        } else {
            // 网络不可用，结束下拉刷新
            ShowToast("网络不可用");
            ((ShoppingActivity)getActivity()).endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
        if (HttpUtils.isNetConn(mApp)) {

            // 如果网络可用，则加载网络数据
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    page=page+1;
                    String url="http://cs.hwwwwh.cn/admin/quan_api.php?page="+page+"&&method="+method;
                    downloadQuansPresenter.download(url);
                    // 加载完毕后在 UI 线程结束下拉刷新
                    ((ShoppingActivity)getActivity()).endLoadingMore();
                }
            }.execute();
            return true;
        } else {
            // 网络不可用，返回 false，不显示正在加载更多
            ShowToast("网络不可用");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toTop:
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    recyclerView.smoothScrollToPosition(0);
                }else{
                    recyclerView.scrollToPosition(0);
                }
                break;
        }
    }


    @Override
    public void setView(List<QuanGoods> quanGoodses) {
        if(quanGoodses != null && quanGoodses.size()!=0&&page==1){
            adapter.setData(quanGoodses);
            recyclerView.setAdapter(adapter);
        }else if(quanGoodses != null && quanGoodses.size()!=0 && page>1){
            adapter.addMoreData(quanGoodses);
        }
        else{
            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
        }
    }


}
