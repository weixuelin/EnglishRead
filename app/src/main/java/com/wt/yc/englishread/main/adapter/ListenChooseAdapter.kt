package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import kotlinx.android.synthetic.main.listen_item.view.*

class ListenChooseAdapter(context: Context, list: ArrayList<String>, val code: Int) : ProBaseAdapter<String>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        if (position == 0) {
            vh.linearLayout1.visibility = View.VISIBLE
        } else {
            vh.linearLayout1.visibility = View.GONE
        }
        when (code) {
            1 -> vh.imageTi.visibility = View.VISIBLE
            2, 3 -> vh.imageTi.visibility = View.GONE
        }


    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.listen_item, parent, false)
        return VH(view)
    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val linearLayout1 = view.linearLayout1
        val imageTi = view.imageTi
    }
}