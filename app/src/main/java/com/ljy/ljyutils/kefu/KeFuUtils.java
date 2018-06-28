package com.ljy.ljyutils.kefu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.ChatManager;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.Error;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.Notifier;
import com.hyphenate.helpdesk.easeui.UIProvider;
import com.hyphenate.helpdesk.easeui.kefu.ChatActivity;
import com.hyphenate.helpdesk.easeui.kefu.Constant;
import com.hyphenate.helpdesk.easeui.kefu.ListenerManager;
import com.hyphenate.helpdesk.easeui.kefu.Preferences;
import com.hyphenate.helpdesk.easeui.util.CommonUtils;
import com.hyphenate.helpdesk.easeui.util.GlideUtils;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;
import com.hyphenate.helpdesk.easeui.util.UserUtil;

import org.json.JSONObject;

import java.util.List;


/**
 * Created by Mr.LJY on 2016/11/29.
 *
 * 对环信客服的配置和调用的类
 */

public class KeFuUtils {



    public  void methodKeFu(final Activity mActivity) {
        login(mActivity, true);
    }

    private  void goChat(Activity mActivity) {
        // 进入主页面
        if (ChatClient.getInstance().isLoggedInBefore()) {
            Intent intent = new IntentBuilder(mActivity)
                    .setTargetClass(ChatActivity.class)
                    .setVisitorInfo(Preferences.getInstance().createVisitorInfo(mActivity))
                    .setServiceIMNumber(Preferences.getInstance().getCustomerAccount())
                    .setScheduleQueue(Preferences.getInstance().createQueueIdentity(null))
                    .setShowUserNick(false)
                    .build();
            mActivity.startActivity(intent);
        } else {
            Toast.makeText(mActivity, "登录失败，请稍候再试", Toast.LENGTH_SHORT).show();
        }
    }

    private  void login(final Activity mActivity, final boolean ifChat) {
//        RetrofitUtils.getInstance().getToken(mActivity, new RetrofitUtils.CallBack() {
//            @Override
//            public void call(String token) {
//                login(mActivity,callback,ifChat,token);
//            }
//        });

        //此处信息应通过接口从自己公司的服务器获取
        final String userName = "ljy123321";
        final String passWord = "1234123";
        final String realName = "JinYang";
        final String phoneNum = "10086";
        if (Preferences.getInstance().getUsername().equals(userName)) {
            //登录:
            Preferences.getInstance().setUsername(userName);
            Preferences.getInstance().setRealname(realName);
            Preferences.getInstance().setPhonenum(phoneNum);
            initLogin(mActivity, userName, passWord, ifChat);
        } else {
            //注册:
            //建议在服务端创建，而不要放到APP中，可以在登录自己APP时从返回的结果中获取环信账号再登录环信服务器
            ChatClient.getInstance().register(userName, passWord, new Callback() {
                @Override
                public void onSuccess() {
                    Preferences.getInstance().setUsername(userName);
                    Preferences.getInstance().setRealname(realName);
                    Preferences.getInstance().setPhonenum(phoneNum);
                    initLogin(mActivity, userName, passWord, ifChat);
                }

                @Override
                public void onError(int code, String error) {
                    Log.i("ljy",code + "_客服系统注冊回调.失败:" + error);
                    if (code==Error.USER_ALREADY_EXIST){
                        Preferences.getInstance().setUsername(userName);
                        Preferences.getInstance().setRealname(realName);
                        Preferences.getInstance().setPhonenum(phoneNum);
                        initLogin(mActivity, userName, passWord, ifChat);
                    }

                }

                @Override
                public void onProgress(int progress, String status) {

                }
            });
        }

    }
//    private static void login(final Activity mActivity, final Callback callback, final boolean ifChat, String token) {
//        Map<String, String> params = new HashMap<>();
//        params.put("action", "HuanXin");
//        params.put("cmd", "CreatAccount");
//        params.put("token", token);
//        params.put("pl", "2");
//        SPUtil spUtil = new SPUtil(mActivity);
//        params.put("uid", spUtil.getFromSp(SPUtil.USERID, ""));
//        params.put("pwd", spUtil.getFromSp(SPUtil.USERPWD, ""));
//        RetrofitUtils.getInstance().isPubUrl().getJsonObject( RetrofitUtils.pubDefaultMethod, params,
//                new RetrofitUtils.SuccessCallBackT<ParserDataBase<Object>>() {
//                    @Override
//                    public void onSuccess(ParserDataBase<Object> parserData) {
//                        if (parserData != null && parserData.getCode()==0) {
//                            Gson gson = new Gson();
//                            String bodyStr = gson.toJson(parserData.getBody());
//                            ParserBodyKeFu parserBody = gson.fromJson(bodyStr,ParserBodyKeFu.class);
//                            String userName = parserBody.getUserName();
//                            String pwd = parserBody.getPwd();
//                            String realName = parserBody.getRealName();
//                            String phoneNum = parserBody.getMobile();
//                            if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
//                                Toast.makeText(mActivity, "暂无网络", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Preferences.getInstance().setUsername(userName);
//                                Preferences.getInstance().setRealname(realName);
//                                Preferences.getInstance().setPhonenum(phoneNum);
//                                initLogin(mActivity, userName, pwd, ifChat);
//                            }
//                        } else {
//                            if (parserData != null)
//                                Toast.makeText(mActivity, TextUtils.isEmpty(parserData.getMessage()) ? "暂无网络" : parserData.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, new RetrofitUtils.FailureCallBack() {
//                    @Override
//                    public void onFaliure(String string) {
//                        Toast.makeText(mActivity, "暂无网络", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }

    private  void initLogin(final Activity mActivity, final String userName, final String pwd, final boolean ifChat) {
        final Callback callback = new Callback() {
            @Override
            public void onSuccess() {
                Preferences.getInstance().setCurrentuser(userName);
                if (ifChat)
                    goChat(mActivity);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("ljy",i + "_客服系统登录回调.失败:" + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        };
        if (!ChatClient.getInstance().isLoggedInBefore()) {
            ChatClient.getInstance().login(userName, pwd, callback);
        } else if (!userName.equals(Preferences.getInstance().getCurrentuser())) {
            ChatClient.getInstance().logout(true, new Callback() {
                @Override
                public void onSuccess() {
                    ChatClient.getInstance().login(userName, pwd, callback);
                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });

        } else {
            if (ifChat) {
                goChat(mActivity);
            }
        }
    }

    /**
     * 重置了环信客服账号，由于客服聊天记录是保存在本地的，避免出现
     * 不同账号相同聊天记录
     */
    public static void resetKeFuAccount() {
        if (ChatClient.getInstance().isLoggedInBefore()) {
            ChatClient.getInstance().logout(true, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }

    }

    //---------------------------------------demoHelper---------------------------------

    private static final String TAG = "DemoHelper";

    public static KeFuUtils instance = new KeFuUtils();

    /**
     * kefuChat.MessageListener
     */
    protected ChatManager.MessageListener messageListener = null;

    /**
     * ChatClient.ConnectionListener
     */
    private ChatClient.ConnectionListener connectionListener;

    private UIProvider _uiProvider;

    private KeFuUtils() {
    }

    public synchronized static KeFuUtils getInstance() {
        return instance;
    }

    /**
     * init helper
     *
     * @param context application context
     */
    public void init(final Context context,Class backHomeActivity) {
        Preferences.init(context);
        Constant.backHomeActivity=backHomeActivity;
        ChatClient.Options options = new ChatClient.Options();
        ////必填项，appkey获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“AppKey”
        options.setAppkey(Preferences.getInstance().getAppKey());
        ////必填项，tenantId获取地址：kefu.easemob.com，“管理员模式 > 设置 > 企业信息”页面的“租户ID”
        options.setTenantId(Preferences.getInstance().getTenantId());

        //在小米手机上当app被kill时使用小米推送进行消息提示，SDK已支持，可选
//        options.setMipushConfig("2882303761517507836", "5631750729836");
//        在华为手机上当APP被kill时使用华为推送进行消息提示, SDK已支持,可选
//        options.setHuaweiPushAppId("10663060");

        // 环信客服 SDK 初始化, 初始化成功后再调用环信下面的内容
        if (ChatClient.getInstance().init(context, options)) {

            //设为调试模式，打成正式包时，最好设为false，以免消耗额外的资源
            ChatClient.getInstance().setDebugMode(false);

            _uiProvider = UIProvider.getInstance();
            //初始化EaseUI
            _uiProvider.init(context);
            //调用easeui的api设置providers
            setEaseUIProvider(context);
            //设置全局监听
//            setGlobalListeners();
        }
    }


    private void setEaseUIProvider(final Context context) {
        //设置头像和昵称
        UIProvider.getInstance().setUserProfileProvider(new UIProvider.UserProfileProvider() {
            @Override
            public void setNickAndAvatar(Context context, Message message, ImageView userAvatarView, TextView usernickView) {
//              SPUtil spUtil = new SPUtil(context);
//              String strUrl = spUtil.getFromSp("user_img_path", "");
                String strUrl = "https://avatars0.githubusercontent.com/u/19702574?s=400&v=4";
                if (message.direct() == Message.Direct.SEND && !TextUtils.isEmpty(strUrl)) {
                    //此处设置当前登录用户的昵称信息(发送方)
                    context = context.getApplicationContext();
                    GlideUtils.loadImg(context, strUrl, userAvatarView, new GlideUtils.CircleTransform(context));
                } else {
                    //设置接收方的昵称和头像(客服的)
                    UserUtil.setNickAndAvatar(context, message, userAvatarView, usernickView);
                }
            }
        });


        //设置通知栏样式
        _uiProvider.getNotifier().setNotificationInfoProvider(new Notifier.NotificationInfoProvider() {
            @Override
            public String getTitle(Message message) {
                //修改标题,这里使用默认
                return null;
            }

            @Override
            public int getSmallIcon(Message message) {
                //设置小图标，这里为默认
                return 0;
            }

            @Override
            public String getDisplayedText(Message message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = CommonUtils.getMessageDigest(message, context);
                if (message.getType() == Message.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                return message.getFrom() + ": " + ticker;
            }

            @Override
            public String getLatestText(Message message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
            }

            @Override
            public Intent getLaunchIntent(Message message) {
                //设置点击通知栏跳转事件

                Intent intent = new IntentBuilder(context)
                        .setTargetClass(ChatActivity.class)
                        .setServiceIMNumber(message.getFrom())
                        .setShowUserNick(true)

                        .build();
                return intent;
            }
        });

        //不设置,则使用默认, 声音和震动设置
//        _uiProvider.setSettingsProvider(new UIProvider.SettingsProvider() {
//            @Override
//            public boolean isMsgNotifyAllowed(Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean isMsgSoundAllowed(Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean isMsgVibrateAllowed(Message message) {
//                return false;
//            }
//
//            @Override
//            public boolean isSpeakerOpened() {
//                return false;
//            }
//        });
//        ChatClient.getInstance().getChat().addMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(List<Message> msgs) {
//
//            }
//
//            @Override
//            public void onCmdMessage(List<Message> msgs) {
//
//            }
//
//            @Override
//            public void onMessageSent() {
//
//            }
//
//            @Override
//            public void onMessageStatusUpdate() {
//
//            }
//        });
    }

    private void setGlobalListeners() {
        // create the global connection listener
        /*connectionListener = new ChatClient.ConnectionListener(){

            @Override
            public void onConnected() {
                //onConnected
            }

            @Override
            public void onDisconnected(int errorcode) {
                if (errorcode == Error.USER_REMOVED){
                    //账号被移除
                }else if (errorcode == Error.USER_LOGIN_ANOTHER_DEVICE){
                    //账号在其他地方登陆
                }
            }
        };

        //注册连接监听
        ChatClient.getInstance().addConnectionListener(connectionListener);*/

        //注册消息事件监听
        registerEventListener();
    }

    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        messageListener = new ChatManager.MessageListener() {

            @Override
            public void onMessage(List<Message> msgs) {
                for (Message message : msgs) {
                    com.hyphenate.helpdesk.util.Log.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    //应用在后台,不需要刷新UI,通知栏提示新消息
                    if (_uiProvider.hasForegroundActivies()) {
                        getNotifier().viberateAndPlayTone(message);
                    }


                    //这里全局监听通知类消息,通知类消息是通过普通消息的扩展实现
                    if (message.isNotificationMessage()) {
                        // 检测是否为留言的通知消息
                        String eventName = getEventNameByNotification(message);
                        if (!TextUtils.isEmpty(eventName)) {
                            if (eventName.equals("TicketStatusChangedEvent") || eventName.equals("CommentCreatedEvent")) {
                                // 检测为留言部分的通知类消息,刷新留言列表
                                JSONObject jsonTicket = null;
                                try {
                                    jsonTicket = message.getJSONObjectAttribute("weichat").getJSONObject("event").getJSONObject("ticket");
                                } catch (Exception e) {
                                }
                                ListenerManager.getInstance().sendBroadCast(eventName, jsonTicket);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCmdMessage(List<Message> msgs) {
                for (Message message : msgs) {
                    com.hyphenate.helpdesk.util.Log.d(TAG, "收到透传消息");
                    //获取消息body
                    EMCmdMessageBody cmdMessageBody = (EMCmdMessageBody) message.getBody();
                    String action = cmdMessageBody.action(); //获取自定义action
                    com.hyphenate.helpdesk.util.Log.d(TAG, String.format("透传消息: action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageStatusUpdate() {

            }

            @Override
            public void onMessageSent() {

            }
        };

        ChatClient.getInstance().getChat().addMessageListener(messageListener);
    }


    /**
     * 获取EventName
     *
     * @param message
     * @return
     */
    public String getEventNameByNotification(Message message) {

        try {
            JSONObject weichatJson = message.getJSONObjectAttribute("weichat");
            if (weichatJson != null && weichatJson.has("event")) {
                JSONObject eventJson = weichatJson.getJSONObject("event");
                if (eventJson != null && eventJson.has("eventName")) {
                    return eventJson.getString("eventName");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pushActivity(Activity activity) {
        _uiProvider.pushActivity(activity);
    }

    public void popActivity(Activity activity) {
        _uiProvider.popActivity(activity);
    }

    public Notifier getNotifier() {
        return _uiProvider.getNotifier();
    }

}
