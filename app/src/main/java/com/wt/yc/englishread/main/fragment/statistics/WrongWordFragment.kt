package com.wt.yc.englishread.main.fragment.statistics

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.main.adapter.WrongWordAdapter
import kotlinx.android.synthetic.main.wrong_word_fragment.*

/**
 * 高频错词
 */
class WrongWordFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.wrong_word_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        wrongWordRecyclerView.layoutManager = LinearLayoutManager(activity)
        wrongWordRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))

        wrongWordRecyclerView.adapter = WrongWordAdapter(activity!!, arrayListOf("", ""))

    }
}