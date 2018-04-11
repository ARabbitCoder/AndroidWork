package com.voole.utils.device;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.DecimalFormat;

/**
 * sd卡管理工具
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-10 下午 02:58
 */

public class StorageUtil {
    /**
     * SDCARD是否可用
     * */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取设备内部总共存储空间
     * */
    @SuppressWarnings("deprecation")
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }

    /**
     *  获取设备内部剩余存储空间
     * */
    @SuppressWarnings("deprecation")
    public static long getFreeInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
    }

    /**
     *  获取SDCARD总共存储空间
     *  */
    @SuppressWarnings("deprecation")
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();
            return totalBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取SDCARD剩余存储空间
     * */
    @SuppressWarnings("deprecation")
    public static long getFreeExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return availableBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 获取显示空间的文字，当空间小于1GB时，以MB显示，否则以GB显示，依此类推
     * */
    public static String getSpaceText(long space) {
        DecimalFormat formatter = new DecimalFormat("0.00");
        String spaceText = null;
        String unit = "B";
        double usingCount = space;

        if (usingCount / 1024.0 > 1) {
            usingCount = usingCount / 1024;
            unit = "KB";
        }

        if (usingCount / 1024.0 > 1) {
            usingCount = usingCount / 1024;
            unit = "MB";
        }

        if (usingCount / 1024.0 > 1) {
            usingCount = usingCount / 1024;
            unit = "GB";
        }

        if (usingCount / 1024.0 > 1) {
            usingCount = usingCount / 1024;
            unit = "TB";
        }

        spaceText = formatter.format(usingCount) + unit;
        return spaceText;
    }
}
