package com.xin.lv.yang.utils.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 页面淡入淡出的效果
 */

public class ScaleInOutTransformer implements ViewPager.PageTransformer {

    private static float MIN_SCALE = 0.5f;
    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        //当视图往左
        if (position < -1) {
            ViewHelper.setAlpha(view, 0);
            ViewHelper.setTranslationX(view, 0);
        } else if (position <= 0) {
            //使用默认的幻灯片切换时，移动到左边
            ViewHelper.setAlpha(view, 1);
            ViewHelper.setTranslationX(view, 0);
            ViewHelper.setScaleX(view, 1);
            ViewHelper.setScaleY(view, 1);
        } else if (position <= 1) {
            //滑出的页面
            ViewHelper.setAlpha(view, 1-position);
            //抵消默认的幻灯片过渡
            ViewHelper.setTranslationX(view, pageWidth * -position);
            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
                    * (1 - Math.abs(position));
            ViewHelper.setScaleX(view, scaleFactor);
            ViewHelper.setScaleY(view, scaleFactor);
        } else {
            //本页是屏幕右
            ViewHelper.setAlpha(view, 0);
            ViewHelper.setScaleX(view, 1);
            ViewHelper.setScaleY(view, 1);
        }
    }
}
