package cn.hwwwwh.lexiangdaxue.ProductClass.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.activity.ProductActivity;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.ProductAdapter;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;
import cn.hwwwwh.lexiangdaxue.ProductClass.presenter.DownloadProductPresenter;
import cn.hwwwwh.lexiangdaxue.ProductClass.view.IProductView;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ProductClass.other.DownloadProduct;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import cn.hwwwwh.lexiangdaxue.other.ArithUtil;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

import static cn.hwwwwh.lexiangdaxue.other.AppConfig.urlPathFruitProduct;

/**
 * Created by 97481 on 2016/11/14.
 */
public class ProductFragment extends BaseFragment implements IProductView {
    public static final String TAG="ProductFragment";
    private String category_Id;
    private RecyclerView recyclerView;
    private String column;
    private DownloadProductPresenter downloadProductPresenter;
    private TextView totalPrice;
    private TextView priceHint;
    private Button settlement;
    private BottomSheetLayout bottomSheetLayout;
    private List<Product> products;
    private ProductAdapter productAdapter;
    private ProductActivity productActivity;
    private ImageView nogoods;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.product_frg);
        recyclerView=getViewById(R.id.frg_lv);
        productActivity= (ProductActivity) getActivity();
        totalPrice=(TextView) productActivity.findViewById(R.id.totalPrice);
        priceHint=(TextView) productActivity.findViewById(R.id.priceHint);
        settlement=(Button) productActivity.findViewById(R.id.settlement);
        bottomSheetLayout=(BottomSheetLayout) productActivity.findViewById(R.id.bottomSheetLayout);
        nogoods=getViewById(R.id.nogoods);
    }

    @Override
    protected void setListener() {
        totalPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(bottomSheetLayout.isSheetShowing()){
                    if(((ProductActivity) getActivity()).getAllSelectCount()==0){
                        for(int i=0;i<products.size();i++){
                            products.get(i).setSelectCount(0);
                        }
                    }
                    else {
                        List<Product> selectList=((ProductActivity) getActivity()).getSelectList();
                        for(int i=0;i<products.size();i++){
                            for (int j=0;j<selectList.size();j++) {
                                if (products.get(i).getName().equals(selectList.get(j).getName())) {
                                    products.get(i).setSelectCount(productActivity.getSelectList().get(j).getSelectCount());
                                }
                            }
                        }
                    }
                    productAdapter.notifyDataSetChanged();
                }

                //更新图标
                double selectValue = productActivity.getSelectValue();
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
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        category_Id=getArguments().getString(TAG);
        column=getArguments().getString("column");
        recyclerView.setLayoutManager(new LinearLayoutManager(productActivity, LinearLayoutManager.VERTICAL, false));
        downloadProductPresenter=new DownloadProductPresenter(this);
        downloadProductPresenter.attachView(getView());

        if(column.equals("零食")){
            downloadProductPresenter.downloadProduct(AppConfig.urlPathProduct+category_Id);
           // new DownloadProduct(view.getContext(),recyclerView,str,getActivity()).execute(urlPath);
        }else if(column.equals("水果")){
            downloadProductPresenter.downloadProduct(AppConfig.urlPathProduct+category_Id);
        }else if(column.equals("早餐")){
            downloadProductPresenter.downloadProduct(AppConfig.urlPathProduct+category_Id);
        }else{
            Toast.makeText(getContext(), "加载失败请重新进入", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void setProductView(List<Product> products) {
        recyclerView.setVisibility(View.VISIBLE);
        nogoods.setVisibility(View.GONE);
        if(productActivity!=null) {
            if(products != null && products.size() != 0){
                    this.products=products;
                    productAdapter = new ProductAdapter(mApp, products, productActivity);
                    recyclerView.setAdapter(productAdapter);
                    final List<Product> selectList = productActivity.getSelectList();
                    for (int i = 0; i < products.size(); i++) {
                        String a = products.get(i).getName();
                        if (selectList.size() != 0) {
                            for (int j = 0; j < selectList.size(); j++) {
                                if (selectList.get(j).getName().equals(a)) {
                                    products.get(i).setSelectCount(selectList.get(j).getSelectCount());
                                }
                            }
                        }
                    }
            }else{
                ToastUtil.show("数据加载失败");
            }
        }
    }

    @Override
    public void setFailView() {
        recyclerView.setVisibility(View.GONE);
        nogoods.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        downloadProductPresenter.detachView();
    }

}
