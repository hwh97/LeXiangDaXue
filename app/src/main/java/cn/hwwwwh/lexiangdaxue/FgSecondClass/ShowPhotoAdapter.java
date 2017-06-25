package cn.hwwwwh.lexiangdaxue.FgSecondClass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.EditActivity;
import cn.hwwwwh.lexiangdaxue.ImageLoader.SelectPhotoAdapter;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShowPhotoActivity;

/**
 * Created by 97481 on 2017/3/20/ 0020.
 */

public class ShowPhotoAdapter extends RecyclerView.Adapter<ShowPhotoAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener ,View.OnTouchListener{

    private View view;
    private Context context;
    public LayoutInflater inflater;
    protected List<SelectPhotoAdapter.SelectPhotoEntity> mDatas;
    //是否显示选择框
    private boolean isShowCheckBox=false;
    //接口实例
    private RecyclerViewOnItemClickListener onItemClickListener;
    public boolean isLongClick=true;
    private TextView hint1;
    private TextView hint2;
    public EditActivity activity;

    public ShowPhotoAdapter(Context context,EditActivity activity){
        this.context=context;
        this.inflater= LayoutInflater.from(context);
        this.activity=activity;
        hint1=(TextView)activity.findViewById(R.id.hint1);
        hint2=(TextView)activity.findViewById(R.id.hint2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.edit_rv_detail,parent,false);
        view.setOnClickListener(this);
        view.setOnTouchListener(this);
        // view.setOnLongClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.bindData(mDatas.get(position).url);
        //长按显示/隐藏
        if (isShowCheckBox) {
            holder.cb.setVisibility(View.VISIBLE);
        } else {
            holder.cb.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setTag(position);
    }



    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第一次从服务器加载数据，或者下拉刷新当前界面数据表）
     *
     * @param data
     */
    public void setData(List<SelectPhotoAdapter.SelectPhotoEntity> data) {

        if (data != null) {
            mDatas = data;
        } else {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    public void addMoreData(List<SelectPhotoAdapter.SelectPhotoEntity> data){
        if(data!=null){
            mDatas.addAll(mDatas.size(), data);
            notifyDataSetChanged();
        }
    }

    public List<SelectPhotoAdapter.SelectPhotoEntity> getmDatas() {
        return mDatas;
    }

    @Override
    public int getItemCount() {
        if(mDatas==null) {
            return 0;
        }else{
            return  mDatas.size();
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            onItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return  onItemClickListener!= null && onItemClickListener.onItemTouchClickListener(v,event, (Integer) v.getTag());
    }

    @Override
    public boolean onLongClick(View v) {
        //不管显示隐藏，清空状态
        return onItemClickListener != null && onItemClickListener.onItemLongClickListener(v, (Integer) v.getTag());
    }

    //设置点击事件
    public void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //设置是否显示CheckBox
    public void setShowBox() {
        //取反
        isShowCheckBox = !isShowCheckBox;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        private ImageView cb;

        public ViewHolder(View itemView) {
            super(itemView);
            intiView(itemView);
        }

        private void intiView(View itemView) {
            imageView=(ImageView)itemView.findViewById(R.id.selectPhoto);
            cb=(ImageView)itemView.findViewById(R.id.cb);
            cb.setOnClickListener(this);
        }

        private void bindData(String url){
            Glide.with(context).load(url).placeholder(R.drawable.loadpic).into(imageView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cb:
                    mDatas.remove(getLayoutPosition());
                    notifyDataSetChanged();
                    if(mDatas.size()==0){
                        hint1.setVisibility(View.INVISIBLE);
                        hint2.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    }

    public interface RecyclerViewOnItemClickListener{
        //点击事件
        void onItemClickListener(View view, int position);

        //长按事件
        boolean onItemLongClickListener(View view, int position);

        //触摸事件
        boolean onItemTouchClickListener(View view, MotionEvent event, int position);

    }


    public boolean getIsShowCheckBox(){
        return  isShowCheckBox;
    }

    public void setNotShowCheckBox(){
        isShowCheckBox=false;
    }

}