@file:Suppress("UNREACHABLE_CODE")

package com.wt.yc.englishread.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import com.nineoldandroids.animation.ValueAnimator
import com.wt.yc.englishread.R
import kotlin.math.PI
import android.graphics.DashPathEffect


class CustomRoundImageView(context: Context, attr: AttributeSet) : ImageView(context, attr) {

    private var valueAnimator: ValueAnimator? = null

    /**
     * view宽的中心点(可以暂时理解为圆心)
     */
    private var mViewCenterX: Int = 0
    /**
     *  view高的中心点(可以暂时理解为圆心)
     */
    private var mViewCenterY = 0

    /**
     * 最里面白色圆的半径
     */
    private var mMinRadio = 40

    /**
     * 圆环的宽度
     */
    private var mRingWidth = 2f
    /**
     * 最里面圆的颜色
     */
    private var mMinCircleColor = 0

    /**
     * 默认圆环的颜色
     */
    private var mRingNormalColor = 0

    /**
     * 外层圆环颜色
     */
    private var circleColor = 0

    private var mPaint: Paint? = null

    /**
     * 渐变颜色
     */
    private var color: IntArray = IntArray(3)

    /**
     * 圆环的矩形区域
     */
    private var mRectF: RectF? = null

    /**
     * 要显示的彩色区域(岁数值变化)
     */
    private var mSelectRing = 0

    private var mMaxValue = 100

    /**
     * 当前进度  最大100
     */
    var values = 0

    init {

        initView(attr)

    }


    private fun initView(attr: AttributeSet) {
        val a: TypedArray = context.obtainStyledAttributes(attr, R.styleable.SuperCircleView)
        //最里面白色圆的半径
        mMinRadio = a.getInteger(R.styleable.SuperCircleView_min_circle_radio, 40)
        //圆环宽度
        mRingWidth = a.getFloat(R.styleable.SuperCircleView_ring_width, 4f)

        //最里面的圆的颜色
        mMinCircleColor = a.getColor(R.styleable.SuperCircleView_circle_color, context.resources.getColor(R.color.write))
        //圆环的默认颜色(圆环占据的是里面的圆的空间)
        mRingNormalColor = a.getColor(R.styleable.SuperCircleView_ring_normal_color, context.resources.getColor(R.color.gray))
        //圆环要显示的彩色的区域
        mSelectRing = a.getInt(R.styleable.SuperCircleView_ring_color_select, 50)

        circleColor = a.getColor(R.styleable.SuperCircleView_max_circle_color, 0)

        mMaxValue = a.getInt(R.styleable.SuperCircleView_maxValue, 100)
        values = a.getInt(R.styleable.SuperCircleView_value, 0)


        a.recycle()

        //抗锯齿画笔
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

        mPaint!!.color = circleColor
        //防止边缘锯齿
        mPaint!!.isAntiAlias = true

        //需要重写onDraw就得调用此
        this.setWillNotDraw(false)

        // 圆环渐变的颜色
        color[0] = circleColor
        color[1] = context.resources.getColor(R.color.red)
        color[2] = context.resources.getColor(R.color.red)

    }


    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        //  view的宽和高,相对于父布局(用于确定圆心)
        val viewWidth = measuredWidth
        val viewHeight = measuredHeight
        mViewCenterX = viewWidth / 2
        mViewCenterY = viewHeight / 2

        val leftNum = mViewCenterX - mMinRadio - mRingWidth / 2
        val topNum = mViewCenterY - mMinRadio - mRingWidth / 2
        val rightNum = mViewCenterX + mMinRadio + mRingWidth / 2
        val bottomNum = mViewCenterY + mMinRadio + mRingWidth / 2

        // 画矩形
        mRectF = RectF(leftNum, topNum, rightNum, bottomNum)

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPaint!!.color = mMinCircleColor
        mPaint!!.strokeCap = Paint.Cap.ROUND

        // 画默认圆环
        drawNormalRing(canvas!!)

        // 画彩色圆环
        drawColorRing(canvas)

    }


    /**
     * 画默认圆环
     * @param canvas
     */
    private fun drawNormalRing(canvas: Canvas) {
        val ringNormalPaint = Paint(mPaint)
        ringNormalPaint.style = Paint.Style.STROKE
        ringNormalPaint.strokeWidth = 2f
        ringNormalPaint.color = mRingNormalColor     // 圆环默认颜色为灰色

        val dashPathEffect = DashPathEffect(floatArrayOf(5f, 5f), 1f)
        ringNormalPaint.pathEffect = dashPathEffect

        canvas.drawArc(mRectF, 360f, 360f, false, ringNormalPaint)
    }


    /**
     * 画彩色圆环
     * @param canvas
     */
    private fun drawColorRing(canvas: Canvas) {
        val ringColorPaint = Paint(mPaint)
        ringColorPaint.style = Paint.Style.STROKE
        ringColorPaint.strokeWidth = mRingWidth
        ringColorPaint.shader = SweepGradient(this.mViewCenterX.toFloat(), this.mViewCenterX.toFloat(), this.color, null)

        /// 逆时针旋转90度
        canvas.rotate(-90f, this.mViewCenterX.toFloat(), this.mViewCenterY.toFloat())

        /// 度数
        val v = values * 360 / 100

        canvas.drawArc(this.mRectF, 360f, v.toFloat(), false, ringColorPaint)
        ringColorPaint.shader = null


        mPaint!!.color = circleColor

        Log.i("result", "---角度-----$v------半径----$mMinRadio")

        val cos = Math.cos(PI * v / 180) * mMinRadio
        val sin = Math.sin(PI * v / 180) * mMinRadio

        Log.i("result", "---cos-----$cos------sin----$sin")

        val x = Math.abs(cos.toInt())
        val y = Math.abs(sin.toInt())

        Log.i("result", "---xxxx====$x-----yyyy=====$y")


        var dx = 0f
        var dy = 0f

        when (v) {
            in 0..90 -> {
                dx = (mViewCenterX + x).toFloat()
                dy = (mViewCenterY + y).toFloat()
            }

            in 91..180 -> {
                dx = (mViewCenterX - x).toFloat()
                dy = (mViewCenterY + y).toFloat()
            }
            in 181..270 -> {
                dx = (mViewCenterX - x).toFloat()
                dy = (mViewCenterY - y).toFloat()
            }
            in 270..359 -> {
                dx = (mViewCenterX + x).toFloat()
                dy = (mViewCenterY - y).toFloat()
            }
        }

        Log.i("result", "---ddddxxxx====$dx-----ddddyyyyy=====$dy")
        Log.i("result", "---mmmmm====$mViewCenterX-----mmmmmm=====$mViewCenterY")

        canvas.drawCircle(dx, dy, 6f, mPaint)

        setValue(values)

    }


    fun setValue(value: Int) {
        this.values = value

        startAnimator(0, value, 2000)

    }


    private fun startAnimator(start: Int, end: Int, animTime: Long) {
        valueAnimator = ValueAnimator.ofInt(start, end)
        valueAnimator!!.duration = animTime

        valueAnimator!!.addUpdateListener { animation ->
            val i = animation!!.animatedValue.toString().toInt()

            mSelectRing = (360 * (i / 100f)).toInt()

            invalidate()
        }

        valueAnimator!!.start()
    }


}