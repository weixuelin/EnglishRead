package com.wt.yc.englishread.main.fragment.expandtest

import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wt.yc.englishread.R
import com.wt.yc.englishread.base.ProV4Fragment
import com.xin.lv.yang.utils.utils.FileUtils
import kotlinx.android.synthetic.main.writing_layout.*
import com.youdao.ocr.online.OcrErrorCode
import com.youdao.ocr.online.OCRResult
import com.youdao.ocr.online.OCRListener
import com.youdao.ocr.online.ImageOCRecognizer
import com.youdao.ocr.online.OCRParameters


/**
 * 写作练习
 */
class WritingExerciseFragment : ProV4Fragment() {
    override fun handler(msg: Message) {

    }

    override fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.writing_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvWritingBack.setOnClickListener {
            fragmentManager!!.popBackStack()
        }


        buttonTi.setOnClickListener {
            val bitmap = writingTextView.cachebBitmap
            initOCR(FileUtils.bitmapToBase64(bitmap))
        }


    }

    private fun initOCR(base64: String) {
        // OCR识别
        val tps = OCRParameters.Builder()
                .source("youdaoocr").timeout(100000)
                .type("10011").lanType("zh-en").build()

        // type识别类型，目前仅支持10011，表示片段识别
        // lanType支持"zh-en"和"en"，其中"zh-en"为中英识别，"en"参数表示只识别英文。
        // 若为纯英文识别，"zh-en"的识别效果不如"en"，请妥善选择

        Log.i("result", "-base64-----$base64")

        ImageOCRecognizer.getInstance(tps).recognize(base64, object : OCRListener {
                    override fun onResult(result: OCRResult, input: String) {
                        // 识别成功
                        Log.i("result", "------$input")
                    }

                    override fun onError(error: OcrErrorCode) {
                        // 识别失败
                    }
                })
    }
}