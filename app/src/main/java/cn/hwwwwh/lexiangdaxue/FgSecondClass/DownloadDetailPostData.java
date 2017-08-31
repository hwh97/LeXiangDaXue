package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.squareup.picasso.Picasso;
import com.ytying.emoji.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.FgSecondClass.activity.PostActivity;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.DetailPostBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.PicBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.other.NineGridPicLayout;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;

/**
 * Created by 97481 on 2017/6/5/ 0005.
 */

public class DownloadDetailPostData extends AsyncTask<String,Void,List<DetailPostBean>>{

    private View itemView;
    private Context context;
    private TextView username;
    private TextView postTime;
    private TextView post_goodcount;
    private TextView post_content;
    private NineGridPicLayout layout_nine_grid;
    private ImageView zeroComment;
    private RelativeLayout noCommentRL;
    private ThumbUpView zan_btn;
    private SessionManager sessionManager;
    private SQLiteHandler sqLiteHandler;
    public HashMap<String,String> hashMap;
    private ProgressDialog pDialog;
    private PostActivity activity;


    public DownloadDetailPostData(Context context,View itemView,PostActivity activity){
        this.context=context;
        this.itemView=itemView;
        sessionManager=new SessionManager(context);
        sqLiteHandler=new SQLiteHandler(context);
        pDialog=new ProgressDialog(context);
        pDialog.setCancelable(true);
        hashMap=sqLiteHandler.getUserDetails();
        this.activity=activity;
    }

    @Override
    protected List<DetailPostBean> doInBackground(String... params) {
        List<DetailPostBean>  list=new ArrayList<>();
        String Url=params[0];
        if(HttpUtils.isNetConn(context)){
            byte[]b=null;
            b =HttpUtils.downloadFromNet(Url);
            String jsonString=new String(b);
            Log.d("lexiangdaxueTag",jsonString);
            try {
                JSONArray arr=new JSONArray(jsonString);
                JSONObject obj=arr.getJSONObject(0);
                DetailPostBean detailPostBean=new DetailPostBean();
                detailPostBean.setId(obj.getString("Post_id"));
                detailPostBean.setPostAdmin(obj.getString("Post_admin"));
                detailPostBean.setPostContent(obj.getString("Post_content"));
                detailPostBean.setCreateTime(obj.getString("Post_createtime"));
                detailPostBean.setGoodCount(obj.getString("Post_goodcount"));
                detailPostBean.setCommentCount(obj.getString("Post_commentcount"));
                detailPostBean.setImgNum(obj.getString("Post_imgnum"));
                detailPostBean.setPostUuid(obj.getString("Post_uuid"));
                int Post_picNum=Integer.parseInt(obj.getString("Post_imgnum"));
                if(Post_picNum>0){
                    if(Post_picNum==1){
                        detailPostBean.setPictureUrl1(obj.getString("picture1"));
                        detailPostBean.setPicture1Height(obj.getString("picture1Height"));
                        detailPostBean.setPicture1Width(obj.getString("picture1Width"));
                    }else if(Post_picNum>=2) {
                        List<String> list2=new ArrayList<>();
                        for(int j=0;j<Post_picNum;j++){
                            list2.add(obj.getString("picture"+(j+1)));
                        }
                        detailPostBean.setPicsData(list2);
                    }
                }
                if(obj.has("Post_isZan")){
                    detailPostBean.setZan(true);
                }
                list.add(detailPostBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(final List<DetailPostBean> list) {
        super.onPostExecute(list);
        if(list!=null){
            username=(TextView)itemView.findViewById(R.id.username);
            postTime=(TextView)itemView.findViewById(R.id.postTime);
            post_goodcount=(TextView)activity.findViewById(R.id.post_goodcount);
            zan_btn=(ThumbUpView)activity.findViewById(R.id.zan_btn);
            zan_btn.setOnThumbUp(new ThumbUpView.OnThumbUp() {
                @Override
                public void like(boolean like) {
                    if(like){
                        if(sessionManager.isLoggedIn()) {
                            list.get(0).setZan(true);
                            int count = (Integer.parseInt(post_goodcount.getText().toString()) + 1);
                            post_goodcount.setText(count + "");
                            list.get(0).setGoodCount(count+"");
                            sendPost(hashMap,list.get(0).getPostUuid(),"0");
                        }else{
                            zan_btn.stopAnim();
                            logoutUser();
                        }
                    }else{
                        if(sessionManager.isLoggedIn()) {
                            list.get(0).setZan(false);
                            int count=(Integer.parseInt(post_goodcount.getText().toString())-1);
                            post_goodcount.setText(count+"");
                            list.get(0).setGoodCount(count+"");
                            sendPost(hashMap,list.get(0).getPostUuid(),"1");
                        }else{
                            zan_btn.stopAnim();
                            zan_btn.setLike();
                            logoutUser();
                        }
                    }
                }
            });
            if (list.get(0).isZan){
                zan_btn.setLike();
            }else{
                zan_btn.setUnlike();
            }
            post_content=(TextView)itemView.findViewById(R.id.post_content);
            layout_nine_grid=(NineGridPicLayout)itemView.findViewById(R.id.layout_nine_grid);
            layout_nine_grid.setContext(context);
            zeroComment=(ImageView)itemView.findViewById(R.id.zeroComment);
            noCommentRL=(RelativeLayout)itemView.findViewById(R.id.noCommentRL);
            username.setText(list.get(0).getPostAdmin());
            postTime.setText(list.get(0).getCreateTime());
            post_goodcount.setText(list.get(0).getGoodCount());
            SpannableString content= StringUtil.stringToSpannableString(list.get(0).getPostContent().toString(),context);
            post_content.setText(content);
            int imgs=Integer.parseInt(list.get(0).getImgNum());
            List<String> picsData=list.get(0).getPicsData();
            // new DownloadDetailPicData2(context, layout).execute("http://cs.hwwwwh.cn/admin/SinglePostPicApi.php?post_uuid=" + data.getPostUuid());
            List<PicBean> urlList = new ArrayList<>();//图片url
            if (imgs == 1) {
                PicBean picBean = new PicBean();
                picBean.setPic_url(list.get(0).getPictureUrl1());
                picBean.setPic_width(list.get(0).getPicture1Width());
                picBean.setPic_height(list.get(0).getPicture1Height());
                urlList.add(picBean);

            } else if(imgs>1){
                for (int j = 0; j < picsData.size(); j++) {
                    PicBean picBean = new PicBean();
                    picBean.setPic_url(picsData.get(j));
                    urlList.add(picBean);
                }
            }
            //1表示用ImageLoader加载
            layout_nine_grid.setloadImageMode(1);
            layout_nine_grid.setIsShowAll(false); //当传入的图片数超过9张时，是否全部显示
            layout_nine_grid.setSpacing(20); //动态设置图片之间的间隔
            layout_nine_grid.setUrlList(urlList); //最后再设置图片url
            if(list.get(0).getCommentCount().equals("0")){
                noCommentRL.setVisibility(View.VISIBLE);
                Picasso.with(context).load(R.drawable.nocomment).config(Bitmap.Config.RGB_565).resize(500,500).into(zeroComment);
                //Glide.with(context).load(R.drawable.nocomment).into(zeroComment);
            }else{
                noCommentRL.setVisibility(View.GONE);
            }
        }else{
            Toast.makeText(context, "加载出错", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void sendPost(final HashMap<String, String> hashMap, final String post_uuid, final String type){
        pDialog.setMessage("提交数据中...");
        showDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlHandleZan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject obj=new JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    if (!error){
                        Log.d("Testlexiangdaxue","操作成功");
                    }else{
                        String error_info=null;
                        if(obj.has("error_msg")) {
                            error_info = obj.getString("error_msg");
                        }
                        Log.d("Testlexiangdaxue","传输参数失败"+error_info);
                        hideDialog();
                        Toast.makeText(context,"未知错误",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e("lexiangdaxueError", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String, String>();
                params.put("name",hashMap.get("name"));
                params.put("email",hashMap.get("email"));
                params.put("post_uuid",post_uuid);
                params.put("isposts","1");
                params.put("type",type);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
        AppController.getInstance().addToRequestQueue(stringRequest,"DetailPostAdapter");
    }

    private void logoutUser(){
        sessionManager.setLogin(false);
        sqLiteHandler.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
