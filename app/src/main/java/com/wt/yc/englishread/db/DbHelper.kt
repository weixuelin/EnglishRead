package com.wt.yc.englishread.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, TABLE, null, VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL(CREATE_SQL)
        p0.execSQL(CREATE_VOICE_SQL)

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

//    "id":"515",
//    "english":"accident",
//    "chinese":"n.意外事件,事故",
//    "ipa":"æksɪdənt",
//    "video":"/Public/Home/video/a/accident.wav",
//    "english_example":null,
//    "chinese_example":null,
//    "icon":null

    companion object {
        val TABLE = "data.db"
        val VERSION = 1

        val ENGLISH_TABLE_NAME = "english_data"

        val BOOK_ID = "book_id"
        val ENGLISH_NAME = "english"
        val ENGLISH_ID = "id"
        val ENGLISH_CHINESE = "chinese"
        val ENGLISH_IPA = "ipa"
        val ENGLISH_VIDEO = "video"
        val ENGLISH_EXAMPLE = "english_example"
        val CHINESE_EXAMPLE = "chinese_example"
        val ENGLISH_ICON = "icon"

        val CREATE_SQL = "CREATE TABLE IF NOT EXISTS $ENGLISH_TABLE_NAME(id integer PRIMARY KEY, $BOOK_ID text, $ENGLISH_NAME text, $ENGLISH_ID text, $ENGLISH_CHINESE text, $ENGLISH_IPA text,$ENGLISH_VIDEO text,$ENGLISH_EXAMPLE text,$CHINESE_EXAMPLE text,$ENGLISH_ICON text)"


        val TABLE_NAME = "voice"

        val WORD_NAME = "name"

        val WORD_PATH = "path"

        val CREATE_VOICE_SQL = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(id integer PRIMARY KEY, $WORD_NAME text, $WORD_PATH text)"


    }

}