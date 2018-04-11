package com.voole.utils.device;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.voole.utils.base.StringUtil;
import com.voole.utils.log.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * get deviceInfo
 *
 * @author guo.rui.qing
 * @time 2017-11-9 下午 03:22
 */

public class DeviceUtil {
    /**
     * get wifi or net macAddress
     *
     * @param context
     * @return
     */
    public static String getMacAddressNew(Context context) {
        //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        if(isCarDevice){
            return getImsiMac(context);
        }
        String macAddress = null, ip = null;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        System.out.println(wifiMgr.getWifiState());
        if (wifiMgr.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                macAddress = info.getMacAddress();
            }
            System.out.println("mac:" + macAddress);
            String str2 = "";
            if (macAddress.contains(":")) {
                String s[] = macAddress.split(":");
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < s.length; i++) {
                    sb.append(s[i]);
                }
                String str1 = sb.toString();//拆分后转换回字符串
                str2 = str1.toUpperCase();
                System.out.println(str2);
            } else {
                str2 = macAddress.toUpperCase();
            }
            return str2;
        } else {
            return getMacAddress(false);
        }

    }

    public static String getWifiMacAddress(Context context) {
        String macAddress = null, ip = null;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        System.out.println("mac:" + macAddress);
        String str2 = "";
        if (macAddress.contains(":")) {
            String s[] = macAddress.split(":");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < s.length; i++) {
                sb.append(s[i]);
            }
            String str1 = sb.toString();//拆分后转换回字符串
            str2 = str1.toUpperCase();
            System.out.println(str2);
        } else {
            str2 = macAddress.toUpperCase();
        }
        return str2;
    }

    /**
     * get macAddress
     *
     * @return
     */
    public static String getMacAddress() {
       /* if(isCarDevice){
            LogUtil.e("DeviceUtil","CarDevice not support the method call setDeviceType(true) first then call getMacAddressNew try to get the MacAdresss--Error-->>");
            if(mContext != null) {
                return getImsiMac(mContext);
            } else {
                return "";
            }
        }
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if ("eth0".equals(intf.getName())) {
                    StringBuffer sb = new StringBuffer();
                    byte[] macBytes = intf.getHardwareAddress();
                    for (int i = 0; i < macBytes.length; i++) {
                        String sTemp = Integer.toHexString(0xFF & macBytes[i]);
                        if (sTemp.length() == 1) {
                            sb.append("0");
                        }
                        sb.append(sTemp);
                    }
                    return sb.toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;*/
       return "bc20bae38816";
    }

    /**
     * get macAddress of UpperCase
     *
     * @param separate
     * @return
     */
    public static String getMacAddress(boolean separate) {
        byte[] bytes = getHardwareAddress();
        if(bytes==null){
            return "";
        }
        StringBuffer hexBuffer = new StringBuffer();
        String temp = "";
        for (int n = 0; n < bytes.length; n++) {
            temp = (Integer.toHexString(bytes[n] & 0XFF));
            if (temp.length() == 1) {
                hexBuffer.append("0");
            }
            hexBuffer.append(temp.toUpperCase());
            if (separate) {
                hexBuffer.append(":");
            }
        }
        if (separate) {
            return hexBuffer.substring(0, hexBuffer.length() - 1);
        } else {
            return hexBuffer.toString();
        }
    }

    public static String getMacAddress(boolean separate, Context context) {
        if(isCarDevice){
            return getImsiMac(context);
        }
        byte[] bytes = getHardwareAddress();
        if (bytes == null) {
            return getMacAddressNew(context);
        }
        StringBuffer hexBuffer = new StringBuffer();
        String temp = "";
        for (int n = 0; n < bytes.length; n++) {
            temp = (Integer.toHexString(bytes[n] & 0XFF));
            if (temp.length() == 1) {
                hexBuffer.append("0");
            }
            hexBuffer.append(temp.toUpperCase());
            if (separate) {
                hexBuffer.append(":");
            }
        }
        if (separate) {
            return hexBuffer.substring(0, hexBuffer.length() - 1);
        } else {
            return hexBuffer.toString();
        }

    }

    private static byte[] getHardwareAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String s = intf.getName();
                if ("eth0".equals(intf.getName())) {
                    return intf.getHardwareAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get ipAddress
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreferenceIpAddress", ex.toString());
        }
        return null;
    }

    /**
     *  获得id加逗号 的id字符串.
     * @param idsStr String
     * @return String
     */
    public static String getIdsStr(String idsStr) {
        if (idsStr.endsWith(",")) {
            idsStr = idsStr.substring(0, idsStr.length() - 1);
        } else if (StringUtil.isNull(idsStr)) {
            idsStr = "0";
        }
        return idsStr;
    }

    /**
     * See if there is still room available
     *
     * @param context
     * @param updateSize
     * @return
     */
    public static boolean hasEnoughSpaceOnCache(Context context, long updateSize) {
        File path = context.getFilesDir();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return (updateSize < availableBlocks * blockSize);
    }

    /**
     * get sdk version
     *
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /**
     * 获取Android系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        String version = android.os.Build.VERSION.RELEASE;
        return version;
    }

    /**
     * HARDWARE 设备硬件名称
     *
     * @return
     */
    public static String getHardwareName() {
        try {
            String str = android.os.Build.HARDWARE + "";
            str = str.replaceAll(" ", "");
            str.trim();
            return str;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取手机的型号
     *
     * @return
     */
    public static String getModel() {
        try {
            String str = android.os.Build.MODEL + "";
            str = str.replaceAll(" ", "");
            str.trim();
            return str;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取CPN架构
     * @return
     */
    public static String getCpuAbi() {
        return  Build.CPU_ABI;
    }

    /**
     * 获取设备制造商
     * @return
     */
    public static String getMANUFACTURER() {
        return  Build.CPU_ABI;
    }



    /**
     * 正则表达式事例
     */
    static Pattern PATTERN = Pattern.compile("([a-zA-Z]+):\\s*(\\d+)");

    /**
     * 获取内存大小
     *
     * @return
     */
    public static String getMemorySize() {
        String memorySize = null;
        String line;
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            while ((line = reader.readLine()) != null) {
                Matcher m = PATTERN.matcher(line);
                if (m.find()) {
                    String name = m.group(1);
                    String size = m.group(2);

                    if ("MemTotal".equalsIgnoreCase(name)) {
                        memorySize = size;
                    }
                }
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return memorySize;
    }

    /**
     * 手机系统的OpenGL版本
     *
     * @param context
     * @return
     */
    public static String getOpenglVersion(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();
        return configurationInfo.getGlEsVersion();
    }

    /**
     * 获取cpu
     *
     * @return
     */
    public static int getNumberOfCores() {
        if (Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        } else {
            // Use saurabh64's answer
            return getNumCoresOldPhones();
        }
    }

    /**
     * Gets the number of cores available in this device, across all processors.
     * Requires: Ability to peruse the filesystem at "/sys/devices/system/cpu"
     * @return The number of cores, or 1 if failed to get result
     */
    private static int getNumCoresOldPhones() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }
        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    /**
     * 获取系统配置文件
     * @param property
     * @param defaultvalue
     * @return
     */
    public static String getSystemProperties(String property, String defaultvalue) {
        String value;
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", new Class[]{java.lang.String.class});
            value = (String) method.invoke(null, new Object[]{property});
            if (value != null && value.length() > 0) {
                return value;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return defaultvalue;
    }

    /**
     * link是32还是64
     * @return
     */
    public static String isLinker64() {
        try {
            File f = new File("/system/bin/linker64");
            if (!f.exists()) {
                return "32";
            }
        } catch (Exception e) {
            return "32";
        }
        return "64";
    }

    /**
     * 获取手机号
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
            return null;
        } else {
            return tm.getLine1Number();
        }
    }

    /**
     * 获取20位 ICCID
     * 前六位运营商代码，剩余14位
     * 898601 14261105295267
     * @param context
     * @return
     */
    public static String getIccIdNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        } else {
            return tm.getSimSerialNumber();  //取出ICCID;
        }
    }

    /**
     * @desc imsi共15位，截取后面12位作为mac地址使用
     * @param context
     * @return 016247731210
     */
    public static String getImsiMac(Context context) {
        String imsiMac = "";
        try {
            String imsiNumber = getImsiNumber(context);
            if(!TextUtils.isEmpty(imsiNumber)) {
                imsiMac = imsiNumber.substring(3);
            } else {
                LogUtil.d("DeviceUtil","getImsiMac(DeviceUtil.java:497)--Info-->>imsiNumber is null ");
            }
        } catch (Exception e) {
            LogUtil.d("DeviceUtil","getImsiMac(DeviceUtil.java:497)--Info-->>imsiNumber is length Less than 3 ");
            e.printStackTrace();
        }
        return imsiMac;
    }

    /**
     *  SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile Subscriber Identification Number）是区别移动用户的标志，
     * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
     * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
     * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
     * 唯一地识别国内GSM移动通信网中移动客户。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
     * 获取 IMSI 号
     * 前3位代表国家
     * 15位 剩余 12位
     * 460 014176215251
     */
    private static String getImsiNumber(Context context) {
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        } else {
            String imsi = telManager.getSubscriberId();
            return imsi;
        }
    }

    public static boolean  isCarDevice = false;
    public static void setDeviceType(boolean isCar){
        isCarDevice = isCar;
    }
    private static  Context mContext;
    public static void setContext(Context context) {
        mContext = context.getApplicationContext();
    }
    public static String getSequenceNo() {
        String sequenceno = null;
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);
        int mYear = mCalendar.get(Calendar.YEAR);
        int mHour = mCalendar.get(Calendar.HOUR);
        int mMinutes = mCalendar.get(Calendar.MINUTE);
        int mSeconds = mCalendar.get(Calendar.SECOND);
        int mMiliSeconds = mCalendar.get(Calendar.MILLISECOND);
        sequenceno = String.format("%s_%4d%02d%02d%02d%07d", "10000101", mYear,
                mHour, mMinutes, mSeconds, mMiliSeconds);
        return sequenceno;
    }
    public static String getAppVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    public static int getAppVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }
    public static String getAppName(Context context) {
        return context.getPackageName();
    }
    public static void installApk(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
    public static void setCurrentPlayerMute(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if (getCurrentPlayerVolume(context) <= 0) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        } else {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
    }
    public static void setCurrentPlayerMute(Context context, boolean b) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, b);
    }
    public static int getCurrentSystemVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
    }
    public static int getCurrentPlayerVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
    public static void increasePlayerVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if (getCurrentPlayerVolume(context) <= 0) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }
    public static void decreasePlayerVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        if (getCurrentPlayerVolume(context) <= 0) {
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
    }
    public static void setPlayerVolume(Context context, int v) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, v, 0);
    }

    public static void setSystemVolume(Context context, int v) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, v, 0);
    }

    public static int getPlayerMaxVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getSystemMaxVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getApplicationContext()
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
    }

    public static void setPlayerMaxVolume(Context context) {
        int max = getPlayerMaxVolume(context);
        setPlayerVolume(context, max);
    }

    public static void setSystemMaxVolume(Context context) {
        int max = getSystemMaxVolume(context);
        setSystemVolume(context, max);
    }

    public static String findPidOfAgent(String pName) {
        String pid;
        String temp;
        pid = "";
        try {
            // String[] cmd = { "/bin/sh", "-c", "ps > /dev/null 2>&1" };
            // Process p = Runtime.getRuntime().exec(cmd);
            Process p = Runtime.getRuntime().exec("ps");
            p.waitFor();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            while ((temp = stdInput.readLine()) != null) {
                if (temp.contains(pName)) {
                    String[] cmdArray = temp.split(" +");
                    // for (int i = 0; i < cmdArray.length; i++) {
                    // Log.d("DDDDDDDDDDD", "loop i=" + i + " => " +
                    // cmdArray[i]);
                    // }
                    pid = cmdArray[1];
                }
            }
        } catch (IOException e) {
            Log.d("VooleEpg", "DeviceUtil-->findPidOfAgent-->" + e.toString());
        } catch (InterruptedException e) {
            Log.d("VooleEpg", "DeviceUtil-->findPidOfAgent-->" + e.toString());
        }
        return pid;
    }

    public static boolean checkPackageExist(Context context, String packageName) {
        if (packageName == null || packageName.equals("")) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager()
                    .getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            if (info != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    public static void setFileToPermission(final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process process = null;
                try {
                    process = Runtime.getRuntime()
                            .exec("chmod 777 " + fileName);
                    process.waitFor();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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

            }
        }).start();
    }

    public static String getCloudUUID() {
        try {
            Class<?> cloudUuid = Class
                    .forName("com.yunos.baseservice.clouduuid.CloudUUID");
            Method m = cloudUuid.getMethod("getCloudUUID");
            String result = (String) m.invoke(null);
            return result;
        } catch (Exception e) {
            return "false";
        }

    }

    public static String getIMEI(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Activity.TELEPHONY_SERVICE);
        if (PackageManager.PERMISSION_GRANTED == context.getPackageManager()
                .checkPermission(Manifest.permission.READ_PHONE_STATE,
                        context.getPackageName())) {
            return manager.getDeviceId();
        } else {
            return null;
        }
    }

    public static boolean copyFileFromAssets(Context context, String fileName,
                                             String path) {
        boolean copyIsFinish = false;
        try {
            File file = new File(path + "/" + fileName);
            if (file.exists()) {
                return true;
            }
            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024 * 4];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static boolean isInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        int len = pinfo.size();
        for (int i = 0; i < len; i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    private String getCPUSerialNumber() {
        ProcessBuilder cmd;
        String cpuInfo = "";
        String result = "";
        int serialIndex = -1;
        final int CPU_SERIAL_NUM = 17;

        try {
            String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];

            while (in.read(re) != -1) {
                cpuInfo = cpuInfo + new String(re);
            }

            serialIndex = cpuInfo.indexOf("Serial");
            serialIndex = cpuInfo.indexOf(": ", serialIndex) + 2;
            result = cpuInfo.substring(serialIndex, serialIndex
                    + CPU_SERIAL_NUM);
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        result = result.replace("\n", "");
        result = result.replace("\r", "");

        // 去掉可能的无效CPU序列号
		/*
		 * if (result.matches(REGEX_ONE_MORE_ZERO)) { return null; }
		 */

        return result;
    }


}
