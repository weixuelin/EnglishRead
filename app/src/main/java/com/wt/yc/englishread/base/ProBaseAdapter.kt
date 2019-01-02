package com.wt.yc.englishread.base

import android.app.Activity
import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.xin.lv.yang.utils.adapter.AdapterCallBack
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


abstract class ProBaseAdapter<T>(var context: Context, var list: ArrayList<T>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val HEAD: Int = 0
    val FOOT: Int = 2
    val NOMAL: Int = 3

    var inflater: LayoutInflater = LayoutInflater.from(context)
    val isHead: Boolean = false
    val isFooter: Boolean = false


    var itemClickListener: ItemClickListener? = null

    override fun getItemCount(): Int = list.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = onCreateView(parent, viewType)

        viewHolder.itemView.setOnClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.onItemClick(viewHolder.adapterPosition)
            }
        }

        viewHolder.itemView.setOnLongClickListener {
            if (itemClickListener != null) {
                itemClickListener!!.onLongClick(viewHolder.adapterPosition)
            }
            true
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            onUpdateHolder(holder, position, payloads)
        }

    }

    override fun getItemViewType(position: Int) = when (position) {
        0 -> {
            if (isHead) {
                HEAD
            } else {
                NOMAL
            }
        }

        itemCount - 1 -> {
            if (isFooter) {
                FOOT
            } else {
                NOMAL
            }
        }

        else -> {
            NOMAL
        }
    }


    fun update(newList: ArrayList<T>, callBack: AdapterCallBack<T>) {
        val result = DiffUtil.calculateDiff(callBack, true)
        result.dispatchUpdatesTo(this)
        setData(newList)
    }

    fun setData(newList: ArrayList<T>) {
        list.clear()
        list.addAll(newList)
    }

    /**
     * 创建viewHolder
     */
    abstract fun onCreateView(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    /**
     * 更新数据信息
     */
    abstract fun onUpdateHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>)


    fun getW(context: Context): Int {
        val manager: WindowManager = (context as Activity).windowManager
        return manager.defaultDisplay.width
    }


    fun updateData(arr: ArrayList<T>) {
        list.addAll(arr)
        notifyDataSetChanged()
    }

    fun updateDataClear(arr: ArrayList<T>) {
        list.clear()
        list.addAll(arr)
        notifyDataSetChanged()
    }


    /**
     * 保留两位小数
     *
     * @param s
     * @return
     */
    fun floatToString(s: Float): String {
        val myFormat = DecimalFormat("#0.00")
        return myFormat.format(s.toDouble())
    }


    private val ONE_DAY_MS = 24 * 60 * 60 * 1000


    /**
     * 获取两个时间戳间隔的天数
     */
    fun getDayByData(start: Long, end: Long): Int {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

        val fromCalendar: Calendar = Calendar.getInstance()
        fromCalendar.time = format.parse(format.format(start * 1000))
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0)
        fromCalendar.set(Calendar.SECOND, 0)
        fromCalendar.set(Calendar.MILLISECOND, 0)

        val toCalendar: Calendar = Calendar.getInstance()
        toCalendar.time = format.parse(format.format(end * 1000))

        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0)
        toCalendar.set(Calendar.SECOND, 0)
        toCalendar.set(Calendar.MILLISECOND, 0)

        return ((toCalendar.time.time - fromCalendar.time.time) / ONE_DAY_MS).toInt()

    }


}