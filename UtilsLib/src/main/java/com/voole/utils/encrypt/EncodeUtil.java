package com.voole.utils.encrypt;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * 3DES加密工具
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 下午 02:51
 */

public class EncodeUtil {
    private static final String hexString = "0123456789ABCDEF";

    // 定义加密算法,可用DES,DESede,Blowfish
    private static final String DESede = "DESede";
    private static final String DES = "DES";

    /** 对指定的字节数组进行3DES加密
     * @param keybyte 加密密钥，长度为24字节
     * @param src 需要进行加密的源数据
     * @return 加密后的数据
     */
    public static byte[] encrypt3DES(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            SecretKeyFactory factory = SecretKeyFactory.getInstance(DESede);
            DESedeKeySpec keySpec = new DESedeKeySpec(keybyte);
            SecretKey key = factory.generateSecret(keySpec);

            //对源数据进行加密
            Cipher cipher = Cipher.getInstance(DESede);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** 对指定的字节数组进行DES加密
     * @param keybyte 加密密钥，长度为24字节
     * @param src 需要进行加密的源数据
     * @return 加密后的数据
     */
    public static byte[] encryptDES(byte[] keybyte, byte[] src) {
        try {
            //生成密钥
            //SecretKey deskey = new SecretKeySpec(keybyte, DES);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(DES);
            DESKeySpec keySpec = new DESKeySpec(keybyte);
            SecretKey key = factory.generateSecret(keySpec);

            //对源数据进行加密
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 兼容PHP解密算法
     *
     * @param encryptText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] desDecrypt(String key,byte[] encryptText ) {
        SecureRandom sr = new SecureRandom();
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey sKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, sKey, sr);
            byte encryptedData[] = encryptText;
            byte decryptedData[] = decryptedData = cipher.doFinal(encryptedData);
            return decryptedData;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /** 对指定的字节数组进行MD5加密
     * @param src 需要进行加密的源数据
     * @return 加密后的数据
     */
    public static byte[] encryptMD5(byte[] src) {
        byte[] result = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(src);
            result = digest.digest();
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将byte数组转换成十六进制字符串
     */
    public static String byte2hex(byte[] bytes) {
        // 根据默认编码获取字节数组
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        // 将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return sb.toString();
    }

    /**
     * 将十六进制字符串转换成byte数组
     */
    public static byte[] hex2byte(String bytes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2) {
            baos.write((hexString.indexOf(bytes.charAt(i)) << 4
                    | hexString.indexOf(bytes.charAt(i + 1))));
        }
        return baos.toByteArray();
    }

    /**
     * 字节填充补足
     * @param str    字符串
     * @param size   字节要求长度
     * @param fillByte 填充字符
     * @return
     */
    public static byte[] fillBytes(String str, int size, byte fillByte) {
        byte [] bytes = new byte [size];
        char [] chars = str.toCharArray();
        int length = chars.length;
        for (int i=0; i<bytes.length; i++) {
            if (i<length) {
                bytes[i] = (byte)chars[i];
            } else {
                bytes[i] = fillByte;
            }
        }
        return bytes;
    }
}
