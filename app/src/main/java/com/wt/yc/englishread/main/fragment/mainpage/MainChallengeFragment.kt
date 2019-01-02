package com.wt.yc.englishread.main.fragment.mainpage

import android.app.Dialog
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
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import kotlinx.android.synthetic.main.main_challenge_fragment.*
import kotlinx.android.synthetic.main.study_head.*
import kotlinx.android.synthetic.main.ti_dialog.view.*

/**
 * 首页挑战——专题挑战
 */
class MainChallengeFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

        }

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_challenge_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTopTitle.text = "专题挑战"
        tvTopName.text = ""

        initClick()

        roundImageView1.setValue(50)
        roundImageView2.setValue(60)
        roundImageView3.setValue(30)


    }

    private fun initClick() {
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        llLayout1.setOnClickListener {
            showTiDialog()
        }
        llLayout2.setOnClickListener {
            showTiDialog()
        }
        llLayout3.setOnClickListener {
            showTiDialog()
        }
        llLayout4.setOnClickListener {
            showTiDialog()
        }
        llLayout5.setOnClickListener {
            showTiDialog()
        }
        llLayout6.setOnClickListener {
            showTiDialog()
        }
        llLayout7.setOnClickListener {
            showTiDialog()
        }

    }


    var showDialog: Dialog? = null


    fun showTiDialog() {
        val view = layoutInflater.inflate(R.layout.ti_dialog, null)
        showDialog = Dialog(activity, R.style.style)

        showDialog!!.setContentView(view)
        showDialog!!.show()
        setAlpha(activity!!, 0.6f)

        showDialog!!.setOnDismissListener {
            setAlpha(activity!!, 1f)
        }

        view.imgClose.setOnClickListener {
            showDialog!!.dismiss()
            setAlpha(activity!!, 1f)
        }

        view.button.setOnClickListener {
            showDialog!!.dismiss()
            setAlpha(activity!!, 1f)
        }
    }


    private fun initChart(pieChart: PieChart) {
        // 设置饼图是否接收点击事件，默认为true
        pieChart.setTouchEnabled(true)
        //设置饼图是否使用百分比
        pieChart.setUsePercentValues(true)
        //设置饼图右下角的文字描述
        val des = Description()
        des.text = ""
        pieChart.description = des
        //是否显示圆盘中间文字，默认显示
        pieChart.setDrawCenterText(true)
        //设置圆盘中间文字的颜色
        pieChart.setCenterTextColor(Color.BLACK)
        //设置圆盘中间文字的字体
        pieChart.setCenterTextTypeface(Typeface.DEFAULT)
        //设置中间圆盘的颜色
        pieChart.setHoleColor(Color.WHITE)
        //设置中间圆盘的半径,值为所占饼图的百分比
        pieChart.holeRadius = 96f
        //是否显示饼图中间空白区域，默认显示
        pieChart.isDrawHoleEnabled = true
        //设置圆盘是否转动，默认转动
        pieChart.isRotationEnabled = false
        //设置初始旋转角度
        pieChart.rotationAngle = 0f

    }

    private fun setDate(pieChart: PieChart?) {
        val valueList: ArrayList<PieEntry> = arrayListOf()
        valueList.add(PieEntry(8f, ""))
        // 显示在比例图上
        val dataSet = PieDataSet(valueList, "")
        dataSet.setDrawValues(true)
        /// 设置饼图各个区域颜色
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.red))
        dataSet.colors = colors
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f
        dataSet.valueTypeface = Typeface.DEFAULT
        /// 是否在图上显示数值
        dataSet.setDrawValues(true)
        dataSet.valueLinePart1Length = 0.1f
//      当值位置为外边线时，表示线的后半段长度。
        dataSet.valueLinePart2Length = 0.2f
//      当ValuePosits为OutsiDice时，指示偏移为切片大小的百分比
        dataSet.valueLinePart1OffsetPercentage = 80f
        // 当值位置为外边线时，表示线的颜色。
        dataSet.valueLineColor = Color.parseColor("#a1a1a1")
//        设置Y值的位置是在圆内还是圆外
        dataSet.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
//        设置Y轴描述线和填充区域的颜色一致
        dataSet.setAutomaticallyDisableSliceSpacing(false)
//        设置每条之前的间隙
        dataSet.sliceSpace = 0f
        val data = PieData(dataSet)
        //设置以百分比显示
        data.setValueFormatter(PercentFormatter())
        //区域文字的大小
        data.setValueTextSize(11f)
        //设置区域文字的颜色
        data.setValueTextColor(Color.BLACK)
        //设置区域文字的字体
        data.setValueTypeface(Typeface.DEFAULT)
        pieChart!!.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()

    }

}