package com.ayearn.playerlib.controller;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.base.PlayItem;
import com.voole.utils.log.LogUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/9 21:20
 * 邮箱：lichao@voole.com
 */

public class RightViewControl {
    private final static String TAG = "RightViewControl";
    public Context mContext;
    public View mView;
    public MediaViewControl mediaViewControl;
    public RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;
    public OnItemClickListener onItemClickListener;
    private MyAdapter myAdapter;
    public int currentPosition;
    public int prePosition;
    public RightViewControl(Context context, View view, MediaViewControl mediaViewControl) {
        this.mContext = context;
        this.mView = view;
        this.mediaViewControl = mediaViewControl;
        initView();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void  showAndHideRecycleView(){
        if (mView.getVisibility()==View.GONE){
            mView.setVisibility(View.VISIBLE);
        }else {
            mView.setVisibility(View.GONE);
        }
    }
    public void hideRecycleView(){
        if (myAdapter.mDatas.size()>0){
            mView.setVisibility(View.GONE);
        }
    }
    public void showRecycleView(){
        if (myAdapter.mDatas.size()>0){
            mView.setVisibility(View.VISIBLE);
        }
    }
    private void initView() {
        mRecyclerView = (RecyclerView)mView.findViewById(R.id.right_recycle_view);
        mLayoutManager = new GridLayoutManager(mContext,5);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(8));
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        myAdapter= new MyAdapter();
        //创建并设置Adapter
        mRecyclerView.setAdapter(myAdapter);
    }
    public void updateCurrentPosition(int position){
        if (position == currentPosition){
            return;
        }
        prePosition = currentPosition;
        currentPosition = position;
        LogUtil.d(TAG,"updateCurrentPosition(RightViewControl.java:65)--Info-->>"+prePosition);
        LogUtil.d(TAG,"updateCurrentPosition(RightViewControl.java:66)--Info-->>........."+currentPosition);
        myAdapter.notifyItemChanged(currentPosition);
        myAdapter.notifyItemChanged(prePosition);
    }
    /**
     *
     * @param mDatas
     */
    public void setAdapter(List<PlayItem> mDatas , int currentPosition) {
        myAdapter.setmDatas(mDatas);
        updateCurrentPosition(currentPosition);
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public List<PlayItem> mDatas= new ArrayList<>();
        public MyAdapter() {
        }
        public void setmDatas(List<PlayItem> datas){
            this.mDatas = datas;
        }
        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_right_item,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }
        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            viewHolder.mTextView.setText((mDatas.get(position).getPlayIndex()+1)+"");
            viewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(position);
                        updateCurrentPosition(position);
                    }
                }
            });

            if (currentPosition == position){
                LogUtil.d(TAG,"onBindViewHolder(RightViewControl.java:114)--Info-->>"+ currentPosition +" position is " + position);
                viewHolder.mTextView.setBackgroundResource(R.drawable.shape_round_bt_focus);
            }
            if (prePosition == position&& currentPosition!=prePosition){
                viewHolder.mTextView.setBackgroundResource(R.drawable.shape_round_bt_unfoces);
            }
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return mDatas.size();
        }
        //自定义的ViewHolder，持有每个Item的的所有界面元素
        class ViewHolder extends RecyclerView.ViewHolder {
            public Button mTextView;
            public ViewHolder(View view){
                super(view);
                mTextView =(Button) view.findViewById(R.id.recycle_text_view);
            }
        }
    }
    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;
//            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace;
//            }

        }

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }
    }
    public interface OnItemClickListener {
        void  onItemClick(int position);
    }
}
