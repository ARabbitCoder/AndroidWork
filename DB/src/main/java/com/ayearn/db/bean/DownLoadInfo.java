package com.ayearn.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-4-12 15:20
 */
@Entity(createInDb = true,nameInDb = "downloadinfo")
public class DownLoadInfo {
    @Id
    private long id;
    @NotNull
    private String downloadurl;
    @NotNull
    private String downloadid;
    @NotNull
    private String downloadname;
    @NotNull
    private int downloadstatus;
    @NotNull
    private String downloadpercent;
    @NotNull
    private boolean isdownloadcomplete;
    public boolean getIsdownloadcomplete() {
        return this.isdownloadcomplete;
    }
    public void setIsdownloadcomplete(boolean isdownloadcomplete) {
        this.isdownloadcomplete = isdownloadcomplete;
    }
    public int getDownloadstatus() {
        return this.downloadstatus;
    }
    public void setDownloadstatus(int downloadstatus) {
        this.downloadstatus = downloadstatus;
    }
    public String getDownloadname() {
        return this.downloadname;
    }
    public void setDownloadname(String downloadname) {
        this.downloadname = downloadname;
    }
    public String getDownloadid() {
        return this.downloadid;
    }
    public void setDownloadid(String downloadid) {
        this.downloadid = downloadid;
    }
    public String getDownloadurl() {
        return this.downloadurl;
    }
    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getDownloadpercent() {
        return this.downloadpercent;
    }
    public void setDownloadpercent(String downloadpercent) {
        this.downloadpercent = downloadpercent;
    }
    @Generated(hash = 294701889)
    public DownLoadInfo(long id, @NotNull String downloadurl,
            @NotNull String downloadid, @NotNull String downloadname,
            int downloadstatus, @NotNull String downloadpercent,
            boolean isdownloadcomplete) {
        this.id = id;
        this.downloadurl = downloadurl;
        this.downloadid = downloadid;
        this.downloadname = downloadname;
        this.downloadstatus = downloadstatus;
        this.downloadpercent = downloadpercent;
        this.isdownloadcomplete = isdownloadcomplete;
    }
    @Generated(hash = 1743687477)
    public DownLoadInfo() {
    }

}
