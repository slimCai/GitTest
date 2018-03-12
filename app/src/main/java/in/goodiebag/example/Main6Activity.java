package in.goodiebag.example;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.goodiebag.example.widget.FlowLayout;
import in.goodiebag.example.widget.RingBackgroundView;

public class Main6Activity extends AppCompatActivity {
    FlowLayout flowLayout;
    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag != null && tag instanceof Integer) {
                Toast.makeText(Main6Activity.this, "" + list.get((Integer) tag), Toast
                        .LENGTH_SHORT).show();
            }
        }
    };
    private List<String> list2;
    private RingBackgroundView ring;
    private List<String> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        datas = new ArrayList<>();
        datas.add("xx1");
        datas.add("xx2");
        datas.add("xx3");
        datas.add("xx4");
        datas.add("xx5");

        ring = (RingBackgroundView) findViewById(R.id.ring);
        ring.setData(datas);
        ring.setNowValue("xx2");

        flowLayout = (FlowLayout) findViewById(R.id.flow);
        list = new ArrayList<>();
        doAdd();
        LinearLayout ll = (LinearLayout) findViewById(R.id.fl);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flowLayout.setNeedResetChildHeight(true);
        for (int i = 0; i < 20; i++) {
            Log.d("slim", "i:" + list.get(i));
            TextView tv = new TextView(this);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            tv.setText(list.get(i));
            tv.setTextColor(Color.parseColor("#ff0000"));
            tv.setBackgroundColor(Color.parseColor("#000000"));
            tv.setLayoutParams(lp);
            tv.setTag(i);
            tv.setOnClickListener(mListener);
            flowLayout.addView(tv);
//            ll.addView(tv);
        }
        list2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list2.add(String.valueOf(i));
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                List<String> list1 = new ArrayList<>();
                list2 = list1;
                Log.d("slim", "size:" + list2.size());
            }
        }, 3000);
    }

    List<String> list;

    private void doAdd() {
        list.add("#试试");
        list.add("#啊啊啊啊");
        list.add("#啊");
        list.add("#213");
        list.add("#sd");
        list.add("#aaaaa");
        list.add("#xx");
        list.add("#fffsdf");
        list.add("#adfasdf");
        list.add("#a");
        list.add("#d");
        list.add("#dddd");
        list.add("#ee");
        list.add("#a");
        list.add("#123");
        list.add("#as");
        list.add("#adfasdf");
        list.add("#asd");
        list.add("#s");
        list.add("#aaa");
        list.add("#ddd");
        list.add("#adfasdf");
        list.add("#a");
    }

    public void aaaa(View view) {
        ring.setNowValue(datas.get(0));
    }

    public void bbbb(View view) {
        ring.setNowValue(datas.get(1));
    }

    public void cccc(View view) {
        ring.setNowValue(datas.get(2));
    }

    public void dddd(View view) {
        ring.setNowValue(datas.get(3));
    }

    public void eeee(View view) {
        ring.setNowValue(datas.get(4));
    }
}
