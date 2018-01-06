package com.ljy.ljyutils.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ljy.ljyutils.R;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPhotoUtil;
import com.ljy.util.LjySystemUtil;
import com.ljy.util.LjyToastUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends AppCompatActivity {

    private Activity mActivity = this;
    private Context mContext;
    private LjyPhotoUtil photoUtil;
    //图片要保存的目录
    private String picFilesPath;

    @BindView(R.id.iv_1)
    ImageView mImageView1;
    private int REQUEST_CODE_CHOOSE = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        mContext = this.getApplicationContext();
        LjySystemUtil.noStatusBar(mActivity);
        initPath();
        initPhotoUtil();
    }

    private void initPath() {
        picFilesPath = Environment.getExternalStorageDirectory().getPath() + "/ljyPic/";
        File tempFile = new File(picFilesPath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
    }

    private void initPhotoUtil() {
        photoUtil = new LjyPhotoUtil(mActivity, new LjyPhotoUtil.CameraResult() {
            @Override
            public void onSuccess(String filePath) {
                LjyLogUtil.i("filePath:" + filePath);
                File file = new File(filePath);
                if (file.exists()) {
                    mImageView1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                }
            }

            @Override
            public void onFail(String message) {
                LjyToastUtil.toast(mContext, message);
            }
        });
    }

    private final int requestCodeCamera = 1001;
    private final int requestCodeCameraCut = 1002;
    private final int requestCodePicture = 1003;
    private final int requestCodePictureCut = 1004;
    private final int requestCodeMatisse = 1005;


    //按钮点击事件监听
    public void onPhotoBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                if (LjySystemUtil.hasPermission(mActivity, Manifest.permission.CAMERA) &&
                        LjySystemUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    doCamera();
                } else {
                    LjySystemUtil.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCodeCamera);
                }
                break;
            case R.id.btn_cameraCut:
                if (LjySystemUtil.hasPermission(mActivity, Manifest.permission.CAMERA) &&
                        LjySystemUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    doCameraCut();
                } else {
                    LjySystemUtil.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCodeCameraCut);
                }
                break;
            case R.id.btn_picture:
                if (LjySystemUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    doPicture();
                } else {
                    LjySystemUtil.requestPermission(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            requestCodePicture);
                }
                break;
            case R.id.btn_pictureCut:
                if (LjySystemUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    doPictureCut();
                } else {
                    LjySystemUtil.requestPermission(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            requestCodePictureCut);
                }
                break;
            case R.id.btn_Matisse:
                if (LjySystemUtil.hasPermission(mActivity, Manifest.permission.CAMERA) &&
                        LjySystemUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    doMatisse();
                } else {
                    LjySystemUtil.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCodeMatisse);
                }
                break;
        }
    }

    List<Uri> mSelected;
    MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoUtil.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            LjyLogUtil.i("mSelected: " + mSelected);
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        photoUtil.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        photoUtil.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LjySystemUtil.onPermissionResult(grantResults, new LjySystemUtil.PermissionResult() {
            @Override
            public void success() {
                switch (requestCode) {
                    case requestCodeCamera:
                        doCamera();
                        break;
                    case requestCodeCameraCut:
                        doCameraCut();
                        break;
                    case requestCodePicture:
                        doPicture();
                        break;
                    case requestCodePictureCut:
                        doPictureCut();
                        break;
                    case requestCodeMatisse:
                        doMatisse();
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void fail(List<Integer> disAllowIndexs) {
                for (int index : disAllowIndexs) {
                    LjyLogUtil.i(String.format("%s 权限被拒绝", permissions[index]));
                }
            }

        });
    }

    private void doMatisse() {
        //Matisse，来自知乎的PhotoPicker
        Matisse.from(mActivity)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(false)//是否显示所选的图片是第几个（右上角标），false则以对号标识选中
                .maxSelectable(9)
                .capture(false)//是否可以拍照，如果允许需要另行配置，具体可查看Matisse相关文章
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    private void doPictureCut() {
        String photoName = String.format("img_%d.jpg", System.currentTimeMillis());
        String photoPath = picFilesPath + photoName;
        photoUtil.getPictureAndCut(photoPath);
    }

    private void doPicture() {
        photoUtil.getPicture();
    }

    private void doCameraCut() {
        String photoName = String.format("img_%d.jpg", System.currentTimeMillis());
        String photoPath = picFilesPath + photoName;
        photoUtil.doCameraAndCut(photoPath);
    }

    private void doCamera() {
        String photoName = String.format("img_%d.jpg", System.currentTimeMillis());
        String photoPath = picFilesPath + photoName;
        photoUtil.doCamera(photoPath);
    }

    private static class MyHandler extends Handler {
        private WeakReference<PhotoActivity> outer;
        int count = 0;

        MyHandler(PhotoActivity outerAct) {
            outer = new WeakReference<>(outerAct);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PhotoActivity activity = outer == null ? null : outer.get();
            if (msg.what == 1) {
                if (activity.mSelected.size() > 0) {
                    if (count >= activity.mSelected.size())
                        count = 0;
                    Glide.with(activity.mActivity).load(activity.mSelected.get(count++)).into(activity.mImageView1);
                    sendEmptyMessageDelayed(1, 1200);
                }
            }
        }
    }
}
