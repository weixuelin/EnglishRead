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

        /**
         * 设置今日目标
         */
        val SET_TODAY_WORD_URL = "$IP/app/student/day_target"
        val SET_TODAY_CODE = 7

        /**
         * 获取学习的信息
         */
        val GET_STUDY_URL = "$IP/app/student/study"
        val GET_STUDY_CODE = 8

        /**
         * 学习 复习的单词
         */
        val REVIEW_WORD_URL = "$IP/app/student/word"
        val REVIRE_CODE = 9


        /**
         * 获取熟悉 陌生 夹生词
         */
        val GET_SHUXI_OR_MOSHENG_WORD_URL = "$IP/app/student/wordone"
        val GET_SHUXI_CODE = 91


        /**
         * 完成复习
         */
        val FINISH_REVIRE_URL = "$IP/app/student/end"
        val FINISH_CODE = 10

        /**
         * 我的生词本
         */
        val GET_ALL_WORD_URL = "$IP/app/student/scb"
        val GET_ALL_CODE = 11


        /**
         * 测试单词接口
         */
        val GET_TEST_URL = "$IP/app/student/paper"
        val GET_TEST_CODE = 12

        /**
         * 书籍信息
         */
        val GET_BOOK_LIST = "$IP/app/student/book"
        val GET_BOOK_CODE = 13

        /**
         * 交卷
         */
        val FINISH_TEST_URL = "$IP/app/student/post_paper"

        /**
         * 获取测试数据
         */
        val GET_SCORE_LIST = "$IP/App/Student/all_test"
        val GET_SCORE_LIST_CODE = 14

        /**
         * 一周学习图片
         */
        val GET_WEEK_URL = "$IP/App/Student/tk_weak"
        val GET_WEEK_CODE = 15

        /**
         * 学习时间统计
         */
        val GET_STUDY_TIME_URL = "$IP/App/Student/study_time"
        val GET_STUDY_TIME_CODE = 16


    }
}