package com.ayearn.download;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ayearn.ui.R;
import com.ayearn.download.downloader.DownloadViewIml;

import java.io.File;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-12 14:23
 */

public class DownloadView extends CardView implements DownloadViewIml{
    private TextView title;
    private ProgressBar progressBar;
    private ImageView controller;
    private TextView percentView;
    private TextView downloadstatus;
    public DownloadView(Context context) {
        super(context);
        initChildView(context);
    }

    public DownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initChildView(context);
    }

    public DownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initChildView(context);
    }

    private void initChildView(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View childview = inflater.inflate(R.layout.custom_progress_layout, this);
        title = childview.findViewById(R.id.progress_text);
        progressBar = childview.findViewById(R.id.custom_progress);
        controller = childview.findViewById(R.id.progress_controller);
        percentView = childview.findViewById(R.id.progress_percent);
        downloadstatus = childview.findViewById(R.id.progress_status);
        //addView(childview);
        initListener();
    }
    private boolean isDownloading = false;
    private boolean isRestartDownload = false;
    private void initListener(){
        if(controller!=null){
            controller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isDownloading){
                        setStatus(1);
                        if(downloadListenr!=null){
                            downloadListenr.stopDownload(cdownloadUrl);
                            isRestartDownload = false;
                        }
                    }else {
                        setStatus(2);
                        if(downloadListenr!=null){
                            downloadListenr.startDownload(cdownloadUrl,new File(cdownloadPath),cfileMd5,cdownloadName,DownloadView.this);
                            isRestartDownload = true;
                        }
                    }
                }
            });
        }
    }
    private String cdownloadUrl;
    private String cdownloadPath;
    private String cdownloadName;
    private String cfileMd5;
    @Override
    public void initDownloadView(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        cdownloadName = jsonObject.getString("downloadname");
        cfileMd5 = jsonObject.getString("filemd5");
        String percent = jsonObject.getString("downloadpercent");
        percent = percent==null?"0":percent;
        if(title!=null){
            title.setText(cdownloadName==null?"":cdownloadName);
        }
        if(percentView!=null){
            percentView.setText(percent+"%");
        }
        if(progressBar!=null){
            progressBar.setProgress(Integer.parseInt(percent));
        }
        int status = jsonObject.getIntValue("downloadstatus");
        this.cdownloadUrl = jsonObject.getString("downloadurl");
        this.cdownloadPath = jsonObject.getString("savefilepath");
        setStatus(status);
    }

    @Override
    public void setDownloadPercent(String percent) {
        if(progressBar!=null){
            progressBar.setProgress(Integer.parseInt(percent));
        }
        if(percentView!=null){
            percentView.setText(percent+"%");
        }
        if(downloadstatus!=null){
            downloadstatus.setText(R.string.downloading);
        }
    }
    @Override
    public void downloadSuccess() {
        if(downloadstatus!=null){
            downloadstatus.setText(R.string.downloadsuccess);
        }
        setStatus(3);
    }

    @Override
    public void downloadStoped() {
        if(downloadstatus!=null){
            downloadstatus.setText(R.string.downloadstop);
        }
        if(controller!=null){
            controller.setImageResource(R.drawable.start);
        }
    }

    @Override
    public void downloadFailed() {
        if(downloadstatus!=null){
            downloadstatus.setTextColor(Color.RED);
            downloadstatus.setText(R.string.downloadfailed);
        }
        setStatus(4);
    }

    /**
     * 1、暂停状态   2、下载状态  3、下载完成状态  4、下载失败状态
     * @param type
     */
    private void setStatus(int type){
        if(controller==null){
            return;
        }
        if(type==-1){
            controller.setImageResource(R.drawable.pause);
            downloadstatus.setText(R.string.downloading);
            isDownloading = true;
            if(downloadListenr!=null){
                downloadListenr.startDownload(cdownloadUrl,new File(cdownloadPath),cfileMd5,cdownloadName,DownloadView.this);
            }
        }
        if(type==1){
            if(!isRestartDownload){
                controller.setImageResource(R.drawable.start);
                downloadstatus.setText(R.string.downloadstop);
                isDownloading = false;
            }
        }
        if(type==2){
            controller.setImageResource(R.drawable.pause);
            downloadstatus.setText(R.string.downloading);
            isDownloading = true;
        }
        if(type==3){
            controller.setOnClickListener(null);
            controller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(downloadListenr!=null){
                        downloadListenr.gotoPlay(cdownloadPath);
                    }
                }
            });
            controller.setImageResource(R.drawable.play);
            percentView.setText("100%");
            progressBar.setProgress(100);
            downloadstatus.setText(R.string.downloadsuccess);
        }
        if(type==4){
            controller.setOnClickListener(null);
            controller.setImageResource(R.drawable.failed);
            downloadstatus.setTextColor(Color.RED);
            downloadstatus.setText(R.string.downloadfailed);
        }
    }

    private DownloadListenr downloadListenr;

    public void setDownloadListenr(DownloadListenr downloadListenr) {
        this.downloadListenr = downloadListenr;
    }

    public interface DownloadListenr{
        void startDownload(String downloadurl, File downloadfile, String filemd5, String filmname, DownloadViewIml iml);
        void stopDownload(String downloadurl);
        void gotoPlay(String filepath);
    }
}
