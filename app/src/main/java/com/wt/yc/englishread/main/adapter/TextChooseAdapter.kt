package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.Info
import kotlinx.android.synthetic.main.text_choose_item.view.*

/**
 * 单元测试选择adapter
 */
class TextChooseAdapter(context: Context, list: ArrayList<Info>) : ProBaseAdapter<Info>(context, list) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info = list[position]

        if (info.click) {

            vh.tvChoose.setBackgroundResource(R.drawable.blue_kong_kuang)
            vh.tvChoose.setTextColor(context.resources.getColor(R.color.black1))

        } else {

            when (info.isErr) {
                // 未作答
                0 -> {
                    vh.tvChoose.setTextColor(context.resources.getColor(R.color.black1))
                    vh.tvChoose.setBackgroundResource(R.drawable.gray_kuang_solid)
                }
                // 错误
                1 -> {
                    vh.tvChoose.setTextColor(context.resources.getColor(R.color.write))
                    vh.tvChoose.setBackgroundResource(R.drawable.green_kuang)
                }
                // 正确
                2 -> {
                    vh.tvChoose.setTextColor(context.resources.getColor(R.color.write))
                    vh.tvChoose.setBackgroundResource(R.drawable.blue_kuang_12)
                }
            }

        }

        vh.tvChoose.text = info.title

    }


    fun updateClick(position: Int) {

        for (i in list.indices) {

            val info = list[i]
            info.click = i == position
        }

        notifyDataSetChanged()


    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.text_choose_item, parent, false)
        return VH(view)
    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvChoose = view.tvChoose

    }
}