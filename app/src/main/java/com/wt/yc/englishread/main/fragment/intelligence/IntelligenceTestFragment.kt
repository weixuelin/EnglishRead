package com.wt.yc.englishread.main.fragment.intelligence

import android.os.Bundle
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.info.QuestionInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.intelligence_test_fragment.*
import kotlinx.android.synthetic.main.view_pager_test.view.*
import org.json.JSONObject
import java.util.*

/**
 * 一测到底信息
 */
class IntelligenceTestFragment : ProV4Fragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.intelligence_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initClick()

        getTestList()

    }

    /**
     * 答案数组集合
     */
    val answerMap = hashMapOf<Int, String>()


    /**
     * 问题数组信息
     */
    var questionArr: java.util.ArrayList<QuestionInfo> = arrayListOf()


    private fun getTestList() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("type", "yj")

        HttpUtils.getInstance().postJson(Config.GET_TEST_URL, json.toString(), Config.GET_TEST_CODE, handler)
        showLoadDialog(activity!!, "获取中")

    }

    private fun initClick() {
        btIntelUp.setOnClickListener {
            var indexPosition = testViewPager.currentItem
            if (indexPosition > 0) {
                indexPosition--
                testViewPager.currentItem = indexPosition

            }

        }

        btIntelPost.setOnClickListener {
            ///  交卷
            save(1)

        }

        btIntelNext.setOnClickListener {
            var indexPosition = testViewPager.currentItem
            indexPosition++
            testViewPager.currentItem = indexPosition

        }

        imageBack.setOnClickListener {
            (activity as MainPageActivity).isTestCode = false

            (activity as MainPageActivity).backTo()
        }
    }

    val viewPagerList: ArrayList<View> = arrayListOf()

    private fun addView() {

        testViewPager.currentItem = 0

        for (i in questionArr.indices) {

            val temp = questionArr[i]
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

    private fun buildArr(answerArr: HashMap<Int, String>): JSONObject {
        val json = JSONObject()
        for (temp in answerArr) {
            json.put(temp.key.toString(), temp.value)
        }
        return json

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

                    questionArr.addAll(info.video!!)

                    log("questionArr----------" + questionArr.size)

                    if (questionArr.size != 0) {

                        addView()
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
                    bookInfo.book_name = ""

//                    fragmentManager!!.popBackStackImmediate("TestDetailsFragment", 0)
//                    fragmentManager!!.popBackStack()
//                    (activity as MainPageActivity).toWhere(Constant.ANSWER_RESULT, bookInfo)

                }

            }
        }
    }
}