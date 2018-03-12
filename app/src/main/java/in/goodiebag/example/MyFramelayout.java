package in.goodiebag.example;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * Created by admin on 17/9/13.
 */

public class MyFramelayout extends FrameLayout {
    public MyFramelayout(@NonNull Context context) {
        super(context);
    }

    public MyFramelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFramelayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("slim", "sss");

        if (onSizeChange != null)
            onSizeChange.size(getMeasuredHeight(), getMeasuredWidth(), getLeft(), getRight());
    }

    private OnSizeChange onSizeChange;

    public void setOnSizeChangeListener(OnSizeChange onSizeChange) {
        this.onSizeChange = onSizeChange;
    }

    public interface OnSizeChange {
        void size(int height, int width, int left, int right);
    }
}
