package cn.hwwwwh.lexiangdaxue.ProductClass.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.LoginActivity;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.CategoryAdapter;
import cn.hwwwwh.lexiangdaxue.ProductClass.adpter.GwcAdapter;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Category;
import cn.hwwwwh.lexiangdaxue.ProductClass.fragment.ProductFragment;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;

import cn.hwwwwh.lexiangdaxue.ProductClass.presenter.DownloadActivityPresenter;
import cn.hwwwwh.lexiangdaxue.ProductClass.view.IActivityView;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.activity.SettlementActivity;
import cn.hwwwwh.lexiangdaxue.other.ArithUtil;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;
import cn.hwwwwh.lexiangdaxue.other.AppConfig;
import me.majiajie.swipeback.SwipeBackActivity;

/**
 * Created by 97481 on 2016/11/13.
 */
public class ProductActivity extends BaseActivity implements View.OnClickListener,IActivityView{
    private RecyclerView rv_Category;
    private ProductFragment productFragment;
    private List<Product> selectList;
    private View bottomSheet;
    private TextView clearAll;
    private BottomSheetLayout bottomSheetLayout;
    private GwcAdapter gwcAdapter;
    private TextView clear;
    private TextView totalPrice;
    private TextView totalCount;
    private TextView money;
    private TextView notification;
    private Button settlement;
    private String column;
    private TextView product_title;
    private Toolbar toolbar;
    private DownloadActivityPresenter downloadActivityPresenter;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product);
        rv_Category = getViewById(R.id.category);
        totalPrice=getViewById(R.id.totalPrice);
        bottomSheetLayout=getViewById(R.id.bottomSheetLayout);
        totalPrice=getViewById(R.id.totalPrice);
        totalCount=getViewById(R.id.select_hint);
        money=getViewById(R.id.money);
        notification=getViewById(R.id.notification);
        settlement=getViewById(R.id.settlement);
        product_title=getViewById(R.id.product_title);
        toolbar=getViewById(R.id.toolbar_product);
        session=new SessionManager(this);
        // SqLite database handler
        db = new SQLiteHandler(this);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    protected void setListener() {
        settlement.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        downloadActivityPresenter=new DownloadActivityPresenter(this);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        notification.setHorizontallyScrolling(true);
        notification.setMovementMethod(ScrollingMovementMethod.getInstance());
        rv_Category.setLayoutManager(new LinearLayoutManager(this));
        Intent intent=this.getIntent();
        column=intent.getExtras().getString("column");
        if(column.equals("零食")){
            /*new DownloadCategory(ProductActivity.this, rv_Category).execute(urlPath);
            new DownloadGongGao(this,ProductActivity.this).execute(urlPathGongGao);*/
            downloadActivityPresenter.downloadCategory(AppConfig.urlPathCategory);
            downloadActivityPresenter.downloadGonggao(AppConfig.urlPathGongGao);
            product_title.setText("零食店");
        }else if(column.equals("水果")){
            downloadActivityPresenter.downloadCategory(AppConfig.urlPathFruitCategory);
            downloadActivityPresenter.downloadGonggao(AppConfig.urlPathGongGaoFruit);
            product_title.setText("水果店");
        }else if(column.equals("早餐")){
            downloadActivityPresenter.downloadCategory(AppConfig.urlPathBkfCategory);
            downloadActivityPresenter.downloadGonggao(AppConfig.urlPathGongGaoBkf);
            product_title.setText("早餐店");
        }
        initProduct();
    }

    public void initProduct(){
        selectList=new ArrayList<>();
        productFragment = new ProductFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, productFragment);
        //通过bundle传值给MyFragment
        Bundle bundle = new Bundle();
        bundle.putString(ProductFragment.TAG,"全部分类");
        bundle.putString("column",column);
        productFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.money: case R.id.totalPrice: case R.id.shuxian: case R.id.select_hint: case R.id.select_text2: case R.id.select_text:
                showBottomSheet();
                break;
            //清空购物车
            case R.id.clear:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("提示");
                builder.setMessage("您确定要清空购物车么？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeAllSelect(getSelectList());
                        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomSheetLayout);
                        totalPrice.setText("0");
                        totalCount.setText("0");
                        totalPrice.setTextColor(getResources().getColor(R.color.darkgray));
                        money.setTextColor(getResources().getColor(R.color.darkgray));
                        bottomSheetLayout.dismissSheet();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.settlement:
                if(session.isLoggedIn()){
                    Intent intent=new Intent(ProductActivity.this,SettlementActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("product", (Serializable) getSelectList());
                    bundle.putString("AllValue",getSelectValue()+"");
                    bundle.putString("column",column);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    logoutUser();
                }

                break;
        }
    }

    private View createBottomSheetView(){
        View view= LayoutInflater.from(this).inflate(R.layout.layout_botton_sheet,(ViewGroup) getWindow().getDecorView(),false);
        RecyclerView rvSelected=(RecyclerView)view.findViewById(R.id.selectRecyclerView);
        rvSelected.setLayoutManager(new LinearLayoutManager(this));
        clearAll = (TextView) view.findViewById(R.id.clear);
        clearAll.setOnClickListener(this);
        List<Product> list=getSelectList();
        gwcAdapter=new GwcAdapter(this,list);
        rvSelected.setAdapter(gwcAdapter);
        return view;
    }

    private void showBottomSheet(){
        bottomSheet= createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else{
            if(getSelectList().size()!=0){
                bottomSheetLayout.showWithSheetView(bottomSheet);
                clear=(TextView)findViewById(R.id.clear);
                clear.setOnClickListener(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("SettlementData", Context.MODE_PRIVATE).edit();
        editor.putBoolean("isFirstLoad", true);
        editor.putBoolean("isToday", true);
        editor.putString("time", "尽快送达");
        editor.putString("payWay","在线支付");
        editor.putString("note","");
        editor.commit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public int getAllSelectCount(){
        int AllCount=0;
        for(int i=0;i<getSelectList().size();i++){
            AllCount=AllCount+getSelectList().get(i).getSelectCount();
        }
        return AllCount;
    }

    public double getSelectValue(){
        List<Product> list=getSelectList();
        double price=0;
        for(int i=0;i<list.size();i++){
            list.get(i).getSelectCount();
            price= ArithUtil.add(ArithUtil.mul(Double.parseDouble(list.get(i).getPrice()), list.get(i).getSelectCount()), price);
        }
        return price;
    }
    //选择List添加
    public void add(Product product){
        for(int i=0;i<selectList.size();i++){
            //已经存在，更新count
            if(product.getName().equals(selectList.get(i).getName())){
                selectList.get(i).setSelectCount(product.getSelectCount());
                return;
            }
        }
        selectList.add(product);
    }
    //选择List移除
    public void remove(String name){
        //先用name 等每个商品有自己的id再修改。
        for(int i=0;i<getSelectList().size();i++){
            if(getSelectList().get(i).getName().equals(name)){
                getSelectList().remove(i);
            }
        }
    }

    //选择List移除
    public void remove(String name,boolean isRefresh){
        //先用name 等每个商品有自己的id再修改。
        for(int i=0;i<getSelectList().size();i++){
            if(getSelectList().get(i).getName().equals(name)){
                getSelectList().remove(i);
            }
        }
        if(isRefresh){
            gwcAdapter.notifyDataSetChanged();
        }
    }

    public void removeAllSelect(List<Product> list){
        list.clear();
        gwcAdapter.notifyDataSetChanged();
    }

    public String getColumn() {
        return column;
    }

    public List<Product> getSelectList() {
        return selectList;
    }

    @Override
    public void setCateGoryView(List<Category> categories) {
        if(categories != null && categories.size()!=0){
            //更新rv
            CategoryAdapter adapterCategory = new CategoryAdapter(ProductActivity.this,categories);
            rv_Category.setAdapter(adapterCategory);
        }else{
            ToastUtil.show("数据加载失败");
        }
    }

    @Override
    public void setGonggaoView(String Gonggao) {
        if(Gonggao != null){
            notification.setText(Gonggao);
        }
        else{
            notification.setText("");
            ToastUtil.show("数据加载失败");
        }
    }
}
