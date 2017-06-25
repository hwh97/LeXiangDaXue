package cn.hwwwwh.lexiangdaxue.FgSecondClass;

/**
 * Created by 97481 on 2017/5/31/ 0031.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.ninegrid.NineGridView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import cn.hwwwwh.lexiangdaxue.R;

public class NineGridPicLayout  extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public NineGridPicLayout(Context context) {
        super(context);
    }

    public NineGridPicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected boolean displayOneImage(final RatioImageView imageView, final String url, final int parentWidth, int picWidth, int picHeight,int loadImageMode) {
        if (loadImageMode==1) {
            //这里是只显示一张图片的情况，显示图片的宽高可以根据实际图片大小自由定制，parentWidth 为该layout的宽度
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
            ImageLoaderUtil.myDisplayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    int newW;
                    int newH;
                    if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                        newW = parentWidth / 2;
                        newH = newW * 5 / 3;
                    } else if (h < w) {//h:w = 2:3
                        newW = parentWidth * 2 / 3;
                        newH = newW * 2 / 3;
                    } else {//newH:h = newW :w
                        newW = parentWidth / 2;
                        newH = h * newW / w;
                    }
                    setOneImageLayoutParams(imageView, newW, newH);
                    imageView.setTag(url);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }else{
            int w = picWidth;
            int h = picHeight;
            int newW;
            int newH;
            if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                newW = parentWidth / 2;
                newH = newW * 5 / 3;
            } else if (h < w) {//h:w = 2:3
                newW = parentWidth * 2 / 3;
                newH = newW * 2 / 3;
            } else {//newH:h = newW :w
                newW = parentWidth / 2;
                newH = h * newW / w;
            }
            setOneImageLayoutParams(imageView, newW, newH);
            //Glide.with(mContext).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).placeholder(R.drawable.loadpic_default).dontAnimate().dontTransform().into(imageView);
            Picasso.with(getContext()).load(url).tag(getContext()).resize(w,h).into(imageView);
        }
        return false;// true 代表按照九宫格默认大小显示(此时不要调用setOneImageLayoutParams)；false 代表按照自定义宽高显示。
    }

    @Override
    protected void displayImage(final RatioImageView imageView, String url,int loadImageMode) {
        if (loadImageMode==1) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
            ImageLoaderUtil.myDisplayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption());
            imageView.setTag(url);
        }else {
            Glide.with(mContext).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loadpic_default).dontAnimate().dontTransform().into(imageView);
        }
    }

    @Override
    protected void onClickImage(int i, String url, List<PicBean> urlList) {
        Toast.makeText(mContext, "点击了图片" + url, Toast.LENGTH_SHORT).show();
    }
}
