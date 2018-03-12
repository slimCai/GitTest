package in.goodiebag.example;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class Main3Activity extends Activity {
    ImageView iv;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        iv = (ImageView) findViewById(R.id.iv);
        bitmap = BitmapUtils.getTailWaterMarkBitmap(this,720,1080,"aa","ss","",BitmapUtils.WATERMARK_LEFT_BOTTOM);
//        bitmap = BitmapUtils.getVideoShareWaterMarkBitmap(this,720,1080,BitmapUtils.WATERMARK_CENTER);
        iv.setImageBitmap(createRoundedRectBitmap(bitmap,DensityUtil.dip2px(5),0,0,DensityUtil.dip2px(5)));
    }

    public void click(View v) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Log.d("aa","aa");            bitmap.recycle();
            bitmap = null;
        }else{
            Log.d("aa","bb");
        }
    }

    private  Bitmap createRoundedRectBitmap(@NonNull Bitmap bitmap,
                                            float topLeftCorner, float topRightCorner,
                                            float bottomRightCorner, float bottomLeftCorner) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = Color.WHITE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        Path path = new Path();
        float[] radii = new float[]{
                topLeftCorner, bottomLeftCorner,
                topRightCorner, topRightCorner,
                bottomRightCorner, bottomRightCorner,
                bottomLeftCorner, bottomLeftCorner
        };

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
