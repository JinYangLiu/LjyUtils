package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.bean.PostHeader;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyRetrofitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RetrofitActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView mTextView;

    private final String appId = "2";
    private final String appSecret = "anxin_bbs!@#";
    private final String safeCode = "bbs_anxin_android_#@!";
    private final String axid = "1177080";
    private final String pwd = "e3ceb5881a0a1fdaad01296d7554868d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
    }

    public void retrofitClick(View view) {
        switch (view.getId()){
            case R.id.btn_get:
                getBbsHomeList();
                break;
            case R.id.btn_post:
                post2GetUserInfo();
                break;
        }
    }

    private void post2GetUserInfo() {
        //接口方法地址
        String methodPath = "/Users/SelectUsersDetail";
        //header
        PostHeader header = new PostHeader(appId, appSecret, safeCode, axid, pwd);
        //body
        Map<String, Object> body = new HashMap<>();
        body.put("recordId", axid);
        //header+body
        Map<String, Object> params = new HashMap<>();
        params.put("header", header);
        params.put("body", body);
        //调用接口
        LjyRetrofitUtil.getInstance().post(methodPath, params, new LjyRetrofitUtil.CallBack() {
            @Override
            public void onSuccess(LjyRetrofitUtil.ParserDataBase<Map<String, Object>> parserData) {
                if (parserData != null && parserData.getCode() == 1 && parserData.getBody() != null) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("userimg:" + parserData.getBody().get("userimg"));
                    stringBuffer.append("\n\n");
                    stringBuffer.append("username:" + parserData.getBody().get("username"));
                    stringBuffer.append("\n\n");
                    stringBuffer.append("nickname:" + parserData.getBody().get("nickname"));
                    stringBuffer.append("\n\n");
                    stringBuffer.append("gold:" + parserData.getBody().get("gold"));
                    stringBuffer.append("\n\n");
                    stringBuffer.append("goldgroup:" + parserData.getBody().get("goldgroup"));
                    stringBuffer.append("\n\n");
                    Map<String, Object> map = (Map<String, Object>) parserData.getBody().get("signInfo");
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        stringBuffer.append(entry.getKey()+":"+entry.getValue());
                        stringBuffer.append("\n\n");
                    }
                    String data = stringBuffer.toString();
                    LjyLogUtil.i(data);
                    mTextView.setText(data);
                } else {
                    String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty(parserData.getMessage()) ? parserData.getMessage() :
                            parserData.getCode() == 1 ? "暂无数据" : "数据错误";
                    mTextView.setText(info);
                }
            }

            @Override
            public void onFail(String failInfo) {
                mTextView.setText(failInfo);
            }
        });
    }

    private void getBbsHomeList() {
        String methodPath = "TopicsExt/SelectTopicsForIndex";
        Map<String, Object> options = new HashMap<>();
        options.put("pageIndex", 1);
        options.put("pageSize", 10);
        LjyRetrofitUtil.getInstance().get(methodPath, options,
                new LjyRetrofitUtil.CallBack() {
                    @Override
                    public void onSuccess(LjyRetrofitUtil.ParserDataBase<Map<String, Object>> parserData) {
                        if (parserData != null && parserData.getCode() == 1 && parserData.getBody() != null) {
                            List<Map<String, Object>> listMap = (List<Map<String, Object>>) parserData.getBody().get("rows");
                            StringBuffer stringBuffer = new StringBuffer();
                            for (Map<String, Object> map : listMap) {
                                stringBuffer.append(map.get("title"));
                                stringBuffer.append("\n\n");
                            }
                            String data = stringBuffer.toString();
                            LjyLogUtil.i(data);
                            mTextView.setText(data);
                        } else {
                            String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty(parserData.getMessage()) ? parserData.getMessage() :
                                    parserData.getCode() == 1 ? "暂无数据" : "数据错误";
                            mTextView.setText(info);
                        }
                    }

                    @Override
                    public void onFail(String failInfo) {
                        mTextView.setText(failInfo);
                    }


                });
    }
}
