package cn.hwwwwh.lexiangdaxue;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.ImageLoader.SelectPhotoAdapter;
import cn.hwwwwh.lexiangdaxue.other.BaseActivity;
import cn.hwwwwh.lexiangdaxue.other.PinchImageView;

public class ShowPhotoActivity extends BaseActivity implements ViewPager.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<Map<String, Object>> data;
    private int lastValue = -1; // 是不是最后一张图片
    private ImageView[] dots; //底部小点图片
    private int currentIndex=1; //记录当前选中位置
    private Toolbar showphoto_toolbar;
    private ImageView deletePhoto;
    private ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_photo);
        showphoto_toolbar=(Toolbar)findViewById(R.id.showphoto_toolbar);
        vp = (ViewPager) findViewById(R.id.photoViewPaper);
        deletePhoto=(ImageView)findViewById(R.id.deletePhoto);
    }

    @Override
    protected void setListener() {
        showphoto_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        selectedPhotos = getIntent().getParcelableArrayListExtra("selectPhotos");
        Log.i("Alex","选择的图片是"+selectedPhotos);
        showphoto_toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_24dp1);
        showphoto_toolbar.setTitle("1/"+selectedPhotos.size());
        showphoto_toolbar.setTitleTextAppearance(getApplicationContext(),R.style.WindowTitle);
        data=getData(selectedPhotos);
        vpAdapter = new ViewPagerAdapter(data,this);
        vp.setAdapter(vpAdapter);
        vp.addOnPageChangeListener(this);
        deletePhoto.setOnClickListener(this);
        vp.setOffscreenPageLimit(9);
    }

    //这个方法长度是动态的，可以改成你从服务器获取的图片，这样数量就不确定啦
    public List<Map<String, Object>> getData(ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos) {
        List<Map<String, Object>> mData = new ArrayList<>();

          if (selectedPhotos != null && selectedPhotos.size() > 0) {
           for (int i = 0; i < selectedPhotos.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("url", selectedPhotos.get(i).url);
            map.put("view", new PinchImageView(this));
            mData.add(map);
           }
          }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("url", "http://img2.duitang.com/uploads/item/201207/19/20120719132725_UkzCN.jpeg");
//        map.put("view", new ImageView(this));
//        mData.add(map);
//
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("url", "http://img4.duitang.com/uploads/item/201404/24/20140424195028_vtvZu.jpeg");
//        map1.put("view", new ImageView(this));
//        mData.add(map1);
//
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("url", "http://download.pchome.net/wallpaper/pic-5041-8-240x320.jpg");
//        map2.put("view", new ImageView(this));
//        mData.add(map2);
//
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("url", "http://www.mangowed.com/uploads/allimg/130425/572-130425105311304.jpg");
//        map3.put("view", new ImageView(this));
//        mData.add(map3);
        return mData;
    }

/*    private void initDots() {
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(3, 0, 3, 0);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        dots = new ImageView[data.size()];
        //循环取得小点图片
        for (int i = 0; i < data.size(); i++) {
            dots[i] = new ImageView(this);
            dots[i].setLayoutParams(mLayoutParams);
            dots[i].setBackgroundResource(R.drawable.ic_lens_black_24dp);
            dots[i].setEnabled(true);//都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);//设置位置tag，方便取出与当前位置对应
            ll.addView(dots[i]);
        }
        currentIndex = 0;
        dots[currentIndex].setEnabled(false);//设置为白色，即选中状态
    }*/

    private void setCurView(int position) { /**设置当前的引导页*/
        if (position < 0 || position >= data.size()) {return;}
        vp.setCurrentItem(position);
    }

    private void setCurDot(int position) { /**设置当前引导小点的选中*/
        if (position < 0 || position > data.size() - 1 || currentIndex == position) {return;}
        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    @Override //当前页面被滑动时调用
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        lastValue = position;
    }

    @Override
    public void onPageSelected(int position) {     //当新的页面被选中时调用
       // setCurDot(position); //设置底部小点选中状态
        currentIndex = position;
        showphoto_toolbar.setTitle((position+1)+"/"+data.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {   //当滑动状态改变时调用
        if(state == 0){
            if(lastValue == data.size()-1){
                Toast.makeText(this, "已经是最后一张了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
//        int position = (Integer)v.getTag();
//        setCurView(position);
//        setCurDot(position);
        switch (v.getId()){
            case R.id.deletePhoto:

                break;
        }
    }

}
