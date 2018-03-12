package in.goodiebag.example.n;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import in.goodiebag.example.R;
import in.goodiebag.example.n.bean.Bean;

/**
 * Created by Weidongjian on 2015/8/18.
 */
@SuppressWarnings("unused")
public class LoopView extends View {

    private float scaleX = 1.0F;

    private static final int DEFAULT_FIRST_LINE_TEXT_SIZE = (int) (Resources.getSystem()
            .getDisplayMetrics().density * 14);
    private static final int DEFAULT_SECOND_LINE_TEXT_SIZE = (int) (Resources.getSystem()
            .getDisplayMetrics().density * 25);

    private static final float DEFAULT_LINE_SPACE = 1f;

    private static final int DEFAULT_VISIBLE_ITEMS = 9;

    private static final int DEFAULT_SPACING_OF_FIRST_AND_SECOND = (int) (Resources.getSystem()
            .getDisplayMetrics().density * 3);

    private static final int DEFAULT_SPACING_OF_FIRST_AND_IMG = (int) (Resources.getSystem()
            .getDisplayMetrics().density * 5);

    private static final float DEFAULT_CENTER_HEIGHT = (Resources.getSystem().getDisplayMetrics()
            .density * 1);

    private static final int DEFAULT_SHADER_COLOR = 0x50000000;

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    private Context mContext;

    Handler mHandler;
    private GestureDetector mFlingGestureDetector;
    OnItemSelectedListener mOnItemSelectedListener;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint mFirstLinePaintOuterText;
    private Paint mFirstLinePaintCenterText;

//    private Paint mSecondLinePaintOuterText;
//    private Paint mSecondLinePaintCenterText;


    private Paint mPaintIndicator;

//    private Paint mTestPaint;
//
//    private Paint mTestPaint2;

    //    List<String> mItems;
    List<Bean> mItems;

    //    String[] mDrawingStrings;
    Bean[] mDrawingStrings;

    int mFirstLineTextSize;
//    int mSecondLineTextSize;

    int mMaxItemWidth;

    int mOuterTextColor;

    int mCenterTextColor;
    int mDividerColor;

    //文本的行高相对于文字高度的倍数
    float mLineSpacingMultiplier;
    boolean isLoop;

    int mFirstLineX;
    int mSecondLineX;

    int mTotalScrollX;
    int mInitPosition;
    private int mSelectedItem;
    int mPreCurrentIndex;
    int mChange;

    int mItemsVisibleCount;



    int mMeasuredHeight;
    int mMeasuredWidth;

    int mHalfCircumference; //圆的半周长
    int mRadius;

    private int mOffset = 0;
    private float mPreviousX;
    long mStartTime = 0;

    private Rect mTempRect = new Rect();

    private int mPaddingLeft, mPaddingRight;
    private int mSpacingOfFirstLineAndSecondLine;


    //第一行图片与文字之间的间距
//    private int mSpacingOfFirstAndImg;

    //第一行文字和第二行文字之间的间距
//    private int mSpacingOfFirstLineAndSecondLine;

    //第一行文字的高度
    private float mFirstLineStringWidth;

    //第二行文字的高度
    private float mSecondLineStingWidth;

    //绘制第一行文字的Y
    private float mFirstLineStringStartX;

    //绘制第二行文字的Y
//    private float mSecondLineStringStartX;

    float[] src = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0};

    float[] white = new float[]{
            1, 0, 0, 0, 255,
            0, 1, 0, 0, 255,
            0, 0, 1, 0, 255,
            0, 0, 0, 1, 0};

    float[] white_alpha_40 = new float[]{
            1, 0, 0, 0, 255,
            0, 1, 0, 0, 255,
            0, 0, 1, 0, 255,
            0, 0, 0, 0.4f, 0};

    //0x50000000
    float[] shader_color = new float[]{
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0.20f, 0};


//    float[] black = new float[]{
//            1, 0, 0, 0, -255,
//            0, 1, 0, 0, -255,
//            0, 0, 1, 0, -255,
//            0, 0, 0, 1, 0};

//    float[] black_alpha_4 = new float[]{
//            1, 0, 0, 0, -255,
//            0, 1, 0, 0, -255,
//            0, 0, 1, 0, -255,
//            0, 0, 0, 1, 0};
//

    /**
     * set text line space, must more than 1
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.mLineSpacingMultiplier = lineSpacingMultiplier;
        }
    }


    /**
     * set outer text color
     *
     * @param centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.mCenterTextColor = centerTextColor;
//        mSecondLinePaintCenterText.setColor(centerTextColor);
    }

    /**
     * set center text color
     *
     * @param outerTextColor
     */
    public void setOuterTextColor(int outerTextColor) {
        this.mOuterTextColor = outerTextColor;
//        mSecondLinePaintOuterText.setColor(outerTextColor);
    }

    /**
     * set divider color
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        mPaintIndicator.setColor(dividerColor);
    }


    public LoopView(Context context) {
        super(context);
        initLoopView(context, null);
    }

    public LoopView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        initLoopView(context, attributeset);
    }

    public LoopView(Context context, AttributeSet attributeset, int defStyleAttr) {
        super(context, attributeset, defStyleAttr);
        initLoopView(context, attributeset);
    }

    private void initLoopView(Context context, AttributeSet attributeset) {
        this.mContext = context;
        mHandler = new MessageHandler(this);
        mFlingGestureDetector = new GestureDetector(context, new LoopViewGestureListener(this));
        mFlingGestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable
                .androidWheelView);
//        mSecondLineTextSize = typedArray.getInteger(R.styleable.androidWheelView_awv_textsize,
//                DEFAULT_SECOND_LINE_TEXT_SIZE);
        mLineSpacingMultiplier = typedArray.getFloat(R.styleable.androidWheelView_awv_lineSpace,
                DEFAULT_LINE_SPACE);
        mCenterTextColor = typedArray.getInteger(R.styleable
                .androidWheelView_awv_centerTextColor, 0xff00ff00);
        mOuterTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_outerTextColor,
                0x66ff0000);
        mDividerColor = typedArray.getInteger(R.styleable.androidWheelView_awv_dividerTextColor,
                0xffff0000);
        mItemsVisibleCount = typedArray.getInteger(R.styleable
                .androidWheelView_awv_itemsVisibleCount, DEFAULT_VISIBLE_ITEMS);
        if (mItemsVisibleCount % 2 == 0) {
            mItemsVisibleCount = DEFAULT_VISIBLE_ITEMS;
        }
        isLoop = typedArray.getBoolean(R.styleable.androidWheelView_awv_isLoop, true);
        typedArray.recycle();

        mFirstLineTextSize = DEFAULT_FIRST_LINE_TEXT_SIZE;
//        mDrawingStrings = new String[mItemsVisibleCount];

//        mSpacingOfFirstAndImg = DEFAULT_SPACING_OF_FIRST_AND_IMG;

        mSpacingOfFirstLineAndSecondLine = DEFAULT_SPACING_OF_FIRST_AND_SECOND;

        mDrawingStrings = new Bean[mItemsVisibleCount];

        mTotalScrollX = 0;
        mInitPosition = -1;

        initPaints();
    }


    /**
     * visible item count, must be odd number
     *
     * @param visibleNumber
     */
    public void setItemsVisibleCount(int visibleNumber) {
        if (visibleNumber % 2 == 0) {
            return;
        }
        if (visibleNumber != mItemsVisibleCount) {
            mItemsVisibleCount = visibleNumber;
//            mDrawingStrings = new String[mItemsVisibleCount];
            mDrawingStrings = new Bean[mItemsVisibleCount];
        }
    }

    private float mShadowRadiusPercent = 0.2f;

    private void initPaints() {
        Paint mSecondLinePaintOuterText = new Paint();
        mSecondLinePaintOuterText.setColor(mOuterTextColor);
        mSecondLinePaintOuterText.setAntiAlias(true);
//        mSecondLinePaintOuterText.setTextSize(mSecondLineTextSize);
        mSecondLinePaintOuterText.setShadowLayer(10, 1, 1, DEFAULT_SHADER_COLOR);

        mFirstLinePaintOuterText = new Paint(mSecondLinePaintOuterText);
        mFirstLinePaintOuterText.setTextSize(mFirstLineTextSize);
//        mFirstLinePaintOuterText.setTextAlign(Paint.Align.CENTER);


        Paint mSecondLinePaintCenterText = new Paint();
        mSecondLinePaintCenterText.setColor(mCenterTextColor);
        mSecondLinePaintCenterText.setAntiAlias(true);
//        mSecondLinePaintCenterText.setTextSize(mSecondLineTextSize);

        Paint.FontMetrics fontMetrics = mSecondLinePaintCenterText.getFontMetrics();
        float secondLineHeight = fontMetrics.bottom - fontMetrics.top;
        mSecondLinePaintCenterText.setShadowLayer(secondLineHeight * mShadowRadiusPercent, 0, 0,
                DEFAULT_SHADER_COLOR);

        mFirstLinePaintCenterText = new Paint(mSecondLinePaintCenterText);
        mFirstLinePaintCenterText.setTextSize(mFirstLineTextSize);
//        mFirstLinePaintOuterText.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics1 = mFirstLinePaintCenterText.getFontMetrics();
        float firstLineHeight = fontMetrics1.bottom - fontMetrics1.top;
        mFirstLinePaintCenterText.setShadowLayer(firstLineHeight * mShadowRadiusPercent, 0, 0,
                DEFAULT_SHADER_COLOR);


        mPaintIndicator = new Paint();
        mPaintIndicator.setColor(mDividerColor);
        mPaintIndicator.setAntiAlias(true);


//        mTestPaint = new Paint();
//        mTestPaint.setColor(Color.RED);
//        mTestPaint.setAntiAlias(true);
//
//        mTestPaint2 = new Paint();
//        mTestPaint2.setColor(Color.BLUE);
//        mTestPaint2.setAntiAlias(true);


    }

    //0~1.0
    private void setShadowLayerColor(float percent) {

        int value = (int) (percent * 10);
        float result = ((float) value) / 10;

        int shader_color = (int) (0xff000000 * result) >> 24 << 24;


        if (DEFAULT_SHADER_COLOR == shader_color) {
            return;
        }

//        DEFAULT_SHADER_COLOR = shader_color;


//        Paint.FontMetrics fontMetrics = mSecondLinePaintCenterText.getFontMetrics();
//        float secondLineHeight = fontMetrics.bottom - fontMetrics.top;
//        mSecondLinePaintCenterText.clearShadowLayer();
//        mSecondLinePaintCenterText.setShadowLayer(secondLineHeight * mShadowRadiusPercent, 1, 1,
//                DEFAULT_SHADER_COLOR);

        Paint.FontMetrics fontMetrics1 = mFirstLinePaintCenterText.getFontMetrics();
        float firstLineHeight = fontMetrics1.bottom - fontMetrics1.top;
//        mFirstLinePaintCenterText.clearShadowLayer();
        mFirstLinePaintCenterText.setShadowLayer(firstLineHeight * mShadowRadiusPercent, 1, 1,
                DEFAULT_SHADER_COLOR);

        invalidate();

    }

    //0~1.0
    private void setShadowLayerRadius(float percent) {

        if (percent > 0.2) {
            percent = 0.2f;
        }

        int value = (int) (percent * 10);
        float result = ((float) value) / 10;
        if (mShadowRadiusPercent == result) {
            return;
        }

        mShadowRadiusPercent = percent;

//        Paint.FontMetrics fontMetrics = mSecondLinePaintCenterText.getFontMetrics();
//        float secondLineHeight = fontMetrics.bottom - fontMetrics.top;
//        mSecondLinePaintCenterText.clearShadowLayer();
//        mSecondLinePaintCenterText.setShadowLayer(secondLineHeight * mShadowRadiusPercent, 1, 1,
//                DEFAULT_SHADER_COLOR);

        Paint.FontMetrics fontMetrics1 = mFirstLinePaintCenterText.getFontMetrics();
        float firstLineHeight = fontMetrics1.bottom - fontMetrics1.top;
//        mFirstLinePaintCenterText.clearShadowLayer();
        mFirstLinePaintCenterText.setShadowLayer(firstLineHeight * mShadowRadiusPercent, 1, 1,
                DEFAULT_SHADER_COLOR);

        invalidate();
    }

    private void remeasure() {
        if (mItems == null) {
            return;
        }

        mMeasuredWidth = getMeasuredWidth();

        mMeasuredHeight = getMeasuredHeight();

        if (mMeasuredWidth == 0 || mMeasuredHeight == 0) {
            return;
        }

        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();

        mMeasuredWidth = mMeasuredWidth - mPaddingRight;

//        mSecondLinePaintCenterText.getTextBounds("\u661F\u671F", 0, 2, mTempRect); // 星期
//        mMaxItemHeight = mTempRect.height();
        mHalfCircumference = (int) (mMeasuredWidth * Math.PI / 2);

        //根据圆的半周长，计算最大的item高度
        mMaxItemWidth = (int) (mHalfCircumference / (mLineSpacingMultiplier *
                (mItemsVisibleCount - 1)));

        //半径
        mRadius = mMeasuredWidth / 2;

        //第一条线的位置，控件的高度减去中间最大的条目的高度，再除以2
        mFirstLineX = (int) ((mMeasuredWidth - mLineSpacingMultiplier * mMaxItemWidth) / 2.0F);

        //第二条线的位置，直接用第一条线的位置加上中间最大的条目的高度不就好了
        mSecondLineX = (int) ((mMeasuredWidth + mLineSpacingMultiplier * mMaxItemWidth) / 2.0F);

        if (mInitPosition == -1) {
            if (isLoop) {
                mInitPosition = (mItems.size() + 1) / 2;
            } else {
                mInitPosition = 0;
            }
        }

        mPreCurrentIndex = mInitPosition;

        float contentW = mFirstLineStringWidth + mSecondLineStingWidth + mSpacingOfFirstLineAndSecondLine;
        Paint.FontMetrics fontMetrics = mFirstLinePaintCenterText.getFontMetrics();
        //计算第一行文字开始绘制的底部Y
        mFirstLineStringStartX = mMaxItemWidth / 2 ;

        //计算第二行文字开始绘制的底部Y
//        mSecondLineStringStartX = getMeasuredWidth() / 7 * 4;


    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemWidth = mLineSpacingMultiplier * mMaxItemWidth;
            mOffset = (int) ((mTotalScrollX % itemWidth + itemWidth) % itemWidth);
            if ((float) mOffset > itemWidth / 2.0F) {
                mOffset = (int) (itemWidth - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture = mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0,
                3, TimeUnit.MILLISECONDS);
    }

    public final void scrollBy(float velocityY) {
        cancelFuture();
        // mChange this number, can mChange fling speed
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0,
                velocityFling, TimeUnit.MILLISECONDS);
    }

    public void cancelFuture() {
        if (mFuture != null && !mFuture.isCancelled()) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    /**
     * set not loop
     */
    public void setNotLoop() {
        isLoop = false;
    }

//    /**
//     * set text size in dp
//     *
//     * @param size
//     */
//    public final void setSecondLineTextSize(float size) {
//        if (size > 0.0F) {
//            mSecondLineTextSize = (int) (mContext.getResources().getDisplayMetrics().density *
//                    size);
//            mSecondLinePaintOuterText.setTextSize(mSecondLineTextSize);
//            mSecondLinePaintCenterText.setTextSize(mSecondLineTextSize);
//        }
//    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.mInitPosition = 0;
        } else {
            if (mItems != null && mItems.size() > initPosition) {
                this.mInitPosition = initPosition;
            }
        }
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        mOnItemSelectedListener = OnItemSelectedListener;
    }

//    public final void setItems(List<String> items) {
//        this.mItems = items;
//        remeasure();
//        invalidate();
//    }

    public final void setItems(List<Bean> items) {
        this.mItems = items;
        remeasure();
        invalidate();
    }

    public final int getSelectedItem() {
        return mSelectedItem;
    }
//
//    protected final void scrollBy(float velocityY) {
//        Timer timer = new Timer();
//        mTimer = timer;
//        timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
//    }

    protected final void onItemSelected() {
        if (mOnItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 0L);
        }
    }


    /**
     * link https://github.com/weidongjian/androidWheelView/issues/10
     *
     * @param scaleX
     */
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }


    /**
     * set current item position
     *
     * @param position
     */
    public void setCurrentPosition(int position) {
        if (position > 0 && position < mItems.size() && position != mSelectedItem) {
            mInitPosition = position;
            mTotalScrollX = 0;
            mOffset = 0;
            mSelectedItem = position;
            invalidate();
        }
    }

    private Bean mBean = new Bean("");

    @Override
    protected void onDraw(Canvas canvas) {
        if (mItems == null) {
            return;
        }

        mChange = (int) (mTotalScrollX / (mLineSpacingMultiplier * mMaxItemWidth));
        mPreCurrentIndex = mInitPosition + mChange % mItems.size();


        if (!isLoop) {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = 0;
            }
            if (mPreCurrentIndex > mItems.size() - 1) {
                mPreCurrentIndex = mItems.size() - 1;
            }
        } else {
            if (mPreCurrentIndex < 0) {
                mPreCurrentIndex = mItems.size() + mPreCurrentIndex;
            }
            if (mPreCurrentIndex > mItems.size() - 1) {
                mPreCurrentIndex = mPreCurrentIndex - mItems.size();
            }
        }
        int j2 = (int) (mTotalScrollX % (mLineSpacingMultiplier * mMaxItemWidth));
        // put value to drawingString
        int k1 = 0;
        while (k1 < mItemsVisibleCount) {
            int l1 = mPreCurrentIndex - (mItemsVisibleCount / 2 - k1);
            if (isLoop) {
                while (l1 < 0) {
                    l1 = l1 + mItems.size();
                }
                while (l1 > mItems.size() - 1) {
                    l1 = l1 - mItems.size();
                }
                mDrawingStrings[k1] = mItems.get(l1);
            } else if (l1 < 0) {
                mDrawingStrings[k1] = mBean;
            } else if (l1 > mItems.size() - 1) {
                mDrawingStrings[k1] = mBean;
            } else {
                mDrawingStrings[k1] = mItems.get(l1);
            }
            k1++;
        }
        canvas.drawLine(mFirstLineX, 0, mFirstLineX, mMeasuredHeight, mPaintIndicator);
        canvas.drawLine(mSecondLineX, 0, mSecondLineX, mMeasuredHeight, mPaintIndicator);

        int i = 0;
        doLog("radius:" + mRadius + "  half:" + mHalfCircumference);
        while (i < mItemsVisibleCount) {
            canvas.save();
            float itemWidth = mMaxItemWidth * mLineSpacingMultiplier;
            double radian = ((itemWidth * i - j2) * Math.PI) / mHalfCircumference;
//            doLog("i:" + i + " radian:" + radian);
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateX = (int) (mRadius - Math.cos(radian) * mRadius - (Math.sin(radian)
                        * itemWidth) / 2D);
                doLog("i:" + i + "  translateX:" + translateX);
                canvas.translate(translateX, 0.0f);
                canvas.scale((float) Math.sin(radian), 1.0f);
                Bean drawingString = mDrawingStrings[i];

                if (translateX <= mFirstLineX && mMaxItemWidth + translateX >= mFirstLineX) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, mFirstLineX - translateX, mMeasuredHeight);

                    //上一个条目的 速度两个字
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX,
                            getFirstLineTextY(drawingString.getFirtLine(),
                                    mFirstLinePaintOuterText, mTempRect, drawingString),
                            mFirstLinePaintOuterText);
                    canvas.restore();


                    canvas.save();
                    canvas.clipRect(0, 0, mFirstLineX - translateX,
                            mMeasuredHeight);


                    //当前条目 速度两个字
                    canvas.drawText(drawingString.getFirtLine(),
                            mFirstLineStringStartX, getFirstLineTextY(drawingString.getFirtLine(),
                                    mFirstLinePaintCenterText, mTempRect, drawingString)
                            , mFirstLinePaintCenterText);

                    canvas.restore();
                } else if (translateX <= mSecondLineX && mMaxItemWidth + translateX >=
                        mSecondLineX) {
                    // second divider
                    canvas.save();

                    canvas.clipRect(0, 0, mSecondLineX - translateX, mMeasuredHeight);

                    //手指向上滑动时 速度文字
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX
                            ,
                            getFirstLineTextY(drawingString.getFirtLine(),
                                    mFirstLinePaintCenterText, mTempRect, drawingString)
                            , mFirstLinePaintCenterText);


                    canvas.restore();


                    canvas.save();

                    canvas.clipRect(mSecondLineX - translateX, 0, (int) (itemWidth),
                            mMeasuredHeight);


                    //当前条目的下一个条目 速度两个字
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX
                            , getFirstLineTextY(drawingString.getFirtLine(),
                                    mFirstLinePaintOuterText, mTempRect, drawingString)
                            , mFirstLinePaintOuterText);

                    canvas.restore();


                } else if (translateX >= mFirstLineX && mMaxItemWidth + translateX <=
                        mSecondLineX) {
                    // center item  什么时候会走这里呢。。。

                    canvas.clipRect(0, 0, (int) (itemWidth), mMeasuredHeight);


                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX
                            ,
                            getFirstLineTextY(drawingString.getFirtLine(),
                                    mFirstLinePaintCenterText, mTempRect, drawingString)
                            , mFirstLinePaintCenterText);


                    mSelectedItem = mItems.indexOf(drawingString);
                } else {
                    // other item

                    canvas.clipRect(0, 0, (int) (itemWidth), mMeasuredHeight);


                    //最上下条目 速度文字
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX
                            ,
                            getFirstLineTextY(drawingString.getFirtLine(),
                                    mFirstLinePaintOuterText, mTempRect, drawingString)

                            , mFirstLinePaintOuterText);

                }
                canvas.restore();
            }
            i++;
        }
    }

    private HashMap<String, Bitmap> mBlurBitmaps;

    public void setBlurBitmaps(HashMap<String, Bitmap> blurBitmaps) {
        mBlurBitmaps = blurBitmaps;
    }


    private int getFirstLineTextY(String content, Paint paint, Rect rect, Bean bean) {
        paint.getTextBounds(content, 0, content.length(), rect);
        int textWidth = rect.height();
        return mMeasuredHeight / 2 - textWidth / 2;
    }

    // text start drawing position
    private int getTextX(String a, Paint paint, Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (mMeasuredWidth - mPaddingLeft - textWidth) / 2 + mPaddingLeft;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return false;
        boolean eventConsumed = mFlingGestureDetector.onTouchEvent(event);
        float itemWidth = mLineSpacingMultiplier * mMaxItemWidth;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartTime = System.currentTimeMillis();
                cancelFuture();
                mPreviousX = event.getRawX();
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = mPreviousX - event.getRawX();
                mPreviousX = event.getRawX();

                mTotalScrollX = (int) (mTotalScrollX + dx);

                if (!isLoop) {
                    float right = mInitPosition * itemWidth;
                    float left = (mItems.size() - 1 - mInitPosition) * itemWidth;

                    if (mTotalScrollX < left) {
                        mTotalScrollX = (int) left;
                    } else if (mTotalScrollX > right) {
                        mTotalScrollX = (int) right;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            default:
                if (!eventConsumed) {
                    float x = event.getX();
                    double l = Math.acos((mRadius - x) / mRadius) * mRadius;
                    int circlePosition = (int) ((l + itemWidth / 2) / itemWidth);

                    float extraOffset = (mTotalScrollX % itemWidth + itemWidth) % itemWidth;
                    mOffset = (int) ((circlePosition - mItemsVisibleCount / 2) * itemWidth -
                            extraOffset);

                    if ((System.currentTimeMillis() - mStartTime) > 120) {
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        smoothScroll(ACTION.CLICK);
                    }
                }
                break;
        }

        invalidate();
        return true;
    }

    public void smoothScrollToNext() {
        if (getSelectedItem() == mItems.size() - 1) {
            return;
        }

        mSelectedItem++;

        float y = mSecondLineX + 30;
        float itemWidth = mLineSpacingMultiplier * mMaxItemWidth;
        double l = Math.acos((mRadius - y) / mRadius) * mRadius;
        int circlePosition = (int) ((l + itemWidth / 2) / itemWidth);
        float extraOffset = (mTotalScrollX % itemWidth + itemWidth) % itemWidth;
        mOffset = (int) ((circlePosition - mItemsVisibleCount / 2) * itemWidth - extraOffset);
        smoothScroll(ACTION.CLICK);
    }

    public void smoothScrollToPre() {
        if (getSelectedItem() == 0) {
            return;
        }

        mSelectedItem--;

        float y = mFirstLineX - 30;
        float itemHeight = mLineSpacingMultiplier * mMaxItemWidth;
        double l = Math.acos((mRadius - y) / mRadius) * mRadius;
        int circlePosition = (int) ((l + itemHeight / 2) / itemHeight);

        float extraOffset = (mTotalScrollX % itemHeight + itemHeight) % itemHeight;
        mOffset = (int) ((circlePosition - mItemsVisibleCount / 2) * itemHeight - extraOffset);
        smoothScroll(ACTION.CLICK);
    }


    private static final String TAG = LoopView.class.getSimpleName();


    private static final boolean DEBUG = false;

    private void doLog(String text) {
        Log.d("slim", text);
    }
}
