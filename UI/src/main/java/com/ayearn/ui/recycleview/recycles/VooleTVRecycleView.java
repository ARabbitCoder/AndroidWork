package com.ayearn.ui.recycleview.recycles;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.ayearn.ui.recycleview.layoutmanager.CarouselLayoutManager;

/**
 * @author liujingwei
 * @DESC 能够监听上下按键监听事件，LayoutManager必须是CarouselLayoutManager
 * @time 2018-3-23 17:18
 */

public class VooleTVRecycleView extends RecyclerView {
    public VooleTVRecycleView(Context context) {
        super(context);
    }

    public VooleTVRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VooleTVRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private CarouselLayoutManager carouselLayoutManager;
    @Override
    public void setLayoutManager(LayoutManager layout) {
        if(layout instanceof CarouselLayoutManager){
            carouselLayoutManager = (CarouselLayoutManager)layout;
        }
        super.setLayoutManager(layout);
    }
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(carouselLayoutManager==null){
            return false;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_DOWN){
            if(carouselLayoutManager!=null){
                int current = carouselLayoutManager.getCenterItemPosition();
                int total = carouselLayoutManager.getmItemsCount();
                int next = current+1;
                int position = next>=total?0:next;
                smoothScrollToPosition(position);
            }
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
            if(carouselLayoutManager!=null){
                int current = carouselLayoutManager.getCenterItemPosition();
                int total = carouselLayoutManager.getmItemsCount();
                int next = current-1;
                int position = next<0?total:next;
                smoothScrollToPosition(position);
            }
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_UP){
            if(carouselLayoutManager!=null){
                int current = carouselLayoutManager.getCenterItemPosition();
                int total = carouselLayoutManager.getmItemsCount();
                int next = current-1;
                int position = next<0?total:next;
                smoothScrollToPosition(position);
            }
            return true;
        }
        if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER){
            int current = carouselLayoutManager.getCenterItemPosition();
            View view = getChildAt(current);
            view.callOnClick();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
