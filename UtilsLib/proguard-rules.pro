# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#参考地址
#https://www.cnblogs.com/ganhang-acm/p/5883185.html
#https://www.jianshu.com/p/856d30f905e6
#-------------------------1.基本不用动区域--------------------------
-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-dontskipnonpubliclibraryclassmembers
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-keepattributes *Annotation*     # 保持注解
-keepattributes Singature        #避免混淆泛型
-ignorewarning                   # 忽略警告
#-ignorewarnings //这1句是屏蔽警告，脚本中把这行注释去掉
-dontoptimize                    # 优化不优化输入的类文件
#打印日志，保留异常，源文件行数信息。
-printmapping out.map
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,SourceFile
-renamesourcefileattribute SourceFile
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#关闭log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static int println(...);
}
# 不混淆第三方引用的库
-dontskipnonpubliclibraryclasses
#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#生成日志数据，gradle build时在本项目根目录输出
-dump class_files.txt            #apk包内所有class的内部结构
-printseeds seeds.txt            #未混淆的类和成员
-printusage unused.txt           #打印未被使用的代码
-printmapping mapping.txt        #混淆前后的映射
-keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
#-libraryjars libs/pcdn.jar        #混淆第三方jar包，其中xxx为jar包名
#-keep class com.xxx.**{*;}       #不混淆某个包内的所有文件
#-dontwarn com.xxx**              #忽略某个包的警告
-keepattributes SourceFile
#不混淆泛型与反射
-keepattributes Signature
-keepattributes EnclosingMethod
# 不混淆内部类
-keepattributes InnerClasses
#不混淆错误
-keepattributes Exceptions
#不混淆Serializable
-keepnames class * implements java.io.Serializable
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# WebView使用javascript功能则需要开启
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}

#--------------------------2.第三方包-------------------------------
#gson解析
-keep class com.google.gson.** {*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-dontwarn com.google.gson.**

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep class io.reactivex.** { *; }
-keep interface io.reactivex.Observer{*;}
-keep interface io.reactivex.ObservableOnSubscribe {*;}
-keep class * implements io.reactivex.ObservableOnSubscribe {*;}
-keep class * implements io.reactivex.functions.Function {*;}
-keep class * implements io.reactivex.ObservableSource {*;}
-keep class * implements io.reactivex.Observer {*;}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

#fastjson解析
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*; }

#httpclient
-keep class org.apache.http.**{*;}


#保持对外提供的接口类不混淆
-keep public class com.order.sdk.OrderQueryManager{*;}
-keep public class com.order.sdk.VooleObservable{*;}
-keep public interface com.order.sdk.OrderCallback{*;}
-keep public class * implements com.order.sdk.OrderCallback{*;}
-keep class com.order.sdk.OrderQueryManager$*{*;}
#保持OrderApi的内部类不被混淆
-keep class com.order.sdk.OrderApi$*{*;}
#--------------------------1.实体类,包含器内部类---------------------------------
-keep class com.order.sdk.bean.AuthenTryInfo{*;}
-keep class com.order.sdk.bean.AuthenTryInfo$*{*;}
-keep class com.order.sdk.bean.OrderInfo{*;}
-keep class com.order.sdk.bean.RegisterInfo{*;}
-keep class com.order.sdk.bean.RegisterInfo$*{*;}
-keep class com.order.sdk.bean.Result{*;}
-keep class com.order.sdk.bean.StatusError{*;}
-keep class com.order.sdk.bean.StatusError$*{*;}
-keep class com.order.sdk.bean.UserAuth{*;}
-keep class com.order.sdk.bean.UserAuth$*{*;}