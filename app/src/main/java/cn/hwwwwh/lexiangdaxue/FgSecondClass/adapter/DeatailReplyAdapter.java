package cn.hwwwwh.lexiangdaxue.FgSecondClass.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.ytying.emoji.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.ReplyBean;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.BaseRecyclerAdapter;
import cn.hwwwwh.lexiangdaxue.other.XCRoundImageView;

/**
 * Created by 97481 on 2017/6/16/ 0016.
 */

public class DeatailReplyAdapter extends BaseRecyclerAdapter<ReplyBean.ReplyDataBean> {

    private Context context;
    private ProgressDialog pDialog;
    private SQLiteHandler sqLiteHandler;
    //用户信息
    private SessionManager session;
    public HashMap<String,String> hashMap;

    public DeatailReplyAdapter(Context context){
        this.context=context;
        pDialog=new ProgressDialog(context);
        pDialog.setCancelable(true);
        sqLiteHandler=new SQLiteHandler(context);
        hashMap=sqLiteHandler.getUserDetails();
        session=new SessionManager(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    public List<String> popReply=new ArrayList<>();

    @Override
    public void addDatas(List<ReplyBean.ReplyDataBean> datas) {
        Iterator<ReplyBean.ReplyDataBean> sListIterator = datas.iterator();
        while(sListIterator.hasNext()){
            ReplyBean.ReplyDataBean replyBean = sListIterator.next();
            if(replyBean.getIspop().equals("1")){
                popReply.add(replyBean.getReply_uuid());
            }
            // Log.d("testlexiangdaxue",replyBean.getReply_uuid());
            if(!replyBean.getIspop().equals("1")&& popReply.contains(replyBean.getReply_uuid())){
                sListIterator.remove();
                //Log.d("testlexiangdaxue","remove"+replyBean.getReply_content());
            }

        }
        //直接用arraylist的remove 会出现下标不统一 ，需要用 Iterator<ReplyBean> sListIterator = datas.iterator();解决。！！

//        for(int i=0;i<datas.size();i++){
//            ReplyBean replyBean=datas.get(i);
//            if(replyBean.ispop){
//                popReply.add(replyBean.getReply_uuid());
//            }
//
//            // Log.d("testlexiangdaxue",replyBean.getReply_uuid());
//            if(!replyBean.ispop&& popReply.contains(replyBean.getReply_uuid())){
//                datas.remove(replyBean);
//                //Log.d("testlexiangdaxue","remove"+replyBean.getReply_content());
//            }
//        }
//        Log.d("testlexiangdaxue",datas.size()+"  ");
        super.addDatas(datas);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_rv_detail, parent, false);
        return new MyHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, ReplyBean.ReplyDataBean data) {

        if(viewHolder instanceof MyHolder) {
            ((MyHolder)viewHolder).bindData(data,RealPosition);
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


    class MyHolder extends BaseRecyclerAdapter.Holder {

        public TextView post_content;
        public TextView post_admin;
        public TextView post_createtime;
        public TextView good_count;
        public ThumbUpView zan;
        private XCRoundImageView headPic;


        public MyHolder(View itemView) {
            super(itemView);
            post_content = (TextView) itemView.findViewById(R.id.post_content);
            good_count=(TextView) itemView.findViewById(R.id.good_count);
            post_admin=(TextView) itemView.findViewById(R.id.post_admin);
            post_createtime=(TextView) itemView.findViewById(R.id.post_createtime);
            zan=(ThumbUpView)itemView.findViewById(R.id.zan);
            headPic=(XCRoundImageView)itemView.findViewById(R.id.headPic);
        }

        public void bindData(final ReplyBean.ReplyDataBean data, final int pos){
            if (data.getReply_isposts()==0){
                String replyInfo="回复 "+data.getReply_replyadmin()+"：";
                String replyContent= data.getReply_content();
                SpannableString styledText = new SpannableString(replyInfo+replyContent);
                SpannableString b= StringUtil.stringToSpannableString(styledText.toString(),context);
                b.setSpan(new TextAppearanceSpan(context, R.style.style0), 0, replyInfo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                post_content.setText(b, TextView.BufferType.SPANNABLE);
            }else {
                SpannableString content= StringUtil.stringToSpannableString(data.getReply_content(),context);
                post_content.setText(content);
            }
            post_admin.setText(data.getReply_admin());
            post_createtime.setText(data.getReply_createtime());
            good_count.setText(data.getReply_goodcount()+"");
            zan.setUnLikeType(ThumbUpView.LikeType.broken);
            zan.setCracksColor(Color.rgb(255, 153, 0));
            zan.setFillColor(Color.rgb(255, 153, 0));
            zan.setEdgeColor(Color.parseColor("#A9A9A9"));
            zan.setOnThumbUp(new ThumbUpView.OnThumbUp() {
                @Override
                public void like(boolean like) {
                    if(like){
                        if(session.isLoggedIn()) {
                            data.setReply_isZan(1);
                            int count = (Integer.parseInt(good_count.getText().toString()) + 1);
                            good_count.setText(count + "");
                            data.setReply_goodcount(count);
                            sendPost(hashMap, data.getReply_uuid(), "0");
                        }else{
                            zan.stopAnim();
                            logoutUser();
                        }
                    }else{
                        if(session.isLoggedIn()) {
                            data.setReply_isZan(0);
                            int count=(Integer.parseInt(good_count.getText().toString())-1);
                            good_count.setText(count+"");
                            data.setReply_goodcount(count);
                            sendPost(hashMap,data.getReply_uuid(),"1");
                        }else{
                            zan.stopAnim();
                            zan.setLike();
                            logoutUser();
                        }
                    }
                }
            });
            if (data.getReply_isZan()==1){
                zan.setLike();
            }else{
                zan.setUnlike();
            }

            if(data.getHeadPic()!=null){
                Glide.with(context).load(data.getHeadPic()).asBitmap().into(headPic);
            }
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
                    params.put("isposts","0");
                    params.put("type",type);
                    params.put("uid",hashMap.get("uid"));
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
            AppController.getInstance().addToRequestQueue(stringRequest,"DetailPostAdapter");
        }



    }

    private void logoutUser(){
        session.setLogin(false);
        sqLiteHandler.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
