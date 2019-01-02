package com.xin.lv.yang.utils.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 自定义RelativeLayout
 */
public class FractionTranslateRelativeLayout extends RelativeLayout {

    private int screenWidth;
    private float fractionX;
    private OnLayoutTranslateListener onLayoutTranslateListener;

    public FractionTranslateRelativeLayout(Context context) {
        super(context);
    }

    public FractionTranslateRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FractionTranslateRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH){

        // Assign the actual screen width to our class variable.
        screenWidth = w;

        super.onSizeChanged(w, h, oldW, oldH);
    }

    public float getFractionX(){
        return fractionX;
    }

    public void setFractionX(float xFraction){
        this.fractionX = xFraction;

        // When we modify the xFraction, we want to adjust the x translation
        // accordingly.  Here, the scale is that if xFraction is -1, then
        // the layout is off screen to the left, if xFraction is 0, then the
        // layout is exactly on the screen, and if xFraction is 1, then the
        // layout is completely offscreen to the right.
        setX((screenWidth > 0) ? (xFraction * screenWidth) : 0);

        if (xFraction == 1 || xFraction == -1) {
            setAlpha(0);
        } else if (xFraction < 1 /* enter */|| xFraction > -1 /* exit */) {
            if (getAlpha() != 1) {
                setAlpha(1);
            }
        }

        if (onLayoutTranslateListener != null) {
            onLayoutTranslateListener.onLayoutTranslate(this, xFraction);
        }
    }

    public void setOnLayoutTranslateListener(OnLayoutTranslateListener onLayoutTranslateListener) {
        this.onLayoutTranslateListener = onLayoutTranslateListener;
    }

    public interface OnLayoutTranslateListener {
        void onLayoutTranslate(FractionTranslateRelativeLayout view, float xFraction);
    }
}
