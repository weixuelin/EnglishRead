package com.wt.yc.englishread.info

class QuestionInfo {
    var word_id: Int = 0
    var title: String = ""
    var ipa: String = ""
    var number: Int = 0
    var answer: ArrayList<AnswerInfo>? = null


    inner class AnswerInfo {
        var dn: String = ""
        var status: Int = 0
    }

}