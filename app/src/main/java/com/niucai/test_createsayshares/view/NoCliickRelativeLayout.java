package com.niucai.test_createsayshares.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Qi on 2017/12/26.
 */

public class NoCliickRelativeLayout extends RelativeLayout {
    public NoCliickRelativeLayout(Context context) {
        super(context);
    }

    public NoCliickRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoCliickRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }
}
