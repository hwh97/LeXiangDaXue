package cn.hwwwwh.lexiangdaxue.FgSecondClass.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ytying.emoji.SmileLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.FgSecondClass.other.MyItemTouchCallback;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.adapter.ShowPhotoAdapter;
import cn.hwwwwh.lexiangdaxue.ImageLoader.SelectPhotoActivity;
import cn.hwwwwh.lexiangdaxue.ImageLoader.SelectPhotoAdapter;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.SpacesItemDecoration;
import cn.hwwwwh.lexiangdaxue.ShowPhotoActivity;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;

public class EditActivity extends BaseActivity implements View.OnLayoutChangeListener,View.OnTouchListener,View.OnClickListener {
    private AppCompatEditText editText;
    private Toolbar toolbar;
    private ImageView emoticon;
    private SmileLayout smileLayout;
    private RelativeLayout changeEdit;
    //Activity最外层的Layout视图
    private RelativeLayout activity_edit;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;
    private ImageView addPhoto;
    private RecyclerView recyclerView;
    public ShowPhotoAdapter adapter;
    private TextView hint1;
    private TextView hint2;
    private Button send_post;
    /**********************************touch事件*********************************************/
    private static final long LONG_PRESS_TIME = 500;
    /**
     * 当前触摸点相对于屏幕的坐标
     */
    private float mCurrentInScreenX;
    private float mCurrentInScreenY;
    /**
     * 触摸点按下时的相对于屏幕的坐标
     */
    private float mDownInScreenX;
    private float mUpInScreenX;
    private float mDownInScreenY;
    /**
     * 当前点击时间
     */
    private long mCurrentClickTime;
    private Handler mBaseHandler = new Handler();
    private LongPressedThread mLongPressedThread;
    private SQLiteHandler sqLiteHandler;
    private ProgressDialog pDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_edit);
        activity_edit =getViewById(R.id.activity_edit);
        editText=getViewById(R.id.textEdit);
        smileLayout = getViewById(R.id.write_smile_panel);
        emoticon=getViewById(R.id.emoticon);
        changeEdit=getViewById(R.id.changeEdit);
        toolbar=getViewById(R.id.toolbar_edit);
        addPhoto=getViewById(R.id.addPhoto);
        recyclerView=getViewById(R.id.showPhoto);
        hint1=getViewById(R.id.hint1);
        hint2=getViewById(R.id.hint2);
        send_post=getViewById(R.id.send_post);
    }

    @Override
    protected void setListener() {
        //监听软键盘关闭打开状态
        activity_edit.addOnLayoutChangeListener(this);
        //解决scrollview与edittext滑动冲突问题
        editText.setOnTouchListener(this);
        //点击表情后弹出表情键盘并隐藏输入法键盘
        emoticon.setOnClickListener(this);
        //点击edittext后弹出输入法键盘并隐藏表情键盘
        editText.setOnClickListener(this);
        addPhoto.setOnClickListener(this);
        send_post.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState)  {
        sqLiteHandler=new SQLiteHandler(this);
        pDialog=new ProgressDialog(this);
        pDialog.setCancelable(true);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        smileLayout.setVisibility(View.GONE);
        //!!!初始化，这句话一定要加
        smileLayout.init(editText);
        changeEdit.setVisibility(View.VISIBLE);
        adapter=new ShowPhotoAdapter(getApplicationContext(),EditActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),4, GridLayoutManager.VERTICAL,false));
        SpacesItemDecoration decoration=new SpacesItemDecoration(10);
        recyclerView.addItemDecoration(decoration);
        ItemTouchHelper helper = new ItemTouchHelper(new MyItemTouchCallback(adapter));
        helper.attachToRecyclerView(recyclerView);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.emoticon:
                hideKeyBoard();
                if (smileLayout.getVisibility() == View.GONE) {
                    smileLayout.setVisibility(View.VISIBLE);
                    changeEdit.setVisibility(View.VISIBLE);
                    emoticon.setImageResource(R.drawable.ic_keyboard_hide_black_24dp);
                }
                else {
                    smileLayout.setVisibility(View.GONE);
                    showKeyboard();
                }
                break;
            case R.id.textEdit:
                smileLayout.setVisibility(View.GONE);
                showKeyboard();
                break;
            case R.id.addPhoto:
                //隐藏删除图标
                if(adapter!=null){
                    adapter.setNotShowCheckBox();
                }
                Intent intent = new Intent(this,SelectPhotoActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("selectedPhotoNum",adapter.getItemCount());
                intent.putExtras(bundle);
                startActivityForResult(intent,10);
                break;
            case R.id.send_post:
                List<String> list = new ArrayList<>();
                if (adapter.getmDatas()!=null) {
                    if (!adapter.getmDatas().isEmpty()) {
                        List<SelectPhotoAdapter.SelectPhotoEntity> photos = adapter.getmDatas();
                        for (int i=0;i<photos.size();i++) {
                            if (photos.get(i).url != null) {
                                list.add(bitmapToString(photos.get(i).url));
                            }
                        }
                    }
                }
                HashMap<String,String> hashMap=sqLiteHandler.getUserDetails();
                sendPost(hashMap,list,editText.getText().toString());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Alex","mainActivity的onActivityResult req="+requestCode+"    result="+requestCode);
        if(data == null || resultCode != SelectPhotoActivity.SELECT_PHOTO_OK)return;
        boolean isFromCamera = data.getBooleanExtra("isFromCamera",false);
        final ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos = data.getParcelableArrayListExtra("selectPhotos");
        Log.i("Alex","选择的图片是"+selectedPhotos);
        if (adapter.getItemCount()==0) {
            adapter.setData(selectedPhotos);
            if (adapter.getItemCount()>=9){

            }
        }
        else {
            adapter.addMoreData(selectedPhotos);
        }
        if(adapter.getmDatas().size()!=0){
            hint1.setVisibility(View.VISIBLE);
            hint2.setVisibility(View.VISIBLE);
        }else {
            hint1.setVisibility(View.INVISIBLE);
            hint2.setVisibility(View.INVISIBLE);
        }
        adapter.setRecyclerViewOnItemClickListener(new ShowPhotoAdapter.RecyclerViewOnItemClickListener(){
            @Override
            public void onItemClickListener(View view, int position) {
                Log.d("testlexiangdaxue","onItemClickListener");
            }

            @Override
            public boolean onItemLongClickListener(View view, int position) {
                Log.d("testlexiangdaxue","onItemLongClickListener");
                //长按事件
                adapter.setShowBox();
                //设置选中的项
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onItemTouchClickListener(View view,MotionEvent event, int position) {
                // TODO Auto-generated method stub
                //当前坐标
                mCurrentInScreenX = event.getRawX();
                mCurrentInScreenY = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //记录Down下时的坐标
                        mDownInScreenX = event.getRawX();
                        mDownInScreenY = event.getRawY();
                        //记录当前点击的时间
                        mCurrentClickTime = Calendar.getInstance().getTimeInMillis();
                        //开一个线程，延迟LONG_PRESS_TIME时间
                        mLongPressedThread = new LongPressedThread();
                        mBaseHandler.postDelayed(mLongPressedThread,LONG_PRESS_TIME);
                        break;
                    case MotionEvent.ACTION_UP: {
                        mUpInScreenX = event.getRawX();
                        if(!isMoved()){
                            //如果按住的时间超过了长按时间，那么其实长按事件已经出发生了,这个时候把数据清零
                            if(Calendar.getInstance().getTimeInMillis() - mCurrentClickTime <= LONG_PRESS_TIME){
                                //取消注册的长按事件
                                mBaseHandler.removeCallbacks(mLongPressedThread);
                                //这里处理长按事件
                                if (selectedPhotos.size() != 0) {
                                    Intent intent = new Intent(EditActivity.this, ShowPhotoActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("selectPhotos", (Serializable) selectedPhotos);//把获取到图片交给别的Activity
                                    startActivity(intent);
                                }

                            }
                        }else{
                            //取消注册的长按事件
                            //mBaseHandler.removeCallbacks(mLongPressedThread);
                            //UP的时候Move过
                            if((mUpInScreenX-mDownInScreenX)>100 ){
                                Toast.makeText(EditActivity.this.getApplicationContext(), "左移事件处理",Toast.LENGTH_SHORT).show();
                            }else if((mUpInScreenX-mDownInScreenX) <-100){
                                Toast.makeText(EditActivity.this.getApplicationContext(), "右移事件处理",Toast.LENGTH_SHORT).show();
                            }else{

                            }

                        }
                        break;
                    }
                }

                return true;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 判断是否移动
     * @return
     */
    private boolean isMoved(){
        //允许有5的偏差 在判断是否移动的时候
        if(Math.abs(mDownInScreenX - mCurrentInScreenX) <= 10 && Math.abs(mDownInScreenY - mCurrentInScreenY) <= 10 ){
            return false;
        }else{
            return true;
        }
    }

    public class LongPressedThread implements Runnable{

        @Override
        public void run() {
            //这里处理长按事件
        }
    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //System.out.println(oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
        //System.out.println(left + " " + top +" " + right + " " + bottom);
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
            //keyboard pop-up
            changeEdit.setVisibility(View.VISIBLE);
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
            //keyboard pop-down
            if(smileLayout.getVisibility()==View.VISIBLE){
                changeEdit.setVisibility(View.VISIBLE);
            }else {
                changeEdit.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((v.getId() == R.id.textEdit && canVerticalScroll(editText))) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard(){
        if (getWindow().getAttributes().softInputMode != 	WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
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
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
        changeEdit.setVisibility(View.VISIBLE);
        emoticon.setImageResource(R.drawable.ic_insert_emoticon_black_24dp);
    }

    /**
     * EditText竖直方向是否可以滚动
     * @param editText  需要判断的EditText
     * @return  true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if(scrollDifference == 0) {
            return false;
        }

        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    private JSONArray jsonArray;
    private String tag_string_req="req_editPost";
    @Override
    public void onBackPressed() {
        if(adapter!=null){
            if(adapter.getIsShowCheckBox()){
                adapter.setShowBox();
                adapter.notifyDataSetChanged();
            }else{
                finish();
            }
        }else finish();
    }

    public void sendPost(final HashMap<String, String> hashMap, final List<String> list, final String content){
        pDialog.setMessage("提交数据中...");
        showDialog();
        if (list!=null) {
            String bitmapJson;
            jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            JSONObject tmpObj = null;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                tmpObj = new JSONObject();
                try {
                    tmpObj.put("encodeBitmap", list.get(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(tmpObj);
                tmpObj = null;
            }
            String bitmapInfos = jsonArray.toString(); // 将JSONArray转换得到String
        }
        showDialog();
        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlHandlePost, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (error) {
                        Log.d("Testlexiangdaxue","成功传输参数");
                        Intent intent=new Intent();
                        setResult(RESULT_OK,intent);
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
                Log.d("lexiangdaxueError",error.toString());
                ShowToast("未知错误");
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", hashMap.get("name"));
                params.put("email", hashMap.get("email"));
                params.put("uid", hashMap.get("uid"));
                params.put("content",content);
                params.put("postTime", String.valueOf(System.currentTimeMillis()).substring(0,10));
                if (list!=null) {
                    params.put("bitmaps", jsonArray.toString());
                }
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,tag_string_req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cancelPendingRequests(tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {
        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        //1.5M的压缩后在100Kb以内，测试得值,压缩后的大小=94486字节,压缩后的大小=74473字节
        //这里的JPEG 如果换成PNG，那么压缩的就有600kB这样
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        Log.d("d", "压缩后的大小=" + b.length);
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}
