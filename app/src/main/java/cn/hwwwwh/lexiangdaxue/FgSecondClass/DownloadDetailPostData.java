package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;

import static com.ytying.emoji.EmotionGridAdapter.dp2px;

/**
 * Created by 97481 on 2017/6/5/ 0005.
 */

public class DownloadDetailPostData extends AsyncTask<String,Void,List<DetailPostBean>>{

    private View itemView;
    private Context context;
    private TextView username;
    private TextView postTime;
    private TextView good_count;
    private TextView post_content;
    private NineGridPicLayout layout_nine_grid;
    private ImageView zeroComment;
    private RelativeLayout noCommentRL;


    public DownloadDetailPostData(Context context,View itemView){
        this.context=context;
        this.itemView=itemView;
    }

    @Override
    protected List<DetailPostBean> doInBackground(String... params) {
        List<DetailPostBean>  list=new ArrayList<>();
        String Url=params[0];
        if(HttpUtils.isNetConn(context)){
            byte[]b=null;
            b=HttpUtils.downloadFromNet(Url);
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
                list.add(detailPostBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<DetailPostBean>  list) {
        super.onPostExecute(list);
        if(list!=null){
            username=(TextView)itemView.findViewById(R.id.username);
            postTime=(TextView)itemView.findViewById(R.id.postTime);
            good_count=(TextView)itemView.findViewById(R.id.good_count);
            post_content=(TextView)itemView.findViewById(R.id.post_content);
            layout_nine_grid=(NineGridPicLayout)itemView.findViewById(R.id.layout_nine_grid);
            zeroComment=(ImageView)itemView.findViewById(R.id.zeroComment);
            noCommentRL=(RelativeLayout)itemView.findViewById(R.id.noCommentRL);
            username.setText(list.get(0).getPostAdmin());
            postTime.setText(list.get(0).getCreateTime());
            good_count.setText(list.get(0).getGoodCount());
            post_content.setText(list.get(0).getPostContent());
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
}
