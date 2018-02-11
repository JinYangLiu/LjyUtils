package com.ljy.ljyutils.presenter;

import android.text.TextUtils;

import com.ljy.ljyutils.bean.PostHeader;
import com.ljy.util.LjyRetrofitUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr.LJY on 2018/1/12.
 * <p>
 * Presenter的基类
 */
class BasePresenter {

    private final static String codeKey = "code";
    private final static String messageKey = "message";
    private final static String bodyKey = "body";

    private final static String appId = "2";
    private final static String appSecret = "anxin_bbs!@#";
    private final static String safeCode = "bbs_anxin_android_#@!";
    private final static String axid = "1177080";
    private final static String pwd = "e3ceb5881a0a1fdaad01296d7554868d";

    <T> void postData(String methodPath, Map<String, Object> body, final PresenterCallBack<T> postCallBack) {

        //header
        PostHeader header = new PostHeader(appId, appSecret, safeCode, axid, pwd);
        //header+body
        Map<String, Object> params = new HashMap<>();
        params.put("header", header);
        params.put("body", body);

        LjyRetrofitUtil.getInstance().post(methodPath, params, new LjyRetrofitUtil.CallBack() {
            @Override
            public void onSuccess(Map<String, Object> parserData) {
                if (parserData != null && "1".equals(parserData.get(codeKey)) && parserData.get(bodyKey) != null) {
                    postCallBack.onSuccess((T) parserData.get(bodyKey));
                } else {
                    String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty((String) parserData.get(messageKey)) ? (String) parserData.get(messageKey) :
                            "1".equals(parserData.get(codeKey)) ? "暂无数据" : "数据错误";
                    postCallBack.onFail(info);
                }
            }

            @Override
            public void onFail(String failInfo) {
                postCallBack.onFail(failInfo);
            }
        });
    }

    <T> void getData(String methodPath, Map<String, Object> params, final PresenterCallBack<T> getCallBack) {
        LjyRetrofitUtil.getInstance().get(methodPath, params,
                new LjyRetrofitUtil.CallBack() {
                    @Override
                    public void onSuccess(Map<String, Object> parserData) {
                        if (parserData != null && "1".equals(parserData.get(codeKey)) && parserData.get(bodyKey) != null) {
                            if (getCallBack != null)
                                getCallBack.onSuccess((T) parserData.get(bodyKey));
                        } else {
                            String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty((String) parserData.get(messageKey)) ? (String) parserData.get(messageKey) :
                                    "1".equals(parserData.get(codeKey)) ? "暂无数据" : "数据错误";
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
