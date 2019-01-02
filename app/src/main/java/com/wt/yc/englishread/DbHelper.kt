package com.wt.yc.englishread

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, TABLE, FACTORY, VERSION) {

    companion object {
        val TABLE = "data.db"
        val VERSION = 1
        val FACTORY: SQLiteDatabase.CursorFactory? = null

        /**
         * 数据库名
         */
        val TABLE_NAME = "voice"

        /**
         * 单次名称
         */
        val WORD_NAME = "name"
        /**
         * 单次阅读语音
         */
        val WORD_PATH = "path"

        val CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(id int primary key, $WORD_NAME text, $WORD_PATH text)"


    }


    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL(CREATE_TABLE_SQL)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}