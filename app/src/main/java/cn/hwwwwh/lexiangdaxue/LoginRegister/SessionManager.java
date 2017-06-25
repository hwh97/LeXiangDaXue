package cn.hwwwwh.lexiangdaxue.LoginRegister;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by 97481 on 2016/10/13.
 */
public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();
    //shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;
    //shared pref mode  Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
    int PRIVATE_MODE=0;
    //shareD preferences file name
    private static final String PREF_NAME="lexiangdaxue_Login";
    private static final String KEY_IS_LOGGEDIN="isLoggedIn";
    public SessionManager(Context context){
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }
    public void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGEDIN,isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
