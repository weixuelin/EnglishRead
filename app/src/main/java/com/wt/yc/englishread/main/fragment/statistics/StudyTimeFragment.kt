package com.wt.yc.englishread.main.fragment.statistics

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.MainInfo
import com.wt.yc.englishread.view.CustomDateChoose
import com.xin.lv.yang.utils.utils.DataUtil
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.study_time_layout.*
import org.json.JSONObject

/**
 * 学习时间统计
 */
class StudyTimeFragment : ProV4Fragment() {
    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.GET_STUDY_TIME_CODE->{
                removeLoadDialog()
                val json=JSONObject(str)
                val code=json.optInt(Config.CODE)

                if(code==Config.SUCCESS){
                    val data=json.optString(Config.DATA)
                    val arr=gson!!.fromJson<ArrayList<MainInfo>>(data,object :TypeToken<ArrayList<MainInfo>>(){}.type)

                    bindData(studyPieChart, 1,arr)

                }
            }

        }
    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.study_time_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initPieBar(studyPieChart)
        initPieBar(rememberPieChart)

        initClick()

        getTime()

    }

    private fun getTime() {
        val json = JSONObject()
        json.put("token", token)
        json.put("uid",uid)
        HttpUtils.getInstance().postJson(Config.GET_STUDY_TIME_URL, json.toString(), Config.GET_STUDY_TIME_CODE, handler!!)
        showLoadDialog(activity!!, "获取中")
    }

    private fun initClick() {
        tvStartTime.setOnClickListener {
            showDataChoose(1)

        }

        tvEndTime.setOnClickListener {
            showDataChoose(2)
        }
    }

    var startLongTime: Long = 0L
    var endLongTime: Long = 0L

    private fun showDataChoose(code: Int) {
        val builder = AlertDialog.Builder(activity)
        val date = CustomDateChoose(activity!!)
        builder.setView(date)

        builder.setPositiveButton("确定") { dialog, i ->
            val time = date.getDate()
            when (code) {
                1 -> {
                    tvStartTime.text = time
                    startLongTime = DataUtil.stringTimeToLong(time)
                }

                2 -> {
                    tvEndTime.text = time
                    endLongTime = DataUtil.stringTimeToLong(time)
                }
            }

            change()

            dialog.dismiss()

        }.setNegativeButton("取消") { _, _ ->

        }

        builder.show()

    }

    /**
     * 改变查询
     */
    private fun change() {

    }

    private fun initPieBar(cxPieChart: PieChart) {
        // 设置饼图是否接收点击事件，默认为true
        cxPieChart.setTouchEnabled(true)
        cxPieChart.setDrawSliceText(false)
        //设置饼图是否使用百分比
        cxPieChart.setUsePercentValues(true)
        //设置饼图右下角的文字描述
        val des = Description()
        des.text = ""
        cxPieChart.description = des
        //是否显示圆盘中间文字，默认显示
        cxPieChart.setDrawCenterText(true)
        //设置圆盘中间文字的颜色
        cxPieChart.setCenterTextColor(Color.RED)
        //设置圆盘中间文字的字体
        cxPieChart.setCenterTextTypeface(Typeface.DEFAULT)
        //设置中间圆盘的颜色
        cxPieChart.setHoleColor(Color.TRANSPARENT)
        //设置中间圆盘的半径,值为所占饼图的百分比
        cxPieChart.holeRadius = 40f
        //是否显示饼图中间空白区域，默认显示
        cxPieChart.isDrawHoleEnabled = true
        //设置圆盘是否转动，默认转动
        cxPieChart.isRotationEnabled = true
        //设置初始旋转角度
        cxPieChart.rotationAngle = 0f

    }


    /**
     *  显示饼状图数据
     */
    private fun bindData(cxPieChart: PieChart, code: Int,arr:ArrayList<MainInfo>) {
        val valueList: ArrayList<PieEntry> = arrayListOf()

        when (code) {
            1 -> {
                val info0=arr[0]
                val info2=arr[1]

                val all=info0.value.toFloat()+info2.value.toFloat()

                valueList.add(PieEntry(info0.value.toFloat()/all, info0.name))
                valueList.add(PieEntry(info2.value.toFloat()/all, info2.name))
            }

            2 -> {
                valueList.add(PieEntry(2f, "记忆时间"))
                valueList.add(PieEntry(3f, "回忆时间"))
                valueList.add(PieEntry(5f, "拼写时间"))
            }
        }

        // 显示在比例图上
        val dataSet = PieDataSet(valueList, "")
        //设置个饼状图之间的距离
        dataSet.sliceSpace = 3f
        // 部分区域被选中时多出的长度
        dataSet.selectionShift = 5f

        dataSet.setDrawValues(true)

        /// 设置饼图各个区域颜色
        val colors: ArrayList<Int> = ArrayList<Int>()

        when (code) {
            1 -> {
                colors.add(resources.getColor(R.color.blue_login))
                colors.add(resources.getColor(R.color.green))
            }

            2 -> {
                colors.add(resources.getColor(R.color.aEFAC50))
                colors.add(resources.getColor(R.color.a2ec7c9))
                colors.add(resources.getColor(R.color.aB6A2DE))
            }
        }

        dataSet.colors = colors
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 14f
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD

        /// 是否在图上显示数值
        dataSet.setDrawValues(true)

//        dataSet.valueLinePart1Length = 0.1f
////      当值位置为外边线时，表示线的后半段长度。
//        dataSet.valueLinePart2Length = 0.2f

//      当ValuePosits为OUTSIDE_SLICE时，指示偏移为切片大小的百分比
        dataSet.valueLinePart1OffsetPercentage = 80f
        // 当值位置为外边线时，表示线的颜色。
        dataSet.valueLineColor = resources.getColor(R.color.line)
//        设置Y值的位置是在圆内还是圆外
        dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
//        设置Y轴描述线和填充区域的颜色一致
        dataSet.setAutomaticallyDisableSliceSpacing(false)
//        设置每条之前的间隙
        dataSet.sliceSpace = 0f

        val data = PieData(dataSet)

        // 设置以百分比显示
        data.setValueFormatter(PercentFormatter())
        // 区域文字的大小
        data.setValueTextSize(14f)
        // 设置区域文字的颜色
        data.setValueTextColor(Color.BLACK)
        // 设置区域文字的字体
        data.setValueTypeface(Typeface.DEFAULT)
        cxPieChart.data = data
        cxPieChart.highlightValues(null)
        cxPieChart.invalidate()
    }
}