package com.xin.lv.yang.utils.photo.image;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * Created by Administrator on 2017/5/15.
 */

public class FroyoGestureDetector  extends EclairGestureDetector {
    protected final ScaleGestureDetector mDetector;

    public FroyoGestureDetector(Context context) {
        super(context);
        ScaleGestureDetector.OnScaleGestureListener mScaleListener = new ScaleGestureDetector.OnScaleGestureListener() {
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                if(!Float.isNaN(scaleFactor) && !Float.isInfinite(scaleFactor)) {
                    FroyoGestureDetector.this.mListener.onScale(scaleFactor, detector.getFocusX(), detector.getFocusY());
                    return true;
                } else {
                    return false;
                }
            }

            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            public void onScaleEnd(ScaleGestureDetector detector) {
            }
        };
        this.mDetector = new ScaleGestureDetector(context, mScaleListener);
    }

    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }

    public boolean onTouchEvent(MotionEvent ev) {
        try {
            this.mDetector.onTouchEvent(ev);
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException var3) {
            return true;
        }
    }
}
