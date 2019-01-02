package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import kotlinx.android.synthetic.main.answer_finish_fragment.*
import kotlinx.android.synthetic.main.study_head.*


/**
 * 答题完成
 */
class AnswerFinishFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.answer_finish_fragment, container, false)
    }

    val textTi = "恋词英语研究会\n2018年10月15日"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTimeTi.text = textTi

        tvFinishBack.setOnClickListener { fragmentManager!!.popBackStack() }
    }
}