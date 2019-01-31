package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.listen_write_fragment.*
import kotlinx.android.synthetic.main.study_head.*
import org.json.JSONObject

class ListenWriteFragment : ProV4Fragment() {
    override fun handler(msg: Message) {
        val str=msg.obj as String
        when(msg.what){
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

                    if (firstArr != null && firstArr!!.size != 0) {
                        initData()
                    } else {
                        showToastShort(activity!!, "暂无学习单词，请重新开始学习!!")

                    }

                }
            }
        }

    }

    var index=0


    private fun initData() {
        writingTextView.setText("")

        val info=firstArr!![index]

        tvTextChinese.text=info.chinese
        tvCiTi.text=info.ipa

    }


    var firstArr:ArrayList<BookInfo>?=null

    var unitId:String=""
    var muId:String=""

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.listen_write_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTopTitle.text = "听写"
        tvTopName.text = ""


        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        tvSure.setOnClickListener {
            // 重新构建数据
        }

        get()

        initClick()


    }

    private fun initClick() {

        tvListenerAgain.setOnClickListener {
            val info=firstArr!![index]
            playVoice(activity!!,Config.IP+info.video)

        }

        tvSure.setOnClickListener {
            val text=writingTextView.text.toString()
            val info=firstArr!![index]
            if(text!=""&&text==info.english){

                if(index!=firstArr!!.size-1){

                    index++

                    initData()
                }else{
                    showToastShort(activity!!,"已到最后一个单词")
                }

            }else{
                showToastShort(activity!!,"请输入正确的英语")
            }

        }
    }

    var rfInfo:BookInfo?=null


    fun get() {
        index=0

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
}