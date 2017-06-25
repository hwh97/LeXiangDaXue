package cn.hwwwwh.lexiangdaxue.fragment;

/**
 * Created by 97481 on 2016/10/11.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.lang.ref.WeakReference;

import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;

public class GuideFourthFragment extends Fragment implements View.OnClickListener {
    private Button btnLogout;
    private SQLiteHandler db;
    private SessionManager session;
    private View view;
    protected WeakReference<View> mRootView;
    private boolean visible=false;
    //不加载下一页数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //fragment可见时执行加载数据或者进度条等
            visible=true;
        } else {
            //不可见时不执行操作
            visible=false;
        }

    }
    //不滑动
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // detach/attach can lead to view recreate frequently
            if (mRootView == null || mRootView.get() == null) {
                view = View.inflate(getActivity(), R.layout.fg4,
                        null);
                // Fetching user details from sqlite
                //get data from database
//        HashMap<String, String> user = db.getUserDetails();
//        String name = user.get("name");
//        String email = user.get("email");
//
//        // Displaying the user details on the screen
//        txtName.setText(name);
//        txtEmail.setText(email);
                btnLogout = (Button) view.findViewById(R.id.btnLogout);
                btnLogout.setOnClickListener(this);
                mRootView = new WeakReference<View>(view);
                // SqLite database handler
                db = new SQLiteHandler(view.getContext());
                // session manager
                session = new SessionManager(view.getContext());
            }
            //fragment 缓存后显示逻辑
            else {
                ViewGroup parent = (ViewGroup) mRootView.get().getParent();
                if (parent != null) {
                    parent.removeView(mRootView.get());
                }
            }
            return mRootView.get();
    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        return view;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogout:
            logoutUser();
                break;
        }
    }
    private void logoutUser(){
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        startActivity(intent);
    }
}