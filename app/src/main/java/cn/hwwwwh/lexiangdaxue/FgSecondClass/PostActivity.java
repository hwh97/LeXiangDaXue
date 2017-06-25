package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

public class PostActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private BGARefreshLayout mRefreshLayout;
    private String post_uuid;;
    private List<String> data = new ArrayList<>();
    private NineGridPicLayout layout;
    private RecyclerView recyclerView;
    private RecyclerViewHeader header ;
    private ScrollView scrollView;
    private int page=1;
    private DeatailPostAdapter adapter;
    private Toolbar toolbar;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post);
        mRefreshLayout=getViewById(R.id.post_BGARefreshLayout);
        recyclerView=getViewById(R.id.reply_rv);
        toolbar=getViewById(R.id.toolbar_post);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        toolbar.setTitle("帖子详情");
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        toolbar.setTitleTextAppearance(this, R.style.TitleText);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        post_uuid=this.getIntent().getStringExtra("post_uuid");
        adapter=new DeatailPostAdapter(this);
        ArrayList<String> list=new ArrayList<String>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setHeader(recyclerView);
        adapter.setOnItemClickListener(new DeatailPostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                Toast.makeText(PostActivity.this,"touch position is "+position,Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        new DownloadReplyData(this,adapter,page,0).execute("http://cs.hwwwwh.cn/admin/ReplyPostApi.php?post_uuid="+post_uuid+"&&Page="+page);
        initRefreshLayout(mRefreshLayout);

    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.post_content, view, false);
        new DownloadDetailPostData(this,header).execute("http://cs.hwwwwh.cn/admin/SinglePostApi.php?post_uuid=" + post_uuid);
        adapter.setHeaderView(header);
    }

    public void initRefreshLayout(BGARefreshLayout refreshLayout){
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
                    new DownloadReplyData(PostActivity.this,adapter,page,0).execute("http://cs.hwwwwh.cn/admin/ReplyPostApi.php?post_uuid="+post_uuid+"&&Page=1");
                    mRefreshLayout.endRefreshing();
                }
            }.execute();
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
            // 如果网络可用，则加载网络数据
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    endLoadingMore();
                    page=page+1;
                    new DownloadReplyData(PostActivity.this,adapter,page,1).execute("http://cs.hwwwwh.cn/admin/ReplyPostApi.php?post_uuid="+post_uuid+"&&Page="+page);
                    // 加载完毕后在 UI 线程结束下拉刷新

                }
            }.execute();
            // 如果网络可用，则异步加载网络数据，并返回 true，显示正在加载更多
            return true;

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


    class DownloadReplyData extends AsyncTask<String,Void,ArrayList<ReplyBean>>{

        private Context context;
        private DeatailPostAdapter adapter;
        private int page;
        private int type;

        public DownloadReplyData(Context context,DeatailPostAdapter adapter,int page,int type){
            this.context=context;
            this.adapter=adapter;
            this.page=page;
            this.type=type;
        }

        @Override
        protected ArrayList<ReplyBean>doInBackground(String... params) {
            ArrayList<ReplyBean> list = new ArrayList<>();
            String Url = params[0];
            if (HttpUtils.isNetConn(context)) {
                byte[] b = null;
                b = HttpUtils.downloadFromNet(Url);
                String jsonString = new String(b);
                Log.d("lexiangdaxueTag", jsonString);
                try {
                    JSONArray arr = new JSONArray(jsonString);
                    for(int i=0;i<arr.length();i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        ReplyBean replyBean = new ReplyBean();
                        replyBean.setReply_id(obj.getString("Reply_id"));
                        replyBean.setReply_admin(obj.getString("Reply_admin"));
                        replyBean.setReply_uuid(obj.getString("Reply_uuid"));
                        replyBean.setReply_content(obj.getString("Reply_content"));
                        replyBean.setReply_createtime(obj.getString("Reply_createtime"));
                        replyBean.setReply_email(obj.getString("Reply_email"));
                        replyBean.setReply_postuuid(obj.getString("Reply_postuuid"));
                        replyBean.setReply_commentcount(obj.getInt("Reply_commentcount"));
                        replyBean.setReply_goodcount(obj.getInt("Reply_goodcount"));
                        if(obj.has("Reply_isposts")){
                            if(obj.getInt("Reply_isposts")==0){
                                replyBean.setReply_isReplys(true);
                            }
                        }
                        if(obj.has("ispop")){
                            replyBean.setIspop(true);
                        }
                        if(replyBean.Reply_isReplys && obj.has("Reply_replyadmin") && obj.has("Reply_replyemail") ){
                            replyBean.setReply_replyadmin(obj.getString("Reply_replyadmin"));
                            replyBean.setRepky_replyemail(obj.getString("Reply_replyemail"));
                        }
                        list.add(replyBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<ReplyBean> replyData) {
            super.onPostExecute(replyData);
            if(replyData != null && replyData.size()!=0 && page ==1){
                adapter.refreshDatas();
                adapter.popReply.clear();
                adapter.addDatas(replyData);
                recyclerView.setAdapter(adapter);
            }else if(replyData != null && replyData.size()!=0 && page>1){
                adapter.addDatas(replyData);
            } else{
                mRefreshLayout.setIsShowLoadingMoreView(false);
                Toast.makeText(context, "没有更多评论了", Toast.LENGTH_SHORT).show();
            }
            if(type==0) {
                setHeader(recyclerView);
                recyclerView.setAdapter(adapter);
            }

        }

    }

}
