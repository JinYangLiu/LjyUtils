package com.ljy.ljyutils.activity;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyBitmapUtil;
import com.ljy.util.LjyColorUtil;
import com.ljy.util.LjyEncryptUtil;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyGlideUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPermissionUtil;
import com.ljy.util.LjyPhotoUtil;
import com.ljy.util.LjyScreenUtils;
import com.ljy.util.LjyStringUtil;
import com.ljy.util.LjySystemUtil;
import com.ljy.util.LjyToastUtil;
import com.ljy.util.LjyViewUtil;
import com.ljy.util.disklrucache.LjyDiskLruCache;
import com.ljy.view.LjyMDDialogManager;
import com.ljy.view.LjyTagView;
import com.zhihu.matisse.Matisse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private Bitmap bitmapNew;
    private ProgressDialog progressDialog;
    private String readInfo;
    private String steganographyPath;
    private LruCache<String, Bitmap> memoryCache;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;//文件缓存50M
    private LjyDiskLruCache ljyDiskLruCache;
    private int DISK_CACHE_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LjyScreenUtils.noStatusBar(mActivity);
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

        steganographyPath = picFilesPath + File.separator + "pic_yxs.png";
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
                                    LjyBitmapUtil.compressInSampleSize(filePath, zipPath, 720, 1080, true);
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
                                    Bitmap bm = BitmapFactory.decodeFile(finalZipPath);
                                    LjyLogUtil.i("bmWidth:" + bm.getWidth());
                                    LjyLogUtil.i("bmHeight:" + bm.getHeight());
                                    mImageView1.setImageBitmap(bm);
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
        if (LjyPermissionUtil.hasPermission(mActivity, Manifest.permission.CAMERA) &&
                LjyPermissionUtil.hasPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                    LjyViewUtil.touchMove(tagView);
                    break;
                case R.id.btn_clearlabel:
                    if (tagsContainer != null)
                        tagsContainer.removeAllViews();
                    break;
                case R.id.btn_rgb:
                    changeRgb();
                    break;
                case R.id.btn_writeInfo:
                    new LjyMDDialogManager(mActivity).alertEditTextMD("请输入要写入的字符串:", new LjyMDDialogManager.PositiveListenerText() {
                        @Override
                        public void positive(String text) {
                            if (TextUtils.isEmpty(text)) {
                                text = "LJY是个码农哦，192.168.0.1";
                                LjyToastUtil.toast(mContext, "没有输入则默认信息为: " + text);
                            }
                            if (text.contains("#&#")) {
                                LjyToastUtil.toast(mContext, "需要写入的信息不能包含: #&#");
                                return;
                            }
                            text += "#&#";
                            writeInfo(text);
                        }
                    }, null);
                    break;
                case R.id.btn_readInfo:
                    readInfo();
                    break;
                case R.id.btn_bitmapCache:
                    bitmapCache();
                    break;
                case R.id.btn_imageloader:
                    startActivity(new Intent(mContext,ImageloaderActivity.class));
                default:
                    break;
            }
        } else {
            LjyPermissionUtil.requestPermission(mActivity, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCodeCamera);
        }
    }

    /**
     * 简单的实现一下图片的三级缓存
     * <p>
     * 缓存策略是一个通用思想，可以用在很多场景，实际开发中经常需要为bitmap做缓存
     * <p>
     * Lru：Least Recently Used,最近最少使用算法：当缓存快满时，会淘汰最近最少使用的缓存目标
     * 在Android应用的开发中，为了防止内存溢出，在处理一些占用内存大而且生命周期较长的对象时候，可以尽量应用软引用和弱引用技术。
     * 1。LruCache：用于内存缓存，内部使用LinkedHashMap以强引用的方式存储外界的缓存对象
     * 2。DiskLruCache：用于存储设备缓存
     */
    private void bitmapCache() {
        //测试图片的url，这里用的是我github的头像的url
        String url = "https://avatars1.githubusercontent.com/u/19702574?s=460&v=4";
        //LruCache初始化
        if (memoryCache == null) {
            int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            int cacheSize = maxMemory / 8;//设置缓存大小为当前进程可用内存的的1/8，单位kb
            memoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight() / 1024;
                }
            };
        }
        //DiskLruCache初始化
        if (ljyDiskLruCache == null) {
            File diskCacheDir = LjyFileUtil.getDiskCacheDir(mContext, "ljyDiskLruCache");
            try {
                ljyDiskLruCache = LjyDiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //三级缓存：
        //1。获取内存缓存
        Bitmap bitmap = memoryCache.get("userImg");
        if (bitmap == null) {
            //2。如果内存缓存中为null，获取磁盘缓存
            try {
                //将url取hash值，以防url直接使用时有特殊字符的情况
                final String key = LjyEncryptUtil.getMD5(url);
                LjyDiskLruCache.Snapshot snapshot = ljyDiskLruCache.get(key);
                if (snapshot != null) {
                    FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                    FileDescriptor fileDescriptor = fileInputStream.getFD();
                    bitmap = LjyBitmapUtil.compressInSampleSize(fileDescriptor, 720, 1080);
                    if (bitmap != null) {
                        LjyToastUtil.showSnackBar(mImageView1, "磁盘缓存中取得bitmap");
                        //显示到UI界面
                        mImageView1.setImageBitmap(bitmap);
                        //存储内存缓存
                        memoryCache.put("userImg", bitmap);
                    }
                }

                if (bitmap == null) {
                    //3。如果磁盘缓存中为null，从网络加载
                    //这里只是为例演示，其实Glide本身已经做了三级缓存
                    LjyGlideUtil.getBitmap(mContext, url, new LjyGlideUtil.CallBack() {
                        @Override
                        public void onCall(Bitmap resource) {
                            LjyToastUtil.showSnackBar(mImageView1, "（伪）从网络上取得bitmap");
                            //显示到UI界面
                            mImageView1.setImageBitmap(resource);
                            //存储内存缓存
                            memoryCache.put("userImg", resource);
                            //存储磁盘缓存
                            try {
                                LjyDiskLruCache.Editor editor = ljyDiskLruCache.edit(key);
                                if (editor != null) {
                                    OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                                    if (LjyBitmapUtil.bitmap2Stream(resource, outputStream)) {
                                        editor.commit();
                                    } else {
                                        editor.abort();
                                    }
                                    ljyDiskLruCache.flush();
                                } else {
                                    LjyLogUtil.i("editor = null");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LjyToastUtil.showSnackBar(mImageView1, "从内存缓存中取得bitmap");
            //显示到UI界面
            mImageView1.setImageBitmap(bitmap);
        }
    }

    private void readInfo() {
        final Bitmap bitmap = BitmapFactory.decodeFile(steganographyPath);
        if (bitmap == null) {
            LjyToastUtil.toast(mContext, "请先写入文件哦");
            return;
        }
        if (progressDialog == null)
            progressDialog = LjyMDDialogManager.getWaitingDialog(mActivity, null, null, false);
        progressDialog.setMessage("读取中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int x = bitmap.getWidth();
                int y = bitmap.getHeight();
                StringBuffer stringBuffer = new StringBuffer();
                for (int j = 0; j < y; j++) {
                    for (int i = 0; i < x; i++) {
                        int pixel = bitmap.getPixel(i, j);
                        readitem(pixel, stringBuffer);
                    }
                }
                String infoBinary = stringBuffer.toString().split(" 00100011 00100110 00100011")[0];

                LjyLogUtil.i("infoBinary_read:" + infoBinary);
                readInfo = LjyStringUtil.binaryToString(infoBinary);
                LjyLogUtil.i("readInfo:" + readInfo);
                mHandler.sendEmptyMessage(777);
                tempRead = 0;
            }
        }).start();


    }

    int tempRead = 0;

    private void readitem(int pixel, StringBuffer stringBuffer) {
        int alpha = LjyColorUtil.alpha(pixel);
        int red = LjyColorUtil.red(pixel);
        int green = LjyColorUtil.green(pixel);
        int blue = LjyColorUtil.blue(pixel);

        String binaryAlpha = Integer.toBinaryString(alpha);//int转2进制
        stringBuffer.append(binaryAlpha.charAt(binaryAlpha.length() - 1));
        tempRead++;
        if (tempRead == 8) {
            stringBuffer.append(" ");
            tempRead = 0;
        }

        String binaryRed = Integer.toBinaryString(red);//int转2进制
        stringBuffer.append(binaryRed.charAt(binaryRed.length() - 1));
        tempRead++;
        if (tempRead == 8) {
            stringBuffer.append(" ");
            tempRead = 0;
        }

        String binaryGreen = Integer.toBinaryString(green);//int转2进制
        stringBuffer.append(binaryGreen.charAt(binaryGreen.length() - 1));
        tempRead++;
        if (tempRead == 8) {
            stringBuffer.append(" ");
            tempRead = 0;
        }

        String binaryBlue = Integer.toBinaryString(blue);//int转2进制
        stringBuffer.append(binaryBlue.charAt(binaryBlue.length() - 1));
        tempRead++;
        if (tempRead == 8) {
            stringBuffer.append(" ");
            tempRead = 0;
        }
    }

    private void writeInfo(final String info) {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mountain);
        if (progressDialog == null)
            progressDialog = LjyMDDialogManager.getWaitingDialog(mActivity, null, null, false);
        progressDialog.setMessage("写入中...");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String infoBinary = LjyStringUtil.stringToBinary(info);
                LjyLogUtil.i("infoBinary_write:" + infoBinary);
                int x = bitmap.getWidth();
                int y = bitmap.getHeight();
                bitmapNew = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
                index = 0;
                for (int j = 0; j < y; j++) {
                    for (int i = 0; i < x; i++) {
                        int pixel = bitmap.getPixel(i, j);
                        int pixelNew = writeItem(pixel, infoBinary);
                        bitmapNew.setPixel(i, j, pixelNew);
                    }
                }

                //保存到文件中：
                LjyLogUtil.i("steganographyPath:" + steganographyPath);
                LjyBitmapUtil.bitmapToFile(bitmapNew, steganographyPath);
                LjyLogUtil.i("writeInfo over");
                mHandler.sendEmptyMessage(666);
            }
        }).start();

    }

    int index = 0;

    private int writeItem(int pixel, String infoBinary) {
        int alpha = LjyColorUtil.alpha(pixel);
        int red = LjyColorUtil.red(pixel);
        int green = LjyColorUtil.green(pixel);
        int blue = LjyColorUtil.blue(pixel);

        String binaryAlpha = Integer.toBinaryString(alpha);//int转2进制
        binaryAlpha = binaryAlpha.substring(0, binaryAlpha.length() - 1);
        binaryAlpha += index < infoBinary.length() ? infoBinary.charAt(index++) : "0";
        int alphaNew = Integer.parseInt(binaryAlpha, 2);

        String binaryRed = Integer.toBinaryString(red);//int转2进制
        binaryRed = binaryRed.substring(0, binaryRed.length() - 1);
        binaryRed += index < infoBinary.length() ? infoBinary.charAt(index++) : "0";
        int redNew = Integer.parseInt(binaryRed, 2);

        String binaryGreen = Integer.toBinaryString(green);//int转2进制
        binaryGreen = binaryGreen.substring(0, binaryGreen.length() - 1);
        binaryGreen += index < infoBinary.length() ? infoBinary.charAt(index++) : "0";
        int greenNew = Integer.parseInt(binaryGreen, 2);

        String binaryBlue = Integer.toBinaryString(blue);//int转2进制
        binaryBlue = binaryBlue.substring(0, binaryBlue.length() - 1);
        binaryBlue += index < infoBinary.length() ? infoBinary.charAt(index++) : "0";
        int blueNew = Integer.parseInt(binaryBlue, 2);

        return Color.argb(alphaNew, redNew, greenNew, blueNew);
    }

    private void changeRgb() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.mountain);
        int x = bitmap.getWidth();
        int y = bitmap.getHeight();
        Bitmap bitmapNew = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                int pixel = bitmap.getPixel(i, j);
                int alpha = LjyColorUtil.alpha(pixel);
                int red = LjyColorUtil.red(pixel);
                int green = LjyColorUtil.green(pixel);
                int blue = LjyColorUtil.blue(pixel);
//                                String binaryString= Integer.toBinaryString(pixel);//int转2进制
//                                LjyLogUtil.i( String.format("pixel:%s,alpha:%s,red:%s,green:%s,blue:%s,binaryString:%s",pixel,alpha,red,green,blue,binaryString));
                bitmapNew.setPixel(i, j, Color.argb(alpha, 255, green, blue));
            }
        }
        mImageView1.setImageBitmap(bitmapNew);
        LjyLogUtil.i("changeRgb over");
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
        LjyPermissionUtil.onPermissionResult(grantResults, new LjyPermissionUtil.PermissionResult() {
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
            } else if (msg.what == 666) {
                if (activity.bitmapNew != null)
                    activity.mImageView1.setImageBitmap(activity.bitmapNew);
                activity.progressDialog.dismiss();
            } else if (msg.what == 777) {
                LjyToastUtil.toast(activity.mContext, "read:" + activity.readInfo);
                activity.progressDialog.dismiss();
            }
        }
    }
}
