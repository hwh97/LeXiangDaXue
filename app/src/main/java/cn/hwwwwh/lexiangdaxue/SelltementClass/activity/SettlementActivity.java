package cn.hwwwwh.lexiangdaxue.SelltementClass.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.Activity.AddressActivity;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.AddressBean;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter.DownloadUserAddressPresenter;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.presenter.IDownloadUserAddressPresenter;
import cn.hwwwwh.lexiangdaxue.FgFourthClass.view.IAddressView;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.fragment.DialogTimeChoose;
import cn.hwwwwh.lexiangdaxue.SelltementClass.adapter.GoodsAdapter;
import cn.hwwwwh.lexiangdaxue.SelltementClass.fragment.SettlementFragment;
import cn.hwwwwh.lexiangdaxue.SelltementClass.other.MyBottomSheetDialog;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.RxBus;
import me.majiajie.swipeback.SwipeBackActivity;
import rx.Subscription;
import rx.functions.Action1;

public class SettlementActivity extends BaseActivity implements View.OnClickListener,IAddressView {

    private RecyclerView recyclerView;
    private TextView allValue;
    private Toolbar toolbar;
    private View bottomSheet;
    private BottomSheetLayout bottomSheetLayout;
    private View getTime;
    private TextView selectTime;
    private View pay_way;
    private TextView pay_method;
    private View preferential_view;
    private String column;
    private TextView column_settlement;
    private View note_view;
    private TextView note;
    private DownloadUserAddressPresenter downloadUserAddressPresenter;
    private SessionManager session;
    private SQLiteHandler db;
    private RelativeLayout address_view;
    private LinearLayout address_view2;
    private TextView address_name;
    private TextView address_phone;
    private TextView address_da;
    public AddressBean addressBean;
    private Subscription rxSbscription;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settlement);
        toolbar = getViewById(R.id.toolbar_settlement);
        recyclerView = getViewById(R.id.select_RV);
        allValue =getViewById(R.id.allValue);
        //bottomSheetLayout=(BottomSheetLayout)findViewById(R.id.timechoose);
        getTime=getViewById(R.id.getTime_View);
        pay_way=getViewById(R.id.pay_way);
        preferential_view=getViewById(R.id.preferential_view);
        selectTime=getViewById(R.id.selectTime);
        pay_method=getViewById(R.id.pay_method);
        column_settlement=getViewById(R.id.column_settlement);
        note_view=getViewById(R.id.note_view);
        note=getViewById(R.id.note);
        //解决scrollView不能自动定位在顶部
        final ScrollView scrollView=getViewById(R.id.settlement_Sl);
        scrollView.smoothScrollTo(0,20);
        session=new SessionManager(this);
        // SqLite database handler
        db = new SQLiteHandler(this);
        address_view=getViewById(R.id.address_view);
        address_view2=getViewById(R.id.address_view2);
        address_name=getViewById(R.id.address_name);
        address_da=getViewById(R.id.address_da);
        address_phone=getViewById(R.id.address_phone);
    }

    @Override
    protected void setListener() {
        getTime.setOnClickListener(this);
        note_view.setOnClickListener(this);
        preferential_view.setOnClickListener(this);
        pay_way.setOnClickListener(this);
        address_view2.setOnClickListener(this);
        address_view.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        RefreshUi();
        refreshPayWay();
        refreshNote();
        final List<Product> list=(List<Product>)this.getIntent().getSerializableExtra("product");
        String AllValue=this.getIntent().getStringExtra("AllValue");
        toolbar.setTitle("结算");
        toolbar.setTitleTextAppearance(this, R.style.TitleText);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //importance
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GoodsAdapter goodsAdapter=new GoodsAdapter(SettlementActivity.this,list);
        recyclerView.setAdapter(goodsAdapter);
        allValue.setText("￥" + AllValue);
        column=this.getIntent().getStringExtra("column");
        column_settlement.setText(column + "店");
        downloadUserAddressPresenter=new DownloadUserAddressPresenter(this);
        downloadUserAddressPresenter.download("http://cs.hwwwwh.cn/admin/UserAddressApi.php?uid="+db.getUserDetails().get("uid"));
        rxSbscription = RxBus.getInstance().toObserverable(AddressBean.class)
                .subscribe(new Action1<AddressBean>() {
                    @Override
                    public void call(AddressBean addressBean) {
                        initAddress(addressBean);
                    }
                });
    }

    public void initAddress(AddressBean addressBean){
        this.addressBean=addressBean;
        address_view2.setVisibility(View.VISIBLE);
        address_view.setVisibility(View.GONE);
        address_name.setText(addressBean.getUa_name());
        address_da.setText(db.getUniDetails().get("uu_city")+db.getUniDetails().get("uu_name")+addressBean.getUa_detailAddress());
        address_phone.setText(addressBean.getUa_phoneNum());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.getTime_View:
                DialogTimeChoose fragment = DialogTimeChoose.newInstance();
                fragment.show(getSupportFragmentManager(),DialogTimeChoose.class.getSimpleName());
                break;
            case R.id.pay_way:
                    openBottomSheet();
                break;
            case R.id.preferential_view:
                Toast.makeText(this,"暂不开放优惠码功能",Toast.LENGTH_SHORT).show();
                break;
            case R.id.note_view:
                Intent intent=new Intent(SettlementActivity.this,NoteActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.address_view:case R.id.address_view2:
                Intent intent2=new Intent(SettlementActivity.this,AddressActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case RESULT_OK:
                if(requestCode==1) {
                    refreshNote();
                }
                break;

        }
    }

    private void openBottomSheet(){
        final MyBottomSheetDialog dialog=new MyBottomSheetDialog(this);
        View view=getLayoutInflater().inflate(R.layout.settlement_payway, null);
        dialog.setContentView(view);
        dialog.show();
        final SharedPreferences.Editor editor=getSharedPreferences("SettlementData",Context.MODE_PRIVATE).edit();
        final TextView onlinePay=(TextView)view.findViewById(R.id.onlinePay);
        onlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("payWay",onlinePay.getText().toString() );
                editor.commit();
                refreshPayWay();
                dialog.dismiss();
            }
        });
        final TextView facePay=(TextView)view.findViewById(R.id.facePay);
        facePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("payWay", facePay.getText().toString());
                editor.commit();
                refreshPayWay();
                dialog.dismiss();
            }
        });
    }

    //更新时间选择界面
    public void RefreshUi(){
        SharedPreferences pref=getApplicationContext().getSharedPreferences("SettlementData",Context.MODE_PRIVATE);
        Boolean isToday=pref.getBoolean("isToday", true);
        String time=pref.getString("time","尽快送达");
        if(!time.equals("尽快送达")){
            if(isToday){
                selectTime.setText("今天"+" "+time);
            }else{
                selectTime.setText("明天"+" "+time);
            }
        }else{
            selectTime.setText("尽快送达");
        }

    }

    //更新支付方式界面
    public void refreshPayWay(){
        SharedPreferences pref=getSharedPreferences("SettlementData", MODE_PRIVATE);
        String payWay=pref.getString("payWay", "在线支付");
        pay_method.setText(payWay);
    }

    public void refreshNote(){
        SharedPreferences pref=getSharedPreferences("SettlementData",MODE_PRIVATE);
        String noteContent=pref.getString("note","");
        if(noteContent.length()!=0)
            note.setText(noteContent);
        else
            note.setText("输入备注");
    }


    @Override
    public void setAddressView(ArrayList<AddressBean> list) {
        this.addressBean=list.get(0);
        initAddress(addressBean);
    }

    @Override
    public void setFail() {
        address_view2.setVisibility(View.GONE);
        address_view.setVisibility(View.VISIBLE);
    }

}
