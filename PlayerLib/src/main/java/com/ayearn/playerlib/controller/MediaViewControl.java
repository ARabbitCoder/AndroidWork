package com.ayearn.playerlib.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.base.PlayItem;
import com.ayearn.playerlib.base.PlayerInterface;
import com.ayearn.playerlib.helper.GestrueBaseHelper;
import com.ayearn.playerlib.helper.HandlerHelper;
import com.ayearn.playerlib.helper.MediaPlayerLayoutShowAndHideHelper;
import com.voole.utils.log.LogUtil;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/8 15:05
 * 邮箱：lichao@voole.com
 */

public class MediaViewControl extends RelativeLayout implements View.OnTouchListener {
    private  Context mContext;
    private PlayerInterface mVooleStandardPlayer;
    public GestrueBaseHelper mGestrueBaseHelper;
    public View mCenterView ,mBottomView ,mTopView ,mRightView;
    public HandlerHelper mHandlerHelper;
    public BottomSeekBarControl bottomSeekBarControl;
    public TopViewControl mTopViewControl;
    public RightViewControl mRightViewControl;
    public MediaPlayerLayoutShowAndHideHelper mediaPlayerLayoutShowAndHideHelper;
    public boolean isPreiewFilm = false;
    public int previewFilmTime = -1;
    public MediaViewControl(Context context) {
        super(context);
        mContext= context;
        mHandlerHelper = new HandlerHelper(this);
        mediaPlayerLayoutShowAndHideHelper = new MediaPlayerLayoutShowAndHideHelper(this);
        initTop();
        initCenter();
        initBottom();
        initRight();
        //setBackgroundResource(R.drawable.play);
        setOnTouchListener(this);
    }

    public MediaViewControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext= context;
        mHandlerHelper = new HandlerHelper(this);
        mediaPlayerLayoutShowAndHideHelper = new MediaPlayerLayoutShowAndHideHelper(this);
        initTop();
        initCenter();
        initBottom();
        initRight();
        //setBackgroundResource(R.drawable.play);
        setOnTouchListener(this);
    }
    private void initRight() {
        mRightView = LayoutInflater.from(mContext).inflate(R.layout.player_right_control, this, false);
        mRightViewControl = new RightViewControl(mContext, mRightView, this);
        mRightView.setVisibility(GONE);
        addView(mRightView);
    }
    private void initCenter() {
        mCenterView= LayoutInflater.from(mContext).inflate(R.layout.player_center_control, this,false);
        mGestrueBaseHelper = new GestrueBaseHelper(mContext,mCenterView,this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mCenterView.setLayoutParams(layoutParams);
        addView(mCenterView);
    }

    private void initTop() {
        mTopView =LayoutInflater.from(mContext).inflate(R.layout.player_top_controller,this,false);
        mTopViewControl = new TopViewControl(mContext,mTopView, this);
        ViewGroup.LayoutParams layoutParams =  mTopView.getLayoutParams();
        mTopView.setLayoutParams(layoutParams);
        addView(mTopView);
    }

    private void initBottom() {
        mBottomView =LayoutInflater.from(mContext).inflate(R.layout.player_bottom_controller,this,false);
        ViewGroup.LayoutParams layoutParams = mBottomView.getLayoutParams();
        bottomSeekBarControl = new BottomSeekBarControl(mContext,mBottomView,this);
        mBottomView.setLayoutParams(layoutParams);
        addView(mBottomView);
    }



    public int getPreviewTime(){
        return previewFilmTime;
    }


    /**
     * 初始化界面的数据
     * @param item
     */
    public void initPlayData(PlayItem item){
        LogUtil.d("TESTORDER","initPlayData(MediaViewControl.java:122)--Info-->>");
        bottomSeekBarControl.initData();
        mGestrueBaseHelper.showAndHideProgressBar(false);
    }

    public void setPlayer(PlayerInterface vooleStandardPlayer){
        this.mVooleStandardPlayer = vooleStandardPlayer;
    }
    public PlayerInterface getPlayer(){
        return  this.mVooleStandardPlayer;
    }

    public void openPreviewMode(){
        mGestrueBaseHelper.openPreviewMode();
    }

    public void offPreviewMode(){
        mGestrueBaseHelper.offPreviewMode();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("lichao" , "onTouch...........");
        mGestrueBaseHelper.setOnTouchEvent(event , false);
        return  true;
    }
    public void setRecycleOnItemClickListener(RightViewControl.OnItemClickListener onItemClickListener){
        mRightViewControl.setOnItemClickListener(onItemClickListener);
    }
}
