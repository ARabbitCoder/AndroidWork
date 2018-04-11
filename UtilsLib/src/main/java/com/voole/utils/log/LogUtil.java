package com.voole.utils.log;

import android.util.Log;

/**
 * But more pretty, simple and powerful
 */
public final class LogUtil {

  public static String Tag = "VooleApk";
  public static boolean DebugMode = true;

  public static final int VERBOSE = 2;
  public static final int DEBUG = 3;
  public static final int INFO = 4;
  public static final int WARN = 5;
  public static final int ERROR = 6;
  public static final int ASSERT = 7;

  private static Printer printer = new LoggerPrinter();

  private LogUtil() {
    //no instance
  }

  public static void setTag(String tag){
    Tag = tag;
    clearLogAdapters();
    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
            // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .tag(tag)
            .build();
    AndroidLogAdapter androidLogAdapter = new AndroidLogAdapter(formatStrategy){
      @Override
      public boolean isLoggable(int priority, String tag) {
        return DebugMode;
      }
    };
    addLogAdapter(androidLogAdapter);
  }

  public static void setDebugMode(boolean isDebug){
    DebugMode = isDebug;
    clearLogAdapters();
    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
            // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .tag(Tag)
            .build();
    AndroidLogAdapter androidLogAdapter = new AndroidLogAdapter(formatStrategy){
      @Override
      public boolean isLoggable(int priority, String tag) {
        return DebugMode;
      }
    };
    addLogAdapter(androidLogAdapter);
  }

  public static void printer(Printer printer) {
    LogUtil.printer = printer;
  }

  private static void addLogAdapter(LogAdapter adapter) {
    printer.addAdapter(adapter);
  }

  private static void clearLogAdapters() {
    printer.clearLogAdapters();
  }

  /**
   * Given tag will be used as tag only once for this method call regardless of the tag that's been
   * set during initialization. After this invocation, the general tag that's been set will
   * be used for the subsequent log calls
   */
  public static Printer t(String tag) {
    return printer.t(tag);
  }

  /**
   * General log function that accepts all configurations as parameter
   */
  private static void log(int priority, String tag, String message, Throwable throwable) {
    printer.log(priority, tag, message, throwable);
  }

  public static void d(String name,String messgae,Object... args){
    Log.d(Tag,name+":"+messgae);
    //d(name+":"+messgae,args);
  }

  public static void d(String message, Object... args) {
    printer.d(message, args);
  }

  public static void d(Object object) {
    Log.d(Tag,object.toString());
    //printer.d(object);
  }
  public static void e(String name,String message, Throwable throwable ,Object... args) {
    printer.e(throwable,name+":"+message, args);
  }
  public static void e(String message, Object... args) {
    printer.e(message,null,  args);
  }

  public static void e( String message, Throwable throwable,Object... args) {
    printer.e(throwable, message, args);
  }
  public static void e( String message, Throwable throwable) {
    printer.e(throwable, message);
  }
  public static void i(String message, Object... args) {
    printer.i(message, args);
  }

  public static void v(String message, Object... args) {
    printer.v(message, args);
  }

  public static void w(String message, Object... args) {
    printer.w(message, args);
  }

  /**
   * Tip: Use this for exceptional situations to log
   * ie: Unexpected errors etc
   */
  public static void wtf(String message, Object... args) {
    printer.wtf(message, args);
  }

  /**
   * Formats the given json content and print it
   */
  public static void json(String json) {
    printer.json(json);
  }

  /**
   * Formats the given xml content and print it
   */
  public static void xml(String xml) {
    printer.xml(xml);
  }

}
