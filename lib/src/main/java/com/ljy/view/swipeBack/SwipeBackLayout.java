package com.ljy.view.swipeBack;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ljy.lib.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Mr.LJY on 2018/1/31.
 */

public class SwipeBackLayout extends FrameLayout {
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int FULL_ALPHA = 255;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_ALL = 11;
    public static final int STATE_IDLE = 0;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_SETTLING = 2;
    private static final float DEFAULT_SCROLL_THRESHOLD = 0.3F;
    private static final int OVERSCROLL_DISTANCE = 10;
    private static final int[] EDGE_FLAGS = new int[]{1, 2, 8, 11};
    private int mEdgeFlag;
    private float mScrollThreshold;
    private Activity mActivity;
    private boolean mEnable;
    private View mContentView;
    private ViewDragHelper mDragHelper;
    private float mScrollPercent;
    private int mContentLeft;
    private int mContentTop;
    private List<SwipeBackLayout.SwipeListener> mListeners;
    private Drawable mShadowLeft;
    private Drawable mShadowRight;
    private Drawable mShadowBottom;
    private float mScrimOpacity;
    private int mScrimColor;
    private boolean mInLayout;
    private Rect mTmpRect;
    private int mTrackingEdge;

    public SwipeBackLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeBackLayoutStyle);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.mScrollThreshold = 0.3F;
        this.mEnable = true;
        this.mScrimColor = -1728053248;
        this.mTmpRect = new Rect();
        this.mDragHelper = ViewDragHelper.create(this, new SwipeBackLayout.ViewDragCallback());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeBackLayout, defStyle, R.style.SwipeBackLayout);
        int edgeSize = a.getDimensionPixelSize(R.styleable.SwipeBackLayout_edge_size, -1);
        if (edgeSize > 0) {
            this.setEdgeSize(edgeSize);
        }

        int mode = EDGE_FLAGS[a.getInt(R.styleable.SwipeBackLayout_edge_flag, 0)];
        this.setEdgeTrackingEnabled(mode);
        int shadowLeft = a.getResourceId(R.styleable.SwipeBackLayout_shadow_left, R.drawable.shadow_left);
        int shadowRight = a.getResourceId(R.styleable.SwipeBackLayout_shadow_right, R.drawable.shadow_right);
        int shadowBottom = a.getResourceId(R.styleable.SwipeBackLayout_shadow_bottom, R.drawable.shadow_bottom);
        this.setShadow(shadowLeft, 1);
        this.setShadow(shadowRight, 2);
        this.setShadow(shadowBottom, 8);
        a.recycle();
        float density = this.getResources().getDisplayMetrics().density;
        float minVel = 400.0F * density;
        this.mDragHelper.setMinVelocity(minVel);
        this.mDragHelper.setMaxVelocity(minVel * 2.0F);
    }

    public void setSensitivity(Context context, float sensitivity) {
        this.mDragHelper.setSensitivity(context, sensitivity);
    }

    private void setContentView(View view) {
        this.mContentView = view;
    }

    public void setEnableGesture(boolean enable) {
        this.mEnable = enable;
    }

    public void setEdgeTrackingEnabled(int edgeFlags) {
        this.mEdgeFlag = edgeFlags;
        this.mDragHelper.setEdgeTrackingEnabled(this.mEdgeFlag);
    }

    public void setScrimColor(int color) {
        this.mScrimColor = color;
        this.invalidate();
    }

    public void setEdgeSize(int size) {
        this.mDragHelper.setEdgeSize(size);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void setSwipeListener(SwipeBackLayout.SwipeListener listener) {
        this.addSwipeListener(listener);
    }

    public void addSwipeListener(SwipeBackLayout.SwipeListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }

        this.mListeners.add(listener);
    }

    public void removeSwipeListener(SwipeBackLayout.SwipeListener listener) {
        if (this.mListeners != null) {
            this.mListeners.remove(listener);
        }
    }

    public void setScrollThresHold(float threshold) {
        if (threshold < 1.0F && threshold > 0.0F) {
            this.mScrollThreshold = threshold;
        } else {
            throw new IllegalArgumentException("Threshold value should be between 0 and 1.0");
        }
    }

    public void setShadow(Drawable shadow, int edgeFlag) {
        if ((edgeFlag & 1) != 0) {
            this.mShadowLeft = shadow;
        } else if ((edgeFlag & 2) != 0) {
            this.mShadowRight = shadow;
        } else if ((edgeFlag & 8) != 0) {
            this.mShadowBottom = shadow;
        }

        this.invalidate();
    }

    public void setShadow(int resId, int edgeFlag) {
        this.setShadow(this.getResources().getDrawable(resId), edgeFlag);
    }

    public void scrollToFinishActivity() {
        int childWidth = this.mContentView.getWidth();
        int childHeight = this.mContentView.getHeight();
        int left = 0;
        int top = 0;
        if ((this.mEdgeFlag & 1) != 0) {
            left = childWidth + this.mShadowLeft.getIntrinsicWidth() + 10;
            this.mTrackingEdge = 1;
        } else if ((this.mEdgeFlag & 2) != 0) {
            left = -childWidth - this.mShadowRight.getIntrinsicWidth() - 10;
            this.mTrackingEdge = 2;
        } else if ((this.mEdgeFlag & 8) != 0) {
            top = -childHeight - this.mShadowBottom.getIntrinsicHeight() - 10;
            this.mTrackingEdge = 8;
        }

        this.mDragHelper.smoothSlideViewTo(this.mContentView, left, top);
        this.invalidate();
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!this.mEnable) {
            return false;
        } else {
            try {
                return this.mDragHelper.shouldInterceptTouchEvent(event);
            } catch (ArrayIndexOutOfBoundsException var3) {
                return false;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mEnable) {
            return false;
        } else {
            this.mDragHelper.processTouchEvent(event);
            return true;
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mInLayout = true;
        if (this.mContentView != null) {
            this.mContentView.layout(this.mContentLeft, this.mContentTop, this.mContentLeft + this.mContentView.getMeasuredWidth(), this.mContentTop + this.mContentView.getMeasuredHeight());
        }

        this.mInLayout = false;
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }

    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean drawContent = child == this.mContentView;
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (this.mScrimOpacity > 0.0F && drawContent && this.mDragHelper.getViewDragState() != 0) {
            this.drawShadow(canvas, child);
            this.drawScrim(canvas, child);
        }

        return ret;
    }

    private void drawScrim(Canvas canvas, View child) {
        int baseAlpha = (this.mScrimColor & -16777216) >>> 24;
        int alpha = (int) ((float) baseAlpha * this.mScrimOpacity);
        int color = alpha << 24 | this.mScrimColor & 16777215;
        if ((this.mTrackingEdge & 1) != 0) {
            canvas.clipRect(0, 0, child.getLeft(), this.getHeight());
        } else if ((this.mTrackingEdge & 2) != 0) {
            canvas.clipRect(child.getRight(), 0, this.getRight(), this.getHeight());
        } else if ((this.mTrackingEdge & 8) != 0) {
            canvas.clipRect(child.getLeft(), child.getBottom(), this.getRight(), this.getHeight());
        }

        canvas.drawColor(color);
    }

    private void drawShadow(Canvas canvas, View child) {
        Rect childRect = this.mTmpRect;
        child.getHitRect(childRect);
        if ((this.mEdgeFlag & 1) != 0) {
            this.mShadowLeft.setBounds(childRect.left - this.mShadowLeft.getIntrinsicWidth(), childRect.top, childRect.left, childRect.bottom);
            this.mShadowLeft.setAlpha((int) (this.mScrimOpacity * 255.0F));
            this.mShadowLeft.draw(canvas);
        }

        if ((this.mEdgeFlag & 2) != 0) {
            this.mShadowRight.setBounds(childRect.right, childRect.top, childRect.right + this.mShadowRight.getIntrinsicWidth(), childRect.bottom);
            this.mShadowRight.setAlpha((int) (this.mScrimOpacity * 255.0F));
            this.mShadowRight.draw(canvas);
        }

        if ((this.mEdgeFlag & 8) != 0) {
            this.mShadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right, childRect.bottom + this.mShadowBottom.getIntrinsicHeight());
            this.mShadowBottom.setAlpha((int) (this.mScrimOpacity * 255.0F));
            this.mShadowBottom.draw(canvas);
        }

    }

    public void attachToActivity(Activity activity) {
        this.mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{16842836});
        int background = a.getResourceId(0, 0);
        a.recycle();
        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        this.addView(decorChild);
        this.setContentView(decorChild);
        decor.addView(this);
    }

    public void computeScroll() {
        this.mScrimOpacity = 1.0F - this.mScrollPercent;
        if (this.mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private boolean mIsScrollOverValid;

        private ViewDragCallback() {
        }

        public boolean tryCaptureView(View view, int i) {
            boolean ret = SwipeBackLayout.this.mDragHelper.isEdgeTouched(SwipeBackLayout.this.mEdgeFlag, i);
            if (ret) {
                if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(1, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 1;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(2, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 2;
                } else if (SwipeBackLayout.this.mDragHelper.isEdgeTouched(8, i)) {
                    SwipeBackLayout.this.mTrackingEdge = 8;
                }

                if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty()) {
                    Iterator var4 = SwipeBackLayout.this.mListeners.iterator();

                    while (var4.hasNext()) {
                        SwipeBackLayout.SwipeListener listener = (SwipeBackLayout.SwipeListener) var4.next();
                        listener.onEdgeTouch(SwipeBackLayout.this.mTrackingEdge);
                    }
                }

                this.mIsScrollOverValid = true;
            }

            boolean directionCheck = false;
            if (SwipeBackLayout.this.mEdgeFlag != 1 && SwipeBackLayout.this.mEdgeFlag != 2) {
                if (SwipeBackLayout.this.mEdgeFlag == 8) {
                    directionCheck = !SwipeBackLayout.this.mDragHelper.checkTouchSlop(1, i);
                } else if (SwipeBackLayout.this.mEdgeFlag == 11) {
                    directionCheck = true;
                }
            } else {
                directionCheck = !SwipeBackLayout.this.mDragHelper.checkTouchSlop(2, i);
            }

            return ret & directionCheck;
        }

        public int getViewHorizontalDragRange(View child) {
            return SwipeBackLayout.this.mEdgeFlag & 3;
        }

        public int getViewVerticalDragRange(View child) {
            return SwipeBackLayout.this.mEdgeFlag & 8;
        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                SwipeBackLayout.this.mScrollPercent = Math.abs((float) left / (float) (SwipeBackLayout.this.mContentView.getWidth() + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth()));
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                SwipeBackLayout.this.mScrollPercent = Math.abs((float) left / (float) (SwipeBackLayout.this.mContentView.getWidth() + SwipeBackLayout.this.mShadowRight.getIntrinsicWidth()));
            } else if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                SwipeBackLayout.this.mScrollPercent = Math.abs((float) top / (float) (SwipeBackLayout.this.mContentView.getHeight() + SwipeBackLayout.this.mShadowBottom.getIntrinsicHeight()));
            }

            SwipeBackLayout.this.mContentLeft = left;
            SwipeBackLayout.this.mContentTop = top;
            SwipeBackLayout.this.invalidate();
            if (SwipeBackLayout.this.mScrollPercent < SwipeBackLayout.this.mScrollThreshold && !this.mIsScrollOverValid) {
                this.mIsScrollOverValid = true;
            }

            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty() && SwipeBackLayout.this.mDragHelper.getViewDragState() == 1 && SwipeBackLayout.this.mScrollPercent >= SwipeBackLayout.this.mScrollThreshold && this.mIsScrollOverValid) {
                this.mIsScrollOverValid = false;
                Iterator var6 = SwipeBackLayout.this.mListeners.iterator();

                while (var6.hasNext()) {
                    SwipeBackLayout.SwipeListener listener = (SwipeBackLayout.SwipeListener) var6.next();
                    listener.onScrollOverThreshold();
                }
            }

            if (SwipeBackLayout.this.mScrollPercent >= 1.0F && !SwipeBackLayout.this.mActivity.isFinishing()) {
                SwipeBackLayout.this.mActivity.finish();
                SwipeBackLayout.this.mActivity.overridePendingTransition(0, 0);
            }

        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int childWidth = releasedChild.getWidth();
            int childHeight = releasedChild.getHeight();
            int left = 0;
            int top = 0;
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                left = xvel <= 0.0F && (xvel != 0.0F || SwipeBackLayout.this.mScrollPercent <= SwipeBackLayout.this.mScrollThreshold) ? 0 : childWidth + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth() + 10;
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                left = xvel >= 0.0F && (xvel != 0.0F || SwipeBackLayout.this.mScrollPercent <= SwipeBackLayout.this.mScrollThreshold) ? 0 : -(childWidth + SwipeBackLayout.this.mShadowLeft.getIntrinsicWidth() + 10);
            } else if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                top = yvel >= 0.0F && (yvel != 0.0F || SwipeBackLayout.this.mScrollPercent <= SwipeBackLayout.this.mScrollThreshold) ? 0 : -(childHeight + SwipeBackLayout.this.mShadowBottom.getIntrinsicHeight() + 10);
            }

            SwipeBackLayout.this.mDragHelper.settleCapturedViewAt(left, top);
            SwipeBackLayout.this.invalidate();
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int ret = 0;
            if ((SwipeBackLayout.this.mTrackingEdge & 1) != 0) {
                ret = Math.min(child.getWidth(), Math.max(left, 0));
            } else if ((SwipeBackLayout.this.mTrackingEdge & 2) != 0) {
                ret = Math.min(0, Math.max(left, -child.getWidth()));
            }

            return ret;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            int ret = 0;
            if ((SwipeBackLayout.this.mTrackingEdge & 8) != 0) {
                ret = Math.min(0, Math.max(top, -child.getHeight()));
            }

            return ret;
        }

        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
            if (SwipeBackLayout.this.mListeners != null && !SwipeBackLayout.this.mListeners.isEmpty()) {
                Iterator var2 = SwipeBackLayout.this.mListeners.iterator();

                while (var2.hasNext()) {
                    SwipeBackLayout.SwipeListener listener = (SwipeBackLayout.SwipeListener) var2.next();
                    listener.onScrollStateChange(state, SwipeBackLayout.this.mScrollPercent);
                }
            }

        }
    }

    public interface SwipeListener {
        void onScrollStateChange(int var1, float var2);

        void onEdgeTouch(int var1);

        void onScrollOverThreshold();
    }
}
