package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import kotlinx.android.synthetic.main.word_item.view.*

class WordAdapter(context: Context, list: ArrayList<String>) : ProBaseAdapter<String>(context, list) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info = list[position]

        vh.imageWordVoice.setOnClickListener {
            if (onVoiceListener != null) {
                onVoiceListener!!.onVoice(vh.adapterPosition)
            }
        }
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.word_item, parent, false)
        return VH(view)

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {

        val imageWordVoice = view.imageWordVoice

    }


    var onVoiceListener: OnVoiceListener? = null

    interface OnVoiceListener {
        fun onVoice(position: Int)
    }
}