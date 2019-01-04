package com.wt.yc.englishread.main.fragment.main

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.reflect.TypeToken
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ItemClickListener
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.base.Share
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.info.MainInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.main.adapter.StudentAdapter
import com.wt.yc.englishread.user.LoginActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import kotlinx.android.synthetic.main.main_fragment_layout.*
import org.json.JSONObject

/**
 * 首页fragment
 */
class MainFragment : ProV4Fragment() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.MAIN_DATA_CODE -> {
                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)

                if (code == Config.SUCCESS) {

                    val jsonObject = json.optJSONObject(Config.DATA)
                    val bannerResult = jsonObject.optString("banner")

                    if (bannerResult != "" && bannerResult != "null") {
                        if (bannerResult.startsWith("{")) {
                            val info = gson!!.fromJson<MainInfo>(bannerResult, MainInfo::class.java)
                            showBanner(arrayListOf(info))

                        } else {
                            val bannerArr = gson!!.fromJson<ArrayList<MainInfo>>(bannerResult, object : TypeToken<ArrayList<MainInfo>>() {}.type)
                            if (bannerArr != null) {
                                showBanner(bannerArr)
                            }
                        }

                    }

                    val studentResult = jsonObject.optString("student")

                    if (studentResult != "" && studentResult != "null") {

                        val studentArr = gson!!.fromJson<ArrayList<MainInfo>>(studentResult, object : TypeToken<ArrayList<MainInfo>>() {}.type)
                        if (studentArr != null) {
                            showStudent(studentArr)
                        }
                    }

                }
            }

        }

    }

    private fun showStudent(studentArr: ArrayList<MainInfo>?) {
        adapter!!.updateData(studentArr!!)

    }

    private fun showBanner(bannerArr: ArrayList<MainInfo>?) {
        for (temp in bannerArr!!) {
            picList.add(Config.IP + temp.icon)
        }

        initViewPager(activity!!, picViewPager, picList, handler!!, 1)

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment_layout, container, false)
    }

    val picList = arrayListOf<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initStudentAdapter()

        initClick()

        getMainData()


    }

    private fun getMainData() {
        HttpUtils.getInstance().postJson(Config.GET_MAIN_DATA, "", Config.MAIN_DATA_CODE, handler)
    }

    private fun initClick() {
        linearLayout1.setOnClickListener {
            if (Share.getUid(activity!!) != 0) {
                startActivity(Intent(activity!!, MainPageActivity::class.java))
            } else {
                startActivity(Intent(activity!!, LoginActivity::class.java))
                showShortToast(activity!!, "请登录")
            }

        }

    }

    var adapter: StudentAdapter? = null
    val list = arrayListOf<MainInfo>()

    private fun initStudentAdapter() {
        studentRecyclerView.isNestedScrollingEnabled = false

        studentRecyclerView.layoutManager = GridLayoutManager(activity, 4)
        adapter = StudentAdapter(activity!!, list)

        studentRecyclerView.adapter = adapter
        adapter!!.itemClickListener = object : ItemClickListener {
            override fun onItemClick(position: Int) {

            }

            override fun onLongClick(position: Int) {

            }

        }


    }
}