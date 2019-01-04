package com.wt.yc.englishread.info

import android.os.Parcel
import android.os.Parcelable

class UserInfo() : Parcelable {
    var id: Int? = 0
    var username: String? = ""
    var mobile: String? = ""
    var token: String? = ""
    var gold: String? = ""
    var user_type: Int = 0
    var school: String? = ""
    var class_name: String? = ""

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        username = parcel.readString()
        mobile = parcel.readString()
        token = parcel.readString()
        gold = parcel.readString()
        user_type = parcel.readInt()
        school = parcel.readString()
        class_name = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(username)
        parcel.writeString(mobile)
        parcel.writeString(token)
        parcel.writeString(gold)
        parcel.writeInt(user_type)
        parcel.writeString(school)
        parcel.writeString(class_name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserInfo> {
        override fun createFromParcel(parcel: Parcel): UserInfo {
            return UserInfo(parcel)
        }

        override fun newArray(size: Int): Array<UserInfo?> {
            return arrayOfNulls(size)
        }
    }


}