package com.wt.yc.englishread.main.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v4.app.FragmentManager
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Constant
import com.wt.yc.englishread.base.ProActivity
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.main.adapter.LoadAdapter
import com.wt.yc.englishread.main.fragment.expandtest.*
import com.wt.yc.englishread.main.fragment.intelligence.IntelligenceTestFragment
import com.wt.yc.englishread.main.fragment.mainpage.*
import com.wt.yc.englishread.main.fragment.statistics.AllTestGradeFragment
import com.wt.yc.englishread.main.fragment.statistics.MyGroupUpFragment
import com.wt.yc.englishread.main.fragment.statistics.StudyTimeFragment
import com.wt.yc.englishread.main.fragment.statistics.WrongWordFragment
import com.wt.yc.englishread.main.fragment.study.*
import com.wt.yc.englishread.user.fragment.UserFragment
import kotlinx.android.synthetic.main.main_page_layout.*
import kotlinx.android.synthetic.main.main_top.*
import kotlinx.android.synthetic.main.open_door.view.*

/**
 * 首页
 */
class MainPageActivity : ProActivity() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

        }

    }

    var manager: FragmentManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_layout)
        manager = supportFragmentManager

        initLoadAdapter()

        initClick()

    }

    var isSignIn = 1

    private fun initClick() {
        userPicHead.setOnClickListener {
            val t = manager!!.beginTransaction()
            t.addToBackStack("UserFragment")
            twoIndexFragment = switchContent(twoIndexFragment!!, UserFragment(), R.id.frameLayout, t)
        }

        imageViewSignIn.setOnClickListener {
            if (isSignIn == 1) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("提示")
                        .setMessage("已获得10金币的奖励!!")
                        .setPositiveButton("确定") { dialog, i ->
                            dialog.dismiss()
                            isSignIn = 2
                        }.show()
            } else {
                showToastShort("您已签到")
            }

        }
    }

    val titleArr = arrayListOf<String>("首页", "学习", "学习统计", "智能测试", "生词本", "书架", "游戏PK榜", "拓展训练")
    val picClick: ArrayList<Int> = arrayListOf(R.drawable.main1, R.drawable.main2, R.drawable.main3, R.drawable.main4,
            R.drawable.main5, R.drawable.main6, R.drawable.main7, R.drawable.main8)

    val pic: ArrayList<Int> = arrayListOf(R.drawable.main1_gray, R.drawable.main2_gray, R.drawable.main3_gray, R.drawable.main4_gray,
            R.drawable.main5_gray, R.drawable.main6_gray, R.drawable.main7_gray, R.drawable.main8_gray)

    /**
     * 学习统计二级
     */
    val sonStudyTongArr = arrayListOf("我的成长经历", "近一周学习趋势", "所有测试成绩", "学习时间统计", "高频错词")

    /**
     * 智能测试
     */
    val testStrArr = arrayListOf("生词测试", "熟词测试", "综合测试", "一测到底")

    /**
     * 拓展训练二级
     */
    val tuoStrArr: ArrayList<String> = arrayListOf("键盘练习", "字母练习", "音标练习", "听力训练", "阅读训练", "写作训练")

    /**
     * 游戏pk榜
     */
    val pkArr = arrayListOf("单词PK", "单词消消乐")

    var adapter: LoadAdapter? = null

    val mainArr = arrayListOf<Info>()

    var lastIndexNum = 0

    private fun initLoadAdapter() {

        twoIndexFragment = oneFragmentList[0]

        val tran = manager!!.beginTransaction()

        twoIndexFragment = switchContent(twoIndexFragment!!, twoIndexFragment!!, R.id.frameLayout, tran)

        for (i in titleArr.indices) {
            val info = Info()
            info.title = titleArr[i]
            info.pic = pic[i]
            info.picClick = picClick[i]
            info.click = i == 0

            when (i) {
                2 -> {
                    val studyArr = arrayListOf<Info.Companion.Son>()

                    for (temp in sonStudyTongArr) {
                        val son = Info.Companion.Son()
                        son.str = temp
                        studyArr.add(son)
                    }

                    info.sonList = studyArr
                }

                3 -> {
                    val studyArr = arrayListOf<Info.Companion.Son>()

                    for (temp in testStrArr) {
                        val son = Info.Companion.Son()
                        son.str = temp
                        studyArr.add(son)
                    }

                    info.sonList = studyArr

                }

                6 -> {
                    val studyArr = arrayListOf<Info.Companion.Son>()

                    for (temp in pkArr) {
                        val son = Info.Companion.Son()
                        son.str = temp
                        studyArr.add(son)
                    }

                    info.sonList = studyArr
                }

                7 -> {
                    val tuoArr = arrayListOf<Info.Companion.Son>()

                    for (temp in tuoStrArr) {
                        val son = Info.Companion.Son()
                        son.str = temp
                        tuoArr.add(son)
                    }

                    info.sonList = tuoArr
                }

            }
            mainArr.add(info)
        }

        adapter = LoadAdapter(this, mainArr)
        pageLeadRecyclerView.setAdapter(adapter)

        pageLeadRecyclerView.setOnGroupClickListener { p0, p1, p2, p3 ->
            val info = mainArr[p2]
            val list = info.sonList

            if (list != null && list.size != 0) {
                /// 展开二级页面

                lastIndexNum = p2

                adapter!!.updateClick(p2)
                false

            } else {

                pageLeadRecyclerView.collapseGroup(lastIndexNum)
                adapter!!.updateSonNoClick(lastIndexNum)

                mainJoup(p2)

                true
            }
        }

        pageLeadRecyclerView.setOnChildClickListener { p0, p1, p2, p3, p4 ->

            adapter!!.twoUpdate(p2, p3)

            toSonJoup(p2, p3)

            true
        }
    }


    var twoIndexFragment: ProV4Fragment? = null

    /**
     * 二级点击跳转
     */
    private fun toSonJoup(personId: Int, p3: Int) {
        when (personId) {
            2 -> {
                when (p3) {
                    0 -> {
                        val tran = manager!!.beginTransaction()
                        val fragment = MyGroupUpFragment()
                        fragment.code = 1
                        tran.replace(R.id.frameLayout, fragment)
                        tran.commit()
                    }
                    1 -> {
                        val tran = manager!!.beginTransaction()
                        val fragment = MyGroupUpFragment()
                        fragment.code = 2
                        tran.replace(R.id.frameLayout, fragment)
                        tran.commit()
                    }
                    2 -> {
                        val tran = manager!!.beginTransaction()
                        val fragment = AllTestGradeFragment()
                        tran.replace(R.id.frameLayout, fragment)
                        tran.commit()
                    }
                    3 -> {
                        val tran = manager!!.beginTransaction()
                        val fragment = StudyTimeFragment()
                        tran.replace(R.id.frameLayout, fragment)
                        tran.commit()

                    }
                    4 -> {
                        val tran = manager!!.beginTransaction()
                        val fragment = WrongWordFragment()
                        tran.replace(R.id.frameLayout, fragment)
                        tran.commit()
                    }
                }
            }

            3 -> {
                when (p3) {
                    0 -> {
                        ///  智能测试  生词测试
                        val tt = manager!!.beginTransaction()
                        val f1 = UnitTestFragment()
                        f1.title = "生词测试"
                        tt.replace(R.id.frameLayout, f1)
                        tt.addToBackStack("UnitTestFragment")
                        tt.commit()

                    }

                    1 -> {
                        ///  智能测试 熟词测试
                        val tt = manager!!.beginTransaction()
                        val f2 = UnitTestFragment()
                        f2.title = "熟词测试"
                        tt.replace(R.id.frameLayout, f2)
                        tt.addToBackStack("UnitTestFragment")
                        tt.commit()
                    }

                    2 -> {
                        ///  智能测试 综合测试
                        val tt = manager!!.beginTransaction()
                        val f3 = UnitTestFragment()
                        f3.title = "综合测试"
                        tt.replace(R.id.frameLayout, f3)
                        tt.addToBackStack("UnitTestFragment")
                        tt.commit()
                    }

                    3 -> {
                        ///  智能测试 一测到底界面
                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, IntelligenceTestFragment())
                        tt.addToBackStack("IntelligenceTestFragment")
                        tt.commit()

                    }
                }
            }

            6 -> {
                when (p3) {
                    0 -> {
                        shopOpenDoor()
                    }
                    1 -> {

                    }
                }
            }

            7 -> {
                when (p3) {
                    0 -> {
                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, KeyExerciseFragment())
                        tt.addToBackStack("KeyExerciseFragment")
                        tt.commit()

                    }

                    1 -> {
                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, LetterExerciseFragment())
                        tt.addToBackStack("LetterExerciseFragment")
                        tt.commit()

                    }

                    2 -> {

                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, SoundmarkExerciseFragment())
                        tt.addToBackStack("SoundmarkExerciseFragment")
                        tt.commit()
                    }

                    3 -> {
                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, HearingExerciseFragment())
                        tt.addToBackStack("HearingExerciseFragment")
                        tt.commit()
                    }

                    4 -> {
                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, ReadExerciseFragment())
                        tt.addToBackStack("ReadExerciseFragment")
                        tt.commit()
                    }

                    5 -> {
                        val tt = manager!!.beginTransaction()
                        tt.replace(R.id.frameLayout, WritingExerciseFragment())
                        tt.addToBackStack("WritingExerciseFragment")
                        tt.commit()

                    }
                }
            }
        }

    }


    /**
     * 开房
     */
    private fun shopOpenDoor() {
        val view = layoutInflater.inflate(R.layout.open_door, null)
        val openDialog = Dialog(this, R.style.style)
        openDialog.setContentView(view)
        openDialog.show()
        setAlpha(0.6f)

        openDialog.setOnDismissListener {
            setAlpha(1f)
            openDialog.dismiss()
        }

        view.imageClose.setOnClickListener {
            openDialog.dismiss()
            setAlpha(1f)
        }

        view.btCreateDoor.setOnClickListener {
            openDialog.dismiss()
            setAlpha(1f)
            startActivity(Intent(this, WordPkActivity::class.java))
        }

        view.btFreshDoor.setOnClickListener {
            openDialog.dismiss()
            setAlpha(1f)
        }

    }

    /**
     * 一级页面
     */
    val oneFragmentList = arrayListOf<ProV4Fragment>(PageFragment(), StudyFragment(),
            NewWordFragment(), BookRackFragment())

    /**
     * 一级点击跳转
     */
    fun mainJoup(position: Int) {

        adapter!!.updateClick(position)

        val tran = manager!!.beginTransaction()

        when (position) {
            0 -> twoIndexFragment = switchContent(twoIndexFragment!!, oneFragmentList[0], R.id.frameLayout, tran)

            1 -> twoIndexFragment = switchContent(twoIndexFragment!!, oneFragmentList[1], R.id.frameLayout, tran)

            3 -> {
            }

            4 -> twoIndexFragment = switchContent(twoIndexFragment!!, oneFragmentList[2], R.id.frameLayout, tran)
            5 -> twoIndexFragment = switchContent(twoIndexFragment!!, oneFragmentList[3], R.id.frameLayout, tran)

        }
    }


    val joupFragmentList =
            arrayListOf<ProV4Fragment>(ReviewFragment(),
                    UnitTestFragment(),
                    ListenChooseFragment(),
                    ListenWriteFragment(),
                    ListenReadFragment(),
                    TestDetailsFragment(),
                    AnswerFinishFragment(),
                    MainChallengeFragment(),
                    MyGroupUpFragment(),
                    MyGroupUpFragment(),
                    StudyFragment())

    /**
     * 到哪个界面
     * code 1 复习  2 单元测试  3 听写   4 听写训练
     */
    fun toWhere(code: Int, info: Info?) {

        val tt = manager!!.beginTransaction()

        when (code) {

            Constant.STUDY_REVIEW -> {
                /// 复习界面
                tt.addToBackStack("ReviewFragment")

                val rf = joupFragmentList[0] as ReviewFragment
                rf.rfInfo = info

                twoIndexFragment = switchContent(twoIndexFragment!!, rf, R.id.frameLayout, tt)

            }


            Constant.STUDY_TEST -> {
                /// 测试内容选择
                tt.addToBackStack("UnitTestFragment")
                val ff = joupFragmentList[1] as UnitTestFragment
                ff.title = "单元测试"
                ff.unitInfo = info

                twoIndexFragment = switchContent(twoIndexFragment!!, ff, R.id.frameLayout, tt)

            }

            Constant.LISTEN_CHOOSE_CODE -> {
                // 听写  汉译英  英译汉
                tt.addToBackStack("ListenChooseFragment")
                val listen = joupFragmentList[2] as UnitTestFragment
                listen.code = info!!.code
                twoIndexFragment = switchContent(twoIndexFragment!!, listen, R.id.frameLayout, tt)

            }

            Constant.LISTEN_WRITE_CODE -> {
                // 听写
                tt.addToBackStack("ListenWriteFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, joupFragmentList[3], R.id.frameLayout, tt)

            }

            Constant.LISTEN_READ_CODE -> {
                // 听读训练
                tt.addToBackStack("ListenReadFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, joupFragmentList[4], R.id.frameLayout, tt)

            }

            Constant.TEST_DETAILS_CODE -> {

                tt.addToBackStack("TestDetailsFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, joupFragmentList[5], R.id.frameLayout, tt)

            }

            Constant.ANSWER_RESULT -> {
                /// 答题完成后的成绩
                tt.addToBackStack("AnswerFinishFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, joupFragmentList[6], R.id.frameLayout, tt)
            }

            Constant.MAIN_TIAO_ZHAN -> {
                // 首页挑战
                tt.addToBackStack("MainChallengeFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, joupFragmentList[7], R.id.frameLayout, tt)
            }

            Constant.MY_GROUP_UP_CODE -> {

                val fragment = joupFragmentList[8] as MyGroupUpFragment
                fragment.code = 1

                tt.addToBackStack("MyGroupUpFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, fragment, R.id.frameLayout, tt)

            }

            Constant.MY_WEEK_CODE -> {

                val fragment = joupFragmentList[9] as MyGroupUpFragment
                fragment.code = 2
                tt.addToBackStack("MyGroupUpFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, fragment, R.id.frameLayout, tt)

            }

            Constant.MAIN_STADUY_CODE -> {
                tt.addToBackStack("StudyFragment")
                twoIndexFragment = switchContent(twoIndexFragment!!, joupFragmentList[10], R.id.frameLayout, tt)

            }
        }
    }
}