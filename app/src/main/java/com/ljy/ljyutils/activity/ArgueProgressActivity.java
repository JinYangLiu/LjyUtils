package com.ljy.ljyutils.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.ljy.ljyutils.R;
import com.ljy.view.LjyArgueProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArgueProgressActivity extends AppCompatActivity {

    @BindView(R.id.argueProgressView)
    LjyArgueProgressView mLjyArgueProgressView;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgressBar;
    private int a=2;
    private int b=3;
    private String str1="<font color=\"#d50000\">" + "【正方】" + "</font>" +"Android牛";
    private String str2="<font color=\"#1976d2\">" + "【反方】" + "</font>" +"iOS牛";
    private Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_argue_progress);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mLjyArgueProgressView.setUI(a,b,str1,str2);
        mLjyArgueProgressView.setOnClickListener(new LjyArgueProgressView.OnClickListener() {
            @Override
            public boolean listenerYes() {
                mLjyArgueProgressView.setUI(++a,b,str1,str2);
                mLjyArgueProgressView.btnClickable(true);
                return true;
            }

            @Override
            public boolean listenerNo() {
                mLjyArgueProgressView.setUI(a,++b,str1,str2);
                mLjyArgueProgressView.btnClickable(true);
                return true;
            }
        });

        CircularProgressDrawable drawable=new CircularProgressDrawable(mContext);
        final Context context = mContext;
        int[] colorResIds={R.color.theme_red};
        int[] colorRes = new int[colorResIds.length];
        for (int i = 0; i < colorResIds.length; i++) {
            colorRes[i] = ContextCompat.getColor(context, colorResIds[i]);
        }
        drawable.setColorSchemeColors(colorRes);
        drawable.setStyle(CircularProgressDrawable.DEFAULT);
        loadingProgressBar.setIndeterminateDrawable(drawable);
    }
}
