package cn.hwwwwh.lexiangdaxue.other;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by 97481 on 2016/11/21.
 */
public class BitmapCache implements ImageLoader.ImageCache {
    private static final String TAG = "BitmapCache";
    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {

        int maxSize = 4 * 1024 * 1024;

        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getRowBytes() * bitmap.getHeight();
            }

        };

    }

    @Override
    public Bitmap getBitmap(String url) {

        Log.i(TAG, "get cache " + url);

        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.i(TAG, "get cache: " + url);
        if (bitmap != null) {

            mCache.put(url, bitmap);

        }

    }
}
