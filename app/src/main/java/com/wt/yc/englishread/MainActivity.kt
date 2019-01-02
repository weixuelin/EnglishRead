package com.wt.yc.englishread

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentManager
import com.wt.yc.englishread.base.ProActivity
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

        val tt = manager!!.beginTransaction()
        tt.add(R.id.linearLayout, MainFragment())
        tt.commit()

        initClick()
    }

    private fun initClick() {
        tvLogin.setOnClickListener {
            startActivityForResult(Intent(this, LoginActivity::class.java), 1234)
        }


        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tt = manager!!.beginTransaction()

                when (tab!!.position) {
                    0 -> tt.replace(R.id.linearLayout, MainFragment())
                    1 -> tt.replace(R.id.linearLayout, AboutFragment())
                    2 -> tt.replace(R.id.linearLayout, MapFragment())
                }

                tt.commit()
            }

        })
    }


}
