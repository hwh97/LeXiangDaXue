package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldoublem.thumbUplib.ThumbUpView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.BaseRecyclerAdapter;

/**
 * Created by 97481 on 2017/6/16/ 0016.
 */

public class DeatailPostAdapter extends BaseRecyclerAdapter<ReplyBean> {
    private Context context;

    public DeatailPostAdapter(Context context){
        this.context=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    public List<String> popReply=new ArrayList<>();

    @Override
    public void addDatas(ArrayList<ReplyBean> datas) {
        for(int i=0;i<datas.size();i++){
            ReplyBean replyBean=datas.get(i);
            if(replyBean.ispop){
                popReply.add(replyBean.getReply_uuid());
            }
            if(!replyBean.ispop&& popReply.contains(replyBean.getReply_uuid())){
                datas.remove(replyBean);
                Log.d("testlexiangdaxue","remove"+replyBean.getReply_content());
            }
        }
        Log.d("testlexiangdaxue",popReply.size()+" ");
        super.addDatas(datas);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_rv_detail, parent, false);
        return new MyHolder(layout);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, ReplyBean data) {

        if(viewHolder instanceof MyHolder) {
            ((MyHolder)viewHolder).bindData(data,RealPosition);
        }
    }



    class MyHolder extends BaseRecyclerAdapter.Holder {

        public TextView post_content;
        public TextView post_admin;
        public TextView post_createtime;
        public TextView good_count;
        public ThumbUpView zan;


        public MyHolder(View itemView) {
            super(itemView);
            post_content = (TextView) itemView.findViewById(R.id.post_content);
            good_count=(TextView) itemView.findViewById(R.id.good_count);
            post_admin=(TextView) itemView.findViewById(R.id.post_admin);
            post_createtime=(TextView) itemView.findViewById(R.id.post_createtime);
            zan=(ThumbUpView)itemView.findViewById(R.id.zan);
        }

        public void bindData(final ReplyBean data, final int pos){
            if (data.Reply_isReplys){
                String replyInfo="回复 "+data.getReply_replyadmin()+"：";
                String replyContent=data.getReply_content();
                SpannableString styledText = new SpannableString(replyInfo+replyContent);
                styledText.setSpan(new TextAppearanceSpan(context, R.style.style0), 0, replyInfo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                post_content.setText(styledText, TextView.BufferType.SPANNABLE);
            }else {
                post_content.setText(data.getReply_content());
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

                    }else{

                    }
                }
            });

        }
    }
}
