package cn.hwwwwh.lexiangdaxue;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.hwwwwh.lexiangdaxue.other.BaseActivity;

public class WebViewActivity extends BaseActivity {

    private String Quan_price;
    private String Quan_time;
    private String Quan_surplus;
    private String GoodsId;
    private Toolbar toolbar_wb;
    private TextView mQuan_time;
    private TextView mQuan_surplus;
    private Button button_wb;
    private WebView webView;
    private static String Ali_click;
    private static String Quan_link;
    private RelativeLayout mDetail;
    private ProgressBar progressBar;
    private Button refresh_wb;
    private Button close_view;
    private RelativeLayout bottom_view;

    //清除tmall页面广告
    private static String noAdTmall="javascript:function setTop(){if(document.querySelector('#detail-base-smart-banner').style.display !='none' || document.querySelector('.app-download-popup.smally.show').style.display !=\"none\"){document.querySelector('#detail-base-smart-banner').style.display=\"none\";document.querySelector('#content.pt85').style.paddingTop='41px';document.querySelector('.app-download-popup.smally.show').style.display=\"none\";}}window.setInterval(setTop, 0);";
    //清除tmall首页广告
    protected static String noAdTmallIndex="javascript:function setIndex(){if(document.querySelector('#J_BottomSmartBanner').style.display!='none' || document.querySelector('.app-download-popup.smally.show').style.display!='none' ){document.querySelector('#J_BottomSmartBanner').style.display=\"none\";document.querySelector('.app-download-popup.smally.show').style.display=\"none\";}} window.setInterval(setIndex, 0);";
    //清除淘宝广告js
    protected static String noAdTaoBao="javascript:function setTaobao(){if(document.querySelector('body').style.paddingTop !=\"0px\"){document.querySelector('body').style.paddingTop=\"0px\";document.querySelector('.app-detail').style.zIndex='2';document.querySelector('.main_layout').style.backgroundColor='white';}} window.setInterval(setTaobao, 0);";
    //清除淘宝评价广告
    protected static String NoAdTaoBaoPingjia="javascript:function setTaobaoPingjia(){if(document.querySelector('#J_ratePop').style.zIndex != '2'){document.querySelector('body').style.paddingTop =\"0px\";document.querySelector('#J_ratePop').style.zIndex='2',document.querySelector('#J_ratePop').style.position='absolute'}}  window.setInterval(setTaobaoPingjia, 0);";

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web_view);
        toolbar_wb=getViewById(R.id.toolbar_wb);
        mQuan_surplus=getViewById(R.id.Quan_surplus);
        mQuan_time=getViewById(R.id.Quan_time);
        button_wb=getViewById(R.id.button_wb);
        webView=getViewById(R.id.webView);
        progressBar=getViewById(R.id.progressBar);
        refresh_wb=getViewById(R.id.refresh_wb);
        mDetail=getViewById(R.id.quanDeatail);
        close_view=getViewById(R.id.close_view);
        bottom_view=getViewById(R.id.bottom_view);
    }

    @Override
    protected void setListener() {
        toolbar_wb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_wb.setOnClickListener(this);
        button_wb.setOnTouchListener(this);
        refresh_wb.setOnClickListener(this);
        close_view.setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        toolbar_wb.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        initBundle();
        mQuan_time.setText("到期 "+Quan_time.substring(0,10));
        mQuan_surplus.setText("剩余"+Quan_surplus+"张");
        button_wb.setText("领"+Quan_price+"元卷");
        progressBar.setMax(100);
        webView.loadUrl(Ali_click);
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        // enable navigator.geolocation
        s.setGeolocationEnabled(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        // enable Web Storage: localStorage, sessionStorage
        s.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl(noAdTmall);
                webView.loadUrl(noAdTmallIndex);
                webView.loadUrl(noAdTaoBao);
                webView.loadUrl(NoAdTaoBaoPingjia);
                if (view.getOriginalUrl().equals(Ali_click) || (view.getUrl().contains(GoodsId)&&view.getUrl().contains("ali_trackid"))){
                    button_wb.setText("领"+Quan_price+"元卷");
                }else {
                    button_wb.setText("返回购物");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
                if(newProgress==100){
                    progressBar.setVisibility(View.INVISIBLE);
                    //Toast.makeText(mContext, "加载结束...", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    //Toast.makeText(mContext, "加载中...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        webView.requestFocus();
    }

    private void initBundle(){
        Bundle bundle=getIntent().getExtras();
        Quan_price=bundle.getString("Quan_price");
        Quan_time=bundle.getString("Quan_time");
        Quan_surplus=bundle.getString("Quan_surplus");
        Quan_link=bundle.getString("Quan_link");
        Ali_click=bundle.getString("Ali_click");
        GoodsId=bundle.getString("GoodsId");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.refresh_wb:
                webView.reload();
                break;
            case R.id.button_wb:
                if(button_wb.getText().equals("返回购物")){
                    webView.loadUrl(Ali_click);
                }else {
                    webView.loadUrl(Quan_link);
                }
                break;
            case R.id.close_view:
                bottom_view.setVisibility(View.GONE);
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) webView.getLayoutParams();
                layoutParams2.setMargins(0,layoutParams2.topMargin,0,0);
                webView.setLayoutParams(layoutParams2);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.button_wb:
                if(event.getAction()==KeyEvent.ACTION_DOWN && !button_wb.getText().equals("返回购物")){
                    mDetail.setVisibility(View.VISIBLE);
                }else if(event.getAction()==KeyEvent.ACTION_UP){
                    mDetail.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return false;
    }
}
