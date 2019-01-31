package com.wt.yc.englishread.base

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.*
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import butterknife.Unbinder
import com.google.gson.Gson
import com.wt.yc.englishread.DbUtil
import com.wt.yc.englishread.R
import com.wt.yc.englishread.tts.TTSForApi
import com.xin.lv.yang.utils.utils.ImageUtil
import com.xin.lv.yang.utils.view.CustomProgressDialog
import com.xin.lv.yang.utils.view.CustomToast
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.ref.WeakReference

abstract class ProV4Fragment : Fragment() {

    open var gson: Gson? = null
    var unbinder: Unbinder? = null
    var activity: Activity? = null

    var uid: Int = 0
    var token: String = ""
    var handler: Handler? = null

    var player: MediaPlayer? = null

    var dbUtil: DbUtil? = null
    var ttsApi: TTSForApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = getActivity()
        handler = ProHandler(this)

        gson = Gson()

        uid = Share.getUid(activity!!)
        token = Share.getToken(activity!!)

        ttsApi = TTSForApi(activity!!)

        dbUtil = DbUtil(activity!!)

    }


    abstract fun handler(msg: Message)

    companion object {

        class ProHandler(proV4Fragment: ProV4Fragment) : Handler(Looper.getMainLooper()) {

            var weak: WeakReference<ProV4Fragment>? = null

            init {
                weak = WeakReference(proV4Fragment)

            }

            val a = weak!!.get()

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                a!!.handler(msg!!)

            }
        }

    }



    fun reOpenApp(){
        val i: Intent = activity!!.baseContext.packageManager.getLaunchIntentForPackage(activity!!.baseContext.packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity!!.startActivity(i)
    }

    var voiceFilePath: String? = ""

    fun playWordVoice(textStr: String) {
        voiceFilePath = dbUtil!!.getVoice(textStr)

        log("voice--------$voiceFilePath")

        if (voiceFilePath != null && voiceFilePath != "") {

            playVoice(activity!!, voiceFilePath!!)

        } else {

            ttsApi!!.start(textStr, object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    when (msg!!.what) {
                        TTSForApi.SUCCESS -> {

                            val filePath: String = msg.obj as String

                            voiceFilePath = filePath

                            log("bbbb-------$filePath")

                            dbUtil!!.saveData(textStr, filePath)

                            playVoice(activity!!, filePath)

                        }
                    }
                }
            })
        }

    }

    /**
     * 播放语音文件
     */
    fun playVoice(context: Context, voiceUrl: String) {
        player = MediaPlayer()
        player!!.setDataSource(context, Uri.parse(voiceUrl))
        player!!.prepareAsync()

        player!!.setOnPreparedListener { mp ->
            mp.start()
        }

        player!!.setOnCompletionListener {
            removeLoadDialog()
        }

    }


    fun playVoiceFromByte(context: Context, bb: ByteArray) {
        val tempMp3 = File.createTempFile("voice", "mp3", context.cacheDir)
        tempMp3.deleteOnExit()
        val fos = FileOutputStream(tempMp3)
        fos.write(bb)
        fos.close()

        val fis = FileInputStream(tempMp3)
        player!!.setDataSource(fis.fd)

        player!!.prepareAsync()
        player!!.setOnPreparedListener { mp ->
            mp.start()
        }

    }


    val isDebug: Boolean = true

    fun log(message: String) {
        if (isDebug) {
            Log.i("result", message)
        }

    }

    /**
     * 创建view
     */
    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = createView(inflater, container, savedInstanceState)

        return view
    }


    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null) {
            unbinder!!.unbind()
        }
    }


    fun showShortToast(activity: Activity, str: String, time: Int = Toast.LENGTH_SHORT) {

        CustomToast.showToast(activity,Gravity.CENTER,0,str)

///        Toast.makeText(activity, str, time).show()

    }


    /**
     * 设置间距
     */
    fun setMargen(ceshiTv: LinearLayout, margen: Int) {
        val lp: LinearLayout.LayoutParams = ceshiTv.layoutParams as LinearLayout.LayoutParams

        lp.setMargins(margen, margen, margen, margen)

        ceshiTv.layoutParams = lp
    }


    fun setMargenTop(layout: RelativeLayout, margen: Int) {
        val lp: LinearLayout.LayoutParams = layout.layoutParams as LinearLayout.LayoutParams

        lp.setMargins(0, margen, 0, 0)

        layout.layoutParams = lp
    }

    fun setAlpha(activity: Activity, f: Float) {
        val manager = activity.window.attributes
        manager.alpha = f
        activity.window.attributes = manager
    }

    fun showToastShort(context: Context, str: String) {

        CustomToast.showToast(context,Gravity.CENTER,0,str)

//        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()

    }

    var customProgressDialog: CustomProgressDialog? = null

    fun showLoadDialog(context: Context, string: String = "加载中") {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context)
        }
        customProgressDialog!!.setText(string)

        if (!customProgressDialog!!.isShowing) {
            customProgressDialog!!.show()
        }


    }

    /**
     * 复制信息到剪切板
     */
    fun copy(context: Context, text: String) {
        val manager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.text = text

    }

    /**
     * 移除load加载框
     */
    fun removeLoadDialog() {
        if (customProgressDialog != null) {
            if (customProgressDialog!!.isShowing) {
                customProgressDialog!!.dismiss()
            }
        }
    }


    fun setLine(searchView: SearchView, resId: Int) {
        try {
            val argClass = searchView.javaClass
            val ownField = argClass.getDeclaredField("mSearchPlate")
            ownField.isAccessible = true
            val mView = ownField.get(searchView) as View
            mView.setBackgroundColor(resId)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    var viewList: ArrayList<View>? = arrayListOf()

    /**
     * 顶部轮播图  1 为正常  2 为圆角
     */
    fun initViewPager(context: Context, picViewPager: ViewPager, picList: ArrayList<String>, handler: Handler, code: Int) {

        picViewPager.removeAllViews()

        viewList!!.clear()

        for (temp in picList) {

            if (temp != null && temp != "" && temp != "null") {

                val view: View = LayoutInflater.from(context).inflate(R.layout.image_layout, null)
                val imageView: ImageView = view.findViewById(R.id.img_image_view)

                val picUrl = when {
                    temp.startsWith("http") -> temp
                    temp.startsWith("/") -> Config.IP + temp
                    else -> temp
                }

                if (picUrl.startsWith("http")) {
                    if (code == 2) {
                        val r = context.resources.getDimension(R.dimen.dp_12).toInt()
                        ImageUtil.getInstance().loadRoundCircleImage(context, imageView, picUrl, 0, r)

                    } else {
                        ImageUtil.getInstance().loadImageNoTransformation(context, imageView, 0, picUrl)
                    }

                } else {
                    log("执行到此-------")
                    imageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, picUrl.toInt()))
                }

                viewList!!.add(view)
            }
        }

        picViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = viewList!!.size


            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(viewList!![position])
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view: View? = viewList!![position]

                container.addView(view)

                return viewList!![position]
            }
        }

        initRunnable(handler, picViewPager)

    }


    var viewpagerRunnable: Runnable? = null;


    private val TIME: Int = 5 * 1000;

    /**
     * 定时轮播
     */
    private fun initRunnable(handler: Handler, viewPager: ViewPager) {
        viewpagerRunnable = Runnable {
            val nowIndex = viewPager.currentItem;
            val count = viewPager.adapter!!.count;

            /// 如果下一张的索引大于最后一张，则切换到第一张
            if (nowIndex + 1 >= count) {
                viewPager.currentItem = 0;

            } else {
                viewPager.currentItem = nowIndex + 1;

            }

            handler.postDelayed(viewpagerRunnable, TIME.toLong());
        }

        handler.postDelayed(viewpagerRunnable, TIME.toLong())
    }


    fun getW(activity: Activity): Int {
        val att = activity.windowManager.defaultDisplay
        return att.width
    }

    fun getH(activity: Activity): Int {
        val att = activity.windowManager.defaultDisplay
        return att.height
    }


    /**
     * 获取虚拟按键的高度
     */
    fun getNavigationBarHeight(context: Context): Int {
        var result = 0;
        if (hasNavBar(context)) {
            val res = context.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    fun hasNavBar(context: Context): Boolean {
        val res = context.resources
        val resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            var hasNav = res.getBoolean(resourceId);
            // check override flag
            val sNavBarOverride = getNavBarOverride()
            if ("1" == sNavBarOverride) {
                hasNav = false
            } else if ("0" == sNavBarOverride) {
                hasNav = true
            }
            return hasNav;
        } else {
            return !ViewConfiguration.get(context).hasPermanentMenuKey()
        }
    }


    fun getNavBarOverride(): String {
        var sNavBarOverride = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                val c = Class.forName("android.os.SystemProperties");
                val m = c.getDeclaredMethod("get", String::class.java)
                m!!.isAccessible = true
                sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
            } catch (e: Throwable) {
            }
        }

        return sNavBarOverride
    }

}