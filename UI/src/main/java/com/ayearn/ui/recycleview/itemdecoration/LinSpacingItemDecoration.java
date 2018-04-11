package com.ayearn.ui.recycleview.itemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zyn on 2018-1-16.
 * //线性间距
 */

public class LinSpacingItemDecoration extends RecyclerView.ItemDecoration{
    private int lspace,rspace,tspace,bspace;

    public LinSpacingItemDecoration(int ls,int rs,int ts,int bs) {
        this.lspace=ls;
        this.rspace=rs;
        this.tspace=ts;
        this.bspace=bs;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = lspace;
        outRect.right = rspace;
        outRect.bottom = bspace;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = tspace;
        } else {
            outRect.top = 0;
        }
    }
}
