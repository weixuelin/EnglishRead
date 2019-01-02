package com.wt.yc.englishread.main.fragment.expandtest

import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment

/**
 * 字母练习
 */
class LetterExerciseFragment:ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return  inflater.inflate(R.layout.letter_exercise_layout,container,false)
    }
}