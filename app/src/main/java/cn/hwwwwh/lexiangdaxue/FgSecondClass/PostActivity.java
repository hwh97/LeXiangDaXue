package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.ytying.emoji.SmileLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

public class PostActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate  {

    private BGARefreshLayout mRefreshLayout;
    private String post_uuid;;
    private List<String> data = new ArrayList<>();
    private NineGridPicLayout layout;
    private RecyclerView recyclerView;
    private RecyclerViewHeader header ;
    private ScrollView scrollView;
    private int page=1;
    private DeatailReplyAdapter adapter;
    private Toolbar toolbar;
    private ImageView img_reoly;
    private AppCompatEditText edit_reply;
    private LinearLayout activity_post;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private ImageView img_emoji;
    private SmileLayout smileLayout;
    private View view;
    private LinearLayout click_view;
    private LinearLayout reply_view;
    private TextView cancel_reply;
    private Button reply_btn;
    private ProgressDialog pDialog;
    private SQLiteHandler sqLiteHandler;
    public HashMap<String,String> hashMap;
    public boolean isReplyPost=true;
    public ReplyBean replyBean;
    //用户信息
    private SessionManager session;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post);
        mRefreshLayout=getViewById(R.id.post_BGARefreshLayout);
        recyclerView=getViewById(R.id.reply_rv);
        toolbar=getViewById(R.id.toolbar_post);
        img_reoly=getViewById(R.id.img_reoly);
        activity_post=getViewById(R.id.activity_post);
        click_view=getViewById(R.id.click_view);
        reply_view=getViewById(R.id.reply_view);
        edit_reply= getViewById(R.id.edit_reply);
        img_emoji=getViewById(R.id.reply_emoji);
        smileLayout = getViewById((R.id.write_smile_panel));
        cancel_reply=getViewById(R.id.cancel_reply);
        reply_btn=getViewById(R.id.reply_btn);
        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(true);
        session=new SessionManager(this);
        sqLiteHandler=new SQLiteHandler(this);
        hashMap=sqLiteHandler.getUserDetails();

    }

    @Override
    protected void setListener() {
        activity_post.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
                //System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
                //System.out.println(left + " " + top +" " + right + " " + bottom);
                //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
                if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
                    //keyboard pop-up

                }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)&&smileLayout.getVisibility()!=View.VISIBLE){
                    //keyboard pop-down
                    click_view.setVisibility(View.VISIBLE);
                    reply_view.setVisibility(View.GONE);
                }
            }
        });
        recyclerView.setOnTouchListener(this);
        img_reoly.setOnClickListener(this);
        img_emoji.setOnClickListener(this);
        edit_reply.setOnClickListener(this);
        reply_view.setOnClickListener(this);
        cancel_reply.setOnClickListener(this);
        reply_btn.setOnClickListener(this);

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
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        adapter=new DeatailReplyAdapter(this);
        ArrayList<String> list=new ArrayList<String>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        setHeader(recyclerView);
        adapter.setOnItemClickListener(new DeatailReplyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                replyBean=(ReplyBean)data;
                click_view.setVisibility(View.GONE);
                reply_view.setVisibility(View.VISIBLE);
                showKeyboard();
                edit_reply.setHint("回复" + replyBean.getReply_admin());
                if(!edit_reply.getHint().equals("回复" + replyBean.getReply_admin())){
                    edit_reply.setText("");
                }
                isReplyPost=false;
            }
        });
        recyclerView.setAdapter(adapter);
        new DownloadReplyData(this,adapter,page,1).execute("http://cs.hwwwwh.cn/admin/ReplyPostApi.php?post_uuid="+post_uuid+"&&Page="+page);
        initRefreshLayout(mRefreshLayout);
        smileLayout.setVisibility(View.GONE);
        //!!!初始化，这句话一定要加
        smileLayout.init(edit_reply);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_UP :
                Log.d("testlex","222");
                click_view.setVisibility(View.VISIBLE);
                reply_view.setVisibility(View.GONE);
                smileLayout.setVisibility(View.GONE);
                break;
            case MotionEvent.ACTION_CANCEL:
                click_view.setVisibility(View.VISIBLE);
                reply_view.setVisibility(View.GONE);
                smileLayout.setVisibility(View.GONE);
                break;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.activity_post:

                break;
            case R.id.img_reoly:
                if(!edit_reply.getHint().equals("写点评论吧")){
                    edit_reply.setText("");
                }
                isReplyPost=true;
                edit_reply.setHint("写点评论吧");
                click_view.setVisibility(View.GONE);
                reply_view.setVisibility(View.VISIBLE);
                showKeyboard();
                break;
            case R.id.reply_emoji:
                hideKeyBoard();
                if(smileLayout.getVisibility()==View.GONE) {
                    smileLayout.setVisibility(View.VISIBLE);
                }else{
                    smileLayout.setVisibility(View.GONE);
                    showKeyboard();
                }
                break;
            case R.id.edit_reply:
                smileLayout.setVisibility(View.GONE);
                showKeyboard();
                break;
            case R.id.reply_view:

                break;
            case R.id.cancel_reply:
                click_view.setVisibility(View.VISIBLE);
                reply_view.setVisibility(View.GONE);
                smileLayout.setVisibility(View.GONE);
                break;
            case R.id.reply_btn:
                if(!session.isLoggedIn())
                    logoutUser();
                else {
                    Log.d("test", edit_reply.getText().toString());
                    if (isReplyPost) {
                        sendPost(hashMap, post_uuid, edit_reply.getText().toString(), "1");
                    } else {
                        sendPost(hashMap, replyBean, post_uuid, edit_reply.getText().toString(), "0");
                    }
                    click_view.setVisibility(View.VISIBLE);
                    reply_view.setVisibility(View.GONE);
                    smileLayout.setVisibility(View.GONE);
                    edit_reply.setText("");
                    beginRefreshing();
                }
                break;
        }

    }

    /**
     * 隐藏键盘
     */

    public void hideKeyBoard(){
        if (getWindow().getAttributes().softInputMode != 	WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN ) {
            if (getCurrentFocus() != null) {
                InputMethodManager manager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 弹出键盘
     */
    public void showKeyboard() {
        edit_reply.setFocusable(true);
        edit_reply.setFocusableInTouchMode(true);
        edit_reply.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                edit_reply.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit_reply, 0);
    }

    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.post_content, view, false);
        new DownloadDetailPostData(this,header).execute("http://cs.hwwwwh.cn/admin/SinglePostApi.php?post_uuid=" + post_uuid+"&&email="+hashMap.get("email"));
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
        private DeatailReplyAdapter adapter;
        private int page;
        private int type;
        private SessionManager sessionManager;
        private SQLiteHandler sqLiteHandler;

        public DownloadReplyData(Context context, DeatailReplyAdapter adapter, int page, int type){
            this.context=context;
            this.adapter=adapter;
            this.page=page;
            this.type=type;
            sessionManager=new SessionManager(context);
            sqLiteHandler=new SQLiteHandler(context);
        }

        @Override
        protected ArrayList<ReplyBean>doInBackground(String... params) {
            ArrayList<ReplyBean> list = new ArrayList<>();
            String Url = params[0];
            if (HttpUtils.isNetConn(context)) {
                byte[] b = null;
                if(sessionManager.isLoggedIn()){
                    HashMap<String,String> hashMap=sqLiteHandler.getUserDetails();
                    String email=hashMap.get("email");
                    b=HttpUtils.downloadFromNet(Url+"&&email="+email);
                }else{
                    b =HttpUtils.downloadFromNet(Url);
                }
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
                        if(obj.has("Reply_isZan")){
                            replyBean.setZan(true);
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

    public void sendPost(final HashMap<String, String> hashMap, final String post_uuid, final String content,final String isposts){
        pDialog.setMessage("提交数据中...");
        showDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlHandleReply, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject obj=new JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    if (!error){
                        Log.d("Testlexiangdaxue","回复成功");
                    }else{
                        String info=null;
                        if(obj.has("error_msg")) {
                            info = obj.getString("error_msg");
                        }
                        Log.d("Testlexiangdaxue","传输参数失败"+info);
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("lexiangdaxueError", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("postTime", String.valueOf(System.currentTimeMillis()).substring(0,10));
                params.put("name",hashMap.get("name"));
                params.put("email",hashMap.get("email"));
                params.put("content",content);
                params.put("post_uuid",post_uuid);
                params.put("isposts",isposts);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,"PostActivity");
    }

    public void sendPost(final HashMap<String, String> hashMap,final ReplyBean replyBean, final String post_uuid, final String content,final String isposts){
        pDialog.setMessage("提交数据中...");
        showDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlHandleReply, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject obj=new JSONObject(response);
                    Log.d("Testlexiangdaxue",response);
                    boolean error = obj.getBoolean("error");
                    if (!error){
                        Log.d("Testlexiangdaxue","回复成功");
                    }else{
                        String info=null;
                        if(obj.has("error_msg")) {
                            info = obj.getString("error_msg");
                        }
                        Log.d("Testlexiangdaxue","传输参数失败"+info);
                        ShowToast("未知错误");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("lexiangdaxueError", error.getMessage(), error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("postTime", String.valueOf(System.currentTimeMillis()).substring(0,10));
                params.put("name",hashMap.get("name"));
                params.put("email",hashMap.get("email"));
                params.put("content",content);
                params.put("post_uuid",post_uuid);
                params.put("isposts",isposts);
                params.put("reply_uuid",replyBean.getReply_uuid());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,"PostActivity");
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void logoutUser(){
        session.setLogin(false);
        sqLiteHandler.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
