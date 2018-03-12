package in.goodiebag.example;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class Main4Activity extends AppCompatActivity {
    HorizontalScrollView hsv, hsv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tv2 = (TextView) findViewById(R.id.tv2);
        TextView tv3 = (TextView) findViewById(R.id.tv3);
        TextView tv4 = (TextView) findViewById(R.id.tv4);

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "avenirnextcondensedmedium.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "avenirnextcondensedregular.ttf");
        Typeface tf3 = Typeface.createFromAsset(getAssets(), "avenirnextregular.ttf");
        Typeface tf4 = Typeface.createFromAsset(getAssets(), "avenirnextultralight.ttf");

        tv1.setTypeface(tf1);
        tv2.setTypeface(tf2);
        tv3.setTypeface(tf3);
        tv4.setTypeface(tf4);

        String s="DIRECTED BY ttteee";
        tv1.setText(s);
        tv2.setText(s);
        tv3.setText(s);
        tv4.setText(s);
    }

}
