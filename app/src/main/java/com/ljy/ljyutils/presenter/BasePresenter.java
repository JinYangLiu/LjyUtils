package com.ljy.ljyutils.presenter;

import android.text.TextUtils;

import com.ljy.ljyutils.bean.PostHeader;
import com.ljy.util.LjyRetrofitUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.LJY on 2018/1/12.
 */

public class BasePresenter {

    private final String CODE = "code";
    private final String MESSAGE = "message";
    private final String BODY = "body";

    private final String appId = "2";
    private final String appSecret = "anxin_bbs!@#";
    private final String safeCode = "bbs_anxin_android_#@!";
    private final String axid = "1177080";
    private final String pwd = "e3ceb5881a0a1fdaad01296d7554868d";

    <T>void postData(String methodPath, Map<String, Object> body, final PresenterCallBack<T> postCallBack) {

        //header
        PostHeader header = new PostHeader(appId, appSecret, safeCode, axid, pwd);
        //header+body
        Map<String, Object> params = new HashMap<>();
        params.put("header", header);
        params.put("body", body);

        LjyRetrofitUtil.getInstance().post(methodPath, params, new LjyRetrofitUtil.CallBack() {
            @Override
            public void onSuccess(Map<String, Object> parserData) {
                if (parserData != null && "1".equals(parserData.get(CODE)) && parserData.get(BODY) != null) {
                    postCallBack.onSuccess((T) parserData.get(BODY));
                } else {
                    String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty((String) parserData.get(MESSAGE)) ? (String) parserData.get(MESSAGE) :
                            "1".equals(parserData.get(CODE)) ? "暂无数据" : "数据错误";
                    postCallBack.onFail(info);
                }
            }

            @Override
            public void onFail(String failInfo) {
                postCallBack.onFail(failInfo);
            }
        });
    }

    <T>void getData(String methodPath, Map<String, Object> params, final PresenterCallBack<T> getCallBack) {
        LjyRetrofitUtil.getInstance().get(methodPath, params,
                new LjyRetrofitUtil.CallBack() {
                    @Override
                    public void onSuccess(Map<String, Object> parserData) {
                        if (parserData != null && "1".equals(parserData.get(CODE)) && parserData.get(BODY) != null) {
                            if (getCallBack != null)
                                getCallBack.onSuccess((T) parserData.get(BODY));
                        } else {
                            String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty((String) parserData.get(MESSAGE)) ? (String) parserData.get(MESSAGE) :
                                    "1".equals(parserData.get(CODE)) ? "暂无数据" : "数据错误";
                            if (getCallBack != null)
                                getCallBack.onFail(info);
                        }
                    }

                    @Override
                    public void onFail(String failInfo) {
                        if (getCallBack != null)
                            getCallBack.onFail(failInfo);
                    }
                });
    }

     interface PresenterCallBack<T> {
        void onSuccess(T parserBody);

        void onFail(String info);
    }

}
