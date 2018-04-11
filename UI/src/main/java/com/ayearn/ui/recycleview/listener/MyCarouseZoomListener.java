package com.ayearn.ui.recycleview.listener;

import android.support.annotation.NonNull;
import android.view.View;

import com.ayearn.ui.recycleview.layoutmanager.CarouselLayoutManager;

/**
 * @author liujingwei
 * @DESC    itemPositionToCenterDiff      -2--> -1 --> 0 -->1 -->2
 * 只适合垂直的
 * @time 2018-1-2 11:43
 */

public class MyCarouseZoomListener implements CarouselLayoutManager.PostLayoutListener {
    private static final float MIN_SCALE = 0.8f;
    private static final float SCALE_A = 1-MIN_SCALE;
    @Override
    public ItemTransformation transformChild(@NonNull View child, float itemPositionToCenterDiff, int orientation) {
        float a = Math.signum(itemPositionToCenterDiff);
        float scale = 0;
        if(a>0){
            scale = -0.05f*itemPositionToCenterDiff*itemPositionToCenterDiff-0.15f*itemPositionToCenterDiff+1;
        }else {
            scale = -0.05f*itemPositionToCenterDiff*itemPositionToCenterDiff+0.15f*itemPositionToCenterDiff+1;
        }
        int halfH = child.getMeasuredHeight()/5;
        int halfW = child.getMeasuredWidth()/3;
        if(orientation==CarouselLayoutManager.HORIZONTAL){
            float tranX = -halfW*itemPositionToCenterDiff;
            return new ItemTransformation(scale, scale, tranX, 0);
        }else {
            float tranY = -halfH*itemPositionToCenterDiff;
            return new ItemTransformation(scale, scale, 0, tranY);
        }
    }
}
