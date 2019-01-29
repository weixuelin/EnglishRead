package com.wt.yc.englishread.main.fragment.mainpage

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
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
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.FinishAdapter
import com.wt.yc.englishread.main.adapter.StudyAdapter
import com.wt.yc.englishread.main.adapter.TextVoiceAdapter
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.pop_text.view.*
import kotlinx.android.synthetic.main.study_fragment_layout.*
import org.json.JSONObject

class StudyFragment : ProV4Fragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.study_fragment_layout, container, false)
    }

    var bookInfo: BookInfo? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (bookInfo != null) {
            tvStudyBookName.text = bookInfo!!.book_name
        }

        initAdapter()
        initMsAdapter()
        initFinishAdapter()

        initClick()

        get()
    }

    fun get() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_STUDY_URL, json.toString(), Config.GET_STUDY_CODE, handler)

    }

    private fun initClick() {

        testLinearLayout.setOnClickListener {

            Log.i("result", "book------$bookInfo")

            bookInfo!!.code = 2

            (activity as MainPageActivity).toWhere(Constant.STUDY_TEST, bookInfo)
        }
    }

    var finishAdapter: FinishAdapter? = null
    val finishList = arrayListOf<BookInfo>()

    /**
     * 初始化完成adapter
     */
    private fun initFinishAdapter() {
        finishNumRecyclerView.isNestedScrollingEnabled = false
        finishNumRecyclerView.layoutManager = LinearLayoutManager(activity)
        finishAdapter = FinishAdapter(activity!!, finishList)
        finishNumRecyclerView.adapter = finishAdapter
    }


    var textArr = arrayListOf<String>("")

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
        mLegend.form = Legend.LegendForm.LINE

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
        xAxis.setDrawAxisLine(false)  // 是否绘制曲线
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
        studyView.tvText1.text = "熟悉词:${text1Arr!![h.x.toInt() - 1]}"
        studyView.tvText2.text = "夹生词:${text11Arr!![h.x.toInt() - 1]}"
        studyView.tvText3.text = "陌生词:${text113Arr!![h.x.toInt() - 1]}"

    }


    var text1Arr: ArrayList<String>? = null
    var text11Arr: ArrayList<String>? = null
    var text113Arr: ArrayList<String>? = null

    /**
     * 显示线性数据
     */
    private fun setTextData(chart: LineChart, text1: String, text2: String, text3: String) {
        val y = arrayListOf<Entry>()
        y.add(Entry(0f, 0f, ""))
        text1Arr = gson!!.fromJson<ArrayList<String>>(text1, object : TypeToken<ArrayList<String>>() {}.type)

        for (i in text1Arr!!.indices) {
            y.add(Entry((i + 1).toFloat(), text1Arr!![i].toFloat(), ""))
        }

        val lineDataSet = arrayListOf<ILineDataSet>()
        val set1 = LineDataSet(y, "熟悉词")
        set1.setCircleColor(resources.getColor(R.color.red))
        set1.setDrawCircles(false)
        set1.color = resources.getColor(R.color.red)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.valueTextColor = resources.getColor(R.color.red)
        lineDataSet.add(set1)

        val y11 = arrayListOf<Entry>()
        y11.add(Entry(0f, 0f, ""))
        text11Arr = gson!!.fromJson<ArrayList<String>>(text2, object : TypeToken<ArrayList<String>>() {}.type)

        for (i in text11Arr!!.indices) {
            y11.add(Entry((i + 1).toFloat(), text11Arr!![i].toFloat(), ""))
        }


        val set2 = LineDataSet(y11, "夹生词")
        set2.setCircleColor(resources.getColor(R.color.green))
        set2.setDrawCircles(false)
        set2.color = resources.getColor(R.color.green)
        set2.mode = LineDataSet.Mode.CUBIC_BEZIER
        set2.valueTextColor = resources.getColor(R.color.green)
        lineDataSet.add(set2)

        val y113 = arrayListOf<Entry>()
        y113.add(Entry(0f, 0f, ""))
        text113Arr = gson!!.fromJson<ArrayList<String>>(text3, object : TypeToken<ArrayList<String>>() {}.type)

        for (i in text113Arr!!.indices) {
            y113.add(Entry((i + 1).toFloat(), text113Arr!![i].toFloat(), ""))
        }

        val set3 = LineDataSet(y113, "陌生词")
        set3.setCircleColor(resources.getColor(R.color.blue_login))
        set3.setDrawCircles(false)
        set3.color = resources.getColor(R.color.blue_login)
        set3.mode = LineDataSet.Mode.CUBIC_BEZIER
        set3.valueTextColor = resources.getColor(R.color.blue_login)
        lineDataSet.add(set3)

        val data = LineData(lineDataSet)

        chart.data = data

    }

    var jsOrMsAdapter: TextVoiceAdapter? = null
    val jsOrMsList = arrayListOf<BookInfo>()

    /**
     * 初始化夹生词adapter
     */
    private fun initMsAdapter() {
        msRecyclerView.isNestedScrollingEnabled = false
        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        msRecyclerView.layoutManager = manager

        jsOrMsAdapter = TextVoiceAdapter(activity!!, jsOrMsList)
        msRecyclerView.adapter = jsOrMsAdapter
        jsOrMsAdapter!!.onVoiceListener = object : TextVoiceAdapter.OnVoiceListener {

            override fun onVoice(position: Int) {
                val info = jsOrMsList[position]
                playVoice(activity!!, Config.IP + info.video!!)
                showLoadDialog(activity!!, "播放中")

            }

        }

    }


    /**
     * 单元集合信息
     */
    val list = arrayListOf<BookInfo>()
    var studyAdapter: StudyAdapter? = null

    /**
     * 初始单元adapter
     */
    private fun initAdapter() {
        list.clear()

        studyRecyclerView.isNestedScrollingEnabled = false
        studyRecyclerView.layoutManager = GridLayoutManager(activity, 4)
        studyAdapter = StudyAdapter(activity!!, list)
        studyRecyclerView.adapter = studyAdapter

        studyAdapter!!.onTvListener = object : StudyAdapter.OnClickListener {
            override fun onReview(position: Int) {
                val info = list[position]
                (activity as MainPageActivity).toWhere(Constant.STUDY_REVIEW, info)

            }

            override fun onTest(position: Int) {

                val info = list[position]
                info.code = 0
                (activity as MainPageActivity).toWhere(Constant.STUDY_TEST, info)

            }

            override fun onDictation(position: Int) {
                (activity as MainPageActivity).toWhere(Constant.LISTEN_WRITE_CODE, BookInfo())

            }

            override fun onDictationTraining(position: Int) {
                (activity as MainPageActivity).toWhere(Constant.LISTEN_READ_CODE, BookInfo())
            }

        }

    }

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.GET_STUDY_CODE -> {
                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                val state = json.optBoolean(Config.STATUS)

                if (code == Config.SUCCESS && state) {
                    val resultData = json.optJSONObject(Config.DATA)
                    val resultBook = resultData.optString("book")
                    val book = gson!!.fromJson<BookInfo>(resultBook, BookInfo::class.java)

                    showBook(book)

                    val bookUnit = resultData.optString("unit")
                    val unitResultArr = gson!!.fromJson<ArrayList<BookInfo>>(bookUnit, object : TypeToken<ArrayList<BookInfo>>() {}.type)

                    studyAdapter!!.updateDataClear(unitResultArr)
                    finishAdapter!!.updateDataClear(unitResultArr)

                    showIsAllTest(unitResultArr)

                    val unitName = resultData.optString("unit_name")
                    val sxWord = resultData.optString("sx")
                    val jsWord = resultData.optString("js")
                    val msWord = resultData.optString("ms")

                    showTongJi(unitName, sxWord, jsWord, msWord)

                    val jsOrMsResult = resultData.optString("js_word")
                    val jsOrMsArr = gson!!.fromJson<ArrayList<BookInfo>>(jsOrMsResult, object : TypeToken<ArrayList<BookInfo>>() {}.type)

                    if (jsOrMsArr != null && jsOrMsArr.size != 0) {
                        showJsOrMs(jsOrMsArr)
                    }


                    val wordInfo = resultData.optString("pm")
                    val bookDetails = gson!!.fromJson<BookInfo>(wordInfo, BookInfo::class.java)
                    showDetails(bookDetails)

                }
            }
        }

    }

    private fun showIsAllTest(unitResultArr: ArrayList<BookInfo>?) {

        var indexNum = 0

        for (temp in unitResultArr!!) {
            if (temp.status == 1) {
                indexNum++
            }
        }

        if (testLinearLayout != null) {

            if (indexNum == unitResultArr.size - 1) {

                testLinearLayout.visibility = View.VISIBLE

            } else {
                testLinearLayout.visibility = View.GONE
            }
        }

    }

    private fun showDetails(bookDetails: BookInfo?) {
        if (tvMingCiNum != null) {
            tvMingCiNum.text = bookDetails!!.zpm.toString()
        }

        if (tvChaJu != null) {
            tvChaJu.text = bookDetails!!.cj.toString()
        }

        if (tvTodayNum != null) {
            tvTodayNum.text = bookDetails!!.study_word.toString()
        }

    }

    private fun showJsOrMs(jsOrMsArr: ArrayList<BookInfo>?) {
        if (jsOrMsAdapter != null) {
            jsOrMsAdapter!!.updateDataClear(jsOrMsArr!!)
        }
    }

    private fun showTongJi(unitName: String?, sxWord: String, jsWord: String, msWord: String) {
        textArr.addAll(gson!!.fromJson(unitName, object : TypeToken<ArrayList<String>>() {}.type))

        if (studyChart != null) {
            initStudyChart(studyChart)
            setTextData(studyChart, sxWord, jsWord, msWord)
        }

    }


    /**
     * 显示书籍
     */
    private fun showBook(book: BookInfo?) {
        this.bookInfo = book

        if (tvStudyBookName != null) {
            tvStudyBookName.text = book!!.book_name
        }

    }
}