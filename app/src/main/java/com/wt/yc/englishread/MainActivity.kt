package com.wt.yc.englishread

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import com.wt.yc.englishread.base.ProActivity
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.base.Share
import com.wt.yc.englishread.main.fragment.main.AboutFragment
import com.wt.yc.englishread.main.fragment.main.MainFragment
import com.wt.yc.englishread.main.fragment.main.MapFragment
import com.wt.yc.englishread.user.LoginActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页
 */
class MainActivity : ProActivity() {

    override fun handler(msg: Message) {

    }

    var manager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        manager = supportFragmentManager

        if (Share.getUid(this) != 0) {
            tvLogin.text = "已登录"
        } else {
            tvLogin.text = "登录"
        }

        initClick()
    }

    val fragmentList = arrayListOf<ProV4Fragment>(MainFragment(), AboutFragment(), MapFragment())

    var indexFragment: ProV4Fragment? = null

    private fun initClick() {

        tvLogin.setOnClickListener {
            startActivityForResult(Intent(this, LoginActivity::class.java), 1234)
        }

        indexFragment = fragmentList[0]

        indexFragment = switchContent(indexFragment!!, indexFragment!!, R.id.linearLayout, manager!!.beginTransaction())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {

                val tt = manager!!.beginTransaction()

                when (tab!!.position) {
                    0 -> indexFragment = switchContent(indexFragment!!, fragmentList[0], R.id.linearLayout, tt)
                    1 -> indexFragment = switchContent(indexFragment!!, fragmentList[1], R.id.linearLayout, tt)
                    2 -> indexFragment = switchContent(indexFragment!!, fragmentList[2], R.id.linearLayout, tt)
                }
            }

        })
    }


}
