package com.hyphenate.helpdesk.easeui.kefu;

/**
 * 发送广播接口,用于通知
 */
public interface IListener {
    void notifyEvent(String eventStr, Object eventObj);
}
