package com.xin.lv.yang.utils.anim;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;


/**
 * 立体效果页面切换的PageTransformer
 */
public class CubeTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        if (position <= 0) {
            //从右向左滑动为当前View
            //设置旋转中心点；
            ViewHelper.setPivotX(view, view.getMeasuredWidth());
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            //只在Y轴做旋转操作
            ViewHelper.setRotationY(view, 90f * position);
        } else if (position <= 1) {
            //从左向右滑动为当前View
            ViewHelper.setPivotX(view, 0);
            ViewHelper.setPivotY(view, view.getMeasuredHeight() * 0.5f);
            ViewHelper.setRotationY(view, 90f * position);
        }
    }
}
