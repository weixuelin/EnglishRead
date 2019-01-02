package com.xin.lv.yang.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("AppCompatCustomView")
public class AdTextView extends TextView {

    private int mDuration; //文字从出现到显示消失的时间
    private int mInterval; //文字停留在中间的时长切换的间隔
    private List<Info> mTexts; //显示文字的数据源
    private int mY = 10;      //文字的Y坐标
    private int mIndex = 0;   // 当前的数据下标
    private Paint paint;     //绘制内容的画笔

    private boolean isMove = true;   //文字是否移动
    private String TAG = "ADTextView";

    private boolean hasInit = false;  //是否初始化刚进入时候文字的纵坐标

    private int showTextNum = 10;   // 每行显示字个数,默认为10个字

    public int getShowTextNum() {
        return showTextNum;
    }

    public void setShowTextNum(int showTextNum) {
        this.showTextNum = showTextNum;
    }

    public interface onClickListener {
        void onClick(int id);
    }

    private onClickListener onClickListener;

    public void setOnClickLitener(AdTextView.onClickListener onClickLitener) {
        this.onClickListener = onClickLitener;
    }

    Context context;

    public AdTextView(Context context) {
        this(context, null);
    }

    public AdTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    // 重写onTouchEvent事件,并且要返回true,表明当前的点击事件由这个组件自身来处理

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 调用回调,将当前数据源的链接传出去
                if (onClickListener != null) {
                    onClickListener.onClick(mTexts.get(mIndex).getId());
                }

                break;
        }
        return true;
    }


    public float size = 14f;

    //设置数据源
    public void setmTexts(List<Info> mTexts) {
        this.mTexts = mTexts;
    }

    public void setMTextSize(float size) {
        this.size = size;
    }

    //设置广告文字的停顿时间
    public void setmInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    //设置文字从出现到消失的时长
    public void setmDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    //设置前缀的文字颜色
    public void setFrontColor(int mFrontColor) {

    }

    //设置正文内容的颜色
    public void setBackColor(int mBackColor) {
        paint.setColor(mBackColor);
    }

    // 初始化默认值
    private void init() {
        mDuration = 500;
        mInterval = 1500;
        mIndex = 0;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setTextSize(sp2px(context, size));

    }


    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }


    int moreLen = 0;

    @Override
    protected void onDraw(Canvas canvas) {
        if (mTexts != null && mTexts.size() > 0) {

            //获取当前的数据
            Info model = mTexts.get(mIndex);

            String title = model.getTitle();

            // 绘制内容的外框
            Rect contentBound = new Rect();

            paint.getTextBounds(title, 0, title.length(), contentBound);

            // 刚开始进入的时候文字应该是位于组件的底部的 ,但是这个值是需要获取组件的高度和当前显示文字的情况下来判断的,
            // 所以应该放在onDraw内来初始化这个值,所以需要前面的是否初始化的属性,判断当mY==0并且未初始化的时候给mY赋值.
            if (mY == 0 && !hasInit) {
                mY = getMeasuredHeight() - contentBound.top;
                hasInit = true;
            }

            // 移动到最上面
            if (mY == 0 - contentBound.bottom) {
                mY = getMeasuredHeight() - contentBound.top;     //  返回底部
                mIndex++;    // 换下一组数据
            }

            int textLen = title.length();

            if (textLen <= showTextNum) {
                canvas.drawText(title, 0, textLen, contentBound.left + 10, mY, paint);
            } else {
                canvas.drawText(title, 0, showTextNum, contentBound.left + 10, mY, paint);
                moreLen = (int) (2 * size + 8);
                canvas.drawText(title, showTextNum + 1, textLen, contentBound.left + 10, mY + moreLen, paint);
            }

            // 移动到中间
            if (mY == getMeasuredHeight() / 2 - (contentBound.top + contentBound.bottom) / 2 - moreLen / 2) {
                isMove = false;  // 停止移动
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        postInvalidate();  // 通知重绘
                        isMove = true;    //设置移动为true
                    }
                }, mInterval);           // 停顿多少毫秒之后再次移动
            }

            // 移动的处理与数据源的处理
            mY -= 1;    // 每次只移动一个像素,尽量保证平滑显示
            //循环使用数据
            if (mIndex == mTexts.size()) {
                mIndex = 0;
            }
            // 如果是处于移动状态时的,则延迟绘制
            // 计算公式为一个比例,一个时间间隔移动组件高度,则多少毫秒来移动1像素
            if (isMove) {
                postInvalidateDelayed(mDuration / getMeasuredHeight());
            }
        }

    }

    public static class Info {
        int id;
        String title;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
