package cn.hwwwwh.lexiangdaxue.ProductClass.adpter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.fragment.ProductFragment;
import cn.hwwwwh.lexiangdaxue.R;

/**
 * Created by 97481 on 2017/4/3/ 0003.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> list;
    private LayoutInflater inflater;
    private ProductActivity activity;
    private View view;
    private List<Boolean> isClicks;//控件是否被点击,默认为false，如果被点击，改变值，控件根据值改变自身颜色

    public CategoryAdapter(ProductActivity activity, List<Category> list){
        this.activity=activity;
        inflater=LayoutInflater.from(activity);
        this.list=list;
        isClicks = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            if(i==0){
                isClicks.add(true);
                continue;
            }
            isClicks.add(false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.category_lv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category=list.get(position);
        holder.bindData(category,position);
    }

    @Override
    public int getItemCount() {
        if(list==null) {
            return 0;
        }else{
            return  list.size();
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tv_summary;
        private ImageView imageView;
        private View view;
        private int tempPositon=0;
        private String column;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_summary=(TextView)itemView.findViewById(R.id.category_name);
            imageView=(ImageView)itemView.findViewById(R.id.gg);
            itemView.setOnClickListener(this);
        }

        public void bindData(Category category,int position) {
            if (isClicks.get(position)){
                imageView.setVisibility(View.VISIBLE);
                itemView.setBackground(itemView.getResources().getDrawable(R.drawable.lv_select_bg));
            }else{
                itemView.setBackground(itemView.getResources().getDrawable(R.drawable.lv_reject_bg));
                imageView.setVisibility(View.INVISIBLE);
            }
            tv_summary.setText(category.getSummary());
            tv_summary.setTag(category.getId());
        }

        @Override
        public void onClick(View v) {
            ProductFragment productFragment = new ProductFragment();
            FragmentTransaction fragmentTransaction=activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, productFragment);
            //通过bundle传值给MyFragment
            Bundle bundle = new Bundle();
            bundle.putString(ProductFragment.TAG, tv_summary.getTag().toString());
            bundle.putString("column",activity.getColumn());
            productFragment.setArguments(bundle);
            fragmentTransaction.commit();
            //tempPostion = position;
            bundle.putBoolean("isNeedRefresh",false);
            for(int i = 0; i <isClicks.size();i++){
                isClicks.set(i,false);
            }
            isClicks.set(getLayoutPosition(),true);
            notifyDataSetChanged();
        }

    }

}
