package com.ljy.ljyutils.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ljy.bean.DownloadBean;
import com.ljy.ljyutils.BuildConfig;
import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyEncryUtil;
import com.ljy.util.LjyFileUtil;
import com.ljy.util.LjyLogUtil;
import com.ljy.util.LjyPermissionUtil;
import com.ljy.util.LjyRetrofitUtil;
import com.ljy.util.LjyStringUtil;
import com.ljy.util.LjySystemUtil;
import com.ljy.util.LjyToastUtil;
import com.ljy.view.LjyMDDialogManager;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class AppUpdateActivity extends BaseActivity {

    @BindView(R.id.text_info)
    TextView textInfo;
    @BindView(R.id.text_info2)
    TextView textInfo2;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    File outputFileJingDong = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "京东商城" + "_1111" + ".apk");
    File outputFileQQ = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "qqMail" + "_1111" + ".apk");
    File outputFileWeChat = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "weChat" + "_1111" + ".apk");
    String updateAppPathJingDong = "http://storage.360buyimg.com/jdmobile/JDMALL-PC2.apk";
    String updateAppPathQQ = "http://app.mail.qq.com/cgi-bin/mailapp?latest=y&from=2&downloadclick=";
    String updateAppPathWeChat = "http://dldir1.qq.com/weixin/android/weixin661android1220_1.apk";
    private boolean isDone = false;
    private Disposable mDisposable;
    private DownloadBean mDownloadBean;
    private List<DownloadBean> beans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_update);
        ButterKnife.bind(mActivity);
        initView();
        beans.add(new DownloadBean(updateAppPathJingDong, outputFileJingDong));
        beans.add(new DownloadBean(updateAppPathQQ, outputFileQQ));
        beans.add(new DownloadBean(updateAppPathWeChat, outputFileWeChat));

    }

    private void initView() {
        String info = String.format("packageName:%s%nappName:%s%nversionName:%s%nversionCode:%d%n",
                getPackageName(), LjySystemUtil.getAppName(mContext), LjySystemUtil.getVersionName(mContext), LjySystemUtil.getVersionCode(mContext));
        textInfo.setText(info);
    }

    public void onUpdateBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update_jd:
                mDownloadBean = beans.get(0);
                updateApp();
                break;
            case R.id.btn_update_qqmail:
                mDownloadBean = beans.get(1);
                updateApp();
                break;
            case R.id.btn_update_wx:
                mDownloadBean = beans.get(2);
                updateApp();
                break;
            case R.id.btn_getApkHash:
                if (mDownloadBean == null)
                    return;
                byte[] apkHash = LjyEncryUtil.getMD5(LjyFileUtil.getBytesFromFile(mDownloadBean.getSaveFile()));
                String hash1 ;
                try {
                    hash1 = new String(apkHash,"utf-8");
                } catch (UnsupportedEncodingException e) {
                   hash1="";
                }
                String hash2 = LjyStringUtil.byte2base64(apkHash);
                String hash3 = LjyStringUtil.byte2hex(apkHash);
                textInfo2.setText("apk_hash_md5_hex: " + hash3);
                LjyLogUtil.i("hash1:" + hash1);
                LjyLogUtil.i("hash2:" + hash2);
                LjyLogUtil.i("hash3:" + hash3);
                break;
            case R.id.btn_pause:
                if (mDisposable != null && !mDisposable.isDisposed())
                    mDisposable.dispose();
                break;
            case R.id.btn_deleteApk:
                for (DownloadBean downloadBean : beans) {
                    LjyFileUtil.deleteFile(downloadBean.getSaveFile());
                }
                break;
            case R.id.btn_save:
                if (LjyPermissionUtil.hasPermission(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    saveApk();
                } else {
                    LjyPermissionUtil.requestPermission(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 666);
                }
                break;
            case R.id.btn_install:
                if (LjySystemUtil.getVersionCode(mContext) == 999) {
                    LjyToastUtil.toast(mContext, "已经是最新了哦");
                } else {
                    installApk();
                }
                break;
            default:
                break;
        }
    }

    private void saveApk() {
        final File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "LjyUtils" + "_1111" + ".apk");
        try {
            boolean isSave = LjyFileUtil.writeBytesToFile(mActivity.getAssets().open("LjyUtils.apk"), apkFile);
            LjyToastUtil.toast(mContext, isSave ? "保存成功" : "保存失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateApp() {
        if (mDownloadBean.isDone())
            return;
        if (mDisposable != null && mDisposable == mDownloadBean.getDisposable() && !mDisposable.isDisposed())
            return;
        if (LjyPermissionUtil.hasPermission(mActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            mDisposable = LjyRetrofitUtil.getInstance().download(mDownloadBean, new LjyRetrofitUtil.ProgressListener() {
                @Override
                public void onProgress(long progress, long total, boolean done) {
                    String info = "文件名:" + mDownloadBean.getSaveFile().getName() + ",\n源文件len:" + mDownloadBean.getTotal() + ",\n已下载len:" + mDownloadBean.getProgress() + ",\nprogress:" + progress + ",\ntotal:" + total + ",\ndone:" + done;
                    LjyLogUtil.i(info);
                    if (mDownloadBean.getTotal() > total) {
                        progress = mDownloadBean.getTotal() - total + progress;
                    } else {
                        mDownloadBean.setTotal(total);
                    }
                    mDownloadBean.setProgress(progress);
                    mDownloadBean.setDone(done);
                    Message message = new Message();
                    message.obj = info;
                    Bundle bundle = new Bundle();
                    bundle.putLong("progress", progress);
                    bundle.putLong("total", mDownloadBean.getTotal());
                    message.setData(bundle);
                    message.what = 111;
                    mHandler.sendMessage(message);
                    isDone = done;
                }
            }, new LjyRetrofitUtil.DownloadCallBack() {
                @Override
                public void onCompleted(boolean isSuccess) {
                    if (isDone && isSuccess) {
                        LjyToastUtil.toast(mContext, "下载成功");
                    } else {
                        LjyToastUtil.toast(mContext, "下载失败");
                    }
                }
            });
        } else {
            LjyPermissionUtil.requestPermission(mActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
        }
    }

    private void installApk() {

        final File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "LjyUtils" + "_1111" + ".apk");

        if (LjyFileUtil.getBytesFromFile(apkFile) == null) {
            LjyToastUtil.toast(mContext, "请先点击左侧按钮保存要安装的apk");
            return;
        }

        String fileMd5 = LjyStringUtil.byte2hex(LjyEncryUtil.getMD5(LjyFileUtil.getBytesFromFile(apkFile)));
        LjyLogUtil.i("fileMd5:" + fileMd5);
        if (!fileMd5.equals("8523F7F2E26F8B43B2A01D92FE1A4D5B")) {
            LjyToastUtil.toast(mContext, "apk的hash值不匹配哦");
            return;
        }

        //8.0以上允许未知来源权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (getPackageManager().canRequestPackageInstalls()) {
                doInstall(apkFile);
            } else {
                new LjyMDDialogManager(mActivity).alertSingleButton("申请权限",
                        "安装应用需要打开未知来源权限，请去设置中开启权限", "好的",
                        new LjyMDDialogManager.OnPositiveListener() {
                            @Override
                            public void positive() {
                                //没权限-申请权限
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                    mActivity.startActivityForResult(intent, 222);
                                }
                            }
                        }, false);
            }
        } else {
            doInstall(apkFile);
        }

    }

    private void doInstall(File apkFile) {
        Uri apkUri;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //判断是否是AndroidN(7.0)以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            apkUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
        } else {
            apkUri = Uri.fromFile(apkFile);
        }
        if (apkUri == null)
            return;
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    MyHandler mHandler = new MyHandler(this);

    //使用静态内部类和弱引用，避免内存泄漏
    static class MyHandler extends Handler {
        private WeakReference<AppUpdateActivity> mOuter;

        public MyHandler(AppUpdateActivity activity) {
            mOuter = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AppUpdateActivity outer = mOuter.get();
            if (outer != null) {
                switch (msg.what) {
                    case 111:
                        if (!outer.isFinishing()) {
                            outer.textInfo2.setText((String) msg.obj);
                            Bundle bundle = msg.getData();
                            outer.progressBar.setMax((int) bundle.getLong("total"));
                            outer.progressBar.setProgress((int) bundle.getLong("progress"));
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LjyPermissionUtil.onPermissionResult(grantResults, new LjyPermissionUtil.PermissionResult() {
            @Override
            public void success() {
                if (requestCode == 999) {
                    updateApp();
                }
                if (requestCode == 666) {
                    saveApk();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222)
            installApk();
    }
}
