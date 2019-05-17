package com.wt.yc.englishread.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.*
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import android.telephony.TelephonyManager
import android.util.Log
import java.lang.reflect.Field
import java.util.regex.Pattern
import android.widget.LinearLayout
import android.util.TypedValue
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.google.gson.Gson
import com.wt.yc.englishread.App
import com.wt.yc.englishread.MainActivity
import com.wt.yc.englishread.R
import com.wt.yc.englishread.user.LoginActivity
import com.wt.yc.englishread.view.CustomPop
import com.xin.lv.yang.utils.utils.ImageUtil
import com.xin.lv.yang.utils.utils.StatusBarUtil
import com.xin.lv.yang.utils.view.CustomProgressDialog
import com.xin.lv.yang.utils.view.CustomToast
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


abstract class ProActivity : AppCompatActivity() {

    private val ERROR = 404
    private val ERROR_BUG = 405

    open var gson: Gson? = null
    open var handler: Handler? = null

    var inPutManager: InputMethodManager? = null

    var token: String = ""

    var uid: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            StatusBarUtil.transparencyBar(this)
//            StatusBarUtil.setStatusBarLightMode(window, true)
//
//        }

        gson = Gson()

        token = Share.getToken(this)
        uid = Share.getUid(this)

        inPutManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        handler = ProHandler(this)

        App.addActivity(this)

    }

    override fun onResume() {
        super.onResume()

    }

    abstract fun handler(msg: Message)


    val isDebug: Boolean = true

    fun log(message: String) {
        if (isDebug) {
            Log.i("result", message)
        }

    }


    /**
     * 检查存储权限
     */
    fun checkPhoto() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), CustomPop.PHOTO_PERMISSION_CODE)


    }


    /**
     * 检查照相机权限
     */
    fun checkCamera() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE), CustomPop.CAMERA_PERMISSION_CODE)

    }


    /**
     * 获取token
     */
    fun getToken() {

    }


    /**
     * 检查网络是否可用
     */
    fun checkNet(): Boolean {
        val manager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = manager.activeNetworkInfo
        return netInfo != null && netInfo.isAvailable

    }

    /**
     * 显示或者隐藏输入法
     */
    fun showInput() {
        if (inPutManager != null) {
            inPutManager!!.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 隐藏输入法
     */
    fun hideInPut(view: View) {
        if (inPutManager != null) {
            inPutManager!!.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getClipStr(context: Context): String {
        val manager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return manager?.text?.toString() ?: ""

    }


    /**
     * 复制信息到剪切板
     */
    fun copy(context: Context, text: String) {
        val manager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.text = text

    }

    fun showToastShort(s: String) {

////        CustomToast.showToast(this,Gravity.CENTER,0,s)

        Toast.makeText(this, s, Toast.LENGTH_LONG).show()

    }

    fun getW(): Int {
        val att = windowManager.defaultDisplay
        return att.width
    }

    fun getH(): Int {
        val att = windowManager.defaultDisplay
        return att.height
    }

    fun setAlpha(f: Float) {
        val manager = window.attributes
        manager.alpha = f
        window.attributes = manager
    }


    private var loadDialog: CustomProgressDialog? = null

    /**
     * 创建加载框
     */
    fun showLoadDialog(string: String = "加载中") {
        if (loadDialog == null) {
            loadDialog = CustomProgressDialog(this)
        }

        loadDialog!!.setText(string)
        loadDialog!!.show()

    }


    /**
     * 移除加载框
     */
    fun removeLoadDialog() {
        if (loadDialog != null && loadDialog!!.isShowing && !this.isFinishing) {
            loadDialog!!.dismiss()
        }
    }


    @SuppressLint("MissingPermission")
    fun getImei(context: Context): String {
        val manager = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val id = manager.deviceId
        Log.i("result", "---ididid----$id")
        return id

    }

    /**
     * 检测apk是否存在
     */
    fun checkApk(activity: Activity, pack: String): Boolean {
        val info: ApplicationInfo = activity.packageManager.getApplicationInfo(pack, 0)
        return info != null

    }

    /**
     * 获取系统版本号
     *
     * @return 系统版本号
     */
    fun getVersion(): String? {
        try {
            val manager = this.packageManager
            val info = manager.getPackageInfo(this.packageName, 0)
            return info.versionCode.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


    /**
     * 获取两个时间戳间隔的天数
     */
    fun getDayByData(start: Long, end: Long): Int {

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

        val fromCalendar: Calendar = Calendar.getInstance()
        fromCalendar.time = format.parse(format.format(start * 1000))
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0)
        fromCalendar.set(Calendar.SECOND, 0)
        fromCalendar.set(Calendar.MILLISECOND, 0)

        val toCalendar: Calendar = Calendar.getInstance()
        toCalendar.time = format.parse(format.format(end * 1000))

        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0)
        toCalendar.set(Calendar.SECOND, 0)
        toCalendar.set(Calendar.MILLISECOND, 0)

        return ((toCalendar.time.time - fromCalendar.time.time) / (1000 * 60 * 60 * 24)).toInt()

    }


    protected var str = "1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}"


    /**
     * 判断是否是电话号码
     *
     * @param mobiles 电话号码字符串
     * @return 是否为电话号码  true 是   false  不是
     */
    fun isMobileNum(mobiles: String): Boolean {
        if (mobiles.length == 11) {
            val p = Pattern.compile(str)
            val m = p.matcher(mobiles)
            return m.matches()
        } else {
            return false
        }
    }


    /**
     * TabLayout 设置下画线的长度
     *
     * @param tabs     TabLayout对象
     * @param leftDip  左边的距离
     * @param rightDip 右边的距离
     */
    fun setIndicator(tabs: TabLayout, leftDip: Int, rightDip: Int) {
        val tabLayout = tabs.javaClass
        var tabStrip: Field? = null
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        tabStrip!!.isAccessible = true
        var llTab: LinearLayout? = null
        try {
            llTab = tabStrip.get(tabs) as LinearLayout
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        val left = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip.toFloat(), Resources.getSystem().getDisplayMetrics()).toInt()
        val right = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip.toFloat(), Resources.getSystem().getDisplayMetrics()).toInt()

        for (i in 0 until llTab!!.childCount) {
            val child = llTab.getChildAt(i)
            child.setPadding(0, 0, 0, 0)
            val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            params.leftMargin = left
            params.rightMargin = right
            child.layoutParams = params
            child.invalidate()
        }

    }


    /**
     * 设置 navigationView 的样式
     *
     * @param navigationView 对象
     */
    @SuppressLint("RestrictedApi")
    fun disableShiftMode(navigationView: BottomNavigationView) {
        val menuView = navigationView.getChildAt(0) as BottomNavigationMenuView
        var shiftingMode: Field? = null
        try {
            shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        shiftingMode!!.isAccessible = true

        try {
            shiftingMode.setBoolean(menuView, false)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        shiftingMode.isAccessible = false

        for (i in 0 until menuView.childCount) {

            val itemView = menuView.getChildAt(i) as BottomNavigationItemView
            itemView.setShiftingMode(false)
            itemView.setChecked(itemView.itemData.isChecked)
        }
    }


    fun checkApkExist(context: Context, packageName: String): Boolean {
        if (packageName == null || packageName.equals("")) {
            return false;
        }
        try {
            val info: ApplicationInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return info != null;

        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    val viewList: ArrayList<View> = arrayListOf()


    fun initViewPager(picViewPager: ViewPager, picList: ArrayList<String>) {
        picViewPager.removeAllViews()

        for (temp in picList) {

            val view: View = layoutInflater.inflate(R.layout.image_layout, null)

            val imageView: ImageView = view.findViewById(R.id.img_image_view)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.adjustViewBounds = true

            if (temp.startsWith("http")) {
                ImageUtil.getInstance().loadImageNoTransformation(this, imageView, 0, temp)
            } else {
                imageView.setImageBitmap(BitmapFactory.decodeResource(picViewPager.resources, temp.toInt()))
            }

            viewList.add(view)
        }

        picViewPager.adapter = object : PagerAdapter() {
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view == `object`
            }

            override fun getCount(): Int = viewList.size


            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(viewList[position])
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(viewList[position])
                return viewList[position]
            }
        }

        initRunnable(handler!!, picViewPager)

    }


    var viewpagerRunnable: Runnable? = null


    private val TIME: Int = 5 * 1000

    /**
     * 定时轮播
     */
    private fun initRunnable(handler: Handler, viewPager: ViewPager) {
        viewpagerRunnable = Runnable {
            val nowIndex = viewPager.currentItem;
            val count = viewPager.adapter!!.count;

            /// 如果下一张的索引大于最后一张，则切换到第一张
            if (nowIndex + 1 >= count) {
                viewPager.currentItem = 0

            } else {
                viewPager.currentItem = nowIndex + 1;

            }

            handler.postDelayed(viewpagerRunnable, TIME.toLong())
        }

        handler.postDelayed(viewpagerRunnable, TIME.toLong())
    }

    /**
     * 保留两位小数
     */
    fun floatToString(ff: Float): String {
        val format: DecimalFormat = DecimalFormat("#0.00")
        return format.format(ff)
    }


    /**
     * 保留两位小数
     */
    fun stringToString(str: String): String {
        val format: DecimalFormat = DecimalFormat("#0.00")
        return format.format(BigDecimal(str))
    }


    companion object {

        class ProHandler(proActivity: ProActivity) : Handler(Looper.getMainLooper()) {
            val weak: WeakReference<ProActivity> = WeakReference(proActivity)
            val a = weak.get()
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg!!.what) {
                    a!!.ERROR -> {
                        a.removeLoadDialog()
                        a.showToastShort("网络连接超时，请检查网络!")
                    }
                    a.ERROR_BUG -> {
                        a.removeLoadDialog()
                        a.showToastShort("提交失败，请稍后再试")
                    }

                    else -> {
                        val json = JSONObject(msg.obj as String)
                        val message = json.optString("msg")

                        if (message.contains("请登录")) {

                            Share.clearUser(a)

                            val intent = Intent(a, LoginActivity::class.java)
                            a.startActivity(intent)

                            if (a is MainActivity) {
                                a.finish()
                            }

                        } else {
                            a.handler(msg)
                        }
                    }
                }

            }
        }
    }


    fun openApp() {
        val i: Intent = packageManager.getLaunchIntentForPackage(packageName)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
    }


    fun switchContent(
            indexFragment: ProV4Fragment,
            to: ProV4Fragment,
            resId: Int,
            transaction: FragmentTransaction): ProV4Fragment {

        // 当前显示的不是待显示的fragment
        if (indexFragment !== to) {

            if (!to.isAdded) {      // 先判断是否被add过
                // 没有add 影藏当前正在显示的，加载并提交
                transaction.hide(indexFragment).add(resId, to, to.javaClass.name).commit()   // 隐藏当前的fragment，add下一个到Activity中

            } else {
                // 影藏当前正在显示的，显示需要显示的
                transaction.hide(indexFragment).replace(resId, to, to.javaClass.name).commit()     // 隐藏当前的fragment，显示下一个
            }
        } else {
            if (to.isAdded) {
                transaction.hide(indexFragment).replace(resId, to, to.javaClass.name).commit()
            } else {
                transaction.add(resId, to, to.javaClass.name).commit()
            }

        }

        return to

    }

}