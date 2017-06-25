package cn.hwwwwh.lexiangdaxue.ProductClass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import cn.hwwwwh.lexiangdaxue.ProductClass.other.DownloadProduct;
import cn.hwwwwh.lexiangdaxue.R;


/**
 * Created by 97481 on 2016/11/14.
 */
public class ProductFragment extends Fragment implements AdapterView.OnItemClickListener{
    public static final String TAG="ProductFragment";
    private String str;
    private String urlPathFruit = "http://cs.hwwwwh.cn/test_product_fruit.json";
    private String urlPathBkf = "http://cs.hwwwwh.cn/test_product_bkf.json";
    private String urlPath = "http://cs.hwwwwh.cn/test_product.json";
    private RecyclerView recyclerView;
    private View view;
    private String column;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.product_frg, null);
        str=getArguments().getString(TAG);
        column=getArguments().getString("column");
        recyclerView=(RecyclerView)view.findViewById(R.id.frg_lv);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));
        if(column.equals("零食")){
            new DownloadProduct(view.getContext(),recyclerView,str,getActivity()).execute(urlPath);
        }else if(column.equals("水果")){
            new DownloadProduct(view.getContext(),recyclerView,str,getActivity()).execute(urlPathFruit);
        }else if(column.equals("早餐")){
            new DownloadProduct(view.getContext(),recyclerView,str,getActivity()).execute(urlPathBkf);
        }else{
            Toast.makeText(view.getContext(), "加载失败请重新进入", Toast.LENGTH_SHORT).show();
        }
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //view2=(View)parent.getChildAt(position);
       // addButton=(ImageButton)view2.findViewById(R.id.addButton);
        //addButton.setOnClickListener(this);
    }
}
