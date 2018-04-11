package com.ayearn.playerlib.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayearn.playerlib.R;
import com.voole.utils.log.LogUtil;


/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/9 10:26
 * 邮箱：lichao@voole.com
 */

public class TopViewControl {
    private static  final  String TAG = TopViewControl.class.getName();
    public Context mContext;
    public View mView;
    public MediaViewControl mediaViewControl;
    public RelativeLayout mVideoViewExit;
    public TextView mFilmName;
    public Button mChanelMenu;

    public TopViewControl(Context context, View view, MediaViewControl mediaViewControl) {
        this.mContext = context;
        this.mView = view;
        this.mediaViewControl = mediaViewControl;
        initView();
        setListener();
    }

    private void setListener() {
        mVideoViewExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity)mContext;
                LogUtil.d(TAG,"onClick(TopViewControl.java:43)--Info-->>");
                activity.finish();
            }
        });
        mChanelMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaViewControl.mRightViewControl.showAndHideRecycleView();
            }
        });
    }
    private void initView() {
        mVideoViewExit= (RelativeLayout) mView.findViewById(R.id.videoview_exit);
        mFilmName= (TextView) mView.findViewById(R.id.filmName);
        mChanelMenu = (Button) mView.findViewById(R.id.channelMenu);
    }

    public void hideSelect(){
        mChanelMenu.setVisibility(View.GONE);
    }

}
