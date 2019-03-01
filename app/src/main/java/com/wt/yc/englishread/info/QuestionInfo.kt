package com.wt.yc.englishread.info

import android.os.Parcel
import android.os.Parcelable

class QuestionInfo() : Parcelable {

    var word_id: Int = 0

    var title: String = ""
    var ipa: String = ""
    var number: Int = 0

    var answer: ArrayList<AnswerInfo>? = null

    constructor(parcel: Parcel) : this() {
        word_id = parcel.readInt()
        title = parcel.readString()
        ipa = parcel.readString()
        number = parcel.readInt()
    }


    inner class AnswerInfo {
        var dn: String = ""
        var status: Int = 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(word_id)
        parcel.writeString(title)
        parcel.writeString(ipa)
        parcel.writeInt(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuestionInfo> {
        override fun createFromParcel(parcel: Parcel): QuestionInfo {
            return QuestionInfo(parcel)
        }

        override fun newArray(size: Int): Array<QuestionInfo?> {
            return arrayOfNulls(size)
        }
    }

}