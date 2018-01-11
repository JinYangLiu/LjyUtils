package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.util.LjyRetrofitUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RetrofitActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        getTasks();
    }

    private void getTasks() {
        String methodPath = "Task/GetUserTask";
        Map<String, String> params = new HashMap<>();
        params.put("axid", "152052");
        params.put("bbsid", "48460");
//        LjyRetrofitUtil.getInstance().getJsonMap(methodPath, params,
//                new LjyRetrofitUtil.CallBack<LjyRetrofitUtil.ParserDataBase<HashMap<String, Object>>>() {
//                    @Override
//                    public void onSuccess(LjyRetrofitUtil.ParserDataBase<HashMap<String, Object>> parserData) {
//                        if (parserData != null && parserData.getCode() == 1 && parserData.getBody() != null) {
//                            String data = parserData.getBody().toString();
//                            mTextView.setText(data);
//                        } else {
//                            String info = parserData == null ? "无数据返回" : !TextUtils.isEmpty(parserData.getMessage()) ? parserData.getMessage() :
//                                    parserData.getCode() == 1 ? "暂无数据" : "数据错误";
//                            mTextView.setText(info);
//                        }
//                    }
//
//                    @Override
//                    public void onFail(String failInfo) {
//                        mTextView.setText(failInfo);
//                    }
//
//
//                });
        LjyRetrofitUtil.getInstance().getJsonObj(methodPath, params,
                new LjyRetrofitUtil.CallBack<LjyRetrofitUtil.ParserDataBase<Object>>() {
                    @Override
                    public void onSuccess(LjyRetrofitUtil.ParserDataBase<Object> parserData) {
                        if (parserData != null && parserData.getCode() == 1 && parserData.getBody() != null) {
                            String data = parserData.getBody().toString();
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
