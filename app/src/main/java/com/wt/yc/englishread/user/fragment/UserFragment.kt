package com.wt.yc.englishread.user.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.Config
import com.wt.yc.englishread.base.ProV4Fragment
import com.wt.yc.englishread.info.UserInfo
import com.wt.yc.englishread.main.activity.MainPageActivity
import com.wt.yc.englishread.user.LoginActivity
import com.xin.lv.yang.utils.utils.HttpUtils
import com.xin.lv.yang.utils.utils.ImageUtil
import kotlinx.android.synthetic.main.user_fragment_layout.*
import org.json.JSONObject

class UserFragment : ProV4Fragment() {

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.user_fragment_layout, container, false)

    }

    override fun handler(msg: Message) {

        val str = msg.obj as String
        when (msg.what) {

            Config.GET_USER_INFO_CODE -> {
                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)
                if (code == Config.SUCCESS) {
                    val result = json.optString(Config.DATA)
                    val user = gson!!.fromJson<UserInfo>(result, UserInfo::class.java)
                    showUserInfo(user)
                }
            }

            Config.UPDATE_PWD_CODE -> {

                removeLoadDialog()
                val json = JSONObject(str)
                val code = json.optInt(Config.CODE)

                if (code == Config.SUCCESS) {

                    showLoadDialog(activity!!, "修改成功")

                    (activity!! as MainPageActivity).backTo()
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
        }
    }

    private fun showUserInfo(user: UserInfo?) {
        ImageUtil.getInstance().loadCircleImage(activity!!, imageUserHead, "", R.drawable.head_pic)
        tvUserInfoName.text = user!!.username
        tvSchool.text = user.school
        tvClass.text = user.class_name
        tvName.text = ""


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvBack.setOnClickListener {
            (activity!! as MainPageActivity).backTo()
        }

        getUserInfo()

        buttonSure.setOnClickListener {
            save()
        }


    }

    fun save() {
        val oldPwd = etOldNumber.text.toString()
        val newPwd = etNewNumber.text.toString()
        val pwdAgain = etNewAgain.text.toString()
        when {
            oldPwd == "" -> showShortToast(activity!!, "请输入原始密码")
            newPwd == "" -> showShortToast(activity!!, "请输入新密码")
            pwdAgain == "" -> showShortToast(activity!!, "请确认密码")
            newPwd != pwdAgain -> showShortToast(activity!!, "两次密码输入不一致，请重新输入")
            else -> {
                val json = JSONObject()
                json.put("uid", uid)
                json.put("token", token)
                json.put("old_pwd", oldPwd)
                json.put("new_pwd", newPwd)
                HttpUtils.getInstance().postJson(Config.UPDATE_PWD_URL, json.toString(), Config.UPDATE_PWD_CODE, handler)
                showLoadDialog(activity!!)

            }
        }
    }


    private fun getUserInfo() {
        val json = JSONObject()
        json.put("uid", uid)
        json.put("token", token)
        HttpUtils.getInstance().postJson(Config.GET_USER_INFO_URL, json.toString(), Config.GET_USER_INFO_CODE, handler)
    }

}