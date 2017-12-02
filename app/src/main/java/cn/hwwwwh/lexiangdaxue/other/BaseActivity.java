package cn.hwwwwh.lexiangdaxue.other;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.HashMap;

import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import me.majiajie.swipeback.SwipeBackActivity;

/**
 * Created by 97481 on 2017/2/3.
 */

public abstract class BaseActivity extends SwipeBackActivity implements View.OnClickListener,View.OnTouchListener {

    private SessionManager session;
    private SQLiteHandler db;
    private AppUtils appUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getManager().push(this);
        session=new SessionManager(this);
        db=new SQLiteHandler(this);
        appUtils=new AppUtils(this);
        initView(savedInstanceState);
        setListener();
        processLogic(savedInstanceState);
    }
    /**
     *  初始化布局以及View控件
     *
     * @param savedInstanceState
     */

    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    /**
     * 处理业务逻辑，状态恢复等操作
     *
     * @param savedInstanceState
     */
    protected abstract void processLogic(Bundle savedInstanceState);

   /**
            * 需要处理点击事件时，重写该方法
    *
            * @param v
    */
    public void onClick(View v) {
    }

    protected HashMap<String,String> getUserDetails(){
        HashMap<String,String> userMap=db.getUserDetails();
        return userMap;
    }

    protected String getPhoneId(){
        return appUtils.getPesudoUniqueID();
    }


    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    protected void ShowToast(String text){
        ToastUtil.show(text);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除缓存
        Glide.get(this).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(getApplicationContext()).clearDiskCache();
            }
        }).start();
    }

    /**
     * 回调函数
     */
    public LifeCycleListener mListener;

    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }
}
