

package com.ayearn.ui.recycleview.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ayearn.ui.recycleview.listener.RecycleItemOnClickListener;

import java.util.List;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-3-26 17:30
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<ViewHolder>{

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected RecyclerView mRecyclerView;
    public BaseRecycleAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }
    /**
     * 1.重写onBindViewHolder(VH holder, int position, List<Object> payloads)这个方法
     * <p>
     * 2.执行两个参数的notifyItemChanged，第二个参数随便什么都行，只要不让它为空就OK~，
     * 这样就可以实现只刷新item中某个控件了！！！
     * payload 的解释为：如果为null，则刷新item全部内容
     * 那言外之意就是不为空就可以局部刷新了~！
     *
     * @param holder   服用的holder
     * @param position 当前位置
     * @param payloads 如果为null，则刷新item全部内容  否则局部刷新
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (holder instanceof ViewHolder) {
            convert(holder, mDatas.get(position));
        }
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if(recycleItemOnClickListener==null){
                    return;
                }
                view.setOnClickListener(new ItemClickListener(mRecyclerView));
                if(mFocusListener==null){
                    return;
                }
                view.setOnFocusChangeListener(mFocusListener);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                view.setOnClickListener(null);
                view.setOnFocusChangeListener(null);
            }
        });

    }
    private RecycleItemOnClickListener recycleItemOnClickListener;
    public void setItemOnClickListenr(RecycleItemOnClickListener recycleItemOnClickListener){
        this.recycleItemOnClickListener = recycleItemOnClickListener;
    }
    private View.OnFocusChangeListener mFocusListener;
    public void setItemOnFocusListener(View.OnFocusChangeListener listener){
        this.mFocusListener = listener;
    }
    private class ItemClickListener implements View.OnClickListener{
        private RecyclerView recyclerView;
        public ItemClickListener(RecyclerView recyclerView){
            this.recyclerView = recyclerView;
        }
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(v);
            int position = holder.getAdapterPosition();
            if (mDatas.size()>position){
                recycleItemOnClickListener.onItemClick(v, holder , position,mDatas.get(position));
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
