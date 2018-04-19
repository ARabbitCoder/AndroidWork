package com.ayearn.download;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.ayearn.db.generated.GreenDaoManager;
import com.ayearn.download.downloader.DownLoadManager;
import com.ayearn.download.downloader.DownloadViewIml;
import com.ayearn.download.m3u8download.ManageDownLoader;
import com.voole.utils.log.LogUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String url = "http://10.5.6.162:8888/test.mp4";
    String urltest = "http://10.5.6.162:8888/6.m3u8";
    private Thread thread;
    TestView1 testView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GreenDaoManager.getInstance().init(this);
        final File filep = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        final File file = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),"test.mp4");
        final DownloadView downloadView = findViewById(R.id.download_test);
        //ManageDownLoader.getInstance().startDownloadM3u8(urltest,filep.getAbsolutePath(),"","变形金刚");
        ManageDownLoader.getInstance().startDownloadMp4(url,file.getAbsolutePath(),"","","变形金刚");
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                M3u8FileParser praser = new M3u8FileParser();
                M3u8Info m3u8Info = praser.praserM3u8Url(urltest,filep.getAbsolutePath());
                ManageDownLoader.getInstance().startDownload(m3u8Info,filep.getAbsolutePath());
            }
        }).start();*/
        testView1 = findViewById(R.id.test_view);
        Button start = findViewById(R.id.startservice);
        final Button bind = findViewById(R.id.bindview);
        try {
            URL u = new URL(url);
            URL url1 = new URL(u,"./index.html");
            url1.getPath();
            LogUtil.d("TAG","onCreate(MainActivity.java:30)--Info-->>"+url1.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(MainActivity.this, DownLoadService.class);
                intent.putExtra("downloadurl",url);
                intent.putExtra("downloadpath",file.getAbsolutePath());
                intent.putExtra("filemd5","");
                intent.putExtra("filmname","变形金刚");
                MainActivity.this.startService(intent);*/
                TextView textView = new TextView(MainActivity.this);
                textView.setText("ceshi1");
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(layoutParams);
                testView1.addView(textView);
            }
        });
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownLoadManager.getInstance().tryBindDownloadView(url,downloadView);
            }
        });
        downloadView.setDownloadListenr(new DownloadView.DownloadListenr() {
            @Override
            public void startDownload(String downloadurl, File downloadfile, String filemd5, String filmname, DownloadViewIml iml) {
                DownLoadManager.getInstance().startDownload(url,downloadfile,filemd5,filmname,iml);
            }

            @Override
            public void stopDownload(String downloadurl) {
                DownLoadManager.getInstance().stopDownload(downloadurl);
            }

            @Override
            public void gotoPlay(String filepath) {

            }
        });

    }


}
