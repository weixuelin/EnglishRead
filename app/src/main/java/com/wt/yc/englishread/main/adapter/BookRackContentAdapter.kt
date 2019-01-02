package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter

class BookRackContentAdapter(context: Context, list: ArrayList<String>) : ProBaseAdapter<String>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(inflater.inflate(R.layout.bool_content_item, parent, false))

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }


    inner class VH(view: View) : RecyclerView.ViewHolder(view) {

    }
}