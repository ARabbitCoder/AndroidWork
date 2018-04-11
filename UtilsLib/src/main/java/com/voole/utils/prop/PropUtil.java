package com.voole.utils.prop;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *  Program configuration file management tool
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-9 下午 04:44
 */

public class PropUtil {
    private static Properties prop = null;

    /**
     * 加载assets下的voole.properties文件
     *
     * @param context
     */
    private static void propertiesInstance(Context context) {
        if (prop == null) {
            InputStream in = null;
            try {
                in = context.getAssets().open("voole.properties");
                prop = new Properties();
                prop.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 加载assets下的指定的配置文件
     * @param context
     * @param filePath
     */
    public static void propertiesInstance(Context context, String filePath) {
        if (prop == null) {
            InputStream in = null;
            try {
                in = context.getAssets().open(filePath);
                prop = new Properties();
                prop.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * get assets voole.properties file value of key
     *
     * @param context
     * @param name
     * @return
     */
    public static String getProperty(Context context, String name) {
        if(prop == null){
            propertiesInstance(context);
        }
        return prop==null?null:(prop.getProperty(name.trim()) == null?null:prop.getProperty(name.trim()).trim());
    }

    /**
     * 获取assets下指定的文件的某个key的value
     * @param context
     * @param filePath
     * @param name
     * @return
     */
    public static String getProperty(Context context,String filePath, String name) {
        if(prop == null){
            propertiesInstance(context,filePath);
        }
        return prop==null?null:(prop.getProperty(name.trim()) == null?null:prop.getProperty(name.trim()).trim());
    }

    /**
     * Add key value pair
     * @param context
     * @param key
     * @param value
     */
    public static void setProperty(Context context, String key, String value) {
        if(prop == null){
            propertiesInstance(context);
        }
        try {
            OutputStream out = context.openFileOutput("voole.propertie",
                    Context.MODE_PRIVATE);
            Enumeration<?> e = prop.propertyNames();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String s = (String) e.nextElement();
                    if (!s.equals(key)) {
                        prop.setProperty(s, prop.getProperty(s));
                    }
                }
            }
            prop.setProperty(key, value);
            prop.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加键值对到assets下指定的文件
     * @param context
     * @param filePath
     * @param key
     * @param value
     */
    public static void setProperty(Context context,String filePath,String key, String value) {
        if(prop == null){
            propertiesInstance(context,filePath);
        }
        try {
            OutputStream out = context.openFileOutput(filePath, Context.MODE_PRIVATE);
            Enumeration<?> e = prop.propertyNames();
            if (e.hasMoreElements()) {
                while (e.hasMoreElements()) {
                    String s = (String) e.nextElement();
                    if (!s.equals(key)) {
                        prop.setProperty(s, prop.getProperty(s));
                    }
                }
            }
            prop.setProperty(key, value);
            prop.store(out, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * load Config
     * @param filePath
     * @return
     */
    public static Properties loadConfig(String filePath){
        Properties properties = new Properties();
        InputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            properties.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }

    /**
     * save Config
     * @param filePath
     * @return
     */
    public static void saveConfig(String filePath, Properties properties){
        try {
            FileOutputStream fos = new FileOutputStream(filePath, false);
            properties.store(fos, "");
            fos.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
