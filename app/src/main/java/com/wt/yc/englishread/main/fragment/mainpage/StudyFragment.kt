package com.wt.yc.englishread.main.fragment.mainpage

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.FinishAdapter
import com.wt.yc.englishread.main.adapter.StudyAdapter
import com.wt.yc.englishread.main.adapter.TextVoiceAdapter
import kotlinx.android.synthetic.main.pop_text.view.*
import kotlinx.android.synthetic.main.study_fragment_layout.*

class StudyFragment : ProV4Fragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.study_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()

        initMsAdapter()
        initFinishAdapter()

        initStudyChart(studyChart)

        initClick()

        get()
    }

    fun get(){

    }

    private fun initClick() {
        tvTestBook.setOnClickListener {
            (activity as MainPageActivity).toWhere(Constant.STUDY_TEST, null)
        }
    }

    /**
     * 初始化完成adapter
     */
    private fun initFinishAdapter() {
        finishNumRecyclerView.isNestedScrollingEnabled = false
        finishNumRecyclerView.layoutManager = LinearLayoutManager(activity)
        finishNumRecyclerView.adapter = FinishAdapter(activity!!, textArr)
    }


    val textArr = arrayListOf("", "一单元", "二单元", "三单元", "四单元", "五单元",
            "六单元", "七单元", "八单元", "九单元", "十单元")

    /**
     * 初始化曲线图
     */
    private fun initStudyChart(studyLineChart: LineChart) {
        // 是否在折线图上添加边框
        studyLineChart.setDrawBorders(false)
        // 曲线描述 -标题
        val des = Description()
        des.text = ""
        studyLineChart.description = des
        // 是否显示表格颜色
        studyLineChart.setDrawGridBackground(false)
        // 禁止绘制图表边框的线
        studyLineChart.setDrawBorders(false)

        val markerView = MarkerView(activity!!, R.layout.pop_text)
        markerView.chartView = studyLineChart
        studyLineChart.markerView = markerView

        // 设置是否启动触摸响应
        studyLineChart.setTouchEnabled(true)
        // 是否可以拖拽
        studyLineChart.isDragEnabled = true
        // 是否可以缩放
        studyLineChart.setScaleEnabled(true)
        // 如果禁用，可以在x和y轴上分别进行缩放
        studyLineChart.setPinchZoom(false)

        // 图例对象
        val mLegend: Legend = studyLineChart.legend
        // mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        // 图例样式 (CIRCLE圆形；LINE线性；SQUARE是方块）
        mLegend.form = Legend.LegendForm.NONE

        studyLineChart.axisRight.isEnabled = false

        val yyy = studyLineChart.axisLeft
        yyy.setDrawZeroLine(false)
        yyy.setDrawGridLines(false)
        val xAxis = studyLineChart.xAxis
        // 显示X轴上的刻度值
        xAxis.setDrawLabels(true)
        // 设置X轴的数据显示在报表的下方
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // 轴线
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        // 设置不从X轴发出纵向直线
        xAxis.setDrawGridLines(false)

        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->

            val num = value.toInt()
            log("--------$num")

            val str = if (num < 0 || num >= textArr.size) {
                ""
            } else {
                textArr[num]
            }

            axis.textSize = 10f

            str

        }

        // 执行的动画,x轴（动画持续时间）
        studyLineChart.animateX(2500)

        setTextData(studyLineChart)

        studyLineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                log("hhhhhhh---------" + h.toString())
                showStudyView(markerView.rootView, h!!)
                markerView.refreshContent(e, h)
            }

        })
    }

    private fun showStudyView(studyView: View, h: Highlight) {
        studyView.tvText1.text = "熟悉词:${h.y.toInt()}"
        studyView.tvText2.text = "夹生词:${h.y.toInt()}"
        studyView.tvText3.text = "陌生词:${h.y.toInt()}"

    }


    /**
     * 显示线性数据
     */
    private fun setTextData(chart: LineChart) {
        val y = arrayListOf<Entry>()

        for (i in 0 until textArr.size) {
            if (i == 0) {
                y.add(Entry(i.toFloat(), 0f, "测试"))
            } else {
                val num1 = Math.random() * 50
                y.add(Entry(i.toFloat(), num1.toFloat(), "测试"))
            }


        }

        val lineDataSet = arrayListOf<ILineDataSet>()
        val set1 = LineDataSet(y, "")
        set1.setCircleColor(resources.getColor(R.color.red))
        set1.setDrawCircles(false)
        set1.color = resources.getColor(R.color.red)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.valueTextColor = resources.getColor(R.color.red)

        lineDataSet.add(set1)

        val data = LineData(lineDataSet)

        chart.data = data

    }

    /**
     * 初始化夹生词adapter
     */
    private fun initMsAdapter() {
        msRecyclerView.isNestedScrollingEnabled = false
        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        msRecyclerView.layoutManager = manager

        val adapter = TextVoiceAdapter(activity!!, arrayListOf("", "", "", ""))
        msRecyclerView.adapter = adapter
        adapter.onVoiceListener = object : TextVoiceAdapter.OnVoiceListener {
            override fun onVoice(position: Int) {
                playWordVoice("voice")

            }

        }

    }


    val list = arrayListOf<Info>()
    var contentArr = arrayListOf("", "", "", "", "", "", "")

    /**
     * 初始单元adapter
     */
    private fun initAdapter() {
        list.clear()

        for (i in 0 until contentArr.size) {
            val info = Info()
            when (i) {
                0, 1, 3, 5 -> {
                    info.click = true
                    info.isLock = 1
                }
                else -> {
                    info.isLock = 2
                    info.click = false
                }
            }

            list.add(info)

        }

        studyRecyclerView.isNestedScrollingEnabled = false
        studyRecyclerView.layoutManager = GridLayoutManager(activity, 4)
        val adapter = StudyAdapter(activity!!, list)
        studyRecyclerView.adapter = adapter

        adapter.onTvListener = object : StudyAdapter.OnClickListener {
            override fun onReview(position: Int) {
                val info = Info()
                info.title = "第${position + 1}单元"
                (activity as MainPageActivity).toWhere(Constant.STUDY_REVIEW, info)

            }

            override fun onTest(position: Int) {
                (activity as MainPageActivity).toWhere(Constant.STUDY_TEST, Info())
            }

            override fun onDictation(position: Int) {
                (activity as MainPageActivity).toWhere(Constant.LISTEN_WRITE_CODE, Info())

            }

            override fun onDictationTraining(position: Int) {
                (activity as MainPageActivity).toWhere(Constant.LISTEN_READ_CODE, Info())
            }

        }

    }

    override fun handler(msg: Message) {

    }
}