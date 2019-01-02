package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import kotlinx.android.synthetic.main.listen_write_fragment.*
import kotlinx.android.synthetic.main.study_head.*

class ListenWriteFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.listen_write_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTopTitle.text = "听写"
        tvTopName.text = ""


        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        tvSure.setOnClickListener {
            // 重新构建数据
        }


    }
}