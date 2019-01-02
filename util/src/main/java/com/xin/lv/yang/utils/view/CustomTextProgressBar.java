package com.xin.lv.yang.utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import com.xin.lv.yang.utils.utils.BitmapUtil;

public class CustomTextProgressBar extends ProgressBar {

    Context mContext;

    int styleDrawable;

    public void setStyleDrawable(int styleDrawable) {
        this.styleDrawable = styleDrawable;
    }

    public void setOnlyText(boolean onlyText) {
        this.onlyText = onlyText;
    }

    public CustomTextProgressBar(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public CustomTextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CustomTextProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    private void init() {

        setIndeterminate(false);
        setMax(100);

        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(BitmapUtil.sp2px(mContext, TEXT_SIZE_SP));
        mPaint.setTypeface(Typeface.MONOSPACE);
        mPaint.setColor(mContext.getResources().getColor(android.R.color.holo_red_light));

        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIconAndText(canvas);

    }

    Paint mPaint;
    Bitmap bitmap;
    String text="50";


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
    }

    boolean onlyText = false;
    int mProgress = 0;
    PorterDuffXfermode mPorterDuffXfermode;

    // IconTextProgressBar的文字大小(sp)
    private static final float TEXT_SIZE_SP = 17f;
    // IconTextProgressBar的图标与文字间距(dp)
    private static final float ICON_TEXT_SPACING_DP = 5f;

    private void drawIconAndText(Canvas canvas) {
        String text = getText();
        Rect textRect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textRect);

        if (onlyText) {
            // 仅绘制文字
            float textX = (getWidth() / 2) - textRect.centerX();
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(text, textX, textY, mPaint);
        } else {
            // 绘制图标和文字
            Bitmap icon = getIcon();

            float textX = (getWidth() / 2) - getOffsetX(icon.getWidth(), textRect.centerX(), ICON_TEXT_SPACING_DP, true);
            float textY = (getHeight() / 2) - textRect.centerY();
            canvas.drawText(text, textX, textY, mPaint);

            float iconX = (getWidth() / 2) - icon.getWidth() - getOffsetX(icon.getWidth(), textRect.centerX(), ICON_TEXT_SPACING_DP, false);
            float iconY = (getHeight() / 2) - icon.getHeight() / 2;
            canvas.drawBitmap(icon, iconX, iconY, mPaint);

            Bitmap bufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas bufferCanvas = new Canvas(bufferBitmap);
            bufferCanvas.drawBitmap(icon, iconX, iconY, mPaint);
            bufferCanvas.drawText(text, textX, textY, mPaint);
            // 设置混合模式
            mPaint.setXfermode(mPorterDuffXfermode);
            mPaint.setColor(Color.WHITE);
            RectF rectF = new RectF(0, 0, getWidth() * mProgress / 100, getHeight());
            // 绘制源图形
            bufferCanvas.drawRect(rectF, mPaint);
            // 绘制目标图
            canvas.drawBitmap(bufferBitmap, 0, 0, null);
            // 清除混合模式
            mPaint.setXfermode(mPorterDuffXfermode);

            if (!icon.isRecycled()) {
                icon.isRecycled();
            }
            if (!bufferBitmap.isRecycled()) {
                bufferBitmap.recycle();
            }
        }
    }

    private String getText() {
        return text;
    }


    private float getOffsetX(float iconWidth, float textHalfWidth, float spacing, boolean isText) {
        float totalWidth = iconWidth + BitmapUtil.dip2px(mContext, spacing) + textHalfWidth * 2;
        // 文字偏移量
        if (isText) return totalWidth / 2 - iconWidth - spacing;
        // 图标偏移量
        return totalWidth / 2 - iconWidth;
    }


    private Bitmap getIcon() {
        return bitmap;
    }
}
