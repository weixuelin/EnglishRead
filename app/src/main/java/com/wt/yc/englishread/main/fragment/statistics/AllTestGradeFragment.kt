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
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.main.adapter.AllGradeAdapter
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.all_test_grade_gragment.*
import org.json.JSONObject

/**
 * 所有的测试成绩信息
 */
class AllTestGradeFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str=msg.obj as String
        when(msg.what){
            Config.GET_SCORE_LIST_CODE->{
                removeLoadDialog()
                val json=JSONObject(str)
                val code=json.optInt(Config.CODE)
                if(code==Config.SUCCESS){
                    val data=json.optString(Config.DATA)
                    val  arr=gson!!.fromJson<ArrayList<BookInfo>>(data,object :TypeToken<ArrayList<BookInfo>>(){}.type)

                    if(arr!=null&&arr.size!=0){
                        adapter!!.updateDataClear(arr)
                    }

                }

            }
        }

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.all_test_grade_gragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()

        initClick()
        initSpinner()

        getData()

    }


    private fun getData() {
        val json = JSONObject()
        json.put("token", token)
        json.put("uid",uid)
        HttpUtils.getInstance().postJson(Config.GET_SCORE_LIST, json.toString(), Config.GET_SCORE_LIST_CODE, handler!!)
        showLoadDialog(activity!!, "获取中")
    }


    val spinnerList = arrayListOf<String>()

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
            if (adapter != null) {
                adapter!!.notifyDataSetChanged()
            }

        }
    }

    val list = arrayListOf<BookInfo>()
    var adapter: AllGradeAdapter? = null

    private fun initAdapter() {
        adapter = AllGradeAdapter(activity!!, list)
        allGradeRecyclerView.layoutManager = LinearLayoutManager(activity)
        allGradeRecyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        allGradeRecyclerView.adapter = adapter
    }
}