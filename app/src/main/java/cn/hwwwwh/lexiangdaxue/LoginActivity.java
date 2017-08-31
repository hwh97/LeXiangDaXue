package cn.hwwwwh.lexiangdaxue;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.RxBus;

/**
 * Created by 97481 on 2016/10/13.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private TextView btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail=(EditText)findViewById(R.id.email);
        inputPassword=(EditText)findViewById(R.id.password);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        btnLinkToRegister=(TextView)findViewById(R.id.register);
        //progress dialog
        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(false);
        //
        db=new SQLiteHandler(getApplicationContext());
        //session
        session=new SessionManager(getApplicationContext());
        //check if user is already logged in or not
        if(session.isLoggedIn()){
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        btnLogin.setOnClickListener(this);
        btnLinkToRegister.setOnClickListener(this);
    }
    //button click even
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //login button click even
            case R.id.btnLogin:
                String email =inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString().trim();
                //
                if(!email.isEmpty()&&!password.isEmpty()){
                    checkLogin(email,password);
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "账号或密码不能为空！", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.register:
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }
    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email,final String password){
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog.setMessage("登录中...");
        showDialog();
        //第二个参数是服务器响应成功的回调，第三个参数是服务器响应失败的回调
        StringRequest strReq=new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try{
                    JSONObject jobj=new JSONObject(response);
                    boolean error=jobj.getBoolean("error");
                    // Check for error node in json
                    if(!error){
                        session.setLogin(true);
                        String uid=jobj.getString("uid");
                        //user 实例
                        JSONObject user = jobj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user
                                .getString("created_at");
                        db.addUser(name, email, uid, created_at);
                        if (jobj.has("university")) {
                            //university 实例
                            JSONObject university = jobj.getJSONObject("university");
                            String user_uuid = university.getString("user_uuid");
                            String uu_province = university.getString("uu_province");
                            String uu_city = university.getString("uu_city");
                            String uu_name = university.getString("uu_name");
                            String university_id = university.getString("university_id");
                            db.addUniversity(user_uuid, uu_province, uu_city, uu_name, university_id);
                            RxBus.getInstance().post(uu_name);
                        }
//                        Intent intent = new Intent(LoginActivity.this,
//                                MainActivity.class);
//                        startActivity(intent);
                        finish();
                    }else {
                        // Error in login. Get the error message
                        String errorMsg = jobj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
          @Override
            protected Map<String,String> getParams(){
              //使用post 方法发生数据
              Map<String,String> params=new HashMap<String,String>();
              params.put("email", email);
              params.put("password", password);
              return params;
          }
        };
        // Adding request to request queue添加strReq和tag给AppController实例
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
