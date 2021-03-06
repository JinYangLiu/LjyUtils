package com.ljy.ljyutils.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
                //模拟缓存数据
                String cache=new Gson().toJson(parserBody);
                //模拟读取缓存
                Map<String, Object> getCache = new Gson().fromJson(cache,new TypeToken<Map<String, Object>>(){}.getType());
                StringBuilder stringBuffer = new StringBuilder();
                stringBuffer.append("getCache----->\n\n");
                //遍历map叠加数据
                appendData(getCache, stringBuffer);

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

    /**
     * 使用递归进行数据叠加
     */
    private void appendData(Map<String, Object> map, StringBuilder stringBuffer) {
        for (Map.Entry<String, Object> entry1 : map.entrySet()) {
            if (entry1.getValue() instanceof Map ) {
               appendData((Map<String, Object>) entry1.getValue(),stringBuffer);
            } else {
                stringBuffer.append(entry1.getKey()).append(":").append(entry1.getValue());
                stringBuffer.append("\n\n");
            }
        }
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
                if (parserBody.get("rows") instanceof List) {
                    List<Map<String, Object>> listMap = (List<Map<String, Object>>) parserBody.get("rows");
                    StringBuilder stringBuffer = new StringBuilder();
                    for (Map<String, Object> map : listMap) {
                        stringBuffer.append(map.get("title"));
                        stringBuffer.append("\n\n");
                    }
                    String data = stringBuffer.toString();
                    LjyLogUtil.i(data);
                    bindTextView.bind(data);
                } else {
                    LjyToastUtil.toast(context, "rows 类型错误");
                }
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
