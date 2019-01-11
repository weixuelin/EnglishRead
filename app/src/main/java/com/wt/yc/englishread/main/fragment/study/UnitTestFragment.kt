package com.wt.yc.englishread.main.fragment.study

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.TextChooseAdapter
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.study_head.*
import kotlinx.android.synthetic.main.unit_test_fragment.*
import org.json.JSONObject

/**
 * 单元测试 界面  生词测试
 */
class UnitTestFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.GET_TEST_CODE -> {
                val json = JSONObject(str)
                val status = json.optBoolean(Config.STATUS)
                if (status) {

                }
            }
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
    var unitInfo: BookInfo? = null

    /**
     * 0 为单元测试     1 为生词测试
     */
    var testCode: Int = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvTopTitle.text = title

        if (unitInfo != null) {
            tvTopName.text = unitInfo!!.unit_name
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

        getTestList()


    }

    private fun getTestList() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        json.put("type", when (testCode) {
            1 -> "sc"
            2 -> ""
            else -> "unit"
        })

        if (testCode == 0) {
            json.put("unit_id", "1")
        }


        HttpUtils.getInstance().postJson(Config.GET_TEST_URL, json.toString(), Config.GET_TEST_CODE, handler)
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


    val list = arrayListOf<BookInfo>()

    var chooseInfo: BookInfo? = null

    private fun initUnitAdapter() {
        
        list.clear()

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