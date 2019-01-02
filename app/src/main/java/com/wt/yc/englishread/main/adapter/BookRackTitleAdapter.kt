package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.Info
import kotlinx.android.synthetic.main.bool_title_item.view.*

class BookRackTitleAdapter(context: Context, list: ArrayList<Info>) : ProBaseAdapter<Info>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as V
        val info = list[position]
        if (info.click) {
            vh.tvTitle.setBackgroundResource(R.drawable.blue_kuang_yuan)
        } else {
            vh.tvTitle.setBackgroundResource(R.drawable.gray_kuang_solid4)
        }


    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return V(inflater.inflate(R.layout.bool_title_item, parent, false))

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    fun changeClick(position: Int) {
        for (i in list.indices) {
            val info = list[i]
            info.click = i == position
        }
        notifyDataSetChanged()

    }

    inner class V(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tvTitle

    }

}