package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.Info
import kotlinx.android.synthetic.main.grow_up_item.view.*

class GrowUpRecordAdapter(context: Context, list: ArrayList<Info>) : ProBaseAdapter<Info>(context, list) {

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.grow_up_item, parent, false)
        return VH(view)
    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info = list[position]
        vh.imgPic.setImageBitmap(BitmapFactory.decodeResource(context.resources, info.pic!!))
        vh.tvTitle.text = info.title

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val imgPic = view.imgPic
        val tvTitle = view.tvTitle
        val imgRight = view.imgRight


    }
}