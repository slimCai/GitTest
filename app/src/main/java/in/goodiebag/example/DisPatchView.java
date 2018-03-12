package in.goodiebag.example;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by admin on 17/9/24.
 */

public class DisPatchView extends View {
    private TouchView tab;
    private HorizontalScrollView root;

    public void setTab(TouchView tab) {
        this.tab = tab;
    }

    public void setRoot(HorizontalScrollView root) {
        this.root = root;
    }

    private boolean falg;

    public void setFalg(boolean falg) {
        this.falg = falg;
    }

    public boolean isFalg() {
        return falg;
    }

    public DisPatchView(Context context) {
        super(context);
    }

    public DisPatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisPatchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private MotionEvent lastDown;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastDown = event;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastDown = null;
                break;
        }
        Log.d("slim", "222");
        if (falg) {
            if (event.getAction() == MotionEvent.ACTION_MOVE && lastDown != null) {
                root.onTouchEvent(lastDown);
                lastDown = null;
            }
            root.onTouchEvent(event);
        } else {
            tab.onTouchEvent(event);
        }
        return true;
    }
}
