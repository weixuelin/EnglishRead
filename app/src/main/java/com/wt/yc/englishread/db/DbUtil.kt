package com.wt.yc.englishread.db

import android.content.ContentValues
import android.content.Context
import com.wt.yc.englishread.info.BookInfo

class DbUtil(context: Context) {
    private var dbHelper: DbHelper? = null

    init {
        dbHelper = DbHelper(context)
    }

    fun addBook() {
        val sql = dbHelper!!.readableDatabase

    }

    fun saveData(textStr: String, filePath: String) {
        val sql = dbHelper!!.readableDatabase
        val values = ContentValues()
        values.put(DbHelper.WORD_NAME, textStr)
        values.put(DbHelper.WORD_PATH, filePath)

        sql.insert(DbHelper.TABLE_NAME, null, values)

    }

    fun getVoice(textStr: String): String? {
        val sql = dbHelper!!.readableDatabase
        val c = sql.query(DbHelper.TABLE_NAME, null, "${DbHelper.WORD_NAME}=?", arrayOf(textStr), null, null, null, null)
        if (c.moveToNext()) {
            return c.getString(c.getColumnIndex(DbHelper.WORD_PATH))
        }

        c.close()

        return ""

    }


    fun getWord(): ArrayList<BookInfo> {

        return arrayListOf()
    }
}