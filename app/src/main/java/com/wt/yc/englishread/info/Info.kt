package com.wt.yc.englishread.info

class Info {
    var pic: Int? = 0
    var picClick: Int? = 0
    var title: String? = ""
    var click: Boolean = false
    var isErr: Int = 1
    var isLock: Int = 1
    var code: Int = 0
    var isSonOpen: Boolean = false
    var answer: String = ""
    var icon: String = ""

    var sonList: ArrayList<Son>? = null
    var id: Int = 0


    companion object {
        class Son {
            var str: String? = ""
            var click: Boolean = false
        }
    }
}