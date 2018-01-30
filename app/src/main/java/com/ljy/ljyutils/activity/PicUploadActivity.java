package com.ljy.ljyutils.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyBitmapUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyRetrofitUtil;
import com.ljy.util.LjySystemUtil;
import com.ljy.util.LjyToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PicUploadActivity extends Activity {

    @BindView(R.id.imageView_show)
    ImageView mImageView;

    @BindView(R.id.btn_uploadImg)
    Button mButton;
    @BindView(R.id.text_info)
    TextView text_info;

    private Context mContext=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_upload);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music);
                uploadImg(bitmap);
            }
        });
    }

    private void uploadImg(Bitmap bitmap) {
        String imgBase64Str = LjyBitmapUtil.bitmapToBase64(bitmap);
        LjyLogUtil.i("imgBase64Str:" + imgBase64Str);
        mImageView.setImageBitmap(LjyBitmapUtil.base64ToBitmap(imgBase64Str));
        Map<String, String> params = new HashMap<>();
        params.put("pl", "2");
        params.put("cmd", "avatar");
        params.put("uid", "1177080");
        params.put("pwd", "e3ceb5881a0a1fdaad01296d7554868d");
        params.put("ct", imgBase64Str);

        String url = "http://www.anxin.com/pub/mobileucenter.aspx";
        LjyRetrofitUtil.getInstance().upload(url, params, new LjyRetrofitUtil.UpLoadCallBack() {
            @Override
            public void onSuccess(String successInfo) {
                text_info.setText(successInfo);
                LjySystemUtil.copyToClipboard(mContext,successInfo);
                LjyToastUtil.toast(mContext,"已复制到剪切板");
            }

            @Override
            public void onFail(String failInfo) {
                text_info.setText(failInfo);
            }
        });

    }
}
