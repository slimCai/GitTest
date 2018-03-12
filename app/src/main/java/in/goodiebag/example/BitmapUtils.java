package in.goodiebag.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by admin on 17/9/20.
 */

public class BitmapUtils {
    static boolean ISCHINESE = true;
    public static final int SHARE_WATERMARK_LEFT_TOP = 0;
    public static final int SHARE_WATERMARK_RIGHT_TOP = 1;
    public static final int SHARE_WATERMARK_LEFT_BOTTOM = 2;
    public static final int SHARE_WATERMARK_RIGHT_BOTTOM = 3;

    public static final int WATERMARK_CENTER = 0;
    public static final int WATERMARK_LEFT_TOP = 1;
    public static final int WATERMARK_LEFT_BOTTOM = 2;
    public static final int WATERMARK_RIGHT_TOP = 3;
    public static final int WATERMARK_RIGHT_BOTTOM = 4;

    /**
     * @param posState SHARE_WATERMARK_LEFT_TOP:左上 SHARE_WATERMARK_RIGHT_TOP:右上
     *                 SHARE_WATERMARK_LEFT_BOTTOM:左下 SHARE_WATERMARK_RIGHT_BOTTOM:右下
     */
    public static Bitmap getVideoShareWaterMarkBitmap(Context context, int videoWidth, int
            videoHeight, int posState) {
        Bitmap bitmap = Bitmap.createBitmap(videoWidth, videoHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#00000000"));
        float reqHeight;
        Rect rect = new Rect();
        int border;

        if (videoWidth > videoHeight) {
            reqHeight = (videoHeight * 0.05f);
            border = (int) (videoHeight * 0.035);
        } else {
            reqHeight = (videoWidth * 0.05f);
            border = (int) (videoWidth * 0.035);
        }
        Bitmap srcBitmap;
        if (ISCHINESE) {
            srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                    .watermark_cn);
        } else {
            srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                    .watermark_en);
        }

        Matrix mtrix = new Matrix();
        mtrix.postScale(reqHeight / (float) srcBitmap.getHeight(), reqHeight / (float) srcBitmap
                .getHeight());

        Bitmap waterMark = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                srcBitmap.getHeight(), mtrix, true);
//        srcBitmap.recycle();

        switch (posState) {
            case SHARE_WATERMARK_LEFT_TOP:
                rect.left = border;
                rect.top = border;
                rect.right = border + waterMark.getWidth();
                rect.bottom = border + waterMark.getHeight();
                break;
            case SHARE_WATERMARK_LEFT_BOTTOM:
                rect.left = border;
                rect.top = videoHeight - border - waterMark.getHeight();
                rect.right = border + waterMark.getWidth();
                rect.bottom = videoHeight - border;
                break;
            case SHARE_WATERMARK_RIGHT_TOP:
                rect.left = videoWidth - border - waterMark.getWidth();
                rect.right = videoWidth - border;
                rect.top = border;
                rect.bottom = border + waterMark.getHeight();
                break;
            case SHARE_WATERMARK_RIGHT_BOTTOM:
                rect.left = videoWidth - border - waterMark.getWidth();
                rect.right = videoWidth - border;
                rect.top = videoHeight - border - waterMark.getHeight();
                rect.bottom = videoHeight - border;
                break;
        }


        Paint paint = new Paint();
        paint.setAlpha(180);

        canvas.drawBitmap(waterMark, null, rect,
                paint);
        canvas.setBitmap(null);

        Bitmap bitmap1 = Bitmap.createBitmap(waterMark.getWidth(), waterMark.getHeight(), Bitmap
                .Config.ARGB_8888);

        Canvas canvas1 = new Canvas(bitmap1);

        canvas1.drawBitmap(waterMark, null, new Rect(0, 0, waterMark.getWidth(), waterMark
                .getHeight()), paint);
        return bitmap1;
    }


    /**
     * @param posState WATERMARK_LEFT_TOP:左上 WATERMARK_RIGHT_TOP:右上
     *                 WATERMARK_LEFT_BOTTOM:左下 WATERMARK_RIGHT_BOTTOM:右下
     *                 WATERMARK_CENTER:居中
     */
    public static Bitmap getTailWaterMarkBitmap(Context context, int videoWidth, int videoHeight,
                                                String userName,
                                                String musicName, String musicAuthor, int
                                                        posState) {
        float rate;
        if (videoHeight > videoWidth) {
            rate = (float) videoWidth / 1080;
        } else {
            rate = (float) videoHeight / 1080;
        }
        Bitmap bitmap = Bitmap.createBitmap(videoWidth, videoHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#000000"));
        Paint bigPaint = new Paint();
        Paint smallPaint = new Paint();
        bigPaint.setAntiAlias(true);
        smallPaint.setAntiAlias(true);
        bigPaint.setColor(Color.parseColor("#ffffff"));
        smallPaint.setColor(Color.parseColor("#ffffff"));
        bigPaint.setTextAlign(Paint.Align.CENTER);
        smallPaint.setTextAlign(Paint.Align.CENTER);
        float contentHeight;
        bigPaint.setTextSize(40 * rate);
        bigPaint.setFakeBoldText(true);
        Paint.FontMetrics bigMetrics = bigPaint.getFontMetrics();
        float bigHeight = bigMetrics.bottom - bigMetrics.top;
        smallPaint.setTextSize((30 * rate));
        smallPaint.setFakeBoldText(false);
        Paint.FontMetrics smallMetrics = smallPaint.getFontMetrics();
        float smallHeight = smallMetrics.bottom - smallMetrics.top;

        Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable
                .watermark_en);


        Bitmap waterMark = Bitmap.createScaledBitmap(srcBitmap, (int) (260 * rate), (int) (rate *
                260 * srcBitmap.getHeight() / srcBitmap.getWidth()), false);
        int state;
        if (!TextUtil.isValidate(userName)) {
            if (!TextUtil.isValidate(musicName)) {
                //无用户 无音乐
                contentHeight = 0;
                state = 0;
            } else {

                if (!TextUtil.isValidate(musicAuthor)) {
                    //无用户 有音乐 无音乐作者
                    contentHeight = (smallHeight + 25 * rate + bigHeight + 5 * rate);
                    state = 1;
                } else {
                    //无用户 有音乐 有音乐作者
                    contentHeight = (smallHeight + 25 * rate + bigHeight + 5 * rate + bigHeight +
                            5 * rate + bigHeight + 150 * rate);
                    state = 2;
                }
            }
        } else {
            if (!TextUtil.isValidate(musicName)) {
                //有用户 无音乐
                contentHeight = smallHeight + 25 * rate + bigHeight + 95 * rate;
                state = 3;
            } else {

                if (!TextUtil.isValidate(musicAuthor)) {
                    //有用户 有音乐 无音乐作者
                    contentHeight = (smallHeight + 25 * rate + bigHeight + 95 * rate +
                            smallHeight + 25 * rate + bigHeight + 5 * rate);
                    state = 4;
                } else {
                    //有用户 有音乐 有音乐作者
                    contentHeight = (smallHeight + 25 * rate + bigHeight + 95 * rate +
                            smallHeight + 25 * rate + bigHeight + 5 * rate + bigHeight +
                            5 * rate + bigHeight + 150 * rate);
                    state = 5;
                }
            }
        }
        int topOffset = 0;
        Rect rect = new Rect();
        int border;

        if (videoWidth > videoHeight) {
            border = (int) (videoHeight * 0.035);
        } else {
            border = (int) (videoWidth * 0.035);
        }

        switch (posState) {
            case WATERMARK_CENTER:
                if (contentHeight == 0) {
                    rect.left = (bitmap.getWidth() - waterMark.getWidth()) / 2;
                    rect.top = (bitmap.getHeight() - waterMark.getHeight()) / 2;
                    rect.right = (bitmap.getWidth() + waterMark.getWidth()) / 2;
                    rect.bottom = (bitmap.getHeight() + waterMark.getHeight()) / 2;
                    canvas.drawBitmap(waterMark, null, rect, null);
                    canvas.setBitmap(null);
                    return bitmap;
                } else {
                    contentHeight += waterMark.getHeight();
                }
                break;
            case WATERMARK_LEFT_BOTTOM:
                rect.left = border;
                rect.top = videoHeight - border - waterMark.getHeight();
                rect.right = border + waterMark.getWidth();
                rect.bottom = videoHeight - border;
                canvas.drawBitmap(waterMark, null, rect, null);
                if (contentHeight == 0) {
                    canvas.setBitmap(null);
                    return bitmap;
                }
                break;
            case WATERMARK_LEFT_TOP:
                rect.left = border;
                rect.top = border;
                rect.right = border + waterMark.getWidth();
                rect.bottom = border + waterMark.getHeight();
                canvas.drawBitmap(waterMark, null, rect, null);
                if (contentHeight == 0) {
                    canvas.setBitmap(null);
                    return bitmap;
                }
                break;
            case WATERMARK_RIGHT_BOTTOM:
                rect.left = videoWidth - border - waterMark.getWidth();
                rect.right = videoWidth - border;
                rect.top = videoHeight - border - waterMark.getHeight();
                rect.bottom = videoHeight - border;
                canvas.drawBitmap(waterMark, null, rect, null);
                if (contentHeight == 0) {
                    canvas.setBitmap(null);
                    return bitmap;
                }
                break;
            case WATERMARK_RIGHT_TOP:
                rect.left = videoWidth - border - waterMark.getWidth();
                rect.right = videoWidth - border;
                rect.top = border;
                rect.bottom = border + waterMark.getHeight();
                canvas.drawBitmap(waterMark, null, rect, null);
                if (contentHeight == 0) {
                    canvas.setBitmap(null);
                    return bitmap;
                }
                break;
        }
        topOffset = (int) ((bitmap.getHeight() - contentHeight) / 2);
        if (posState == WATERMARK_CENTER) {
            rect.left = (bitmap.getWidth() - waterMark.getWidth()) / 2;
            rect.top = bitmap.getHeight() - topOffset - waterMark.getHeight();
            rect.right = (bitmap.getWidth() + waterMark.getWidth()) / 2;
            rect.bottom = bitmap.getHeight() - topOffset;
            canvas.drawBitmap(waterMark, null, rect, null);
        }
        String musicNameStr;
        String musicAuthorStr;
        String courtesyStr = "Courtesy of Original Artist";
        String userTitleStr = "DIRECTED BY";
        switch (state) {
            case 1:
                //无用户 有音乐 无音乐作者
                canvas.drawText("MUSIC", videoWidth / 2, topOffset + (smallHeight / 2), smallPaint);
                musicNameStr = "\"" + musicName + "\"";
                canvas.drawText(musicNameStr, videoWidth / 2, (topOffset + smallHeight +
                        bigHeight / 2 + 25 * rate), bigPaint);
                break;
            case 2:
                //无用户 有音乐 有音乐作者
                canvas.drawText("MUSIC", videoWidth / 2, topOffset + (smallHeight / 2), smallPaint);
                musicNameStr = "\"" + musicName + "\"";
                canvas.drawText(musicNameStr, videoWidth / 2, (topOffset + smallHeight +
                        bigHeight / 2 + 25 * rate), bigPaint);
                musicAuthorStr = "Performed by " + musicAuthor;
                canvas.drawText(musicAuthorStr, videoWidth / 2, (topOffset + smallHeight +
                        bigHeight + 25 * rate + 5 * rate + bigHeight / 2), bigPaint);
                canvas.drawText(courtesyStr, videoWidth / 2, (topOffset + smallHeight +
                                bigHeight + 25 * rate + 5 * rate + bigHeight + 5 * rate +
                                bigHeight / 2),
                        bigPaint);
                break;
            case 3:
                //有用户 无音乐
                canvas.drawText(userTitleStr, videoWidth / 2, topOffset + (smallHeight / 2),
                        smallPaint);
                canvas.drawText(userName, videoWidth / 2, topOffset + smallHeight + 25 * rate +
                        bigHeight / 2, bigPaint);
                break;
            case 4:
                //有用户 有音乐 无音乐作者
                canvas.drawText(userTitleStr, videoWidth / 2, topOffset + (smallHeight / 2),
                        smallPaint);
                canvas.drawText(userName, videoWidth / 2, topOffset + smallHeight + 25 * rate +
                        bigHeight / 2, bigPaint);
                canvas.drawText("MUSIC", videoWidth / 2, topOffset + smallHeight + 25 * rate +
                        bigHeight + 95 * rate + smallHeight / 2, smallPaint);
                musicNameStr = "\"" + musicName + "\"";
                canvas.drawText(musicNameStr, videoWidth / 2, topOffset + smallHeight + 25 * rate
                                + bigHeight + 95 * rate + smallHeight + 25 * rate + bigHeight / 2,
                        bigPaint);
                break;
            case 5:
                //有用户 有音乐 有音乐作者
                canvas.drawText(userTitleStr, videoWidth / 2, topOffset + (smallHeight / 2),
                        smallPaint);
                canvas.drawText(userName, videoWidth / 2, topOffset + smallHeight + 25 * rate +
                        bigHeight / 2, bigPaint);
                canvas.drawText("MUSIC", videoWidth / 2, topOffset + smallHeight + 25 * rate +
                        bigHeight + 95 * rate + smallHeight / 2, smallPaint);
                musicNameStr = "\"" + musicName + "\"";
                canvas.drawText(musicNameStr, videoWidth / 2, topOffset + smallHeight + 25 * rate
                                + bigHeight + 95 * rate + smallHeight + 25 * rate + bigHeight / 2,
                        bigPaint);
                musicAuthorStr = "Performed by " + musicAuthor;
                canvas.drawText(musicAuthorStr, videoWidth / 2, topOffset + smallHeight + 25 *
                        rate + bigHeight + 95 * rate + smallHeight + 25 * rate + bigHeight + 5 *
                        rate + bigHeight / 2, bigPaint);
                canvas.drawText(courtesyStr, videoWidth / 2, topOffset + smallHeight + 25 * rate
                        + bigHeight + 95 * rate + smallHeight + 25 * rate + bigHeight + 5 * rate
                        + bigHeight + 5 * rate + bigHeight / 2, bigPaint);
                break;
        }
        canvas.setBitmap(null);
        return bitmap;
    }

    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {

            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

                .getHeight(), Bitmap.Config.ARGB_8888);

        return sourceImg;
    }

}
