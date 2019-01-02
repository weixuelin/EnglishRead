package com.wt.yc.englishread.tts

import android.content.Context
import android.os.Handler
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import com.xin.lv.yang.utils.utils.AppUtils
import com.xin.lv.yang.utils.utils.FileUtils
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


/**
 * 有道语音合成服务
 */
class TTSForApi(val context: Context) {

    val url = "http://openapi.youdao.com/ttsapi"

    val q = ""
    val langType = "en"
    val appKey = TTSForApi.appKey
    var salt = ""
    var sign = ""
    val format = "mp3"
    val voice = "3"
    val secre_key = TTSForApi.secre_key


    companion object {
        val SUCCESS = 1234
        val appKey = "1525f677c96cebef"
        val secre_key = "s10HpXbbsesy1AfeLbOWPAH2spbaTQrI"
    }


    /**
     *
     * @param result 音频字节流
     * @param file 存储路径
     */
    private fun byte2File(result: ByteArray, file: String): Boolean {
        val audioFile = File(file)
        var fos: FileOutputStream? = null

        if (audioFile.exists()) {
            audioFile.createNewFile()
        }

        Log.i("result", "-------" + audioFile.exists())

        try {
            fos = FileOutputStream(audioFile)
            fos.write(result)

            return true

        } catch (e: Exception) {

            return false

        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return false

            }
        }
    }


    fun start(text: String, handler: Handler) {
        Thread {
            kotlin.run {

                val byteArr = requestTTSorHttp(createUrl(text))

                Log.i("result", "------------${byteArr.toString()}")

                val fileName = "${System.currentTimeMillis()}.mp3"

                val file = FileUtils.byte2File(byteArr, FileUtils.getCachePath(context), fileName)

                if (file.exists()) {

                    Log.i("result", "-------成功----")

                    val message = handler.obtainMessage()
                    message.obj = file.path
                    message.what = SUCCESS
                    handler.sendMessage(message)
                }
            }

        }.start()

    }


    fun createUrl(text: String): String {

        salt = System.currentTimeMillis().toString()
        sign = md5("$appKey$text$salt$secre_key")

        val str = "$url?q=${encode(text)}&langType=$langType&appKey=$appKey&salt=$salt&sign=$sign&format=$format&voice=$voice"

        Log.i("result", "---------$str")

        return str

    }

    /**
     * 请求语音合成服务
     * @param ttsUrl
     * @return
     */
    private fun requestTTSorHttp(ttsUrl: String): ByteArray? {
        val httpClient = OkHttpClient()
        val get = Request.Builder().url(ttsUrl).get().build()

        val response = httpClient.newCall(get).execute()

        val header = response.headers()

        Log.i("result", "hhhhhhh-----" + header.toString())

        return if ("audio/mp3" == header["Content-Type"]) {

            if (response.isSuccessful) {

                val byteResult = response.body()!!.bytes()

                byteResult

            } else {
                null
            }
        } else {
            null
        }
    }


    /**
     * 生成32位MD5摘要
     * @param string
     * @return
     */
    fun md5(string: String): String {

        return AppUtils.MD5(string)

    }


    /**
     * 根据api地址和参数生成请求URL
     * @param url
     * @param params
     * @return
     */
    fun getUrlWithQueryString(url: String, params: Map<String, String>?): String {
        if (params == null) {
            return url
        }

        val builder = StringBuilder(url)
        if (url.contains("?")) {
            builder.append("&")
        } else {
            builder.append("?")
        }

        var i = 0
        for (key in params.keys) {
            val value = params[key]
                    ?: // 过滤空的key
                    continue

            if (i != 0) {
                builder.append('&')
            }

            builder.append(key)
            builder.append('=')
            builder.append(encode(value))

            i++
        }

        return builder.toString()
    }

    /**
     * 进行URL编码
     * @param input
     * @return
     */
    fun encode(input: String?): String? {
        try {
            return URLEncoder.encode(input, "utf-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return input
    }


}