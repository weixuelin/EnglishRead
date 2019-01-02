package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.tts.TTSForApi
import kotlinx.android.synthetic.main.review_fragment.*
import kotlinx.android.synthetic.main.review_two_view.view.*
import kotlinx.android.synthetic.main.review_view.view.*
import kotlinx.android.synthetic.main.study_head.*


class ReviewFragment : ProV4Fragment() {
    var rfInfo: Info? = null

    override fun handler(msg: Message) {
        val str=msg.obj as String
        when(msg.what){

        }


    }


    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.review_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        tvTopTitle.text = "复习"
        tvTopName.text = "${rfInfo!!.title} 第一阶段"

        initClick()
        getOneList()
    }

    /**
     * 获取第一阶段的测试信息
     */
    private fun getOneList() {
        initFirstList()
    }

    /**
     * 获取第二阶段的测试信息
     */
    private fun getTwoList() {
        initTwoList()

    }

    /**
     * 获取第三阶段的测试信息
     */
    private fun getThreeList() {
        initThreeList()

    }


    var vList: ArrayList<View> = arrayListOf()

    val firstArr = arrayListOf("", "", "", "")
    val twoArr = arrayListOf("", "", "", "")
    val threeArr = arrayListOf("", "", "", "")

    var indexView = 0

    var indexCode = 1

    private fun initClick() {
        buttonNext.setOnClickListener {

            when (indexCode) {
                1 -> {
                    indexView++
                    reviewViewPager.currentItem = indexView

                    val number = reviewViewPager.currentItem

                    if (number == firstArr.size - 1) {
                        getTwoList()
                        indexView = 0
                        indexCode = 2
                    }
                }

                2 -> {
                    indexView++
                    reviewViewPager.currentItem = indexView

                    val number = reviewViewPager.currentItem

                    if (number == twoArr.size - 1) {
                        getThreeList()
                        indexView = 0
                        indexCode = 3
                    }
                }

                3 -> {
                    indexView++
                    reviewViewPager.currentItem = indexView

                }
            }
        }
    }


    val textStr = "You say that you love rain,but you open your umbrella when it rains..."

    /**
     * 初始化第一阶段
     */
    private fun initFirstList() {
        vList.clear()

        for (i in 0 until firstArr.size) {
            val vv = layoutInflater.inflate(R.layout.review_view, null)
            vv.tvContent.text = "测试-$i"
            vv.tvYuTi.text = "[]"
            vv.tvWordYiSi.text = ""
            vv.tvLiFanYi.text = "I have a dream that on day"

            vv.linearSure.setOnClickListener {
                handler!!.postDelayed({
                    toNextOne()
                }, 100)

            }

            vv.linearError.setOnClickListener {
                handler!!.postDelayed({

                    toNextOne()

                }, 100)

            }

            vv.linearAgain.setOnClickListener {
                playWordVoice(textStr)

            }

            vv.imagePlay.setOnClickListener {
                playWordVoice(textStr)
            }

            vList.add(vv)

        }

        reviewViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = vList.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(vList[position])
                return vList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(vList[position])
            }

        }
    }


    private fun toNextOne() {
        voiceFilePath = null
        indexView++
        if (indexView == firstArr.size) {
            getTwoList()
        } else {
            reviewViewPager.currentItem = indexView
        }
    }


    private fun toThreeOne() {
        indexView++
        if (indexView == twoArr.size) {
            getThreeList()
        } else {
            reviewViewPager.currentItem = indexView
        }

    }

    /**
     * 初始化第二阶段
     */
    private fun initTwoList() {
        vList.clear()
        val str = "${rfInfo!!.title} 第二阶段"
        tvTopName.text = str
        indexView = 0

        for (i in 0 until twoArr.size) {
            val twoView = layoutInflater.inflate(R.layout.review_two_view, null)
            twoView.tvTwoContent.text = ""
            twoView.tvTwoYuTi.text = ""
            twoView.tvTwoWordYiSi.text = ""
            twoView.tvTwoLiFanYi.text = ""

            twoView.imageTwoPlay.setOnClickListener {
                playWordVoice(textStr)

            }

            twoView.linearTwoSure.setOnClickListener {
                toThreeOne()
            }

            twoView.linearTwoError.setOnClickListener {
                toThreeOne()
            }

            twoView.linearTwoAgain.setOnClickListener {
                playWordVoice(textStr)
            }

            vList.add(twoView)

        }

        reviewViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = vList.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(vList[position])
                return vList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(vList[position])
            }

        }

    }

    /**
     * 初始化第三阶段
     */
    private fun initThreeList() {
        vList.clear()
        val str = "${rfInfo!!.title} 第三阶段"
        tvTopName.text = str
        indexView = 0

        for (i in 0 until threeArr.size) {
            val threeView = layoutInflater.inflate(R.layout.review_three_view, null)

            vList.add(threeView)

        }

        reviewViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = vList.size

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(vList[position])
                return vList[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(vList[position])
            }

        }

    }
}