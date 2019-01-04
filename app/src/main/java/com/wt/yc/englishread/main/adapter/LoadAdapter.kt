package com.wt.yc.englishread.main.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import com.wt.yc.englishread.R
import com.wt.yc.englishread.info.Info
import kotlinx.android.synthetic.main.main_item.view.*
import kotlinx.android.synthetic.main.son_item.view.*


class LoadAdapter(val context: Context, val list: ArrayList<Info>) : BaseExpandableListAdapter() {

    var inflater: LayoutInflater? = null

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun getGroup(p0: Int) = list[p0]

    override fun isChildSelectable(p0: Int, p1: Int) = true

    override fun hasStableIds() = true

    override fun getGroupView(p0: Int, p1: Boolean, view: View?, p3: ViewGroup?): View {
        val contentView: View?
        val vh: VH?
        if (view == null) {
            contentView = inflater!!.inflate(R.layout.main_item, p3, false)

            vh = VH(contentView)

            contentView.tag = vh

        } else {
            contentView = view
            vh = contentView.tag as VH
        }

        showOne(vh, list[p0])

        return contentView!!

    }

    override fun getChildrenCount(p0: Int) = if (list[p0].sonList == null) {
        0
    } else {
        list[p0].sonList!!.size
    }

    override fun getChild(p0: Int, p1: Int) = if (list[p0].sonList == null) {
        ""
    } else {
        list[p0].sonList!![p1].str
    }

    override fun getGroupId(p0: Int) = 0.toLong()

    override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
        val vh: SonVh?
        val contentView: View?

        if (p3 == null) {
            contentView = inflater!!.inflate(R.layout.son_item, p4, false)
            vh = SonVh(contentView)
            contentView.tag = vh

        } else {
            contentView = p3
            vh = contentView.tag as SonVh
        }
        val son = list[p0].sonList!![p1]

        showTwo(vh, son)

        return contentView!!


    }


    private fun showTwo(vh: SonVh, son: Info.Companion.Son) {
        vh.tvSonText.text = son.str
        if (son.click) {
            vh.tvSonText.setTextColor(context.resources.getColor(R.color.blue_login))
        } else {
            vh.tvSonText.setTextColor(context.resources.getColor(R.color.a9899a3))
        }

    }

    override fun getChildId(p0: Int, p1: Int) = 0.toLong()

    override fun getGroupCount() = list.size


    fun showOne(vh: VH, info: Info) {

        vh.tvTitle.text = info.title

        if (info.click) {

            vh.parentLinear.setBackgroundColor(context.resources.getColor(R.color.write))
            vh.imageLinear.setBackgroundResource(R.drawable.blue_bg)
            vh.tvTitle.setTextColor(context.resources.getColor(R.color.blue_login))
            vh.imageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, info.picClick!!))

        } else {

            vh.parentLinear.setBackgroundColor(context.resources.getColor(R.color.main_recycler))
            vh.imageLinear.setBackgroundColor(0)
            vh.tvTitle.setTextColor(context.resources.getColor(R.color.a9899a3))
            vh.imageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, info.pic!!))
        }

    }


    /**
     * 改变显示状态
     */
    fun updateClick(position: Int) {

        for (i in list.indices) {

            val info = list[i]
            info.click = i == position

        }

        notifyDataSetChanged()


    }


    fun twoUpdate(p2: Int, p3: Int) {
        val arr = list[p2].sonList
        for (i in arr!!.indices) {
            val son = arr[i]
            son.click = i == p3

        }

        notifyDataSetChanged()

    }

    /**
     *  将所有的选替换为未选中
     */
    fun updateSonNoClick(p2: Int) {
        val arr = list[p2].sonList

        if (arr != null && arr.size != 0) {
            for (temp in arr) {
                temp.click = false
            }
        }

    }


    inner class VH(view: View) {
        val parentLinear = view.mainItemLinear
        val imageLinear = view.picLinear
        val imageView = view.imageView
        val tvTitle = view.tvTitle

    }

    inner class SonVh(sonView: View) {
        val tvSonText = sonView.tvSonText
    }
}