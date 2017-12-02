package cn.hwwwwh.lexiangdaxue.FgFourthClass.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.userBean;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.other.Util;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter.DownloadUserPresenter;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.view.IUserView;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.other.MyBottomSheetDialog;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.RxBus;
import cn.hwwwwh.lexiangdaxue.other.XCRoundImageView;
import id.zelory.compressor.Compressor;


public class userinfoActivity extends BaseActivity implements IUserView {

    private SQLiteHandler db;
    private SessionManager session;
    public DownloadUserPresenter downloadUserPresenter;
    private TextView bindEmail;
    private TextView nickName;
    private TextView userAccount;
    private XCRoundImageView userHeaderPic;
    private Toolbar toolbar;
    private LinearLayout headPicView;
    private LinearLayout nickNameView;
    private LinearLayout changePwdView;
    private BottomSheetLayout bottomSheet;
    private String tag_string_req="req_UserInfo";
    private ProgressDialog pDialog;
    private String uid;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 10002;
    private static final int PICK_ACTIVITY_REQUEST_CODE = 10003;
    private static final int CROP_ACTIVITY_REQUEST_CODE = 10008;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 10010;
    private String imageFilePath; //拍照和选择照片后图片路径
    private File cropFile; //裁剪后的图片文件
    private Uri pickPhotoImageUri; //API22以下相册选择图片uri



    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userinfo);
        downloadUserPresenter=new DownloadUserPresenter(this);
        bindEmail=getViewById(R.id.bindEmail);
        nickName=getViewById(R.id.nickName);
        userAccount=getViewById(R.id.userAccount);
        userHeaderPic=getViewById(R.id.userHeaderPic);
        toolbar=getViewById(R.id.toolbar_userinfo);
        // SqLite database handler
        db = new SQLiteHandler(this);
        // session manager
        session = new SessionManager(this);
        headPicView=getViewById(R.id.headPicView);
        bottomSheet=getViewById(R.id.activity_userinfo);
        nickNameView=getViewById(R.id.nickNameView);
        changePwdView=getViewById(R.id.changePwdView);
        uid=db.getUserDetails().get("uid");
    }

    @Override
    protected void setListener() {
        headPicView.setOnClickListener(this);
        nickNameView.setOnClickListener(this);
        changePwdView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.headPicView:
                //打开app检查权限
                permission();
                openPicBottomSheet();
                break;
            case R.id.nickNameView:
                openNickBottomSheet();
                break ;
            case R.id.changePwdView:
                openPwdBottomSheet();

                break;
        }
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#11B57C"));
        }
        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(true);
        toolbar.setTitle("我的账户");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backsoace_24dp1_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        downloadUserPresenter.download("http://cs.hwwwwh.cn/admin/userinfo.php?unique_id="+uid);
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
    public void setUserView(ArrayList<userBean> list) {
        bindEmail.setText(list.get(0).getEmail());
        nickName.setText(list.get(0).getName());
        userAccount.setText(list.get(0).getEmail());
        Glide.with(getApplicationContext()).load(list.get(0).getHead_url()).asBitmap().placeholder(R.drawable.headpic).into(userHeaderPic);
    }

    @Override
    public void setFailView() {
        ShowToast("获取用户信息失败");
        Log.d("LexiandaxueTag","http://cs.hwwwwh.cn/admin/userinfo.php?unique_id="+uid);
    }

    private void openPicBottomSheet(){
        final MyBottomSheetDialog dialog=new MyBottomSheetDialog(this);
        View view=getLayoutInflater().inflate(R.layout.bottomsheet_picchoose, null);
        dialog.setContentView(view);
        dialog.show();
        final TextView takePic=(TextView)view.findViewById(R.id.takePic);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
                dialog.dismiss();
            }
        });
        final TextView Album=(TextView)view.findViewById(R.id.Album);
        Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
                dialog.dismiss();
            }
        });
    }

    private void openNickBottomSheet(){
        final MyBottomSheetDialog dialog=new MyBottomSheetDialog(this);
        View view=getLayoutInflater().inflate(R.layout.bottomsheet_nickname, null);
        dialog.setContentView(view);
        dialog.show();
        final LinearLayout done_view=(LinearLayout)view.findViewById(R.id.done_view);
        final EditText nickEdit=(EditText) view.findViewById(R.id.nickEdit);
        nickEdit.setText(nickName.getText().toString());
        done_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkString(nickEdit.getText().toString())){
                    if(!nickEdit.getText().toString().isEmpty())
                        ShowToast("检测到昵称不符要求");
                    else
                        ShowToast("昵称不能为空");
                }else{
                    sendNNPost(nickEdit.getText().toString(),"1");
                    dialog.dismiss();
                }
            }
        });

        nickEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void openPwdBottomSheet(){
        final MyBottomSheetDialog dialog=new MyBottomSheetDialog(this);
        View view=getLayoutInflater().inflate(R.layout.bottomsheet_pwd, null);
        view.setFocusable(true);
        dialog.setContentView(view);
        dialog.show();
        final EditText oldPwd=(EditText) view.findViewById(R.id.oldEdit);
        final EditText newPwd=(EditText)view.findViewById(R.id.newEdit);
        LinearLayout doneView=(LinearLayout)view.findViewById(R.id.done_view);
        doneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!oldPwd.getText().toString().isEmpty() && !newPwd.getText().toString().isEmpty()){
                    if(oldPwd.getText().length()>=6&&newPwd.getText().length()>=6) {
                        sendPost(oldPwd.getText().toString(), newPwd.getText().toString(), "2");
                        dialog.dismiss();
                    }
                    else
                        ShowToast("密码长度错误");
                }else{
                    ShowToast("请补全信息");
                }
            }
        });

    }

    public void showKeyboard(EditText editText) {
        if(editText!=null){
            //设置可获得焦点
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            //请求获得焦点
            editText.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(editText, 0);
        }else{
            ShowToast("11");
        }
    }


    private boolean checkString(String s) {
        return s.matches("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
    }


    //拍照获取图片
    private void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File imageFile = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
            if (!imageFile.getParentFile().exists()) imageFile.getParentFile().mkdirs();
            imageFilePath = imageFile.getPath();
            //兼容性判断
            Uri imageUri;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                imageUri = Util.file2Uri(this, imageFile);
            } else {
                imageUri = Uri.fromFile(imageFile);
            }
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI

            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    //从相册中取图片
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //CropHelper.handleResult(this, requestCode, resultCode, data);
        switch (requestCode) {
            case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                //拍照
                if (resultCode == Activity.RESULT_OK) {
                    crop(false);
                }
                break;

            case CROP_ACTIVITY_REQUEST_CODE:
                //裁剪完成
                if (data != null) {
                    Bitmap bitmap;
                    try {
                        bitmap = BitmapFactory.decodeFile(cropFile.getPath());
                        Bitmap compressedImage = new Compressor(this)
                                .setMaxWidth(130)
                                .setMaxHeight(130)
                                .setQuality(50)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                                .compressToBitmap(cropFile);

                        String headPic=bitmapToString(compressedImage);
                        sendPost(headPic,"0");
                        /*Luban.with(this)
                                .load(cropFile.getPath())// 传人要压缩的图片列表
                                .ignoreBy(100)                                  // 忽略不压缩图片的大小
                                .setTargetDir(cropFile.getParentFile().getPath())                        // 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        // TODO 压缩成功后调用，返回压缩后的图片文件
                                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                                        String headPic=bitmapToString(bitmap);
                                        sendPost(headPic,"0");
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // TODO 当压缩过程出现问题时调用
                                        Log.d("Throwable",e.getMessage());
                                    }
                                }).launch();    //启动压缩*/
                        //userHeaderPic.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case PICK_ACTIVITY_REQUEST_CODE:
                //从相册选择
                if (data != null && resultCode == Activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        imageFilePath = Util.getPathByUri4kitkat(this, data.getData());
                    } else {
                        pickPhotoImageUri = data.getData();
                    }
                    crop(true);
                }
                break;
        }

    }

    //把bitmap转换成String
    public static String bitmapToString(Bitmap bm) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        Log.d("d", "压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    //提交头像
    public void sendPost(final String headPic, final String type){
        pDialog.setMessage("提交数据中...");
        showDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlUpdateUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d("Testlexiangdaxue","成功传输参数");
                        ShowToast("更换头像成功");
                        userBean userBean=new userBean();
                        //user 实例
                        JSONObject user = jObj.getJSONObject("user");
                        userBean.setHead_url(user.getString("headUrl"));
                        Glide.with(userinfoActivity.this).load(user.getString("headUrl")).asBitmap().into(userHeaderPic);
                        RxBus.getIntanceBus().post(userBean);
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
                Log.d("lexiangdaxueError",error.toString());
                ShowToast("未知错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("type",type);
                params.put("header",headPic);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
    }

    //提交昵称
    public void sendNNPost(final String nn, final String type){
        pDialog.setMessage("提交数据中...");
        showDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlUpdateUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d("Testlexiangdaxue","成功传输参数");
                        ShowToast("更换昵称成功");
                        userBean userBean=new userBean();
                        //user 实例
                        JSONObject user = jObj.getJSONObject("user");
                        String newName=user.getString("nikeName");
                        userBean.setName(newName);
                        nickName.setText(newName);
                        db.updateUserNN(newName,uid);
                        RxBus.getIntanceBus().post(userBean);
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
                Log.d("lexiangdaxueError",error.toString());
                ShowToast("未知错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("type",type);
                params.put("nickName",nn);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
    }

    //密码更改
    public void sendPost(final String oldPwd,final String newPwd,final String type){
        pDialog.setMessage("提交数据中...");
        showDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlUpdateUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Log.d("Testlexiangdaxue","成功传输参数");
                        ShowToast("更换密码成功");
                    } else {
                        String error_info=null;
                        if(jObj.has("error_msg")) {
                            error_info = jObj.getString("error_msg");
                        }
                        Log.d("Testlexiangdaxue","传输参数失败"+error_info);
                        hideDialog();
                        ShowToast(error_info);
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
                Log.d("lexiangdaxueError",error.toString());
                ShowToast("未知错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", uid);
                params.put("type",type);
                params.put("oldPwd",oldPwd);
                params.put("newPwd",newPwd);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
    }


    /**
     * 裁剪
     *
     * @param isPick 是否是从相册选择
     */
    private void crop(boolean isPick) {
        cropFile = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!cropFile.getParentFile().exists()) cropFile.getParentFile().mkdirs();
        Uri outputUri, imageUri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            outputUri = Util.file2Uri(this, cropFile);
            imageUri = Util.file2Uri(this, new File(imageFilePath));
        } else {
            outputUri = Uri.fromFile(cropFile);
            imageUri = isPick ? pickPhotoImageUri : Uri.fromFile(new File(imageFilePath));
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection

        //授予"相机"保存文件的权限 针对API24+
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, outputUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivityForResult(intent, CROP_ACTIVITY_REQUEST_CODE);
    }

    /**
     * 申请权限
     */
    private void permission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //拒绝权限以后
                showMessageOKCancel();
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意授权
            } else {
                //拒绝授权后重新申请
                permission();
            }
        }
    }

    private void showMessageOKCancel() {
        new AlertDialog.Builder(this)
                .setMessage("必须授予储存空间的权限！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cancelPendingRequests(tag_string_req);
    }
}
