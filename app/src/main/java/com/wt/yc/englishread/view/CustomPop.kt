package com.wt.yc.englishread.view

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.support.v4.app.ActivityCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.wt.yc.englishread.R

/**
 * 自定义底部  相机  相册 选择
 */
class CustomPop(val context: Context) : PopupWindow(context) {
    var view: View = LayoutInflater.from(context).inflate(R.layout.choose_photo_layout, null)

    var tvCamera: TextView
    var tvPhoto: TextView
    var tvClose: TextView

    companion object {
        val CAMERA_CODE: Int = 333
        val PHOTO_CODE: Int = 444

        val CAMERA_PERMISSION_CODE: Int = 555
        val PHOTO_PERMISSION_CODE: Int = 666
    }


    init {
        tvCamera = view.findViewById(R.id.tv_camera) as TextView
        tvPhoto = view.findViewById(R.id.tv_photo) as TextView
        tvClose = view.findViewById(R.id.tv_close) as TextView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        animationStyle = R.style.pop_anima
        setBackgroundDrawable(BitmapDrawable())
        isOutsideTouchable = true
        isFocusable = true
        setOnDismissListener {
            if (click != null) {
                click!!.onClose()
            }

        }

    }

    var click: OnCheckListener? = null

    fun show(click: OnCheckListener) {
        contentView = view

        this.click = click

        showAtLocation(contentView, Gravity.BOTTOM, 0, 0)

        tvCamera.setOnClickListener {
            click.onCamera()
        }

        tvPhoto.setOnClickListener {
            click.onPhoto()
        }

        tvClose.setOnClickListener {
            click.onClose()
        }
    }


    /**
     * 检查数据存储权限
     */
    fun checkPhotoPremission(): Boolean {
        val code = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        return code == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查照相机权限
     */
    fun checkCameraPremission(): Boolean {
        val code = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        return code == PackageManager.PERMISSION_GRANTED
    }

    interface OnCheckListener {
        fun onCamera()
        fun onPhoto()
        fun onClose()
    }

}