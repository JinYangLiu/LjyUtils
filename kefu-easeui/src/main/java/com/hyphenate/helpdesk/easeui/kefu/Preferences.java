package com.hyphenate.helpdesk.easeui.kefu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hyphenate.helpdesk.model.AgentIdentityInfo;
import com.hyphenate.helpdesk.model.ContentFactory;
import com.hyphenate.helpdesk.model.QueueIdentityInfo;
import com.hyphenate.helpdesk.model.VisitorInfo;

public class Preferences {
    private static final String TAG = Preferences.class.getSimpleName();
    static private Preferences instance = null;
    static private String PREFERENCE_NAME = "info";

    private SharedPreferences pref = null;
    private SharedPreferences.Editor editor = null;
    public static final String USERNAME = "username";
    public static final String REALNAME = "realName";
    public static final String PHONENUM = "phoneNum";
    public static final String CURRENTUSER = "currentUser";


    static public Preferences getInstance() {
        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        instance = new Preferences();
        instance.pref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        instance.editor = instance.pref.edit();
    }

    public synchronized String getAppKey() {
        return Constant.DEFAULT_CUSTOMER_APPKEY;
    }

    public synchronized String getTenantId() {
        return Constant.DEFAULT_TENANT_ID;
    }

    public String getCustomerAccount(){
        return  Constant.DEFAULT_CUSTOMER_ACCOUNT;
    }

    public synchronized void setUsername(String username) {
        editor.putString(USERNAME, username);
        editor.commit();
    }

    public synchronized String getUsername() {
        return pref.getString(USERNAME, "");
    }

    public synchronized void setRealname(String username) {
        editor.putString(REALNAME, username);
        editor.commit();
    }

    public synchronized String getRealname() {
        return pref.getString(REALNAME, "");
    }

    public synchronized void setPhonenum(String username) {
        editor.putString(PHONENUM, username);
        editor.commit();
    }

    public synchronized String getPhonenum() {
        return pref.getString(PHONENUM, "");
    }
    public synchronized void setCurrentuser(String currentuser) {
        editor.putString(CURRENTUSER, currentuser);
        editor.commit();
    }

    public synchronized String getCurrentuser() {
        return pref.getString(CURRENTUSER, "");
    }




    public  VisitorInfo createVisitorInfo(Activity mActivity) {
        VisitorInfo info = ContentFactory.createVisitorInfo(null);
        info.nickName(getUsername())
                .name(getRealname())
                .qq("")
                .companyName("")
                .description(getPhonenum())
                .email("");
        return info;
    }





    public static AgentIdentityInfo createAgentIdentity(String agentName) {
        if (TextUtils.isEmpty(agentName)){
            return null;
        }
        AgentIdentityInfo info = ContentFactory.createAgentIdentityInfo(null);
        info.agentName(agentName);
        return info;
    }

    public static QueueIdentityInfo createQueueIdentity(String queueName) {
        if (TextUtils.isEmpty(queueName)){
            return null;
        }
        QueueIdentityInfo info = ContentFactory.createQueueIdentityInfo(null);
        info.queueName(queueName);
        return info;
    }


}
