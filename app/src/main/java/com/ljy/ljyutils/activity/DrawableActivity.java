package com.ljy.ljyutils.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ljy.ljyutils.R;
import com.ljy.ljyutils.base.BaseActivity;
import com.ljy.util.LjyColorUtil;
import com.ljy.util.LjyLogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Drawable:
 * 1.BitmapDrawable:
 * 详见drawable_bitmap.xml
 * 2.NinePatchDrawable
 * 详见drawable_nine_patch.xml
 * 3.ShapeDrawable/GradientDrawable
 * 详见:drawable_shape.xml
 * 4.LayerDrawable
 * 详见 drawable_layer.xml
 * 5.StateListDrawable:
 * 详见drawable_selector.xml
 * 6.LevelListDrawable
 * 详见drawable_level_list.xml
 * 7.TransitionDrawable
 * 详见drawable_transition.xml
 * 8.InsetDrawable
 * 详见drawable_inset.xml
 * 9.ScaleDrawable
 * 详见drawable_scale.xml
 * 10. ClipDrawable
 * 详见drawable_clip.xml
 */
public class DrawableActivity extends BaseActivity {
    @BindView(R.id.iv_content)
    ImageView imageViewContent;
    @BindView(R.id.checkboxParent)
    RelativeLayout checkboxParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);
        ButterKnife.bind(mActivity);
    }


    private int count = 0;

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bitmapDrawable:
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_music);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                bitmapDrawable.setDither(true);
                bitmapDrawable.setAntiAlias(true);
                bitmapDrawable.setFilterBitmap(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    bitmapDrawable.setMipMap(false);
                }
                bitmapDrawable.setGravity(Gravity.CENTER);
                bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);
                imageViewContent.setBackground(bitmapDrawable);
                break;
            case R.id.btn_ninePatchDrawable:
                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.chat_bg);
                byte[] chunk = bitmap2.getNinePatchChunk();
                boolean isNinePatchChunk = NinePatch.isNinePatchChunk(chunk);
                LjyLogUtil.i("isNinePatchChunk" + isNinePatchChunk);
                NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(getResources(),bitmap2, chunk, new Rect(), null);
                ninePatchDrawable.setDither(true);
                imageViewContent.setBackground(ninePatchDrawable);
                break;
            case R.id.btn_shapeDrawable:
                imageViewContent.setBackground(getResources().getDrawable(R.drawable.drawable_shape));
                break;
            case R.id.btn_layerDrawable:
                imageViewContent.setBackground(getResources().getDrawable(R.drawable.drawable_layer));
                break;
            case R.id.btn_selectorDrawable:
                checkboxParent.setVisibility(checkboxParent.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_LevelListDrawable:
                Drawable drawable = getResources().getDrawable(R.drawable.drawable_level_list);
                imageViewContent.setImageDrawable(drawable);
                drawable.setLevel(count++ % 4);
                imageViewContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageViewContent.setImageLevel(count++ % 4);
                    }
                });
                break;
            case R.id.btn_TransitionDrawable:
                imageViewContent.setBackground(getResources().getDrawable(R.drawable.drawable_transition));
                final TransitionDrawable transitionDrawable= (TransitionDrawable) imageViewContent.getBackground();
                transitionDrawable.startTransition(1000);
                imageViewContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transitionDrawable.reverseTransition(3000);
                    }
                });
                break;
            case R.id.btn_InsetDrawable:
                imageViewContent.setBackground(getResources().getDrawable(R.drawable.drawable_inset));
                break;
            case R.id.btn_scaleDrawable:
                imageViewContent.setBackground(getResources().getDrawable(R.drawable.drawable_scale));
                ScaleDrawable scaleDrawable= (ScaleDrawable) imageViewContent.getBackground();
                scaleDrawable.setLevel(1);//必须设置,否则不可见,范围0~10000,因为默认为0
                break;
            case R.id.btn_ClipDrawable:
//                imageViewContent.setBackground(getResources().getDrawable(R.drawable.drawable_clip));
//                ClipDrawable clipDrawable= (ClipDrawable) imageViewContent.getBackground();
                imageViewContent.setImageDrawable(getResources().getDrawable(R.drawable.drawable_clip));
                ClipDrawable clipDrawable= (ClipDrawable) imageViewContent.getDrawable();
                clipDrawable.setLevel(5000);//10000表示不裁剪,0表示全部裁剪
                break;
            case R.id.btn_CustomDrawable:
                CustomDrawable customDrawable=new CustomDrawable(LjyColorUtil.getInstance().randomColor());
                imageViewContent.setBackground(customDrawable);
                break;
        }
    }

    public class CustomDrawable extends Drawable{

        private final Paint mPaint;

        public CustomDrawable(int color) {
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(color);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            Rect rect=getBounds();
            float cx=rect.exactCenterX();
            float cy=rect.exactCenterY();
            canvas.drawCircle(cx,cy,Math.min(cx,cy),mPaint);
        }

        @Override
        public void setAlpha(int alpha) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {
            mPaint.setColorFilter(colorFilter);
            invalidateSelf();
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }
}
