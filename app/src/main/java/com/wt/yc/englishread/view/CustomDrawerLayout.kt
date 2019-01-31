package com.wt.yc.englishread.view

import android.content.Context
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet

class CustomDrawerLayout(context:Context,attr: AttributeSet) :DrawerLayout(context,attr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val width = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY)
        val height= MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY)

        super.onMeasure(width, height)


    }
}