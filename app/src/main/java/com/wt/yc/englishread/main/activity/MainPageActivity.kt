package com.wt.yc.englishread.main.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.widget.LinearLayout
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.*
import com.wt.yc.englishread.info.BookInfo
import com.wt.yc.englishread.info.Info
import com.wt.yc.englishread.info.UserInfo
import com.wt.yc.englishread.main.adapter.LoadAdapter
import com.wt.yc.englishread.main.fragment.expandtest.*
import com.wt.yc.englishread.main.fragment.intelligence.IntelligenceTestFragment
import com.wt.yc.englishread.main.fragment.mainpage.*
import com.wt.yc.englishread.main.fragment.statistics.*
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

            ImageUtil.getInstance().loadCircleImage(this, userPicHead, "", R.drawable.head_pic)

            tvUserName.text = user!!.username
            tvAllMoney.text = user.gold
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_page_layout)

        val ww = resources.getDimension(R.dimen.dp_120).toInt()
        pageLeadRecyclerView.layoutParams = LinearLayout.LayoutParams(ww, LinearLayout.LayoutParams.WRAP_CONTENT)

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


    var indexFragment: ProV4Fragment? = null

    private fun initCustomViewPager() {

        val ttt = supportFragmentManager.beginTransaction()

        indexFragment = PageFragment()

        ttt.addToBackStack("PageFragment")

        indexFragment = switchContent(indexFragment!!, indexFragment!!, R.id.mainPageFrame, ttt)


    }


    /**
     * 返回上一个界面
     */
    fun backTo() {

        val fragmentNum = supportFragmentManager.backStackEntryCount

        Log.i("result", "------$fragmentNum")

        if (fragmentNum != 0) {
            val backStack = supportFragmentManager.getBackStackEntryAt(fragmentNum - 1)
            // 获取当前栈顶的Fragment的标记值
            val tag = backStack.name

            Log.i("result", "------$tag")

            when (tag) {

                "PageFragment", "StudyFragment", "TestDetailsFragment" -> finish()

                else -> {
                    supportFragmentManager.popBackStack()
                }
            }

        } else {

            finish()

        }

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
            val tttt = supportFragmentManager.beginTransaction()

            val fff = UserFragment()
            tttt.addToBackStack("UserFragment")

            indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)


        }

        userPicHead.setOnClickListener {

            val tttt = supportFragmentManager.beginTransaction()

            val fff = UserFragment()
            tttt.addToBackStack("UserFragment")

            indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)

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
        startActivity(Intent(this, LoginActivity::class.java))

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

                if (isTestCode) {
                    showToastShort("正在进行测试！！")
                } else {
                    mainJoup(p2)
                }

                true
            }
        }

        pageLeadRecyclerView.setOnChildClickListener { p0, p1, p2, p3, p4 ->

            if (isTestCode) {
                showToastShort("正在进行测试！！")
            } else {
                adapter!!.twoUpdate(p2, p3)
                toSonJoup(p2, p3)
            }

            true
        }

    }


    /**
     * 二级点击跳转
     */
    private fun toSonJoup(personId: Int, p3: Int) {

        val tttt = supportFragmentManager.beginTransaction()

        supportFragmentManager.popBackStack()

        when (personId) {
            2 -> {

                when (p3) {
                    0 -> {

                        val fragment = MyGroupUpFragment()
                        fragment.code = 1

                        tttt.addToBackStack("MyGroupUpFragment")

                        indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)

                    }

                    1 -> {

                        tttt.addToBackStack("MyGroupUpFragment")
                        val fragment = MyGroupUpFragment()
                        fragment.code = 2
                        indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)

                    }

                    2 -> {

                        tttt.addToBackStack("AllTestGradeFragment")
                        val fragment = AllTestGradeFragment()

                        indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)

                    }

                    3 -> {

                        tttt.addToBackStack("StudyTimeFragment")
                        val fragment = StudyTimeFragment()

                        indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)


                    }
                    4 -> {
                        tttt.addToBackStack("WrongWordFragment")

                        val fragment = WrongWordFragment()

                        indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)
                    }
                }
            }

            3 -> {

                isTestCode = true

                val tttt = supportFragmentManager.beginTransaction()

                when (p3) {
                    0 -> {
                        ///  智能测试  生词测试
                        val f1 = TestDetailsFragment()
                        tttt.addToBackStack("TestDetailsFragment")

                        f1.testCode = 1

                        indexFragment = switchContent(indexFragment!!, f1, R.id.mainPageFrame, tttt)

                    }

                    1 -> {
                        ///  智能测试 熟词测试
                        val f2 = TestDetailsFragment()
                        f2.testCode = 3

                        tttt.addToBackStack("TestDetailsFragment")

                        indexFragment = switchContent(indexFragment!!, f2, R.id.mainPageFrame, tttt)

                    }

                    2 -> {
                        ///  智能测试 综合测试
                        val f3 = TestDetailsFragment()
                        f3.testCode = 2

                        tttt.addToBackStack("TestDetailsFragment")

                        indexFragment = switchContent(indexFragment!!, f3, R.id.mainPageFrame, tttt)
                    }

                    3 -> {
                        ///  智能测试 一测到底界面

                        val fff = IntelligenceTestFragment()
                        tttt.addToBackStack("IntelligenceTestFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)

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


                val tttt = supportFragmentManager.beginTransaction()

                when (p3) {
                    0 -> {

                        val fff = KeyExerciseFragment()
                        tttt.addToBackStack("KeyExerciseFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)

                    }

                    1 -> {

                        val fff = LetterExerciseFragment()
                        tttt.addToBackStack("LetterExerciseFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)

                    }

                    2 -> {

                        val fff = SoundmarkExerciseFragment()
                        tttt.addToBackStack("SoundmarkExerciseFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)
                    }

                    3 -> {

                        val fff = HearingExerciseFragment()
                        tttt.addToBackStack("HearingExerciseFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)

                    }

                    4 -> {

                        val fff = ReadExerciseFragment()
                        tttt.addToBackStack("ReadExerciseFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)

                    }

                    5 -> {

                        val fff = WritingExerciseFragment()
                        tttt.addToBackStack("WritingExerciseFragment")
                        indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)


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
        val tttt = supportFragmentManager.beginTransaction()

        when (position) {
            0 -> {
                val fff = PageFragment()
                tttt.addToBackStack("PageFragment")
                indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)
            }

            1 -> {
                val fff = StudyFragment()
                tttt.addToBackStack("StudyFragment")
                indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)
            }

            3 -> {

            }

            4 -> {
                val fff = NewWordFragment()
                tttt.addToBackStack("NewWordFragment")
                indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)
            }

            5 -> {
                val fff = BookRackFragment()
                tttt.addToBackStack("BookRackFragment")
                indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)
            }

        }
    }


    /**
     * 到哪个界面
     * code 1 复习  2 单元测试  3 听写   4 听写训练
     */
    fun toWhere(code: Int, info: BookInfo?) {

        val tttt = supportFragmentManager.beginTransaction()

        when (code) {

            Constant.STUDY_REVIEW -> {

                /// 复习界面
                ///  ReviewFragment
                val rf = ReviewFragment()
                rf.rfInfo = info

                tttt.addToBackStack("ReviewFragment")
                indexFragment = switchContent(indexFragment!!, rf, R.id.mainPageFrame, tttt)

            }


            Constant.STUDY_TEST -> {
                /// 测试内容选择
                isTestCode = true

                val ff = TestDetailsFragment()
                ff.unitInfo = info

                tttt.addToBackStack("TestDetailsFragment")
                indexFragment = switchContent(indexFragment!!, ff, R.id.mainPageFrame, tttt)
            }

            Constant.LISTEN_CHOOSE_CODE -> {

                // 听写  汉译英  英译汉
                val listen = ListenChooseFragment()
                listen.code = info!!.code

                tttt.addToBackStack("ListenChooseFragment")
                indexFragment = switchContent(indexFragment!!, listen, R.id.mainPageFrame, tttt)


            }

            Constant.LISTEN_WRITE_CODE -> {
                // 听写
                val listenWrite = ListenWriteFragment()
                tttt.addToBackStack("ListenWriteFragment")
                indexFragment = switchContent(indexFragment!!, listenWrite, R.id.mainPageFrame, tttt)


            }

            Constant.LISTEN_READ_CODE -> {
                // 听读训练
                val listenRead = ListenReadFragment()
                tttt.addToBackStack("ListenReadFragment")
                indexFragment = switchContent(indexFragment!!, listenRead, R.id.mainPageFrame, tttt)


            }

            Constant.TEST_DETAILS_CODE -> {


            }

            Constant.ANSWER_RESULT -> {

                /// 答题完成后的成绩
                isTestCode = false

                val fff = AnswerFinishFragment()

                fff.bookInfo = info

                tttt.addToBackStack("AnswerFinishFragment")

                indexFragment = switchContent(indexFragment!!, fff, R.id.mainPageFrame, tttt)


            }

            Constant.MAIN_TIAO_ZHAN -> {
                // 首页挑战

            }

            Constant.MY_GROUP_UP_CODE -> {

                tttt.addToBackStack("MyGroupUpFragment")
                val fragment = MyGroupUpFragment()
                fragment.code = 1
                indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)

            }

            Constant.MY_WEEK_CODE -> {

                ///  MyGroupUpFragment
                tttt.addToBackStack("MyGroupUpFragment")
                val fragment = MyGroupUpFragment()

                fragment.code = 2

                indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)


            }

            Constant.ALL_TEST_SCORE -> {
                tttt.addToBackStack("AllTestGradeFragment")
                val fragment = AllTestGradeFragment()
                indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)
            }

            Constant.STUDY_TIME -> {
                tttt.addToBackStack("StudyTimeFragment")
                val fragment = StudyTimeFragment()
                indexFragment = switchContent(indexFragment!!, fragment, R.id.mainPageFrame, tttt)
            }

            Constant.MAIN_STADUY_CODE -> {

                val studyFragment = StudyFragment()
                studyFragment.bookInfo = info

                tttt.addToBackStack("StudyFragment")
                indexFragment = switchContent(indexFragment!!, studyFragment, R.id.mainPageFrame, tttt)

            }
        }
    }


    /**
     * 是否正在进行单元测试  true  正在测试   false 测试完成
     */
    var isTestCode = false

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        if (keyCode == KeyEvent.KEYCODE_BACK &&
                event!!.action == KeyEvent.ACTION_DOWN) {

            return if (indexFragment is TestDetailsFragment) {

                Log.i("result", "不能退出---")

                if (isTestCode) {

                    showToastShort("正在测试")

                } else {
                    backTo()

                }

                true

            } else {

                Log.i("result", "可以退出---")

                backTo()

                true

            }
        }

        return false

    }
}