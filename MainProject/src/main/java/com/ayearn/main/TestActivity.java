package com.ayearn.main;

import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ayearn.db.generated.GreenDaoManager;
import com.ayearn.playerlib.VideoViewPlayer;
import com.ayearn.ui.dialog.AlertAppDiaLog;
import com.voole.utils.log.LogUtil;

import java.io.File;

public class TestActivity extends AppCompatActivity{
    private VideoViewPlayer videoViewPlayer;
    private Button button;
    private Button button1;
    private boolean isStop = false;
    String url = "http://10.5.6.162:8888/test.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        button = findViewById(R.id.test);
        button1 = findViewById(R.id.test1);
        GreenDaoManager.getInstance().init(this);
    }
    private void testDownlaod(){
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),"test.mp4");
        LogUtil.d("Main","testDownlaod(TestActivity.java:46)--Info-->>"+file.getAbsolutePath());
        //DownLoadManager.getInstance().startDownload(url,testIntercepter,downloadView);
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
