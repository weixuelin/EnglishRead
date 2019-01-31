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
import kotlinx.android.synthetic.main.listen_tead_fragment.*
import kotlinx.android.synthetic.main.study_head.*
import org.json.JSONObject

class ListenReadFragment : ProV4Fragment() {


    override fun handler(msg: Message) {
        val str=msg.obj as String
        when(msg.what){
            Config.REVIRE_CODE -> {

                removeLoadDialog()

                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                if (code == Config.SUCCESS) {
                    val data = json.optJSONObject(Config.DATA)

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

    private fun initData() {
        val info=firstArr[index]
        tvTextEnglish.text=info.english
        tvChinese.text=info.chinese

    }

    lateinit var firstArr:ArrayList<BookInfo>

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.listen_tead_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTopTitle.text = "听读训练"
        tvTopName.text = ""
        initClick()

        get()
    }

    var index=0

    fun get() {
        index=0

        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)

        json.put("id", if (info!!.code == 1) {
            info!!.book_id
        } else {
            info!!.id
        })

        json.put("type", if (info!!.code == 1) {
            "go_on"
        } else {
            "new_word"
        })

        HttpUtils.getInstance().postJson(Config.REVIEW_WORD_URL, json.toString(), Config.REVIRE_CODE, handler)
        showLoadDialog(activity!!, "获取中")
    }


    var info:BookInfo?=null

    private fun initClick() {
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        imagePlay.setOnClickListener {
            val info=firstArr[index]
            playVoice(activity!!,Config.IP+info.video)
        }

        imageBack.setOnClickListener {
            if(index!=0){
                index--
                initData()
            }else{
                showToastShort(activity!!,"已到第一个!!")
            }
        }

        imageNext.setOnClickListener {

            if(index!=firstArr.size-1){

                index++

                initData()
            }else{
                showToastShort(activity!!,"已到最后一个!!")
            }
        }


    }

    override fun onResume() {
        super.onResume()

    }
}