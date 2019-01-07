package com.wt.yc.englishread.main.fragment.study

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.review_fragment.*
import kotlinx.android.synthetic.main.review_three_view.view.*
import kotlinx.android.synthetic.main.review_two_view.view.*
import kotlinx.android.synthetic.main.review_view.view.*
import kotlinx.android.synthetic.main.study_head.*
import org.json.JSONObject
import java.util.*


class ReviewFragment : ProV4Fragment() {
    var rfInfo: BookInfo? = null

    /**
     * 书籍编号id
     */
    var unitId: String = ""
    /**
     * 单元id
     */
    var muId: String = ""


    override fun handler(msg: Message) {
        val str = msg.obj as String

        when (msg.what) {

            9999 -> {

                Log.i("result", "---------")

                indexTime--

                if (indexTime <= 0) {
                    timer!!.cancel()
                    tvTextTime.text = "0"
                } else {
                    tvTextTime.text = indexTime.toString()
                }
            }

            Config.REVIRE_CODE -> {
                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                if (code == Config.SUCCESS) {
                    val data = json.optJSONObject(Config.DATA)

                    unitId = data.optString("unit_id")
                    muId = data.optString("mu_id")

                    val wordResult = data.optString("word")

                    if (wordResult != null && wordResult != "") {

                        firstArr = gson!!.fromJson(wordResult, object : TypeToken<ArrayList<BookInfo>>() {}.type)

                    }

                    getOneList()

                }
            }
        }
    }


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.review_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        tvTopTitle.text = "复习"

        if (rfInfo != null) {
            tvTopName.text = "${rfInfo!!.unit_name} 第一阶段"
            get()

        }

        initClick()

    }

    override fun onStop() {
        super.onStop()
        if(timer!=null){
            timer!!.cancel()
        }

    }

    fun get() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("id", rfInfo!!.id)
        json.put("type", "new_word")
        HttpUtils.getInstance().postJson(Config.REVIEW_WORD_URL, json.toString(), Config.REVIRE_CODE, handler)
    }

    /**
     * 获取第一阶段的测试信息
     */
    private fun getOneList() {

        twoArr.addAll(firstArr)

        threeArr.addAll(firstArr)

        initFirstList()

        initTime()
    }

    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var indexTime = 10


    private fun initTime() {
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                val message = handler!!.obtainMessage()
                message.what = 9999
                message.obj = ""
                handler!!.sendMessage(message)
            }
        }

        timer!!.schedule(timerTask, 0, 1000)
    }

    /**
     * 获取第二阶段的测试信息
     */
    private fun getTwoList() {
        initTwoList()

    }

    /**
     * 获取第三阶段的测试信息
     */
    private fun getThreeList() {
        initThreeList()

    }

    val finishWordList = arrayListOf<BookInfo>()

    var vList: ArrayList<View> = arrayListOf()

    var firstArr = arrayListOf<BookInfo>()

    val twoArr = arrayListOf<BookInfo>()

    val threeArr = arrayListOf<BookInfo>()

    var indexView = 0

    var indexCode = 1
    /**
     * 输入的单词
     */
    var inputStr: String = ""

    private fun initClick() {

        buttonNext.setOnClickListener {

            when (indexCode) {
                1 -> {

                    val number = reviewViewPager.currentItem

                    if (number == firstArr.size - 1) {

                        showTiDialog()

                        timer!!.cancel()

                    } else {

                        indexView++
                        reviewViewPager.currentItem = indexView

                        indexTime = 10

                        initTime()

                        buttonNext.visibility = View.GONE

                    }
                }

                2 -> {

                    val number = reviewViewPager.currentItem

                    if (number == twoArr.size - 1) {
                        showThreeDialog()
                    } else {
                        indexView++
                        reviewViewPager.currentItem = indexView
                    }
                }

                3 -> {

                    val index = reviewViewPager.currentItem

                    if (index == threeArr.size - 1) {
                        // 提交数据
                        save()

                    } else {

                        if (inputStr != "") {

                            val info = threeArr[index]

                            if (info.english == inputStr) {
                                info.status = 1

                            } else {
                                info.status = 0
                            }

                            finishWordList.add(info)

                            indexView++
                            reviewViewPager.currentItem = indexView
                            initTime()

                        } else {
                            showShortToast(activity!!, "请输入单词")
                        }
                    }

                }
            }
        }

        tvFinishBack.setOnClickListener {
            (activity!! as MainPageActivity).backTo()
        }
    }

    /**
     * 保存数据
     */
    private fun save() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("study_data", buildList())
        HttpUtils.getInstance().postJson(Config.FINISH_REVIRE_URL, json.toString(), Config.FINISH_CODE, handler!!)

    }

    private fun buildList(): String? {

        return gson!!.toJson("")

    }


    private fun showThreeDialog() {

        val builder = AlertDialog.Builder(activity!!)

        builder.setMessage("进入第三阶段，识记阶段").setTitle("提示")
                .setPositiveButton("确定") { _, _ ->

                    getThreeList()

                    indexView = 0
                    indexCode = 3
                }.show()


    }


    private fun showTiDialog() {

        val builder = AlertDialog.Builder(activity!!)

        builder.setMessage("进入第二阶段，逆向思维阶段").setTitle("提示")
                .setPositiveButton("确定") { _, _ ->
                    getTwoList()
                    indexView = 0
                    indexCode = 2
                }.show()

    }


    /**
     * 初始化第一阶段
     */
    private fun initFirstList() {
        vList.clear()

        for (i in firstArr.indices) {
            addOneView(firstArr[i])
        }

        reviewViewPager.adapter = firstAdapter
    }


    private fun addOneView(info: BookInfo) {
        val vv = layoutInflater.inflate(R.layout.review_view, null)

        vv.tvContent.text = info.english
        vv.tvYuTi.text = "[${info.ipa}]"
        vv.tvWordYiSi.text = ""
        val eng = info.english_example
        val chn = info.chinese_example

        if (eng != null && eng != "null" && eng != "") {
            if (chn != null && chn != "null" && chn != "") {

                vv.tvLiFanYi.text = "$eng\n$chn"
            } else {
                vv.tvLiFanYi.text = eng
            }

        } else {
            vv.tvLiFanYi.text = ""
        }

        vv.linearSure.setOnClickListener {
            vv.reviewImageView.setImageResource(R.drawable.true_pic)
            vv.tvWordYiSi.text = info.chinese

            if (!isExist(finishWordList, info)) {
                val endTime = System.currentTimeMillis()
                info.time = ""
                finishWordList.add(info)
            }
            buttonNext.visibility = View.VISIBLE
        }

        vv.linearError.setOnClickListener {
            vv.reviewImageView.setImageResource(R.drawable.forget)
            vv.tvWordYiSi.text = info.chinese

            firstArr.add(info)

            addOneView(info)

            firstAdapter.notifyDataSetChanged()

            buttonNext.visibility = View.VISIBLE

        }

        vv.linearAgain.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)

        }

        vv.imagePlay.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }

        vList.add(vv)

    }

    private fun isExist(finishWordList: ArrayList<BookInfo>, info: BookInfo): Boolean {
        for (temp in finishWordList) {
            if (temp.id == info.id) {
                return true
            }
        }
        return false

    }

    val firstAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int = vList.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(vList[position])
            return vList[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(vList[position])
        }

    }


    private fun toNextOne() {
        voiceFilePath = null
        indexView++
        indexTime = 10
        if (indexView == firstArr.size) {
            getTwoList()
        } else {
            reviewViewPager.currentItem = indexView

            initTime()
        }
    }


    private fun toThreeOne() {
        indexView++
        if (indexView == twoArr.size) {
            getThreeList()
        } else {
            reviewViewPager.currentItem = indexView
        }

    }

    /**
     * 初始化第二阶段
     */
    private fun initTwoList() {

        vList.clear()

        val str = "${rfInfo!!.unit_name} 第二阶段"
        tvTopName.text = str
        indexView = 0

        for (i in 0 until twoArr.size) {
            val twoView = layoutInflater.inflate(R.layout.review_two_view, null)
            val info = twoArr[i]
            twoView.tvTwoContent.text = info.english
            twoView.tvTwoYuTi.text = "[${info.ipa}]"
            twoView.tvTwoWordYiSi.text = info.chinese
            val eng = info.english_example
            val chn = info.chinese_example

            if (eng != null && eng != "null" && eng != "") {
                if (chn != null && chn != "null" && chn != "") {

                    twoView.tvTwoLiFanYi.text = "$eng\n$chn"
                } else {
                    twoView.tvTwoLiFanYi.text = eng
                }

            } else {
                twoView.tvTwoLiFanYi.text = ""
            }


            twoView.imageTwoPlay.setOnClickListener {
                playVoice(activity!!, Config.IP + info.video)

            }

            twoView.linearTwoSure.setOnClickListener {
                val number = reviewViewPager.currentItem

                if (number == twoArr.size - 1) {
                    showThreeDialog()
                } else {
                    indexView++
                    reviewViewPager.currentItem = indexView
                }

            }

            twoView.linearTwoError.setOnClickListener {
                val number = reviewViewPager.currentItem

                if (number == twoArr.size - 1) {
                    showThreeDialog()
                } else {
                    indexView++
                    reviewViewPager.currentItem = indexView
                }

            }

            twoView.linearTwoAgain.setOnClickListener {
                playVoice(activity!!, Config.IP + info.video)
            }

            vList.add(twoView)

        }

        reviewViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = vList.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(vList[position])
                return vList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(vList[position])
            }

        }
    }

    /**
     * 初始化第三阶段
     */
    private fun initThreeList() {
        vList.clear()

        val str = "${rfInfo!!.unit_name} 第三阶段"
        tvTopName.text = str
        indexView = 0

        for (i in 0 until threeArr.size) {
            val info = threeArr[i]

            val threeView = layoutInflater.inflate(R.layout.review_three_view, null)

            threeView.tvThreeYuTi.text = "[${info.ipa}]"
            threeView.tvThreeWordYiSi.text = info.chinese
            threeView.inPutEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    inputStr = threeView.inPutEditText.text.toString()
                }

            })


            threeView.imageThreePlay.setOnClickListener {
                playVoice(activity!!, Config.IP + info.video)
            }

            vList.add(threeView)

        }

        reviewViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = vList.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(vList[position])
                return vList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(vList[position])
            }

        }

    }
}