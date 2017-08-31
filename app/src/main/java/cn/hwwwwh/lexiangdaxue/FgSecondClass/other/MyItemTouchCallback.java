package cn.hwwwwh.lexiangdaxue.FgSecondClass.other;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.Collections;
import java.util.List;

import cn.hwwwwh.lexiangdaxue.FgSecondClass.adapter.ShowPhotoAdapter;
import cn.hwwwwh.lexiangdaxue.ImageLoader.SelectPhotoAdapter;

/**
 * Created by 97481 on 2017/3/24/ 0024.
 */

public class MyItemTouchCallback extends ItemTouchHelper.Callback {

    private final ShowPhotoAdapter adapter;
    private Boolean isMove=false;
    private List<SelectPhotoAdapter.SelectPhotoEntity> selectPhotoEntity;
    private int finalPosition=0;
    private int startPosition=0;

    public MyItemTouchCallback(ShowPhotoAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag;
        int swipeFlag;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP
                    | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
            swipeFlag = 0;
        } else {
            dragFlag = ItemTouchHelper.DOWN | ItemTouchHelper.UP;
            swipeFlag = ItemTouchHelper.END;
        }
        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(adapter.getmDatas(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(adapter.getmDatas(), i, i - 1);
            }
        }
        recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        finalPosition=toPos;
        Log.d("testlexiangdaxue",toPos+"");
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.END) {
            adapter.getmDatas().remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            //viewHolder.itemView.setBackgroundColor(Color.BLUE);
            startPosition=viewHolder.getLayoutPosition();
            finalPosition=viewHolder.getLayoutPosition();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //viewHolder.itemView.setBackgroundColor(0);
        Log.d("testlexiangdaxue",startPosition+"  "+finalPosition);
        if (startPosition == finalPosition) {
            //长按事件
            adapter.setShowBox();
        }
    }
}