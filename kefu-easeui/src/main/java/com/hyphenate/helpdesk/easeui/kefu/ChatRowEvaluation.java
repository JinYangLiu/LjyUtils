package com.hyphenate.helpdesk.easeui.kefu;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.hyphenate.chat.Message;
import com.hyphenate.helpdesk.R;
import com.hyphenate.helpdesk.easeui.util.UserUtil;
import com.hyphenate.helpdesk.easeui.widget.chatrow.ChatRow;
import com.hyphenate.helpdesk.model.AgentInfo;
import com.hyphenate.helpdesk.model.MessageHelper;

public class ChatRowEvaluation extends ChatRow {

    Button btnEval;

    public ChatRowEvaluation(Context context, Message message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == Message.Direct.RECEIVE ? R.layout.em_row_received_satisfaction
                : R.layout.em_row_sent_satisfaction, this);
    }

    @Override
    protected void onFindViewById() {
        btnEval = (Button) findViewById(R.id.btn_eval);
    }

    @Override
    protected void onUpdateView() {
    }

    @Override
    protected void onSetUpView() {
        try {
            if (MessageHelper.getEvalRequest(message) != null) {

                btnEval.setEnabled(true);
                btnEval.setText(R.string.chatrow_eval_btn_text);
                btnEval.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ((Activity) context).startActivityForResult(new Intent(context, SatisfactionActivity.class)
                                .putExtra("msgId", message.messageId()), CustomChatFragment.REQUEST_CODE_EVAL);
                    }
                });

                AgentInfo agentInfo = MessageHelper.getAgentInfo(message);
                String strUrl = agentInfo.getAvatar();
                Log.i("ljy_評價消息", "strUrl:" + strUrl);
                Log.i("ljy_評價消息", "昵称:" + agentInfo.getNickname());
                //设置头像和nick
                UserUtil.setNickAndAvatar(context, message, userAvatarView, usernickView);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onBubbleClick() {


    }

}
