package com.voole.utils.file;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * File Util
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-9 下午 04:43
 */

public class FileUtil {
    /**
     * 设置文件权限
     * @param fileName
     */
    public static boolean setFilePermission(final String fileName) {
        boolean success = true;
        Process process = null;
        try {
            File file = new File(fileName);
            file.setExecutable(true, false);
            file.setReadable(true, false);
            file.setWritable(true, false);
            process = Runtime.getRuntime().exec("chmod 777" + fileName);
            // process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            if (process != null) {
                try {
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.getErrorStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    /**
     * 属性拷贝
     * @param from
     * @param to
     */
    public static void propertyCopy(Object from, Object to) {
        Field[] fields = from.getClass().getFields();
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i].getName();
            Field field;
            try {
                field = to.getClass().getField(name);
                field.set(to, fields[i].get(from));
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
