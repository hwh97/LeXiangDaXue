package cn.hwwwwh.lexiangdaxue;

/**
 * Created by 97481 on 2017/3/22/ 0022.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Map;

import cn.hwwwwh.lexiangdaxue.other.PinchImageView;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    List<Map<String, Object>> viewLists;

    public ViewPagerAdapter(List<Map<String, Object>> lists, Context context) {
        this.viewLists = lists;
        this.context = context;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {  //获得size
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) { //销毁Item
        PinchImageView x = (PinchImageView) viewLists.get(position).get("view");
        x.setScaleType(PinchImageView.ScaleType.FIT_CENTER);
        view.removeView(x);
    }


    @Override
    public Object instantiateItem(ViewGroup view, int position){ //实例化Item
        PinchImageView imageView = (PinchImageView) viewLists.get(position).get("view");
        imageView.setScaleType(PinchImageView.ScaleType.FIT_CENTER);
        Glide.with(context)
                .load(viewLists.get(position).get("url").toString())
//    .placeholder(R.mipmap.new_default)
                .error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
        view.addView(imageView, 0);

        return viewLists.get(position).get("view");
    }

    public void setViewLists(List<Map<String, Object>> viewLists) {
        this.viewLists = viewLists;
        notifyDataSetChanged();
    }
}