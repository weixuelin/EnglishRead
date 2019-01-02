package com.wt.yc.englishread.main.fragment.mainpage

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.main.adapter.WordAdapter
import kotlinx.android.synthetic.main.new_word_layout.*

/**
 * 生词本
 */
class NewWordFragment : ProV4Fragment() {

    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.new_word_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()

        initClick()
    }

    var chooseCode = 1

    private fun initClick() {
        wordRadioGroup.setOnCheckedChangeListener { p0, p1 ->
            when (p1) {
                R.id.rb1 -> {
                    chooseCode = 1

                }

                R.id.rb2 -> {
                    chooseCode = 2

                }
            }

            getFromNet()
        }

        swipeRefreshView.setOnRefreshListener {

        }

        swipeRefreshView.onLoadMore(wordRecyclerView) {

        }
    }



    private fun getFromNet() {

    }

    val wordList = arrayListOf("", "", "", "", "", "", "", "", "")

    private fun initAdapter() {
        wordRecyclerView.layoutManager = GridLayoutManager(activity, 5)
        val adapter = WordAdapter(activity!!, wordList)
        wordRecyclerView.adapter = adapter

        adapter.onVoiceListener = object : WordAdapter.OnVoiceListener {
            override fun onVoice(position: Int) {

            }

        }
    }
}