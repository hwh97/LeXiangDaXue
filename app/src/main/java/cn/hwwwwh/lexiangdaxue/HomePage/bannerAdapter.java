package cn.hwwwwh.lexiangdaxue.HomePage;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stx.xhb.xbanner.XBanner;

/**
 * Created by 97481 on 2016/10/24.
 */
public class bannerAdapter implements XBanner.XBannerAdapter {
    @Override
         public void loadBanner(XBanner banner, View view, int position) {
       // Glide.with(this).load(imgesUrl.get(position)).into((ImageView) view);
    }
}
