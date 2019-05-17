package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.Info

class LineAdapter(context: Context, list: ArrayList<Info>, val code: Int) : ProBaseAdapter<Info>(context, list) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info = list[position]
        vh.textView.text = info.title

        val click = info.click
        if (click) {
            vh.lineItemLinear.setBackgroundColor(context.resources.getColor(R.color.blue_login))
        } else {
            vh.lineItemLinear.setBackgroundColor(context.resources.getColor(R.color.a00C9B0))
        }

        vh.itemView.setOnTouchListener { view, motionEvent ->

            if (onlineTouch != null) {

                onlineTouch!!.onTouch(code, vh.adapterPosition, motionEvent)

            }

            false
        }
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(inflater.inflate(R.layout.list_item_1, parent, false))

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.lineTextView)
        val lineItemLinear = view.findViewById<LinearLayout>(R.id.lineItemLinear)
    }

    var onlineTouch: OnLineTouch? = null

    interface OnLineTouch {
        fun onTouch(code: Int, adapterPosition: Int, motionEvent: MotionEvent)
    }


}