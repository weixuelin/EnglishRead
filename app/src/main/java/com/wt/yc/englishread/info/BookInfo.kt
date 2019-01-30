package com.wt.yc.englishread.info

import android.os.Parcel
import android.os.Parcelable

class BookInfo() : Parcelable {
    var book_id: Int = 0
    var book_name: String = ""
    var unit_id: Int = 0
    var unit_name: String = ""
    var id: Int = 0
    var english: String = ""
    var sxc: Int = 0
    var jsc: Int = 0
    var msc: Int = 0
    var xs: String = ""
    var sx: String = ""
    var xx: String = ""
    var fx: String = ""
    var time: String = ""
    var type: String = ""
    var test_time: String = ""
    var test_cj: String = ""
    var status: Int = 0
    var unit_num: Int = 0
    var video: String? = ""
    var zpm: Int = 0
    var cj: Int = 0
    var study_word: Int = 0
    var jd: Int = 0
    var code: Int = 0
    var chinese: String = ""
    var ipa: String = ""
    var english_example: String = ""
    var chinese_example: String = ""
    var userName=""
    var testTime=""
    var score=""

    constructor(parcel: Parcel) : this() {
        book_id = parcel.readInt()
        book_name = parcel.readString()
        unit_id = parcel.readInt()
        unit_name = parcel.readString()
        id = parcel.readInt()
        english = parcel.readString()
        sxc = parcel.readInt()
        jsc = parcel.readInt()
        msc = parcel.readInt()
        xs = parcel.readString()
        sx = parcel.readString()
        xx = parcel.readString()
        fx = parcel.readString()
        time = parcel.readString()
        type = parcel.readString()
        test_time = parcel.readString()
        test_cj = parcel.readString()
        status = parcel.readInt()
        unit_num = parcel.readInt()
        video = parcel.readString()
        zpm = parcel.readInt()
        cj = parcel.readInt()
        study_word = parcel.readInt()
        jd = parcel.readInt()
        code = parcel.readInt()
        chinese = parcel.readString()
        ipa = parcel.readString()
        english_example = parcel.readString()
        chinese_example = parcel.readString()
        userName = parcel.readString()
        testTime = parcel.readString()
        score = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(book_id)
        parcel.writeString(book_name)
        parcel.writeInt(unit_id)
        parcel.writeString(unit_name)
        parcel.writeInt(id)
        parcel.writeString(english)
        parcel.writeInt(sxc)
        parcel.writeInt(jsc)
        parcel.writeInt(msc)
        parcel.writeString(xs)
        parcel.writeString(sx)
        parcel.writeString(xx)
        parcel.writeString(fx)
        parcel.writeString(time)
        parcel.writeString(type)
        parcel.writeString(test_time)
        parcel.writeString(test_cj)
        parcel.writeInt(status)
        parcel.writeInt(unit_num)
        parcel.writeString(video)
        parcel.writeInt(zpm)
        parcel.writeInt(cj)
        parcel.writeInt(study_word)
        parcel.writeInt(jd)
        parcel.writeInt(code)
        parcel.writeString(chinese)
        parcel.writeString(ipa)
        parcel.writeString(english_example)
        parcel.writeString(chinese_example)
        parcel.writeString(userName)
        parcel.writeString(testTime)
        parcel.writeString(score)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookInfo> {
        override fun createFromParcel(parcel: Parcel): BookInfo {
            return BookInfo(parcel)
        }

        override fun newArray(size: Int): Array<BookInfo?> {
            return arrayOfNulls(size)
        }
    }


}