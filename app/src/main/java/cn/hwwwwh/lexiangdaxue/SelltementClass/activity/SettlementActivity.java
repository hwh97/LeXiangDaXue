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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.ProductClass.bean.Product;

import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.SelltementClass.fragment.DialogTimeChoose;
import cn.hwwwwh.lexiangdaxue.SelltementClass.adapter.GoodsAdapter;
import cn.hwwwwh.lexiangdaxue.SelltementClass.fragment.SettlementFragment;
import me.majiajie.swipeback.SwipeBackActivity;

public class SettlementActivity extends SwipeBackActivity  implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        initView();
        List<Product> list=(List<Product>)this.getIntent().getSerializableExtra("product");
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
    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_settlement);
        recyclerView = (RecyclerView) findViewById(R.id.select_RV);
        allValue = (TextView) findViewById(R.id.allValue);
        //bottomSheetLayout=(BottomSheetLayout)findViewById(R.id.timechoose);
        getTime=(RelativeLayout)findViewById(R.id.getTime_View);
        getTime.setOnClickListener(this);
        pay_way=(RelativeLayout)findViewById(R.id.pay_way);
        pay_way.setOnClickListener(this);
        preferential_view=(RelativeLayout)findViewById(R.id.preferential_view);
        preferential_view.setOnClickListener(this);
        selectTime=(TextView)findViewById(R.id.selectTime);
        pay_method=(TextView)findViewById(R.id.pay_method);
        column_settlement=(TextView)findViewById(R.id.column_settlement);
        note_view=findViewById(R.id.note_view);
        note_view.setOnClickListener(this);
        note=(TextView)findViewById(R.id.note);
        //解决scrollView不能自动定位在顶部
        final ScrollView scrollView=(ScrollView)findViewById(R.id.settlement_Sl);
        scrollView.smoothScrollTo(0,20);
        RefreshUi();
        refreshPayWay();
        refreshNote();
    }


    private void showBottomSheet(){
        bottomSheet= createBottomSheetView();
        if(bottomSheetLayout.isSheetShowing()){
            bottomSheetLayout.dismissSheet();
        }else{
            bottomSheetLayout.showWithSheetView(bottomSheet);
        }
    }

    public View createBottomSheetView(){
        View view= LayoutInflater.from(this).inflate(R.layout.settlement_buttonsheet,(ViewGroup)getWindow().getDecorView(),false);
        SettlementFragment settlementFragment=new SettlementFragment();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.settlement_container,settlementFragment);
        fragmentTransaction.commit();
        return view;
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
        final BottomSheetDialog dialog=new BottomSheetDialog(this);
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

}
