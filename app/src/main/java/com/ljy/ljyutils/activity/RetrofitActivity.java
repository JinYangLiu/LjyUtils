package com.ljy.ljyutils.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.presenter.RetrofitActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RetrofitActivity extends AppCompatActivity {

    @BindView(R.id.text1)
    TextView mTextView;
    private RetrofitActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        presenter = new RetrofitActivityPresenter(this);
    }

    public void retrofitClick(View view) {
        switch (view.getId()) {
            case R.id.btn_getbbsid:
                presenter.getBbsId(
                        new RetrofitActivityPresenter.BindTextView() {
                            @Override
                            public void bind(String text) {
                                String str = "---&#183;---";
                                str = Html.fromHtml(str).toString();
                                mTextView.setText(text + str);
                            }
                        });
                Toast.makeText(RetrofitActivity.this, "aaaaa", 100000).show();
                break;
            case R.id.btn_get:
                presenter.getBbsHomeList(
                        new RetrofitActivityPresenter.BindTextView() {
                            @Override
                            public void bind(String text) {
                                mTextView.setText(text);
                            }
                        });
                break;
            case R.id.btn_post:
                presenter.post2GetUserInfo(new RetrofitActivityPresenter.BindTextView() {
                    @Override
                    public void bind(String text) {
                        mTextView.setText(text);
                    }
                });
                break;
        }
    }


}
