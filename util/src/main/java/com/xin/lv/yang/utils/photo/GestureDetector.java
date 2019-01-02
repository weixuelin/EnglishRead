package com.xin.lv.yang.utils.photo;

import android.view.MotionEvent;


public interface GestureDetector {

    boolean onTouchEvent(MotionEvent var1);

    boolean isScaling();

    boolean isDragging();

    void setOnGestureListener(OnGestureListener var1);
}
