package cn.hwwwwh.lexiangdaxue.selectSchoolClass.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.RxBus;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.bean.City;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.bean.School;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.other.AddressSelector;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.other.CityInterface;
import cn.hwwwwh.lexiangdaxue.selectSchoolClass.other.OnItemClickListener;

public class selectActivity extends BaseActivity {
    private Toolbar toolbar;
    private AddressSelector addressSelector;
    private ArrayList<City> cities1 = new ArrayList<>();
    private ArrayList<City> cities2 = new ArrayList<>();
    private ArrayList<City> cities3 = new ArrayList<>();
    private SessionManager session;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private String province;
    private String City;
    private String name;
    private int schoolID;
    private String tag_string_req="req_selectAty";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select);
        pDialog=new ProgressDialog(this);
        session=new SessionManager(this);
        // SqLite database handler
        db = new SQLiteHandler(this);
        toolbar=getViewById(R.id.toolbar_setSchool);
    }

    @Override
    protected void setListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_menu, menu);
        return true;
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {
        toolbar.setTitle("学校选择");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backsoace_24dp1_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent=new Intent(selectActivity.this, SearchSchool.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#11B57C"));
        }
        new DownloadData(0).execute("http://cs.hwwwwh.cn/admin/AddressApi.php?method=0");
        addressSelector = (AddressSelector) findViewById(R.id.address);
        initTab();
        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface city, int tabPosition) {
                switch (tabPosition){
                    case 0:
                        province=city.getCityName();
                        try {
                            String s = URLEncoder.encode(province, "utf-8");
                            new DownloadData(1).execute("http://cs.hwwwwh.cn/admin/AddressApi.php?method=1&province="+s);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        //在安卓5.0以下get参数包含中文的不能自动转换UTF-8,导致服务器不识别参数需手动转换
                        City=city.getCityName();
                        try {
                            String s = URLEncoder.encode(City, "utf-8");
                            new DownloadData(2).execute("http://cs.hwwwwh.cn/admin/AddressApi.php?method=2&city="+s);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        City city1= (City) city;
                        if(city1.getIsOpen()==1) {
                            name=city.getCityName();
                            schoolID=city1.getSchoolId();
                            sendPost(province,City,name,schoolID);
                               /* final EditText et = new EditText(getApplicationContext());
                                et.setTextColor(getResources().getColor(R.color.black));
                                et.setTextSize(14);

                                AlertDialog.Builder builder = new AlertDialog.Builder(selectActivity.this);
                                builder.setView(et);

                                builder.setMessage("请输入具体收货地址");

                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ShowToast(et.getText().toString());
                                    }
                                });

                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                                builder.create().show();*/
                        }
                        else
                            ShowToast("该学校暂未开通！");
                        break;
                }
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()){
                    case 0:
                        addressSelector.setCities(cities1);
                        break;
                    case 1:
                        addressSelector.setCities(cities2);
                        break;
                    case 2:
                        addressSelector.setCities(cities3);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });
    }

    public void sendPost(final String province, final String city, final String name, final int schoolID){
        pDialog.setMessage("提交数据中...");
        showDialog();
        HashMap<String,String> userDeatil=db.getUserDetails();
        final String uid=userDeatil.get("uid");
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlAddUU, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d("Testlexiangdaxue","成功传输参数");
                        db.deleteUniversity();
                        db.addUniversity(db.getUserDetails().get("uid"),province,city,name,String.valueOf(schoolID));
                        Intent intent=new Intent();
                        intent.putExtra("university",name);
                        setResult(RESULT_OK,intent);
                        School s=new School();
                        s.setName(name);s.setUniversity_id(schoolID);s.setProvince(province);s.setCity(city);
                        RxBus.getIntanceBus().post(s);
                        finish();
                    } else {
                        String error_info=null;
                        if(jObj.has("error_msg")) {
                            error_info = jObj.getString("error_msg");
                        }
                        Log.d("Testlexiangdaxue","传输参数失败"+error_info);
                        hideDialog();
                        ShowToast("未知错误");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("lexiangdaxueError",response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.d("lexiangdaxueError",error.getMessage(),error);
                ShowToast("未知错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_uuid", uid);
                params.put("uu_province", province);
                params.put("uu_city", city);
                params.put("uu_name",name);
                params.put("university_id", String.valueOf(schoolID));
                Log.d("Testlexiangdaxue",uid+province+city+name+String.valueOf(schoolID));

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cancelPendingRequests(tag_string_req);
    }

    public void initTab(){
        HashMap <String,String>  hashMap=db.getUniDetails();
        if(hashMap.size()>0){
            addressSelector.setTabAmount(3,new String[]{hashMap.get("uu_province"),hashMap.get("uu_city"),hashMap.get("uu_name")});
        }else{
            addressSelector.setTabAmount(3,new String[]{"省份","城市","学校"});
        }
    }

    class DownloadData extends AsyncTask<String,Void,String>{

        private int method;

        public DownloadData(int method){
            this.method=method;
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonString=null;
            String Url=params[0];
            if(HttpUtils.isNetConn(getApplicationContext())){
                byte[]b=null;
                b =HttpUtils.downloadFromNet(Url);
                jsonString=new String(b);
                Log.d("lexiangdaxueTag",jsonString);

            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);
            if(jsonString!=null){
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    if (method == 0) {
                        cities1.clear();
                        for(int i =0;i<jsonArray.length();i++){
                            cities1.add(new Gson().fromJson(jsonArray.get(i).toString(),City.class));
                        }
                        addressSelector.setCities(cities1);
                    }else if (method==1){
                        cities2.clear();
                        for(int i =0;i<jsonArray.length();i++){
                            cities2.add(new Gson().fromJson(jsonArray.get(i).toString(),City.class));
                        }
                        addressSelector.setCities(cities2);
                    }else if(method==2){
                        cities3.clear();
                        for(int i =0;i<jsonArray.length();i++){
                            cities3.add(new Gson().fromJson(jsonArray.get(i).toString(),City.class));
                        }
                        addressSelector.setCities(cities3);
                    }else{
                        ShowToast("加载出错");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                ShowToast("网络不通");
            }

        }
    }


}
