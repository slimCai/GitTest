package in.goodiebag.example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.DynamicDrawableSpan;

import in.goodiebag.example.R;


/**
 * @author ccx admin on 17/12/10.
 */

public class YoutubeDrawableSpan extends DynamicDrawableSpan {
    private boolean touchDown;
    private Context context;
    private Paint paint;

    public YoutubeDrawableSpan(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
    }

    public void setTouchDown(boolean isTouchDown) {
        this.touchDown = isTouchDown;
    }

    @Override
    public Drawable getDrawable() {
        Drawable finalDrawable = context.getResources().getDrawable(R.drawable.icon_12_link_youtube);
        finalDrawable.setBounds(0, 0, finalDrawable.getIntrinsicWidth(), finalDrawable.getIntrinsicHeight());
        return finalDrawable;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            //获得文字、图片高度
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right + (getTextWidth(paint, "说啥"));

    }

    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY;
        //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        //偏移画布后开始绘制
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = ((bottom - top) - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText("说啥", x + b.getBounds().right, baseline + top, paint);
//        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
    }
}
