package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.main.adapter.ListenChooseAdapter
import kotlinx.android.synthetic.main.listen_choose_layout.*
import kotlinx.android.synthetic.main.study_head.*

/**
 * 听写 选择
 */
class ListenChooseFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    var code = 1

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.listen_choose_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        when (code) {
            1 -> {
                tvTextTi.text = "听选"
            }
            2 -> {
                tvTextTi.text = "汉译英"
            }
            3 -> {
                tvTextTi.text = "英译汉"
            }
        }

        initAdapter()

    }

    private fun initAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        val adapter = ListenChooseAdapter(activity!!, arrayListOf("", "", "", ""),code)
        recyclerView.adapter = adapter

    }
}