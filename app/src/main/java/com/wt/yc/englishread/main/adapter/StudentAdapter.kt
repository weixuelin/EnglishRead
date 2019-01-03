package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProBaseAdapter
import com.wt.yc.englishread.info.MainInfo
import com.xin.lv.yang.utils.utils.ImageUtil
import kotlinx.android.synthetic.main.student_item.view.*

class StudentAdapter(context: Context, list: ArrayList<MainInfo>) : ProBaseAdapter<MainInfo>(context, list) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val info = list[position]
        val vh = holder as VH
        vh.tvStudentTitle.text = info.title
        vh.tvStudentContent.text = info.descriptions
        val w = ((getW(context) - context.resources.getDimension(R.dimen.dp_50)) / 4).toInt()
        vh.studentImageView.layoutParams = LinearLayout.LayoutParams(w, w * 2 / 3)

        ImageUtil.getInstance().loadRoundCircleImage(context, vh.studentImageView, Config.IP + info.icon, 0, 4)

    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = inflater.inflate(R.layout.student_item, parent, false)
        return VH(view)

    }

    override fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {

    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val studentImageView = view.studentImageView
        val tvStudentTitle = view.tvStudentTitle
        val tvStudentContent = view.tvStudentContent

    }
}