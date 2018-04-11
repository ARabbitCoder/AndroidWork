package com.voole.utils.file;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

/**
 * Format Change
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 上午 11:03
 */

public class FormatUtil {
    /**
     * list转为String保存
     * @param SceneList
     * @return
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static String SceneList2String(List SceneList)
            throws IOException {
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
        objectOutputStream.writeObject(SceneList);
        // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
        String SceneListString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // 关闭objectOutputStream
        objectOutputStream.close();
        return SceneListString;
    }

    /**
     * stirng转为List
     * @param SceneListString
     * @return
     * @throws StreamCorruptedException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    public static List String2SceneList(String SceneListString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        List SceneList = (List) objectInputStream
                .readObject();
        objectInputStream.close();
        return SceneList;
    }

    /**
     * 根据InputStream得到返回的结果字符串
     * @param in 输入流
     * @return String
     */
    public static String convertStreamToString(InputStream in) {

        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        int n;
        if (in == null) {
            return null;
        } else {
            try {
                while ((n = in.read(b)) != -1) {
                    out.append(new String(b, 0, n));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out.toString();
        }
    }

    /**
     * 将字符串转为InputStream
     * @param str
     * @return InputStream
     */
    public static InputStream convertStrToInputStream(String str) {
        ByteArrayInputStream stream = null;
        if (str != null && str.length() > 0) {
            stream = new ByteArrayInputStream(str.getBytes());
        }
        return stream;
    }
}
