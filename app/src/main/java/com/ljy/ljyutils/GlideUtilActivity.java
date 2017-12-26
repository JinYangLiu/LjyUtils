package com.ljy.ljyutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ljy.lib.LjyGlideUtil;
import com.ljy.lib.LjyLogUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlideUtilActivity extends AppCompatActivity {

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    @BindView(R.id.iv_5)
    ImageView iv5;
    private String imgUrl1;
    private Context mContext = this;
    private Bitmap bitmap;
    private String imgUrl2;
    private String imgUrl3;
    private String imgUrl4;
    private String imgUrl5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_util);
        ButterKnife.bind(this);
        initUrl();
    }

    private void initUrl() {
        imgUrl1 = "https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3035449810,1990226329&fm=173&s=5F024F818A1768CC5BB119E00300B0B1&w=640&h=427&img.JPEG";
        imgUrl2="https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3488420919,4057488548&fm=173&s=47006CA366434FFD409DEC3A03000073&w=550&h=413&img.JPEG";
        imgUrl3="https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=4063553433,1286266556&fm=173&s=BCB0399D4E126FCC30152ACD0300F0A2&w=499&h=424&img.JPEG";
        imgUrl4="https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2673965485,1528016629&fm=173&s=CE07FC079CD859EB1C78A9740300A062&w=500&h=358&img.JPEG";
        imgUrl5="https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=2552578485,1421460773&fm=173&s=5A53A140E976B5DC12ECCC9A0300C093&w=290&h=258&img.JPEG";
    }

    public void onGlideBtnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clearMemory:
                LjyGlideUtil.clearMemory(mContext);
                break;
            case R.id.btn_pauseRequests:
                LjyGlideUtil.pauseRequests(mContext);
                break;
            case R.id.btn_resumeRequests:
                LjyGlideUtil.resumeRequests(mContext);
                break;
            case R.id.btn_show_iv:
                LjyGlideUtil.loadImg(mContext, imgUrl1, iv1);
                setImageView2();
                setImageView3();
                setImageView4();
                setImageView5();

                break;
            case R.id.btn_clear_iv:
                LjyGlideUtil.clear(iv1);
                iv2.setImageBitmap(null);
                iv3.setImageBitmap(null);
                LjyGlideUtil.clear(iv4);
                LjyGlideUtil.clear(iv5);
                break;
            default:
                break;
        }
    }

    private void setImageView5() {
        LjyGlideUtil.loadImg(mContext,imgUrl5,iv5,new LjyGlideUtil.CircleTransform(mContext));
    }

    private void setImageView4() {
        LjyGlideUtil.loadImg(mContext,imgUrl4,new GlideDrawableImageViewTarget(iv4){

            @Override
            public void onStart() {
                super.onStart();
                LjyLogUtil.i("---onStart");
            }

            @Override
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
                LjyLogUtil.i("---onLoadStarted");
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                super.onResourceReady(resource, animation);
                LjyLogUtil.i("---onResourceReady");
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                LjyLogUtil.i("---onLoadFailed");
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                super.onLoadCleared(placeholder);
                LjyLogUtil.i("---onLoadCleared");
            }

            @Override
            public void onStop() {
                super.onStop();
                LjyLogUtil.i("---onStop");
            }

            @Override
            public void onDestroy() {
                super.onDestroy();
                LjyLogUtil.i("---onDestroy");
            }
        });

    }

    private void setImageView3() {
        LjyGlideUtil.getBitmap(mContext, imgUrl3, new LjyGlideUtil.CallBack() {
            @Override
            public void onCall(Bitmap resource) {
                iv3.setImageBitmap(resource);
            }
        });
    }

    private void setImageView2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bitmap = LjyGlideUtil.getBitmap(mContext, imgUrl2);
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    Handler mHandler = new MyHandler(this);

    //使用静态内部类和弱引用，避免内存泄漏
    static class MyHandler extends Handler {
        private WeakReference<GlideUtilActivity> mOuter;

        public MyHandler(GlideUtilActivity activity) {
            mOuter = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GlideUtilActivity outer = mOuter.get();
            if (outer != null) {
                switch (msg.what) {
                    case 1:
                        if (!outer.isFinishing()) {
                            outer.iv2.setImageBitmap(outer.bitmap);
                        }
                        break;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(1);
    }
}
