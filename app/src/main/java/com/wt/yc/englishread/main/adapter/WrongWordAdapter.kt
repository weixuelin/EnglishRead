package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import kotlinx.android.synthetic.main.wrong_item.view.*

class WrongWordAdapter(context: Context, list: ArrayList<String>) : ProBaseAdapter<String>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val info=list[position]
        val vh=holder as V
        vh.tvTextView1.text=""
        vh.tvTextView2.text=""
        vh.tvTextView3.text=""
        vh.tvTextView4.text=""
        vh.tvTextView5.text=""
        vh.tvTextView6.text=""
        vh.tvTextView7.text=""
        vh.tvTextView8.text=""


    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.wrong_item, parent, false)
        return V(view)

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class V(view: View) : RecyclerView.ViewHolder(view) {
        val tvTextView1=view.tvTextView1
        val tvTextView2=view.tvTextView2
        val tvTextView3=view.tvTextView3
        val tvTextView4=view.tvTextView4
        val tvTextView5=view.tvTextView5
        val tvTextView6=view.tvTextView6
        val tvTextView7=view.tvTextView7
        val tvTextView8=view.tvTextView8


    }
}