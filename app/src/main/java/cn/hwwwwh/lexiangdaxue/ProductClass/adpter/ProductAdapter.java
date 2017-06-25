package cn.hwwwwh.lexiangdaxue.ProductClass.adpter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.BitmapCache;

/**
 * Created by 97481 on 2016/11/27.
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<Product> list;
    private LayoutInflater inflater;
    private TextView select_hint;
    private Activity activity;
    private TextView money_fuhao;
    private Double TotalPrice;
    private Double price;
    private int totalCount;
    private BottomSheetLayout bottomSheetLayout;
    private RequestQueue queue;
    private ImageLoader imageLoader;
    private View view;

    public ProductAdapter(Context context,List<Product> list,Activity activity){
        this.context=context;
        this.list=list;
        this.inflater= LayoutInflater.from(context);
        this.activity=activity;
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new BitmapCache());
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.product_lv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product=list.get(position);
        holder.bindDate(product,position);
    }

    @Override
    public int getItemCount() {
        if(list==null) {
            return 0;
        }else{
            return  list.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView product_pic;
        private TextView product_name;
        private TextView product_price;
        private TextView product_count;
        private ImageButton addButton;
        private ImageButton removeButton;
        private TextView totalPrice;
        private int count=0;

        public ViewHolder(View itemView) {
            super(itemView);
            intiview();
            product_pic=(ImageView)itemView.findViewById(R.id.product_pic);
            product_name=(TextView)itemView.findViewById(R.id.product_name);
            product_price=(TextView)itemView.findViewById(R.id.product_price);
            product_count=(TextView)itemView.findViewById(R.id.product_count);
            addButton=(ImageButton)itemView.findViewById(R.id.addButton);
            removeButton=(ImageButton)itemView.findViewById(R.id.removeButton);
            totalPrice=(TextView)activity.findViewById(R.id.totalPrice);
            addButton.setOnClickListener(this);
            removeButton.setOnClickListener(this);
        }

        public void bindDate(Product product,int positon) {
            product_name.setText(product.getName());
            product_price.setText(product.getPrice());
            String imgUrl = product.getImage_Url();
            if (imgUrl != null && !imgUrl.equals("")) {
                Glide.with(view.getContext()).load(imgUrl).placeholder(R.drawable.loadpic).into(product_pic);
            }
            if(product.getSelectCount()!=0){
                product_count.setVisibility(View.VISIBLE);
                product_count.setText("" + product.getSelectCount());
                removeButton.setVisibility(View.VISIBLE);
            }
            else{
                product_count.setVisibility(View.INVISIBLE);
                product_count.setText("" + product.getSelectCount());
                removeButton.setVisibility(View.INVISIBLE);
            }
        }
        public void intiview(){
            totalPrice = (TextView) activity.findViewById(R.id.totalPrice);
            select_hint = (TextView) activity.findViewById(R.id.select_hint);
            money_fuhao = (TextView) activity.findViewById(R.id.money);
        }
        @Override
        public void onClick(View v) {
            count = Integer.parseInt(product_count.getText().toString());
            price = Double.parseDouble(product_price.getText().toString());
            TotalPrice = Double.parseDouble(totalPrice.getText().toString());
            totalCount=Integer.parseInt(select_hint.getText().toString());
            Product product=list.get(getAdapterPosition());
            switch (v.getId()) {
                //添加按钮点击事件
                case R.id.addButton:
                    removeButton.setVisibility(View.VISIBLE);
                    if (count < 10 && count >= 0) {
                        product_count.setVisibility(View.VISIBLE);
                        count = count + 1;
                        product_count.setText(count + "");
                        totalPrice.setTextColor(view.getResources().getColor(R.color.darkorange));
                        money_fuhao.setTextColor(view.getResources().getColor(R.color.darkorange));
                        //通过Activity的方法添加信息
                        ProductActivity productActivity= (ProductActivity) activity;
                        product.setSelectCount(count);
                        productActivity.add(product);
                        //ArithUtil为了解决精度问题
                        select_hint.setText(((ProductActivity)activity).getAllSelectCount()+"");
                        TotalPrice = ((ProductActivity) activity).getSelectValue();
                        totalPrice.setText(TotalPrice + "");
                        Toast.makeText(context, product_count.getText()+" ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "超过最大数量10", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //减去按钮点击事件
                case R.id.removeButton:
                    //通过Activity的方法添加信息
                    ProductActivity productActivity= (ProductActivity) activity;
                    if (count == 1) {
                        removeButton.setVisibility(View.INVISIBLE);
                        product_count.setVisibility(View.INVISIBLE);
                        count = count - 1;
                        product_count.setText(count + "");
                        product.setSelectCount(count);
                        productActivity.remove(product.getName());
                    } else if (count <= 10) {
                        count = count - 1;
                        product_count.setText(count + "");
                        product.setSelectCount(count);
                        productActivity.add(product);
                    }
                    //ArithUtil为了解决精度问题
                    totalPrice.setText(((ProductActivity)activity).getSelectValue() + "");
                    select_hint.setText(((ProductActivity)activity).getAllSelectCount() + "");
                    TotalPrice=((ProductActivity)activity).getSelectValue();
                    if(TotalPrice==0){
                        totalPrice.setTextColor(view.getResources().getColor(R.color.darkgray));
                        money_fuhao.setTextColor(view.getResources().getColor(R.color.darkgray));
                    }
                    break;
            }
        }
    }

}