/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.helpdesk.easeui.kefu;


import android.content.Context;

public class Constant {
    private static final Constant mInstance = new Constant();

    private Constant(){}

    public static synchronized Constant getInstance() {
        return mInstance;
    }

    private Class backHomeActivity = null;
    private OnUrlLinkClickListener listener = null;
    private String appKey;
    private String account;
    private String tenantId;

    public Class getBackHomeActivity() {
        return backHomeActivity;
    }

    public Constant setBackHomeActivity(Class backHomeActivity) {
        this.backHomeActivity = backHomeActivity;
        return this;
    }

    public OnUrlLinkClickListener getOnUrlLinkClickListener() {
        return listener;
    }

    public Constant setOnUrlLinkClickListener(OnUrlLinkClickListener listener) {
        this.listener = listener;
        return this;
    }

    public String getAppKey() {
        return appKey;
    }

    public Constant setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public Constant setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Constant setTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public interface OnUrlLinkClickListener {
        void onClick(Context context,String url);
    }

}
