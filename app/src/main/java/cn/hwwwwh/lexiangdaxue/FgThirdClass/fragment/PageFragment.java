package cn.hwwwwh.lexiangdaxue.FgThirdClass.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgThirdClass.activity.OrderDetailActivity;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.adapter.orderDataAdapter;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.bean.Order;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.presenter.IDownloadOrderPre;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.view.IDownloadOrderView;
import cn.hwwwwh.lexiangdaxue.MainActivity;
import cn.hwwwwh.lexiangdaxue.R;
import cn.hwwwwh.lexiangdaxue.ShoppingClass.other.SpacesItemDecoration;
import cn.hwwwwh.lexiangdaxue.other.BaseFragment;
import cn.hwwwwh.lexiangdaxue.other.BaseRecyclerAdapter;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;

/**
 * Created by Administrator on 2017/10/12.
 */

public class PageFragment extends BaseFragment implements IDownloadOrderView {

    private IDownloadOrderPre iDownloadOrderPre;
    private SmartRefreshLayout smartRefreshLayout;
    private boolean isLazyLoad=false;
    private RecyclerView recyclerView;
    private int page=1;
    private int method=-1;
    private orderDataAdapter orderDataAdapter;

    public static  PageFragment newInstance(int method){
        Bundle args=new Bundle();
        args.putInt("method",method);
        PageFragment pageFragment=new PageFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fg3_page);
        smartRefreshLayout = getViewById(R.id.refreshLayout);
        recyclerView=getViewById(R.id.recyclerView);
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //$method=0表示待支付，=1表示进行中，=2表示退款售后。
        if(getArguments().getInt("method")!=0){
            method=getArguments().getInt("method")-1;
        }
        //设置 Header 为 Material风格
        smartRefreshLayout.setRefreshHeader(new WaterDropHeader(mApp));
        //refreshLayout.setRefreshHeader(new FunGameHitBlockHeader(this));
        //refreshLayout.setPrimaryColors(Color.CYAN);
        //设置 Footer 为 球脉冲
        smartRefreshLayout.setRefreshFooter(new BallPulseFooter(mApp).setSpinnerStyle(SpinnerStyle.Scale));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
                page=1;
                iDownloadOrderPre.download(getUserDetails().get("token"),getPhoneId(),page,method);
            }
        });
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
                page=page+1;
                iDownloadOrderPre.download(getUserDetails().get("token"),getPhoneId(),page,method);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderDataAdapter=new orderDataAdapter();
        RecyclerView.ItemDecoration itemDecoration=new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom=30;
            }
        };
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(orderDataAdapter);

        orderDataAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                Intent intent=new Intent(getContext(), OrderDetailActivity.class);
                intent.putExtra("orderSn",((Order.OrderDataBean)data).getOrder_sn());
                startActivity(intent);

            }
        });

    }

    @Override
    protected void lazyLoad() {
        //下载全部
        if(!isLazyLoad) {
            isLazyLoad = true;
            iDownloadOrderPre = new IDownloadOrderPre(this, (MainActivity) getActivity());
            iDownloadOrderPre.download(getUserDetails().get("token"),getPhoneId(),page,method);
        }
    }

    @Override
    public void initOrder(List<Order.OrderDataBean> order) {
        if(page==1)
            orderDataAdapter.refreshDatas();
        orderDataAdapter.addDatas(order);
    }

    @Override
    public void initOrderFail(String msg) {
        if(page==1) {
            List<Order.OrderDataBean> order=new ArrayList<>();
            orderDataAdapter.refreshDatas();
            orderDataAdapter.addDatas(order);
        }
        ToastUtil.show(msg);
    }

}
