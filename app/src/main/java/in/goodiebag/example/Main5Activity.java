package in.goodiebag.example;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;

public class Main5Activity extends Activity {
    private ListView mList;
    private DrawerLayout drawerLayout;
//    private LinearLayout drawerContent;

    @SuppressLint("WifiManagerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.top);
//        setSupportActionBar(toolbar);
        View view = findViewById(R.id.nav_view);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) (DensityUtil.getMetricsWidth(this) * 0.6f);
        view.setLayoutParams(layoutParams);
        mList = (ListView) findViewById(R.id.list);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
//        drawerLayout.set
//        drawerContent = (LinearLayout) findViewById(R.id.drawer_content);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        StatusBarUtil.setColorNav(this);
        StatusBarUtil.setColorNav(this);
        StatusBarUtil.setColorNav(this);
        StatusBarUtil.setColorNav(this);
        StatusBarUtil.setColorNav(this);
    }

    WifiManager wifiManager;

    public void ccc(View v) {
        Log.d("slim", "ccc");
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        } else {
            wifiManager.setWifiEnabled(true);
        }

    }

    public void aa(View v) {
        Log.d("slim", "aa");
        //(JZUtils.scanForActivity(getContext()))//.getWindow().getDecorView();
        ViewGroup vp = (ViewGroup) Main5Activity.this.findViewById(Window.ID_ANDROID_CONTENT);
        Log.d("slim", "vp:" + vp.getChildCount());
        View old = vp.findViewById(R.id.jz_fullscreen_id);
        if (old != null) {
            vp.removeView(old);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        Log.d("slim", "count:" + decorView.getChildCount());
        deleteAll(decorView);
        View view = new View(this);
        view.setBackgroundColor(Color.parseColor("#ff0000"));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vp.addView(view, lp);
    }

    private void deleteAll(ViewGroup decorView) {
        View v = decorView.findViewById(R.id.h_dec);
        if (v != null) {
            decorView.removeView(v);
            deleteAll(decorView);
        }
    }

    public void bb(View v) {
        Log.d("slim", "bb");
    }

    public void show(View v) {
        drawerLayout.openDrawer(Gravity.START, true);
    }

    public void sd(View v) {
    }
}
