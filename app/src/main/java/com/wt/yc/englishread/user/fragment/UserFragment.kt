package com.wt.yc.englishread.user.fragment

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import kotlinx.android.synthetic.main.user_fragment_layout.*

class UserFragment : ProV4Fragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_fragment_layout, container, false)

    }

    override fun handler(msg: Message) {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

    }
}