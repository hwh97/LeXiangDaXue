package cn.hwwwwh.lexiangdaxue.ProductClass.other;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.ProductAdapter;
import cn.hwwwwh.lexiangdaxue.other.ArithUtil;
import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.other.HttpUtils;
import cn.hwwwwh.lexiangdaxue.other.ParserJson;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

/**
 * Created by 97481 on 2016/11/14.
 */
public class DownloadProduct extends AsyncTask<String,Void,List<Product>> {
    private Context context;
    private RecyclerView recyclerView;
    private String categoryName;
    private Activity activity;
    private TextView totalPrice;
    private ProductAdapter productAdapter;
    private String jsonString;
    private TextView priceHint;
    private Button settlement;
    public DownloadProduct(Context context, RecyclerView recyclerView,String categoryName,Activity activity){
        this.context=context;
        this.recyclerView=recyclerView;
        this.categoryName=categoryName;
        this.activity=activity;
    }


    @Override
    protected void onPostExecute(final List<Product> list) {
        super.onPostExecute(list);
        if(list != null && list.size() != 0){
            productAdapter=new ProductAdapter(context,list,activity);
            recyclerView.setAdapter(productAdapter);
            ProductActivity productActivity= (ProductActivity) activity;
            final List<Product> selectList=productActivity.getSelectList();
            totalPrice=(TextView)activity.findViewById(R.id.totalPrice);
            for(int i=0;i<list.size();i++){
                String a=list.get(i).getName();
                if(selectList.size()!=0){
                    for(int j=0;j<selectList.size();j++){
                        if(selectList.get(j).getName().equals(a)){
                            list.get(i).setSelectCount(selectList.get(j).getSelectCount());
                        }
                    }
                }
            }
            priceHint=(TextView)activity.findViewById(R.id.priceHint);
            settlement=(Button)activity.findViewById(R.id.settlement);
            totalPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    BottomSheetLayout bottomSheetLayout = (BottomSheetLayout) activity.findViewById(R.id.bottomSheetLayout);
                    if(bottomSheetLayout.isSheetShowing()){
                        if(((ProductActivity) activity).getAllSelectCount()==0){
                            for(int i=0;i<list.size();i++){
                                list.get(i).setSelectCount(0);
                            }
                        }
                        else {
                            List<Product> selectList=((ProductActivity) activity).getSelectList();
                            for(int i=0;i<list.size();i++){
                                for (int j=0;j<selectList.size();j++) {
                                    if (list.get(i).getName().equals(selectList.get(j).getName())) {
                                        list.get(i).setSelectCount(((ProductActivity) activity).getSelectList().get(j).getSelectCount());
                                    }
                                }
                            }
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                    //更新图标
                    double selectValue = ((ProductActivity) activity).getSelectValue();
                    if (selectValue==0){
                        priceHint.setVisibility(View.VISIBLE);
                        settlement.setVisibility(View.INVISIBLE);
                        priceHint.setText("4元起送");
                    }else if(selectValue<4){
                        priceHint.setVisibility(View.VISIBLE);
                        settlement.setVisibility(View.INVISIBLE);
                        priceHint.setText("还差"+ ArithUtil.sub(4 , selectValue)+"起送");
                    }else{
                        priceHint.setVisibility(View.INVISIBLE);
                        settlement.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }else{
            ToastUtil.show("数据加载失败");
        }
    }

    @Override
    protected  List<Product> doInBackground(String... params) {
        List<Product> list=null;
        String jsonString=null;
        if(HttpUtils.isNetConn(context)){
            byte[] b=HttpUtils.downloadFromNet(params[0]);
            jsonString=new String(b);
            Log.d("Tag",jsonString);
            list= ParserJson.parserJsonToProduct(jsonString,categoryName);
        }
        return list;
    }
}
