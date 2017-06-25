package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.squareup.picasso.Picasso;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.hwwwwh.lexiangdaxue.MainActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.SpacesItemDecoration;
import cn.hwwwwh.lexiangdaxue.fragment.GuideTwoFragment;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

/**
 * Created by 97481 on 2017/3/4/ 0004.
 */

public class FgSecondPageFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    public static final String PAGE="PAGE";
    public int mPage;
    private int page=1;
    //method为排序方法，0为最新，1热门，2为精华
    private int method=1;
    private RecyclerView recyclerView;
    private postAdapter adapter;
    private boolean isLazyLoad=false;

    public static FgSecondPageFragment newInstance(int page){
        Bundle args=new Bundle();
        args.putInt(PAGE, page);
        FgSecondPageFragment pageFragment=new FgSecondPageFragment();
        pageFragment.setArguments(args);
        return  pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage=getArguments().getInt(PAGE);
        method=mPage;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fgsecond_fragment);
        recyclerView=getViewById(R.id.fg2_recycleView);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        recyclerView.getItemAnimator().setChangeDuration(0);
        adapter=new postAdapter(mApp);
        recyclerView.setAdapter(adapter);
        SpacesItemDecoration spacesItemDecoration=new SpacesItemDecoration(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(mApp, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(spacesItemDecoration);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Picasso.with(mApp).resumeTag(mApp);
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Picasso.with(mApp).pauseTag(mApp);
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    Picasso.with(mApp).pauseTag(mApp);
                }
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if(!isLazyLoad){
            isLazyLoad=true;
            new DownloadPostData(mApp, recyclerView, adapter,page).execute("http://cs.hwwwwh.cn/admin/postsApi.php?page=1");
        }
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(HttpUtils.isNetConn(getActivity())){
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
                    page=1;
                    // 加载完毕后在 UI 线程结束下拉刷新
                    new DownloadPostData(mApp, recyclerView, adapter,page).execute("http://cs.hwwwwh.cn/admin/postsApi.php?page=1");
                    ((GuideTwoFragment)((MainActivity)getActivity()).adapter.getItem(1)).endRefreshing();
                }
            }.execute();
        }else{
            // 网络不可用，结束下拉刷新
            ShowToast("网络不可用");
            ((GuideTwoFragment)((MainActivity)getActivity()).adapter.getItem(1)).endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        // 在这里加载更多数据，或者更具产品需求实现上拉刷新也可以
        if (HttpUtils.isNetConn(getActivity())) {

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
                    page=page+1;
                    String url="http://cs.hwwwwh.cn/admin/postsApi.php?page="+page;
                    new DownloadPostData(mApp,recyclerView,adapter,page).execute(url);
                    // 加载完毕后在 UI 线程结束下拉刷新
                    ((GuideTwoFragment)((MainActivity)getActivity()).adapter.getItem(1)).endLoadingMore();
                }
            }.execute();
            return true;
        } else {
            // 网络不可用，返回 false，不显示正在加载更多
            ShowToast("网络不可用");
            return false;
        }
    }


}
