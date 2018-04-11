package com.voole.utils.device;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
/**
 * 手机信息
 * @version V1.0
 * @author guo.rui.qing
 * @time 2013-4-17下午4:10:29
 */
public class PhoneInfoUtil {

	private static final String TAG = PhoneInfoUtil.class.getSimpleName();

	/**
	 *内存信息文件
	 */
	private static final String FILE_MEMORY = "/proc/meminfo";

	/**
	 *CPU信息文件
	 */
	private static final String FILE_CPU = "/proc/cpuinfo";

	/**
	 *手机串号
	 */
	public String mIMEI;

	/**
	 *手机网络制式
	 */
	public int mPhoneType;

	/**
	 *用户系统SDK版本
	 */
	public int mSysVersion;

	/**
	 *移动国家代码
	 */
	public String mNetWorkCountryIso;

	/**
	 *电话网络运营商
	 */
	public String mNetWorkOperator;

	/**
	 *运营商名称
	 */
	public String mNetWorkOperatorName;

	
	/**
	 *当前网络制式 
	 */
	public int mNetWorkType;

	/**
	 *是否离线状态
	 */
	public boolean mIsOnLine;

	
	/**
	 *网络连接类型名称 
	 */
	public String mConnectTypeName;
	/**
	 *剩余内存
	 */
	public long mFreeMem;

	/**
	 *总内存
	 */
	public long mTotalMem;

	/**
	 *CPU信息
	 */
	public String mCupInfo;

	/**
	 *产品名称
	 */
	public String mProductName;
	/**
	 *型号
	 */
	public String mModelName;
	/**
	 *制造商
	 */
	public String mManufacturerName;

	private PhoneInfoUtil() {
	}
	/**
	 *  获取手机串号
	 * @param context
	 * @return String
	 */
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
	/**
	 *或其手机网络制式
	 * @param context
	 * @return int
	 */
	public static int getPhoneType(Context context) {

		TelephonyManager manager = (TelephonyManager) context

		.getSystemService(Activity.TELEPHONY_SERVICE);

		return manager.getPhoneType();

	}
	/**
	 *  获取用户sdk版本
	 * @return int
	 */
	public static int getSysVersion() {

		return Build.VERSION.SDK_INT;

	}
	/**
	 *  获取国家移动代码
	 * @param context
	 * @return String
	 */
	public static String getNetWorkCountryIso(Context context) {

		TelephonyManager manager = (TelephonyManager) context

		.getSystemService(Activity.TELEPHONY_SERVICE);

		return manager.getNetworkCountryIso();

	}

	/**
	 *  获取电话运营商
	 * @param context
	 * @return String
	 */
	public static String getNetWorkOperator(Context context) {

		TelephonyManager manager = (TelephonyManager) context

		.getSystemService(Activity.TELEPHONY_SERVICE);

		return manager.getNetworkOperator();

	}
	/**
	 * 获取运营商名称
	 * @param context
	 * @return String
	 */
	public static String getNetWorkOperatorName(Context context) {

		TelephonyManager manager = (TelephonyManager) context

		.getSystemService(Activity.TELEPHONY_SERVICE);

		return manager.getNetworkOperatorName();

	}

	/**
	 * 获取当前网络制式 
	 * @param context
	 * @return int
	 */
	public static int getNetworkType(Context context) {

		TelephonyManager manager = (TelephonyManager) context

		.getSystemService(Activity.TELEPHONY_SERVICE);

		return manager.getNetworkType();

	}

	/**
	 * 判断是否离线 
	 * @param context
	 * @return boolean
	 */
	public static boolean isOnline(Context context) {

		ConnectivityManager manager = (ConnectivityManager) context

		.getSystemService(Activity.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {

			return true;

		}

		return false;

	}
	/**
	 *  获取网络连接类型名称
	 * @param context
	 * @return String
	 */
	public static String getConnectTypeName(Context context) {

		if (!isOnline(context)) {

			return "OFFLINE";

		}

		ConnectivityManager manager = (ConnectivityManager) context

		.getSystemService(Activity.CONNECTIVITY_SERVICE);

		NetworkInfo info = manager.getActiveNetworkInfo();

		if (info != null) {

			return info.getTypeName();

		} else {

			return "OFFLINE";

		}

	}
	/**
	 * 获取剩余内存
	 * @param context
	 * @return long
	 */
	public static long getFreeMem(Context context) {

		ActivityManager manager = (ActivityManager) context

		.getSystemService(Activity.ACTIVITY_SERVICE);

		MemoryInfo info = new MemoryInfo();

		manager.getMemoryInfo(info);

		long free = info.availMem / 1024 / 1024;

		return free;

	}

	/**
	 *  获取总内存
	 * @param context
	 * @return long
	 */
	public static long getTotalMem(Context context) {

		try {

			FileReader fr = new FileReader(FILE_MEMORY);

			BufferedReader br = new BufferedReader(fr);

			String text = br.readLine();

			String[] array = text.split("\\s+");

			Log.w(TAG, text);

			return Long.valueOf(array[1]) / 1024;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return -1;

	}
	/**
	 *  获取CPU信息
	 * @return String
	 */
	public static String getCpuInfo() {

		try {

			FileReader fr = new FileReader(FILE_CPU);

			BufferedReader br = new BufferedReader(fr);

			String text = br.readLine();

			String[] array = text.split(":\\s+", 2);

			for (int i = 0; i < array.length; i++) {

				Log.w(TAG, " .....  " + array[i]);

			}

			Log.w(TAG, text);

			return array[1];

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 *  获取产品名称
	 * @return String
	 */
	public static String getProductName() {

		return Build.PRODUCT;

	}
	/**
	 *  获取型号
	 * @return String
	 */
	public static String getModelName() {

		return Build.MODEL;

	}
	/**
	 *  获取生产商 
	 * @return String
	 */
	public static String getManufacturerName() {

		return Build.MANUFACTURER;

	}
	public static PhoneInfoUtil getPhoneInfo(Context context) {

		PhoneInfoUtil result = new PhoneInfoUtil();

		result.mIMEI = getIMEI(context);

		result.mPhoneType = getPhoneType(context);

		result.mSysVersion = getSysVersion();

		result.mNetWorkCountryIso = getNetWorkCountryIso(context);

		result.mNetWorkOperator = getNetWorkOperator(context);

		result.mNetWorkOperatorName = getNetWorkOperatorName(context);

		result.mNetWorkType = getNetworkType(context);

		result.mIsOnLine = isOnline(context);

		result.mConnectTypeName = getConnectTypeName(context);

		result.mFreeMem = getFreeMem(context);

		result.mTotalMem = getTotalMem(context);

		result.mCupInfo = getCpuInfo();

		result.mProductName = getProductName();

		result.mModelName = getModelName();

		result.mManufacturerName = getManufacturerName();

		return result;

	}
	/**
	 *  dip转px
	 * @param context
	 * @param dipValue
	 * @return int
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 *  px转dip 
	 * @param context
	 * @param pxValue
	 * @return int
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();

		builder.append("IMEI : " + mIMEI + "\n");

		builder.append("mPhoneType : " + mPhoneType + "\n");

		builder.append("mSysVersion : " + mSysVersion + "\n");

		builder.append("mNetWorkCountryIso : " + mNetWorkCountryIso + "\n");

		builder.append("mNetWorkOperator : " + mNetWorkOperator + "\n");

		builder.append("mNetWorkOperatorName : " + mNetWorkOperatorName + "\n");

		builder.append("mNetWorkType : " + mNetWorkType + "\n");

		builder.append("mIsOnLine : " + mIsOnLine + "\n");

		builder.append("mConnectTypeName : " + mConnectTypeName + "\n");

		builder.append("mFreeMem : " + mFreeMem + "M\n");

		builder.append("mTotalMem : " + mTotalMem + "M\n");

		builder.append("mCupInfo : " + mCupInfo + "\n");

		builder.append("mProductName : " + mProductName + "\n");

		builder.append("mModelName : " + mModelName + "\n");

		builder.append("mManufacturerName : " + mManufacturerName + "\n");

		return builder.toString();

	}



    public static String getMacAddressNew(Context context){
        //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
        String macAddress = null, ip = null;
        WifiManager wifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        System.out.println(wifiMgr.getWifiState());
        if(wifiMgr.getWifiState()==WifiManager.WIFI_STATE_ENABLED){
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                macAddress = info.getMacAddress();
            }
            System.out.println("mac:" + macAddress );
            String str2 = "";
            if(macAddress.contains(":")){
                String s[] = macAddress.split(":");
                StringBuffer sb = new StringBuffer();
                for(int i=0;i<s.length;i++){
                    sb.append(s[i]);
                }
                String str1 = sb.toString();//拆分后转换回字符串
                str2 = str1.toUpperCase();
                System.out.println(str2);
            }else{
                str2 = macAddress.toUpperCase();
            }
            return str2;
        }else{
            return getMacAddress(false);
        }

    }

    /**
     * 返回硬件地址
     * @return string
     */
    public static String getMacAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().equals("eth0")) {
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
        return null;
    }
    /**
     * 返回硬件地址大写
     * @param separate
     * @return string 
     */
    public static String getMacAddress(boolean separate) {
        byte[] bytes = getHardwareAddress();
        StringBuffer hexBuffer = new StringBuffer();
        String temp = "";
        for (int n=0; n<bytes.length; n++) {
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
            return hexBuffer.substring(0, hexBuffer.length()-1);
        } else {
            return hexBuffer.toString();
        }
    }

	/**
	 * 获取mac地址
	 * @param separate 是否添加：分隔符
	 * @param context
	 * @return
	 */
    public static String getMacAddress(boolean separate,Context context)  {
        byte[] bytes = getHardwareAddress();
        if(bytes==null){
            return getMacAddressNew(context);
        }
        StringBuffer hexBuffer = new StringBuffer();
        String temp = "";
        for (int n=0; n<bytes.length; n++) {
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
            return hexBuffer.substring(0, hexBuffer.length()-1);
        } else {
            return hexBuffer.toString();
        }

    }

	/**
	 * 获取MAC地址
	 * @return byte类型
	 */
	private static byte[] getHardwareAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String s = intf.getName();
                if (intf.getName().equals("eth0")) {
                    return intf.getHardwareAddress();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
      }
        return null;
    }

    /**
     * 返回Ip地址
     * @return string
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString().trim();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IpAddress", ex.toString());
        }
        return null;
    }


}
