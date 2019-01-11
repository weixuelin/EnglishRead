package com.wt.yc.englishread.main.fragment.mainpage

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.main.adapter.WordAdapter
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.new_word_layout.*
import org.json.JSONObject

/**
 * 生词本
 */
class NewWordFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.GET_ALL_CODE -> {
                swipeRefreshView.isRefreshing = false
                removeLoadDialog()
                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)
                if (status) {
                    val data = json.optString(Config.DATA)
                    if (data != "") {
                        val arr: ArrayList<BookInfo> = gson!!.fromJson(data, object : TypeToken<ArrayList<BookInfo>>() {}.type)
                        if (arr.size != 0) {
                            chooseList(arr)
                        }

                    }
                }

            }
        }

    }

    val moshengList = arrayListOf<BookInfo>()
    val jiaShengList = arrayListOf<BookInfo>()

    private fun chooseList(arr: ArrayList<BookInfo>) {
        for (temp in arr) {
            if (temp.status == 0) {
                jiaShengList.add(temp)
            } else if (temp.status == 2) {
                moshengList.add(temp)
            }
        }

        adapter.updateDataClear(jiaShengList)

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.new_word_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()

        initClick()

        getAllWordList()

    }

    private fun getAllWordList() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_ALL_WORD_URL, json.toString(), Config.GET_ALL_CODE, handler)
        showLoadDialog(activity!!, "获取中")
    }

    var chooseCode = 1

    private fun initClick() {
        wordRadioGroup.setOnCheckedChangeListener { p0, p1 ->
            when (p1) {
                R.id.rb1 -> {
                    chooseCode = 1
                    adapter.updateDataClear(jiaShengList)

                }

                R.id.rb2 -> {
                    chooseCode = 2
                    adapter.updateDataClear(moshengList)
                }
            }


        }

        swipeRefreshView.setOnRefreshListener {
            getAllWordList()
        }

    }


    val wordList = arrayListOf<BookInfo>()
    lateinit var adapter: WordAdapter

    private fun initAdapter() {
        wordRecyclerView.layoutManager = GridLayoutManager(activity, 5)
        adapter = WordAdapter(activity!!, wordList)
        wordRecyclerView.adapter = adapter
        adapter.itemClickListener=object :ItemClickListener{
            override fun onItemClick(position: Int) {
                val info = wordList[position]
                playVoice(activity!!, Config.IP + info.video)
                showLoadDialog(activity!!, "播放中")
            }

            override fun onLongClick(position: Int) {

            }

        }

        adapter.onVoiceListener = object : WordAdapter.OnVoiceListener {
            override fun onVoice(position: Int) {

            }
        }
    }
}