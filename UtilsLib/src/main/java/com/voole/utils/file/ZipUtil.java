package com.voole.utils.file;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 下午 02:54
 */

public class ZipUtil {
    /**
     * 解压zip文件
     * @param src  zip文件的路径
     * @param dest	解压到的文件夹路径
     * @param handler	如果不需要异步线程通知，传递null
     * @author Mars
     */
    public static void unCompress(InputStream is, String dest, Handler handler){
        ZipInputStream zipStream=new ZipInputStream(is);
        try {
            File parentFile=new File(dest);
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            File fout=null;
            ZipEntry entry;
            byte[] buf=new byte[512];
            int len=-1;
            while((entry = zipStream.getNextEntry())!=null){
                try {
                    Message msg=null;
                    Bundle bundle=null;
                    if(handler!=null){
                        msg=handler.obtainMessage();
                        bundle=new Bundle();
                    }
                    if(entry.isDirectory()){
                        fout=new File(parentFile,entry.getName());
                        fout.mkdirs();
                    }else{
                        fout=new File(parentFile,entry.getName());
                        if(!fout.exists()){
                            (new File(fout.getParent())).mkdirs();
                        }
                        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(fout));
                        while((len=zipStream.read(buf))!=-1){
                            out.write(buf,0,len);
                        }
                        out.close();
                    }
                    if(handler!=null){
                        bundle.putString("state", "正在解压："+fout.getAbsolutePath());
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(zipStream!=null){
                try {
                    zipStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
