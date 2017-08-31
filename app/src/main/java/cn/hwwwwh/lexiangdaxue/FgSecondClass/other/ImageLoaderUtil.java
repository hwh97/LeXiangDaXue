package cn.hwwwwh.lexiangdaxue.FgSecondClass.other;

/**
 * Created by 97481 on 2017/6/18/ 0018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.hwwwwh.lexiangdaxue.R;

/**
 * Created by HMY
 */
public class ImageLoaderUtil {

    public static ImageLoader getImageLoader(Context context) {
        return ImageLoader.getInstance();
    }

    public static DisplayImageOptions getPhotoImageOption() {
        Integer extra = 1;
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.loadpic_default).showImageOnFail(R.drawable.loadpic_default)
                .showImageOnLoading(R.drawable.loadpic_default)
                .extraForDownloader(extra)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    public static void myDisplayImage(Context context, ImageView imageView, String url, DisplayImageOptions options) {
        ImageAware imageAware = new ImageViewAware(imageView, false);
        getImageLoader(context).displayImage(url, imageAware, options);
    }

    public static void myDisplayImage(Context context, ImageView imageView, String url, DisplayImageOptions options, ImageLoadingListener listener) {
        ImageAware imageAware = new ImageViewAware(imageView, false);
        getImageLoader(context).displayImage(url, imageAware, options,listener);
    }
}