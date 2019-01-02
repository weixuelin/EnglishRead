package com.xin.lv.yang.utils.photo.image;

import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;


public class GestureDetector {
    /** @deprecated */
    @Deprecated
    public GestureDetector(android.view.GestureDetector.OnGestureListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }

    /** @deprecated */
    @Deprecated
    public GestureDetector(android.view.GestureDetector.OnGestureListener listener) {
        throw new RuntimeException("Stub!");
    }

    public GestureDetector(Context context, android.view.GestureDetector.OnGestureListener listener) {
        throw new RuntimeException("Stub!");
    }

    public GestureDetector(Context context, android.view.GestureDetector.OnGestureListener listener, Handler handler) {
        throw new RuntimeException("Stub!");
    }

    public GestureDetector(Context context, android.view.GestureDetector.OnGestureListener listener, Handler handler, boolean unused) {
        throw new RuntimeException("Stub!");
    }

    public void setOnDoubleTapListener(android.view.GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        throw new RuntimeException("Stub!");
    }

    public void setContextClickListener(android.view.GestureDetector.OnContextClickListener onContextClickListener) {
        throw new RuntimeException("Stub!");
    }

    public void setIsLongpressEnabled(boolean isLongpressEnabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isLongpressEnabled() {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    public boolean onGenericMotionEvent(MotionEvent ev) {
        throw new RuntimeException("Stub!");
    }

    public static class SimpleOnGestureListener implements OnGestureListener, OnDoubleTapListener, OnContextClickListener {
        public SimpleOnGestureListener() {
            throw new RuntimeException("Stub!");
        }

        public boolean onSingleTapUp(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public void onLongPress(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            throw new RuntimeException("Stub!");
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            throw new RuntimeException("Stub!");
        }

        public void onShowPress(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public boolean onDown(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public boolean onDoubleTap(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }

        public boolean onContextClick(MotionEvent e) {
            throw new RuntimeException("Stub!");
        }
    }

    public interface OnContextClickListener {
        boolean onContextClick(MotionEvent var1);
    }

    public interface OnDoubleTapListener {
        boolean onSingleTapConfirmed(MotionEvent var1);

        boolean onDoubleTap(MotionEvent var1);

        boolean onDoubleTapEvent(MotionEvent var1);
    }

    public interface OnGestureListener {
        boolean onDown(MotionEvent var1);

        void onShowPress(MotionEvent var1);

        boolean onSingleTapUp(MotionEvent var1);

        boolean onScroll(MotionEvent var1, MotionEvent var2, float var3, float var4);

        void onLongPress(MotionEvent var1);

        boolean onFling(MotionEvent var1, MotionEvent var2, float var3, float var4);
    }
}
