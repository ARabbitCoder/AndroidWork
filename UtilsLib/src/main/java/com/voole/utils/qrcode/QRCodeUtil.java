package com.voole.utils.qrcode;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.voole.utils.log.LogUtil;

import java.util.Hashtable;

/**
 * 二维码生成工具类
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-9 下午 04:44
 */

public class QRCodeUtil {
    /**
     * 最小边界
     */
    private static final int PADDING_SIZE_MIN = 10;

    /**
     * 生成QR图
     * @param text  二维码文字描述
     * @param width
     * @param height
     * @return Bitmap图片
     */
    public static Bitmap createImage(String text, int width, int height) {
        Bitmap bitmap = null;
        int startX = 0;
        int startY = 0;
        try {
            QRCodeWriter writer = new QRCodeWriter();
            LogUtil.d("QRCodeUtil","gener:" + text);
            if (text == null || "".equals(text) || text.length() < 1) {
                return null;
            }

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE,
                    width, height, hints);

            boolean isFirstBlackPoint = false;

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        if (isFirstBlackPoint == false) {
                            isFirstBlackPoint = true;
                            startX = x;
                            startY = y;
                        }
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap
                    .createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (startX <= PADDING_SIZE_MIN) {
            return bitmap;
        }

        int x1 = startX - PADDING_SIZE_MIN;
        int y1 = startY - PADDING_SIZE_MIN;
        if (x1 < 0 || y1 < 0) {
            return bitmap;
        }
        int w1 = width - x1 * 2;
        int h1 = height - y1 * 2;
        Bitmap bitmapQR = Bitmap.createBitmap(bitmap, x1, y1, w1, h1);
        return bitmapQR;
    }
}
