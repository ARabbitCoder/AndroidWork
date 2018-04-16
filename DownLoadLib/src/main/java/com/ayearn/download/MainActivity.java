package com.ayearn.download;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ayearn.db.generated.GreenDaoManager;
import com.ayearn.download.downloader.DownLoadManager;
import com.ayearn.download.downloader.DownloadViewIml;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    String url = "http://10.5.6.162:8888/test.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GreenDaoManager.getInstance().init(this);
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MOVIES),"test.mp4");
        DownloadView downloadView = findViewById(R.id.download_test);
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
        DownLoadManager.getInstance().startDownload(url,file,"","变形金刚",downloadView);
    }
}
