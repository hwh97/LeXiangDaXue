package cn.hwwwwh.lexiangdaxue.ProductClass.Product;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.List;


import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.BitmapCache;

/**
 * Created by 97481 on 2016/11/14.
 */
public class Product_Adapter extends BaseAdapter implements TextWatcher {
    private Context context;
    private List<Product> list;
    private LayoutInflater inflater;
    private TextView select_hint;
    private Activity activity;
    private TextView product_price;
    private TextView money_fuhao;
    private Double TotalPrice;
    private Double price;
    private int totalCount;
    private BottomSheetLayout bottomSheetLayout;
    private RequestQueue queue;
    private ImageLoader imageLoader;

    public Product_Adapter(Context context,List<Product> list,Activity activity){
        this.context=context;
        this.list=list;
        this.inflater=LayoutInflater.from(context);
        this.activity=activity;
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new BitmapCache());
    }

    class ViewHolder{
        private NetworkImageView product_pic;
        private TextView product_name;
        private TextView product_price;
        private TextView product_count;
        private ImageButton addButton;
        private ImageButton removeButton;
        private TextView totalPrice;
    }

    public class MyListener implements View.OnClickListener{
        int mPostion;
        private ViewGroup parent;
        private TextView product_count;
        private ImageButton removeButton;
        private View view;
        private int count=0;
        private View view2;
        private TextView totalPrice;

        public MyListener(int mPostion,  ViewGroup parent,View convertView){
            this.mPostion=mPostion;
            this.parent=parent;
            this.view=convertView;
        }

        public void intiview(){
            totalPrice = (TextView) activity.findViewById(R.id.totalPrice);
            select_hint = (TextView) activity.findViewById(R.id.select_hint);
            money_fuhao = (TextView) activity.findViewById(R.id.money);
        }

        @Override
        public void onClick(View v) {
            //这里不能使用getChildAt()，因为getChildAt只对可见列表取view。可用getView解决
            view2 = getView(mPostion, view, parent);
            intiview();
            product_count = (TextView) view2.findViewById(R.id.product_count);
            removeButton = (ImageButton) view2.findViewById(R.id.removeButton);
            product_price = (TextView) view2.findViewById(R.id.product_price);
            count = Integer.parseInt(product_count.getText().toString());
            price = Double.parseDouble(product_price.getText().toString());
            TotalPrice = Double.parseDouble(totalPrice.getText().toString());
            totalCount=Integer.parseInt(select_hint.getText().toString());
            Product product=list.get(mPostion);
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
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Product product=list.get(position);
        final ViewHolder  viewHolder;
        //可以理解为从vlist获取view  之后把view返回给ListView
        //view2=parent.getChildAt(position);
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.product_lv,null);
            viewHolder.product_pic=(NetworkImageView)convertView.findViewById(R.id.product_pic);
            viewHolder.product_name=(TextView)convertView.findViewById(R.id.product_name);
            viewHolder.product_price=(TextView)convertView.findViewById(R.id.product_price);
            viewHolder.product_count=(TextView)convertView.findViewById(R.id.product_count);
            viewHolder.addButton=(ImageButton)convertView.findViewById(R.id.addButton);
            viewHolder.removeButton=(ImageButton)convertView.findViewById(R.id.removeButton);
            viewHolder.totalPrice=(TextView)activity.findViewById(R.id.totalPrice);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //多次加载View问题
        if(parent.getChildCount() == position)
        {
            if(product.getSelectCount()!=0){
                viewHolder.product_count.setVisibility(View.VISIBLE);
                viewHolder.product_count.setText(""+product.getSelectCount());
                viewHolder.removeButton.setVisibility(View.VISIBLE);
            }
            //结束测试
        }
        else
        {
            //这里就是多次加载的问题，可以不用理这里面的 代码，
        }
        String imgUrl = product.getImage_Url();
        if (imgUrl != null && !imgUrl.equals("")) {

            viewHolder.product_pic.setDefaultImageResId(R.mipmap.ic_launcher);
            viewHolder.product_pic.setErrorImageResId(R.mipmap.ic_launcher);
            viewHolder.product_pic.setImageUrl(imgUrl, imageLoader);

        }
        viewHolder.product_name.setText(product.getName());
        viewHolder.product_price.setText(product.getPrice());
        //viewHolder.product_count.setText("0");
        MyListener myListener=new MyListener(position,parent,convertView);
        //给Button添加单击事件  添加Button之后ListView将失去焦点  需要的直接把Button的焦点去掉
        viewHolder.addButton.setOnClickListener(myListener);
        viewHolder.removeButton.setOnClickListener(myListener);
        viewHolder.totalPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bottomSheetLayout = (BottomSheetLayout) activity.findViewById(R.id.bottomSheetLayout);
                if (bottomSheetLayout.isSheetShowing()) {
                    Log.d("isShowing", "bottomSheetLayoutisSheetShowing");
                    //update ListView
                    List<Product> list = ((ProductActivity) activity).getSelectList();
                    for (int i = 0; i < list.size(); i++) {
                        String name = list.get(i).getName();
                        //********************************
                        if (product.getName().equals(name)) {
                            //选择数目为0时隐藏
                            if (list.get(i).getSelectCount() == 0) {
                                viewHolder.product_count.setText(list.get(i).getSelectCount() + "");
                                viewHolder.product_count.setVisibility(View.INVISIBLE);
                                viewHolder.removeButton.setVisibility(View.INVISIBLE);
                            } else {
                                viewHolder.product_count.setText(list.get(i).getSelectCount() + "");
                            }
                        }
                    }
                    if (list.size() == 0) {
                        viewHolder.product_count.setVisibility(View.INVISIBLE);
                        viewHolder.product_count.setText("0");
                        viewHolder.removeButton.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
