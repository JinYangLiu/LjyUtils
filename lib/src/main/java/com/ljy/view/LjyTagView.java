package com.ljy.view;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ljy.lib.R;
import com.ljy.util.LjyViewUtil;

import java.io.Serializable;

/**
 * Created by Mr.LJY on 2018/1/8.
 *
 * 仿Nice标签View
 */
public class LjyTagView extends RelativeLayout {
    public Animation blackAnimation1;
    public Animation blackAnimation2;
    public Animation whiteAnimation;

    public TextView textview;// 文字描述显示View
    public ImageView blackIcon1;// 黑色圆圈View
    public ImageView blackIcon2;// 黑色圆圈View
    protected ImageView brandIcon;// 白色圆圈View
    protected ImageView geoIcon;// 白色定位圆圈View
    public ImageView viewPointer;// 指向brandIcon或者geoIcon，根据设置的类型的不同

    public boolean isShow = false;
    private String content;
    private Handler handler = new Handler();
    private OnLongClickListener onLongClickListener;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private LjyTagView(Context context) {
        this(context, null);
    }

    private LjyTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.blackAnimation1 = AnimationUtils.loadAnimation(context, R.anim.black_anim);
        this.blackAnimation2 = AnimationUtils.loadAnimation(context, R.anim.black_anim);
        this.whiteAnimation = AnimationUtils.loadAnimation(context, R.anim.white_anim);
        LayoutInflater.from(context).inflate(R.layout.view_tag, this);
        this.textview = ((TextView) findViewById(R.id.text));
        this.textview.getBackground().setAlpha(178);
        this.textview.setVisibility(View.VISIBLE);
        this.blackIcon1 = ((ImageView) findViewById(R.id.blackIcon1));
        this.blackIcon2 = ((ImageView) findViewById(R.id.blackIcon2));
        this.brandIcon = ((ImageView) findViewById(R.id.brandIcon));
        this.geoIcon = ((ImageView) findViewById(R.id.geoIcon));
        this.viewPointer = brandIcon;
        setVisible();
    }

    public static LjyTagView getTag(Context context, String content, RelativeLayout parentView) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.bid = 2L;
        tagInfo.bname = content;
        tagInfo.direct = TagInfo.Direction.Left;
        tagInfo.type = TagInfo.Type.CustomPoint;
        LjyTagView tagView = new LjyTagView(context);
        tagView.setData(tagInfo);
        tagView.setContent(content);
        tagView.setClickable(true);
        LayoutParams params = new LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tagView.setLayoutParams(params);
        tagView.setTouch(parentView);

        return tagView;
    }

    private void setTouch(RelativeLayout parentView) {
        final int parentViewWidth = parentView.getWidth();
        final int parentViewHeight = parentView.getHeight();
        final int viewWidth = LjyViewUtil.getViewWidth(this);
        final int viewHeight = LjyViewUtil.getViewHeight(this);
        setOnTouchListener(new OnTouchListener() {
            public int lastY;
            public int lastX;
            public int firstX;
            public int firstY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = firstX = (int) event.getRawX();
                        lastY = firstY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        int dx = x - lastX;
                        int dy = y - lastY;

                        int top = v.getTop() + dy;

                        int left = v.getLeft() + dx;

                        if (top <= 0) {
                            top = 0;
                        }

                        if (top >= parentViewHeight - viewHeight) {
                            top = parentViewHeight - viewHeight;
                        }
                        if (left >= parentViewWidth - viewWidth) {
                            left = parentViewWidth - viewWidth;
                        }

                        if (left <= 0) {
                            left = 0;
                        }

                        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                        param.leftMargin = left;
                        param.topMargin = top;
                        v.setLayoutParams(param);
                        v.postInvalidate();

                        if (x == firstX && y == firstY && x == lastX && y == lastY) {
                            if (onLongClickListener != null)
                                setOnLongClickListener(onLongClickListener);
                        } else {
                            setOnLongClickListener(null);
                        }
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    default:
                        break;

                }
                return false;
            }
        });
    }

    public void setTagBackGround(int resId) {
        textview.setBackgroundResource(resId);
    }

    private final void clearAnim() {
        this.blackIcon1.clearAnimation();
        this.blackIcon2.clearAnimation();
        this.viewPointer.clearAnimation();
        this.isShow = false;
    }

    private final void startBlackAnimation1(final ImageView imageView) {
        blackAnimation1.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow) {
                    return;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.clearAnimation();
                        blackAnimation1.reset();
                        startBlackAnimation2(blackIcon2);
                    }
                }, 10);
            }
        });
        imageView.clearAnimation();
        imageView.startAnimation(blackAnimation1);
    }


    private final void startBlackAnimation2(final ImageView imageView) {
        blackAnimation2.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow) {
                    return;
                }
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        imageView.clearAnimation();
                        blackAnimation2.reset();
                        startWhiteAnimation(viewPointer);
                    }
                }, 10);
            }
        });
        imageView.clearAnimation();
        imageView.startAnimation(blackAnimation2);
    }

    public final void startWhiteAnimation(final ImageView imageView) {
        whiteAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isShow) {
                    return;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageView.clearAnimation();
                        whiteAnimation.reset();
                        startBlackAnimation1(blackIcon1);
                    }
                }, 10);
            }
        });
        imageView.clearAnimation();
        imageView.startAnimation(whiteAnimation);
    }

    protected final void setVisible() {
        if ((textview != null) && (taginfo != null)) {
            if (TagInfo.Type.CustomPoint != taginfo.type && TagInfo.Type.OfficalPoint != taginfo.type) {
                this.viewPointer = this.brandIcon;
                this.geoIcon.setVisibility(View.VISIBLE);
                this.brandIcon.setVisibility(View.GONE);
            } else {
                this.viewPointer = this.geoIcon;
                this.geoIcon.setVisibility(View.GONE);
                this.brandIcon.setVisibility(View.VISIBLE);
            }
            textview.setText(taginfo.bname);
            textview.setVisibility(View.VISIBLE);
        }
        clearAnim();
        show();
    }

    public void show() {
        if (this.isShow) {
            return;
        }
        this.isShow = true;
        startWhiteAnimation(viewPointer);
    }

    private TagInfo taginfo;

    public void setData(TagInfo mTagInfo) {
        this.taginfo = mTagInfo;
        setVisible();
    }

    public TagInfo getData() {
        return taginfo;
    }

    public void setOnTagLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public static class TagInfo implements Parcelable, Serializable {
        private static final long serialVersionUID = -2939266917839493174L;
        public String bname = "";
        public long bid = 0L;
        public double pic_x = 0.0D;
        public double pic_y = 0.0D;
        public Direction direct = Direction.Left;
        public Type type = Type.Undefined;

        private enum Direction {
            Left("left"), Right("right");

            Direction(String valString) {
                this.valueString = valString;
            }

            public static int size() {
                return Direction.values().length;
            }

            public String valueString;

            public String toString() {
                return valueString;
            }
        }

        private enum Type {
            Undefined("undefined"), Exists("exists"), CustomPoint("custom_point"), OfficalPoint("offical_point");

            Type(String typeString) {
                this.valueString = typeString;
            }

            public static int size() {
                return Type.values().length;
            }

            public String valueString;

            public String toString() {
                return valueString;
            }
        }

        public TagInfo() {
        }

        protected TagInfo(Parcel in) {
            bname = in.readString();
            bid = in.readLong();
            pic_x = in.readDouble();
            pic_y = in.readDouble();
        }

        public static final Creator<TagInfo> CREATOR = new Creator<TagInfo>() {
            @Override
            public TagInfo createFromParcel(Parcel in) {
                return new TagInfo(in);
            }

            @Override
            public TagInfo[] newArray(int size) {
                return new TagInfo[size];
            }
        };

        public final int describeContents() {
            return 0;
        }

        public final void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(bname);
            parcel.writeLong(bid);
            parcel.writeDouble(pic_x);
            parcel.writeDouble(pic_y);
            parcel.writeString(direct.toString());
            parcel.writeString(type.toString());
        }
    }
}