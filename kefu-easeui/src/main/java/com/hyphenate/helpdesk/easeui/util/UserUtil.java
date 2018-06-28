package com.hyphenate.helpdesk.easeui.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.R;
import com.hyphenate.helpdesk.model.AgentInfo;
import com.hyphenate.helpdesk.model.MessageHelper;

/**
 */
public class UserUtil {

    private static String nickName;
    private static String imgPhoto;


    public static void setNickAndAvatar(Context context, Message message, ImageView userAvatarView, TextView userNickView) {
        AgentInfo agentInfo = MessageHelper.getAgentInfo(message);

        if (agentInfo != null) {
            if (userNickView != null) {
                if (nickName == null || (!"调度员".equals(agentInfo.getNickname()) && !nickName.equals(agentInfo.getNickname())))
                    nickName = agentInfo.getNickname();
                userNickView.setText(nickName);
            }

            if (userAvatarView != null) {
                String strUrl = agentInfo.getAvatar();
                if (strUrl != null) {
                    if (!strUrl.startsWith("http") || !strUrl.startsWith("https")) {
                        strUrl = "http:" + strUrl;
                    }
                }
                Log.i("ljy_setNickAndAvatar", "strUrl:" + strUrl);
                if (imgPhoto == null || (strUrl != null && !imgPhoto.equals(strUrl)))
                    imgPhoto = strUrl;
                setPhoto(context, userAvatarView, imgPhoto);
            }

        } else {
            if (userAvatarView != null)
                if (TextUtils.isEmpty(imgPhoto))
                    userAvatarView.setImageResource(R.drawable.hd_default_avatar);
                else
                    setPhoto(context, userAvatarView, imgPhoto);
            if (userNickView != null)
                userNickView.setText(TextUtils.isEmpty(nickName) ? message.from() : nickName);
        }

    }

    private static void setPhoto(Context context, ImageView userAvatarView, String strUrl) {
        Context var10000 = context.getApplicationContext();
        GlideUtils var10005 = new GlideUtils();
        var10005.getClass();
        GlideUtils.loadImg(var10000, strUrl, userAvatarView, new GlideUtils.CircleTransform(context.getApplicationContext()));
        userAvatarView.setBackgroundColor(16777215);
    }

}
