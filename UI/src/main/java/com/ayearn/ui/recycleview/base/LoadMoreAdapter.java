package com.ayearn.ui.recycleview.base;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ayearn.ui.R;

import java.util.List;

/**
 * @author liujingwei
 * @DESC 实现下拉加载更多的Adapter,获取到的数据必须调用setMoreDataAndNotify(List<T>) 才能生效
 * @time 2018-4-2 11:03
 */

public abstract class LoadMoreAdapter<T> extends BaseRecycleAdapter<T>{
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private boolean isLoading = false;
    private int footViewID = R.layout.recycle_foot_view;
    public LoadMoreAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_FOOTER){
            return ViewHolder.get(mContext,parent,footViewID);
        }else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if(!checkLayoutManager(recyclerView)){
            return;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisiableItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisiableItemPosition + 1 == getItemCount()){
                    if (!isLoading){
                        isLoading = true;
                        if (loadListener!=null){
                            loadListener.getMoreData();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    public int getItemCount() {
        int count = super.getItemCount()+1;
        return count;
    }
    public static final int TYPE_NORMAL = 1;
    private static final int TYPE_FOOTER = 2;
    @Override
    public int getItemViewType(int position) {
        if((position+1)==getItemCount()){
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    public void setFootViewID(int layout){
        this.footViewID = layout;
    }
    /**
     * 设置获取到的待加载数据
     * @param list
     */
    public void setMoreDataAndNotify(List<T> list){
        if(mDatas!=null){
            mDatas.addAll(list);
            notifyDataSetChanged();
            isLoading = false;
        }
    }


    public interface LoadListener{
        void getMoreData();
    }
    private LoadListener loadListener;
    public void setOnLoadListener(LoadListener loadListener){
        this.loadListener = loadListener;
    }

    private boolean checkLayoutManager(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            linearLayoutManager =(LinearLayoutManager) layoutManager;
            return true;
        }
        if(layoutManager instanceof GridLayoutManager){
            linearLayoutManager =(GridLayoutManager) layoutManager;
            ((GridLayoutManager) linearLayoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position +1 == getItemCount()? ((GridLayoutManager)linearLayoutManager).getSpanCount() : 1;
                }
            });
            return true;
        }
        return false;
    }
}
