package com.wt.yc.englishread.main.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.view.KeyEvent
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.*
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.info.UserInfo
import com.wt.yc.englishread.main.adapter.LoadAdapter
import com.wt.yc.englishread.main.fragment.expandtest.*
import com.wt.yc.englishread.main.fragment.intelligence.IntelligenceTestFragment
import com.wt.yc.englishread.main.fragment.mainpage.*
import com.wt.yc.englishread.main.fragment.statistics.AllTestGradeFragment
import com.wt.yc.englishread.main.fragment.statistics.MyGroupUpFragment
import com.wt.yc.englishread.main.fragment.statistics.StudyTimeFragment
import com.wt.yc.englishread.main.fragment.statistics.WrongWordFragment
import com.wt.yc.englishread.main.fragment.study.*
import com.wt.yc.englishread.user.LoginActivity
import com.wt.yc.englishread.user.fragment.UserFragment
import com.xin.lv.yang.utils.utils.HttpUtils
import com.xin.lv.yang.utils.utils.ImageUtil
import kotlinx.android.synthetic.main.main_page_layout.*
import kotlinx.android.synthetic.main.main_top.*
import kotlinx.android.synthetic.main.open_door.view.*
import kotlinx.android.synthetic.main.set_num_doalog.view.*
import org.json.JSONObject

/**
 * 首页
 */
class MainPageActivity : ProActivity() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {
            Config.SIGN_CODE -> {

                removeLoadDialog()

                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                if (code == Config.SUCCESS) {
                    showToastShort("签到成功")
                    isSignIn = 2

                    showSignDialog()

                    getUserInfo()
                }
            }

            Config.GET_USER_INFO_CODE -> {

                removeLoadDialog()
                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                val status = json.optBoolean(Config.STATUS)

                if (code == Config.SUCCESS && status) {
                    val result = json.optString(Config.DATA)
                    val user = gson!!.fromJson<UserInfo>(result, UserInfo::class.java)
                    showUserInfo(user)
                }
            }

            Config.SET_TODAY_CODE -> {

                removeLoadDialog()

                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)

                if (code == Config.SUCCESS) {
                    showToastShort("设置成功")

                } else {
                    showToastShort(json.optString(Config.MSG))
                }
            }
        }

    }

    private fun showSignDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("提示")
                .setMessage("已获得10金币的奖励!!")
                .setPositiveButton("确定") { dialog, i ->
                    dialog.dismiss()

                }.show()
    }


    private fun showUserInfo(user: UserInfo?) {

        if (!isFinishing) {

//            ImageUtil.getInstance().loadCircleImage(this, userPicHead, "", R.drawable.head_pic)

            tvUserName.text = user!!.username
            tvAllMoney.text = user.gold
        }


    }

    var lastIndexPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_page_layout)

        initLoadAdapter()

        initCustomViewPager()

        initClick()

        getUserInfo()

    }

    private fun getUserInfo() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_USER_INFO_URL, json.toString(), Config.GET_USER_INFO_CODE, handler)
    }


    val testList = arrayListOf<ProV4Fragment>(StudyFragment())

    /**
     * 一级页面
     */
    val oneFragmentList = arrayListOf<ProV4Fragment>(
            PageFragment(), StudyFragment(),
            NewWordFragment(), BookRackFragment(),
            UserFragment(), ReviewFragment(),
            UnitTestFragment(), ListenChooseFragment(),
            ListenWriteFragment(), ListenReadFragment(),
            TestDetailsFragment(), AnswerFinishFragment(),
            MainChallengeFragment(), MyGroupUpFragment(),
            MyGroupUpFragment(), StudyFragment(),
            AllTestGradeFragment(), StudyTimeFragment(),
            WrongWordFragment(), IntelligenceTestFragment(),
            KeyExerciseFragment(), LetterExerciseFragment(),
            SoundmarkExerciseFragment(), HearingExerciseFragment(),
            ReadExerciseFragment(), WritingExerciseFragment())


    private fun initCustomViewPager() {

        customViewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = oneFragmentList[position]

            override fun getCount(): Int = oneFragmentList.size

        }
        customViewPager.currentItem = 0
    }


    /**
     * 返回上一个界面
     */
    fun backTo() {
        customViewPager.currentItem = lastIndexPosition

    }


    /**
     *  1 为未签到   2 为当天已签到
     */
    var isSignIn = 1

    private fun initClick() {
        imageMainBack.setOnClickListener {
            finish()
        }

        tvUserName.setOnClickListener {
            lastIndexPosition = customViewPager.currentItem

            customViewPager.currentItem = 4
        }

        userPicHead.setOnClickListener {

            lastIndexPosition = customViewPager.currentItem

            customViewPager.currentItem = 4

        }

        imageViewSignIn.setOnClickListener {

            if (isSignIn == 1) {

                if (Share.getUid(this) != 0) {
                    sign()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }

            } else {
                showToastShort("您已签到")
            }

        }

        imageMainLoginOut.setOnClickListener {
            loginOut()
        }

        imageMainSet.setOnClickListener {
            ///  设置今日目标
            showSetNum()

        }
    }


    /**
     * 设置今日单词目标
     */
    private fun showSetNum() {
        val setDCView = layoutInflater.inflate(R.layout.set_num_doalog, null)

        val setDialog: Dialog = Dialog(this, R.style.style)
        setDialog.setContentView(setDCView)

        setDialog.show()
        setAlpha(0.6f)

        setDialog.setOnDismissListener {
            setAlpha(1f)
        }


        setDCView.setNumClose.setOnClickListener {
            setAlpha(1f)
            setDialog.dismiss()
        }

        setDCView.setNumQure.setOnClickListener {
            val wordResult = setDCView.setNumEdit.text.toString()

            if (wordResult != "") {
                val json = JSONObject()
                json.put("uid", uid)
                json.put("token", token)
                json.put("target", wordResult)
                HttpUtils.getInstance().postJson(Config.SET_TODAY_WORD_URL, json.toString(), Config.SET_TODAY_CODE, handler)
                showLoadDialog("设置中")

                setAlpha(1f)
                setDialog.dismiss()

            } else {
                showToastShort("请输入数量")
            }

        }

    }

    /**
     * 登出
     */
    private fun loginOut() {
        Share.clearUser(this)

    }

    /**
     * 签到
     */
    fun sign() {
        uid = Share.getUid(this)
        token = Share.getToken(this)
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.SIGN_URL, json.toString(), Config.SIGN_CODE, handler)
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

    /**
     * 点击的一级目录位置
     */
    var lastIndexNum = 0

    /**
     * 初始化左侧导航显示数据
     */
    private fun initLoadAdapter() {

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


    /**
     * 二级点击跳转
     */
    private fun toSonJoup(personId: Int, p3: Int) {

        when (personId) {
            2 -> {
                when (p3) {
                    0 -> {

                        val fragment = oneFragmentList[13] as MyGroupUpFragment
                        fragment.code = 1

                        customViewPager.currentItem = 13

                    }
                    1 -> {

                        val fragment = oneFragmentList[13] as MyGroupUpFragment
                        fragment.code = 2
                        customViewPager.currentItem = 14
                    }
                    2 -> {

                        customViewPager.currentItem = 16
                    }
                    3 -> {

                        customViewPager.currentItem = 17


                    }
                    4 -> {

                        customViewPager.currentItem = 18

                    }
                }
            }

            3 -> {
                when (p3) {
                    0 -> {
                        ///  智能测试  生词测试
                        val f1 = oneFragmentList[10] as TestDetailsFragment

                        f1.testCode = 1

                        customViewPager.currentItem = 10

                    }

                    1 -> {
                        ///  智能测试 熟词测试
                        val f2 = oneFragmentList[6] as UnitTestFragment
                        f2.title = "熟词测试"

                        customViewPager.currentItem = 6

                    }

                    2 -> {
                        ///  智能测试 综合测试
                        val f3 = oneFragmentList[6] as UnitTestFragment
                        f3.title = "综合测试"
                        customViewPager.currentItem = 6
                    }

                    3 -> {
                        ///  智能测试 一测到底界面

                        customViewPager.currentItem = 19

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

                        customViewPager.currentItem = 20

                    }

                    1 -> {
                        customViewPager.currentItem = 21

                    }

                    2 -> {
                        customViewPager.currentItem = 22
                    }

                    3 -> {
                        customViewPager.currentItem = 23

                    }

                    4 -> {
                        customViewPager.currentItem = 24

                    }

                    5 -> {
                        customViewPager.currentItem = 25


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
     * 一级点击跳转     PageFragment(), StudyFragment(),
     *                  NewWordFragment(), BookRackFragment()
     */
    fun mainJoup(position: Int) {

        adapter!!.updateClick(position)

        when (position) {
            0 -> customViewPager.currentItem = 0   // 首页  PageFragment
            1 -> customViewPager.currentItem = 1   // 学习  StudyFragment

            3 -> {
            }

            4 -> customViewPager.currentItem = 2  // 生词本  NewWordFragment
            5 -> customViewPager.currentItem = 3  // 书架    BookRackFragment

        }
    }


    /**
     * 到哪个界面
     * code 1 复习  2 单元测试  3 听写   4 听写训练
     */
    fun toWhere(code: Int, info: BookInfo?) {

        when (code) {

            Constant.STUDY_REVIEW -> {
                /// 复习界面

                val rf = oneFragmentList[5] as ReviewFragment
                rf.rfInfo = info
                customViewPager.currentItem = 5

            }


            Constant.STUDY_TEST -> {
                /// 测试内容选择
                val ff = oneFragmentList[10] as TestDetailsFragment
                ff.testCode = 0
                ff.unitInfo = info
                customViewPager.currentItem = 10

            }

            Constant.LISTEN_CHOOSE_CODE -> {
                // 听写  汉译英  英译汉
                val listen = oneFragmentList[7] as ListenChooseFragment
                listen.code = info!!.code

                customViewPager.currentItem = 7

            }

            Constant.LISTEN_WRITE_CODE -> {
                // 听写
                customViewPager.currentItem = 8

            }

            Constant.LISTEN_READ_CODE -> {
                // 听读训练
                customViewPager.currentItem = 9


            }

            Constant.TEST_DETAILS_CODE -> {

                customViewPager.currentItem = 10

            }

            Constant.ANSWER_RESULT -> {
                /// 答题完成后的成绩
                customViewPager.currentItem = 11

            }

            Constant.MAIN_TIAO_ZHAN -> {
                // 首页挑战
                customViewPager.currentItem = 12
            }

            Constant.MY_GROUP_UP_CODE -> {

                val fragment = oneFragmentList[13] as MyGroupUpFragment
                fragment.code = 1

                customViewPager.currentItem = 13

            }

            Constant.MY_WEEK_CODE -> {
                ///  MyGroupUpFragment
                val fragment = oneFragmentList[14] as MyGroupUpFragment
                fragment.code = 2
                customViewPager.currentItem = 14

            }

            Constant.MAIN_STADUY_CODE -> {
                val studyFragment = oneFragmentList[15] as StudyFragment
                studyFragment.bookInfo = info

                //  StudyFragment
                customViewPager.currentItem = 15

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {

            val pp = customViewPager.currentItem
            val ff = oneFragmentList[pp]

            return if (ff is TestDetailsFragment) {

                Log.i("result", "不能退出---")

                ff.indexTime != 0

            } else {

                Log.i("result", "可以退出---")

                backTo()

                false

            }
        }
        return onKeyDown(keyCode, event)

    }
}