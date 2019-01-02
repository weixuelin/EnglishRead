package com.wt.yc.englishread.main.fragment.mainpage

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.MainActivity
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.BookRackContentAdapter
import com.wt.yc.englishread.main.adapter.BookRackTitleAdapter
import com.xin.lv.yang.utils.view.AdTextView
import kotlinx.android.synthetic.main.book_rack_fragment.*

class BookRackFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.book_rack_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initTitleAdapter()
        initContentAdapter()
    }


    val contentList = arrayListOf<String>("测试一", "测试二", "测试三", "测试四")

    private fun initContentAdapter() {
        bookContentRecyclerView.layoutManager = GridLayoutManager(activity, 5)
        val adapter = BookRackContentAdapter(activity!!, contentList)
        bookContentRecyclerView.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                (activity as MainPageActivity).toWhere(Constant.MAIN_STADUY_CODE, null)

            }

            override fun onLongClick(position: Int) {

            }

        }

    }

    val titleList = arrayListOf<Info>()

    private fun initTitleAdapter() {
        for (i in 0..4) {
            val info = Info()
            info.click = i == 0

            titleList.add(info)
        }

        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.HORIZONTAL
        bookTitleRecyclerView.layoutManager = manager
        val adapter = BookRackTitleAdapter(activity!!, titleList)
        bookTitleRecyclerView.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                adapter.changeClick(position)
            }

            override fun onLongClick(position: Int) {

            }

        }
    }
}