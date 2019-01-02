package com.wt.yc.englishread.view

import android.content.Context
import android.widget.DatePicker
import java.util.*

class CustomDateChoose(context: Context) : DatePicker(context), DatePicker.OnDateChangedListener {
    var dateStr: String = ""

    override fun onDateChanged(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        dateStr = "${p1}年${p2 + 1}月${p3}日"

    }


    fun getDate(): String {
        return dateStr
    }

    init {
        val calendar: Calendar = Calendar.getInstance()

        init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this)

        dateStr = "${calendar.get(Calendar.YEAR)}年${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DAY_OF_MONTH)}日"
    }

}