package com.wt.yc.englishread.main.activity

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProActivity
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.adapter.BookRackContentAdapter
import kotlinx.android.synthetic.main.choose_book_layout.*

class ChooseBookActivity : ProActivity() {

    override fun handler(msg: Message) {

    }

    val list = arrayListOf<Info>(Info(), Info(), Info())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(true)

        setContentView(R.layout.choose_book_layout)

        recyclerView.layoutManager = GridLayoutManager(this, 5)

        val adapter = BookRackContentAdapter(this, list)

        recyclerView.adapter = adapter

        adapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onLongClick(position: Int) {

            }

        }
    }


}