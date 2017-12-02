package cn.hwwwwh.lexiangdaxue;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;

import cn.hwwwwh.lexiangdaxue.FgFourthClass.bean.userBean;
import cn.hwwwwh.lexiangdaxue.FgSecondClass.fragment.GuideTwoFragment;
import cn.hwwwwh.lexiangdaxue.FgThirdClass.fragment.GuideThirdFragment;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SQLiteHandler;
import cn.hwwwwh.lexiangdaxue.LoginRegister.SessionManager;
import cn.hwwwwh.lexiangdaxue.fragment.GuideFragmentAdapter;
import cn.hwwwwh.lexiangdaxue.other.RxBus;
import cn.hwwwwh.lexiangdaxue.other.ToastUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static final int TAB_HOME = 0;
    public static final int TAB_CATAGORY = 1;
    public static final int TAB_CAR = 3;
    public  static  final int TAB_THIRD=2;
    private SQLiteHandler db;
    private SessionManager session;
    private ViewPager viewPager;
    private RadioButton main_tab_home, main_tab_catagory, main_tab_car,main_tab_third;
    public GuideFragmentAdapter adapter;
    private RxBus rxBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //window.setStatusBarColor(getResources().getColor(R.color.snow));
        }
        setContentView(R.layout.activity_main);
        initView();
        addListener();
        initRxBus();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getExtras()!=null){
            viewPager.setCurrentItem(intent.getExtras().getInt("order"));
            ((GuideThirdFragment)adapter.getItem(2)).initTab();
        }
    }

    private void initRxBus() {
        rxBus = RxBus.getIntanceBus();
        registerRxBus(Integer.class, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                ((GuideThirdFragment)adapter.getItem(2)).initTab();
                ((GuideTwoFragment)adapter.getItem(1)).loadTab();
                viewPager.setCurrentItem(integer);
            }

        });
    }

    //注册
    public <T> void registerRxBus(Class<T> eventType, Consumer<T> action) {
        Disposable disposable = rxBus.doSubscribe(eventType, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Log.e("NewsMainPresenter", throwable.toString());
            }
        });
        rxBus.addSubscription(this,disposable);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
       // super.onSaveInstanceState(outState);
    }


    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        main_tab_home = (RadioButton) findViewById(R.id.main_tab_home);
        main_tab_catagory = (RadioButton) findViewById(R.id.main_tab_catagory);
        main_tab_car = (RadioButton) findViewById(R.id.main_tab_car);
        main_tab_third=(RadioButton) findViewById(R.id.main_tab_third);

        main_tab_home.setOnClickListener(this);
        main_tab_catagory.setOnClickListener(this);
        main_tab_car.setOnClickListener(this);
        main_tab_third.setOnClickListener(this);

        adapter = new GuideFragmentAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());
    }

    private void addListener() {
        final Drawable home = getResources().getDrawable(R.drawable.ic_home149);
        final Drawable home_gray = getResources().getDrawable(R.drawable.ic_home149_gray);
        final Drawable find = getResources().getDrawable(R.drawable.ic_find);
        final Drawable find_gray = getResources().getDrawable(R.drawable.ic_find_gray);
        final Drawable shop = getResources().getDrawable(R.drawable.ic_shopping_basket_24px);
        final Drawable shop_gray = getResources().getDrawable(R.drawable.ic_shopping_basket_24px_gray);
        final Drawable people = getResources().getDrawable(R.drawable.ic_pople);
        final Drawable people_gray = getResources().getDrawable(R.drawable.ic_pople_gray);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case TAB_HOME:
                        main_tab_home.setChecked(true);
                        main_tab_home.setTextColor(MainActivity.this.getResources().getColor(R.color.darkorange));
                        main_tab_catagory.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_car.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_third.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_home.setCompoundDrawablesWithIntrinsicBounds(null, home, null, null);
                        main_tab_catagory.setCompoundDrawablesWithIntrinsicBounds(null, find_gray, null, null);
                        main_tab_third.setCompoundDrawablesWithIntrinsicBounds(null, shop_gray, null, null);
                        main_tab_car.setCompoundDrawablesWithIntrinsicBounds(null, people_gray, null, null);
                        break;
                    case TAB_CATAGORY:
                        main_tab_catagory.setChecked(true);
                        main_tab_home.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_catagory.setTextColor(MainActivity.this.getResources().getColor(R.color.darkorange));
                        main_tab_car.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_third.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_home.setCompoundDrawablesWithIntrinsicBounds(null, home_gray, null, null);
                        main_tab_catagory.setCompoundDrawablesWithIntrinsicBounds(null, find, null, null);
                        main_tab_third.setCompoundDrawablesWithIntrinsicBounds(null, shop_gray, null, null);
                        main_tab_car.setCompoundDrawablesWithIntrinsicBounds(null, people_gray, null, null);
                        break;
                    case TAB_THIRD:
                        main_tab_catagory.setChecked(true);
                        main_tab_home.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_catagory.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_car.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_third.setTextColor(MainActivity.this.getResources().getColor(R.color.darkorange));
                        main_tab_home.setCompoundDrawablesWithIntrinsicBounds(null, home_gray, null, null);
                        main_tab_catagory.setCompoundDrawablesWithIntrinsicBounds(null, find_gray, null, null);
                        main_tab_third.setCompoundDrawablesWithIntrinsicBounds(null, shop, null, null);
                        main_tab_car.setCompoundDrawablesWithIntrinsicBounds(null, people_gray, null, null);
                        break;
                    case TAB_CAR:
                        main_tab_car.setChecked(true);
                        main_tab_home.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_catagory.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_car.setTextColor(MainActivity.this.getResources().getColor(R.color.darkorange));
                        main_tab_third.setTextColor(MainActivity.this.getResources().getColor(R.color.darkgray));
                        main_tab_home.setCompoundDrawablesWithIntrinsicBounds(null, home_gray, null, null);
                        main_tab_catagory.setCompoundDrawablesWithIntrinsicBounds(null, find_gray, null, null);
                        main_tab_third.setCompoundDrawablesWithIntrinsicBounds(null, shop_gray, null, null);
                        main_tab_car.setCompoundDrawablesWithIntrinsicBounds(null, people, null, null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab_home:
                viewPager.setCurrentItem(TAB_HOME);
                break;
            case R.id.main_tab_catagory:
                viewPager.setCurrentItem(TAB_CATAGORY);
                break;
            case R.id.main_tab_car:
                viewPager.setCurrentItem(TAB_CAR);

                break;
            case R.id.main_tab_third:
                //if logged keep way
                if (!session.isLoggedIn()) {
                    logoutUser();
                }
                else {
                    viewPager.setCurrentItem(TAB_THIRD);
                }
                break;
            default:
                break;
        }
    }

    private void logoutUser(){
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxBus.unSubscribe(this);
    }
}
