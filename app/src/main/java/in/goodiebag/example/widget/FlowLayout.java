package in.goodiebag.example.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import in.goodiebag.example.DensityUtil;
import in.goodiebag.example.R;


/**
 * Created by qiao on 16/6/15.
 */
public class FlowLayout extends ViewGroup {

    private int mChildSpace;
    private int mLineSpace;
    private int mChildHeight;
    private int mWidth;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mWidth = DensityUtil.getMetricsWidth(getContext()) - DensityUtil.dip2px(20);

        final TypedArray attributes = getContext().obtainStyledAttributes(
                attrs, R.styleable.FlowLayout);

        mChildSpace = (int) attributes.getDimension(
                R.styleable.FlowLayout_childSpace, DensityUtil.dip2px(20));

        mLineSpace = (int) attributes.getDimension(
                R.styleable.FlowLayout_lineSpace, 10);


        attributes.recycle();
    }

    public void setChildSpace(int childSpace) {
        this.mChildSpace = childSpace;
    }

    public void setLineSpace(int lineSpace) {
        this.mLineSpace = lineSpace;
    }

    public void setChildHeight(int childHeight) {
        this.mChildHeight = childHeight;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int total = 0;
        int line = 0;
        int childCount = getChildCount();
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == VISIBLE) {
                if (needResetChildHeight) {
                    needResetChildHeight = false;
                    mChildHeight = childAt.getMeasuredHeight();
                }
                total += childAt.getMeasuredWidth() + mChildSpace;
                if (total - mChildSpace > getMeasuredWidth()) {
                    line++;
                    total = 0;
                }
            }
        }
        setMeasuredDimension(mWidth, line * (mChildHeight + mLineSpace) + mChildHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int total = 0;
        int line = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == VISIBLE) {
                total += childAt.getMeasuredWidth() + mChildSpace;
                if (total - mChildSpace > getMeasuredWidth()) {
                    line++;
                    total = childAt.getMeasuredWidth() + mChildSpace;
                }
                childAt.layout(total - childAt.getMeasuredWidth() - mChildSpace, line *
                        (mChildHeight + mLineSpace), total, line * (mChildHeight + mLineSpace) +
                        mChildHeight);
            }
        }
    }

    private boolean needResetChildHeight = false;

    public void setNeedResetChildHeight(boolean needResetChildHeight) {
        this.needResetChildHeight = needResetChildHeight;
    }
}
