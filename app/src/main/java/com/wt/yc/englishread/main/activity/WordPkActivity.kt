package com.wt.yc.englishread.main.activity

import android.os.Bundle
import android.os.Message
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProActivity
import kotlinx.android.synthetic.main.main_top.*
import kotlinx.android.synthetic.main.word_pk_layout.*

/**
 * 单词PK
 */
class WordPkActivity:ProActivity() {

    override fun handler(msg: Message) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_pk_layout)

        initClick()
    }

    private fun initClick() {
        buttonClose.setOnClickListener {
            finish()
        }

        imageMainBack.setOnClickListener {
            finish()
        }

        imageMainLoginOut.setOnClickListener {

        }

        imageMainSet.setOnClickListener {

        }
    }
}