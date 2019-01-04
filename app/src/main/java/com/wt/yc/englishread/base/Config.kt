package com.wt.yc.englishread.base

class Config {

    companion object {

        val CODE = "code"
        val DATA = "data"

        val SUCCESS = 200
        val CODE201 = 201

        val MSG = "msg"
        val STATUS = "status"


        val IP = "http://lianci.59156.cn"

        /**
         * 登录
         */
        val LOGIN = "$IP/app/public/login"
        val LOGIN_CODE = 1

        /**
         * 首页数据接口
         */
        val GET_MAIN_DATA = "$IP/app/index/index"
        val MAIN_DATA_CODE = 2

        /**
         * 每日签到
         */
        val SIGN_URL = "$IP/app/student/sign_in"
        val SIGN_CODE = 3

        /**
         * 学习首页数据
         */
        val GET_MAIN_PAGE_URL = "$IP/app/student/index"
        val MAIN_PAGE_CODE = 4

        /**
         *  获取用户信息
         */
        val GET_USER_INFO_URL = "$IP/app/student/find_user"
        val GET_USER_INFO_CODE = 5

        /**
         * 修改密码
         */
        val UPDATE_PWD_URL = "$IP/app/student/edit_pwd"
        val UPDATE_PWD_CODE = 6

    }
}