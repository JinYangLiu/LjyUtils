package com.ljy.ljyutils.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyBitmapUtil;
import com.ljy.util.LjyColorUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPhotoUtil;
import com.ljy.util.LjySystemUtil;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyMDDialogManager;
import com.ljy.view.LjyTagView;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity {

    private LjyPhotoUtil photoUtil;
    //图片要保存的目录
    private String picFilesPath;

    @BindView(R.id.iv_1)
    ImageView mImageView1;
    @BindView(R.id.tags_container)
    RelativeLayout tagsContainer;
    private int REQUEST_CODE_CHOOSE = 222;
    private boolean isWatermark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LjySystemUtil.noStatusBar(mActivity);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(mActivity);
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
            public void onSuccess(final String filePath) {
                if (isZip) {
                    new Thread() {
                        public void run() {
                            String zipPath = "";
                            switch (zipType) {
                                case quality:
                                    zipPath = getNewPicturePathByTimeStamp("quality");
                                    LjyBitmapUtil.compressQuality(BitmapFactory.decodeFile(filePath), zipPath, 25);
                                    break;
                                case size:
                                    zipPath = getNewPicturePathByTimeStamp("size");
                                    LjyBitmapUtil.compressSize(BitmapFactory.decodeFile(filePath), zipPath, 4);
                                    break;
                                case inSampleSize:
                                    zipPath = getNewPicturePathByTimeStamp("inSampleSize");
                                    LjyBitmapUtil.compressSample(filePath, zipPath, 4);
                                    break;
                                case huffman:
                                    zipPath = getNewPicturePathByTimeStamp("huffman");
                                    LjyBitmapUtil.compressHuffman(BitmapFactory.decodeFile(filePath), zipPath, 25, true);
                                    break;
                                case mix:
                                    zipPath = getNewPicturePathByTimeStamp("mix");
                                    LjyBitmapUtil.compressMix(BitmapFactory.decodeFile(filePath), zipPath, 90);
                                    break;
                            }
                            if (TextUtils.isEmpty(zipPath))
                                return;
                            final String finalZipPath = zipPath;
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mImageView1.setImageBitmap(BitmapFactory.decodeFile(finalZipPath));
                                }
                            });
                        }
                    }.start();
                } else if (isWatermark) {
                    new Thread() {
                        public void run() {
                            //添加水印前先压缩一下，避免过大
                            String zipPath = getNewPicturePathByTimeStamp("mix");
                            LjyBitmapUtil.compressMix(BitmapFactory.decodeFile(filePath), zipPath, 90);
                            //添加水印
                            final String watermarkPath = getNewPicturePathByTimeStamp("watermark");
                            LjyBitmapUtil.addWatermark(BitmapFactory.decodeFile(zipPath), "LJY_ABCDEF", 0x55f44336, watermarkPath);
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mImageView1.setImageBitmap(BitmapFactory.decodeFile(watermarkPath));
                                }
                            });
                        }
                    }.start();
                } else {
                    File file = new File(filePath);
                    if (file.exists()) {
                        mImageView1.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                    }
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

    private boolean isZip = false;
    private ZipType zipType;
    int temp = 1;


    //按钮点击事件监听
    public void onPhotoBtnClick(View view) {
        if (LjySystemUtil.hasPermission(mActivity, Manifest.permission.CAMERA) &&
                LjySystemUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            switch (view.getId()) {
                case R.id.btn_camera:
                    doCamera();
                    break;
                case R.id.btn_cameraCut:
                    doCameraCut();
                    break;
                case R.id.btn_picture:
                    doPicture();
                    break;
                case R.id.btn_pictureCut:
                    doPictureCut();
                    break;
                case R.id.btn_Matisse:
                    doPictures();
                    break;
                case R.id.btn_quality:
                    doZip(ZipType.quality);
                    break;
                case R.id.btn_size:
                    doZip(ZipType.size);
                    break;
                case R.id.btn_inSampleSize:
                    doZip(ZipType.inSampleSize);
                    break;
                case R.id.btn_Huffman:
                    doZip(ZipType.huffman);
                    break;
                case R.id.btn_mix:
                    doZip(ZipType.mix);
                    break;
                case R.id.btn_watermark:
                    doWatermark();
                    break;
                case R.id.btn_label:
                    final LjyTagView tagView = LjyTagView.getTag(mContext, "标签_" + temp++, tagsContainer);
                    tagView.setOnTagLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            LjyLogUtil.i("onLongClick");
                            LjyMDDialogManager dialog = new LjyMDDialogManager(mActivity);
                            dialog.alertTwoButton("温馨提示", "是否删除" + tagView.getContent(), "确定", new LjyMDDialogManager.OnPositiveListener() {
                                @Override
                                public void positive() {
                                    if (tagsContainer != null)
                                        tagsContainer.removeView(tagView);
                                }
                            }, "取消", null, false);

                            return false;
                        }
                    });

                    tagsContainer.addView(tagView);
                    break;
                case R.id.btn_clearlabel:
                    if (tagsContainer != null)
                        tagsContainer.removeAllViews();
                    break;
                case R.id.btn_rgb:
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mountain);
                    int x = bitmap.getWidth();
                    int y = bitmap.getHeight();
                    Bitmap bitmapNew=Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
                    for (int j = 0; j < y; j++) {
                        for (int i = 0; i < x; i++) {
                                int pixel = bitmap.getPixel(i, j);
                                int alpha=LjyColorUtil.alpha(pixel);
                                int red = LjyColorUtil.red(pixel);
                                int green = LjyColorUtil.green(pixel);
                                int blue = LjyColorUtil.blue(pixel);
//                                String binaryString= Integer.toBinaryString(pixel);//int转2进制
//                                LjyLogUtil.i( String.format("pixel:%s,alpha:%s,red:%s,green:%s,blue:%s,binaryString:%s",pixel,alpha,red,green,blue,binaryString));
                            bitmapNew.setPixel(i, j, Color.argb(alpha,255,green,blue));
                        }
                    }
                    mImageView1.setImageBitmap(bitmapNew);
                    LjyLogUtil.i("bitmapNew over");
                    break;
                default:
                    break;
            }
        } else {
            LjySystemUtil.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCodeCamera);
        }
    }

    private void doWatermark() {
        isWatermark = true;
        photoUtil.getPicture();
    }

    enum ZipType {
        quality, size, inSampleSize, huffman, mix
    }

    private void doZip(ZipType zipType) {
        photoUtil.getPicture();
        isZip = true;
        this.zipType = zipType;
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
                        doPictures();
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

    private void doPictures() {
        photoUtil.getPictures(mActivity, REQUEST_CODE_CHOOSE);
    }

    private void doPictureCut() {
        isZip = false;
        photoUtil.getPictureAndCut(getNewPicturePathByTimeStamp("pictureCut"));
    }

    private void doPicture() {
        isZip = false;
        photoUtil.getPicture();
    }

    private void doCameraCut() {
        isZip = false;
        photoUtil.doCameraAndCut(getNewPicturePathByTimeStamp("cameraCut"));
    }


    private void doCamera() {
        isZip = false;
        photoUtil.doCamera(getNewPicturePathByTimeStamp("camera"));
    }

    @NonNull
    private String getNewPicturePathByTimeStamp(String tag) {
        String photoName = String.format("img_%s_%d.jpg", tag, System.currentTimeMillis());
        return picFilesPath + photoName;
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
