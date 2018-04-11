package com.ayearn.playerlib.controller.progress;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.ayearn.playerlib.R;

/**
 * Created by castorflex on 11/10/13.
 */
public class CircularProgressBar extends ProgressBar {
	//进度条的类型，一个大，一个小的
	public static final int STYLE_BIG = 1;
	public static final int STYLE_SMALL = 2;
	//大的类型的宽度和小的类型的宽度
	private int smallWidth = 0;
	private int bigWidth = 0;
	//当前的宽度
	private int width = smallWidth;
	  public CircularProgressBar(Context context) {
	    this(context, null);
	  }
	
	  public CircularProgressBar(Context context, AttributeSet attrs) {
	    this(context, attrs, R.attr.cpbStyle);
	  }
	
	  public CircularProgressBar(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	
	    if (isInEditMode()) {
	      setIndeterminateDrawable(new CircularProgressDrawable.Builder(context, true).build());
	      return;
	    }
	
	    Resources res = context.getResources();
	    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar, defStyle, 0);
	
	
	    final int color = a.getColor(R.styleable.CircularProgressBar_cpb_color, res.getColor(R.color.cpb_default_color));
	    final float strokeWidth = a.getDimension(R.styleable.CircularProgressBar_cpb_stroke_width, res.getDimension(R.dimen.cpb_default_stroke_width));
	    final float sweepSpeed = a.getFloat(R.styleable.CircularProgressBar_cpb_sweep_speed, Float.parseFloat(res.getString(R.string.cpb_default_sweep_speed)));
	    final float rotationSpeed = a.getFloat(R.styleable.CircularProgressBar_cpb_rotation_speed, Float.parseFloat(res.getString(R.string.cpb_default_rotation_speed)));
	    final int colorsId = a.getResourceId(R.styleable.CircularProgressBar_cpb_colors, 0);
	    final int minSweepAngle = a.getInteger(R.styleable.CircularProgressBar_cpb_min_sweep_angle, res.getInteger(R.integer.cpb_default_min_sweep_angle));
	    final int maxSweepAngle = a.getInteger(R.styleable.CircularProgressBar_cpb_max_sweep_angle, res.getInteger(R.integer.cpb_default_max_sweep_angle));
	    //获得大样式的宽度
	    bigWidth = (int) res.getDimension(R.dimen.cpb_big_width);
	    //获得小样式的宽度
	    smallWidth = (int) res.getDimension(R.dimen.cpb_small_width);
	    a.recycle();
	
	    int[] colors = null;
	    //colors
	    if (colorsId != 0) {
	      colors = res.getIntArray(colorsId);
	    }
	
	    Drawable indeterminateDrawable;
	    CircularProgressDrawable.Builder builder = new CircularProgressDrawable.Builder(context)
	        .sweepSpeed(sweepSpeed)
	        .rotationSpeed(rotationSpeed)
	        .strokeWidth(strokeWidth)
	        .minSweepAngle(minSweepAngle)
	        .maxSweepAngle(maxSweepAngle);
	
	    if (colors != null && colors.length > 0)
	      builder.colors(colors);
	    else
	      builder.color(color);
	
	    indeterminateDrawable = builder.build();
	    setIndeterminateDrawable(indeterminateDrawable);
	  }
	
	  private CircularProgressDrawable checkIndeterminateDrawable() {
	     Drawable ret = getIndeterminateDrawable();
	     if (ret == null || !(ret instanceof CircularProgressDrawable))
	        throw new RuntimeException("The drawable is not a CircularProgressDrawable");
	     return (CircularProgressDrawable) ret;
	  }
	
	  public void progressiveStop() {
	    checkIndeterminateDrawable().progressiveStop();
	  }
	
	  public void progressiveStop(CircularProgressDrawable.OnEndListener listener) {
	    checkIndeterminateDrawable().progressiveStop(listener);
	  }
	  
	  @Override
 	protected synchronized void onMeasure(int widthMeasureSpec,
 			int heightMeasureSpec) {
 		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
 		if(width!=0){
 		setMeasuredDimension(width, width);
 		} 
 		
 	}

 	/**
 	 *  
 	 * @param style
 	 * @description 设置style，目前仅有小的style和大的style两种，其大小可以在配置文件（smooth_circular_progressbar_attrs.xml）中设置
 	 * @version 1.0
 	 * @author houjie
 	 * @date 2016-10-8 下午3:52:58 
 	 * @update 2016-10-8 下午3:52:58
 	 */
 	public void setStyle(int style) {
 		
 		//style为STYLE_BIG时，将控件的大小设置为bigWidth
 		if (style == STYLE_BIG) {
 			if (bigWidth != 0) {
 				width = bigWidth;
 			}
 		}
 		//style为STYLE_SMALL时，将控件的大小设置为smallWidth
 		if (style == STYLE_SMALL) {
 			if (smallWidth != 0) {
 				width = smallWidth;
 			}
 		}
 		//使控件重新计算大小，使用invalidate()方法没有作用，目前只想到此方法，若有其他方法实现，可以更改
 		if (getVisibility() == View.VISIBLE) {
 			setVisibility(View.GONE);
 			setVisibility(View.VISIBLE);
 		}
 		
 	}
}
