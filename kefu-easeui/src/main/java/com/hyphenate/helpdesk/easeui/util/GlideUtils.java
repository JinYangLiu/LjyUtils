package com.hyphenate.helpdesk.easeui.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import com.hyphenate.helpdesk.R;

import static com.bumptech.glide.Glide.with;

/**
 * Glide框架的封装
 *
 * @author LJY
 */
public class GlideUtils {

    /**
     * 加载网络图片
     *
     * @param mContext
     * @param imgurl
     * @param iv_userimage
     */
    public static void loadImg(Context mContext, String imgurl, ImageView iv_userimage) {

        if (TextUtils.isEmpty(imgurl)) {
            iv_userimage.setImageResource(R.drawable.hd_default_avatar);
            return;
        }
        with(mContext.getApplicationContext()).load(imgurl).into(iv_userimage);//.error(R.drawable.mis_default_error)
    }

    public static Bitmap getImgBitmap(Context mContext, String imgurl) {

        try {
            Bitmap bitmap = Glide.with(mContext.getApplicationContext())
                    .load(imgurl)
                    .asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            return bitmap;
        } catch (Exception e) {
           Log.i("ljy"," Exception:"+e.getLocalizedMessage());
        }
        return null;
    }


    /**
     * 加载图片，并监听状态
     *
     * @param mContext
     * @param imgurl
     * @param iv_userimage
     * @param target
     */
    public static void loadImg(Context mContext, String imgurl, ImageView iv_userimage, GlideDrawableImageViewTarget target) {

        if (TextUtils.isEmpty(imgurl)) {
            iv_userimage.setImageResource(R.drawable.hd_default_avatar);
            return;
        }
        with(mContext.getApplicationContext()).load(imgurl).into(target);//.error(R.drawable.mis_default_error)
    }
    /**
     * 获取网络图片
     * @param url
     * @param callback
     */
    public static void getBitmap(Context mContext,String url , final CallBack callback) {
        if (Util.isOnMainThread()) {
            Glide.with(mContext.getApplicationContext()).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    callback.onCall(resource);
                }
            });
        }
    }
    public interface CallBack{
        void onCall(Bitmap resource);
    }


    public static void loadImg(Context mContext, int resId, ImageView iv_userimage, BitmapTransformation transformation) {
        with(mContext.getApplicationContext()).load(resId)//.error(R.drawable.mis_default_error)
                .transform(transformation)
                .into(iv_userimage);
    }
    /**
     * 加载图片，并以圆角或圆形显示
     *
     * @param mContext
     * @param imgurl
     * @param iv_userimage
     * @param transformation
     */
    public static void loadImg(Context mContext, String imgurl, ImageView iv_userimage, BitmapTransformation transformation) {

        if (TextUtils.isEmpty(imgurl)) {
            iv_userimage.setImageResource(R.drawable.hd_default_avatar);
            return;
        }
        with(mContext.getApplicationContext()).load(imgurl)//.error(R.drawable.mis_default_error)
                .transform(transformation)
                .into(iv_userimage);
    }

    /**
     * 圆形
     *
     * @author LJY
     */
    public static class CircleTransform extends BitmapTransformation {


        public CircleTransform(Context context) {
            super(context);
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
    public class GlideRoundTransform extends BitmapTransformation {

        private float radius = 0f;

        public GlideRoundTransform(Context context) {
            this(context, 4);
        }

        public GlideRoundTransform(Context context, float dp) {
            super(context);
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
    //当Activity销毁是停止请求图片
    public static void pauseRequest(Activity activity){
        if (Util.isOnMainThread() && !activity.isFinishing()&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1&&!activity.isDestroyed()) {
            Glide.with(activity).pauseRequests();
        }

    }

}
