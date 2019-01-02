package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.TextChooseAdapter
import kotlinx.android.synthetic.main.study_head.*
import kotlinx.android.synthetic.main.unit_test_fragment.*

/**
 * 单元测试 界面
 */
class UnitTestFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

        }

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.unit_test_fragment, container, false)
    }

    /**
     * 1 为正常 无计时  2 为继续存在计时
     */
    var code = 1
    var title = ""
    var unitInfo: Info? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTopTitle.text = title

        if (unitInfo != null) {
            tvTopName.text = unitInfo!!.title
        } else {
            tvTopName.text = ""
        }

        initUnitAdapter()
        initClick()

        when (code) {
            1 -> {
                imageTime.visibility = View.GONE
                tvTimeTi.visibility = View.GONE

            }
            2 -> {
                imageTime.visibility = View.VISIBLE
                tvTimeTi.visibility = View.VISIBLE
            }
        }


    }

    private fun initClick() {
        tvFinishBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        buttonStart.setOnClickListener {
            if (chooseInfo != null) {
                (activity as MainPageActivity).toWhere(Constant.TEST_DETAILS_CODE, chooseInfo!!)
            } else {
                showShortToast(activity!!, "请选择题目")
            }

        }

    }


    val list = arrayListOf<Info>()
    var chooseInfo: Info? = null

    private fun initUnitAdapter() {
        list.clear()

        for (i in 0..4) {
            val info = Info()
            when (i) {
                0 -> {
                    info.title = "1"
                    info.isErr = 0
                }
                1 -> {
                    info.title = "2"
                    info.isErr = 1
                }

                else -> {
                    info.title = "3"
                    info.isErr = 2

                }
            }

            list.add(info)

        }

        unitRecyclerView.layoutManager = GridLayoutManager(activity, 6)
        val adapter = TextChooseAdapter(activity!!, list)
        unitRecyclerView.adapter = adapter

        adapter.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {
                chooseInfo = list[position]
                adapter.updateClick(position)

            }

            override fun onLongClick(position: Int) {

            }

        }
    }
}