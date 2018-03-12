package in.goodiebag.example.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import in.goodiebag.example.R;

public class RingBackgroundView extends View {
    private List<String> data;
    private float perPro;
    private float pro;
    private Paint mBgPaint, mProPaint;
    private boolean hasInit = false;
    private float mSwipeAngle;

    public void setData(List<String> data) {
        this.data = data;
        perPro = 1.0f / data.size();
    }

    public void setNowValue(String str) {
        if (data != null && data.size() > 0) {
            if (data.contains(str)) {
                int offset = data.indexOf(str);
                if (pro != (offset + 1) * perPro) {
                    if (!hasInit) {
                        hasInit = true;
                        pro = (offset + 1) * perPro;
                        mSwipeAngle = 360 * pro;
                        invalidate();
                    } else {
                        float lastPro = pro;
                        pro = (offset + 1) * perPro;
                        ValueAnimator animator = ValueAnimator.ofFloat(lastPro, pro);
                        animator.setDuration(200);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float a = (float) animation.getAnimatedValue();
                                mSwipeAngle = 360 * a;
                                invalidate();
                            }
                        });
                        animator.start();
                    }
                }
            }
        }
    }

    public RingBackgroundView(Context context) {
        this(context, null);
    }

    public RingBackgroundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingBackgroundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        pro = 0;
        perPro = 0;
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RingBackgroundView);
        int bgColor = a.getColor(R.styleable.RingBackgroundView_bgColor, Color.parseColor
                ("#ffffff"));
        int proColor = a.getColor(R.styleable.RingBackgroundView_proColor, Color.parseColor
                ("#ffffff"));
        int bgWidth = a.getDimensionPixelOffset(R.styleable.RingBackgroundView_bgLineWidth, 1);
        int proWidth = a.getDimensionPixelOffset(R.styleable.RingBackgroundView_proLineWidth, 1);
        mBgPaint = new Paint();

        mBgPaint.setStrokeWidth(bgWidth);
        mBgPaint.setColor(bgColor);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setAntiAlias(true);

        mProPaint = new Paint();
        mProPaint.setStrokeWidth(proWidth);
        mProPaint.setColor(proColor);
        mProPaint.setStyle(Paint.Style.STROKE);
        mProPaint.setAntiAlias(true);
        a.recycle();
    }

    private int mViewWidth, mViewHeight;
    private RectF rect;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldw == 0 && oldh == 0) {
            mViewWidth = w;
            mViewHeight = h;
            rect = new RectF(1, 1, w - 1, h - 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(1.0f * mViewWidth / 2, 1.0f * mViewHeight / 2, (mViewWidth - 2) / 2,
                mBgPaint);
        if (rect != null)
            canvas.drawArc(rect, -90, mSwipeAngle, false, mProPaint);
    }
}
