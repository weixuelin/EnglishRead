package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.xin.lv.yang.utils.utils.DataUtil
import com.xin.lv.yang.utils.utils.FileUtils
import com.xin.lv.yang.utils.utils.TextUtils
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


    var bookInfo: BookInfo? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (bookInfo != null) {

            tvTimeTi.text = "恋词英语研究会\n${DataUtil.longToTime(System.currentTimeMillis() / 1000, "yyyy年MM月dd日")}"

            testUserName.text = "${bookInfo!!.userName} 同学:"

            val scoreTi = "        于 ${bookInfo!!.time} 参加恋上单词组织的 【${bookInfo!!.book_name}】 获得${bookInfo!!.score}分, 用时 ${bookInfo!!.testTime} 分钟。"

            val sp = SpannableString(scoreTi)
            val startNum = scoreTi.indexOf("【")
            val endNum = scoreTi.indexOf("】")

            val startN11 = scoreTi.lastIndexOf("时")
            val startN22 = scoreTi.lastIndexOf("分")

            val startN33 = scoreTi.lastIndexOf("得")
            val startN44 = scoreTi.lastIndexOf(",")

            TextUtils.getInstance().setColor(activity!!, sp, startNum, endNum + 1, R.color.red, TextUtils.COLOR)
            TextUtils.getInstance().setColor(activity!!, sp, startN33 + 1, startN44 - 1, R.color.red, TextUtils.COLOR)
            TextUtils.getInstance().setColor(activity!!, sp, startN11 + 1, startN22, R.color.red, TextUtils.COLOR)

            testUserScore.setText(sp, TextView.BufferType.SPANNABLE)

            val score = bookInfo!!.score.toInt()
            testUserTi.text = if (score > 80) {
                "这次测试成绩不错，请继续保持！！"
            } else if (score in 61..78) {
                "测试成绩还有待提高，请继续努力！！"
            } else {
                "学习又偷懒了吧，打起精神，不要灰心，相信你可以的！！"
            }

        }

        tvFinishBack.setOnClickListener { fragmentManager!!.popBackStack() }

        btTestAgain.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

    }
}