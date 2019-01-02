package com.wt.yc.englishread.main.fragment.statistics

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.view.CustomDateChoose
import kotlinx.android.synthetic.main.group_up_fragment.*

/**
 * 我的成长经历
 */
class MyGroupUpFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

        }

    }

    var code: Int = 1

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.group_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        when (code) {
            1 -> {
                chooseTimeLinear.visibility = View.VISIBLE
                tvGroupTitle.visibility = View.GONE
            }

            2 -> {
                chooseTimeLinear.visibility = View.GONE
                tvGroupTitle.visibility = View.VISIBLE
            }
        }

        initChart()

        initClick()
    }


    private fun initClick() {
        tvChooseStartTime.setOnClickListener {
            showDataChoose(1)

        }

        tvChooseEndTime.setOnClickListener {
            showDataChoose(2)

        }
    }


    private fun showDataChoose(code: Int) {
        val builder = AlertDialog.Builder(activity)
        val date = CustomDateChoose(activity!!)
        builder.setView(date)

        builder.setPositiveButton("确定") { dialog, i ->
            when (code) {
                1 -> {
                    tvChooseStartTime.text = date.getDate()
                }
                2 -> {
                    tvChooseEndTime.text = date.getDate()
                }

            }
            dialog.dismiss()

        }.setNegativeButton("取消") { _, _ ->

        }

        builder.show()

    }

    val textArr: ArrayList<String> = arrayListOf("", "初次记忆", "第一周期", "第二周期", "第三周期", "第四周期", "第五周期", "第六周期", "第七周期")

    private fun initChart() {
        // 是否在折线图上添加边框
        groupUpChart.setDrawBorders(false)
        // 曲线描述 -标题
        val des = Description()
        des.text = ""
        groupUpChart.description = des
        // 是否显示表格颜色
        groupUpChart.setDrawGridBackground(false)
        // 禁止绘制图表边框的线
        groupUpChart.setDrawBorders(false)

        val markerView = MarkerView(activity!!, R.layout.group_up_pop_text)
        markerView.chartView = groupUpChart
        groupUpChart.markerView = markerView

        // 设置是否启动触摸响应
        groupUpChart.setTouchEnabled(true)
        // 是否可以拖拽
        groupUpChart.isDragEnabled = true
        // 是否可以缩放
        groupUpChart.setScaleEnabled(true)
        // 如果禁用，可以在x和y轴上分别进行缩放
        groupUpChart.setPinchZoom(false)

        groupUpChart.axisRight.isEnabled = false

        val yyy = groupUpChart.axisLeft
        yyy.setDrawZeroLine(false)
        yyy.setDrawGridLines(false)
        val xAxis = groupUpChart.xAxis
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

            val str = if (num <= 0 || num >= textArr.size) {
                ""
            } else {
                textArr[num]
            }

            axis.textSize = 10f

            str

        }

        // 执行的动画,x轴（动画持续时间）
        groupUpChart.animateX(2500)

        groupUpChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {

            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                markerView.refreshContent(e, h)
            }

        })

        setDate()
    }

    val colorList = arrayListOf(R.color.zi, R.color.blue1,
            R.color.aF0AD4E, R.color.a4EBBF3,
            R.color.a00C9B0, R.color.aF46A80)

    private fun setDate() {
        val y = arrayListOf<Entry>()
        val y2 = arrayListOf<Entry>()
        val y3 = arrayListOf<Entry>()
        val y4 = arrayListOf<Entry>()
        val y5 = arrayListOf<Entry>()
        val y6 = arrayListOf<Entry>()

        for (i in textArr.indices) {
            when (i) {
                0, 1 -> {
                    // 图例对象
                    val mLegend: Legend = groupUpChart.legend
                    // mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
                    // 图例样式 (CIRCLE圆形；LINE线性；SQUARE是方块）
                    mLegend.form = Legend.LegendForm.CIRCLE
                }

                else -> {
                    // 图例对象
                    val mLegend: Legend = groupUpChart.legend
                    // mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
                    // 图例样式 (CIRCLE圆形；LINE线性；SQUARE是方块）
                    mLegend.form = Legend.LegendForm.NONE
                }
            }

            if (i == 0) {
                y.add(Entry(i.toFloat(), 0f, ""))
                y2.add(Entry(i.toFloat(), 0f, ""))
                y3.add(Entry(i.toFloat(), 0f, ""))
                y4.add(Entry(i.toFloat(), 0f, ""))
                y5.add(Entry(i.toFloat(), 0f, ""))
                y6.add(Entry(i.toFloat(), 0f, ""))

            } else {
                y.add(Entry(i.toFloat(), (Math.random() * 50).toFloat(), ""))
                y2.add(Entry(i.toFloat(), (Math.random() * 50).toFloat(), ""))
                y3.add(Entry(i.toFloat(), (Math.random() * 50).toFloat(), ""))
                y4.add(Entry(i.toFloat(), (Math.random() * 50).toFloat(), ""))
                y5.add(Entry(i.toFloat(), (Math.random() * 50).toFloat(), ""))
                y6.add(Entry(i.toFloat(), (Math.random() * 50).toFloat(), ""))
            }


        }

        val lineDataSet = arrayListOf<ILineDataSet>()

        lineDataSet.add(setDataSer(y, 0))
        lineDataSet.add(setDataSer(y2, 1))
        lineDataSet.add(setDataSer(y3, 2))
        lineDataSet.add(setDataSer(y4, 3))
        lineDataSet.add(setDataSer(y5, 4))
        lineDataSet.add(setDataSer(y6, 5))

        val data = LineData(lineDataSet)

        groupUpChart.data = data
    }

    private fun setDataSer(y: ArrayList<Entry>, indexCode: Int): LineDataSet {
        val set1 = LineDataSet(y, "")
        set1.setCircleColor(resources.getColor(R.color.red))
        set1.setDrawCircles(false)
        set1.color = resources.getColor(colorList[indexCode])
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.valueTextColor = resources.getColor(colorList[indexCode])
        return set1

    }
}