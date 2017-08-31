package cn.hwwwwh.lexiangdaxue.FgFourthClass.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.Activity.AddressActivity;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.Activity.userinfoActivity;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.userBean;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter.DownloadUserPresenter;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter.IDownloadUserPresenter;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.view.IUserView;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.BlurBitmapUtil;
import cn.hwwwwh.lexiangdaxue.other.RxBus;
import cn.hwwwwh.lexiangdaxue.other.XCRoundImageView;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.activity.selectActivity;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.Subscription;
import rx.functions.Action1;

public class GuideFourthFragment extends BaseFragment implements View.OnClickListener,IUserView {
    private ImageView iv;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private AppBarLayout app_bar;
    private XCRoundImageView head;
    private TextView user;
    private XCRoundImageView head1;
    private TextView user1;
    private LinearLayout fab2;
    private LinearLayout fab;
    private Subscription rxSbscription;
    private LinearLayout address_view;
    private DownloadUserPresenter downloadUserPresenter;
    private Button logout_btn;


    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fg4,getActivity());
        app_bar=getViewById(R.id.app_bar);
        head=getViewById(R.id.head);
        user=getViewById(R.id.user);
        head1=getViewById(R.id.head1);
        user1=getViewById(R.id.user1);
        fab=getViewById(R.id.fab);
        fab2=getViewById(R.id.fab2);
        address_view=getViewById(R.id.address_view);
        logout_btn=getViewById(R.id.logout_btn);
    }

    @Override
    protected void setListener() {
        fab.setOnClickListener(this);
        fab2.setOnClickListener(this);
        address_view.setOnClickListener(this);
        logout_btn.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isLoggedIn()){
            downloadUserPresenter.download("http://cs.hwwwwh.cn/admin/userinfo.php?unique_id="+getUserDetails().get("uid"));
        }else{
            setFailView();
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        collapsingToolbarLayout = getViewById(R.id.collapsing_toolbar_layout);
        iv = getViewById(R.id.iv);
        toolbar = getViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.mine_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_setting:
                        ShowToast("setting");
                        break;
                }
                return true;
            }
        });
        //collapsingToolbarLayout.setTitle("DesignLibrarySample");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.GREEN);
        collapsingToolbarLayout.setExpandedTitleColor(Color.GREEN);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int scrollRangle = appBarLayout.getTotalScrollRange();
                //初始verticalOffset为0，不能参与计算。
                if (verticalOffset == 0) {
                    head.setAlpha(1f);
                    user.setAlpha(1f);

                    head1.setAlpha(0f);
                    user1.setAlpha(0f);
                } else {
                    //保留一位小数
                    float alpha = 1.0f * verticalOffset / scrollRangle;
                    Log.d("lexiantest",1+(1.0f * verticalOffset / scrollRangle) +"");
                    //ShowToast(1-alpha+"");
                    head.setAlpha(1+alpha);
                    user.setAlpha(1+alpha);
                    if((1+alpha)<0.3) {
                        head.setAlpha(0f);
                        user.setAlpha(0f);
                        head1.setAlpha(-1f * alpha);
                        user1.setAlpha(-1f * alpha);
                    }
                    if((1+alpha)>0.4){
                        head1.setAlpha(0f);
                        user1.setAlpha(0f);
                    }
                }
            }
        });
        downloadUserPresenter=new DownloadUserPresenter(this);
        if(isLoggedIn()){
            downloadUserPresenter.download("http://cs.hwwwwh.cn/admin/userinfo.php?unique_id="+getUserDetails().get("uid"));
        }else{
            setFailView();
        }
        rxSbscription = RxBus.getInstance().toObserverable(userBean.class)
                .subscribe(new Action1<userBean>() {
                    @Override
                    public void call(userBean userBean) {
                        if(userBean.getHead_url()!=null) {
                            Glide.with(getActivity()).load(userBean.getHead_url()).asBitmap().into(head);
                            Glide.with(getActivity()).load(userBean.getHead_url()).asBitmap().into(head1);
                            Glide.with(getActivity()).load(userBean.getHead_url()).bitmapTransform(new BlurTransformation(getContext(), 23, 2)).into(iv);
                        }else if(userBean.getName()!=null){
                            user1.setText(userBean.getName());
                            user.setText(userBean.getName());
                        }
                    }

                });
    }

    @Override
    public void setUserView(ArrayList<userBean> list) {
        Glide.with(getActivity()).load(list.get(0).getHead_url()).asBitmap().into(head);
        Glide.with(getActivity()).load(list.get(0).getHead_url()).asBitmap().into(head1);
        Glide.with(getActivity()).load(list.get(0).getHead_url()).bitmapTransform(new BlurTransformation(getContext(), 23, 2)).into(iv);
        user1.setText(list.get(0).getName());
        user.setText(list.get(0).getName());
    }

    @Override
    public void setFailView() {
        user.setText("未登录");
        user1.setText("未登录");
        Glide.with(getActivity()).load(R.drawable.headpic).asBitmap().into(head);
        Glide.with(getActivity()).load(R.drawable.headpic).asBitmap().into(head1);
        Glide.with(getActivity()).load(R.drawable.headpic).bitmapTransform(new BlurTransformation(getContext(), 23, 2)).into(iv);
    }

    @Override
    protected void lazyLoad() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!rxSbscription.isUnsubscribed()){
            rxSbscription.unsubscribe();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:case R.id.fab2:
                if(isLoggedIn()) {
                    Intent intent = new Intent(getActivity(), userinfoActivity.class);
                    startActivity(intent);
                }else{
                    logoutUser();
                }
                break;
            case R.id.address_view:
                if(!isLoggedIn()){
                    logoutUser();
                    return;
                }
                if(!isSelectUni()){
                    logoutUniversity();
                    return;
                }
                Intent intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
                break;
            case R.id.logout_btn:
                if(isLoggedIn()){
                    logout();
                }
                break;

        }
    }

    @Override
    public void logout() {
        super.logout();
        setFailView();
    }
}