package cn.hwwwwh.lexiangdaxue.ShoppingClass.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ShoppingClass.bean.QuanGoods;
import cn.hwwwwh.lexiangdaxue.WebViewActivity;
import cn.hwwwwh.lexiangdaxue.R;

/**
 * Created by 97481 on 2017/1/29.
 */

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder>{

    public View view;
    public Context context;
    public List<QuanGoods> list;
    public LayoutInflater inflater;
    public RequestQueue queue;
    public ImageLoader imageLoader;

    public ShoppingAdapter(Context context){
        this.context=context;
        this.inflater= LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.fg_shopping_rv_detail,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuanGoods quanGoods=list.get(position);
        holder.bindData(quanGoods,position);
    }


    public void addMoreData(List<QuanGoods> data){
        if(list!=null){
            list.addAll(list.size(), data);
            notifyDataSetChanged();
        }
    }

    /**
     * 设置全新的数据集合，如果传入null，则清空数据列表（第一次从服务器加载数据，或者下拉刷新当前界面数据表）
     *
     * @param data
     */
    public void setData(List<QuanGoods> data) {
        if (data != null) {
            list = data;
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(list==null) {
            return 0;
        }else{
            return  list.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView shopping_pic;
        private TextView D_title;
        private TextView Quan_price;
        private TextView Sales_num;
        private TextView Org_Price;
        private TextView Price;
        private ImageView biaozhi;


        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        public void bindData(QuanGoods quanGoods,int positon){
            String imageUrl=quanGoods.getPic();
            if(imageUrl!=null&&!imageUrl.equals("")){
               //Picasso.with(view.getContext()).load(imageUrl).placeholder(R.drawable.loadding).into(shopping_pic);
                Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.loadpic).into(shopping_pic);
            }
            D_title.setText(quanGoods.getD_title());
            Quan_price.setText(quanGoods.getQuan_price()+"元购物券");
            Sales_num.setText("月销 "+quanGoods.getSales_num());
            Org_Price.setText("￥"+quanGoods.getOrg_Price());
            Price.setText("￥"+quanGoods.getPrice());
            Org_Price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
            if(quanGoods.isTmall()){
                biaozhi.setImageResource(R.drawable.tmall);
            }else{
                biaozhi.setImageResource(R.drawable.taobao);
            }
        }

        public void initView(View itemView){
            shopping_pic=(ImageView) itemView.findViewById(R.id.shopping_pic);
            D_title=(TextView)itemView.findViewById(R.id.D_title);
            Quan_price=(TextView)itemView.findViewById(R.id.Quan_price);
            Sales_num=(TextView)itemView.findViewById(R.id.Sales_num);
            Org_Price=(TextView)itemView.findViewById(R.id.Org_Price);
            Price=(TextView)itemView.findViewById(R.id.Price);
            biaozhi=(ImageView)itemView.findViewById(R.id.biaozhi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, WebViewActivity.class);
                    Bundle bundle=new Bundle();
                    QuanGoods quanGoods=list.get(getAdapterPosition());
                    bundle.putString("Quan_price",quanGoods.getQuan_price());
                    bundle.putString("Quan_time",quanGoods.getQuan_time());
                    bundle.putString("Quan_surplus",quanGoods.getQuan_surplus());
                    bundle.putString("Quan_link",quanGoods.getQuan_link());
                    bundle.putString("Ali_click",quanGoods.getAli_click());
                    bundle.putString("GoodsId",quanGoods.getGoodsId());
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

    }
}
