package com.ayearn.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016-12-8.
 */
public class ViewFactory {
    public static final int LINEARLAYOUT_TYPE = 1;      //线性布局
    public static final int RELATIVELAYOUT_TYPE = 2;    //相对布局
    public static final int FRAMELAYOUT_TYPE = 3;       //帧布局

    /**
     * 创建一个view 默认背景为白色
     * @param context
     * @param width 宽度
     * @param height 高度
     * @param margins margin值 leftMargin = margins[0] topMargin = margins[1] rightMargin = margins[2] bottomMargin = margins[3]
     * @param parentLayoutType 父布局的类型 LINEARLAYOUT_TYPE（1）=线性布局 RELATIVELAYOUT_TYPE（2）=相对布局 FRAMELAYOUT_TYPE（3）=帧布局
     * @return
     */
    public static View createView(Context context, int width, int height, int[] margins, int parentLayoutType) {
        if(context == null || ((Activity)context).isFinishing()) {
            return null;
        }

        View view = new View(context);
        ViewGroup.MarginLayoutParams params = getParentLayoutParam(width,height,parentLayoutType);
        if(margins!=null && margins.length ==4 && params !=null) {
            params.leftMargin = margins[0];
            params.topMargin = margins[1];
            params.rightMargin = margins[2];
            params.bottomMargin = margins[3];
        }
        view.setBackgroundColor(Color.WHITE);
        view.setLayoutParams(params);
        return view;
    }

    /**
     * 创建一个view 默认背景为白色
     * @param context
     * @param width 宽度
     * @param height 高度
     * @param margins margin值 leftMargin = margins[0] topMargin = margins[1] rightMargin = margins[2] bottomMargin = margins[3]
     * @param parentLayoutType 父布局的类型 LINEARLAYOUT_TYPE（1）=线性布局 RELATIVELAYOUT_TYPE（2）=相对布局 FRAMELAYOUT_TYPE（3）=帧布局
     * @return
     */
    public static View createView(Context context, int width, int height, int[] margins, int parentLayoutType, int color) {
        if(context == null || ((Activity)context).isFinishing()) {
            return null;
        }
        View view = new View(context);
        ViewGroup.MarginLayoutParams params = getParentLayoutParam(width,height,parentLayoutType);
        if(margins!=null && margins.length ==4 && params !=null) {
            params.leftMargin = margins[0];
            params.topMargin = margins[1];
            params.rightMargin = margins[2];
            params.bottomMargin = margins[3];
        }
        view.setBackgroundColor(color);
        view.setLayoutParams(params);
        return view;
    }

    /**
     *
     * @param context
     * @param width
     * @param height
     * @param margins
     * @param parentLayoutType
     * @param sourseid 资源id    注意：此处设置的是ImageView的background而不是src
     * @return
     */
    public static ImageView createImageView(Activity context, int width, int height, int[] margins, int parentLayoutType, int sourseid) {
        ImageView imageView = new ImageView(context);
        ViewGroup.MarginLayoutParams params = getParentLayoutParam(width,height,parentLayoutType);
        if(margins!=null && margins.length ==4 && params !=null) {
            params.leftMargin = margins[0];
            params.topMargin = margins[1];
            params.rightMargin = margins[2];
            params.bottomMargin = margins[3];
        }
        imageView.setBackgroundResource(sourseid);
        imageView.setLayoutParams(params);
        return imageView;
    }

    /**
     * 根据type值获取LayoutpParams
     * @param width
     * @param height
     * @param type
     * @return
     */
    private static ViewGroup.MarginLayoutParams getParentLayoutParam(int width, int height, int type) {
        switch (type) {
            case LINEARLAYOUT_TYPE:
                return new LinearLayout.LayoutParams
                        (width>=0?width: ViewGroup.LayoutParams.MATCH_PARENT, height>=0?height: ViewGroup.LayoutParams.MATCH_PARENT);
            case RELATIVELAYOUT_TYPE:
                return new RelativeLayout.LayoutParams
                        (width>=0?width: ViewGroup.LayoutParams.MATCH_PARENT, height>=0?height: ViewGroup.LayoutParams.MATCH_PARENT);
            case FRAMELAYOUT_TYPE:
                return new FrameLayout.LayoutParams
                        (width>=0?width: ViewGroup.LayoutParams.MATCH_PARENT, height>=0?height: ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return null;
    }

}
