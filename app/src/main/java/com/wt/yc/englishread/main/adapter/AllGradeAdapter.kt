package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import kotlinx.android.synthetic.main.all_grade_item.view.*

class AllGradeAdapter(context: Context, list: ArrayList<String>) : ProBaseAdapter<String>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        vh.gradeTv1.text = "1234"
        vh.gradeTv2.text = "1234"
        vh.gradeTv3.text = "1234"
        vh.gradeTv4.text = "1234"
        vh.gradeTv5.text = "1234"

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