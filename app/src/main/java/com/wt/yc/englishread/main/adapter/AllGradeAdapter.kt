package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.BookInfo
import kotlinx.android.synthetic.main.all_grade_item.view.*

class AllGradeAdapter(context: Context, list: ArrayList<BookInfo>) : ProBaseAdapter<BookInfo>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info=list[position]

        vh.gradeTv1.text = info.type
        vh.gradeTv2.text = info.test_cj
        vh.gradeTv3.text = info.time
        vh.gradeTv4.text = info.test_time
        vh.gradeTv5.text = info.type

    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.all_grade_item, parent, false)
        return VH(view)

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val gradeTv1 = view.gradeTv1
        val gradeTv2 = view.gradeTv2
        val gradeTv3 = view.gradeTv3
        val gradeTv4 = view.gradeTv4
        val gradeTv5 = view.gradeTv5

    }
}