package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.ninegridimageview.ItemImageClickListener;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.lzy.ninegrid.ImageInfo;
import com.squareup.picasso.Picasso;
import com.ytying.emoji.SmileLayout;
import com.ytying.emoji.SmileManager;
import com.ytying.emoji.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import me.codeboy.android.aligntextview.CBAlignTextView;

/**
 * Created by 97481 on 2017/3/7/ 0007.
 */

public class postAdapter extends RecyclerView.Adapter<postAdapter.ViewHolder> {
    private View view;
    public Context context;
    public LayoutInflater inflater;
    public List<postData> list;
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
        postData data=list.get(position);
        holder.bindData(data);
    }

    public void addMoreData(List<postData> data){
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
    public void setData(List<postData> data) {
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
        public ImageView fg2_rv_img1;
        public ImageView fg2_rv_img2;
        public ImageView fg2_rv_img3;
        public RelativeLayout thirdRl;
        public TextView pic_num;
        public LinearLayout imgs;
        public TextView seeAll;
        public LinearLayout LL_zan;
        public ThumbUpView zan_pic;
        public RelativeLayout post_rv;
        public NineGridPicLayout layout;

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
//            fg2_rv_img1=(ImageView)itemView.findViewById(R.id.fg2_rv_img1);
//            fg2_rv_img2=(ImageView)itemView.findViewById(R.id.fg2_rv_img2);
//            fg2_rv_img3=(ImageView)itemView.findViewById(R.id.fg2_rv_img3);
//            thirdRl=(RelativeLayout)itemView.findViewById(R.id.thirdRl);
//            pic_num=(TextView)itemView.findViewById(R.id.pic_num);
//            imgs=(LinearLayout)itemView.findViewById(R.id.imgs);
            seeAll=(TextView)itemView.findViewById(R.id.seeAll);
            LL_zan=(LinearLayout)itemView.findViewById(R.id.LL_zan);
            zan_pic=(ThumbUpView)itemView.findViewById(R.id.zan_pic);
            post_rv=(RelativeLayout)itemView.findViewById(R.id.post_rv);
            layout=(NineGridPicLayout)itemView.findViewById(R.id.layout_nine_grid);
        }

        public void bindData(final postData data){
            final int img_num=Integer.parseInt(data.getImgNum());
            if(img_num>0) {
                // new DownloadDetailPicData2(context, layout).execute("http://cs.hwwwwh.cn/admin/SinglePostPicApi.php?post_uuid=" + data.getPostUuid());
                List<PicBean> urlList = new ArrayList<>();//图片url
                if (img_num==1) {
                    PicBean picBean=new PicBean();
                    picBean.setPic_url(data.getPictureUrl1());
                    picBean.setPic_width(data.getPicture1Width());
                    picBean.setPic_height(data.getPicture1Height());
                    urlList.add(picBean);
                }else {
                    List<String> picsData=data.getPicsData();
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
            post_rv.setOnClickListener(this);
            post_admin.setText(data.getPostAdmin());
            post_createtime.setText(data.getCreateTime());
            SpannableString content2=StringUtil.stringToSpannableString(data.getPostContent(),context);
            post_content.setText(content2);
            post_content.setOnClickListener(this);
            post_content.setVerticalScrollBarEnabled(false);
            dealContent(data);
            comment_count.setText(data.getCommentCount());
            good_count.setText(data.getGoodCount());
            zan_pic.setUnLikeType(ThumbUpView.LikeType.broken);
            zan_pic.setCracksColor(Color.rgb(255, 153, 0));
            zan_pic.setFillColor(Color.rgb(255, 153, 0));
            zan_pic.setEdgeColor(Color.parseColor("#A9A9A9"));
            zan_pic.setOnThumbUp(new ThumbUpView.OnThumbUp() {
                @Override
                public void like(boolean like) {
                        if(like){
                            if(session.isLoggedIn()) {
                                data.setIsZan(true);
                                int count=(Integer.parseInt(good_count.getText().toString())+1);
                                good_count.setText(count+"");
                                data.setGoodCount(count+"");
                                sendPost(hashMap,data.getPostUuid(),"0");
                            }else{
                                zan_pic.stopAnim();
                                logoutUser();
                            }
                        }else{
                            if(session.isLoggedIn()) {
                                data.setIsZan(false);
                                int count=(Integer.parseInt(good_count.getText().toString())-1);
                                good_count.setText(count+"");
                                data.setGoodCount(count+"");
                                sendPost(hashMap,data.getPostUuid(),"1");
                            }else{
                                zan_pic.stopAnim();
                                logoutUser();
                            }
                        }
                }
            });
            if (data.getIsZan()){
                zan_pic.setLike();
            }else{
                zan_pic.setUnlike();
            }
            int i=Integer.parseInt(data.getCommentCount());
            if(i>3){
                allComment.setVisibility(View.VISIBLE);
                allComment.setText("查看所有"+i+"条评论");
            }else{
                allComment.setVisibility(View.GONE);
            }
            if(data.getComment1()!=null){
                area_comment.setVisibility(View.VISIBLE);
                comment_user1.setVisibility(View.VISIBLE);
                comment_content1.setVisibility(View.VISIBLE);
                comment_user1.setText(data.getUser1()+":");
                //超过字符显示...
                StringBuffer a=new StringBuffer(data.getComment1());
                if(a.length()>80){
                    a=a.replace(80,a.length(),"...");
                }
                SpannableString content=StringUtil.stringToSpannableString(a.toString(),context);
                comment_content1.setText(content);
                //加载第二条评论
                if(data.getComment2()!=null){
                    comment_user2.setVisibility(View.VISIBLE);
                    comment_content2.setVisibility(View.VISIBLE);
                    comment_user2.setText(data.getUser2()+":");
                    //超过字符显示...
                    StringBuffer b=new StringBuffer(data.getComment2());
                    if(b.length()>80){
                        b=b.replace(80,b.length(),"...");
                    }
                    content=StringUtil.stringToSpannableString(b.toString(),context);
                    comment_content2.setText(content);
                    //加载第三条评论
                    if(data.getComment3()!=null){
                        comment_user3.setVisibility(View.VISIBLE);
                        comment_content3.setVisibility(View.VISIBLE);
                        comment_user3.setText(data.getUser3()+":");
                        //超过字符显示...
                        StringBuffer c=new StringBuffer(data.getComment3());
                        if(c.length()>80){
                            c=c.replace(80,c.length(),"...");
                        }
                        content=StringUtil.stringToSpannableString(c.toString(),context);
                        comment_content3.setText(content, TextView.BufferType.SPANNABLE);
                    }else{
                        comment_user3.setVisibility(View.GONE);
                        comment_content3.setVisibility(View.GONE);
                    }
                }else{
                    comment_user2.setVisibility(View.GONE);
                    comment_content2.setVisibility(View.GONE);
                    comment_user3.setVisibility(View.GONE);
                    comment_content3.setVisibility(View.GONE);
                }
            }else{
                area_comment.setVisibility(View.GONE);
            }
//            //显示图片逻辑

//            if(img_num>3){
//                thirdRl.setVisibility(View.VISIBLE);
//                pic_num.setVisibility(View.VISIBLE);
//                pic_num.setText(img_num+"张");
//            }
//            if(img_num==1) {
//                int screenWidth = getScreenWidth(context);
//                fg2_rv_img2.setVisibility(View.GONE);
//                fg2_rv_img1.setMaxHeight(screenWidth*2);
//                fg2_rv_img1.setMaxWidth((int)(screenWidth*0.7));
//                thirdRl.setVisibility(View.GONE);
//                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) fg2_rv_img1.getLayoutParams();
//                int width=Integer.parseInt(data.getPicture1Width());
//                int height=Integer.parseInt(data.getPicture1Height());
//                if(width>(int)(screenWidth*0.8)){
//                    layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.weight = 0;
//                    fg2_rv_img1.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//                else{
//                    layoutParams = new LinearLayout.LayoutParams(width, height);
//                    layoutParams.weight = 0;
//                    fg2_rv_img1.setScaleType(ImageView.ScaleType.FIT_XY);
//                }
//                fg2_rv_img1.setLayoutParams(layoutParams);
//            }else{
//                if(img_num==2){
//                    thirdRl.setVisibility(View.INVISIBLE);
//                }
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getPixelsFromDp(80));
//                layoutParams.weight = 1;
//                fg2_rv_img1.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                fg2_rv_img1.setLayoutParams(layoutParams);
//            }
//            if(data.getPictureUrl1()!=null){
//                imgs.setVisibility(View.VISIBLE);
//                fg2_rv_img1.setVisibility(View.VISIBLE);
//                Glide.with(context).load(data.getPictureUrl1()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loadpic).dontAnimate().into(fg2_rv_img1);
//                if(data.getPictureUrl2()!=null){
//                    fg2_rv_img2.setVisibility(View.VISIBLE);
//                    Glide.with(context).load(data.getPictureUrl2()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loadpic).dontAnimate().into(fg2_rv_img2);
//                    if(data.getPictureUrl3()!=null){
//                        thirdRl.setVisibility(View.VISIBLE);
//                        fg2_rv_img3.setVisibility(View.VISIBLE);
//                        Glide.with(context).load(data.getPictureUrl3()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loadpic).dontAnimate().into(fg2_rv_img3);
//                    }
//                }
//            }else{
//                imgs.setVisibility(View.GONE);
//            }
//
        }

        public void dealContent(final postData data){
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
                        Log.d("lexiangdx", l.getLineCount() + " ");
                        Log.d("lexiangdx", l.getEllipsisCount(l.getLineCount() - 1) + " ");
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

        public  int getScreenWidth(Context context) {
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            return display.getWidth();
        }

        private int getPixelsFromDp(int size){

            DisplayMetrics metrics =context.getResources().getDisplayMetrics();

            return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_rv: case R.id.post_content:
                    postData data=list.get(getLayoutPosition());
                    Intent intent =new Intent(context,PostActivity.class);
                    intent.putExtra("post_uuid",data.getPostUuid());
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