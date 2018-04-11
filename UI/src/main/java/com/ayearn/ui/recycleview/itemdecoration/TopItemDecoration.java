package com.ayearn.ui.recycleview.itemdecoration;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.ayearn.ui.R;
import com.voole.utils.log.LogUtil;
import com.voole.utils.screen.DensityUtil;


/**
 * 利用recycleview的ItemDecoration 实现的吸顶效果
 * Created by liujingwei on 2017-7-3.
 */

public class TopItemDecoration extends RecyclerView.ItemDecoration{
    public ItemDecorationCallBack callBack;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private int alignBottom;
    private Paint.FontMetrics fontMetrics;
    private int leftMagin;
    public TopItemDecoration(Context context, ItemDecorationCallBack callBack) {
        Resources res = context.getResources();
        this.callBack = callBack;
        //设置悬浮栏的画笔---paint
        paint = new Paint();
        paint.setColor(res.getColor(R.color.colorPrimaryDark));
        //设置悬浮栏中文本的画笔
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(DensityUtil.getInstance().dip2px(context,14));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = new Paint.FontMetrics();
        //决定悬浮栏的高度等
        topGap = 40;
        //决定文本的显示位置等
        alignBottom = 15;
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        /*int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for(int i = 0;i < childCount;i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            String text = callBack.getGroupName(position);
            if(position==0||isFirstInGroup(position)) {
                float top = childView.getTop() - topGap;
                float bottom = childView.getTop();
                c.drawRect(left,top,right,bottom,paint);
                c.drawText(text,left,bottom,textPaint);
            }else {
                float top = childView.getTop();
                float bottom = childView.getTop();
                c.drawRect(left, top, right, bottom, paint);
                return;
            }
        }*/

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        drawTextView(c, parent, state);
        //drawXmlView(c, parent, state);
    }

    private void drawTextView(Canvas c, RecyclerView parent, RecyclerView.State state){
        //屏幕上出现的item数
        int itemCount = state.getItemCount();
        //总item数
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        String preGroupName;
        String currentGroupName=null;
        for(int i=0;i<childCount;i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            preGroupName = currentGroupName;
            currentGroupName = callBack.getGroupName(position);
            //如果是同一组则跳过
            if(currentGroupName==null|| TextUtils.equals(currentGroupName,preGroupName)){
                continue;
            }
            int viewBottom = childView.getBottom();
            float top = Math.max(topGap,childView.getTop()); //
            LogUtil.d("LogFilter","onDrawOver(TopItemDecoration.java:95)--Info-->>viewBottom"+"--"+position+"--"+viewBottom+"---getTop "+childView.getTop()+"--toGap "+topGap);

            if(position+1<itemCount) {
                String nextGroupName = callBack.getGroupName(position+1);
                //不同组的情况      viewBottom < top 上一个（即将消失的viewitem）的底部小于top值时
                if(!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;  //将底部值赋给top,在调用绘制时的rect高度变小，造成顶走的效果
                }
            }
            //根据是否同组情况的top值绘制（同组时）
            c.drawRect(left,top - topGap,right,top,paint);
            Paint.FontMetrics fm = textPaint.getFontMetrics();
            //文字竖直居中显示
            float baseLine = top - (topGap - (fm.bottom - fm.top)) / 2 - fm.bottom;
            c.drawText(currentGroupName, left + 5, baseLine, textPaint);
        }
    }

    public void drawXmlView(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //屏幕上出现的item数
        int itemCount = state.getItemCount();
        //总item数
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        String preGroupName;
        String currentGroupName=null;
        for(int i=0;i<childCount;i++) {
            View childView = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(childView);
            preGroupName = currentGroupName;
            currentGroupName = callBack.getGroupName(position);
            //如果是同一组则跳过
            if(currentGroupName==null|| TextUtils.equals(currentGroupName,preGroupName)){
                continue;
            }
            int viewBottom = childView.getBottom();
            float top = Math.max(topGap,childView.getTop()); //
            if(position+1<itemCount) {
                String nextGroupName = callBack.getGroupName(position+1);
                //不同组的情况      viewBottom < top 上一组最后一个（即将消失的viewitem）的底部小于top值时
                if(!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                    top = viewBottom;  //将底部值赋给top,造成顶走的效果，也可以通过平移画布实现同样的效果
                }
            }

            View groupView = callBack.getGroupView(position);
            if (groupView == null) return;
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(right, topGap);
            groupView.setLayoutParams(layoutParams);
            groupView.setDrawingCacheEnabled(true);
            groupView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            //指定高度、宽度的groupView
            groupView.layout(0, 0, right, topGap);
            groupView.buildDrawingCache();
            Bitmap bitmap = groupView.getDrawingCache();
            // 根据是否同组情况的top值绘制（同组时）       top - topGap 上一个分组的自后一个item小于悬浮栏高度时该值为负值
            c.drawRect(left,top - topGap,right,top,paint);
            c.drawBitmap(bitmap, left + 5, top - topGap, null);
        }

    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        if (pos == 0 || isFirstInGroup(pos)) {
            outRect.top = topGap;
        } else {
            outRect.top = 0;
        }

    }

    private boolean isFirstInGroup(int position) {
        if(position==0) {
            return true;
        }
        String currentGroupName = callBack.getGroupName(position);
        String preGroupName = callBack.getGroupName(position - 1);
        if(currentGroupName.equals(preGroupName)) {
            return false;
        }else {
            return true;
        }
    }

    public interface ItemDecorationCallBack {
        String getGroupName(int position);
        View getGroupView(int position);
    }


}
