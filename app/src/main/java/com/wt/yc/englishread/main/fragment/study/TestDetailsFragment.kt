package com.wt.yc.englishread.main.fragment.study

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
                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)

                if (status) {
                    val data = json.optString(Config.DATA)
                    val info = gson!!.fromJson<Info>(data, Info::class.java)

                    questionArr = info.video!!

                    initTestViewPager()
                    showTime()
                }
            }

            1234 -> {

                if (indexTime != 0) {
                    indexTime--
                }

                tvTestTime.text = "${indexTime / 60}分 ${indexTime % 60}秒"

            }

            Config.FINISH_CODE -> {

                indexTime = 0
                removeLoadDialog()

                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)

                if (status) {

                    fragmentManager!!.popBackStackImmediate("TestDetailsFragment", 0)
                    (activity as MainPageActivity).toWhere(Constant.ANSWER_RESULT, null)

                }
            }
        }
    }

    var timeStr = "6分钟"
    val timer = Timer()
    var indexTime = 6 * 60

    private fun showTime() {

        tvTestTime.text = timeStr

        val timerTask = object : TimerTask() {
            override fun run() {
                val message = handler!!.obtainMessage()
                message.what = 1234
                message.obj = ""
                handler!!.sendMessage(message)

            }

        }

        timer.schedule(timerTask, 0, 1000)

    }


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.test_details_fragment, container, false)
    }


    /**
     * 0 为单元测试     1 为生词测试
     */
    var testCode: Int = 1
    var unitInfo: BookInfo? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTitle.text = when (testCode) {
            0 -> "单元测试"
            1 -> "生词测试"
            else -> ""
        }

        tvName.text = "第一单元"

        initClick()

        initRightMuLu()

        getQuestList()


    }

    override fun onResume() {
        super.onResume()
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
            2 -> ""
            else -> ""
        })

        if (testCode == 0) {
            json.put("unit_id", unitInfo!!.id)
        }

        HttpUtils.getInstance().postJson(Config.GET_TEST_URL, json.toString(), Config.GET_TEST_CODE, handler)


    }

    /**
     * 目录列表信息
     */
    val muLuArr = arrayListOf("测试一", "测试二", "测试三", "测试四")

    /**
     * 初始化目录信息
     */
    private fun initRightMuLu() {
        muLuRecyclerView.layoutManager = LinearLayoutManager(activity)
        muLuRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        val muLuAdapter = MuLuAdapter(activity!!, muLuArr)
        muLuRecyclerView.adapter = muLuAdapter
        muLuAdapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                drawerLayout.closeDrawer(Gravity.END)
            }

            override fun onLongClick(position: Int) {

            }

        }

    }

    var indexNum = 0

    private fun initClick() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

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
            timer.cancel()

            val str = buildArr(answerMap)

            val json = JSONObject()
            json.put("uid", uid)
            json.put("token", token)
            json.put("arr", str)

            json.put("time", 6 * 60 - indexTime)

            HttpUtils.getInstance().postJson(Config.FINISH_TEST_URL, json.toString(), Config.FINISH_CODE, handler)
            showLoadDialog(activity!!, "交卷中")

            log("wwwwwww------$str")

        }

        btNext.setOnClickListener {
            indexNum++
            testViewPager.currentItem = indexNum
        }
    }

    private fun buildArr(answerArr: HashMap<Int, String>): String {
        val json = JSONObject()
        for (temp in answerArr) {
            json.put(temp.key.toString(), temp.value)
        }
        return json.toString()

    }

    /**
     * viewPager 集合
     */
    val viewPagerList = arrayListOf<View>()

    /**
     * 答案数组
     */
    val answerMap = hashMapOf<Int, String>()

    /**
     * 问题数组信息
     */
    var questionArr: ArrayList<QuestionInfo> = arrayListOf()

    private fun initTestViewPager() {

        for (temp in questionArr) {

            val view = layoutInflater.inflate(R.layout.view_pager_test, null)

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

            view.radioGroup.setOnCheckedChangeListener { p0, p1 ->
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