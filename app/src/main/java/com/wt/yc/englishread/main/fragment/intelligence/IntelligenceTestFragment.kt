package com.wt.yc.englishread.main.fragment.intelligence

import android.os.Bundle
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import kotlinx.android.synthetic.main.intel_quest_item.view.*
import kotlinx.android.synthetic.main.intelligence_test_fragment.*

class IntelligenceTestFragment : ProV4Fragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.intelligence_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        addView()

        initClick()
    }

    private fun initClick() {
        btIntelUp.setOnClickListener {
            var indexPosition = testViewPager.currentItem
            if (indexPosition > 0) {
                indexPosition--
                testViewPager.currentItem = indexPosition

            }

        }

        btIntelPost.setOnClickListener {
            ///  交卷

        }

        btIntelNext.setOnClickListener {
            var indexPosition = testViewPager.currentItem
            indexPosition++
            testViewPager.currentItem = indexPosition

        }
    }

    val viewPagerList: ArrayList<View> = arrayListOf()
    val questionArr: ArrayList<String> = arrayListOf("", "", "", "", "")

    private fun addView() {
        testViewPager.currentItem = 0

        for (i in questionArr.indices) {
            val temp = questionArr[i]
            val view = layoutInflater.inflate(R.layout.intel_quest_item, null)
            view.tvIntelNum.text = (i + 1).toString()
            view.tvIntelContent.text = temp
            view.intelRadioGroup.setOnCheckedChangeListener { p0, p1 ->
                when (p1) {

                }
            }

            view.imageViewTing.setOnClickListener {
                playVoice(activity!!, "")
            }

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

    override fun handler(msg: Message) {

    }
}