package cn.hwwwwh.lexiangdaxue.ProductClass.adpter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.R;

/**
 * Created by 97481 on 2016/11/20.
 */
public class GwcAdapter extends RecyclerView.Adapter<GwcAdapter.ViewHolder> implements View.OnClickListener{
    private ProductActivity product_activity;
    private List<Product> list;
    private LayoutInflater inflater;
    private View view;
    private TextView totalPrice;
    private TextView totalCount;
    private BottomSheetLayout bottomSheetLayout;
    private TextView money;

    public GwcAdapter(ProductActivity activity, List<Product> list){
        this.product_activity= activity;
        this.list=list;
        inflater=LayoutInflater.from(product_activity);
        totalPrice=(TextView)product_activity.findViewById(R.id.totalPrice);
        totalCount=(TextView)product_activity.findViewById(R.id.select_hint);
        money=(TextView)product_activity.findViewById(R.id.money);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view=inflater.inflate(R.layout.item_selected_goods,parent,false);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvCost,tvCount,tvName;
        private ImageButton tvAdd,tvMinus;
        private Product product;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            tvMinus = (ImageButton) itemView.findViewById(R.id.tvMinus);
            tvAdd = (ImageButton) itemView.findViewById(R.id.tvAdd);
            tvMinus.setOnClickListener(this);
            tvAdd.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int count=product.getSelectCount();
            List<Product> list=product_activity.getSelectList();
            switch (v.getId()){
                case R.id.tvAdd:
                    if (count < 10 && count >= 0) {
                        int newCount = product.getSelectCount() + 1;
                        tvCount.setText(newCount + "");
                        product.setSelectCount(newCount);
                        product_activity.add(product);
                        totalCount.setText(product_activity.getAllSelectCount() + "");
                        totalPrice.setText(product_activity.getSelectValue() + "");
                    }
                    else{
                        Toast.makeText(product_activity, "超过最大数量10", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.tvMinus:
                    //只选择了一样商品
                    if(list.size()==1){
                        if(count==1){
                            count=count-1;
                            tvCount.setText(count + "");
                            product.setSelectCount(count);
                            totalCount.setText(product_activity.getAllSelectCount() + "");
                            totalPrice.setText(product_activity.getSelectValue() + "");
                            //remove放在后面以便先之心onTextChange()
                            product_activity.remove(product.getName());
                            bottomSheetLayout=(BottomSheetLayout)product_activity.findViewById(R.id.bottomSheetLayout);
                            bottomSheetLayout.dismissSheet();
                        }
                        else if(count<=10){
                            count=count-1;
                            tvCount.setText(count + "");
                            product.setSelectCount(count);
                            product_activity.add(product);
                            totalCount.setText(product_activity.getAllSelectCount() + "");
                            totalPrice.setText(product_activity.getSelectValue() + "");
                        }

                    }
                    //选择多个商品
                    else{
                        //需要更新
                        if(product.getSelectCount()==1){
                            count=count-1;
                            tvCount.setText(count + "");
                            product.setSelectCount(count);
                            totalCount.setText(product_activity.getAllSelectCount() + "");
                            totalPrice.setText(product_activity.getSelectValue() + "");
                            //remove放在后面以便先执行onTextChange()
                            product_activity.remove(product.getName(),true);
                        }
                        //不需要更新
                        else{
                            count=count-1;
                            tvCount.setText(count + "");
                            product.setSelectCount(count);
                            product_activity.add(product);
                            totalCount.setText(product_activity.getAllSelectCount() + "");
                            totalPrice.setText(product_activity.getSelectValue() + "");
                        }
                    }
                    //Text color
                    double TotalPrice = product_activity.getSelectValue();
                    if(TotalPrice==0){
                        totalPrice.setTextColor(product_activity.getResources().getColor(R.color.darkgray));
                        money.setTextColor(product_activity.getResources().getColor(R.color.darkgray));
                    }
                    break;
            }
        }

        public void bindDate(Product product,int position){
            this.product=product;
            tvName.setText(product.getName());
            tvCost.setText(product.getPrice());
            tvCount.setText(String.valueOf(product.getSelectCount()));
        }
    }
}
