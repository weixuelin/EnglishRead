package com.wt.yc.englishread.base

import android.content.Context
import com.wt.yc.englishread.info.UserInfo

class Share {

    companion object {

        val USER = "user"
        val ACCOUNT="account"

        fun getToken(context: Context): String = context.getSharedPreferences(USER, Context.MODE_PRIVATE).getString("token", "")

        fun getUid(context: Context): Int = context.getSharedPreferences(USER, Context.MODE_PRIVATE).getInt("uid", 0)


        fun saveTokenAndUid(context: Context, userInfo: UserInfo?) {
            val edit = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit()
            edit.putString("token", userInfo!!.token)
            edit.putInt("uid", userInfo.id!!)
            edit.apply()

        }


        /**
         * 保存账号和密码
         */
        fun saveAccountOrPwd(context: Context, account: String, pwd: String) {
            val edit = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE).edit()
            edit.putString("account", account)
            edit.putString("pwd", pwd)
            edit.apply()
        }

        fun getAccount(context: Context) = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE).getString("account", "")

        fun clearUser(context: Context) {
            val edit = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit()
            edit.clear()
            edit.apply()
        }


        /**
         * 保存单元信息id
         */
        fun saveUnitId(context: Context, unitId: Int) {
            val edit = context.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE).edit()
            edit.putInt("unitId", unitId)
            edit.apply()

        }

        fun getUnitId(context: Context) = context.getSharedPreferences(ACCOUNT,Context.MODE_PRIVATE).getInt("unitId",0)

    }
}