package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import kotlinx.android.synthetic.main.test_item.view.*

class TestListAdapter(context: Context, list: ArrayList<String>) : ProBaseAdapter<String>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.test_item, parent, false)
        return VH(view)
    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTestTime = view.tvTestTime
        val tvTestName = view.tvTestName
        val tvTestDate = view.tvTestDate
        val tvTextPic = view.tvTextPic
        val tvTestScore = view.tvTestScore
        val tvTestFen = view.tvTestFen
        val ivRight = view.ivRight

    }
}