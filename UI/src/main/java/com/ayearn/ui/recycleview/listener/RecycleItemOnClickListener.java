package com.ayearn.ui.recycleview.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-4 10:56
 */

public interface RecycleItemOnClickListener {
    void onItemClick(View view, RecyclerView.ViewHolder holder, int position, Object obj);
}
