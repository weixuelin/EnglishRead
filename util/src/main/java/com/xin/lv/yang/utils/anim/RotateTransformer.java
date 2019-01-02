package com.xin.lv.yang.utils.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * viewPager 旋转效果
 */

public class RotateTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        if (position < -1) {
        } else if (position <= 0) {
            ViewHelper.setScaleX(view, 1 + position);
            ViewHelper.setScaleY(view, 1 + position);
            ViewHelper.setRotation(view, 360 * position);
        } else if (position <= 1) {
            ViewHelper.setScaleX(view, 1 - position);
            ViewHelper.setScaleY(view, 1 - position);
            ViewHelper.setRotation(view, 360 * position);
        } else {
        }
    }
}
