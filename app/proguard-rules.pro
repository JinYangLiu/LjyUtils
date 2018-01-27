# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AS\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-dontwarn
-optimizationpasses 7
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-allowaccessmodification
-repackageclasses
-dontusemixedcaseclassnames
-dontoptimize
-keepattributes InnerClasses
-dontoptimize
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses

-keepattributes EnclosingMethod
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-dontwarn android.support.**
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class  com.nostra13.universalimageloader.** { *; }
-keep class  org.kobjects.** { *; }
-keep class  org.ksoap2.** { *; }
-keep class  org.kxml2.** { *; }
-keep class  org.xmlpull.v1.** { *; }
-dontwarn org.apache.commons.httpclient.**
-keep class  org.apache.commons.httpclient.** { *; }
-keep class  com.sina.weibo.** { *; }
-keep class  com.sina.sso.** { *; }
-keep class  com.tencent.weibo.sdk.android.** { *; }
-keep class  com.tencent.a.** { *; }
-keep class  com.tencent.mm.** { *; }
-keep class  com.tencent.connect.** { *; }
-keep class  com.tencent.open.** { *; }
-keep class  com.tencent.tauth.** { *; }
-keep class  com.tencent.start.** { *; }
-keep class  com.loopj.android.http.** { *; }
-keep class  com.daimajia.easing.** { *; }
-keep class  com.daimajia.androidanimations.library.** { *; }
-keep class  com.nineoldandroids.animation.** { *; }
-keep class  com.nineoldandroids.util.** { *; }
-keep class  com.nineoldandroids.view.** { *; }

# 友盟混淆
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
# 友盟混淆

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}


-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-keepclassmembers class * extends android.app.Activity {
   public void *(android.webkit.ValueCallback<Uri>);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.webkit.ValueCallback<Uri>,java.lang.String);
}

-keepattributes InnerClasses
-dontoptimize


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature


# Gson specific classes
-keep class com.google.gson.** { *; }




#bugly start
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
#bugly end
#okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#okhttp3
#eventBus混淆配置
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}
#eventBus混淆配置结束

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#butterknife

#retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#retrofit

#RxJava RxAndroid
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
#RxJava RxAndroid
#zxing
-keep class com.google.zxing.** { *; }
