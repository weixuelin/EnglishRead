package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.MuLuAdapter
import com.wt.yc.englishread.main.adapter.TextChooseAdapter
import com.xin.lv.yang.utils.view.AdTextView
import kotlinx.android.synthetic.main.test_details_fragment.*
import kotlinx.android.synthetic.main.view_pager_test.view.*
import java.lang.StringBuilder


/**
 * 测试详情fragment 页面
 */
class TestDetailsFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

        }
    }


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.test_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initTestViewPager()
        tvTitle.text = "单元测试"
        tvName.text = "第一单元"

        initClick()

        initRightMuLu()

        getQuestList()
    }

    /**
     * 获取问题列表
     */
    private fun getQuestList() {

    }

    /**
     * 目录列表信息
     */
    val muLuArr = arrayListOf("测试一", "测试二", "测试三", "测试四")

    /**
     * 初始化目录信息
     */
    private fun initRightMuLu() {
        muLuRecyclerView.layoutManager = LinearLayoutManager(activity)
        muLuRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        val muLuAdapter = MuLuAdapter(activity!!, muLuArr)
        muLuRecyclerView.adapter = muLuAdapter
        muLuAdapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                drawerLayout.closeDrawer(Gravity.END)
            }

            override fun onLongClick(position: Int) {

            }

        }

    }

    var indexNum = 0

    private fun initClick() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        tvMuLu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.END)
        }

        btUp.setOnClickListener {
            if (indexNum > 0) {
                indexNum--
                testViewPager.currentItem = indexNum
            }
        }

        btPost.setOnClickListener {
            val str = buildArr(answerArr)

            log("wwwwwww------$str")

            fragmentManager!!.popBackStackImmediate("TestDetailsFragment", 0)

            (activity as MainPageActivity).toWhere(Constant.ANSWER_RESULT, null)

        }

        btNext.setOnClickListener {
            indexNum++
            testViewPager.currentItem = indexNum
        }
    }

    private fun buildArr(answerArr: ArrayList<Info>): String {
        val sb = StringBuilder()
        val len = answerArr.size

        for (i in answerArr.indices) {
            val temp = answerArr[i]
            sb.append(temp.answer)
            if (i < len - 1) {
                sb.append(",")
            }
        }

        return sb.toString()
    }

    /**
     * viewPager 集合
     */
    val viewPagerList = arrayListOf<View>()

    /**
     * 答案数组
     */
    val answerArr = arrayListOf<Info>()

    /**
     * 问题数组信息
     */
    val questionArr: ArrayList<Info> = arrayListOf(Info(), Info(), Info(), Info(), Info())

    private fun initTestViewPager() {

        for (temp in questionArr) {

            val view = layoutInflater.inflate(R.layout.view_pager_test, null)

            view.rb1.text = "测试题一"
            view.rb2.text = "测试题二"
            view.rb3.text = "测试题三"
            view.rb4.text = "测试题四"

            view.radioGroup.setOnCheckedChangeListener { p0, p1 ->
                when (p1) {
                    R.id.rb1 -> {
                        temp.answer = "A"
                    }
                    R.id.rb2 -> {
                        temp.answer = "B"
                    }
                    R.id.rb3 -> {
                        temp.answer = "C"
                    }
                    R.id.rb4 -> {
                        temp.answer = "D"
                    }

                }
            }

            checkArr(temp)

            viewPagerList.add(view)
        }

        testViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int {
                return viewPagerList.size
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val pageView = viewPagerList[position]
                container.addView(pageView)
                return pageView
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(viewPagerList[position])
            }

        }
    }

    private fun checkArr(temp: Info) {
        val num = isExit(answerArr, temp)

        if (num != -1) {

            val info = answerArr[num]
            answerArr.removeAt(num)

            info.answer = temp.answer

            answerArr.add(num, info)

        } else {
            answerArr.add(temp)
        }


    }

    private fun isExit(answerArr: ArrayList<Info>, temp: Info): Int {
        for (i in answerArr.indices) {
            val tt = answerArr[i]
            if (tt.id == temp.id) {
                return i
            }
        }
        return -1
    }

}