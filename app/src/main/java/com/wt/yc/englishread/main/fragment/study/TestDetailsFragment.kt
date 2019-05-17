package com.wt.yc.englishread.main.fragment.study

import android.app.AlertDialog
import android.os.Bundle
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.info.QuestionInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.MuLuAdapter
import com.wt.yc.englishread.main.adapter.TextChooseAdapter
import com.xin.lv.yang.utils.utils.HttpUtils
import com.xin.lv.yang.utils.view.AdTextView
import kotlinx.android.synthetic.main.test_details_fragment.*
import kotlinx.android.synthetic.main.view_pager_test.view.*
import org.json.JSONObject
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.HashMap


/**
 * 测试详情fragment 页面  开始答题
 */
class TestDetailsFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String

        when (msg.what) {

            Config.GET_TEST_CODE -> {

                removeLoadDialog()

                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)

                if (status) {

                    val data = json.optString(Config.DATA)
                    val info = gson!!.fromJson<Info>(data, Info::class.java)

                    questionArr = info.video!!

                    log("questionArr----------" + questionArr.size)

                    if (questionArr.size != 0) {

                        initTestViewPager()
                        showTime()

                    } else {
                        indexTime = 0
                        (activity as MainPageActivity).isTestCode = false
                        tvTestTime.visibility = View.GONE

                    }

                } else {

                    showShortToast(activity!!, "生成试卷失败,请继续学习!!")

                    tvTestTime.visibility = View.GONE

                    indexTime = 0
                    (activity as MainPageActivity).isTestCode = false

                }
            }

            1234 -> {

                if (indexTime != 0) {

                    indexTime--

                    finishTime++
                }

                if (tvTestTime != null) {
                    tvTestTime.text = "${indexTime / 60}分 ${indexTime % 60}秒"
                }

                if (finishTime == 6 * 60) {

                    save(2)

                }

            }

            Config.FINISH_CODE -> {

                indexTime = 0
                removeLoadDialog()

                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)

                showShortToast(activity!!, json.optString(Config.MSG))

                if (status) {
                    val data = json.optJSONObject("data")
                    val userName = data.optString("username")
                    val testTime = data.optString("test_time")
                    val time = data.optString("time")
                    val fs = data.optString("fs")

                    val bookInfo = BookInfo()
                    bookInfo.userName = userName
                    bookInfo.testTime = testTime
                    bookInfo.time = time
                    bookInfo.score = fs
                    bookInfo.book_name = tvTitle.text.toString()

                    fragmentManager!!.popBackStackImmediate("TestDetailsFragment", 0)
                    fragmentManager!!.popBackStack()

                    (activity as MainPageActivity).toWhere(Constant.ANSWER_RESULT, bookInfo)

                }

            }

            Config.GET_STUDY_CODE -> {

                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                val state = json.optBoolean(Config.STATUS)
                if (code == Config.SUCCESS && state) {
                    val resultData = json.optJSONObject(Config.DATA)

                    val bookUnit = resultData.optString("unit")

                    val unitResultArr = gson!!.fromJson<ArrayList<BookInfo>>(bookUnit, object : TypeToken<ArrayList<BookInfo>>() {}.type)

                    showMuLuAdapter(unitResultArr)

                }
            }
        }
    }

    private fun showMuLuAdapter(unitResultArr: ArrayList<BookInfo>?) {

        this.muLuArr = unitResultArr!!
        val arr = arrayListOf<String>()

        for (temp in unitResultArr) {
            arr.add(temp.unit_name)

        }

        muLuAdapter!!.updateDataClear(arr)


    }

    var timeStr = "6分钟"


    var timer: Timer? = null

    /**
     * 倒计时的时间
     */
    var indexTime = 6 * 60

    /**
     * 测试完成时间
     */
    var finishTime = 0

    private fun showTime() {

        if (timer != null) {
            timer!!.cancel()

            timer = null
        }

        timer = Timer()

        indexTime = 6 * 60

        tvTestTime.text = timeStr

        val timerTask = object : TimerTask() {
            override fun run() {
                val message = handler!!.obtainMessage()
                message.what = 1234
                message.obj = ""
                handler!!.sendMessage(message)

            }
        }

        timer!!.schedule(timerTask, 0, 1000)

    }


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.test_details_fragment, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()

        indexTime = 0

        if (timer != null) {
            timer!!.cancel()
        }
    }


    /**
     * 0 为单元测试     1 为生词测试    2 为本书测试，综合测试   3 为熟词测试
     */
    var testCode: Int = 1

    var unitInfo: BookInfo? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (unitInfo != null) {
            testCode = unitInfo!!.code
        }

        tvTitle.text = when (testCode) {
            0 -> "单元测试"
            1 -> "生词测试"
            2 -> "综合测试"
            3 -> "熟词测试"
            else -> ""
        }

        tvName.text = ""

        initClick()

        initRightMuLu()

        getQuestList()

        get()


    }

    override fun onResume() {
        super.onResume()

        if (timer != null) {
            timer!!.purge()
        }
    }

    /**
     * 获取问题列表
     */
    private fun getQuestList() {
        getTestList()
    }


    private fun getTestList() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("type", when (testCode) {
            0 -> "unit"
            1 -> "sc"
            2 -> "zh"
            3 -> "sx"
            else -> "unit"
        })

        if (testCode == 0 && unitInfo != null) {
            json.put("unit_id", unitInfo!!.id)
        }

        HttpUtils.getInstance().postJson(Config.GET_TEST_URL, json.toString(), Config.GET_TEST_CODE, handler)
        showLoadDialog(activity!!, "获取中")


    }

    var isVisibleCode = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleCode = isVisibleToUser
    }

    /**
     * 目录列表信息
     */
    var muLuArr = arrayListOf<BookInfo>()

    fun get() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_STUDY_URL, json.toString(), Config.GET_STUDY_CODE, handler)

    }


    var muLuAdapter: MuLuAdapter? = null

    /**
     * 初始化目录信息
     */
    private fun initRightMuLu() {

        muLuRecyclerView.layoutManager = LinearLayoutManager(activity)
        muLuRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        muLuAdapter = MuLuAdapter(activity!!, arrayListOf())

        muLuRecyclerView.adapter = muLuAdapter
        muLuAdapter!!.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {

                drawerLayout.closeDrawer(Gravity.END)

                testCode = 0
                unitInfo = muLuArr[position]

                questionArr.clear()

                testViewPager.removeAllViews()

                getQuestList()

            }

            override fun onLongClick(position: Int) {

            }

        }

    }

    /**
     * 题目在集合中的位置
     */
    var indexNum = 0

    private fun initClick() {
        imageCancel.setOnClickListener {
            (activity as MainPageActivity).isTestCode = false
            (activity as MainPageActivity).backTo()
        }

        imageViewMuLu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(Gravity.END)) {
                drawerLayout.closeDrawer(Gravity.END)
            } else {
                drawerLayout.openDrawer(Gravity.END)
            }

        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END)

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END)
            }

            override fun onDrawerOpened(drawerView: View) {

            }

        })

        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        tvMuLu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.END)
        }

        btUp.setOnClickListener {
            if (indexNum > 0) {
                indexNum--
                testViewPager.currentItem = indexNum
            }
        }

        btPost.setOnClickListener {

            save(1)

        }

        btNext.setOnClickListener {
            indexNum++
            if (indexNum != questionArr.size) {
                testViewPager.currentItem = indexNum
            } else {
                showTiSave()
            }

        }
    }


    /**
     * 自动交卷   code 1 为正常作答   2 为未作答,时间走完,重新计时.
     */
    private fun save(code: Int) {

        if (answerMap.isNotEmpty()) {

            val str = buildArr(answerMap)

            val json = JSONObject()
            json.put("uid", uid)
            json.put("token", token)
            json.put("arr", str)

            json.put("time", finishTime)

            HttpUtils.getInstance().postJson(Config.FINISH_TEST_URL, json.toString(), Config.FINISH_CODE, handler)
            showLoadDialog(activity!!, "交卷中")

            timer!!.cancel()

        } else {

            if (code == 2) {
                showToastShort(activity!!, "您未答题,重新计时!!")
                showTime()
            } else {
                showToastShort(activity!!, "您未答题,请答题!!")
            }
        }
    }


    /**
     * 提示交卷
     */
    private fun showTiSave() {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("已是最后一题，是否交卷?")
                .setTitle("提示")
                .setPositiveButton("交卷") { dialog, _ ->
                    save(1)
                    dialog.dismiss()

                }.setNegativeButton("取消") { _, _ ->

                    indexNum--

                }.show()

    }

    private fun buildArr(answerArr: HashMap<Int, String>): JSONObject {
        val json = JSONObject()
        for (temp in answerArr) {
            json.put(temp.key.toString(), temp.value)
        }
        return json

    }

    /**
     * viewPager 集合
     */
    val viewPagerList = arrayListOf<View>()

    /**
     * 答案数组集合
     */
    val answerMap = hashMapOf<Int, String>()

    /**
     * 问题数组信息
     */
    var questionArr: ArrayList<QuestionInfo> = arrayListOf()

    private fun initTestViewPager() {

        tvTestTime.visibility = View.VISIBLE

        for (temp in questionArr) {

            val view = layoutInflater.inflate(R.layout.view_pager_test, null)

            /// 显示题号
            view.tvTestName.text = temp.number.toString()
            view.tvTestYinBiao.text = temp.ipa
            val answerArr = temp.answer

            view.rb1.text = answerArr!![0].dn
            view.rb2.text = answerArr[1].dn
            view.rb3.text = answerArr[2].dn
            view.rb4.text = answerArr[3].dn

            view.ivVoicePlay.setOnClickListener {
                playVoice(activity!!, Config.IP + temp.title)
            }

            view.radioGroup.setOnCheckedChangeListener { radioButton, p1 ->

                val number = temp.number

                when (p1) {
                    R.id.rb1 -> answerMap[number] = answerArr[0].dn
                    R.id.rb2 -> answerMap[number] = answerArr[1].dn
                    R.id.rb3 -> answerMap[number] = answerArr[2].dn
                    R.id.rb4 -> answerMap[number] = answerArr[3].dn
                }
            }

            viewPagerList.add(view)

        }

        testViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return viewPagerList.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val pageView = viewPagerList[position]
                container.addView(pageView)
                return pageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(viewPagerList[position])
            }

        }
    }


    private fun isExit(answerArr: ArrayList<Info>, temp: Info): Int {
        for (i in answerArr.indices) {
            val tt = answerArr[i]
            if (tt.id == temp.id) {
                return i
            }
        }
        return -1
    }

}