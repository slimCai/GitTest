package in.goodiebag.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.example.l.LoopView;
import in.goodiebag.example.n.bean.Bean;

public class Main2Activity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        LoopView loopView = (LoopView) findViewById(R.id.loop);
        LoopView loopView2 = (LoopView) findViewById(R.id.loop2);
        List<Bean> beanList = new ArrayList<>();
        List<Bean> beanList1 = new ArrayList<>();
        beanList.add(new Bean("慢放"));
        beanList1.add(new Bean("慢放"));
        beanList.add(new Bean("正常"));
        beanList1.add(new Bean("正常"));
        beanList1.add(new Bean("2倍快进"));
        beanList.add(new Bean("2倍快进"));
        beanList.add(new Bean("卓别林"));
        beanList.add(new Bean("步进"));
        beanList.add(new Bean("4倍快进"));
        beanList.add(new Bean("8毫米"));
        loopView.setItems(beanList1);
        loopView.setTextSize(12);
        loopView.setNotLoop();
        loopView.setCurrentPosition(0);
        loopView.setItemsVisibleCount(9);

        loopView2.setItems(beanList);
        loopView2.setTextSize(12);
        loopView2.setNotLoop();
        loopView2.setCurrentPosition(0);
        loopView2.setItemsVisibleCount(9);
//        loopView.setClickable(false);
//        loopView.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
    }

}
