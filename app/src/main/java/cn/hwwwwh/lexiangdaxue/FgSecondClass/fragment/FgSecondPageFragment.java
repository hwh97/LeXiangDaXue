package cn.hwwwwh.lexiangdaxue.FgSecondClass.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.adapter.postAdapter;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.postData;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.presenter.DownloadPostPresenter;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.view.IPostView;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.MainActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.RecyclerViewScrollDetector;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.SpacesItemDecoration;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

/**
 * Created by 97481 on 2017/3/4/ 0004.
 */

public class FgSecondPageFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,View.OnClickListener,IPostView{

    public static final String PAGE="PAGE";
    public int mPage;
    private int page=1;
    //method为排序方法，0为最新，1热门
    private int method=1;
    private RecyclerView recyclerView;
    private postAdapter adapter;
    private boolean isLazyLoad=false;
    private ImageView toTop;
    private ViewPager fg2_viewPager;
    private DownloadPostPresenter downloadPostPresenter;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;

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
        downloadPostPresenter=new DownloadPostPresenter(this);
        sessionManager=new SessionManager(mApp);
        sqLiteHandler=new SQLiteHandler(mApp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fgsecond_fragment);
        recyclerView=getViewById(R.id.fg2_recycleView);
        toTop=(ImageView)getActivity().findViewById(R.id.toTop);
        fg2_viewPager = (ViewPager) getActivity().findViewById(R.id.fg2_viewpager);
    }

    @Override
    protected void setListener() {
        fg2_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toTop.setVisibility(View.INVISIBLE);
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
                if(toTop.getVisibility()==View.VISIBLE){
                    toTop.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScrollDown() {
                if(toTop.getVisibility()!=View.VISIBLE){
                    toTop.setVisibility(View.VISIBLE);
                }
            }
            //以滑进顶部识别区1000
            @Override
            public void onScrollTop() {
                if(toTop.getVisibility()==View.VISIBLE){
                    toTop.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        recyclerView.getItemAnimator().setChangeDuration(0);
        adapter=new postAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        SpacesItemDecoration spacesItemDecoration=new SpacesItemDecoration(1);
        recyclerView.setLayoutManager(new LinearLayoutManager(mApp, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(spacesItemDecoration);
    }

    @Override
    protected void lazyLoad() {
        if(!isLazyLoad){
            isLazyLoad=true;
            downloadPostPresenter.download(handleUrl("http://cs.hwwwwh.cn/admin/postsApi.php?page=1"+"&method="+method));
            //new DownloadPostData(mApp, recyclerView, adapter,page).execute("http://cs.hwwwwh.cn/admin/postsApi.php?page=1"+"&method="+method);
        }
    }

    public String handleUrl(String url){
        if(sessionManager.isLoggedIn()){
            HashMap<String,String> hashMap=sqLiteHandler.getUserDetails();
            String email=hashMap.get("email");
            url=url+"&&email="+email;
        }
        return url;
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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if(HttpUtils.isNetConn(mApp)){
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
                    downloadPostPresenter.download(handleUrl("http://cs.hwwwwh.cn/admin/postsApi.php?page=1"+"&method="+method));
                    // new DownloadPostData(mApp, recyclerView, adapter,page).execute("http://cs.hwwwwh.cn/admin/postsApi.php?page=1"+"&method="+method);
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
        if (HttpUtils.isNetConn(mApp)) {

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
                    String url="http://cs.hwwwwh.cn/admin/postsApi.php?page="+page+"&method="+method;
                    downloadPostPresenter.download(handleUrl(url));
                    //new DownloadPostData(mApp,recyclerView,adapter,page).execute(url);
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


    @Override
    public void setView(List<postData> postDatas) {
        if(postDatas != null && postDatas.size()!=0 && page ==1){
            adapter.setData(postDatas);
            recyclerView.setAdapter(adapter);
        }else if(postDatas != null && postDatas.size()!=0 && page>1){
            adapter.addMoreData(postDatas);
        } else{
            ShowToast("没有更多数据了");
        }
    }

    @Override
    public void downloadFail() {
        ShowToast("没有更多数据了");
    }

}


