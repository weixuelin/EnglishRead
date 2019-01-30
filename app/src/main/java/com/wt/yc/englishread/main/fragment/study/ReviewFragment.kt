package com.wt.yc.englishread.main.fragment.study

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.InputFilter
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
import kotlin.collections.HashMap


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

                    if (tvTextTime != null) {
                        tvTextTime.text = "0"
                    }

                } else {
                    if (tvTextTime != null) {
                        tvTextTime.text = indexTime.toString()
                    }

                }
            }

            Config.REVIRE_CODE -> {
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
                            showToastShort(activity!!, "暂无学习单次，请重新开始学习!!")
                        }

                    }

                }
            }

            Config.FINISH_CODE -> {

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
     * 今日目标完成弹出dialog
     */
    private fun showFinish(num: Int, gold: Int) {

        val view = layoutInflater.inflate(R.layout.finish_dialog, null)
        val textViewTi = view.textViewTi
        textViewTi.text = "学习单词数${num}\n获得金币${gold}"

        val dialog: Dialog = Dialog(activity, R.style.style)
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

        tvTopTitle.text = "复习"

        if (rfInfo != null) {
            tvTopName.text = "${rfInfo!!.unit_name} 第一阶段"
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

        firstArr.clear()

        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)

        json.put("id", if (rfInfo!!.code == 1) {
            rfInfo!!.book_id
        } else {
            rfInfo!!.id
        })

        json.put("type", if (rfInfo!!.code == 1) {
            "go_on"
        } else {
            "new_word"
        })

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
        initTime()

    }

    var timer: Timer? = null
    var timerTask: TimerTask? = null

    /**
     * 当前定时时间
     */
    var indexTime = 10


    private fun initTime() {
        indexTime = 10

        if (timer != null) {
            timer!!.cancel()
        }

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
        oneIndexNum = 0
        indexCode = 2

        initTwoList()

    }

    /**
     * 获取第三阶段的测试信息
     */
    private fun getThreeList() {

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

                    isFirstClickError = false

                    if (oneIndexNum == firstArr.size - 1) {

                        showTiDialog()

                        timer!!.cancel()

                    } else {

                        oneIndexNum++

                        addOneView(firstArr[oneIndexNum])

                        initTime()
                        buttonNext.visibility = View.GONE

                    }
                }

                2 -> {

                    if (oneIndexNum == twoArr.size - 1) {

                        showThreeDialog()

                    } else {

                        oneIndexNum++

                        addTwo(twoArr[oneIndexNum])
                        initTime()
                    }
                }

                3 -> {

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

                            val endTime = 10 - indexTime
                            info.time = endTime.toString()

                            threeHashMap.add(info)

                            oneIndexNum++

                            addThree(threeArr[oneIndexNum])

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
     * 提示是否保存
     */
    private fun showSave() {

        val build = AlertDialog.Builder(activity!!)
        build.setMessage("复习结束，是否保存？？").setPositiveButton("保存") { dialog, _ ->
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

    }


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
     * 计数
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

    private fun addOneView(info: BookInfo) {

        reviewLinearLayout.removeAllViews()
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

            vv.reviewImageView.setImageResource(R.drawable.icon_true)
            vv.tvWordYiSi.text = info.chinese

            val endTime = 10 - indexTime
            info.time = endTime.toString()
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

            buttonNext.visibility = View.VISIBLE

        }

        vv.linearError.setOnClickListener {
            vv.reviewImageView.setImageResource(R.drawable.icon_onremenber)
            vv.tvWordYiSi.text = info.chinese

            isFirstClickError = true

            val endTime = 10 - indexTime
            info.time = endTime.toString()
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

            buttonNext.visibility = View.VISIBLE

        }

        vv.linearAgain.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)

        }

        vv.imagePlay.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }

        reviewLinearLayout.addView(vv)

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

            Log.i("result","点击到此--------")

            twoView.reviewTwoImageView.setImageResource(R.drawable.icon_true)


        }


        twoView.linearTwoError.setOnClickListener {

            twoView.reviewTwoImageView.setImageResource(R.drawable.icon_onremenber)

        }

        twoView.linearTwoAgain.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }


        reviewLinearLayout.addView(twoView)


    }

    /**
     * 初始化第三阶段
     */
    private fun initThreeList() {

        val str = "${rfInfo!!.unit_name} 第三阶段"
        tvTopName.text = str

        oneIndexNum = 0

        addThree(threeArr[0])

    }


    /**
     * 添加第三阶段的数据
     */
    fun addThree(info: BookInfo) {

        inputStr = ""

        initTime()

        reviewLinearLayout.removeAllViews()

        val threeView = layoutInflater.inflate(R.layout.review_three_view, null)

        val english=info.english

        threeView.inPutEditText.textLength = english.length

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
            }

        })


        threeView.imageThreePlay.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }

        reviewLinearLayout.addView(threeView)

    }
}