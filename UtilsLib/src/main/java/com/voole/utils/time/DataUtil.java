package com.voole.utils.time;

/**
 * 日期转换类工具
 * @author guo.rui.qing
 * @desc
 * @time 2017-11-9 下午 04:45
 */

public class DataUtil {
    /**
     * 将时间秒转换成时分秒格式
     * @param second
     * @return hh:mm:ss格式时间字符串
     */
    public static String secondToString(int second) {
        int s = second % 60;
        int m = second / 60 % 60;
        int h = second / 60 / 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
                + (s < 10 ? "0" + s : s);
    }

    /**
     * 将字符串时间转换成int时间（秒）
     * @param time
     * @return
     */
    public static int stringToSecond(String time){
        if(time == null || "".equals(time)) {
            return 0;
        }
        try {
            String[] split = time.split(":");
            int h = Integer.parseInt(split[0].trim()) * 60 * 60;
            int m = Integer.parseInt(split[1].trim()) * 60;
            int s = Integer.parseInt(split[2].trim());
            return h + m + s;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     *  把数字变为时间显示
     * @param time long
     * @return String
     */
    public static String changeDigitalToDateStr(long time) {
        int hour = 60 * 60 * 1000;
        int min = 60 * 1000;
        int sec = 1000;
        String hourStr = "";
        String minStr = "";
        String secStr = "";
        String resultStr = "";
        if (time / hour >= 1) {
            hourStr = String.valueOf(time / hour) + "小时";
            time = time % hour;
            if (time / min > 1) {
                minStr = String.valueOf(time / min) + "分";
                time = time % min;
                secStr = time / sec + "秒";
            } else {
                minStr = "0分";
                secStr = String.valueOf(time / sec) + "秒";
            }
        } else if (time / min >= 1) {
            minStr = String.valueOf(time / min) + "分";
            time = time % min;
            secStr = time / sec + "秒";
        } else {
            secStr = String.valueOf(time / sec) + "秒";
        }
        resultStr = hourStr + minStr + secStr;
        return resultStr;
    }

}
