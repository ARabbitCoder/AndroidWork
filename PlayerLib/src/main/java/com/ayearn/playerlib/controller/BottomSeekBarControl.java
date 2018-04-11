package com.ayearn.playerlib.controller;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ayearn.playerlib.R;
import com.ayearn.playerlib.helper.GestrueBaseHelper;
import com.ayearn.playerlib.helper.HandlerHelper;
import com.voole.utils.log.LogUtil;
import com.voole.utils.time.TimeUtil;

/**
 * @author Created by lichao
 * @desc
 * @time 2018/1/8 18:17
 * 邮箱：lichao@voole.com
 */

public class BottomSeekBarControl  {
    private Context mContext ;
    private View mBottomView;
    private GestrueBaseHelper mGestrueBaseHelper;
    public SeekBar mSeekBar;
    private Button mPlayBtn;
    public TextView mStartTime ,mEndTime;
    public  MediaViewControl mediaViewControl;
    public BottomSeekBarControl(Context context, View bottomView, MediaViewControl mediaViewControl) {
        this.mContext = context;
        this.mBottomView = bottomView;
        this. mGestrueBaseHelper = mediaViewControl.mGestrueBaseHelper;
        this.mediaViewControl= mediaViewControl;
        initView();
        initListener();
    }

    public void initData() {
        LogUtil.d("lichao","initData(BottomSeekBarControl.java:39)--Info-->>");
        mEndTime.setText(TimeUtil.currentPostionToPlayTime((int) mediaViewControl.getPlayer().getDuration()));
        mStartTime.setText(TimeUtil.currentPostionToPlayTime((int) mediaViewControl.getPlayer().getCurrentPosition()));
        mSeekBar.setMax((int) mediaViewControl.getPlayer().getDuration());
        mediaViewControl.mHandlerHelper.sendEmptyMessage(HandlerHelper.SEEK_BAR_TIMER_UPDATE);
    }

    private void initListener() {
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaViewControl.mGestrueBaseHelper.startAndPause();
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("lichao","onStopTrackingTouch.....");
                mStartTime.setText(TimeUtil.currentPostionToPlayTime(seekBar.getProgress()));
                mediaViewControl.getPlayer().seek(seekBar.getProgress());
                mGestrueBaseHelper.mSeekBarControl.hiddenSeekbarCenterProgressLayout();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("lichao","onStopTrackingTouch.....");
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.d("lichao","onStopTrackingTouch.....progress is " +progress + " fromUser is " + fromUser);
                if (fromUser){
                    mGestrueBaseHelper.mSeekBarControl.showCurrentDragPlayTime(progress,true);
                    mStartTime.setText(TimeUtil.currentPostionToPlayTime(seekBar.getProgress()));
                }
            }
        });
    }

    private void initView() {
        mPlayBtn =mBottomView.findViewById(R.id.playBtn);
        mStartTime =mBottomView.findViewById(R.id.startTime);
        mEndTime =mBottomView.findViewById(R.id.endTime);
        mSeekBar =mBottomView.findViewById(R.id.seekbar);
    }

}
