package com.wt.yc.englishread

import android.graphics.Point
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProActivity
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.adapter.LineAdapter
import com.wt.yc.englishread.view.LineView
import kotlinx.android.synthetic.main.line_add_layout.*

class TestActivity : ProActivity() {

    override fun handler(msg: Message) {

    }

    var recyclerRightW = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.line_add_layout)

        recyclerViewLeft.layoutManager = LinearLayoutManager(this)
        val leftAdapter = LineAdapter(this, arrayListOf<Info>(), 1)
        recyclerViewLeft.adapter = leftAdapter

        recyclerViewRight.layoutManager = LinearLayoutManager(this)

        val rightAdapter = LineAdapter(this, arrayListOf<Info>(), 2)
        recyclerViewRight.adapter = rightAdapter


        leftAdapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("result", "position------$position")

            }

            override fun onLongClick(position: Int) {

            }

        }

        rightAdapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onLongClick(position: Int) {

            }

        }

    }


    var startX = 0f
    var startY = 0f
    var endX = 0f
    var endY = 0f

    var clickPosition: Int? = 0


    /**
     * 点击事件
     */
    fun onTouch(code: Int, itemW: Int, itemH: Int, position: Int, motionEvent: MotionEvent?) {

        if (code == 1) {

            this.clickPosition = position

            startX = itemW.toFloat()
            startY = motionEvent!!.y + itemH

            LineView.startMap[position] = Point(startX.toInt(), startY.toInt())

            Log.i("result", "---------$startX---------$startY")

        } else if (code == 2) {

            LineView.positionMap[clickPosition!!] = position

            recyclerRightW = recyclerViewRight.left

            endX = recyclerRightW.toFloat()
            endY = motionEvent!!.y + itemH

            LineView.endMap[position] = Point(endX.toInt(), endY.toInt())

            Log.i("result", "结束---------$endX---------$endY")

            lineView.drawLine()
        }


    }
}