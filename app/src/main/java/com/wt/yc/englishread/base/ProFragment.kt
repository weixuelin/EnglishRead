package com.wt.yc.englishread.base

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.gson.Gson
import com.xin.lv.yang.utils.view.CustomProgressDialog

abstract class ProFragment : Fragment() {
    open lateinit var gson: Gson
    var unbinder: Unbinder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gson = Gson()

    }

    abstract fun createView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = createView(inflater, container, savedInstanceState!!)
        unbinder = ButterKnife.bind(this, view)

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        unbinder!!.unbind()
    }


    fun showShortToast(activity: Activity, str: String, time: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(activity, str, time).show()
    }


    fun setAlpha(activity: Activity, f: Float) {
        val manager = activity.window.attributes
        manager.alpha = f
        activity.window.attributes = manager
    }

    fun showToastShort(context: Context, str: String) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
    }

    var customProgressDialog: CustomProgressDialog? = null

    fun showLoadDialog(context: Context, string: String = "加载中") {
        if (customProgressDialog == null) {
            customProgressDialog = CustomProgressDialog(context)
        }
        customProgressDialog!!.setText(string)
        customProgressDialog!!.show()

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
}