package com.hyphenate.helpdesk.easeui.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.R;
import com.hyphenate.helpdesk.easeui.kefu.Constant;
import com.hyphenate.helpdesk.model.AgentInfo;
import com.hyphenate.helpdesk.model.MessageHelper;

/**
 */
public class UserUtil {

    private static String nickName;


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
//                GlideUtils.loadImg(context.getApplicationContext(), strUrl, userAvatarView, new GlideUtils.CircleTransform(context.getApplicationContext()));
                userAvatarView.setImageResource(Constant.getInstance().getAvatarPhotoResId()==-1?R.drawable.ax_default_avatar:Constant.getInstance().getAvatarPhotoResId());
            }

        } else {
            if (userAvatarView != null){
                userAvatarView.setImageResource(Constant.getInstance().getAvatarPhotoResId()==-1?R.drawable.ax_default_avatar:Constant.getInstance().getAvatarPhotoResId());
            }
            if (userNickView != null)
                userNickView.setText(TextUtils.isEmpty(nickName) ? message.from() : nickName);
        }

    }
}
