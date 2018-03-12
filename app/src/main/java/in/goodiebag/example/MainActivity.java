package in.goodiebag.example;

import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import in.goodiebag.carouselpicker.LoopPagerAdapterWrapper;
import in.goodiebag.example.R;

public class MainActivity extends AppCompatActivity {
    CarouselPicker imageCarousel, textCarousel, mixCarousel;
    TextView tvSelected;
    View v1, v2, v3, v4, v5;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("slim", "click");
            switch (v.getId()) {
                case R.id.v1:
                    changePage(-2);
                    break;
                case R.id.v2:
                    changePage(-1);
                    break;
                case R.id.v3:
                    changePage(0);
                    break;
                case R.id.v4:
                    changePage(1);
                    break;
                case R.id.v5:
                    changePage(2);
                    break;
            }
        }
    };

    private void changePage(int offset) {
        Log.d("slim", "offset:" + offset);
        textCarousel.setCurrentItem(textCarousel.getOffset() + offset, false);
        textCarousel.requestLayout();
        textCarousel.invalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageCarousel = (CarouselPicker) findViewById(R.id.imageCarousel);
        textCarousel = (CarouselPicker) findViewById(R.id.textCarousel);
        mixCarousel = (CarouselPicker) findViewById(R.id.mixCarousel);
        tvSelected = (TextView) findViewById(R.id.tvSelectedItem);
        v1 = findViewById(R.id.v1);
        v1.setOnClickListener(onClickListener);
        v2 = findViewById(R.id.v2);
        v2.setOnClickListener(onClickListener);
        v3 = findViewById(R.id.v3);
        v3.setOnClickListener(onClickListener);
        v4 = findViewById(R.id.v4);
        v4.setOnClickListener(onClickListener);
        v5 = findViewById(R.id.v5);
        v5.setOnClickListener(onClickListener);
        MyFramelayout fl = (MyFramelayout) findViewById(R.id.fl);
        fl.setOnSizeChangeListener(new MyFramelayout.OnSizeChange() {
            @Override
            public void size(int height, int width, int left, int right) {
                int thisW = width / 5;
                setWidthAndMargin(v1, 0, thisW);
                setWidthAndMargin(v2, 1, thisW);
                setWidthAndMargin(v3, 2, thisW);
                setWidthAndMargin(v4, 3, thisW);
                setWidthAndMargin(v5, 4, thisW);
                textCarousel.setmLeft(left);
                textCarousel.setmWidth(thisW);
                textCarousel.setmRight(right);
            }
        });

        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        imageItems.add(new CarouselPicker.TextItem("hi1", 10));
        imageItems.add(new CarouselPicker.TextItem("hi2", 10));
        imageItems.add(new CarouselPicker.TextItem("hi3", 10));
        imageItems.add(new CarouselPicker.TextItem("hi4", 10));
        imageItems.add(new CarouselPicker.TextItem("hi5", 10));
        imageItems.add(new CarouselPicker.TextItem("hi6", 10));
        imageItems.add(new CarouselPicker.TextItem("hi7", 10));
        imageItems.add(new CarouselPicker.TextItem("hi8", 10));
        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker
                .CarouselViewAdapter(this, 8, imageItems, 0);
        imageCarousel.setAdapter(imageAdapter);
        imageCarousel.setUnit(8);
        imageCarousel.setCurrentItem(404);

//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) imageCarousel
//                .getLayoutParams();
//        WindowManager wm = this.getWindowManager();
//        Point p = new Point();
//        wm.getDefaultDisplay().getSize(p);
//        lp.leftMargin = p.x / 5;
//        lp.rightMargin = p.x / 5;
//        imageCarousel.setLayoutParams(lp);

        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("hi1", 10));
        textItems.add(new CarouselPicker.TextItem("hi2", 10));
        textItems.add(new CarouselPicker.TextItem("hi3", 10));
        textItems.add(new CarouselPicker.TextItem("hi4", 10));
        textItems.add(new CarouselPicker.TextItem("hi5", 10));
        final CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker
                .CarouselViewAdapter(this, 5, textItems, 0);

        textCarousel.setAdapter(textAdapter);
        textCarousel.setUnit(5);
        textCarousel.setCurrentItem(507, false);
//        textCarousel.setUnit(5);
        textCarousel.setOnPagerClick(new CarouselPicker.OnPagerClick() {
            @Override
            public void onClick(int pos) {
                switch (pos) {
                    case 0:
                        v1.performClick();
                        break;
                    case 1:
                        v2.performClick();
                        break;
                    case 2:
                        v3.performClick();
                        break;
                    case 3:
                        v4.performClick();
                        break;
                    case 4:
                        v5.performClick();
                        break;
                }
            }
        });
        textCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvSelected.setText("Selected item in image carousel is  : " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    public void click(View v) {
        textCarousel.scrollOffset(-2);
    }

    private void setWidthAndMargin(View v, int i, int thisW) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) v.getLayoutParams();
        lp.width = thisW;
        lp.leftMargin = thisW * i;
        v.setLayoutParams(lp);
    }
}
