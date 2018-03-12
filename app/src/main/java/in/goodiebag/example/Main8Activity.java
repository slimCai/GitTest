package in.goodiebag.example;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;

import in.goodiebag.example.widget.StaticLayoutView;
import in.goodiebag.example.widget.YoutubeDrawableSpan;

public class Main8Activity extends AppCompatActivity {
    private StaticLayoutView stlv;
    private String str = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main8);
//        MyFragmentDialog instance = MyFragmentDialog.getInstance();
//        instance.show(getSupportFragmentManager(),"");
        stlv = (StaticLayoutView) findViewById(R.id.stlv);
        String str = "大发送到发送发送到发送到发送地方，asdfasdfadsfadsfaa下载链接:https:www.youtube.be.com";
        int lastIndex = str.length();
        str+="youtube";
        Spannable spannable = new SpannableString(str);
        YoutubeDrawableSpan is = new YoutubeDrawableSpan(this);
        spannable.setSpan(is , str.indexOf("https") , lastIndex , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        stlv.setLayout(getStaticLayout(spannable,DensityUtil.getMetricsWidth(this),12,false));
        stlv.setTag("aaa");
        stlv.setTag(R.id.hint_id,"bbb");
        stlv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("slim", "tag:" + v.getTag() + "  tagId:" + v.getTag(R.id.hint_id));
            }
        });
    }


    public StaticLayout getStaticLayout(Spannable spannable, int width, int textSize, boolean isMedium) {
        Canvas dummyCanvas = new Canvas();
        StaticLayout staticLayout = null;
        try {
            staticLayout = new StaticLayout(
                    spannable,
                    getTextPaint(textSize),
                    width,
                    DefaultAlignment,
                    DefaultSpacingmult,
                    DefaultSpacingadd,
                    DefaultIncludepad
            );
        } catch (NullPointerException | IllegalArgumentException ignored) {
        }

        if (staticLayout != null) {
            staticLayout.draw(dummyCanvas);
        }
        return staticLayout;
    }


    public TextPaint getTextPaint(int textSize) {
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = getResources().getDisplayMetrics().density;

        textPaint.setTextSize(DensityUtil.dip2px(textSize));
        textPaint.setFlags(textPaint.getFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#000000"));
        return textPaint;
    }


    public static final Layout.Alignment DefaultAlignment = Layout.Alignment.ALIGN_NORMAL;
    public static final float DefaultSpacingmult = 1.0f;
    public static final float DefaultSpacingadd = 0f;
    public static final float Spacingadd_2 = DensityUtil.dip2px(2f);
    public static final boolean DefaultIncludepad = true;
    public static final int DefaultTextSize = 14;
    public static final float NormalTextSize = 15.5f;
    public static final int LittleTextSize = 12;
}
