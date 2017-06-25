package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.ldoublem.thumbUplib.ThumbUpView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.squareup.picasso.Picasso;
import com.ytying.emoji.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.LoginRegister.AppController;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import me.codeboy.android.aligntextview.CBAlignTextView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by 97481 on 2017/6/19/ 0019.
 */

public class PostViewBinder extends ItemViewBinder<postData,PostViewBinder.ViewHolder>{

    private ProgressDialog pDialog;
    private SQLiteHandler sqLiteHandler;
    public HashMap<String,String> hashMap;


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view=inflater.inflate(R.layout.fgsecond_rv_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull postData item) {
        holder.bindData(item);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView post_admin;
        public TextView post_content;
        public TextView post_createtime;
        public TextView comment_count;
        public TextView good_count;
        public TextView comment_user1;
        public CBAlignTextView comment_content1;
        public RelativeLayout area_comment;
        public TextView comment_user2;
        public CBAlignTextView comment_content2;
        public TextView comment_user3;
        public CBAlignTextView comment_content3;
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
        public NineGridImageView layout;

        public ViewHolder(View itemView) {
            super(itemView);
            intiView(itemView);
        }

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


        private void intiView(View itemView) {
            post_admin=(TextView) itemView.findViewById(R.id.post_admin);
            post_content=(TextView) itemView.findViewById(R.id.post_content);
            post_createtime=(TextView) itemView.findViewById(R.id.post_createtime);
            comment_count=(TextView) itemView.findViewById(R.id.comment_count);
            good_count=(TextView) itemView.findViewById(R.id.good_count);
            area_comment=(RelativeLayout)itemView.findViewById(R.id.area_comment);
            comment_user1=(TextView)itemView.findViewById(R.id.comment_user1);
            comment_content1=(CBAlignTextView)itemView.findViewById(R.id.comment_content1);
            comment_user2=(TextView)itemView.findViewById(R.id.comment_user2);
            comment_content2=(CBAlignTextView)itemView.findViewById(R.id.comment_content2);
            comment_user3=(TextView)itemView.findViewById(R.id.comment_user3);
            comment_content3=(CBAlignTextView)itemView.findViewById(R.id.comment_content3);
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
            layout=(NineGridImageView)itemView.findViewById(R.id.layout_nine_grid);
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
                layout.setAdapter(mAdapter);
                layout.setImagesData(urlList);
            }else{
                layout.setVisibility(View.GONE);
            }
            sqLiteHandler=new SQLiteHandler(itemView.getContext());
            hashMap=sqLiteHandler.getUserDetails();
           // post_rv.setOnClickListener((View.OnClickListener) itemView.getContext());
            post_admin.setText(data.getPostAdmin());
            post_createtime.setText(data.getCreateTime());
            dealContent(data.getPostContent());
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
                        data.setIsZan(true);
                        int count=(Integer.parseInt(good_count.getText().toString())+1);
                        good_count.setText(count+"");
                        data.setGoodCount(count+"");
                        sendPost(hashMap,data.getPostUuid(),"0");
                    }else{
                        data.setIsZan(false);
                        int count=(Integer.parseInt(good_count.getText().toString())-1);
                        good_count.setText(count+"");
                        data.setGoodCount(count+"");
                        sendPost(hashMap,data.getPostUuid(),"1");
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
            }
            if(data.getComment1()!=null){
                area_comment.setVisibility(View.VISIBLE);
                comment_user1.setVisibility(View.VISIBLE);
                comment_content1.setVisibility(View.VISIBLE);
                comment_user1.setText(data.getUser1()+"：");
                //超过字符显示...
                StringBuffer a=new StringBuffer(data.getComment1());
                if(a.length()>80){
                    a=a.replace(80,a.length(),"...");
                }
                comment_content1.setText(a);
                //加载第二条评论
                if(data.getComment2()!=null){
                    comment_user2.setVisibility(View.VISIBLE);
                    comment_content2.setVisibility(View.VISIBLE);
                    comment_user2.setText(data.getUser2()+"：");
                    //超过字符显示...
                    StringBuffer b=new StringBuffer(data.getComment2());
                    if(b.length()>80){
                        b=b.replace(80,b.length(),"...");
                    }
                    comment_content2.setText(b);
                    //加载第三条评论
                    if(data.getComment3()!=null){
                        comment_user3.setVisibility(View.VISIBLE);
                        comment_content3.setVisibility(View.VISIBLE);
                        comment_user3.setText(data.getUser3()+"：");
                        //超过字符显示...
                        StringBuffer c=new StringBuffer(data.getComment3());
                        if(c.length()>80){
                            c=c.replace(80,c.length(),"...");
                        }
                        comment_content3.setText(c);
                    }
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

        public void dealContent(String content){
            SpannableString content2= StringUtil.stringToSpannableString(content,itemView.getContext());
            post_content.setText(content2);
            /*通过textview获取Layout，然后根据Layout的一个方法getEllipsisCount(int)，
            来判断是否已经省略，但Layout大多时候获取到的都是null，为什么呢？原因是，Layout要等TextView绘制完了才能够拿到Layout的对象。以下两种方法*/
           /* ViewTreeObserver vto =post_reply.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                }
            });*/
            post_content.post(new Runnable() {
                @Override
                public void run() {
                    Layout l=post_content.getLayout();
                    if (l != null) {
                        int lines = l.getLineCount();
                        if (lines > 0) {
                            if (l.getEllipsisCount(lines - 1) > 0) {
                                seeAll.setVisibility(View.VISIBLE);
                            }else{
                                seeAll.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.post_rv:
//                    postData data=list.get(getLayoutPosition());
//                    Intent intent =new Intent(itemView.getContext(),PostActivity.class);
//                    intent.putExtra("post_uuid",data.getPostUuid());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    itemView.getContext().startActivity(intent);
                    break;
            }
        }


        public void sendPost(final HashMap<String, String> hashMap, final String post_uuid, final String type){
            pDialog=new ProgressDialog(itemView.getContext());
            pDialog.setCancelable(true);
            pDialog.setMessage("提交数据中...");
//            showDialog();

            StringRequest stringRequest=new StringRequest(Request.Method.POST, AppConfig.urlHandleZan, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    hideDialog();
                    try {
                        JSONObject obj=new JSONObject(response);
                        boolean error = obj.getBoolean("error");
                        String info=obj.getString("error_msg");
                        if (!error){
                            Log.d("Testlexiangdaxue","成功点赞");
                        }else{
                            Log.d("Testlexiangdaxue","传输参数失败"+info);
                            hideDialog();
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

}
