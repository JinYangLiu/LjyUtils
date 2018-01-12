package com.ljy.ljyutils.presenter;

import android.content.Context;

import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.LJY on 2018/1/12.
 */

public class RetrofitActivityPresenter extends BasePresenter {

    private final Context context;

    public RetrofitActivityPresenter(Context context) {
        this.context = context;
    }

    public void post2GetUserInfo(final BindTextView bindTextView) {
        String methodPath = "/Users/SelectUsersDetail";
        Map<String, Object> body = new HashMap<>();
        body.put("recordId", "1177080");
        postData(methodPath, body, new PresenterCallBack<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> parserBody) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("userimg:" + parserBody.get("userimg"));
                stringBuffer.append("\n\n");
                stringBuffer.append("username:" + parserBody.get("username"));
                stringBuffer.append("\n\n");
                stringBuffer.append("nickname:" + parserBody.get("nickname"));
                stringBuffer.append("\n\n");
                stringBuffer.append("gold:" + parserBody.get("gold"));
                stringBuffer.append("\n\n");
                stringBuffer.append("goldgroup:" + parserBody.get("goldgroup"));
                stringBuffer.append("\n\n");
                Map<String, Object> map = (Map<String, Object>) parserBody.get("signInfo");
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    stringBuffer.append(entry.getKey() + ":" + entry.getValue());
                    stringBuffer.append("\n\n");
                }
                String data = stringBuffer.toString();
                LjyLogUtil.i(data);
                bindTextView.bind(data);
            }

            @Override
            public void onFail(String info) {
                LjyToastUtil.toast(context, info);
            }
        });
    }

    public void getBbsId(final BindTextView bindTextView) {
        String methodPath = "Users/GetUid";
        Map<String, Object> options = new HashMap<>();
        options.put("axid", "1177080");
        getData(methodPath, options, new PresenterCallBack<String>() {
            @Override
            public void onSuccess(String bbsId) {
                bindTextView.bind("bbsId--->" + bbsId);
            }

            @Override
            public void onFail(String info) {
                LjyToastUtil.toast(context, info);
            }
        });
    }

    public void getBbsHomeList(final BindTextView bindTextView) {
        String methodPath = "TopicsExt/SelectTopicsForIndex";
        Map<String, Object> options = new HashMap<>();
        options.put("pageIndex", 1);
        options.put("pageSize", 10);
        getData(methodPath, options, new PresenterCallBack<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> parserBody) {
                List<Map<String, Object>> listMap = (List<Map<String, Object>>) parserBody.get("rows");
                StringBuffer stringBuffer = new StringBuffer();
                for (Map<String, Object> map : listMap) {
                    stringBuffer.append(map.get("title"));
                    stringBuffer.append("\n\n");
                }
                String data = stringBuffer.toString();
                LjyLogUtil.i(data);
                bindTextView.bind(data);
            }

            @Override
            public void onFail(String info) {
                LjyToastUtil.toast(context, info);
            }
        });
    }

    public interface BindTextView {
        void bind(String text);
    }


}
