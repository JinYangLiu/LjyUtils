package com.ljy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

/**
 * Created by Mr.LJY on 2017/12/25.
 *
 * 对Glide的封装
 *
 */

public class LjyGlideUtil {

    /**
     * 根据给定的imgUrl加载网络图片到指定的imageView
     */
    public static void loadImg(Context mContext, String imgUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imgUrl) || mContext == null||imageView==null) {
            String paraName=mContext==null?"context":imageView==null?"imageView":"imgUrl";
            LjyToastUtil.toastDebug(mContext,"参数"+paraName+"不能为空哦");
            return;
        }
        mContext = mContext.getApplicationContext();

        Glide.with(mContext).load(imgUrl).into(imageView);
    }

    /**
     * 根据imgUrl获取返回bitmap
     * 需要在子线程中使用
     * @param mContext
     * @param imgUrl
     * @return
     */
    public static Bitmap getBitmap(Context mContext, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl) || mContext == null)
            return null;
        mContext = mContext.getApplicationContext();

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(mContext)
                    .load(imgUrl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 通过回调获取bitmap
     *
     * @param mContext
     * @param imgUrl
     * @param callback
     */
    public static void getBitmap(Context mContext, String imgUrl, final CallBack callback) {
        if (TextUtils.isEmpty(imgUrl) || mContext == null)
            return;
        mContext = mContext.getApplicationContext();

        Glide.with(mContext)
                .load(imgUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        callback.onCall(resource);
                    }
                });
    }


    /**
     * 加载图片，添加回调
     *
     * @param mContext
     * @param imgUrl
     * @param target
     */
    public static void loadImg(Context mContext, String imgUrl, GlideDrawableImageViewTarget target) {
        if (TextUtils.isEmpty(imgUrl) || mContext == null)
            return;
        mContext = mContext.getApplicationContext();

        Glide.with(mContext).load(imgUrl).into(target);
    }


    /**
     * 加载图片，并以圆角或圆形显示
     *
     * @param mContext
     * @param imgUrl
     * @param imageView
     * @param transformation
     */
    public static void loadImg(Context mContext, String imgUrl, ImageView imageView, BitmapTransformation transformation) {
        if (TextUtils.isEmpty(imgUrl) || mContext == null||imageView==null)
            return;
        mContext = mContext.getApplicationContext();

        Glide.with(mContext.getApplicationContext()).load(imgUrl)//.error(R.drawable.mis_default_error)
                .transform(transformation)
                .into(imageView);
    }

    /**
     * 当列表在滑动的时候，调用pauseRequests()取消请求，滑动停止时，调用resumeRequests()恢复请求。这样是不是会好些呢？
     *
     * @param mContext
     */
    public static void resumeRequests(Context mContext) {
        if (mContext == null)
            return;
        mContext = mContext.getApplicationContext();

        Glide.with(mContext).resumeRequests();
    }

    public static void pauseRequests(Context mContext) {
        if (mContext == null)
            return;
        mContext = mContext.getApplicationContext();

        Glide.with(mContext).pauseRequests();
    }

    /**
     * 清除这个view的所有图片加载请求
     */
    public static void clear( View v) {
        if (v == null)
            return;

        Glide.clear(v);
    }

    /**
     * 清除缓存
     * @param mContext
     */
    public static void clearMemory(Context mContext) {
        if (mContext == null)
            return;
        mContext = mContext.getApplicationContext();
        Glide.get(mContext).clearMemory();
    }




    /**
     * 回调接口
     */
    public interface CallBack {
        void onCall(Bitmap resource);
    }

    /**
     * 圆形
     *
     * @author LJY
     */
    public static class CircleTransform extends BitmapTransformation {


        public CircleTransform(Context context) {
            super(context.getApplicationContext());
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    /**
     * 圆角
     */
    public static class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, float dp) {
            super(context.getApplicationContext());
            this.radius = dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName() + Math.round(radius);
        }
    }


}
