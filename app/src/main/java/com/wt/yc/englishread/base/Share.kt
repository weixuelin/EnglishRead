package com.wt.yc.englishread.base

import android.content.Context
import com.wt.yc.englishread.info.UserInfo

class Share {

    companion object {

        val USER = "user"

        fun getToken(context: Context): String = context.getSharedPreferences(USER, Context.MODE_PRIVATE).getString("token", "")

        fun getUid(context: Context): Int = context.getSharedPreferences(USER, Context.MODE_PRIVATE).getInt("uid", 0)



        fun saveTokenAndUid(context: Context, userInfo: UserInfo?) {
            val edit = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit()
            edit.putString("token", userInfo!!.token)
            edit.putInt("uid", userInfo.id!!)
            edit.apply()

        }

        fun saveAccountOrPwd(context: Context, account: String, pwd: String) {
            val edit = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit()
            edit.putString("account", account)
            edit.putString("pwd", pwd)
            edit.apply()
        }

        fun getAccount(context: Context)= context.getSharedPreferences(USER, Context.MODE_PRIVATE).getString("account", "")

    }
}