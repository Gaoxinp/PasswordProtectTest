package com.example.passwordprotecttest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

/**
 * Created by 高信朋 on 2016/11/10.
 */

public class MyExpandableListView extends ExpandableListView {
    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);




        return super.dispatchTouchEvent(ev);
    }
}
