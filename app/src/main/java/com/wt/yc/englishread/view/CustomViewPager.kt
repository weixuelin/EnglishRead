package com.wt.yc.englishread.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class CustomViewPager(context:Context,attr: AttributeSet) : ViewPager(context,attr) {

    /**
     * 是否可以滑动
     */
    private var isCanScroll = false

    fun isCanScroll(): Boolean {
        return isCanScroll
    }

    fun setCanScroll(canScroll: Boolean) {
        isCanScroll = canScroll
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return if (isCanScroll) {
            super.onTouchEvent(arg0)
        } else {
            false
        }
    }

    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item,false)
    }

    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return if (isCanScroll) {
            super.onInterceptTouchEvent(arg0)
        } else {
            false
        }
    }
}