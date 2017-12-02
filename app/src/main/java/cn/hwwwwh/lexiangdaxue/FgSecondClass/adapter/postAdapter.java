package cn.hwwwwh.lexiangdaxue.FgSecondClass.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.squareup.picasso.Picasso;
import com.ytying.emoji.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.FgSecondClass.activity.PostActivity;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.PicBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.PostsBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.other.NineGridPicLayout;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.bean.postData;
import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.XCRoundImageView;

/**
 * Created by 97481 on 2017/3/7/ 0007.
 */

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {

    private View view;
    public Context context;
    public LayoutInflater inflater;
    public List<PostsBean.PostsDataBean> list;
    private ProgressDialog pDialog;
    private SQLiteHandler sqLiteHandler;
    public HashMap<String,String> hashMap;
    private SessionManager session;

    public postAdapter(Context context){
        this.context=context;
        this.inflater= LayoutInflater.from(context);
        pDialog=new ProgressDialog(context);
        pDialog.setCancelable(true);
        sqLiteHandler=new SQLiteHandler(context);
        hashMap=sqLiteHandler.getUserDetails();
        session=new SessionManager(context);
        Log.d("testlexiangdaxue",hashMap.get("name")+"");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.fgsecond_rv_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostsBean.PostsDataBean data=list.get(position);
        holder.bindData(data);
    }

    public void addMoreData(List<PostsBean.PostsDataBean> data){
        if(list!=null){
            list.addAll(list.size(), data);
            notifyItemChanged(list.size());
            //notifyDataSetChanged();
        }
    }

    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第一次从服务器加载数据，或者下拉刷新当前界面数据表）
     *
     * @param data
     */
    public void setData(List<PostsBean.PostsDataBean> data) {
        if (data != null) {
            list = data;
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(list==null) {
            return 0;
        }else{
            return  list.size();
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView post_admin;
        public AppCompatTextView post_content;
        public TextView post_createtime;
        public TextView comment_count;
        public TextView good_count;
        public TextView comment_user1;
        public TextView comment_content1;
        public LinearLayout area_comment;
        public TextView comment_user2;
        public TextView comment_content2;
        public TextView comment_user3;
        public TextView comment_content3;
        public TextView allComment;
        public TextView seeAll;
        public LinearLayout LL_zan;
        public ThumbUpView zan_pic;
        public RelativeLayout post_rv;
        public NineGridPicLayout layout;
        public XCRoundImageView headPic;

        private NineGridImageViewAdapter<PicBean> mAdapter = new NineGridImageViewAdapter<PicBean>() {

            @Override
            protected void onDisplayImage(Context context, ImageView imageView, PicBean picBean) {
                Picasso.with(context).load(picBean.getPic_url()).placeholder(R.drawable.loadpic).into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }

        };

        public ViewHolder(View itemView) {
            super(itemView);
            intiView(itemView);
        }

        private void intiView(View itemView) {
            post_admin=(TextView) itemView.findViewById(R.id.post_admin);
            post_content=(AppCompatTextView) itemView.findViewById(R.id.post_content);
            post_createtime=(TextView) itemView.findViewById(R.id.post_createtime);
            comment_count=(TextView) itemView.findViewById(R.id.comment_count);
            good_count=(TextView) itemView.findViewById(R.id.good_count);
            area_comment=(LinearLayout)itemView.findViewById(R.id.area_comment);
            comment_user1=(TextView)itemView.findViewById(R.id.comment_user1);
            comment_content1=(TextView)itemView.findViewById(R.id.comment_content1);
            comment_user2=(TextView)itemView.findViewById(R.id.comment_user2);
            comment_content2=(TextView)itemView.findViewById(R.id.comment_content2);
            comment_user3=(TextView)itemView.findViewById(R.id.comment_user3);
            comment_content3=(TextView)itemView.findViewById(R.id.comment_content3);
            allComment=(TextView)itemView.findViewById(R.id.allComment);
            seeAll=(TextView)itemView.findViewById(R.id.seeAll);
            LL_zan=(LinearLayout)itemView.findViewById(R.id.LL_zan);
            zan_pic=(ThumbUpView)itemView.findViewById(R.id.zan_pic);
            post_rv=(RelativeLayout)itemView.findViewById(R.id.post_rv);
            layout=(NineGridPicLayout)itemView.findViewById(R.id.layout_nine_grid);
            headPic=(XCRoundImageView)itemView.findViewById(R.id.headPic);
        }

        public void bindData(final PostsBean.PostsDataBean data){
            final int img_num=data.getPost_imgnum();
            if(img_num>0) {
                // new DownloadDetailPicData2(context, layout).execute("http://cs.hwwwwh.cn/admin/SinglePostPicApi.php?post_uuid=" + data.getPostUuid());
                List<PicBean> urlList = new ArrayList<>();//图片url
                if (img_num==1) {
                    PicBean picBean=new PicBean();
                    picBean.setPic_url(data.getPicture().get(0));
                    urlList.add(picBean);
                }else {
                    List<String> picsData=data.getPicture();
                    for(int j=0;j<picsData.size();j++){
                        PicBean picBean=new PicBean();
                        picBean.setPic_url(picsData.get(j));
                        urlList.add(picBean);
                    }
                }
                layout.setIsShowAll(false); //当传入的图片数超过9张时，是否全部显示
                layout.setContext(context);
                layout.setSpacing(10); //动态设置图片之间的间隔
                layout.setSingleImageSize(160);
                layout.setUrlList(urlList); //最后再设置图片url

            }else{
                layout.setVisibility(View.GONE);
            }
            if(data.getHeadPic()!=null){
                Glide.with(context).load(data.getHeadPic()).asBitmap().into(headPic);
            }
            post_rv.setOnClickListener(this);
            post_admin.setText(data.getPost_admin());
            post_createtime.setText(data.getPost_createtime());
            SpannableString content2=StringUtil.stringToSpannableString(data.getPost_content(),context);
            post_content.setText(content2);
            post_content.setOnClickListener(this);
            post_content.setVerticalScrollBarEnabled(false);
            dealContent(data);
            comment_count.setText(data.getPost_commentcount()+"");
            good_count.setText(data.getPost_goodcount()+"");
            zan_pic.setUnLikeType(ThumbUpView.LikeType.broken);
            zan_pic.setCracksColor(Color.rgb(255, 153, 0));
            zan_pic.setFillColor(Color.rgb(255, 153, 0));
            zan_pic.setEdgeColor(Color.parseColor("#A9A9A9"));
            zan_pic.setOnThumbUp(new ThumbUpView.OnThumbUp() {
                @Override
                public void like(boolean like) {
                        if(like){
                            if(session.isLoggedIn()) {
                                data.setIsZan("1");
                                int count=(Integer.parseInt(good_count.getText().toString())+1);
                                good_count.setText(count+"");
                                data.setPost_goodcount(count);
                                sendPost(hashMap,data.getPost_uuid(),"0");
                            }else{
                                zan_pic.stopAnim();
                                logoutUser();
                            }
                        }else{
                            if(session.isLoggedIn()) {
                                data.setIsZan("0");
                                int count=(Integer.parseInt(good_count.getText().toString())-1);
                                good_count.setText(count+"");
                                data.setPost_goodcount(count);
                                sendPost(hashMap,data.getPost_uuid(),"1");
                            }else{
                                zan_pic.stopAnim();
                                logoutUser();
                            }
                        }
                }
            });
            if (data.getIsZan().equals("1")){
                zan_pic.setLike();
            }else{
                zan_pic.setUnlike();
            }
            int i=data.getPost_commentcount();
            if(i>3){
                allComment.setVisibility(View.VISIBLE);
                allComment.setText("查看所有"+i+"条评论");
            }else{
                allComment.setVisibility(View.GONE);
            }
            if(data.getComment()!=null) {
                for (int j = 0; j < data.getComment().size(); j++) {
                    SpannableString content;
                    if (j == 0) {
                        area_comment.setVisibility(View.VISIBLE);
                        comment_user1.setVisibility(View.VISIBLE);
                        comment_content1.setVisibility(View.VISIBLE);
                        comment_user2.setVisibility(View.GONE);
                        comment_content2.setVisibility(View.GONE);
                        comment_user3.setVisibility(View.GONE);
                        comment_content3.setVisibility(View.GONE);
                        comment_user1.setText(data.getComment().get(0).getReply_admin() + ":");
                        //超过字符显示...
                        StringBuffer a = new StringBuffer(data.getComment().get(0).getReply_content());
                        if (a.length() > 80) {
                            a = a.replace(80, a.length(), "...");
                        }
                        content = StringUtil.stringToSpannableString(a.toString(), context);
                        comment_content1.setText(content);
                    } else if (j == 1) {
                        comment_user2.setVisibility(View.VISIBLE);
                        comment_content2.setVisibility(View.VISIBLE);
                        comment_user3.setVisibility(View.GONE);
                        comment_content3.setVisibility(View.GONE);
                        comment_user2.setText(data.getComment().get(1).getReply_admin() + ":");
                        //超过字符显示...
                        StringBuffer b = new StringBuffer(data.getComment().get(1).getReply_content());
                        if (b.length() > 80) {
                            b = b.replace(80, b.length(), "...");
                        }
                        content = StringUtil.stringToSpannableString(b.toString(), context);
                        comment_content2.setText(content);
                    } else if (j == 2) {
                        comment_user3.setVisibility(View.VISIBLE);
                        comment_content3.setVisibility(View.VISIBLE);
                        comment_user3.setText(data.getComment().get(2).getReply_admin() + ":");
                        //超过字符显示...
                        StringBuffer c = new StringBuffer(data.getComment().get(2).getReply_content());
                        if (c.length() > 80) {
                            c = c.replace(80, c.length(), "...");
                        }
                        content = StringUtil.stringToSpannableString(c.toString(), context);
                        comment_content3.setText(content, TextView.BufferType.SPANNABLE);
                    } else {
                        return;
                    }
                }
            }else{
                area_comment.setVisibility(View.GONE);
            }
        }

        public void dealContent(final PostsBean.PostsDataBean data){
            /*通过textview获取Layout，然后根据Layout的一个方法getEllipsisCount(int)，
            来判断是否已经省略，但Layout大多时候获取到的都是null，为什么呢？原因是，Layout要等TextView绘制完了才能够拿到Layout的对象。以下两种方法*/
           /* ViewTreeObserver vto =post_reply.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                }
            });*/
            if(data.getIsShowAll()==0) {
                post_content.post(new Runnable() {
                    @Override
                    public void run() {
                        Layout l = post_content.getLayout();
//                        Log.d("lexiangdx", l.getLineCount() + " ");
//                        Log.d("lexiangdx", l.getEllipsisCount(l.getLineCount() - 1) + " ");
                        if (l != null) {
                            int lines = l.getLineCount();
                            if (lines > 0) {
                                if (l.getEllipsisCount(lines - 1) > 0) {
                                    seeAll.setVisibility(View.VISIBLE);
                                    data.setIsShowAll(1);
                                }else{
                                    seeAll.setVisibility(View.GONE);
                                    data.setIsShowAll(2);
                                }
//                                if (lines >= 4 ) {
//                                    seeAll.setVisibility(View.VISIBLE);
//                                    data.setIsShowAll(1);
//                                } else {
//                                    seeAll.setVisibility(View.GONE);
//                                    data.setIsShowAll(2);
//                                }
                            }
                        }
                    }
                });
            }else if(data.getIsShowAll()==1){
                seeAll.setVisibility(View.VISIBLE);
            }else {
                seeAll.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_rv: case R.id.post_content:
                    PostsBean.PostsDataBean data=list.get(getLayoutPosition());
                    Intent intent =new Intent(context,PostActivity.class);
                    intent.putExtra("post_uuid",data.getPost_uuid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
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
                            Log.d("Testlexiangdaxue","成功点赞");
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
                    Log.d("lexiangdaxueError",error.toString());
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
                    params.put("uid",hashMap.get("uid"));
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30*1000,1,1f));
            AppController.getInstance().addToRequestQueue(stringRequest,"postAdapter");
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