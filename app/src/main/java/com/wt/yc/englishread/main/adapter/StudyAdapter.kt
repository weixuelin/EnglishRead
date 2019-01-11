package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import kotlinx.android.synthetic.main.study_fragment_layout.view.*
import kotlinx.android.synthetic.main.study_item.view.*

class StudyAdapter(context: Context, list: ArrayList<BookInfo>) : ProBaseAdapter<BookInfo>(context, list) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as VH
        val info = list[position]
        vh.tvStudyUnitName.text = info.unit_name
        vh.tvWordNumber.text = "单词:" + info.unit_num.toString()

        when (info.status) {
            0 -> {
                // 未解锁
                vh.study1.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.study2.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.study3.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.study4.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.imageLock.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.lock))
                vh.tvStudyUnitName.setTextColor(context.resources.getColor(R.color.black1))
            }

            1 -> {
                /// 已学习完
                vh.linearBg.setBackgroundResource(R.drawable.blue_kuang_top_6)

                vh.imageLock.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.choose_y))
                vh.study1.setTextColor(context.resources.getColor(R.color.green))
                vh.study2.setTextColor(context.resources.getColor(R.color.green))
                vh.study3.setTextColor(context.resources.getColor(R.color.green))
                vh.study4.setTextColor(context.resources.getColor(R.color.green))
                vh.tvStudyUnitName.setTextColor(context.resources.getColor(R.color.write))
            }

            2 -> {
                /// 正在学习
                vh.study1.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.study2.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.study3.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.study4.setTextColor(context.resources.getColor(R.color.a8f919b))
                vh.imageLock.setImageBitmap(BitmapFactory.decodeResource(context.resources, R.drawable.unlock))
                vh.tvStudyUnitName.setTextColor(context.resources.getColor(R.color.black1))
            }

        }

        vh.itemView.tvStudy1.setOnClickListener {
            when (info.status) {
                0 -> Toast.makeText(context, "当前单元未解锁", Toast.LENGTH_LONG).show()
                else -> {
                    if (onTvListener != null) {
                        onTvListener!!.onReview(vh.adapterPosition)
                    }
                }
            }

        }

        vh.itemView.tvStudy2.setOnClickListener {
            when (info.status) {
                0 -> Toast.makeText(context, "当前单元未解锁", Toast.LENGTH_LONG).show()
                else -> {
                    if (onTvListener != null) {
                        onTvListener!!.onTest(vh.adapterPosition)
                    }
                }
            }
        }

        vh.itemView.tvStudy3.setOnClickListener {

            when (info.status) {
                0 -> Toast.makeText(context, "当前单元未解锁", Toast.LENGTH_LONG).show()
                else -> {
                    if (onTvListener != null) {
                        onTvListener!!.onDictation(vh.adapterPosition)
                    }
                }
            }

        }

        vh.itemView.tvStudy4.setOnClickListener {
            when (info.status) {
                0 -> Toast.makeText(context, "当前单元未解锁", Toast.LENGTH_LONG).show()
                else -> {
                    if (onTvListener != null) {
                        onTvListener!!.onDictationTraining(vh.adapterPosition)
                    }
                }
            }
        }

    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.study_item, parent, false)
        return VH(view)
    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val linearBg = view.linearBg
        val imageLock = view.imageLock
        val study1 = view.tvStudy1
        val study2 = view.tvStudy2
        val study3 = view.tvStudy3
        val study4 = view.tvStudy4
        val tvStudyUnitName = view.tvStudyUnitName
        val tvWordNumber = view.tvWordNumber
    }


    var onTvListener: OnClickListener? = null

    interface OnClickListener {
        fun onReview(position: Int)
        fun onTest(position: Int)
        fun onDictation(position: Int)
        fun onDictationTraining(position: Int)
    }
}