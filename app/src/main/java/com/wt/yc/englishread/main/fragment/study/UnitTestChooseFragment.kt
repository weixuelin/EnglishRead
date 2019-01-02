package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.view.AdTextView
import kotlinx.android.synthetic.main.study_head.*
import kotlinx.android.synthetic.main.unit_choose_fragment.*

class UnitTestChooseFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.unit_choose_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initClick()


    }

    private fun initClick() {
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }


        chooseLinear1.setOnClickListener {
            val info = Info()
            info.code = 1


            (activity as MainPageActivity).toWhere(Constant.LISTEN_CHOOSE_CODE, info)

        }

        chooseLinear2.setOnClickListener {
            val info = Info()
            info.code = 2
            (activity as MainPageActivity).toWhere(Constant.LISTEN_CHOOSE_CODE, info)
        }

        chooseLinear3.setOnClickListener {
            val info = Info()
            info.code = 3
            (activity as MainPageActivity).toWhere(Constant.LISTEN_CHOOSE_CODE, info)
        }
    }
}