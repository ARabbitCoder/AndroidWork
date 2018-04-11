package com.ayearn.main;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ayearn.playerlib.VideoViewPlayer;
import com.ayearn.playerlib.base.PlayData;
import com.ayearn.playerlib.base.PlayItem;
import com.ayearn.playerlib.player.VideoPlayer;
import com.ayearn.ui.dialog.AlertAppDiaLog;
import com.voole.utils.log.LogUtil;
import com.voole.utils.net.NetCallback;
import com.voole.utils.net.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private VideoViewPlayer videoViewPlayer;
    private Button button;
    private boolean isStop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        button = findViewById(R.id.test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showDialog();
                isStop = true;
            }
        });
        /*videoViewPlayer = findViewById(R.id.video_view);
        List<PlayItem> list = new ArrayList<PlayItem>();
        for (int i=0;i<10;i++){
            PlayItem playItem = new PlayItem();
            playItem.setPlayUrl("http://10.5.6.10:8888/test.mp4");
            playItem.setContinueTime(0);
            list.add(playItem);
        }
        PlayData playData = new PlayData(list.get(0),list);
        videoViewPlayer.startPlayFilm(playData);*/
        //AlertAppDiaLog alertAppDiaLog =

        NetUtil.getInstance().downloadFileWithProgress("http://10.5.6.102:8888/test.mp4", getFilesDir().getAbsolutePath(), "test.mp4", true,new NetCallback() {
            @Override
            public void isUploading(int currentPercent) {

            }

            @Override
            public void isDownloading(int currentPercent) {
                LogUtil.d("liutest","isDownloading(TestActivity.java:56)--Info-->>"+currentPercent);
            }

            @Override
            public void isFailed(int errorCode) {

            }

            @Override
            public void isSuccessed(String result) {
                LogUtil.d("liutest","isSuccessed(TestActivity.java:69)--Info-->>"+result);
            }
        });
    }

    public void showDialog(){
        new AlertAppDiaLog.Builder(TestActivity.this).setCancelable(false).setTitle("test").setMessage("hhhhhhhhhhh").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(TestActivity.this,"ensure",Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }
}
