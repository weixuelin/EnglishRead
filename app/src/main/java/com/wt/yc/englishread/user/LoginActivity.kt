package com.wt.yc.englishread.user

import android.content.Intent
import android.os.Bundle
import android.os.Message
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProActivity
import com.wt.yc.englishread.base.Share
import com.wt.yc.englishread.info.UserInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import com.xin.lv.yang.utils.utils.ImageCode
import kotlinx.android.synthetic.main.login_layout.*
import org.json.JSONObject

/**
 * 登录
 */
class LoginActivity : ProActivity() {

    override fun handler(msg: Message) {

        val str = msg.obj as String
        when (msg.what) {
            Config.LOGIN_CODE -> {
                removeLoadDialog()
                val json = JSONObject(str)
                val code = json.optInt("code")
                val message = json.optString(Config.MSG)

                showToastShort(message)

                if (code == Config.SUCCESS) {

                    val result = json.optString("data")
                    val userInfo = gson!!.fromJson<UserInfo>(result, UserInfo::class.java)

                    Share.saveTokenAndUid(this, userInfo)

                    startActivity(Intent(this, MainPageActivity::class.java))

                    finish()

                }

            }

        }

    }


    var code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Share.getUid(this) != 0) {

            startActivity(Intent(this, MainPageActivity::class.java))
            finish()

        } else {


            setContentView(R.layout.login_layout)

            val account = Share.getAccount(this)
            etAccount.setText(account)

            val bitmap = ImageCode.getInstance().createBitmap()
            code = ImageCode.getInstance().code
            imageCode.setImageBitmap(bitmap)

            imageCode.setOnClickListener {
                val bb = ImageCode.getInstance().createBitmap()
                code = ImageCode.getInstance().code
                imageCode.setImageBitmap(bb)

            }

            imageBack.setOnClickListener {
                finish()
            }

            btLogin.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        val account = etAccount.text.toString()
        val pwd = etPwd.text.toString()
        val imageYan = etImageYan.text.toString()
        when {
            account == "" -> showToastShort("请输入账号")
            pwd == "" -> showToastShort("请输入密码")
            imageYan == "" -> showToastShort("请输入图片验证码")
            imageYan.toUpperCase() != code.toUpperCase() -> showToastShort("图片验证码错误")
            else -> {
                val json = JSONObject()
                json.put("user", account)
                json.put("password", pwd)

                HttpUtils.getInstance().postJson(Config.LOGIN, json.toString(), Config.LOGIN_CODE, handler)
                showLoadDialog("登录中")

                Share.saveAccountOrPwd(this, account, pwd)

            }
        }
    }
}