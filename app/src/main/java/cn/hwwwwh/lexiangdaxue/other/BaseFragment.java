package cn.hwwwwh.lexiangdaxue.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.activity.selectActivity;

/**
 * Created by 97481 on 2017/2/2.
 */

 public abstract class BaseFragment extends Fragment{
    protected View mContentView;
    protected AppController mApp;
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "LazyLoadFragment";
    private SessionManager session;
    private SQLiteHandler db;
    private AppUtils appUtils;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mApp=AppController.getInstance();
        session=new SessionManager(mApp);
        db=new SQLiteHandler(mApp);
        appUtils=new AppUtils(mApp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isInit = false;
        isLoad = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 避免多次从xml中加载布局文件
        if (mContentView == null) {
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
            isInit = true;
            /**初始化的时候去加载数据**/
            isCanLoadData();
        } else {
            ViewGroup parent = (ViewGroup) mContentView.getParent();
            if (parent != null) {
                parent.removeView(mContentView);
            }
        }
        return mContentView;
    }



    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData() {
        if (!isInit) {
            return;
        }

        if (getUserVisibleHint()) {
            lazyLoad();
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    protected void setContentView(@LayoutRes int layoutResID, Activity activity) {
        mContentView = LayoutInflater.from(activity).inflate(layoutResID, null);
    }

    protected void setContentView(@LayoutRes int layoutResID) {
        mContentView = LayoutInflater.from(mApp).inflate(layoutResID, null);
    }

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void setListener();

    protected abstract void processLogic(Bundle savedInstanceState);

    protected void ShowToast(String text){
        ToastUtil.show(text);
    }


    public void logout() {
        session.setLogin(false);
        db.deleteUsers();
        db.deleteUniversity();
    }

    protected void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    protected boolean isLoggedIn(){
        if(session.isLoggedIn())
            return  true;
        else
            return false;
    }

    protected HashMap<String,String> getUserDetails(){
        HashMap<String,String> userMap=db.getUserDetails();
        return userMap;
    }

    protected String getPhoneId(){
        return appUtils.getPesudoUniqueID();
    }

    protected void logoutUniversity(){
        db.deleteUniversity();
        // Launching the login activity
        Intent intent = new Intent(getContext(), selectActivity.class);
        startActivity(intent);
    }

    protected boolean isSelectUni(){
        if(db.getUserDetails().isEmpty())
            return false;
        else
            return true;
    }

    protected HashMap<String,String> getUniDetails(){
        HashMap<String,String> uniMap=db.getUniDetails();
        return uniMap;
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad();

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected void stopLoad() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Glide.with(mApp).pauseRequests();
//        //清除缓存
//        Glide.get(mApp).clearMemory();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.get(mApp).clearDiskCache();
//            }
//        }).start();
    }
}
