package com.wt.yc.englishread.user

import android.os.Bundle
import android.os.Message
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProActivity
import com.xin.lv.yang.utils.utils.ImageCode
import kotlinx.android.synthetic.main.login_layout.*

/**
 * 登录
 */
class LoginActivity : ProActivity() {

    override fun handler(msg: Message) {
        val str = msg.obj as String
        when (msg.what) {

        }

    }


    var code = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        val bitmap = ImageCode.getInstance().createBitmap()
        code = ImageCode.getInstance().code
        imageCode.setImageBitmap(bitmap)

        imageCode.setOnClickListener {
            val bb = ImageCode.getInstance().createBitmap()
            code = ImageCode.getInstance().code
            imageCode.setImageBitmap(bb)

        }

        imageBack.setOnClickListener {
            finish()
        }
    }
}