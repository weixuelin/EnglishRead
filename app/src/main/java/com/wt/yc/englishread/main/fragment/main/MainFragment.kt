package com.wt.yc.englishread.main.fragment.main

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.StudentAdapter
import kotlinx.android.synthetic.main.main_fragment_layout.*

/**
 * 首页fragment
 */
class MainFragment : ProV4Fragment() {

    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment_layout, container, false)
    }

    val picList = arrayListOf("https://i01piccdn.sogoucdn.com/5b328b8a33658654","https://i04piccdn.sogoucdn.com/0b7aaf8556ce2bbe")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initStudentAdapter()

        initClick()

        initViewPager(activity!!, picViewPager, picList, handler!!, 2)

    }

    private fun initClick() {
        linearLayout1.setOnClickListener {
            startActivity(Intent(activity!!, MainPageActivity::class.java))
        }

    }

    private fun initStudentAdapter() {
        studentRecyclerView.isNestedScrollingEnabled = false
        studentRecyclerView.layoutManager = GridLayoutManager(activity, 4)
        val adapter = StudentAdapter(activity!!, arrayListOf<String>("", "", "", "", "", ""))
        studentRecyclerView.adapter = adapter
        adapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onLongClick(position: Int) {

            }

        }


    }
}