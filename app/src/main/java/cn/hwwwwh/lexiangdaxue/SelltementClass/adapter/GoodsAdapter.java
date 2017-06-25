package cn.hwwwwh.lexiangdaxue.SelltementClass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.ArithUtil;

/**
 * Created by 97481 on 2016/12/2.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ViewHolder> {
    private List<Product> list=null;
    private Context context;
    private LayoutInflater inflater;
    private View view;

    public GoodsAdapter(Context context,List<Product> list){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.pay_recycleview,parent,false);
        Log.d("test3",list.size()+"");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product=list.get(position);
        holder.bindData(product);
    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView goodName;
        private TextView pay_count;
        private TextView pay_price;

        public ViewHolder(View itemView) {
            super(itemView);
            goodName=(TextView)itemView.findViewById(R.id.goodName);
            pay_count=(TextView)itemView.findViewById(R.id.pay_count);
            pay_price=(TextView)itemView.findViewById(R.id.pay_price);
        }

        public void bindData(Product product){
            goodName.setText(product.getName());
            pay_count.setText("×" + product.getSelectCount());
            double a=Double.parseDouble(product.getPrice());
            double value= ArithUtil.mul(a,product.getSelectCount());
            pay_price.setText("￥"+value);
        }
    }
}
