package com.voole.utils.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * MD5加密工具
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 下午 02:47
 */

public class MD5Util {
    /**
     *一般的字符串加密方法
     * @param content
     * @param charset
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sign(String content, String charset)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (content == null) {
            return "";
        }

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        messageDigest.update(content.getBytes(charset));

        byte[] byteArray = messageDigest.digest();
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                buffer.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                buffer.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return buffer.toString();
    }

    /**
     * 对数组进行加密
     * @param array
     * @param charset
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sign(String[] array, String charset)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = "";
            }
        }
        Arrays.sort(array);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length; i++) {
            buffer.append(array[i].trim());
        }
        return sign(buffer.toString(), charset);
    }
}
