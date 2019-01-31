package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.BookInfo
import kotlinx.android.synthetic.main.finish_item.view.*

class FinishAdapter(context: Context, list: ArrayList<BookInfo>) : ProBaseAdapter<BookInfo>(context, list) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info = list[position]
        vh.tvFinishNum.text = "${info.jd}%"
        vh.progressBar.progress = info.jd
        vh.tvFinish.text = info.unit_name


    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.finish_item, parent, false)
        return VH(view)
    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvFinish = view.tvFinish
        val tvFinishNum = view.tvFinishNum
        val progressBar = view.progressBar

    }
}