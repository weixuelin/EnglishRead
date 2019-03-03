package com.wt.yc.englishread.main.fragment.study

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Message
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.finish_dialog.view.*
import kotlinx.android.synthetic.main.review_fragment.*
import kotlinx.android.synthetic.main.review_three_view.view.*
import kotlinx.android.synthetic.main.review_two_view.view.*
import kotlinx.android.synthetic.main.review_view.view.*
import kotlinx.android.synthetic.main.study_head.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


/**
 * 学习与复习
 */
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

        when (msg.what) {

            9999 -> {

                Log.i("result", "---------")

                indexTime++

                if (tvTextTime != null) {
                    tvTextTime.text = msg.arg1.toString()
                }

            }

            Config.REVIRE_CODE -> {

                val str = msg.obj as String

                removeLoadDialog()

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

                    if (firstArr != null && firstArr.size != 0) {
                        getOneList()
                    } else {
                        if (isVisibleCode) {
                            showToastShort(activity!!, "暂无学习单词，请重新开始学习!!")
                        }
                    }

                } else {
                    if (isVisibleCode) {
                        showToastShort(activity!!, "暂无学习单词!!")
                    }
                }
            }

            Config.FINISH_CODE -> {
                val str = msg.obj as String
                removeLoadDialog()
                val json = JSONObject(str)

                if (json != null) {

                    val num = json.optInt("num")
                    val gold = json.optInt("gold")

                    showFinish(num, gold)

                }

            }
        }
    }


    /**
     * 今日目标完成弹出
     */
    private fun showFinish(num: Int, gold: Int) {

        val view = layoutInflater.inflate(R.layout.finish_dialog, null)
        val textViewTi = view.textViewTi

        val ss = "学习单词数$num\n获得金币$gold"

        textViewTi.text = ss

        val dialog = Dialog(activity, R.style.style)

        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
        setAlpha(activity!!, 0.6f)

        view.imageClose.setOnClickListener {
            dialog.dismiss()
            setAlpha(activity!!, 1f)

            (activity!! as MainPageActivity).backTo()
        }

        view.buttonFinish.setOnClickListener {
            dialog.dismiss()
            setAlpha(activity!!, 1f)

            (activity!! as MainPageActivity).backTo()
        }


        dialog.setOnDismissListener {
            setAlpha(activity!!, 1f)
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

        if (rfInfo != null) {

            if (rfInfo!!.code == 1) {
                tvTopTitle.text = "学习"
                tvTopName.text = "${rfInfo!!.unit_name}"
            } else {
                tvTopTitle.text = "复习"
                tvTopName.text = "${rfInfo!!.unit_name} 第一阶段"
            }

            get()

        }

        initClick()

    }


    var isVisibleCode = false

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleCode = isVisibleToUser
    }


    override fun onStop() {
        super.onStop()
        if (timer != null) {
            timer!!.cancel()
        }
    }

    fun get() {

        Log.i("reuslt", "-------" + rfInfo!!.id)
        firstArr.clear()

        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("id", rfInfo!!.id)
        json.put("type", "new_word")
        HttpUtils.getInstance().postJson(Config.REVIEW_WORD_URL, json.toString(), Config.REVIRE_CODE, handler)
        showLoadDialog(activity!!, "获取中")
    }

    /**
     * 获取第一阶段的测试信息
     */
    private fun getOneList() {
        twoArr.clear()
        threeArr.clear()

        twoArr.addAll(firstArr)
        threeArr.addAll(firstArr)

        oneIndexNum = 0

        initFirstList()

    }

    var timer: Timer? = null
    var timerTask: TimerTask? = null

    /**
     * 当前定时时间
     */
    var indexTime = 0


    private fun initTime() {

        tvTextTime.visibility = View.VISIBLE

        indexTime = 0

        if (timer != null) {
            timer!!.cancel()
            timer = null

        }

        timer = Timer()

        timerTask = object : TimerTask() {
            override fun run() {
                val message = handler!!.obtainMessage()
                message.what = 9999
                message.obj = ""
                message.arg1 = 25 - indexTime
                handler!!.sendMessage(message)
            }
        }

        timer!!.schedule(timerTask, 0, 1000)
    }

    /**
     * 获取第二阶段的测试信息
     */
    private fun getTwoList() {
        remberClickNum = 1
        forgetClickNum = 1
        oneIndexNum = 0
        indexCode = 2

        initTwoList()

    }

    /**
     * 获取第三阶段的测试信息
     */
    private fun getThreeList() {
        remberClickNum = 1
        forgetClickNum = 1

        oneIndexNum = 0

        indexCode = 3

        initThreeList()

    }

    /**
     * 第一阶段答案
     */
    val finishWordList: ArrayList<BookInfo> = arrayListOf()

    /**
     * 第三阶段答案
     */
    val threeHashMap: ArrayList<BookInfo> = arrayListOf()

    var firstArr = arrayListOf<BookInfo>()

    val twoArr = arrayListOf<BookInfo>()

    val threeArr = arrayListOf<BookInfo>()


    var indexCode = 1

    /**
     * 输入的单词
     */
    var inputStr: String = ""

    private fun initClick() {

        buttonNext.setOnClickListener {
            when (indexCode) {
                1 -> {
                    addFirstTi()

                }

                2 -> {
                    addTwoDataTi()

                }

                3 -> {

                    addThreeDataTi()

                }
            }
        }

        tvFinishBack.setOnClickListener {
            (activity!! as MainPageActivity).backTo()
        }
    }

    /**
     * 第三阶段数据带提示
     */
    private fun addThreeDataTi() {
        if (oneIndexNum == threeArr.size - 1) {
            // 提交数据
            showSave()

        } else {

            if (inputStr != "") {

                val info = threeArr[oneIndexNum]

                Log.i("result", "------" + info.english + "------" + inputStr)

                if (info.english.trim() == inputStr.trim()) {

                    info.status = 1

                } else {
                    info.status = 0
                    threeArr.add(info)
                }

                info.time = indexTime.toString()

                threeHashMap.add(info)

                oneIndexNum++

                addThree(threeArr[oneIndexNum])

            } else {
                showShortToast(activity!!, "请输入单词")
            }
        }
    }

    /**
     * 加载第一阶段数据
     */
    private fun addFirstTi() {

        isFirstClickError = false

        if (oneIndexNum == firstArr.size - 1) {

            showTiDialog()
            timer!!.cancel()

        } else {

            oneIndexNum++

            addOneView(firstArr[oneIndexNum])

            buttonNext.visibility = View.GONE

        }
    }

    /**
     * 第二阶段数据加载提示
     */
    fun addTwoDataTi() {
        if (oneIndexNum == twoArr.size - 1) {

            showThreeDialog()

        } else {

            oneIndexNum++

            addTwo(twoArr[oneIndexNum])
        }
    }

    /**
     * 提示学习完成
     */
    private fun showFinishTiDialog() {
        val build = AlertDialog.Builder(activity)
        build.setMessage("已学习完成!!").setTitle("提示").setPositiveButton("确定") { dialog, _ ->
            dialog.dismiss()
            (activity as MainPageActivity).supportFragmentManager.popBackStack()
        }.show()

    }


    /**
     * 提示是否保存
     */
    private fun showSave() {
        val message = if (rfInfo!!.code == 1) {
            "学习结束，是否保存？？"
        } else {
            "复习结束，是否保存？？"
        }

        val build = AlertDialog.Builder(activity!!)
        build.setMessage(message).setPositiveButton("保存") { dialog, _ ->
            save()
            dialog.dismiss()
        }.setNegativeButton("取消") { _, _ ->
            (activity!! as MainPageActivity).backTo()
        }.show()

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
        showLoadDialog(activity!!, "提交中")

        if (timer != null) {
            timer!!.cancel()
        }

    }


    /**
     * 创建保存数据
     */
    private fun buildList(): JSONArray? {

        val arrList1 = JSONArray()
        val arrList2 = JSONArray()

        for (temp in finishWordList) {
            val json = JSONObject()
            json.put("word_id", temp.id.toString())
            json.put("status", temp.status.toString())
            json.put("time", temp.time)
            json.put("unit_id", if (rfInfo!!.code == 1) {
                rfInfo!!.book_id.toString()
            } else {
                rfInfo!!.id.toString()
            })

            arrList1.put(json)

        }

        for (temp in threeHashMap) {
            val json = JSONObject()
            json.put("word_id", temp.id.toString())
            json.put("status", temp.status.toString())
            json.put("time", temp.time)
            json.put("unit_id", if (rfInfo!!.code == 1) {
                rfInfo!!.book_id.toString()
            } else {
                rfInfo!!.id.toString()
            })

            arrList2.put(json)

        }

        val jsArr = JSONArray()

        jsArr.put(arrList1)
        jsArr.put(arrList2)

        return jsArr

    }


    private fun showThreeDialog() {

        val builder = AlertDialog.Builder(activity!!)

        builder.setMessage("进入第三阶段，识记阶段").setTitle("提示")
                .setPositiveButton("确定") { _, _ ->

                    getThreeList()

                }.show()


    }


    private fun showTiDialog() {

        val builder = AlertDialog.Builder(activity!!)

        builder.setMessage("进入第二阶段，逆向思维阶段").setTitle("提示")
                .setPositiveButton("确定") { _, _ ->

                    getTwoList()

                }.show()

    }

    /**
     * 计数,集合信息计数
     */
    var oneIndexNum = 0

    /**
     * 初始化第一阶段
     */
    private fun initFirstList() {
        oneIndexNum = 0

        addOneView(firstArr[oneIndexNum])

    }

    /**
     * 第一次是否点击不记得
     */
    var isFirstClickError = false

    /**
     * 记得点击次数
     */
    var remberClickNum = 1
    /**
     * 忘记点击次数
     */
    var forgetClickNum = 1

    private fun addOneView(info: BookInfo) {
        buttonNext.visibility = View.GONE

        reviewLinearLayout.removeAllViews()
        val vv = layoutInflater.inflate(R.layout.review_view, null)

        vv.tvContent.text = info.english
        vv.tvYuTi.text = "[${info.ipa}]"
        vv.tvWordYiSi.text = "是否记得??"

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
            if (remberClickNum == 1) {

                vv.reviewImageView.setImageResource(R.drawable.icon_true)
                vv.tvWordYiSi.text = info.chinese

                info.time = indexTime.toString()

                info.status = 1

                val index = isExist(finishWordList, info)

                if (index != -1) {

                    ///  存在
                    finishWordList.removeAt(index)

                    finishWordList.add(index, info)

                    if (isFirstClickError) {

                        firstArr.removeAt(firstArr.size - 1)

                        isFirstClickError = false

                    }

                } else {
                    /// 不存在
                    finishWordList.add(info)
                }
                remberClickNum++

            } else {

                remberClickNum = 1

                addFirstTi()
            }

            buttonNext.visibility = View.GONE

        }

        vv.linearError.setOnClickListener {
            if (forgetClickNum == 1) {
                vv.reviewImageView.setImageResource(R.drawable.icon_onremenber)
                vv.tvWordYiSi.text = info.chinese

                isFirstClickError = true

                info.time = indexTime.toString()
                info.status = 0

                val index = isExist(finishWordList, info)

                if (index != -1) {
                    ///  存在改变数据
                    finishWordList.removeAt(index)
                    finishWordList.add(index, info)
                } else {
                    finishWordList.add(info)
                }

                firstArr.add(info)
                forgetClickNum++

            } else {

                forgetClickNum = 1

                addFirstTi()

            }

            buttonNext.visibility = View.GONE

        }

        vv.linearAgain.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)

        }

        vv.imagePlay.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }

        reviewLinearLayout.addView(vv)

        playVoice(activity!!, Config.IP + info.video)

        initTime()

    }

    private fun isExist(finishWordList: ArrayList<BookInfo>, info: BookInfo): Int {
        for (i in finishWordList.indices) {
            val temp = finishWordList[i]
            if (temp.id == info.id) {
                return i
            }
        }
        return -1

    }


    /**
     * 初始化第二阶段
     */
    private fun initTwoList() {

        val str = "${rfInfo!!.unit_name} 第二阶段"
        tvTopName.text = str

        addTwo(twoArr[oneIndexNum])

    }

    /**
     * 添加第二阶段的数据
     */
    fun addTwo(info: BookInfo) {

        reviewLinearLayout.removeAllViews()

        val twoView = layoutInflater.inflate(R.layout.review_two_view, null)

        twoView.tvTwoContent.text = info.english
        twoView.tvTwoYuTi.text = "[${info.ipa}]"
        twoView.tvTwoWordYiSi.text = info.chinese

        twoView.tvTwoContent.visibility = View.GONE

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

            Log.i("result", "点击到此--------")
            if (remberClickNum == 1) {
                twoView.reviewTwoImageView.setImageResource(R.drawable.icon_true)
                twoView.tvTwoContent.visibility = View.VISIBLE
                remberClickNum++
            } else {
                remberClickNum = 1
                addTwoDataTi()
            }

        }

        twoView.linearTwoError.setOnClickListener {
            if (forgetClickNum == 1) {

                twoView.reviewTwoImageView.setImageResource(R.drawable.icon_onremenber)
                twoView.tvTwoContent.visibility = View.VISIBLE
                forgetClickNum++
            } else {
                forgetClickNum = 1
                addTwoDataTi()
            }

        }

        twoView.linearTwoAgain.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }


        reviewLinearLayout.addView(twoView)

        initTime()

        playVoice(activity!!, Config.IP + info.video)

    }

    /**
     * 初始化第三阶段
     */
    private fun initThreeList() {

        val str = "${rfInfo!!.unit_name} 第三阶段"
        tvTopName.text = str

        oneIndexNum = 0

        addThree(threeArr[oneIndexNum])

    }


    /**
     * 第几次做题  1 为正常做题  2 为罚题  3 为再次罚题
     */
    var threeAnswerNum = 1

    /**
     * 罚题次数
     */
    var faAnswerNum = 1
    /**
     * 再次罚题次数
     */
    var faAgainNum = 0

    /**
     * 罚题做错次数
     */
    var faTiTrueNum = 0

    /**
     * 添加第三阶段的数据
     */
    fun addThree(info: BookInfo) {

        inputStr = ""

        reviewLinearLayout.removeAllViews()

        val threeView = layoutInflater.inflate(R.layout.review_three_view, null)

        val english = info.english

        Log.i("result", "========" + english.length)

        val len = english.length

        threeView.inPutEditText.layoutParams = LinearLayout.LayoutParams(english.length * 100, LinearLayout.LayoutParams.WRAP_CONTENT)

        threeView.inPutEditText.filters = arrayOf(InputFilter.LengthFilter(english.length))

        threeView.tvThreeYuTi.text = "[${info.ipa}]"
        threeView.tvThreeWordYiSi.text = info.chinese

        threeView.inPutEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                inputStr = threeView.inPutEditText.text.toString()

                if (inputStr.length == len) {
                    checkTrueOrFalse(threeView.inPutEditText, english, inputStr)
                }
            }

        })


        threeView.imageThreePlay.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }

        reviewLinearLayout.addView(threeView)

        initTime()

        playVoice(activity!!, Config.IP + info.video)

    }

    /**
     * 检查正确和错误
     */
    private fun checkTrueOrFalse(editText: EditText, english: String, inputStr: String) {
        when (threeAnswerNum) {
            1 -> {
                if (english == inputStr) {

                    buttonNext.visibility = View.VISIBLE

                    showTrueTi()

                } else {

                    buttonNext.visibility = View.GONE

                    val ll = chooseDiffIndex(english, inputStr)

                    val sp = SpannableString(english)

                    for (temp in ll) {

                        sp.setSpan(ForegroundColorSpan(Color.RED), temp, temp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    editText.setText(sp, TextView.BufferType.SPANNABLE)

                    threeAnswerNum = 2

                    timeToAgain(editText)

                }
            }

            2 -> {
                if (faAnswerNum == 3) {
                    // 判断罚题做错次数
                    if (faTiTrueNum == 0) {
                        buttonNext.visibility = View.VISIBLE
                    } else {
                        threeAnswerNum = 3
                        faAgainNum = faTiTrueNum
                    }

                } else {

                    if (english == inputStr) {

                        showTrueTi()

                    } else {

                        faTiTrueNum++

                        val ll = chooseDiffIndex(english, inputStr)

                        val sp = SpannableString(english)

                        for (temp in ll) {

                            sp.setSpan(ForegroundColorSpan(Color.RED), temp, temp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        }

                        editText.setText(sp, TextView.BufferType.SPANNABLE)
                    }

                    timeToAgain(editText)
                    faAnswerNum++
                }
            }

            3 -> {

                buttonNext.visibility = View.VISIBLE

                if (english == inputStr) {

                    showTrueTi()

                } else {

                    val ll = chooseDiffIndex(english, inputStr)

                    val sp = SpannableString(english)

                    for (temp in ll) {

                        sp.setSpan(ForegroundColorSpan(Color.RED), temp, temp + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }

                    editText.setText(sp, TextView.BufferType.SPANNABLE)
                }

            }
        }


    }

    /**
     * 1s后重新输入
     */

    private fun timeToAgain(editText:EditText) {
      handler!!.postDelayed({
          editText.setText("")
          editText.hint = "请重新输入"
      },1000)

    }


    /**
     * 显示正确提示
     */
    private fun showTrueTi() {

    }

    /**
     * 找出不同的位置
     */
    private fun chooseDiffIndex(english: String, inputStr: String): ArrayList<Int> {
        val list = arrayListOf<Int>()
        val charList1 = english.toCharArray()
        val charList2 = inputStr.toCharArray()
        for (i in charList1.indices) {
            val cc = charList1[i]
            for (j in charList2.indices) {
                val ccc = charList2[j]
                if (cc != ccc) {
                    list.add(j)
                }
            }
        }
        return list

    }
}