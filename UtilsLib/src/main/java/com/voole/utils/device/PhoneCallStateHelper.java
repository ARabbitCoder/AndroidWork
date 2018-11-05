package com.voole.utils.device;
import android.content.Context;
import android.os.Looper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author:liujingwei
 * date:2018/10/29 下午6:03
 */
public class PhoneCallStateHelper extends PhoneStateListener {
    private final int STATUS_NORMAL = 0;
    /**
     * 来电
     */
    private final int STATUS_CALLING = 1;
    /**
     * 接起
     */
    private final int STATUS_HOOKUP = 2;
    /**
     * 响铃时挂断
     */
    private int currentStatus = 0;
    private Map<String, PhoneListener> phoneCallListenerMap = new ConcurrentHashMap<>();
    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE: /* 无任何状态时 */
                callOrEnd(STATUS_NORMAL);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: /* 接起电话时 */
                callOrEnd(STATUS_HOOKUP);
                break;
            case TelephonyManager.CALL_STATE_RINGING: /* 电话进来时 */
                callOrEnd(STATUS_CALLING);
                break;
            default:
                break;

        }

    }

    /**
     * must be called on UI thread
     * 开始监听手机电话状态
     *
     * @param key
     * @param listener
     */
    public void beginListenerPhoneStatus(String key, Context context,PhoneListener listener) {
        if (null == context) {
            return;
        }
        if (phoneCallListenerMap.size() > 0) {
            if (null != listener) {
                phoneCallListenerMap.put(key, listener);
            }
        } else {
            if (null != listener) {
                phoneCallListenerMap.put(key, listener);
            }
            if (Looper.getMainLooper() == Looper.myLooper()) {
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telManager.listen(this, PhoneStateListener.LISTEN_CALL_STATE);
            } else {
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telManager.listen(PhoneCallStateHelper.this, PhoneStateListener.LISTEN_CALL_STATE);
            }
        }
    }

    /**
     * 停止监听手机电话状态
     *
     * @param key
     */
    public void stopListenPhoneStatus(String key,Context context) {
        if (null == context) {
            return;
        }
        if (null != phoneCallListenerMap) {
            phoneCallListenerMap.remove(key);
            //当app层没有任何地方关心电话状态时，则取消系统的电话监听
            if (phoneCallListenerMap.size() == 0) {
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telManager.listen(this, PhoneStateListener.LISTEN_NONE);
            }
        }
    }


    /**
     * 遍历监听集合，进行一一回调
     *
     * @param callOrEnd 电话状态，打进来还是挂断，STATUS_CALLING：打进来 STATUS_NORMAL：挂断
     */
    private void callOrEnd(int callOrEnd) {
        if (null == phoneCallListenerMap) {
            return;
        }
        for (String key : phoneCallListenerMap.keySet()) {
            PhoneListener listener = phoneCallListenerMap.get(key);
            if (null != listener) {
                if(callOrEnd==STATUS_NORMAL){
                    if(currentStatus==STATUS_NORMAL){
                        listener.onPhoneNormal();
                    }
                    if(currentStatus==STATUS_CALLING){
                        listener.onPhoneCallingHangDown();
                    }
                    if(currentStatus==STATUS_HOOKUP){
                        listener.onPhoneHangDown();
                    }
                }
                if(callOrEnd==STATUS_CALLING){
                    currentStatus = STATUS_CALLING;
                    listener.onPhoneCall();
                }
                if (callOrEnd == STATUS_HOOKUP) {
                    currentStatus=STATUS_HOOKUP;
                    listener.onPhoneHangUp();
                }
            }
        }
        if(callOrEnd==STATUS_NORMAL){
            currentStatus=STATUS_NORMAL;
        }
    }


    public interface PhoneListener {
        /**
         * 当电话打进来响玲
         */
        void onPhoneCall();

        /**
         * 当接通后电话挂断
         */
        void onPhoneHangDown();

        /**
         * 响铃时挂断或者未接通
         */
        void onPhoneCallingHangDown();

        /**
         * 当电话接起
         */
        void onPhoneHangUp();

        /**
         * 当没有活动时
         */
        void onPhoneNormal();

    }
}

