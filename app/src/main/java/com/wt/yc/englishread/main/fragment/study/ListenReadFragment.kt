package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import kotlinx.android.synthetic.main.study_head.*

class ListenReadFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.listen_tead_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvTopTitle.text = "听读训练"
        tvTopName.text = ""
        initClick()
    }

    private fun initClick() {
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

    }
}