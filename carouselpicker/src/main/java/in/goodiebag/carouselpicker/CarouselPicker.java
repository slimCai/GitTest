package in.goodiebag.carouselpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavan on 25/04/17.
 */

public class CarouselPicker extends ViewPager {
    private int itemsVisible = 3;
    private float divisor;
    private OnPageChangeListener mOuterListener;
    private int offset;

    private int unit;

    public void setUnit(int unit) {
        this.unit = unit;
        this.setOffscreenPageLimit(unit + 2);
    }

    public CarouselPicker(Context context) {
        this(context, null);
    }

    public CarouselPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
        init();
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
        offset = item;
        Log.d("slim", "set:" + offset);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
        offset = item;
        Log.d("slim", "set2:" + offset);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        mOuterListener = listener;
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable
                    .CarouselPicker);
            itemsVisible = array.getInteger(R.styleable.CarouselPicker_items_visible, itemsVisible);
            switch (itemsVisible) {
                case 3:
                    TypedValue threeValue = new TypedValue();
                    getResources().getValue(R.dimen.three_items, threeValue, true);
                    divisor = 1.5f;
                    break;
                case 5:
                    TypedValue fiveValue = new TypedValue();
                    getResources().getValue(R.dimen.five_items, fiveValue, true);
                    divisor = 1.25f;
                    break;
                case 7:
                    TypedValue sevenValue = new TypedValue();
                    getResources().getValue(R.dimen.seven_items, sevenValue, true);
                    divisor = sevenValue.getFloat();
                    break;
                case 8:
                    divisor = 1.13f;
                    break;
                default:
                    divisor = 3;
                    break;
            }
            array.recycle();
        }
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        this.setPageTransformer(false, new CustomPageTransformer(getContext()));
        this.setClipChildren(false);
        this.setFadingEdgeLength(0);
        super.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                if (mOuterListener != null)
                    mOuterListener.onPageScrolled(position % unit, positionOffset,
                            positionOffsetPixels);
//                offset = position;
//                Log.d("slim", "onPageScrolled:" + offset);
            }

            @Override
            public void onPageSelected(int position) {
                if (mOuterListener != null)
                    mOuterListener.onPageSelected(position % unit);
                offset = position;
                Log.d("slim", "onPageSelected:" + offset + "  " + (position % 5));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mOuterListener != null)
                    mOuterListener.onPageScrollStateChanged(state);
            }
        });
    }

    private int getDis() {
        return unit == 5 ? 2 : 1;
    }

    public int getOffset() {
        return offset;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;

        if (getChildCount() > 0) {
            for (int i = offset - getDis(); i < offset + getDis(); i++) {
                View child = getChildAt(i % unit);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec
                        .UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        setPageMargin((int) (-w / divisor));


    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public static class CarouselViewAdapter extends PagerAdapter {

        List<PickerItem> items = new ArrayList<>();
        Context context;
        int unit;
        int drawable;
        OnClickListener mListener;

        public void setListener(OnClickListener listener) {
            this.mListener = listener;
        }

        public CarouselViewAdapter(Context context, int unit, List<PickerItem> items, int
                drawable) {
            this.context = context;
            this.unit = unit;
            this.drawable = drawable;
            this.items = items;
            if (this.drawable == 0) {
                this.drawable = R.layout.page;
            }
        }


        @Override
        public int getCount() {
            return 1000;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final int a = position % unit;
            View view = LayoutInflater.from(context).inflate(this.drawable, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            final TextView tv = (TextView) view.findViewById(R.id.tv);
            final PickerItem pickerItem = items.get(a);
            iv.setVisibility(VISIBLE);

            iv.setVisibility(GONE);
            tv.setVisibility(VISIBLE);
            tv.setText(pickerItem.getText());
            int textSize = ((TextItem) pickerItem).getTextSize();
            if (textSize != 0) {
                tv.setTextSize(dpToPx(((TextItem) pickerItem).getTextSize()));
            }


            view.setTag(position);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        private int dpToPx(int dp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        }
    }

    /**
     * An interface which should be implemented by all the Item classes.
     * The picker only accepts items in the form of PickerItem.
     */
    public interface PickerItem {
        String getText();

    }

    /**
     * A PickerItem which supports text.
     */
    public static class TextItem implements PickerItem {
        private String text;
        private int textSize;

        public TextItem(String text, int textSize) {
            this.text = text;
            this.textSize = textSize;
        }

        public String getText() {
            return text;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }
    }

    private float x1, y1, x2, y2;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                if (Math.abs(x1 - x2) < 5 && Math.abs(y1 - y2) < 5) {
                    if (onPagerClick != null)
                        onPagerClick.onClick(getClickPos((int) x2));
                }
                break;

        }
        return super.onTouchEvent(ev);
    }

    private int getClickPos(int x) {
        if (x > mLeft && x < mLeft + mWidth) {
            return 0;
        } else if (x > mLeft + mWidth && x < mLeft + mWidth * 2) {
            return 1;
        } else if (x > mLeft + mWidth * 2 && x < mLeft + mWidth * 3) {
            return 2;
        } else if (x > mLeft + mWidth * 3 && x < mLeft + mWidth * 4) {
            return 3;
        } else if (x > mLeft + mWidth * 4 && x < mRight) {
            return 4;
        }
        return -1;
    }

    private OnPagerClick onPagerClick;

    public void setOnPagerClick(OnPagerClick onPagerClick) {
        this.onPagerClick = onPagerClick;
    }

    public interface OnPagerClick {
        void onClick(int pos);
    }

    private int mLeft, mRight, mWidth;

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public void setmLeft(int mLeft) {
        this.mLeft = mLeft;
    }

    public void setmRight(int mRight) {
        this.mRight = mRight;
    }

    OverScroller overScroller = new OverScroller(getContext(), new LinearInterpolator());

    public void scrollOffset(int offset) {
        beginFakeDrag();
        fakeDragBy(offset * mWidth);
        endFakeDrag();
    }
}
