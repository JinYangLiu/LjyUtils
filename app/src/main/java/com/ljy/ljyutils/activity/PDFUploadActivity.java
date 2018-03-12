package com.ljy.ljyutils.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyToastUtil;

import java.io.File;
import java.net.URISyntaxException;

public class PDFUploadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfupload);
    }

    public void uploadPDFClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的PDF文件"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
           LjyToastUtil.toast(mContext,"请安装文件管理器");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                try {
                    String filePath = LjyFileUtil.getPath(mContext, uri);
                    LjyToastUtil.toast(mContext, "文件路径：" + filePath);
                    LjyLogUtil.i("文件路径：" + filePath);
                    if (!TextUtils.isEmpty(filePath)&&filePath.endsWith(".pdf")) {
                        File file = new File(filePath);
                        if (file.exists()) {
                            byte[] fileBytes = LjyFileUtil.getBytesFromFile(file);
                            LjyLogUtil.i("fileBytes.len=" + fileBytes.length);
                            String base64File = Base64.encodeToString(fileBytes, Base64.DEFAULT);
                            LjyLogUtil.i("base64File.len=" + base64File.length());
                        }else{
                            LjyToastUtil.toast(mContext,"文件不存在");
                        }
                    }else {
                        LjyToastUtil.toast(mContext,"选择pdf类型的文件");
                    }
                } catch (URISyntaxException e) {
                    LjyLogUtil.e("URISyntaxException");
                }

            }
        }
    }
}
