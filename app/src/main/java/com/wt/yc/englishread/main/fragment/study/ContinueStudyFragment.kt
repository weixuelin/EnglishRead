package com.wt.yc.englishread.main.fragment.study

import android.app.AlertDialog
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.base.Share
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.review_fragment.*
import kotlinx.android.synthetic.main.review_view.view.*
import kotlinx.android.synthetic.main.study_head.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * 继续学习
 */
class ContinueStudyFragment : ProV4Fragment() {

    var bookInfo: BookInfo? = null

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

                indexTime++

                if (tvTextTime != null) {
                    tvTextTime.text = indexTime.toString()
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

                        val firstArr: ArrayList<BookInfo> = gson!!.fromJson(wordResult, object : TypeToken<ArrayList<BookInfo>>() {}.type)

                        if (firstArr != null && firstArr.size != 0) {
                            getOneList(firstArr)
                        }
                    }

                }
            }
        }

    }

    var indexNum = 0
    var bookList: ArrayList<BookInfo>? = null

    private fun getOneList(firstArr: ArrayList<BookInfo>) {
        this.bookList = firstArr
        addOneView(firstArr[indexNum])

    }

    var timer: Timer? = null
    var timerTask: TimerTask? = null

    /**
     * 当前定时时间
     */
    var indexTime = 0


    /**
     * 第一阶段答案
     */
    val finishWordList: ArrayList<BookInfo> = arrayListOf()


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
                handler!!.sendMessage(message)
            }
        }

        timer!!.schedule(timerTask, 0, 1000)
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

            vv.reviewImageView.setImageResource(R.drawable.icon_true)
            vv.tvWordYiSi.text = info.chinese

            info.time = indexTime.toString()

            info.status = 1

            val index = isExist(finishWordList, info)

            if (index != -1) {

                ///  存在
                finishWordList.removeAt(index)

                finishWordList.add(index, info)


            } else {
                /// 不存在
                finishWordList.add(info)
            }

            buttonNext.visibility = View.VISIBLE

        }

        vv.linearError.setOnClickListener {
            vv.reviewImageView.setImageResource(R.drawable.icon_onremenber)
            vv.tvWordYiSi.text = info.chinese

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

            bookList!!.add(info)

            buttonNext.visibility = View.VISIBLE

        }

        vv.linearAgain.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)

        }

        vv.imagePlay.setOnClickListener {
            playVoice(activity!!, Config.IP + info.video)
        }

        playVoice(activity!!, Config.IP + info.video)

        reviewLinearLayout.addView(vv)
        initTime()


    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.review_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initClick()

        tvTopTitle.text = "学习   " + bookInfo!!.book_name

        val unitId = Share.getUnitId(activity!!)

        get(unitId)

    }

    fun get(unitId: Int) {

        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("id", unitId)
        json.put("type", "go_on")

        HttpUtils.getInstance().postJson(Config.REVIEW_WORD_URL, json.toString(), Config.REVIRE_CODE, handler)
        showLoadDialog(activity!!, "获取中")

    }

    var startTime: Long = 0

    private fun initClick() {
        tvFinishBack.setOnClickListener {
            (activity!! as MainPageActivity).backTo()
        }

        buttonNext.setOnClickListener {
            indexNum++

            if (indexNum == bookList!!.size) {

                showFinish()

            } else {

                addOneView(bookList!![indexNum])

                startTime = System.currentTimeMillis()
            }

        }

    }

    private fun showFinish() {

        val build = AlertDialog.Builder(activity!!)
        build.setTitle("提示").setMessage("已学习完成是否退出学习?").setPositiveButton("确定") { dialog, _ ->

            save()

            (activity as MainPageActivity).backTo()

        }.setNegativeButton("取消") { _, _ -> }.show()
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

        if (timer != null) {
            timer!!.cancel()
        }

    }


    /**
     * 创建保存数据
     */
    private fun buildList(): JSONArray? {

        val arrList1 = JSONArray()

        for (temp in finishWordList) {

            val json = JSONObject()
            json.put("word_id", temp.id.toString())
            json.put("status", temp.status.toString())
            json.put("time", temp.time)
            json.put("unit_id", bookInfo!!.id.toShort())

            arrList1.put(json)
        }


        val jsArr = JSONArray()
        jsArr.put(arrList1)

        return jsArr

    }
}