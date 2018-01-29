package com.ljy.ljyutils.base;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyRetrofitUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.entity.UMessage;
import com.umeng.message.tag.TagManager;

import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mr.LJY on 2017/12/26.
 */

public class MyApplication extends Application {
    private static Context applicationContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
        initMyUtil();
        initBugly();
        initUmengPush();

    }

    private void initUmengPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);

        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token,device token是【友盟+】消息推送生成的用于标识设备的id，
                // 长度为44位，不能定制和修改。同一台设备上不同应用对应的device token不一样。
                //如需手动获取device token，可以调用mPushAgent.getRegistrationId()方法（需在注册成功后调用）。
                LjyLogUtil.i("deviceToken:" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LjyLogUtil.i("s:" + s + ",s1:" + s1);
            }
        });

//        uMengPushMoreSetting(mPushAgent);


    }

    private void initMyUtil() {
        LjyLogUtil.setIsLog(true);
        LjyRetrofitUtil.setBaseUrl("http://pub2.bbs.anxin.com");
        LjyRetrofitUtil.setTimeOut(30, 60, 60);
    }

    private void initBugly() {
        setStrictMode();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = true;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;
        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(),
                        "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                Toast.makeText(getApplicationContext(), patchFilePath, Toast.LENGTH_SHORT).show();
//                Beta.applyDownloadedPatch();
            }

            @Override
            public void onDownloadFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplySuccess(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {
                Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
            }
        };

        long start = System.currentTimeMillis();
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId,调试时将第三个参数设置为true
        Bugly.init(this, "699b776659", true);
        long end = System.currentTimeMillis();
        Log.e("init time--->", end - start + "ms");
    }

    public static Context getMyApplicationContext() {
        return applicationContext;
    }

    @TargetApi(9)
    protected void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }

    private void uMengPushMoreSetting(PushAgent mPushAgent) {
        // 自定义通知打开动作
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            //自定义通知栏样式
            //此方法会在通知展示到通知栏时回调
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                //msg.builder_id是服务器下发的消息字段，用来指定通知消息的样式。开发者可在【友盟+】Push网站上指定，默认值为0。
                switch (msg.builder_id) {
                    case 1:
                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
//                                R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
//                                getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon,
//                                getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
                        return builder.getNotification();
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
                //自定义参数"extra": {"key1": "value1", "key2": "value2"}
//                for (Map.Entry entry : msg.extra.entrySet()) {
//                    String key = (String) entry.getKey();
//                    String value = (String) entry.getValue();
//                }
            }

            /*
             自定义消息，是指发送后不会在系统通知栏展现，
             SDK将消息体传给第三方应用后需要开发者写展现代码才能看到的推送形式。
             【友盟+】消息推送SDK不对该消息进行展示和提醒，消息内容全部都传递给应用处理。
             * */
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//                new Handler(getMainLooper()).post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // 对于自定义消息，PushSDK默认只统计送达。若开发者需要统计点击和忽略，则需手动调用统计方法。
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        //为免过度打扰用户，SDK默认在“23:00”到“7:00”之间收到通知消息时不响铃，不振动，不闪灯。如果需要改变默认的静音时间，可以使用以下接口：
        //public void setNoDisturbMode(int startHour, int startMinute, int endHour, int endMinute)
        mPushAgent.setNoDisturbMode(22, 0, 8, 0);
        //通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。
        mPushAgent.setDisplayNotificationNumber(2);
        //客户端控制通知到达响铃、震动、呼吸灯
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
        /*
         MsgConstant.NOTIFICATION_PLAY_SERVER（服务端控制）
         MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE（客户端允许）
         MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE（客户端禁止）
         > 服务端控制：通过服务端推送状态来设置客户端响铃、震动、呼吸灯的状态
         > 客户端允许：不关心服务端推送状态，客户端都会响铃、震动、呼吸灯亮
         > 客户端禁止：不关心服务端推送状态，客户端不会响铃、震动、呼吸灯亮
         * */

        //自定义通知栏图标:
        /*
         * 如果在发送后台没有指定通知栏图标，SDK将使用本地的默认图标，其中，
         * 大图标默认使用：drawable下的umeng_push_notification_default_large_icon，
         * 小图标默认使用drawable下的umeng_push_notification_default_small_icon。
         * 若开发者没有设置这两个图标，
         * 则默认使用应用的图标(<application android:icon="@drawable/ic_launcher"></application>)，
         * 请确保应用设置了默认图标。
         * > 小图标smallIcon要求为48*48像素，图片各边至少留一个像素的透明，图标主体使用颜色，背景均使用透明。
         * > 大图标largeIcon 要求为64*64像素。
         */

        //自定义通知栏声音
        /*
         * 如果在发送后台没有指定通知栏的声音，SDK将采用本地默认的声音，
         * 即在res/raw/下的umeng_push_notification_default_sound。
         * 若无此文件，则默认使用系统的Notification声音。
         */

        //如果应用在前台的时候，开发者可以自定义配置是否显示通知，默认情况下，应用在前台是显示通知的
        //此方法请在mPushAgent.register方法之前调用。
        mPushAgent.setNotificaitonOnForeground(false);

        //为用户加上标签，方便推送时按照标签来筛选
        //目前每个用户tag限制在1024个， 每个tag 最大128个字符
        //在原有标签基础上增加新标签。
        mPushAgent.getTagManager().addTags(new TagManager.TCallBack() {

            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
            }

        }, "aaa", "bbb", "ccc");

        //将之前添加的标签中的一个或多个删除。
        mPushAgent.getTagManager().deleteTags(new TagManager.TCallBack() {

            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {

            }

        }, "bbb");

        //获取服务器端的所有标签
        mPushAgent.getTagManager().getTags(new TagManager.TagListCallBack() {

            @Override
            public void onMessage(boolean isSuccess, List<String> result) {

            }

        });

        //设置用户加权标签（Weighted Tag）
        //加权标签是给标签增加了一个权值。例如点击了“财经”栏目，就给该用户“财经”标签增加相应权值。
        //推送消息时，可以选择“财经”标签权值大于某个值的用户来进行推送
        Hashtable<String, Integer> table = new Hashtable<>();
        table.put("财经", 5);
        table.put("文学", 1);
        table.put("算术", 1010);
        mPushAgent.getTagManager().addWeightedTags(new TagManager.TCallBack() {

            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {

            }

        }, table);

        //删除加权标签
        mPushAgent.getTagManager().deleteWeightedTags(new TagManager.TCallBack() {

            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {

            }

        }, "财经", "算术");

        //获取服务器端的所有加权标签
        mPushAgent.getTagManager().getWeightedTags(new TagManager.WeightedTagListCallBack() {
            @Override
            public void onMessage(boolean isSuccess, final Hashtable<String, Integer> result) {

            }
        });

        //设置用户别名（Alias）
        //如果你的应用有自有的用户id体系，可以在SDK中通过Alias字段上传自有用户id，按用户id向用户推送消息
        //若要使用新的alias，请先调用deleteAlias接口移除掉旧的alias，再调用addAlias添加新的alias，
        String id = "10001";
        String aliasType = "UserID";
        //1.设置用户id和device_token的一对多的映射关系：
        mPushAgent.addAlias(id, aliasType, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean isSuccess, String message) {

            }

        });
        //设置用户id和device_token的一一映射关系，确保同一个alias只对应一台设备：
        mPushAgent.setAlias(id, aliasType,

                new UTrack.ICallBack() {

                    @Override
                    public void onMessage(boolean isSuccess, String message) {

                    }

                });

        //若是要移除用户id，可调用以下接口
        mPushAgent.deleteAlias("zhangsan@sina.com", aliasType, new UTrack.ICallBack() {

            @Override
            public void onMessage(boolean isSuccess, String message) {

            }

        });

        /*
        为了便于开发者更好的集成配置文件，我们提供了对于AndroidManifest配置文件的检查工具，
        可以自行检查开发者的配置问题。SDK默认是不检查集成配置文件的，
        在线上版本请关掉该配置检查，或者去掉这行检查代码（sdk默认是不做该项检查的）。
        自定义检查集成配置文件的接口如下：
         */
        mPushAgent.setPushCheck(true);

        //关闭推送
        mPushAgent.disable(new IUmengCallback() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {

            }

        });

        //若调用关闭推送后，想要再次开启推送，则需要调用以下代码（请在Activity内调用）：
        mPushAgent.enable(new IUmengCallback() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {

            }

        });

        // 完全自定义处理
        /*
        若开发者需要实现对消息的完全自定义处理，则可以继承 UmengMessageService,
        实现自己的Service来完全控制达到消息的处理。
        使用完全自定义处理后，PushSDK只负责下发消息体且只统计送达数，展示逻辑需由开发者自己写代码实现，
        点击数和忽略数需由开发者调用UTrack类的trackMsgClick和trackMsgDismissed方法进行统计。
         */
//        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

        // 应用内消息
        //1.应用内消息默认为线上模式，如需使用测试模式，请调用如下代码：
//        InAppMessageManager.getInstance(this).setInAppMsgDebugMode(true);
        //2.更多具体使用可参考官方文档

        //小米，华为，魅族 Push通道的使用，请参考官方文档
        //http://dev.umeng.com/sdk_integate/android_sdk/android_push_doc#3_6
    }
}
