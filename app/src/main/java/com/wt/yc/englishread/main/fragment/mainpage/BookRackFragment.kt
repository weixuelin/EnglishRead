package com.wt.yc.englishread.main.fragment.mainpage

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.MainActivity
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.BookRackContentAdapter
import com.wt.yc.englishread.main.adapter.BookRackTitleAdapter
import com.xin.lv.yang.utils.utils.HttpUtils
import com.xin.lv.yang.utils.view.AdTextView
import kotlinx.android.synthetic.main.book_rack_fragment.*
import org.json.JSONObject

class BookRackFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.GET_BOOK_CODE -> {
                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)
                if (status) {
                    val data = json.optJSONObject(Config.DATA)
                    val classify = data.optString("book_classify")
                    if (classify != "") {
                        val classifyArr: ArrayList<Info> = gson!!.fromJson(classify, object : TypeToken<ArrayList<Info>>() {}.type)

                        for (i in classifyArr.indices) {
                            val temp = classifyArr[i]
                            temp.click = i == 0
                        }

                        this.titleArr = classifyArr

                        titleAdapter.updateDataClear(classifyArr)


                    }

                    val bookStr = data.optString("book")

                    if (bookStr != "") {

                        val bookInfoArr: ArrayList<Info> = gson!!.fromJson(bookStr, object : TypeToken<ArrayList<Info>>() {}.type)

                        this.bookInfoArr = bookInfoArr

                        chooseContent(0, bookInfoArr)


                    }

                }
            }
        }

    }


    var titleArr: ArrayList<Info>? = null
    var bookInfoArr: ArrayList<Info>? = null

    private fun chooseContent(i: Int, bookInfoArr: ArrayList<Info>) {

        val titleInfo = titleArr!![i]
        val list = arrayListOf<Info>()

        for (temp in bookInfoArr) {
            if (titleInfo.id == temp.classify_id) {
                list.add(temp)
            }
        }
        contentAdapter.updateDataClear(list)

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.book_rack_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initTitleAdapter()
        initContentAdapter()

        getBookList()
    }

    private fun getBookList() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_BOOK_LIST, json.toString(), Config.GET_BOOK_CODE, handler)
    }


    val contentList = arrayListOf<Info>()
    lateinit var contentAdapter: BookRackContentAdapter

    private fun initContentAdapter() {
        bookContentRecyclerView.layoutManager = GridLayoutManager(activity, 5)
        contentAdapter = BookRackContentAdapter(activity!!, contentList)
        bookContentRecyclerView.adapter = contentAdapter
        contentAdapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {

//                val infof = contentList[position]
//                val bookInfo = BookInfo()
//                bookInfo.id = infof.id
//                bookInfo.book_name = infof.book_name
//                (activity as MainPageActivity).toWhere(Constant.MAIN_STADUY_CODE, bookInfo)

            }

            override fun onLongClick(position: Int) {

            }

        }

    }

    val titleList = arrayListOf<Info>()

    lateinit var titleAdapter: BookRackTitleAdapter

    private fun initTitleAdapter() {

        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        bookTitleRecyclerView.layoutManager = manager
        titleAdapter = BookRackTitleAdapter(activity!!, titleList)
        bookTitleRecyclerView.adapter = titleAdapter
        titleAdapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                titleAdapter.changeClick(position)

                contentList.clear()
                contentAdapter.notifyDataSetChanged()

                chooseContent(position, bookInfoArr!!)
            }

            override fun onLongClick(position: Int) {

            }

        }
    }
}