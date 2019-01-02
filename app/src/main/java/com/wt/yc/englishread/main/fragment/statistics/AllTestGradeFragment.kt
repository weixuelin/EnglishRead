package com.wt.yc.englishread.main.fragment.statistics

import android.os.Bundle
import android.os.Message
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.main.adapter.AllGradeAdapter
import kotlinx.android.synthetic.main.all_test_grade_gragment.*

/**
 * 所有的测试成绩信息
 */
class AllTestGradeFragment : ProV4Fragment() {

    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.all_test_grade_gragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()

        initClick()
        initSpinner()
    }

    val spinnerList = arrayListOf("测试一", "测试二", "测试三", "测试四", "测试五")

    private fun initSpinner() {
        testSpinner.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, spinnerList)

        testSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val resultStr = spinnerList[p2]
                getFromNet(resultStr)
            }

        }

    }

    /**
     * 从网络上获取
     */
    private fun getFromNet(resultStr: String) {


    }

    private fun initClick() {
        buttonNext.setOnClickListener {
            list.add("")
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }

        }
    }

    val list = arrayListOf("", "", "", "")
    var adapter: AllGradeAdapter? = null

    private fun initAdapter() {
        adapter = AllGradeAdapter(activity!!, list)
        allGradeRecyclerView.layoutManager = LinearLayoutManager(activity)
        allGradeRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        allGradeRecyclerView.adapter = adapter
    }
}