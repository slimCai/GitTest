package in.goodiebag.example.l;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import in.goodiebag.example.R;
import in.goodiebag.example.n.bean.Bean;


public class LoopView extends View {

    Handler mHandler;

    private float scaleX = 1.0F;

    private static final int DEFAULT_TEXT_SIZE = (int) (Resources.getSystem().getDisplayMetrics()
            .density * 12);

    private static final float DEFAULT_LINE_SPACE = 2f;

    private static final int DEFAULT_VISIBIE_ITEMS = 9;

    public enum ACTION {
        CLICK, FLING, DAGGLE
    }

    private Context context;

    private GestureDetector flingGestureDetector;
    OnItemSelectedListener onItemSelectedListener;
    private float mFirstLineStringStartX;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintIndicator;

    List<Bean> items;

    int textSize;

    int maxItemWidth;

    int outerTextColor;

    int centerTextColor;
    int dividerColor;

    float lineSpacingMultiplier;
    boolean isLoop;

    int firstLineX;
    int secondLineX;

    public int totalScrollX;
    int initPosition;
    private int selectedItem;
    int preCurrentIndex;
    int change;

    int itemsVisibleCount;

    Bean[] drawingStrings;
//    HashMap<String,Integer> drawingStr

    int measuredHeight;
    int measuredWidth;

    int halfCircumference;
    int radius;

    private int mOffset = 0;
    private float previousX;
    long startTime = 0;

    private Rect tempRect = new Rect();

    private int paddingLeft, paddingRight;

    /**
     * set text line space, must more than 1
     *
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        if (lineSpacingMultiplier > 1.0f) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
        }
    }

    /**
     * set outer text color
     *
     * @param centerTextColor
     */
    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
        paintCenterText.setColor(centerTextColor);
    }

    /**
     * set center text color
     *
     * @param outerTextColor
     */
    public void setOuterTextColor(int outerTextColor) {
        this.outerTextColor = outerTextColor;
        paintOuterText.setColor(outerTextColor);
    }

    /**
     * set divider color
     *
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        paintIndicator.setColor(dividerColor);
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
        this.context = context;
        mHandler = new MessageHandler(this);
        flingGestureDetector = new GestureDetector(context, new in.goodiebag.example.l
                .LoopViewGestureListener(this));
        flingGestureDetector.setIsLongpressEnabled(false);

        TypedArray typedArray = context.obtainStyledAttributes(attributeset, R.styleable
                .androidWheelView);
        textSize = typedArray.getInteger(R.styleable.androidWheelView_awv_textsize,
                DEFAULT_TEXT_SIZE);
        textSize = (int) (Resources.getSystem().getDisplayMetrics().density * textSize);
        lineSpacingMultiplier = typedArray.getFloat(R.styleable.androidWheelView_awv_lineSpace,
                DEFAULT_LINE_SPACE);
        centerTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_centerTextColor,
                0xff313131);
        outerTextColor = typedArray.getInteger(R.styleable.androidWheelView_awv_outerTextColor,
                0xffafafaf);
        dividerColor = typedArray.getInteger(R.styleable.androidWheelView_awv_dividerTextColor,
                0xffc5c5c5);
        itemsVisibleCount =
                typedArray.getInteger(R.styleable.androidWheelView_awv_itemsVisibleCount,
                        DEFAULT_VISIBIE_ITEMS);
        if (itemsVisibleCount % 2 == 0) {
            itemsVisibleCount = DEFAULT_VISIBIE_ITEMS;
        }
        isLoop = typedArray.getBoolean(R.styleable.androidWheelView_awv_isLoop, true);
        typedArray.recycle();

//        drawingStrings = new String[itemsVisibleCount];
        drawingStrings = new Bean[itemsVisibleCount];
        totalScrollX = 0;
        initPosition = -1;

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
        if (visibleNumber != itemsVisibleCount) {
            itemsVisibleCount = visibleNumber;
//            drawingStrings = new String[itemsVisibleCount];
            drawingStrings = new Bean[itemsVisibleCount];
        }
    }

    private void initPaints() {
        paintOuterText = new Paint();
        paintOuterText.setColor(outerTextColor);
        paintOuterText.setAntiAlias(true);
        paintOuterText.setTypeface(Typeface.MONOSPACE);
        paintOuterText.setTextSize(textSize);

        paintCenterText = new Paint();
        paintCenterText.setColor(centerTextColor);
        paintCenterText.setAntiAlias(true);
        paintCenterText.setTextScaleX(scaleX);
        paintCenterText.setTypeface(Typeface.MONOSPACE);
        paintCenterText.setTextSize(textSize);

        paintIndicator = new Paint();
        paintIndicator.setColor(dividerColor);
        paintIndicator.setAntiAlias(true);

    }

    private void remeasure() {
        if (items == null) {
            return;
        }

        measuredWidth = getMeasuredWidth();

        measuredHeight = getMeasuredHeight();

        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }

        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();

        measuredWidth = measuredWidth - paddingRight;

        halfCircumference = (int) (measuredWidth * Math.PI / 2);

        maxItemWidth = (int) (halfCircumference / (lineSpacingMultiplier * (itemsVisibleCount -
                1)));


        radius = measuredWidth / 2;
        firstLineX = (int) ((measuredWidth - lineSpacingMultiplier * maxItemWidth) / 2.0F);
        secondLineX = (int) ((measuredWidth + lineSpacingMultiplier * maxItemWidth) / 2.0F);
        if (initPosition == -1) {
            if (isLoop) {
                initPosition = (items.size() + 1) / 2;
            } else {
                initPosition = 0;
            }
        }

        mFirstLineStringStartX = maxItemWidth / 2;

        preCurrentIndex = initPosition;
    }

    void smoothScroll(ACTION action) {
        cancelFuture();
        if (action == ACTION.FLING || action == ACTION.DAGGLE) {
            float itemW = lineSpacingMultiplier * maxItemWidth;
            mOffset = (int) ((totalScrollX % itemW + itemW) % itemW);
            if ((float) mOffset > itemW / 2.0F) {
                mOffset = (int) (itemW - (float) mOffset);
            } else {
                mOffset = -mOffset;
            }
        }
        mFuture =
                mExecutor.scheduleWithFixedDelay(new SmoothScrollTimerTask(this, mOffset), 0, 10,
                        TimeUnit.MILLISECONDS);
    }

    protected final void scrollBy(float velocityY) {
        cancelFuture();
        // change this number, can change fling speed
        int velocityFling = 10;
        mFuture = mExecutor.scheduleWithFixedDelay(new InertiaTimerTask(this, velocityY), 0,
                velocityFling,
                TimeUnit.MILLISECONDS);
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

    /**
     * set text size in dp
     *
     * @param size
     */
    public final void setTextSize(float size) {
        if (size > 0.0F) {
            textSize = (int) (context.getResources().getDisplayMetrics().density * size);
            paintOuterText.setTextSize(textSize);
            paintCenterText.setTextSize(textSize);
        }
    }

    public final void setInitPosition(int initPosition) {
        if (initPosition < 0) {
            this.initPosition = 0;
        } else {
            if (items != null && items.size() > initPosition) {
                this.initPosition = initPosition;
            }
        }
    }

    public final void setListener(OnItemSelectedListener OnItemSelectedListener) {
        onItemSelectedListener = OnItemSelectedListener;
    }

    public final void setItems(List<Bean> items) {

        this.items = items;
        remeasure();
        invalidate();
    }


    public final int getSelectedItem() {
        return selectedItem;
    }
    //
    // protected final void scrollBy(float velocityY) {
    // Timer timer = new Timer();
    // mTimer = timer;
    // timer.schedule(new InertiaTimerTask(this, velocityY, timer), 0L, 20L);
    // }

    protected final void onItemSelected() {
        if (onItemSelectedListener != null) {
            postDelayed(new OnItemSelectedRunnable(this), 200L);
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
        if (items == null || items.isEmpty()) {
            return;
        }
        int size = items.size();
        if (position >= 0 && position < size && position != selectedItem) {
            initPosition = position;
            totalScrollX = 0;
            mOffset = 0;
            invalidate();
        }
    }

    private Bean mBean = new Bean("");

    @Override
    protected void onDraw(Canvas canvas) {
        if (items == null) {
            return;
        }

        change = (int) (totalScrollX / (lineSpacingMultiplier * maxItemWidth));
        preCurrentIndex = initPosition + change % items.size();

        if (!isLoop) {
            if (preCurrentIndex < 0) {
                preCurrentIndex = 0;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = items.size() - 1;
            }
        } else {
            if (preCurrentIndex < 0) {
                preCurrentIndex = items.size() + preCurrentIndex;
            }
            if (preCurrentIndex > items.size() - 1) {
                preCurrentIndex = preCurrentIndex - items.size();
            }
        }

        int j2 = (int) (totalScrollX % (lineSpacingMultiplier * maxItemWidth));
        // put value to drawingString
        int k1 = 0;
        while (k1 < itemsVisibleCount) {
            int l1 = preCurrentIndex - (itemsVisibleCount / 2 - k1);
            if (isLoop) {
                while (l1 < 0) {
                    l1 = l1 + items.size();
                }
                while (l1 > items.size() - 1) {
                    l1 = l1 - items.size();
                }
                drawingStrings[k1] = items.get(l1);
            } else if (l1 < 0) {
//                drawingStrings[k1] = "";
                drawingStrings[k1] = mBean;
            } else if (l1 > items.size() - 1) {
//                drawingStrings[k1] = "";
                drawingStrings[k1] = mBean;
            } else {
                // drawingStrings[k1] = items.get(l1);
                drawingStrings[k1] = items.get(l1);
            }
            k1++;
        }
        canvas.drawLine(firstLineX, 0, firstLineX, measuredHeight, paintIndicator);
        canvas.drawLine(secondLineX, 0, secondLineX, measuredHeight, paintIndicator);

        int i = 0;
        while (i < itemsVisibleCount) {
            canvas.save();
            float itemW = maxItemWidth * lineSpacingMultiplier;
            double radian = ((itemW * i - j2) * Math.PI) / halfCircumference;
            if (radian >= Math.PI || radian <= 0) {
                canvas.restore();
            } else {
                int translateX = (int) (radius - Math.cos(radian) * radius - (Math.sin(radian) *
                        maxItemWidth) / 2D);
                canvas.translate(translateX, 0.0F);
                canvas.scale((float) Math.sin(radian), 1.0F);
                Bean drawingString = drawingStrings[i];
                int xOffset = getTextX(drawingString.getFirtLine(), paintOuterText, tempRect);
                if (translateX <= firstLineX && maxItemWidth + translateX >= firstLineX) {
                    // first divider
                    canvas.save();
                    canvas.clipRect(0, 0, firstLineX - translateX, measuredHeight);
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX - xOffset,
                            getTextY(drawingString.getFirtLine(), paintOuterText, tempRect),
                            paintOuterText);
                    canvas.restore();
                    canvas.save();

                    canvas.clipRect(firstLineX - translateX, 0, itemW, measuredHeight);

                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX - xOffset,
                            getTextY(drawingString.getFirtLine(), paintCenterText, tempRect),
                            paintCenterText);
                    canvas.restore();
                } else if (translateX <= secondLineX && maxItemWidth + translateX >= secondLineX) {
                    // second divider
                    canvas.save();
//                    canvas.clipRect(0, 0, measuredWidth, secondLineY - translateX);
                    canvas.clipRect(0, 0, secondLineX - translateX, measuredHeight);
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX - xOffset,
                            getTextY(drawingString.getFirtLine(), paintCenterText, tempRect),
                            paintCenterText);
                    canvas.restore();
                    canvas.save();
//                    canvas.clipRect(0, secondLineY - translateX, measuredWidth, (int)
// (itemHeight));

                    canvas.clipRect(secondLineX - translateX, 0, (int) (itemW), measuredHeight);
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX - xOffset,
                            getTextY(drawingString.getFirtLine(), paintOuterText, tempRect),
                            paintOuterText);
                    canvas.restore();
                } else if (translateX >= firstLineX && maxItemWidth + translateX <= secondLineX) {
                    // center item
//                    canvas.clipRect(0, 0, measuredWidth, (int) (itemHeight));
                    canvas.clipRect(0, 0, (int) (itemW), measuredHeight);
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX - xOffset,
                            getTextY(drawingString.getFirtLine(), paintCenterText, tempRect),
                            paintCenterText);
                    selectedItem = items.indexOf(drawingString);
                } else {
                    // other item
                    canvas.clipRect(0, 0, (int) (itemW), measuredHeight);
                    canvas.drawText(drawingString.getFirtLine(), mFirstLineStringStartX - xOffset,
                            getTextY(drawingString.getFirtLine(), paintOuterText, tempRect),
                            paintOuterText);
                }
                canvas.restore();
            }
            i++;
        }
    }

    private int getTextX(String a, Paint paint,
                         Rect rect) {
        paint.getTextBounds(a, 0, a.length(), rect);
        int textWidth = rect.width();
        textWidth *= scaleX;
        return (int) (textWidth / 2) - 5;
    }

    // text start drawing position
    private int getTextY(String content, Paint paint, Rect rect) {
        paint.getTextBounds(content, 0, content.length(), rect);
        int textWidth = rect.height();
        return measuredHeight / 2 - textWidth / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        remeasure();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean eventConsumed = flingGestureDetector.onTouchEvent(event);
        float itemW = lineSpacingMultiplier * maxItemWidth;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                cancelFuture();
                previousX = event.getRawX();
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = previousX - event.getRawX();
                previousX = event.getRawX();

                totalScrollX = (int) (totalScrollX + dx);

                if (!isLoop) {
                    float top = -initPosition * itemW;
                    float bottom = (items.size() - 1 - initPosition) * itemW;

                    if (totalScrollX < top) {
                        totalScrollX = (int) top;
                    } else if (totalScrollX > bottom) {
                        totalScrollX = (int) bottom;
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            default:
                if (!eventConsumed) {
                    float x = event.getX();
                    double l = Math.acos((radius - x) / radius) * radius;
                    int circlePosition = (int) ((l + itemW / 2) / itemW);

                    float extraOffset = (totalScrollX % itemW + itemW) % itemW;
                    mOffset = (int) ((circlePosition - itemsVisibleCount / 2) * itemW -
                            extraOffset);

                    if ((System.currentTimeMillis() - startTime) > 120) {
                        smoothScroll(ACTION.DAGGLE);
                    } else {
                        smoothScroll(ACTION.CLICK);
                    }
                }
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }

        invalidate();
        return true;
    }


}