package com.wt.yc.englishread

import android.content.ContentValues
import android.content.Context


class DbUtil(context: Context) {
    var helper: DbHelper? = null

    init {
        helper = DbHelper(context)
    }


    fun saveData(textStr: String, filePath: String) {
        val sql = helper!!.readableDatabase
        val values = ContentValues()
        values.put(DbHelper.WORD_NAME, textStr)
        values.put(DbHelper.WORD_PATH, filePath)

        sql.insert(DbHelper.TABLE_NAME, null, values)

    }

    fun getVoice(textStr: String): String? {
        val sql = helper!!.readableDatabase
        val c = sql.query(DbHelper.TABLE_NAME, null, "${DbHelper.WORD_NAME}=?", arrayOf(textStr), null, null, null, null)
        if (c.moveToNext()) {
            return c.getString(c.getColumnIndex(DbHelper.WORD_PATH))
        }

        c.close()

        return ""

    }

}