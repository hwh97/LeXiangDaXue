package cn.hwwwwh.lexiangdaxue.FgFirstClass.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.Data;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.HomeData;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.bean.UserBean;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.presenter.DownloadHomeDataPresenter;
import cn.hwwwwh.lexiangdaxue.FgFirstClass.view.IHomeDataView;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.activity.ShoppingActivity;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ObservableScrollView;
import cn.hwwwwh.lexiangdaxue.other.RxBus;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.activity.selectActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


import static android.app.Activity.RESULT_OK;


public class GuideOneFragment extends BaseFragment implements XBanner.XBannerAdapter,
        View.OnClickListener, ObservableScrollView.ScrollViewListener,BGARefreshLayout.BGARefreshLayoutDelegate,IHomeDataView{
    private XBanner xBanner;
    private List<String> imgesUrl=new ArrayList<>();;
    private View lin_fruit;
    private View lin_lingshi;
    private View lin_breakfast;
    private View lin_Shopping;
    private TextView title_Main;
    private int imageHeight;
    private ObservableScrollView scrollView;
    private TextView title_Main2;
    private View title_Main_layout;
    private RelativeLayout title_Main_layout2;
    private ImageView localicon_icon;
    private ImageView zhezhao;
    private int height;
    private BGARefreshLayout mRefreshLayout;
    private TextView sy_quan_text;
    private ImageView sy_quan_img;
    private LinearLayout home_quan;
    private RelativeLayout home_right_1;
    private TextView home_quan_text;
    private LinearLayout PopActivity;
    private Subscription rxSbscription;
    private String url;
    private SQLiteHandler db;
    private RxBus rxBus;
    private DownloadHomeDataPresenter downloadHomeDataPresenter;
    private List<Data.RotationPicBean> list=new ArrayList<>();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fg1);
        xBanner=getViewById(R.id.banner_1);
        lin_fruit=getViewById(R.id.fruit);
        lin_lingshi=getViewById(R.id.lingshi);
        lin_breakfast=getViewById(R.id.breakfastView);
        lin_Shopping=getViewById(R.id.Shopping);
        title_Main=getViewById(R.id.title_Main);
        scrollView=getViewById(R.id.scrollview);
        title_Main2=getViewById(R.id.title_Main2);
        title_Main_layout=getViewById(R.id.title_Main_layout);
        title_Main_layout2=getViewById(R.id.title_Main2_layout);
        localicon_icon=getViewById(R.id.localicon_icon);
        zhezhao = getViewById(R.id.zhezhao);
        mRefreshLayout=getViewById(R.id.mRefreshLayout);
        sy_quan_text=getViewById(R.id.sy_quan_text);
        sy_quan_img=getViewById(R.id.sy_quan_img);
        home_quan=getViewById(R.id.home_quan);
        home_right_1=getViewById(R.id.home_right_1);
        home_quan_text=getViewById(R.id.home_quan_text);
        PopActivity=getViewById(R.id.PopActivity);
    }

    @Override
    protected void setListener() {
        xBanner.setOnItemClickListener(new XBanner.OnItemClickListener() {
            @Override
            public void onItemClick(XBanner banner, int position) {
                if(list.size()!=0)
                ShowToast(list.get(position).getApppic_linkurl());
            }
        });
        lin_fruit.setOnClickListener(this);
        lin_lingshi.setOnClickListener(this);
        lin_breakfast.setOnClickListener(this);
        lin_Shopping.setOnClickListener(this);
        title_Main_layout.setOnClickListener(this);
        title_Main_layout2.setOnClickListener(this);
        scrollView.setScrollViewListener(this);
        home_quan.setOnClickListener(this);
        home_right_1.setOnClickListener(this);
        db=new SQLiteHandler(mApp);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        localicon_icon.setAlpha(0);
        ViewGroup.MarginLayoutParams margin2 = new ViewGroup.MarginLayoutParams(title_Main_layout2.getLayoutParams());
        margin2.setMargins(margin2.leftMargin, 8, margin2.rightMargin, margin2.bottomMargin);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(margin2);
        layoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        title_Main_layout2.setLayoutParams(layoutParams2);
        ViewGroup.MarginLayoutParams margin3 = new ViewGroup.MarginLayoutParams(zhezhao.getLayoutParams());
        margin3.setMargins(margin3.leftMargin, margin3.topMargin, margin3.rightMargin, margin3.bottomMargin);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(margin3);
        layoutParams3.height = layoutParams2.height + 8+8;
        zhezhao.setLayoutParams(layoutParams3);
        if(!getUniDetails().isEmpty()){
            String uni=getUniDetails().get("uu_name");
            initSchool(uni);
        }
        downloadHomeDataPresenter=new DownloadHomeDataPresenter(this);
        initRxBus();
        initListeners();
        initRefreshLayout();
        xBanner.setmAdapter(this);
    }


    private void initRxBus() {
        rxBus = RxBus.getIntanceBus();
        registerRxBus(String.class, new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if(!s.isEmpty()){
                    initSchool(s);
                }
            }
        });
    }

    //注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Log.e("NewsMainPresenter", throwable.toString());
            }
        });
        rxBus.addSubscription(this,disposable);
    }

    @Override
    protected void lazyLoad() {
        if (!isLoad) {
            isLoad = true;
            if (isLoggedIn()) {
                //url= AppConfig.urlHomeApi+" && uid="+getUserDetails().get("uid");
            } else {
                // url= AppConfig.urlHomeApi;
            }
            url = AppConfig.urlHomeApi;
            downloadHomeDataPresenter.downloadData(url, AppConfig.urlHomeUvApi, getUserDetails().get("token"), getPhoneId());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        xBanner.startAutoPlay();
    }

    @Override
    public void onDestroy() {
        rxBus.unSubscribe(this);
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        xBanner.stopAutoPlay();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.fruit ||v.getId()==R.id.lingshi||v.getId()==R.id.breakfastView){
            if(!isLoggedIn()){
                logoutUser();
                return;
            }
            if(!isSelectUni()){
                logoutUniversity();
                return;
            }
        }
        Intent intent=new Intent(mApp, ProductActivity.class);
        Bundle bundle=new Bundle();
        switch(v.getId()){
            case R.id.fruit:
                bundle.putString("column","水果");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.lingshi:
                bundle.putString("column", "零食");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.breakfastView:
                bundle.putString("column", "早餐");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.Shopping:
                Intent intent2=new Intent(mApp, ShoppingActivity.class);
                startActivity(intent2);
                break;
            case R.id.title_Main_layout:case R.id.title_Main2_layout:
                if(isLoggedIn()) {
                    Intent intent3 = new Intent(mApp, selectActivity.class);
                    startActivityForResult(intent3, 2000);
                }else
                    logoutUser();
                break;
            case R.id.home_quan:
                onClick(lin_Shopping);
                break;
            case R.id.home_right_1:
                onClick(lin_breakfast);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && requestCode==2000){
            String uni=data.getStringExtra("university");
            initSchool(uni);
        }
    }

    public void initSchool(String uni){
        if(uni.length()>9){
            uni=uni.substring(0,4)+".."+uni.substring(uni.length()-4,uni.length());
        }
        title_Main.setText(uni);
        title_Main2.setText(uni);
    }

    /**
     * 获取顶部图片高度后，设置滚动监听
     */
    private void initListeners() {

        ViewTreeObserver vto = xBanner.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                xBanner.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                imageHeight = xBanner.getHeight()/4;
            }
        });

       height=title_Main_layout2.getLayoutParams().height;
    }

    /**
     * 滑动监听
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        // TODO Auto-generated method stub
        if (y <= height) {  //设置标题的背景颜色
            title_Main_layout.setBackgroundColor(Color.argb((int) 0, 144, 151, 166));
            title_Main.setTextColor(Color.argb((int) 0, 0, 0, 0));
            localicon_icon.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            localicon_icon.setImageAlpha(0);
            title_Main.setBackgroundColor(Color.argb((int) 0, 0, 0, 0));
        } else if (y > height && y <= imageHeight+height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
            float scale = (float) (y-height)/ imageHeight;
            float alpha = (255 * scale);
            title_Main.setTextColor(Color.argb((int) alpha, 0, 0, 0));
            title_Main.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
            title_Main_layout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
            localicon_icon.setImageAlpha((int) alpha);
            localicon_icon.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getActivity().getWindow();
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
///                       | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                if(y>height*1.2){
//                    window.setStatusBarColor(Color.WHITE);
//                }else {
//                    window.setStatusBarColor(Color.WHITE);
//                }
//            }
        } else {  //滑动到banner下面设置普通颜色
            title_Main.setTextColor(Color.argb((int) 255, 0, 0, 0));
            title_Main.setBackgroundColor(Color.argb((int) 255, 255,255,255));
            title_Main_layout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            localicon_icon.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
            localicon_icon.setImageAlpha(255);
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getActivity().getWindow();
////                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
////                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
////                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
////                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(Color.WHITE);
//            }
        }

    }

    public void addPopActivity(List<Data.ActivityPicBean> list){
        PopActivity.removeAllViews();
        for(final Data.ActivityPicBean data:list){
            ImageView imageView = new ImageView(mApp);
            imageView.setPadding(0,5,0,5);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            DisplayMetrics metric = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
            int width = metric.widthPixels;     // 屏幕宽度（像素）
            int height = metric.heightPixels;   // 屏幕高度（像素）
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height/5);
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowToast("linkUrl:"+data.getApppic_linkurl());
                }
            });
            Glide.with(mApp).load(data.getApppic_url()).placeholder(R.drawable.loadpic).into(imageView);
            PopActivity.addView(imageView);
        }
    }

    public void addRotationPic(List<Data.RotationPicBean> list){
        this.list=list;
        //xbanner
        imgesUrl.clear();
        //使用接口控制轮播图
        for(Data.RotationPicBean bean :list){
            imgesUrl.add(bean.getApppic_url());
        }
        xBanner.setData(imgesUrl, null);
    }

    public void initRefreshLayout(){
        // 为BGARefreshLayout 设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mApp, false);
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
                    if (isLoggedIn()){
                        //url= AppConfig.urlHomeApi+" && uid="+db.getUserDetails().get("uid");
                    }
                    downloadHomeDataPresenter.downloadData(AppConfig.urlHomeApi,AppConfig.urlHomeUvApi,getUserDetails().get("token"),getPhoneId());
                    mRefreshLayout.endRefreshing();
                }
            }.execute();
        } else {
            // 网络不可用，结束下拉刷新
            ShowToast("网络不可用");
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
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
    public void setView(HomeData homeData) {
        //更新UI
        Glide.with(mApp).load(homeData.getPic()).placeholder(R.drawable.loadpic).into(sy_quan_img);
        sy_quan_text.setText("券后价"+homeData.getPrice()+"元");
        home_quan_text.setText(homeData.getD_title());
        addPopActivity(homeData.getPicData());
        addRotationPic(homeData.getRotationPic());
        if(homeData.getUserBean()!=null){
            UserBean school=homeData.getUserBean();
            if(school.getUni().getUu_name().equals(getUniDetails().get("uu_name"))) {
                initSchool(homeData.getUserBean().getUni().getUu_name());
            }else{
                db.updateUniversity(school.getUni().getUser_uuid(),school.getUni().getUu_province(),school.getUni().getUu_city(),school.getUni().getUu_name(),school.getUni().getUniversity_id()+"");
                initSchool(homeData.getUserBean().getUni().getUu_name());
            }
        }else{
            db.deleteUniversity();
            initSchool("绑定所在学校");
        }
    }

    @Override
    public void setFailView() {
        ShowToast("网络出错");
    }

    @Override
    public void setUniFailView(String error) {
        ShowToast(error);
    }

    @Override
    public void loadBanner(XBanner banner, Object model, View view, int position) {
        Glide.with(this).load(imgesUrl.get(position)).into((ImageView) view);
    }

}