package cn.hwwwwh.lexiangdaxue.FgSecondClass;

/**
 * Created by 97481 on 2017/6/18/ 0018.
 */

import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * RecyclerView滑动事件
 */
public class ScrollListListener extends RecyclerView.OnScrollListener {

    ImageLoader imageLoader=ImageLoader.getInstance();
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        switch (newState){
            case RecyclerView.SCROLL_STATE_DRAGGING:
                //正在滑动
                imageLoader.pause();
                Glide.with(recyclerView.getContext()).pauseRequests();
                break;
            case RecyclerView.SCROLL_STATE_IDLE:
                //滑动停止
                imageLoader.resume();
                Glide.with(recyclerView.getContext()).resumeRequests();
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //请忽视这个方法,这个方法是我自己使用的,判断向上,向下滑动的
        /**
         * 向下滑动 dy为负数 false
         * 向上滑动 dy为正数 true
         */
//        if (dy > 0) {
//            TScrollState.upAndDownState(true);
//        } else {
//            TScrollState.upAndDownState(false);
//        }

    }


}
