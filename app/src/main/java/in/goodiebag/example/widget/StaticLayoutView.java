package in.goodiebag.example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import in.goodiebag.example.TextUtil;


/**
 * StaticLayoutView
 * <p>
 * Created by luoshiqiang on 15/8/29.
 */
public class StaticLayoutView extends View {

    private Layout layout = null;
    private TouchableSpan mPressedSpan;
    private int width = 0;
    private TouchCallback mTouchCallback;
    private boolean isTimeOut = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            isTimeOut = true;
            if(mTouchCallback2 != null){
                mTouchCallback2.onLongClick();
            }
        }
    };
    private Handler mHandler = new Handler(){};
    private TouchCallback2 mTouchCallback2;

    public interface TouchCallback2{
        void startTouchDown();
        void startMove();
        void onUpOrCancle();
        void onClick();
        void onLongClick();
    }

    public void setTouchCallback2(TouchCallback2 mTouchCallback2){
        this.mTouchCallback2 = mTouchCallback2;
    }

    //应对7.1.1的问题  点击时莫名错乱问题 暂时处理
//    private boolean isComment;

//    public void setIsComment() {
////        isComment = true;
//    }

    public void setTouchCallback(TouchCallback touchCallback) {
        this.mTouchCallback = touchCallback;
    }

    public StaticLayoutView(Context context) {
        this(context, null);
    }

    public StaticLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StaticLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        requestLayout();
        invalidate();
    }

    public Layout getLayout() {
        return layout;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        if (layout != null) {
            canvas.translate(getPaddingLeft(), getPaddingTop());
            layout.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (layout != null) {
            setMeasuredDimension(layout.getWidth() + width, layout.getHeight()+getPaddingBottom());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getLayout() == null) {
            return super.onTouchEvent(event);
        }
        super.onTouchEvent(event);
        Spannable spannable = getLayout().getText() instanceof Spannable ? (Spannable) getLayout().getText() : null;
        try {
            if (TextUtil.isValidate(spannable)) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP
                        || action == MotionEvent.ACTION_DOWN) {

                    int x = (int) event.getX();
                    int y = (int) event.getY();

//                    LogUtil.d("infoinfo", "StaticLayoutView x : " + x);
//                    LogUtil.d("infoinfo", "StaticLayoutView y : " + y);
                    int paddingLeft = getPaddingLeft();
                    x -= paddingLeft;
                    int paddingTop = getPaddingTop();
                    y -= paddingTop;
//                    LogUtil.d("infoinfo", "StaticLayoutView paddingLeft : " + paddingLeft);
//                    LogUtil.d("infoinfo", "StaticLayoutView paddingTop : " + paddingTop);
                    int scrollX = getScrollX();
                    x += scrollX;
                    int scrollY = getScrollY();
                    y += scrollY;
//                    LogUtil.d("infoinfo", "StaticLayoutView scrollX : " + scrollX);
//                    LogUtil.d("infoinfo", "StaticLayoutView scrollY : " + scrollY);
                    Layout layout = getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);
//                    LogUtil.d("infoinfo", "StaticLayoutView line : " + line);
//                    LogUtil.d("infoinfo", "StaticLayoutView off : " + off);

                    TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);

                    if (link.length != 0) {
                        mPressedSpan = link[0];
                        if (action == MotionEvent.ACTION_UP) {
                            mPressedSpan.onClick(this);
                            int spanStart = spannable.getSpanStart(mPressedSpan);
                            int spanEnd = spannable.getSpanEnd(mPressedSpan);
//                            LogUtil.d("infoinfo", "StaticLayoutView ACTION_UP spanStart : " + spanStart);
//                            LogUtil.d("infoinfo", "StaticLayoutView ACTION_UP spanEnd : " + spanEnd);
                            if (mTouchCallback != null) {
                                mTouchCallback.onTouchUp(spanStart, spanEnd);
                            }
                            spannable.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            if (isComment) {
//                                spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }
                            Selection.removeSelection(spannable);
                        } else if (action == MotionEvent.ACTION_DOWN) {
                            int spanStart = spannable.getSpanStart(mPressedSpan);
                            int spanEnd = spannable.getSpanEnd(mPressedSpan);
//                            LogUtil.d("infoinfo", "StaticLayoutView ACTION_DOWN spanStart : " + spanStart);
//                            LogUtil.d("infoinfo", "StaticLayoutView ACTION_DOWN spanEnd : " + spanEnd);
                            if (mTouchCallback != null) {
                                mTouchCallback.onTouchDown(spanStart, spanEnd);
                            }
//                            if (isComment) {
//                                spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }
                            spannable.setSpan(new BackgroundColorSpan(Color.parseColor("#cccccc")), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Selection.removeSelection(spannable);
                        }
                        requestLayout();
                        invalidate();
                        return true;
                    } else {
                        int spanStart = spannable.getSpanStart(mPressedSpan);
                        int spanEnd = spannable.getSpanEnd(mPressedSpan);
//                        LogUtil.d("infoinfo", "StaticLayoutView link.length = 0 spanStart : " + spanStart);
//                        LogUtil.d("infoinfo", "StaticLayoutView link.length = 0 spanEnd : " + spanEnd);
                        if (mTouchCallback != null) {
                            mTouchCallback.onTouchClear(spanStart, spanEnd);
                        }
                        if (!(spanStart == -1 || spanEnd == -1)) {
                            spannable.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            if (isComment) {
//                                spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }
                        }
                        requestLayout();
                        invalidate();
                        Selection.removeSelection(spannable);
                        if(action == MotionEvent.ACTION_DOWN){
                            isTimeOut = false;
                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable,300);
                            if(mTouchCallback2 != null){
                                mTouchCallback2.startTouchDown();
                            }
                        }else if(action == MotionEvent.ACTION_UP){
                            mHandler.removeCallbacks(mRunnable);
                            if(mTouchCallback2 != null){
                                mTouchCallback2.onUpOrCancle();
                            }
                            if(!isTimeOut && mTouchCallback2 != null){
                                mTouchCallback2.onClick();
                            }
                        }
                        return true;
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {
                    TouchableSpan touchedSpan = getPressedSpan(this, spannable, event);
                    if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                        int spanStart = spannable.getSpanStart(mPressedSpan);
                        int spanEnd = spannable.getSpanEnd(mPressedSpan);
//                        LogUtil.d("infoinfo", "StaticLayoutView ACTION_MOVE spanStart : " + spanStart);
//                        LogUtil.d("infoinfo", "StaticLayoutView ACTION_MOVE spanEnd : " + spanEnd);
                        if (mTouchCallback != null) {
                            mTouchCallback.onTouchMove(spanStart, spanEnd);
                        }
                        if (!(spanStart == -1 || spanEnd == -1)) {
                            spannable.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            if (isComment) {
//                                spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            }
                        }
                        if(mTouchCallback2 != null ){
                            mHandler.removeCallbacks(mRunnable);
                            isTimeOut = false;
                            mTouchCallback2.startMove();
                        }
                        requestLayout();
                        invalidate();
                        Selection.removeSelection(spannable);

                    }
                } else {
                    int spanStart = spannable.getSpanStart(mPressedSpan);
                    int spanEnd = spannable.getSpanEnd(mPressedSpan);
//                    LogUtil.d("infoinfo", "StaticLayoutView else spanStart : " + spanStart);
//                    LogUtil.d("infoinfo", "StaticLayoutView else spanEnd : " + spanEnd);
                    if (mTouchCallback != null) {
                        mTouchCallback.onTouchClear(spanStart, spanEnd);
                    }
                    if (!(spanStart == -1 || spanEnd == -1)) {
                        spannable.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        if (isComment) {
//                            spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        }
                    }
                    if(mTouchCallback2 != null ){
                        mHandler.removeCallbacks(mRunnable);
                        isTimeOut = false;
                        mTouchCallback2.startMove();
                    }
                    requestLayout();
                    invalidate();
                    Selection.removeSelection(spannable);
                }
            } else {
                int spanStart = spannable.getSpanStart(mPressedSpan);
                int spanEnd = spannable.getSpanEnd(mPressedSpan);
//                LogUtil.d("infoinfo", "StaticLayoutView else else spanStart : " + spanStart);
//                LogUtil.d("infoinfo", "StaticLayoutView else else spanEnd : " + spanEnd);
                if (mTouchCallback != null) {
                    mTouchCallback.onTouchClear(spanStart, spanEnd);
                }
                if (!(spanStart == -1 || spanEnd == -1)) {
                    spannable.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    if (isComment) {
//                        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), spanStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    }
                }
                if(mTouchCallback2 != null ){
                    mHandler.removeCallbacks(mRunnable);
                    isTimeOut = false;
                    mTouchCallback2.startMove();
                }
                requestLayout();
                invalidate();
                Selection.removeSelection(spannable);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    private TouchableSpan getPressedSpan(StaticLayoutView staticLayoutView, Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= staticLayoutView.getPaddingLeft();
        y -= staticLayoutView.getPaddingTop();

        x += staticLayoutView.getScrollX();
        y += staticLayoutView.getScrollY();

        Layout layout = staticLayoutView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);
        TouchableSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    public interface TouchCallback {
        void onTouchDown(int start, int end);

        void onTouchUp(int start, int end);

        void onTouchMove(int start, int end);

        void onTouchClear(int start, int end);
    }
}
