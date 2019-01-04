package com.wt.yc.englishread.main.fragment.mainpage

import android.app.Dialog
import android.app.ProgressDialog.show
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.main.adapter.GrowUpRecordAdapter
import com.wt.yc.englishread.main.adapter.TestListAdapter
import kotlinx.android.synthetic.main.page_fragment.*
import android.text.SpannableString
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.utils.BitmapUtil
import com.xin.lv.yang.utils.utils.DataUtil
import com.xin.lv.yang.utils.utils.HttpUtils
import com.xin.lv.yang.utils.utils.TextUtils
import kotlinx.android.synthetic.main.finish_dialog.view.*
import kotlinx.android.synthetic.main.pai_number_dialog.view.*
import kotlinx.android.synthetic.main.set_num_doalog.view.*
import org.json.JSONObject
import kotlin.collections.ArrayList

/**
 * 学习首页fragment
 */
class PageFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

            Config.MAIN_PAGE_CODE -> {

                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)
                if (status) {
                    val jsonObject = json.optJSONObject(Config.DATA)

                    val bookResult = jsonObject.optString("book")
                    val bookResultUnit = jsonObject.optString("unit")
                    val bookResultWord = jsonObject.optString("word")

                    val book1 = gson!!.fromJson<BookInfo>(bookResult, BookInfo::class.java)
                    val book2 = gson!!.fromJson<BookInfo>(bookResultUnit, BookInfo::class.java)
                    val book3 = gson!!.fromJson<BookInfo>(bookResultWord, BookInfo::class.java)

                    showBook1(book1, book2, book3)

                    val countBookArr = gson!!.fromJson<ArrayList<BookInfo>>(jsonObject.optString("word_tj"), object : TypeToken<ArrayList<BookInfo>>() {}.type)

                    showCountArr(countBookArr)

                    val weekStr = jsonObject.optString("weak")
                    val weekInfo = gson!!.fromJson<BookInfo>(weekStr, BookInfo::class.java)
                    showWeekStr(weekInfo)

                    val testResult = jsonObject.optString("test")
                    val arr: ArrayList<BookInfo> = gson!!.fromJson(testResult, object : TypeToken<ArrayList<BookInfo>>() {}.type)
                    if (arr != null && arr.size != 0) {
                        textAdapter!!.updateData(arr)
                    }

                    /**
                     * 学习转化量
                     */
                    val study = jsonObject.optString("xfl")
                    val studyBook = gson!!.fromJson<BookInfo>(study, BookInfo::class.java)
                    showBook(studyBook)

                    val level = jsonObject.optString("level")
                    val target = jsonObject.optString("target")
                    tvUserLevel.text = level
                    tvLevelFen.text = "[$target]"


                }
            }
        }

    }


    /**
     * 学习转化量
     */
    fun showBook(info: BookInfo) {
        tvStudyNum.text = info.xx
        tvFuXiStudyNum.text = info.fx

//        setTextData(zhlLineChart, 1, 2)

    }

    var studyArr1: Array<String>? = null
    var studyArr2: Array<String>? = null

    /**
     * 显示一周学习量
     */
    private fun showWeekStr(book: BookInfo?) {
        val wordXs = book!!.xs
        val wordShuXi = book.sx

        studyArr1 = gson!!.fromJson<Array<String>>(wordXs, object : TypeToken<Array<String>>() {}.type)

        studyArr2 = gson!!.fromJson<Array<String>>(wordShuXi, object : TypeToken<Array<String>>() {}.type)

        setTextData(studyLineChart, 2, 2)

    }

    /**
     * 显示单词统计
     */
    private fun showCountArr(countBookArr: ArrayList<BookInfo>?) {
        bindData(countBookArr!!)
    }


    /**
     * 显示学习的单词信息
     */
    private fun showBook1(book1: BookInfo?, book2: BookInfo?, book3: BookInfo?) {
        tvBookName.text = book1!!.book_name
        tvUnitName.text = "正在学习单元: ${book2!!.unit_name}"
        tvUnitWord.text = "上次学习单词: ${book3!!.english}"

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.page_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initWOrH()

        initCXpieChart()
        initGfzChart()
        initZhLChart()
        initStudyChart()

        initAdapter()
        initCzAdapter()

        initClick()

        get()

        setIndexTime()


    }

    private fun setIndexTime() {
        val indexTime = System.currentTimeMillis()
        val timeStr = DataUtil.longToTime(indexTime / 1000, "yyyy-MM-dd")
        tvIndexTime.text = timeStr
        tvIndexWeek.text = DataUtil().getWeekByDateStr(timeStr, "-")

    }

    fun get() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_MAIN_PAGE_URL, json.toString(), Config.MAIN_PAGE_CODE, handler)

    }

    /**
     * 动态设置宽高
     */
    private fun initWOrH() {

        val scanW = getW(activity!!) - BitmapUtil.dip2px(activity!!, 170f)
        val w = scanW / 3
        val h = w * 4 / 5

        linear1.layoutParams = LinearLayout.LayoutParams(w, h)

        linear2.layoutParams = LinearLayout.LayoutParams(w, h)
        setMargen(linear2, 8)

        linear3.layoutParams = LinearLayout.LayoutParams(w, h)
        setMargen(linear3, 8)

        linear4.layoutParams = LinearLayout.LayoutParams(w * 2, h)
        linear5.layoutParams = LinearLayout.LayoutParams(w, h)
        linear6.layoutParams = LinearLayout.LayoutParams(w * 2, h)

        linear7.layoutParams = LinearLayout.LayoutParams(w, h)
        setMargen(linear7, 8)

        linear8.layoutParams = LinearLayout.LayoutParams(w * 2, h)

        linear9.layoutParams = LinearLayout.LayoutParams(w, h)
        setMargen(linear9, 8)

    }

    private fun initClick() {
        buttonContinue.setOnClickListener {
            /// 继续学习
            (activity as MainPageActivity).toWhere(Constant.MAIN_STADUY_CODE, Info())

        }

        imagePaiMing.setOnClickListener {
            showNumDialog()
        }

        tvIndexInfo.setOnClickListener { showSetNum() }

        linear3.setOnClickListener {
            (activity as MainPageActivity).toWhere(Constant.MAIN_TIAO_ZHAN, Info())
        }
    }

    override fun onResume() {
        super.onResume()

        initImagePic()

    }

    /**
     * 今日目标完成弹出dialog
     */
    private fun showFinish() {
        val view = layoutInflater.inflate(R.layout.finish_dialog, null)
        val dialog: Dialog = Dialog(activity, R.style.style)
        dialog.setContentView(view)
        dialog.show()
        setAlpha(activity!!, 0.6f)

        view.imageClose.setOnClickListener {
            dialog.dismiss()
            setAlpha(activity!!, 1f)
        }

        view.buttonFinish.setOnClickListener {
            dialog.dismiss()
            setAlpha(activity!!, 1f)
        }
        dialog.setOnDismissListener {
            setAlpha(activity!!, 1f)
        }

    }


    /**
     * 设置今日单词目标
     */
    private fun showSetNum() {
        val setDCView = layoutInflater.inflate(R.layout.set_num_doalog, null)

        val setDialog: Dialog = Dialog(activity, R.style.style)
        setDialog.setContentView(setDCView)
        setDialog.show()
        setAlpha(activity!!, 0.6f)

        setDialog.setOnDismissListener {
            setAlpha(activity!!, 1f)
        }


        setDCView.setNumClose.setOnClickListener {
            setAlpha(activity!!, 1f)
            setDialog.dismiss()
        }

        setDCView.setNumQure.setOnClickListener {
            setAlpha(activity!!, 1f)
            setDialog.dismiss()
        }

    }

    /**
     * 显示排名dialog
     */
    private fun showNumDialog() {
        val paiText = "排名第 224 名"
        val view = layoutInflater.inflate(R.layout.pai_number_dialog, null)
        val sp = SpannableString(paiText)

        TextUtils.getInstance().setColor(activity, sp, 3, paiText.length - 1, R.color.blue_login, TextUtils.COLOR)
        view.tvPaiMing.setText(sp, TextView.BufferType.SPANNABLE)

        val dialog = Dialog(activity, R.style.style)
        dialog.setContentView(view)
        dialog.show()
        setAlpha(activity!!, 0.6f)

        dialog.setOnDismissListener {
            setAlpha(activity!!, 1f)
        }

        view.paiClose.setOnClickListener {
            dialog.dismiss()
            setAlpha(activity!!, 1f)
        }

        view.paiBtQure.setOnClickListener {
            dialog.dismiss()
            setAlpha(activity!!, 1f)
        }

    }

    private fun initImagePic() {
        val date = DataUtil.longToTime(System.currentTimeMillis() / 1000, "yyyy-MM-dd")
        tvIndexTime.text = date

        tvIndexWeek.text = DataUtil().getWeekByDateStr(date, "-")

        val p = 50
        progressBar.progress = p

        val vto = picFrameLayout.viewTreeObserver

        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                picFrameLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                val pw = picFrameLayout.width

                val picw = p * pw / 100

                log("wwww--------$pw-------$picw")

                val params = imagePic.layoutParams as FrameLayout.LayoutParams
                params.setMargins(picw, 0, 0, 0)
                imagePic.layoutParams = params

            }
        })

    }


    val czPicList = arrayListOf<Int>(R.drawable.chengzhang_pic, R.drawable.week_pic, R.drawable.all_test_pic, R.drawable.study_time_pic)
    val czTextArr = arrayListOf<String>("我的成长经历", "一周学习趋势", "所有测试成绩", "学习时间统计")
    val czList = arrayListOf<Info>()


    private fun initCzAdapter() {

        for (i in czPicList.indices) {
            val info = Info()
            info.title = czTextArr[i]
            info.pic = czPicList[i]
            czList.add(info)
        }

        czRecyclerView.isNestedScrollingEnabled = false
        czRecyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = GrowUpRecordAdapter(activity!!, czList)
        czRecyclerView.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> (activity as MainPageActivity).toWhere(Constant.MY_GROUP_UP_CODE, null)
                    1 -> (activity as MainPageActivity).toWhere(Constant.MY_WEEK_CODE, null)
                }
            }

            override fun onLongClick(position: Int) {

            }

        }
    }

    var textAdapter: TestListAdapter? = null
    val testList = arrayListOf<BookInfo>()
    private fun initAdapter() {
        testRecyclerView.isNestedScrollingEnabled = false
        testRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        testRecyclerView.addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        textAdapter = TestListAdapter(activity!!, testList)
        testRecyclerView.adapter = textAdapter
    }


    /**
     * 初始化学习图标信息
     */
    private fun initStudyChart() {
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

            val str = if (num < 0 || num >= lineStr.size) {
                ""
            } else {
                lineStr[num]
            }

            axis.textSize = 10f

            str

        }

        // 执行的动画,x轴（动画持续时间）
        studyLineChart.animateX(2500)


    }

    /**
     * 转换量初始化
     */
    private fun initZhLChart() {
        // 是否在折线图上添加边框
        zhlLineChart.setDrawBorders(false)
        // 曲线描述 -标题
        val des = Description()
        des.text = ""
        zhlLineChart.description = des
        // 是否显示表格颜色
        zhlLineChart.setDrawGridBackground(false)
        // 禁止绘制图表边框的线
        zhlLineChart.setDrawBorders(false)
        // 表格的的颜色，在这里是是给颜色设置一个透明度
        // lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);
        // 设置是否启动触摸响应
        zhlLineChart.setTouchEnabled(true)
        // 是否可以拖拽
        zhlLineChart.isDragEnabled = true
        // 是否可以缩放
        zhlLineChart.setScaleEnabled(true)
        // 如果禁用，可以在x和y轴上分别进行缩放
        zhlLineChart.setPinchZoom(false)
        // lineChart.setMarkerView(mv)
        // 设置背景色
        // lineChart.setBackgroundColor(getResources().getColor(R.color.bg_white));
        // 图例对象
        val mLegend: Legend = zhlLineChart.legend
        // mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        // 图例样式 (CIRCLE圆形；LINE线性；SQUARE是方块）
        mLegend.form = Legend.LegendForm.NONE

        zhlLineChart.axisRight.isEnabled = false

        val yyy = zhlLineChart.axisLeft
        yyy.setDrawZeroLine(false)
        yyy.setDrawGridLines(false)
        val xAxis = zhlLineChart.xAxis
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
            val str = if (num < 0 || num >= lineStr.size) {
                ""
            } else {
                lineStr[num]
            }
            axis.textSize = 10f
            str
        }

        // 执行的动画,x轴（动画持续时间）
        zhlLineChart.animateX(2500)


    }

    /**
     * 一周学习量
     */
    val lineStr = arrayListOf<String>("", "一", "二", "三", "四", "五", "六")

    /**
     * 显示线性数据
     */
    private fun setTextData(chart: LineChart, code: Int, countLen: Int) {
        val y = arrayListOf<Entry>()
        val y2 = arrayListOf<Entry>()

        val y3 = arrayListOf<Entry>()

        for (i in 0 until lineStr.size) {
            when (i) {
                0 -> {
                    when (countLen) {
                        2 -> {
                            y.add(Entry(i.toFloat(), 0f, ""))
                            y2.add(Entry(i.toFloat(), 0f, "熟悉的单词"))
                        }
                        3 -> {
                            y.add(Entry(i.toFloat(), 0f, ""))
                            y2.add(Entry(i.toFloat(), 0f, ""))
                            y3.add(Entry(i.toFloat(), 0f, ""))
                        }
                    }

                }

                else -> {

                    when (countLen) {
                        2 -> {
                            y.add(Entry(i.toFloat(), studyArr1!![i - 1].toFloat(), ""))
                            y2.add(Entry(i.toFloat(), studyArr2!![i - 1].toFloat(), ""))
                        }

                        3 -> {

//                            y.add(Entry(i.toFloat(), num1.toFloat(), ""))
//                            y2.add(Entry(i.toFloat(), num2.toFloat(), ""))
//                            y3.add(Entry(i.toFloat(), num3.toFloat(), ""))

                        }
                    }

                }
            }

        }

        val lineDataSet = arrayListOf<ILineDataSet>()
        val set1 = LineDataSet(y, "学习的单词")
        set1.setCircleColor(resources.getColor(R.color.red))
        set1.setDrawCircles(false)
        set1.color = resources.getColor(R.color.red)
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.valueTextColor = resources.getColor(R.color.red)
        if (code == 2) {
            set1.setDrawFilled(true)
            // 填充颜色
            set1.fillColor = resources.getColor(R.color.red)
        }

        val set2 = LineDataSet(y2, "熟悉的单词")
        set2.setCircleColor(resources.getColor(R.color.blue_login))
        set2.setDrawCircles(false)
        set2.color = resources.getColor(R.color.blue_login)
        set2.mode = LineDataSet.Mode.CUBIC_BEZIER
        set2.valueTextColor = resources.getColor(R.color.blue_login)

        if (code == 2) {
            set2.setDrawFilled(true)
            // 填充颜色
            set2.fillColor = resources.getColor(R.color.blue_login)
        }

        if (countLen == 3) {
            val set3 = LineDataSet(y3, "")
            set3.setCircleColor(resources.getColor(R.color.colorAccent))
            set3.setDrawCircles(false)
            set3.color = resources.getColor(R.color.colorAccent)
            set3.mode = LineDataSet.Mode.CUBIC_BEZIER
            set3.valueTextColor = resources.getColor(R.color.colorAccent)

            if (code == 2) {
                set3.setDrawFilled(true)
                // 填充颜色
                set3.fillColor = resources.getColor(R.color.colorAccent)
            }

            lineDataSet.add(set3)
        }

        lineDataSet.add(set1)
        lineDataSet.add(set2)

        val data = LineData(lineDataSet)

        chart.data = data

    }

    private fun initGfzChart() {
        //设置矩形阴影是否显示
        gfzBarChart.setDrawBarShadow(false)
        //设置值是否在矩形的上方显示
        gfzBarChart.setDrawValueAboveBar(true)
        //设置右下角描述
        val des = Description()
        des.text = ""
        gfzBarChart.description = des
        //没用数据时显示
        gfzBarChart.setNoDataText("没有数据")

        // 设置是否可以触摸
        gfzBarChart.setTouchEnabled(true)
        // 是否可以拖拽
        gfzBarChart.isDragEnabled = true
        // 是否可以缩放
        gfzBarChart.setScaleEnabled(true)
        // 集双指缩放
        gfzBarChart.setPinchZoom(false)
        // 设置是否显示表格颜色,矩形之间的空隙
        gfzBarChart.setDrawGridBackground(false)
        // 设置表格的的颜色，矩形之间的空隙颜色
        gfzBarChart.setGridBackgroundColor(Color.GRAY)
        //设置比例显示
        val l: Legend = gfzBarChart.legend
        //设置比例显示在柱形图哪个位置
        l.position = Legend.LegendPosition.BELOW_CHART_LEFT
        //设置比例显示形状，方形，圆形，线性
        l.form = Legend.LegendForm.SQUARE
        //设置比例显示形状的大小
        l.formSize = 10f
        //设置比例显示文字的大小
        l.textSize = 10f
        l.xEntrySpace = 4f

        //设置X轴方向上的属性
        val xAxis: XAxis = gfzBarChart.xAxis
        //设置标签显示在柱形图的上方还是下方
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.typeface = Typeface.DEFAULT
        //设置是否绘制表格
        xAxis.setDrawGridLines(false)

        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f

        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->

            val num = value.toInt()
            log("--------$num")

            val str = if (num < 0 || num >= gfzText.size) {
                ""
            } else {
                gfzText[num]
            }

            axis.textSize = 10f

            str

        }

        //设置柱形图左边y轴方向上的属性
        val leftAxis: YAxis = gfzBarChart.axisLeft
        leftAxis.typeface = Typeface.DEFAULT
        //设置y轴上的标签数,boolean值为true代表必须8个
        leftAxis.setLabelCount(8, false)
        leftAxis.setDrawZeroLine(false)
        leftAxis.setDrawGridLines(false)

        //设置标签在柱形图的哪个位置
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        //设置y轴标签之间的间距
        leftAxis.spaceTop = 15f
        leftAxis.setAxisMinValue(0f)

        //设置柱形图右边y轴方向上的属性,属性含义与上面相同
        val rightAxis: YAxis = gfzBarChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.typeface = Typeface.DEFAULT
        rightAxis.setLabelCount(6, true)
        rightAxis.spaceTop = 15f
        rightAxis.setAxisMinValue(0f)

        /// 隐藏右边的坐标轴
        gfzBarChart.axisRight.isEnabled = false

        setgfzChartData()

    }

    val gfzText = arrayListOf("", "攻击力", "防御力", "可挑战次数", "成就")

    /**
     * 设置攻防战数据
     */
    private fun setgfzChartData() {
        // 设置每个矩形在y轴上的值
        val yVals1: ArrayList<BarEntry> = arrayListOf()

        for (i in 0 until gfzText.size) {
            yVals1.add(BarEntry(i.toFloat(), i * 10.toFloat()))
        }

        // 第一个参数是各个矩形在y轴方向上的值得集合，第二个参数为比例的文字说明
        val set1 = BarDataSet(yVals1, "")

        val colors: ArrayList<Int> = arrayListOf(resources.getColor(R.color.green), resources.getColor(R.color.blue_login), resources.getColor(R.color.red))

        set1.colors = colors

        val dataSets: ArrayList<IBarDataSet> = arrayListOf()

        dataSets.add(set1)

        // 设置柱形图的数据
        val data = BarData(dataSets)

        data.setValueTextSize(10f)
        data.setValueTypeface(Typeface.DEFAULT)
        gfzBarChart.data = data

    }

    /**
     * 词性分布图信息
     */
    private fun initCXpieChart() {
        // 设置饼图是否接收点击事件，默认为true
        cxPieChart.setTouchEnabled(true)
        cxPieChart.setDrawSliceText(false)
        //设置饼图是否使用百分比
        cxPieChart.setUsePercentValues(true)
        //设置饼图右下角的文字描述
        val des = Description()
        des.text = "统计"
        cxPieChart.description = des
        //是否显示圆盘中间文字，默认显示
        cxPieChart.setDrawCenterText(false)
        //设置圆盘中间文字的颜色
        cxPieChart.setCenterTextColor(Color.RED)
        //设置圆盘中间文字的字体
        cxPieChart.setCenterTextTypeface(Typeface.DEFAULT)
        //设置中间圆盘的颜色
        cxPieChart.setHoleColor(Color.TRANSPARENT)
        //设置中间圆盘的半径,值为所占饼图的百分比
        cxPieChart.holeRadius = 65f
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
    private fun bindData(countBookArr: ArrayList<BookInfo>) {
        val bb1 = countBookArr[0]  // 熟悉词
        val bb2 = countBookArr[1]  // 夹生词
        val bb3 = countBookArr[2]  // 陌生词

        val all = bb1.sxc + bb2.jsc + bb3.msc
        val num1 = bb1.sxc / all
        val num2 = bb3.msc / all
        val num3 = bb2.jsc / all

        val valueList: ArrayList<PieEntry> = arrayListOf()
        valueList.add(PieEntry(num1.toFloat(), "熟悉词"))
        valueList.add(PieEntry(num2.toFloat(), "陌生词"))
        valueList.add(PieEntry(num3.toFloat(), "夹生词"))

        // 显示在比例图上
        val dataSet = PieDataSet(valueList, "")
        //设置个饼状图之间的距离
        dataSet.sliceSpace = 3f
        // 部分区域被选中时多出的长度
        dataSet.selectionShift = 5f

        dataSet.setDrawValues(true)

        /// 设置饼图各个区域颜色
        val colors: ArrayList<Int> = ArrayList<Int>()
        colors.add(resources.getColor(R.color.blue_login))
        colors.add(resources.getColor(R.color.green))
        colors.add(resources.getColor(R.color.red))

        dataSet.colors = colors

        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 14f
        dataSet.valueTypeface = Typeface.DEFAULT_BOLD

        /// 是否在图上显示数值
        dataSet.setDrawValues(true)

        dataSet.valueLinePart1Length = 0.1f
//      当值位置为外边线时，表示线的后半段长度。
        dataSet.valueLinePart2Length = 0.2f

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

        cxPieChart.data = data

        cxPieChart.highlightValues(null)
        cxPieChart.invalidate()
    }

}